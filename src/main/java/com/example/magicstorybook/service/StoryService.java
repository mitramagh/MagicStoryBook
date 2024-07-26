package com.example.magicstorybook.service;


import com.example.magicstorybook.model.Story;
import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoryService {
    @Autowired
    private StoryRepository storyRepository;

    public List<Story> getStoriesByUser(User user) {
        return storyRepository.findByUser(user);
    }

    public Optional<Story> getStoryById(Long id) {
        return storyRepository.findById(id);
    }

    public Story saveStory(Story story) {
        return storyRepository.save(story);
    }

    public void deleteStory(Long id) {
        storyRepository.deleteById(id);
    }
}

