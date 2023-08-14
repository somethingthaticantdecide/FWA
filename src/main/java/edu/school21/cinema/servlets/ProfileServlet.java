package edu.school21.cinema.servlets;

import edu.school21.cinema.models.Image;
import edu.school21.cinema.models.Session;
import edu.school21.cinema.services.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.Base64;
import java.util.List;

@WebServlet(urlPatterns = "/profile")
@MultipartConfig
public class ProfileServlet extends HttpServlet {

    public ProfileServlet(){}

    private UserService userService;

    @Override
    public void init (ServletConfig config ) {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        this.userService = springContext.getBean(UserService.class);
    }

    private String getFileName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");

        List<Session> sessions = userService.getUserSessions(userService.find(username).getId());
        request.getSession().setAttribute("sessions", sessions);
        List<Image> images = userService.getUserImages(username);

        File file;
        if (images.isEmpty()) {
            file = new File(getClass().getClassLoader().getResource("/images/blankProfile.png").getFile());
        } else {
            file = new File(userService.getFilesUploadPath() + File.separator + username + File.separator + images.get(images.size() - 1).getFilename());
        }
        String encodedString = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
        request.getSession().setAttribute("avatar", encodedString);

        request.getSession().setAttribute("images", images);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
        dispatcher.forward(request, response);
    }

}