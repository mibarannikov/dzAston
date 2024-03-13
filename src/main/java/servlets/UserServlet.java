package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.UserDto;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "UserServlet", urlPatterns = {
        "/user/get",
        "/user/create",
        "/user/edit",
        "/user/remove",
        "/user/all"})
public class UserServlet extends HttpServlet {
    private static final Gson gson = new GsonBuilder().create();
    private static final UserService userService = new UserService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String userPath = request.getServletPath();
        switch (userPath) {
            case "/user/get": {
                Integer id = Optional.ofNullable(request.getParameter("id")).map(Integer::valueOf).orElse(0);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(userService.getUser(id)));
                return;
            }
            case "/user/all": {
                Integer id = Optional.ofNullable(request.getParameter("id")).map(Integer::valueOf).orElse(0);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(userService.getAllUser()));
                return;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Установка кодировки при чтении данных из запроса
        response.setCharacterEncoding("UTF-8");
        String userPath = request.getServletPath();
        switch (userPath) {
            case "/user/create": {
                String body = getBody(request);
                UserDto userDto = gson.fromJson(body, UserDto.class);
                userService.creatUser(userDto);
                response.getWriter().write("done");
                return;
            }
            case "/user/edit": {
                String body = getBody(request);
                UserDto userDto = gson.fromJson(body, UserDto.class);
                userService.editeUser(userDto);
                response.getWriter().write("done");
                return;
            }
            case "/user/remove": {
                String body = getBody(request);
                UserDto userDto = gson.fromJson(body, UserDto.class);
                userService.removeUser(userDto);
                response.getWriter().write("done");
                return;
            }
        }
    }

    private String getBody(HttpServletRequest request) {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Теперь requestBody содержит данные из тела запроса
        String requestData = requestBody.toString();
        return requestData;
    }
}
