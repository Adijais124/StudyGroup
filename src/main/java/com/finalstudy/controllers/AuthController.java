package com.finalstudy.controllers;

import com.finalstudy.models.User;
import com.finalstudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService; // Inject UserService dependency

    // Sign-Up Page
    @GetMapping("/signup")
    public String showSignUpForm() {
        return "signup"; // Maps to signup.html
    }

    // Handle User Registration
    @PostMapping("/signup")
    public String handleSignUp(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String course) {
        userService.registerUser(name, email, password, course);
        System.out.println("User registered: " + name + ", " + email + ", " + course);
        return "redirect:/login"; // Redirect to login page after successful sign-up
    }

    // Login Page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Maps to login.html
    }

    // Handle User Login
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (userService.checkPassword(password, user.getPassword())) {
                // Store email in session
                session.setAttribute("userEmail", email);
                System.out.println("Login successful for: " + email);
                return "redirect:/dashboard"; // Redirect to dashboard on success
            }
        }

        // Log failed attempt and redirect to login with error
        System.out.println("Invalid login attempt for: " + email);
        return "redirect:/login?error"; // Redirect to login page with error
    }
}