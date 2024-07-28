package com.example.magicstorybook.controller;

import com.example.magicstorybook.model.Story;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.StoryRepository;
import com.example.magicstorybook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoryRepository storyRepository;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());


//    @GetMapping("/profile")
//    public OidcUser getCurrentUser(@AuthenticationPrincipal OidcUser principal) {
//        if (principal != null) {
//            System.out.println("User is authenticated: " + principal.getEmail());
//        } else {
//            System.out.println("User is not authenticated.");
//        }
//        return principal;


    @GetMapping("/profile")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            logger.severe("OAuth2User is null");
            return ResponseEntity.notFound().build();
        }
        String email = oAuth2User.getAttribute("email");
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/story")
    public ResponseEntity<Story> createStory(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody Story story) {
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        story.setUser(user);
        Story savedStory = storyRepository.save(story);
        return ResponseEntity.ok(savedStory);
    }
    @GetMapping("/stories")
    public ResponseEntity<List<Story>> getStories(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Story> stories = storyRepository.findByUser(user);
        return ResponseEntity.ok(stories);
    }
}
