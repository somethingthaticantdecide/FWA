package edu.school21.cinema.services;

import edu.school21.cinema.models.Image;
import edu.school21.cinema.models.Session;
import edu.school21.cinema.models.User;
import edu.school21.cinema.repositories.UserDaoImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;

public class UserService {
    private final UserDaoImpl userDao;
    private final PasswordEncoder passwordEncoder;
    private final String filesUploadPath;

    public User find(String username)  {
        return userDao.find(username);
    }

    public UserService(UserDaoImpl userDao, PasswordEncoder passwordEncoder, String filesUploadPath) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.filesUploadPath = filesUploadPath;
    }

    public void signUp(String first_name, String last_name, String phone, String password)  {
        userDao.save(first_name, last_name, phone, passwordEncoder.encode(password));
    }

    public boolean authorize(String username, String password) {
        User user = userDao.find(username);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    public void addSessionStart(Long user_id, String date, String time, String ip)  {
        if (Objects.equals(ip, "0:0:0:0:0:0:0:1")) {
            ip = "localhost";
        }
        userDao.addSessionStart(user_id, date, time, ip);
    }

    public List<Session> getUserSessions(Long user_id)  {
        return userDao.getUserSessions(user_id);
    }

    public String getFilesUploadPath() {
        return filesUploadPath;
    }


    public List<Image> getUserImages(String username) {
        return userDao.getUserImages(username);
    }

    public void addFileToUser(String username, String fileName, String length, String probeContentType) {
        userDao.addFileToUser(username, fileName, length, probeContentType);
    }
}
