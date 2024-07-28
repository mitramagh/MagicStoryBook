package com.example.magicstorybook.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<User> registerUser(OAuth2AuthenticationToken authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.badRequest().build();
        }
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        Optional<User> existingUser = userRepository.findUserByEmail(email);
        if (existingUser.isPresent()) {
            return ResponseEntity.status(409).body(existingUser.get()); // Conflict, user already exists
        }

        User newUser = new User(firstName, lastName, email);
        userRepository.save(newUser);
        return ResponseEntity.ok(newUser);
    }
}
