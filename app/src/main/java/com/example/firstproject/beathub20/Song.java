package com.example.firstproject.beathub20;

public class Song {
    private String title;
    private String artist;
    private String audioUrl;
    private String imageUrl;

    public Song() {
        // Required empty constructor for Firestore
    }

    public Song(String title, String artist, String audioUrl, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.audioUrl = audioUrl;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
