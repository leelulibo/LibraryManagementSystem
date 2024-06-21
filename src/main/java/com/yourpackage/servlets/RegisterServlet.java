package com.yourpackage.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate input parameters
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            response.sendRedirect("register.html?error=emptyFields");
            return;
        }

        // Dummy registration logic for illustration
        if (!password.equals(confirmPassword)) {
            response.sendRedirect("register.html?error=passwordMismatch");
            return;
        }

        // Here, you would typically save the user details to a database

        // Redirect to login page after successful registration
        response.sendRedirect("login.html");
    }
}
