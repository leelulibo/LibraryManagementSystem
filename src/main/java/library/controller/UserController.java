package library.controller;

import io.javalin.http.Handler;
import org.example.User;
import library.persistence.UserDAO;
import org.example.Server;
import org.example.ServiceRegistry;
import org.example.Routes;

import java.util.Objects;

public class UserController {

    private static final UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);

    public static final Handler logout = ctx -> {
        ctx.sessionAttribute(Server.SESSION_USER_KEY, null);
        ctx.redirect(Routes.LOGIN_PAGE);
    };


//    public static final Handler logout = ctx -> {
//        ctx.sessionAttribute(Server.SESSION_USER_KEY, null);
//        ctx.redirect(Routes.LOGIN_ACTION);
//    };

    private static final UserDAO personDAO = ServiceRegistry.lookup(UserDAO.class);
    public static final Handler login = context -> {
        String email = context.formParamAsClass("email", String.class)
                .check(Objects::nonNull, "Email is required")
                .get();
        String password = context.formParam("password");

        context.sessionAttribute(Server.SESSION_USER_KEY, userDAO);
        context.redirect(Routes.BOOKS);

        User user = new User(email, password);
        User person = (new User(user.toString(),password));
        context.sessionAttribute(Server.SESSION_USER_KEY, person);
        context.redirect(Routes.BOOKS);
        };
//    };

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
