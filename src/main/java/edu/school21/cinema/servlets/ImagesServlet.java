package edu.school21.cinema.servlets;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@WebServlet(name = "ImagesServlet", urlPatterns = {"/images/*"})
@MultipartConfig
public class ImagesServlet extends HttpServlet {

    public ImagesServlet(){}

    private String pathToPicture;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        pathToPicture = (String) springContext.getBean("getFilesUploadPath");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/jsp");

        String username = (String) request.getSession().getAttribute("username");

        byte[] fileContent = FileUtils.readFileToByteArray(new File(pathToPicture + File.separator + username + request.getPathInfo()));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        request.getSession().setAttribute("picture", encodedString);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/showPicture.jsp");
        dispatcher.forward(request, response);
    }
}