package com.example.magicstorybook.controller;

import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "https://magic-story-book-ui-fm3a.onrender.com")
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
            String profilePicture = oAuth2User.getAttribute("picture");

            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                return ResponseEntity.ok(createResponse(existingUser.get(), authentication));
            }

            User newUser = new User(firstName, lastName, email, profilePicture);
            userRepository.save(newUser);
            return ResponseEntity.status(201).body(createResponse(newUser, authentication));
        }

        return ResponseEntity.status(401).body("Not authenticated with OAuth2"); // Unauthorized
    }

    private Map<String, Object> createResponse(User user, OAuth2AuthenticationToken authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User logged in successfully: " + user.getFirstName());

        // Retrieve the OAuth2 access token or ID token
        String token = authentication.getPrincipal().getAttribute("sub"); // Assuming you used the token before

        if (token == null) {
            // If not found, fall back to the access token if available
            token = (String) authentication.getPrincipal().getAttributes().get("access_token");
        }

        response.put("token", token); // Return the token
        response.put("user", user);
        return response;
    }
}