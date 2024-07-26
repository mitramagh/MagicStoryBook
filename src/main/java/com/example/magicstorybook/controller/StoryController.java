package com.example.magicstorybook.controller;


import com.example.magicstorybook.model.Story;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.service.StoryService;
import com.example.magicstorybook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stories")
public class StoryController {
    @Autowired
    private StoryService storyService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Story> getStoriesByUser(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        User user = userService.findUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return storyService.getStoriesByUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Story> getStoryById(@PathVariable Long id) {
        return storyService.getStoryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Story createStory(@AuthenticationPrincipal OAuth2User principal, @RequestBody Map<String, String> storyDetails) {
        String email = principal.getAttribute("email");
        User user = userService.findUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Story story = new Story();
        story.setUser(user);
        story.setGenre(storyDetails.get("genre"));
        story.setSetting(storyDetails.get("setting"));
        story.setCharacter(storyDetails.get("character"));
        story.setTitle(storyDetails.get("title"));
        story.setContent(storyDetails.get("content"));
        story.setAgeRange(storyDetails.get("ageRange"));
        story.setWordRange(storyDetails.get("wordRange"));

        return storyService.saveStory(story);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }
}

