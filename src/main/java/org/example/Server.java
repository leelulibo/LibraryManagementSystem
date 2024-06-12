package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Server {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("../../webapp", Location.CLASSPATH);
        }).start(7000);

        app.get("/", ctx -> ctx.redirect("/login.html"));

        app.post("/login.action", ctx -> {
            String email = ctx.formParam("email");
            // Perform login logic
            if (email != null && email.equals("test@example.com")) {
                ctx.result("Login successful!");
            } else {
                ctx.result("Invalid email address.");
            }
        });

        // Add other routes as needed
    }
}