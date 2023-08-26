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
import java.time.format.DateTimeFormatter;

@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init (ServletConfig config ) {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        this.userService = springContext.getBean(UserService.class);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (!userService.authorize(username, password)) {
            response.sendRedirect("/signIn");
            return;
        }

        String time = ZonedDateTime.now().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        String date = ZonedDateTime.now().toLocalDate().toString();
        HttpSession session = request.getSession();

        userService.addSessionStart(userService.find(username).getId(), date, time, request.getRemoteAddr());
        session.setAttribute("username", request.getParameter("username"));
        response.sendRedirect("/profile");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/html/signIn.html").forward(request, response);
    }
}