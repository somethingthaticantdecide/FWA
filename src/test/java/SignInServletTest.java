import edu.school21.cinema.models.User;
import edu.school21.cinema.services.UserService;
import edu.school21.cinema.servlets.SignInServlet;
import org.apache.struts.mock.MockHttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

public class SignInServletTest {
    private SignInServlet signInServlet;
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
    public void setup() {
        MockitoAnnotations.initMocks(this);

        signInServlet = new SignInServlet();
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("springContext")).thenReturn(springContext);
        when(springContext.getBean(UserService.class)).thenReturn(userService);
        signInServlet.init(servletConfig);
    }

    @Test
    public void testDoPost_WhenUserServiceAuthorizeReturnsTrue_ShouldRedirectToProfilePage() throws Exception {
        // Arrange
        String firstName = "testUser";
        String lastName = "testUser";
        String password = "testPassword";
        String phoneNumber = "testPhoneNumber";
        String ipAddress = "127.0.0.1";
        long userId = 2;

        when(userService.authorize(firstName, password)).thenReturn(true);
        when(userService.find(firstName)).thenReturn(new User(userId, firstName, lastName, password, phoneNumber));
        when(request.getParameter("username")).thenReturn(firstName);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getRemoteAddr()).thenReturn(ipAddress);
        when(request.getSession()).thenReturn(new MockHttpSession());

        // Act
        signInServlet.doPost(request, response);

        // Assert
        String time = ZonedDateTime.now().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        String date = ZonedDateTime.now().toLocalDate().toString();

        verify(userService).addSessionStart(userId, date, time, ipAddress);
        verify(response).sendRedirect("/profile");
        verify(userService).authorize(firstName, password);
        verify(userService).find(firstName);
    }

    @Test
    public void testDoPost_WhenUserServiceAuthorizeReturnsFalse_ShouldRedirectToSignInPage() throws Exception {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        when(userService.authorize(username, password)).thenReturn(false);
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);

        // Act
        signInServlet.doPost(request, response);

        // Assert
        verify(response).sendRedirect("/signIn");
        verify(userService).authorize(username, password);
        verify(userService, never()).find(username);
        verify(userService, never()).addSessionStart(anyLong(), anyString(), anyString(), anyString());
    }

    @Test
    public void testDoGet_ShouldForwardToSignInPage() throws Exception {
        // Arrange
        when(request.getRequestDispatcher("/WEB-INF/html/signIn.html")).thenReturn(requestDispatcher);

        // Act
        signInServlet.doGet(request, response);

        // Assert
        verify(requestDispatcher).forward(request, response);
    }
}