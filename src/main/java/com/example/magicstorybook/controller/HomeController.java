package com.example.magicstorybook.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ResponseEntity<?> home(@AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            return ResponseEntity.ok().body("Welcome " + principal.getAttribute("name"));
        }
        return ResponseEntity.status(401).body("Not authenticated");  // Unauthorized
    }

}
