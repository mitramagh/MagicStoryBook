package com.example.magicstorybook.controller;

import com.example.magicstorybook.model.UserDTO;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/signup")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> showSignupForm(OAuth2AuthenticationToken authentication) {
        if (authentication != null && authentication.getPrincipal() != null) {
            OAuth2User oAuth2User = authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String firstName = oAuth2User.getAttribute("given_name");
            String lastName = oAuth2User.getAttribute("family_name");

            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                return ResponseEntity.status(409).body("User already registered"); // Conflict, user already exists
            }

            User newUser = new User(firstName, lastName, email);
            userRepository.save(newUser);
            return ResponseEntity.status(201).body("User registered successfully"); // Created
        }

        return ResponseEntity.status(401).body("Not authenticated with OAuth2"); // Unauthorized
    }

    @PostMapping
    public ResponseEntity<?> signupUser(@RequestBody UserDTO newUserDTO) {
        Optional<User> existingUser = userRepository.findByEmail(newUserDTO.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(409).body("User already exists"); // Conflict, user already exists
        }

        User newUser = new User(newUserDTO.getFirstName(), newUserDTO.getLastName(), newUserDTO.getEmail());
        userRepository.save(newUser);
        return ResponseEntity.status(201).body("User registered successfully"); // Created
    }

    //add logout endpoint
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok().body("{\"message\": \"Logged out successfully\"}");
    }
}
