package com.example.magicstorybook.service;


import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.example.magicstorybook.model.CustomOAuth2User;

//@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract user information from the OAuth2 response
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String email = (String) attributes.get("email");

        // Create and return a custom user principal with the extracted information
        return new CustomOAuth2User(oAuth2User.getAuthorities(), attributes, "sub", firstName, lastName, email);
    }
}
