package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CommentDto;
import service.CommentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "CommentServlet", urlPatterns = {
        "/comment/get",
        "/comment/create",
        "/comment/edit",
        "/comment/remove",
        "/comment/all",
        "/comment/allforpost",
        "/comment/allforuser"
})
public class CommentServlet extends HttpServlet {
    private static final Gson gson = new GsonBuilder().create();
    private static final CommentService commentService = new CommentService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String userPath = request.getServletPath();
        switch (userPath) {
            case "/comment/get": {
                Integer id = Optional.ofNullable(request.getParameter("id")).map(Integer::valueOf).orElse(0);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(commentService.getComment(id)));
                return;
            }
            case "/comment/allforpost": {
                Integer id = Optional.ofNullable(request.getParameter("id")).map(Integer::valueOf).orElse(0);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(commentService.findByPost(id)));
                return;
            }
            case "/comment/allforuser": {
                Integer id = Optional.ofNullable(request.getParameter("id")).map(Integer::valueOf).orElse(0);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(commentService.findByUser(id)));
                return;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Установка кодировки при чтении данных из запроса
        response.setCharacterEncoding("UTF-8");
        String userPath = request.getServletPath();
        switch (userPath) {
            case "/comment/create": {
                String body = getBody(request);
                CommentDto commentDto = gson.fromJson(body, CommentDto.class);
                commentService.creatComment(commentDto);
                response.getWriter().write("done");
                return;
            }
            case "/comment/edit": {
                String body = getBody(request);
                CommentDto commentDto = gson.fromJson(body, CommentDto.class);
                commentService.editeComment(commentDto);
                response.getWriter().write("done");
                return;
            }
            case "/comment/remove": {
                String body = getBody(request);
                CommentDto commentDto = gson.fromJson(body, CommentDto.class);
                commentService.removeComment(commentDto);
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
