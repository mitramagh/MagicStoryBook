//package com.example.magicstorybook.controller;
//
//import com.example.magicstorybook.model.Story;
//import com.example.magicstorybook.model.User;
//import com.example.magicstorybook.repository.StoryRepository;
//import com.example.magicstorybook.repository.UserRepository;
//import com.example.magicstorybook.service.OpenAIService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class StoryControllerTest {
//
//    @Mock
//    private OpenAIService openAIService;
//
//    @Mock
//    private StoryRepository storyRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private StoryController storyController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateStory_Success() {
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("genre", "Fantasy");
//        requestBody.put("setting", "A magical forest");
//        requestBody.put("characters", Arrays.asList("Elf", "Wizard"));
//        requestBody.put("title", "The Enchanted Journey");
//        requestBody.put("specialMessage", "Always believe in magic");
//        requestBody.put("ageRange", "8");
//        requestBody.put("wordRange", "500-1000");
//        requestBody.put("userId", 1L);
//
//        User user = new User();
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        String storyContent = "Once upon a time...";
//        String imageUrl = "http://image.url";
//        when(openAIService.createStory(anyString())).thenReturn(storyContent);
//        when(openAIService.createImage(anyString())).thenReturn(imageUrl);
//
//        ResponseEntity<Map<String, String>> response = storyController.createStory(requestBody);
//
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertEquals(storyContent, response.getBody().get("content"));
//        assertEquals(imageUrl, response.getBody().get("image"));
//
//        verify(storyRepository, times(1)).save(any(Story.class));
//    }
//
//    @Test
//    void testCreateStory_InvalidCharacters() {
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("genre", "Fantasy");
//        requestBody.put("setting", "A magical forest");
//        requestBody.put("characters", Arrays.asList());
//        requestBody.put("title", "The Enchanted Journey");
//        requestBody.put("specialMessage", "Always believe in magic");
//        requestBody.put("ageRange", "8");
//        requestBody.put("wordRange", "500-1000");
//        requestBody.put("userId", 1L);
//
//        ResponseEntity<Map<String, String>> response = storyController.createStory(requestBody);
//
//        assertNotNull(response);
//        assertEquals(400, response.getStatusCodeValue());
//        assertEquals("The number of characters must be between 1 and 3.", response.getBody().get("error"));
//
//        verify(userRepository, never()).findById(anyLong());
//        verify(storyRepository, never()).save(any(Story.class));
//    }
//
//    @Test
//    void testCreateStory_UserNotFound() {
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("genre", "Fantasy");
//        requestBody.put("setting", "A magical forest");
//        requestBody.put("characters", Arrays.asList("Elf", "Wizard"));
//        requestBody.put("title", "The Enchanted Journey");
//        requestBody.put("specialMessage", "Always believe in magic");
//        requestBody.put("ageRange", "8");
//        requestBody.put("wordRange", "500-1000");
//        requestBody.put("userId", 1L);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        ResponseEntity<Map<String, String>> response = storyController.createStory(requestBody);
//
//        assertNotNull(response);
//        assertEquals(400, response.getStatusCodeValue());
//        assertEquals("User not found", response.getBody().get("error"));
//
//        verify(storyRepository, never()).save(any(Story.class));
//    }
//
//    @Test
//    void testGetAllStories() {
//        List<Story> stories = Arrays.asList(new Story(), new Story());
//        when(storyRepository.findAll()).thenReturn(stories);
//
//        ResponseEntity<List<Story>> response = storyController.getAllStories();
//
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(stories, response.getBody());
//    }
//
//    @Test
//    void testGetStoryById_StoryPresent() {
//        Story story = new Story();
//        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));
//
//        ResponseEntity<Story> response = storyController.getStoryById(1L);
//
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(story, response.getBody());
//    }
//
//    @Test
//    void testGetStoryById_StoryNotPresent() {
//        when(storyRepository.findById(1L)).thenReturn(Optional.empty());
//
//        ResponseEntity<Story> response = storyController.getStoryById(1L);
//
//        assertNotNull(response);
//        assertEquals(404, response.getStatusCodeValue());
//    }
//
//    @Test
//    void testDeleteStory_StoryPresent() {
//        Story story = new Story();
//        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));
//
//        ResponseEntity<String> response = storyController.deleteStory(1L);
//
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Story deleted successfully", response.getBody());
//    }
//
//    @Test
//    void testDeleteStory_StoryNotPresent() {
//        when(storyRepository.findById(1L)).thenReturn(Optional.empty());
//
//        ResponseEntity<String> response = storyController.deleteStory(1L);
//
//        assertNotNull(response);
//        assertEquals(404, response.getStatusCodeValue());
//    }
//}
