package org.example.Library.config.controller;

import org.example.Library.dto.RegistrationRequest;
import org.example.Library.model.User;
import org.example.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.*;

@Controller

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Assuming register.html exists in your templates folder

    }

    // POST mapping for handling user registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.saveUser(user); // Save user using userService
        return "redirect:/users/" + user.getUsername(); // Redirect to user profile
    }

    // GET mapping to retrieve user by username
    @GetMapping("/{username}")
    public String getUserProfile(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "profile"; // Assuming profile.html exists in your templates folder
    }

    // Endpoint to test database connection
    @GetMapping("/test-connection")
    @ResponseBody
    public String testDatabaseConnection() {
        try {
            userService.testConnection();
            return "Database connection is successful.";
        } catch (Exception e) {
            return "Failed to connect to the database: " + e.getMessage();
        }
    }

    // GET mapping for testing endpoint
    @GetMapping("/test")
    @ResponseBody
    public String testEndpoint() {
        return "UserController is up and running!";
    }

    // GET mapping to insert a test user into the database
    @GetMapping("/insert-test-user")
    @ResponseBody
    public String insertTestUser() {
        userService.insertTestUser();
        return "Test user inserted successfully.";
    }
}

