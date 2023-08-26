import edu.school21.cinema.models.User;

import edu.school21.cinema.services.UserService;
import edu.school21.cinema.servlets.SignUpServlet;
import org.apache.struts.mock.MockHttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SignUpServletTest {
    private SignUpServlet signUpServlet;
    @Mock
    private UserService userService;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private ApplicationContext springContext;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;

    @Before
    public void setUp() {
        signUpServlet = new SignUpServlet();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("springContext")).thenReturn(springContext);
        when(springContext.getBean(UserService.class)).thenReturn(userService);
        signUpServlet.init(servletConfig);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        // Arrange
        String firstName = "testUser";
        String lastName = "testUser";
        String password = "testPassword";
        String phoneNumber = "testPhoneNumber";
        String ipAddress = "127.0.0.1";
        long userId = 1;

        when(request.getParameter("firstName")).thenReturn(firstName);
        when(request.getParameter("lastName")).thenReturn(lastName);
        when(request.getParameter("phoneNumber")).thenReturn(phoneNumber);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getRemoteAddr()).thenReturn(ipAddress);

        when(request.getSession()).thenReturn(new MockHttpSession());
        when(request.getRequestDispatcher("/WEB-INF/html/signUp.html")).thenReturn(requestDispatcher);

        when(userService.find(firstName)).thenReturn(new User(userId, firstName, lastName, password, phoneNumber));

        // Act
        signUpServlet.doPost(request, response);

        // Assert
        ZonedDateTime now = ZonedDateTime.now();
        String expectedTime = now.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        String expectedDate = now.toLocalDate().toString();

        verify(userService).signUp(firstName, lastName, phoneNumber, password);
        verify(userService).addSessionStart(any(), eq(expectedDate), eq(expectedTime), eq(ipAddress));
        verify(response).sendRedirect("/profile");
        verify(userService, times(2)).find(firstName);
    }

    @Test
    public void testDoPost_existingUser() throws ServletException, IOException {
        // Arrange
        String firstName = "testUser";
        String lastName = "testUser";
        String password = "testPassword";
        String phoneNumber = "testPhoneNumber";
        long userId = 1;

        when(request.getParameter("firstName")).thenReturn(firstName);
        when(userService.find(firstName)).thenReturn(new User(userId, firstName, lastName, password, phoneNumber));
        when(request.getRequestDispatcher("/WEB-INF/html/signUp.html")).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(new MockHttpSession());

        // Act
        signUpServlet.doPost(request, response);

        // Assert
        verify(request).getRequestDispatcher("/WEB-INF/html/signUp.html");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        // Arrange
        when(request.getRequestDispatcher("/WEB-INF/html/signUp.html")).thenReturn(requestDispatcher);

        // Act
        signUpServlet.doGet(request, response);

        // Assert
        verify(request).getRequestDispatcher("/WEB-INF/html/signUp.html");
        verify(requestDispatcher).forward(request, response);
    }
}