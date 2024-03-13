package servlets;

import entity.Comment;
import entity.Post;
import entity.User;
import repository.CommentRepository;
import repository.PostRepository;
import repository.UserRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "HelloWorldServlet", urlPatterns = {"/hello"})
public class HelloWorldServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userPath = request.getServletPath();
        UserRepository userRep = new UserRepository();
        PostRepository postRep = new PostRepository();
        CommentRepository commRep = new CommentRepository();
        User user = userRep.find(1);
        System.out.println("user" + user);
//        Post post = postRep.find(1);
//        System.out.println("post" + post);
        Post post = new Post();
        post.setText("dfdsfdsfsf");
        post.setUser(userRep.find(1));
        Post post2 = new Post();
        User user2 = new User();
        user2.setName("Петька");
        user2.setUsername("superPetka");
        post2.setText("это важный пост");
        post2.setUser(user2);
        postRep.create(post2);
        User user3 = new User(null, "first", "первый комментирующий");
        Comment comment = new Comment(null, "так себе пост", postRep.find(2), user3);
        commRep.create(comment);
        User user1 = new User();
        user1.setName("fffffff");
        user1.setUsername("eeeeeeee");
        postRep.create(post);

        User user4 = new User(3, "ferst but", "первый но его отредактировали");
        userRep.edit(user4);
        response.setContentType("text/html;charset=UTF-8");
        List<User> users = userRep.findAll();
        //        userRep.remove(userRep.find(2));
        switch (userPath) {
            case "/hello": {

                PrintWriter out = response.getWriter();
                out.println("<html>");
                out.println("<head><title>Hello World Servlet</title></head>");
                out.println("<body>");
//                out.println("<h1>" + user.getId() + " " + user.getUsername() + "</h1>");
//                out.println("<h1>" + post.getId() + " " + post.getText() + "</h1>");
//                out.println("<h1>" + post.getUser().getId() + " " + post.getUser().getUsername() + "</h1>");
                users.forEach(u -> out.println("<h1>" + u.getId() + " " + u.getUsername() + " " + u.getName() + "</h1>"));
                postRep.findAllByUser(userRep.find(1)).forEach(p -> out.println("<h1>" + p.getId() + " " + p.getText() + " " + p.getUser().getName() + "</h1>"));
                out.println("</body>");
                out.println("</html>");
                break;
            }
        }
    }
}

