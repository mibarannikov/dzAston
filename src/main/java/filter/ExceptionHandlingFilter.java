package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "ExceptionHandlingFilter", servletNames = {"CommentServlet", "UserServlet", "PostServlet"})
public class ExceptionHandlingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // Вызов следующего фильтра в цепочке или сервлета, который будет обрабатывать запрос
            chain.doFilter(request, response);
        } catch (Exception e) {
            // Обработка исключения: формирование ответа с сообщением об ошибке
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.getWriter().println("Произошла ошибка: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {

    }


}
