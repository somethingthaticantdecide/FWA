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

    private UserService userService;

    @Override
    public void init (ServletConfig config) {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        this.userService = springContext.getBean(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Получение имени пользователя из атрибута сессии
        String username = (String) request.getSession().getAttribute("username");

        // Получение списка сессий пользователя и сохранение его в атрибут сессии
        List<Session> sessions = userService.getUserSessions(userService.find(username).getId());
        request.getSession().setAttribute("sessions", sessions);

        // Получение списка изображений пользователя
        List<Image> images = userService.getUserImages(username);

        String pathname;
        if (images.isEmpty()) {
            // Если у пользователя нет изображений, используется заглушка
            pathname = getClass().getClassLoader().getResource("/images/blankProfile.png").getFile();
        } else {
            // Получение последнего изображения пользователя
            String filename = images.get(images.size() - 1).getFilename();
            // Формирование пути к файлу
            pathname = userService.getFilesUploadPath() + File.separator + username + File.separator + filename;
        }
        File file = new File(pathname);
        // Кодирование файла в Base64 и сохранение в атрибут сессии
        String encodedString = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));

        // Сохранение списка изображений в атрибут сессии
        request.getSession().setAttribute("avatar", encodedString);
        request.getSession().setAttribute("images", images);

        // Перенаправление запроса на JSP страницу
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
        dispatcher.forward(request, response);
    }
}