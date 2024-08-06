package com.example.magicstorybook.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stories")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String genre;
    private String setting;
    private String title;
    private String specialMessage;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "age_range")
    private String ageRange;

//    @Lob
    @Column(columnDefinition = "TEXT")
    private String image;
    @Column(name = "word_range")
    private String wordRange;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @ElementCollection
    @CollectionTable(name = "story_characters", joinColumns = @JoinColumn(name = "story_id"))
    @Column(name = "character")
    private List<String> characters = new ArrayList<>();

    // Constructors, getters, and setters

    public Story() {
    }

    public Story(User user, String genre, String setting, List<String> characters, String title, String content, String image, String ageRange, String wordRange, String specialMessage) {
        this.user = user;
        this.genre = genre;
        this.setting = setting;
        this.characters = characters;
        this.title = title;
        this.content = content;
        this.image = image;
        this.ageRange = ageRange;
        this.wordRange = wordRange;
        this.creationDate = LocalDateTime.now();
        this.specialMessage = specialMessage;
    }

    // Getters and setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSpecialMessage() {
        return specialMessage;
    }

    public void setSpecialMessage(String specialMessage) {
        this.specialMessage = specialMessage;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getWordRange() {
        return wordRange;
    }

    public void setWordRange(String wordRange) {
        this.wordRange = wordRange;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
