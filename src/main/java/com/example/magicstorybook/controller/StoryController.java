package com.example.magicstorybook.controller;

import com.example.magicstorybook.model.Story;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.StoryRepository;
import com.example.magicstorybook.repository.UserRepository;
import com.example.magicstorybook.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createStory(
            @RequestBody Map<String, Object> requestBody) {

        String genre = (String) requestBody.get("genre");
        String setting = (String) requestBody.get("setting");
        List<String> characters = (List<String>) requestBody.get("characters");
        String title = (String) requestBody.get("title");
        String specialMessage = (String) requestBody.get("specialMessage");
        String ageRange = (String) requestBody.get("ageRange");
        String wordRange = (String) requestBody.get("wordRange");
        Long userId = ((Number) requestBody.get("userId")).longValue(); // Convert to Long

        if (characters == null || characters.size() < 1 || characters.size() > 3) {
            return ResponseEntity.badRequest().body(Map.of("error", "The number of characters must be between 1 and 3."));
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();

        String storyPrompt = String.format("Write a %s-word story for a %s-year-old child. The genre is %s. The story should include the characters: %s. The setting is %s. Title: %s. Special message: %s.",
                wordRange, ageRange, genre, String.join(", ", characters), setting, title, specialMessage);

        String imagePrompt = String.format("Create an illustration for a %s-year-old child story with the genre %s, set in %s, featuring characters %s.",
                ageRange, genre, setting, String.join(", ", characters));

        try {
            String storyContent = openAIService.createStory(storyPrompt);
            String imageUrl = openAIService.createImage(imagePrompt);

            // Create a new story and associate it with the userId
            Story newStory = new Story(userId, genre, setting, characters, title, storyContent, imageUrl, ageRange, wordRange, specialMessage);
            storyRepository.save(newStory);

            // Return the content and image URL in the response
            return ResponseEntity.ok(Map.of("content", storyContent, "image", imageUrl));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error generating story or image: " + e.getMessage()));
        }
    }

    @GetMapping("/my-stories")
    public ResponseEntity<List<Story>> getMyStories(@RequestParam Long userId) {
        List<Story> stories = storyRepository.findByUserId(userId);
        return ResponseEntity.ok(stories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStory(@PathVariable Long id) {
        Optional<Story> story = storyRepository.findById(id);
        if (story.isPresent()) {
            storyRepository.deleteById(id);
            return ResponseEntity.ok("Story deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
