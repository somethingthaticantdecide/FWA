package edu.school21.cinema.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@WebFilter(urlPatterns =  {"/", "/signIn", "/signUp"})
public class SignInUpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        // Получение имени пользователя из сессии
        String username = (String) session.getAttribute("username");

        // Получение URI запроса
        String uri = request.getRequestURI();

        // Проверка авторизации пользователя и URI запроса
        if (username == null && uri.equalsIgnoreCase("/")) {
            // Перенаправление на страницу входа
            response.sendRedirect("/signIn");
        } else if (username != null && (uri.equalsIgnoreCase("/signIn") || uri.equalsIgnoreCase("/signUp") || uri.equalsIgnoreCase("/"))) {
            // Перенаправление на профиль пользователя
            response.sendRedirect("/profile");
        } else {
            // Продолжение выполнения цепочки фильтров
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}