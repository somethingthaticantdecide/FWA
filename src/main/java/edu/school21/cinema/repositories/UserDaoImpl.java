package edu.school21.cinema.repositories;

import edu.school21.cinema.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class UserDaoImpl implements UserDAO {

    private final String SQL_FIND_PERSON_BY_NAME = "SELECT * FROM users WHERE first_name = ?";
    private final String SQL_INSERT_PERSON = "INSERT INTO users(first_name, last_name, phone, password) VALUES (?, ?, ?, ?)";
    private final String SQL_INSERT_SESSION = "INSERT INTO sessions(user_id, date, time, ip) VALUES (?, ?, ?, ?)";
    private final String SQL_FILE_UPLOAD = "INSERT INTO images(user_id, filename, size, mime) VALUES (?, ?, ?, ?)";
    private final String SQL_GET_USER_SESSIONS = "SELECT * FROM sessions WHERE user_id = ?";
    private final String SQL_GET_USER_IMAGES = "SELECT * FROM images WHERE user_id = ?";

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(String firstName, String lastName, String phone, String password) {
        jdbcTemplate.update(SQL_INSERT_PERSON, firstName, lastName, phone, password);
    }

    @Override
    public User find(String username) {
        return jdbcTemplate.query(SQL_FIND_PERSON_BY_NAME, new BeanPropertyRowMapper<>(User.class), username)
                .stream().findAny().orElse(null);
    }

    @Override
    public void addSessionStart(Long user_id, String date, String time, String ip) {
        jdbcTemplate.update(SQL_INSERT_SESSION, user_id, date, time, ip);
    }

    @Override
    public List<Session> getUserSessions(Long user_id) {
        return jdbcTemplate.query(SQL_GET_USER_SESSIONS, new BeanPropertyRowMapper<>(Session.class), String.valueOf(user_id));
    }

    @Override
    public List<Image> getUserImages(String username) {
        return jdbcTemplate.query(SQL_GET_USER_IMAGES, new BeanPropertyRowMapper<>(Image.class), username);
    }

    @Override
    public void addFileToUser(String username, String fileName, String length, String probeContentType) {
        jdbcTemplate.update(SQL_FILE_UPLOAD, username, fileName, length, probeContentType);
    }
}
