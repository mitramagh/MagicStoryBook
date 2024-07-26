package com.example.magicstorybook.controller;

import com.example.magicstorybook.model.CustomOAuth2User;
import com.example.magicstorybook.model.Story;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.StoryRepository;
import com.example.magicstorybook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String email = oAuth2User.getEmail();
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/stories")
    public ResponseEntity<Story> createStory(@AuthenticationPrincipal CustomOAuth2User oAuth2User, @RequestBody Story story) {
        String email = oAuth2User.getEmail();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        story.setUser(user);
        Story savedStory = storyRepository.save(story);
        return ResponseEntity.ok(savedStory);
    }

    @GetMapping("/stories")
    public ResponseEntity<List<Story>> getStories(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String email = oAuth2User.getEmail();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Story> stories = storyRepository.findByUser(user);
        return ResponseEntity.ok(stories);
    }
}
