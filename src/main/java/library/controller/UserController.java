package library.controller;

import io.javalin.http.Handler;
import org.example.User;
import library.persistence.UserDAO;
import org.example.Server;
import org.example.ServiceRegistry;
import org.example.Routes;

public class UserController {

    private static final UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);

    public static final Handler logout = ctx -> {
        ctx.sessionAttribute(Server.SESSION_USER_KEY, null);
        ctx.redirect(Routes.LOGIN_ACTION);
    };

    public static final Handler login = context -> {
        String email = context.formParam("username"); // Assuming "username" matches your form input name
        String password = context.formParam("password");


        if (email == null || email.isEmpty()) {
            context.status(400).result("Email is required");
            return;
        }


        User user = userDAO.findUserByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User(email, "Default User Name"); // Adjust default user name
                    userDAO.saveUser(newUser);
                    return newUser;
                });

        context.sessionAttribute(Server.SESSION_USER_KEY, user);
        context.redirect(Routes.BOOKS); // Adjust the redirect route as necessary
    };

    // TODO Implement user registration
    public static final Handler register = context -> {
        String email = context.formParam("username");
        String password = context.formParam("password");
        String confirmPassword = context.formParam("confirmPassword");

        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        context.render("login.html");

    };

    public static Handler viewLogin = context -> {
        context.render("login.html");
    };
    public static Handler viewRegister = context -> {
        context.render("register.html");
    };
}
