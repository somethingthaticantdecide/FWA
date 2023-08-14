package edu.school21.cinema.servlets;

import edu.school21.cinema.services.UserService;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.ZonedDateTime;

@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {
    public SignInServlet(){}

    private UserService userService;

    @Override
    public void init(ServletConfig config ) {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        this.userService = springContext.getBean(UserService.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (userService.authorize(username, password)) {
            userService.addSessionStart(userService.find(username).getId(), ZonedDateTime.now().toLocalDate().toString(), ZonedDateTime.now().toLocalTime().toString(), request.getRemoteAddr());
            request.getSession().setAttribute("username", request.getParameter("username"));
            response.sendRedirect("/profile");
        } else {
            response.sendRedirect("/signIn");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/html/signIn.html").forward(request, response);
    }
}