package com.example.magicstorybook.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class LogoutController {

    private static final Logger logger = Logger.getLogger(LogoutController.class.getName());

    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            logger.info("Logging out user: " + authentication.getName());
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            return ResponseEntity.ok("{\"message\": \"Logged out successfully\"}");
        } else {
            logger.warning("No user is authenticated.");
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("{\"message\": \"No user is authenticated\"}");
        }
    }
}
