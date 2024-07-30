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
    public ResponseEntity<String> createStory(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @RequestParam String genre,
            @RequestParam String setting,
            @RequestParam List<String> characters,
            @RequestParam String title,
            @RequestParam String ageRange,
            @RequestParam String wordRange) {

        if (oAuth2User == null) {
            return ResponseEntity.badRequest().body("User is not authenticated");
        }

        String email = oAuth2User.getAttribute("email");

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOptional.get();

        List<Story> existingStories = storyRepository.findByUserAndGenreAndSettingAndTitleAndAgeRangeAndWordRange(
                user, genre, setting, title, ageRange, wordRange);

        for (Story existingStory : existingStories) {
            if (existingStory.getCharacters().containsAll(characters) && characters.containsAll(existingStory.getCharacters())) {
                return ResponseEntity.status(409).body("A story with the same parameters and characters already exists. Please make another selection.");
            }
        }

        String prompt = String.format("hi, I'm a %s kid, create a story for me in %s genre, with a %s setting, and with characters %s, titled %s, in %s words",
                ageRange, genre, setting, String.join(", ", characters), title, wordRange);

        String storyContent = openAIService.createStory(prompt);

        // Example to get image byte array
        // You need to adjust this part to actually call the OpenAI API to generate an image
        byte[] imageBytes = openAIService.createImage(prompt);

        Story newStory = new Story(user, genre, setting, characters, title, storyContent, imageBytes, ageRange, wordRange);
        storyRepository.save(newStory);

        return ResponseEntity.ok(storyContent);
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
}
