package com.example.magicstorybook.repository;
import com.example.magicstorybook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.magicstorybook.model.Story;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByUserId(Long userId);

    List<Story> findByUserIdAndGenreAndSettingAndTitleAndAgeRangeAndWordRange(
            Long userId, String genre, String setting, String title, String ageRange, String wordRange);
}