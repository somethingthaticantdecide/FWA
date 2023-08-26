package edu.school21.cinema.servlets;

import edu.school21.cinema.services.UserService;
import org.springframework.context.ApplicationContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init (ServletConfig config) {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        this.userService = springContext.getBean(UserService.class);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");

        if (userService.find(firstName) != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/html/signUp.html");
            dispatcher.forward(request, response);
        }

        userService.signUp(firstName, lastName, phoneNumber, password);

        String time = ZonedDateTime.now().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        String date = ZonedDateTime.now().toLocalDate().toString();
        userService.addSessionStart(userService.find(firstName).getId(), date, time, request.getRemoteAddr());

        HttpSession session = request.getSession();
        session.setAttribute("username", request.getParameter("firstName"));
        response.sendRedirect("/profile");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/html/signUp.html").forward(request, response);
    }
}