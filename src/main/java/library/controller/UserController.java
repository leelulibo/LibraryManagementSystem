package library.controller;

import io.javalin.http.Handler;
import org.example.User;
import library.persistence.UserDAO;
import org.example.Server;
import org.example.ServiceRegistry;
import org.example.Routes;

import java.util.Objects;

public class UserController {

    public static final Handler logout = ctx -> {
        ctx.sessionAttribute(Server.SESSION_USER_KEY, null);
        ctx.redirect(Routes.LOGIN_PAGE);
    };

    private static final UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);

    public static final Handler login = context -> {
        String email = context.formParam("email");

        if (email == null || email.isEmpty()) {
            context.status(400).result("Email is required");
            return;
        }

        User user = userDAO.findUserByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User(email, "Default User Name"); // Adjust as needed
                    userDAO.saveUser(newUser);
                    return newUser;
                });

        context.sessionAttribute(Server.SESSION_USER_KEY, user);
        context.redirect(Routes.BOOKS); // Adjust the redirect route as necessary
    };
}
