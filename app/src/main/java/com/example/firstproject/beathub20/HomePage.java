package com.example.firstproject.beathub20;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerSongs;
    private SongAdapter adapter;
    private ArrayList<Song> songList;
    private FirebaseFirestore firestore;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerSongs = findViewById(R.id.recyclerSongs);
        searchView = findViewById(R.id.searchView);
        recyclerSongs.setLayoutManager(new LinearLayoutManager(this));

        songList = new ArrayList<>();
        adapter = new SongAdapter(this, songList);
        recyclerSongs.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        loadSongsFromFirebase();

        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Handled by onQueryTextChange
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }

    private void loadSongsFromFirebase() {
        firestore.collection("songs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    songList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Song song = doc.toObject(Song.class);
                        if (song != null) {
                            songList.add(song);
                            Log.d("Firestore", "Loaded song: " + song.getTitle());
                        }
                    }
                    adapter.updateFullList(songList); // ✅ Efficiently update adapter list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomePage.this, "Failed to load songs", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error fetching songs", e);
                });
    }
}
