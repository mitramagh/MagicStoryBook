package com.example.magicstorybook.controller;

import com.example.magicstorybook.model.Story;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.StoryRepository;
import com.example.magicstorybook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private OAuth2AuthorizedClientService authorizedClientService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentUser_UserPresent() {
        // Mock the OAuth2User
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttribute("email")).thenReturn("test@example.com");

        // Mock the OAuth2AuthenticationToken
        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        when(authentication.getAuthorizedClientRegistrationId()).thenReturn("client-id");
        when(authentication.getName()).thenReturn("user-name");

        // Mock the UserRepository
        User user = new User();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Mock the OAuth2AuthorizedClientService
        OAuth2AuthorizedClient authorizedClient = mock(OAuth2AuthorizedClient.class);
        when(authorizedClientService.loadAuthorizedClient("client-id", "user-name")).thenReturn(authorizedClient);

        // Mock the OAuth2AccessToken
        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class);
        when(accessToken.getTokenValue()).thenReturn("token");
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);

        // Call the method to test
        ResponseEntity<Map<String, Object>> response = userController.getCurrentUser(oAuth2User, authentication);

        // Verify the response
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("user"));
        assertTrue(response.getBody().containsKey("token"));
    }

    @Test
    void testGetCurrentUser_UserNotPresent() {
        // Mock the OAuth2User
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttribute("email")).thenReturn("test@example.com");

        // Mock the OAuth2AuthenticationToken
        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        when(authentication.getAuthorizedClientRegistrationId()).thenReturn("client-id");
        when(authentication.getName()).thenReturn("user-name");

        // Mock the UserRepository to return an empty Optional
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Mock the OAuth2AuthorizedClientService
        OAuth2AuthorizedClient authorizedClient = mock(OAuth2AuthorizedClient.class);
        when(authorizedClientService.loadAuthorizedClient("client-id", "user-name")).thenReturn(authorizedClient);

        // Mock the OAuth2AccessToken
        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class);
        when(accessToken.getTokenValue()).thenReturn("token");
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);

        // Call the method to test
        ResponseEntity<Map<String, Object>> response = userController.getCurrentUser(oAuth2User, authentication);

        // Verify the response
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    void testGetUserById_UserPresent() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserById_UserNotPresent() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetUsers() {
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsers();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
    }

    @Test
    void testGetStoriesByUserId() {
        List<Story> stories = new ArrayList<>();
        when(storyRepository.findByUserId(1L)).thenReturn(stories);

        ResponseEntity<List<Story>> response = userController.getStoriesByUserId(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stories, response.getBody());
    }
    @Test
    void testDeleteUser_UserPresent() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userController.deleteUser(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User deleted successfully", response.getBody());
    }

    @Test
    void testDeleteUser_UserNotPresent() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userController.deleteUser(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }


}
