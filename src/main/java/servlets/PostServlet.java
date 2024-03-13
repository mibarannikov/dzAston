package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PostDto;
import service.PostService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "PostServlet", urlPatterns = {
        "/post/get",
        "/post/create",
        "/post/edit",
        "/post/remove",
        "/post/all",
        "/post/allforuser"})
public class PostServlet extends HttpServlet {

    private static final Gson gson = new GsonBuilder().create();
    private static final PostService postService = new PostService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String userPath = request.getServletPath();
        switch (userPath) {
            case "/post/get": {
                Integer id = Optional.ofNullable(request.getParameter("id")).map(Integer::valueOf).orElse(0);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(postService.getPost(id)));
                return;
            }
            case "/post/all": {
                Integer id = Optional.ofNullable(request.getParameter("id")).map(Integer::valueOf).orElse(0);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(postService.getAllPost()));
                return;
            }
            case "/post/allforuser": {
                Integer id = Optional.ofNullable(request.getParameter("id")).map(Integer::valueOf).orElse(0);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(postService.findByUser(id)));
                return;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Установка кодировки при чтении данных из запроса
        response.setCharacterEncoding("UTF-8");
        String userPath = request.getServletPath();
        switch (userPath) {
            case "/post/create": {
                String body = getBody(request);
                PostDto postDto = gson.fromJson(body, PostDto.class);
                postService.creatPost(postDto);
                response.getWriter().write("done");
                return;
            }
            case "/post/edit": {
                String body = getBody(request);
                PostDto postDto = gson.fromJson(body, PostDto.class);
                postService.editePost(postDto);
                response.getWriter().write("done");
                return;
            }
            case "/post/remove": {
                String body = getBody(request);
                PostDto postDto = gson.fromJson(body, PostDto.class);
                postService.removePost(postDto);
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
        String requestData = requestBody.toString();
        return requestData;
    }
}
