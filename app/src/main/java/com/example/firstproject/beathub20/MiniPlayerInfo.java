package com.example.firstproject.beathub20;

public class MiniPlayerInfo {
    private static String currentTitle = "";
    private static String currentArtist = "";
    private static String currentImageUrl = "";

    public static void setSong(String title, String artist, String imageUrl) {
        currentTitle = title;
        currentArtist = artist;
        currentImageUrl = imageUrl;
    }

    public static boolean hasSong() {
        return currentTitle != null && !currentTitle.isEmpty()
                && currentArtist != null && !currentArtist.isEmpty()
                && currentImageUrl != null && !currentImageUrl.isEmpty();
    }

    public static String getTitle() {
        return currentTitle;
    }

    public static String getArtist() {
        return currentArtist;
    }

    public static String getImageUrl() {
        return currentImageUrl;
    }
}
