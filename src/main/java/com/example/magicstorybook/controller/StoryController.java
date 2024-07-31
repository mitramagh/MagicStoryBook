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
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @RequestParam String genre,
            @RequestParam String setting,
            @RequestParam List<String> characters,
            @RequestParam String title,
            @RequestParam String ageRange,
            @RequestParam String wordRange) {

        if (oAuth2User == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User is not authenticated"));
        }

        if (characters.size() < 1 || characters.size() > 3) {
            return ResponseEntity.badRequest().body(Map.of("error", "The number of characters must be between 1 and 3."));
        }

        String email = oAuth2User.getAttribute("email");

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }
        User user = userOptional.get();

        List<Story> existingStories = storyRepository.findByUserAndGenreAndSettingAndTitleAndAgeRangeAndWordRange(
                user, genre, setting, title, ageRange, wordRange);

        for (Story existingStory : existingStories) {
            if (existingStory.getCharacters().containsAll(characters) && characters.containsAll(existingStory.getCharacters())) {
                return ResponseEntity.status(409).body(Map.of("error", "A story with the same parameters and characters already exists. Please make another selection."));
            }
        }

        String storyPrompt = String.format("Write a %s-word story for a %s-year-old child. The genre is %s. The story should include the characters: %s. The setting is %s. Title: %s.",
                wordRange, ageRange, genre, String.join(", ", characters), setting, title);

        String imagePrompt = String.format("Create an illustration for a story with the genre %s, set in %s, featuring characters %s.",
                genre, setting, String.join(", ", characters));

        try {
            String storyContent = openAIService.createStory(storyPrompt);
            String imageUrl = openAIService.createImage(imagePrompt);

            Story newStory = new Story(user, genre, setting, characters, title, storyContent, imageUrl, ageRange, wordRange);
            storyRepository.save(newStory);

            return ResponseEntity.ok(Map.of("content", storyContent, "image", imageUrl));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error generating story or image: " + e.getMessage()));
        }
    }

    @GetMapping("/my-stories")
    public ResponseEntity<List<Story>> getMyStories(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.badRequest().body(null);
        }

        String email = oAuth2User.getAttribute("email");

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        User user = userOptional.get();

        return ResponseEntity.ok(user.getStories());
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
