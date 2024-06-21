//import io.javalin.http.Context;
//import org.example.Server;
//import org.example.User;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//public class ServerTest {
//
//    @Test
//    void testGetUserLoggedIn() {
//        // Mock context and user
//        Context context = Mockito.mock(Context.class);
//        User user = new User("test@example.com", "Test User");
//
//        // Set up session attribute
////        when(context.sessionAttribute(Server.SESSION_USER_KEY)).thenReturn(user);
//
//        // Call method under test
//        User loggedInUser = Server.getUserLoggedIn(context);
//
//        // Assert that the retrieved user is the expected user
//        assertEquals(user, loggedInUser);
//    }
//}
