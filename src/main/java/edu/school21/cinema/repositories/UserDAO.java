package edu.school21.cinema.repositories;

import edu.school21.cinema.models.Image;
import edu.school21.cinema.models.Session;
import edu.school21.cinema.models.User;

import java.util.List;

public interface UserDAO {

    void save(String firstName, String lastName, String phone, String password);

    User find(String username);

    void addSessionStart(Long user_id, String date, String time, String ip);

    List<Session> getUserSessions(Long user_id);

    List<Image> getUserImages(String username);

    void addFileToUser(String username, String fileName, String length, String probeContentType);
}
