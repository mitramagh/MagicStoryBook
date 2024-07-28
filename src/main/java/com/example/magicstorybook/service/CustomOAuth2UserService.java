package com.example.magicstorybook.service;

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

        // Find or create user
        Optional<User> optionalUser = userService.findByEmail(email);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            logger.debug("User found in database: {}", user);
        } else {
            user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            logger.debug("User not found, creating new user: {}", user);
        }

        // Update user information
        user.setFirstName(firstName);
        user.setLastName(lastName);

        User savedUser = userRepository.save(user);
        logger.debug("User after save: {}", savedUser);

        return oAuth2User;
    }
}
