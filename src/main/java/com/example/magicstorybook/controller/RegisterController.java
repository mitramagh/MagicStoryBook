package com.example.magicstorybook.controller;

import com.example.magicstorybook.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.UserRepository;


import java.util.Optional;

@Controller
@RequestMapping("/signup")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showSignupForm(Model model, OAuth2AuthenticationToken authentication) {
        if (authentication != null && authentication.getPrincipal() != null) {
            OAuth2User oAuth2User = authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String firstName = oAuth2User.getAttribute("given_name");
            String lastName = oAuth2User.getAttribute("family_name");

            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                model.addAttribute("message", "User already registered");
                return "redirect:/login"; // Redirect to login if user already exists
            }

            User newUser = new User(firstName, lastName, email);
            userRepository.save(newUser);
            model.addAttribute("message", "User registered successfully");
            return "redirect:/"; // Redirect to home page after successful registration
        }

        return "register"; // Show registration form if not authenticated with OAuth2
    }

    @PostMapping
    public ResponseEntity<User> signupUser(@RequestBody UserDTO newUserDTO) {
        Optional<User> existingUser = userRepository.findByEmail(newUserDTO.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(409).body(existingUser.get()); // Conflict, user already exists
        }

        User newUser = new User(newUserDTO.getFirstName(), newUserDTO.getLastName(), newUserDTO.getEmail());
        userRepository.save(newUser);
        return ResponseEntity.status(201).body(newUser); // Created
    }
}
