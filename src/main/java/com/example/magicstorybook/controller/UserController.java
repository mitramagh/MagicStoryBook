package com.example.magicstorybook.controller;

import com.example.magicstorybook.model.Story;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.StoryRepository;
import com.example.magicstorybook.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Transactional
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User, OAuth2AuthenticationToken authentication) {
        if (oAuth2User == null) {
            logger.severe("OAuth2User is null");
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
        String email = oAuth2User.getAttribute("email");
        logger.info("Extracted email: " + email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            logger.info("User found: " + user.get());
        } else {
            logger.warning("User not found for email: " + email);
        }

        // Extracting and logging the token
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        String token = authorizedClient.getAccessToken().getTokenValue();
        logger.info("Token: " + token);

        return user.map(u -> {
            Map<String, Object> response = new HashMap<>();
            response.put("user", u);
            response.put("token", token);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("Fetching user by ID: " + id);
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        logger.info("Fetching user by email: " + email);
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("Updating user with ID: " + id);
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            userRepository.save(updatedUser);
            logger.info("User updated successfully: " + updatedUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            logger.warning("User not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stories")
    public ResponseEntity<Map<String, Object>> getStories(@AuthenticationPrincipal OAuth2User oAuth2User, OAuth2AuthenticationToken authentication) {
        if (oAuth2User == null) {
            logger.severe("OAuth2User is null");
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
        String email = oAuth2User.getAttribute("email");
        logger.info("Fetching stories for user email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warning("User not found for email: " + email);
                    return new RuntimeException("User not found");
                });
        List<Story> stories = storyRepository.findByUserId(user.getId());

        // Extracting and logging the token
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        String token = authorizedClient.getAccessToken().getTokenValue();
        logger.info("Token: " + token);

        Map<String, Object> response = new HashMap<>();
        response.put("stories", stories);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: " + id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            logger.info("User deleted successfully");
            return ResponseEntity.ok("User deleted successfully");
        } else {
            logger.warning("User not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }
}