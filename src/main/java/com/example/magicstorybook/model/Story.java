package com.example.magicstorybook.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stories")
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "genre")
    private String genre;

    @Column(name = "setting")
    private String setting;

    @Column(name = "character")
    private String character;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "age_range")
    private String ageRange;

    @Column(name = "word_range")
    private String wordRange;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();


// Getters and Setters
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

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
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
