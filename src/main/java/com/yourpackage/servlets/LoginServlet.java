package com.yourpackage.servlets;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate the input parameters
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new ServletException("Username and password must not be empty");
        }

        try {
            // Assume a method validateUser(username, password) that checks user credentials
            boolean isValidUser = validateUser(username, password);

            if (!isValidUser) {
                throw new ServletException("Invalid login credentials");
            }

            // If validation passes, redirect to the home page or another page
            response.sendRedirect("home.jsp");
        } catch (Exception e) {
            // Catch any other unexpected exceptions and wrap them in a ServletException
            throw new ServletException("An error occurred during login", e);
        }
    }

    private boolean validateUser(String username, String password) {
        // Replace this with real user validation logic, e.g., database check
        return "admin".equals(username) && "password".equals(password);
    }
}
