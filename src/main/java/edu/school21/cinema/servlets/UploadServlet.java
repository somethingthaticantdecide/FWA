package edu.school21.cinema.servlets;

import edu.school21.cinema.services.UserService;
import org.springframework.context.ApplicationContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet(name = "UploadServlet", urlPatterns = {"/upload"})
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB
        maxFileSize=1024*1024*50,      	            // 50 MB
        maxRequestSize=1024*1024*100)   	        // 100 MB
public class UploadServlet extends HttpServlet {

    public UploadServlet(){}

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
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        Part filePart = request.getPart("file");
        String fileName = getFileName(filePart);
        File file = new File(userService.getFilesUploadPath() + File.separator + username);

        file.mkdirs();
        try (OutputStream out = new FileOutputStream(file.getAbsolutePath() + File.separator + fileName);
             InputStream fileContent = filePart.getInputStream()) {
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = fileContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        }
        Path path = new File(file.getAbsolutePath() + File.separator + fileName).toPath();
        userService.addFileToUser(username, fileName, String.format("%,d KB", Files.size(path) / 1024), Files.probeContentType(path));
        response.sendRedirect("/profile");
    }
}