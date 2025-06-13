package com.example.firstproject.beathub20;

import java.util.ArrayList;

public class LikedSongsManager {

    private static LikedSongsManager instance;
    private final ArrayList<Song> likedSongs;

    private LikedSongsManager() {
        likedSongs = new ArrayList<>();
    }

    public static LikedSongsManager getInstance() {
        if (instance == null) {
            instance = new LikedSongsManager();
        }
        return instance;
    }

    public void likeSong(Song song) {
        if (!likedSongs.contains(song)) {
            likedSongs.add(song);
        }
    }

    public void unlikeSong(Song song) {
        likedSongs.remove(song);
    }

    public ArrayList<Song> getLikedSongs() {
        return new ArrayList<>(likedSongs);
    }
}
