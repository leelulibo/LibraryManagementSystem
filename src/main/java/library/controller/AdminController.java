package library.controller;

import io.javalin.http.Handler;
import library.persistence.BookDAO;
import library.persistence.UserDAO;
import org.example.ServiceRegistry;
import org.example.User;

import java.util.Collection;
import java.util.Map;

public class AdminController {


    public static Handler viewAdminLogin = context -> {
        UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);
        Collection<User> users = userDAO.getAllUsers();
        Map<String, Object> viewModel = Map.of(
                "users", users
        );
        context.render("admin_portal.html", viewModel);
    };
}
