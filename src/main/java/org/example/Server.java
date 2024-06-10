package org.example;

import io.javalin.Javalin;

public class Server {

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.enableCorsForAllOrigins();
        }).start(7000);

        app.get("/", ctx -> ctx.result("Hello, World!"));

        app.routes(() -> {
            app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));
            app.post("/data", ctx -> {
                String body = ctx.body();
                ctx.result("Received: " + body);
            });
            app.get("/users/{userId}", ctx -> {
                String userId = ctx.pathParam("userId");
                ctx.result("User ID: " + userId);
            });
        });

        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(500);
            ctx.result("Internal Server Error: " + e.getMessage());
        });

        app.error(404, ctx -> {
            ctx.json("Custom 404 Page Not Found");
        });
    }
}
