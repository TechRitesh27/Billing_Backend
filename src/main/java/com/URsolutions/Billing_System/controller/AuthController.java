package com.URsolutions.Billing_System.controller;

import com.URsolutions.Billing_System.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "billing-software-ecigauf9q-riteshs-projects-eb2687fd.vercel.app")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        // ✅ Hardcoded credentials for admin
        if ("Admin".equals(username) && "Admin123".equals(password)) {
            return ResponseEntity.ok("Login successful");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}