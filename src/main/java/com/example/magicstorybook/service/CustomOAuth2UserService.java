package com.example.magicstorybook.service;

import com.example.magicstorybook.model.CustomOAuth2User;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Extract user information from the OAuth2 response
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        // Save or update user in the database
        User user = userRepository.findUserByEmail(email)
                .orElseGet(() -> new User(firstName, lastName, email));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);

        return new CustomOAuth2User(oAuth2User);
    }
}
