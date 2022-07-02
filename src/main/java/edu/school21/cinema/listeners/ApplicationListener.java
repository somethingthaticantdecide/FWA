package edu.school21.cinema.listeners;

import edu.school21.cinema.config.SpringConfig;
import edu.school21.cinema.models.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        ctx.setAttribute("springContext", context);
    }
}
