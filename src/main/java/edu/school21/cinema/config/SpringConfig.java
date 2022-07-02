package edu.school21.cinema.config;

import edu.school21.cinema.repositories.UserDaoImpl;
import edu.school21.cinema.services.UserService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@ComponentScan("edu.school21.cinema")
@PropertySource("classpath:../application.properties")
public class SpringConfig {

    private final Environment environment;

    private final String URL = "url";
    private final String USER = "user";
    private final String DRIVER = "driver";
    private final String PASSWORD = "password";
    private final String FILES_UPLOAD_PATH = "upload";

    public SpringConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty(URL));
        driverManagerDataSource.setUsername(environment.getProperty(USER));
        driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
        driverManagerDataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty(DRIVER)));
        return driverManagerDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public UserService userService(UserDaoImpl userDao) {
        return new UserService(userDao, passwordEncoder(), environment.getProperty(FILES_UPLOAD_PATH));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public String getFilesUploadPath() {
        return environment.getProperty(FILES_UPLOAD_PATH);
    }

}
