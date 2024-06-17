package org.example;

import io.javalin.apibuilder.ApiBuilder;
import library.controller.UserController;
import org.example.Routes;

public class Routes {
    public static final String LOGIN_PAGE = "/login";
    public static final String LOGIN_ACTION = "/login-action";
    public static final String LOGOUT_ACTION = "/logout";
    public static final String BOOKS = "/books";

    public static void configure(Server server) {
        server.routes(() -> {
            ApiBuilder.post(LOGIN_ACTION, UserController.login);
            ApiBuilder.post(LOGOUT_ACTION, UserController.logout);
        });
    }
}
