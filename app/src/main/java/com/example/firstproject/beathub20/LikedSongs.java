package com.example.firstproject.beathub20;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LikedSongs extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_songs);

        recyclerView = findViewById(R.id.recyclerLikedSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Song> likedSongs = LikedSongsManager.getInstance().getLikedSongs();
        adapter = new SongAdapter(this, likedSongs);
        recyclerView.setAdapter(adapter);
    }
}
