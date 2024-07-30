package com.example.magicstorybook.service;

import com.example.magicstorybook.model.CustomOAuth2User;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Test log statement to verify logging
        logger.info("Loading user from OAuth2UserRequest");
        logger.debug("OAuth2 User Attributes: {}", oAuth2User.getAttributes());

        // Extract user information from the OAuth2 response
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        logger.debug("Extracted email: {}", email);
        logger.debug("Extracted first name: {}", firstName);
        logger.debug("Extracted last name: {}", lastName);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User(firstName, lastName, email);
                    logger.debug("User not found, creating new user: {}", newUser);
                    return newUser;
                });

        // Update user information
        user.setFirstName(firstName);
        user.setLastName(lastName);
        User savedUser = userRepository.save(user);
        logger.debug("User after save: {}", savedUser);

        return new CustomOAuth2User(oAuth2User);
    }

    //register new user
    public User registerNewUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User newUser = new User(firstName, lastName, email);
        userRepository.save(newUser);
        return newUser;
    }
}
