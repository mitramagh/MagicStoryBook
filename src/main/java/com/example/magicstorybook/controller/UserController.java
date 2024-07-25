package com.example.magicstorybook.controller;

import com.example.magicstorybook.model.CustomOAuth2User;
import com.example.magicstorybook.model.Story;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.StoryRepository;
import com.example.magicstorybook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String email = oAuth2User.getEmail();
        return userRepository.findByEmail(email);
    }

    @PostMapping("/stories")
    public Story createStory(@AuthenticationPrincipal CustomOAuth2User oAuth2User, @RequestBody Story story) {
        String email = oAuth2User.getEmail();
        User user = userRepository.findByEmail(email);
        story.setUser(user);
        return storyRepository.save(story);
    }

    @GetMapping("/stories")
    public List<Story> getStories(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String email = oAuth2User.getEmail();
        User user = userRepository.findByEmail(email);
        return storyRepository.findByUser(user);
    }
}

