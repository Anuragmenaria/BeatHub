package com.example.firstproject.beathub20;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerSongs;
    private SongAdapter adapter;
    private ArrayList<Song> songList;
    private FirebaseFirestore firestore;
    private SearchView searchView;
    private LinearLayout miniPlayerLayout;
    private ImageView miniImage;
    private TextView miniTitle, miniArtist;
    private Button likedSongsBtn;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imageMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerSongs = findViewById(R.id.recyclerSongs);
        searchView = findViewById(R.id.searchView);
        likedSongsBtn = findViewById(R.id.buttonLikedSongs);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        imageMenu = findViewById(R.id.imageMenu);

        imageMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomePage.this, loginpage.class));
                finish();
            } else if (id == R.id.nav_view_profile) {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_recents) {
                Toast.makeText(this, "Recents clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            } else if (id ==R.id.nav_buttonLikedSongs) {
                startActivity(new Intent(HomePage.this,LikedSongs.class));

            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        recyclerSongs.setLayoutManager(new LinearLayoutManager(this));
        songList = new ArrayList<>();
        adapter = new SongAdapter(this, songList);
        recyclerSongs.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        loadSongsFromFirebase();

        // SearchView logic
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        // Liked Songs Button
        likedSongsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, LikedSongs.class);
            startActivity(intent);
        });

        // MiniPlayer init
        miniPlayerLayout = findViewById(R.id.miniPlayer);
        miniImage = findViewById(R.id.miniPlayerImage);
        miniTitle = findViewById(R.id.miniPlayerTitle);
        miniArtist = findViewById(R.id.miniPlayerArtist);

        updateMiniPlayer();
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
                    adapter.updateFullList(songList);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching songs", e));
    }

    private void updateMiniPlayer() {
        if (!MiniPlayerInfo.hasSong()) {
            miniPlayerLayout.setVisibility(View.GONE);
        } else {
            miniPlayerLayout.setVisibility(View.VISIBLE);
            miniTitle.setText(MiniPlayerInfo.getTitle());
            miniArtist.setText(MiniPlayerInfo.getArtist());

            Glide.with(this)
                    .load(MiniPlayerInfo.getImageUrl())
                    .placeholder(R.drawable.placeholder_album)
                    .into(miniImage);

            // Navigate to full player on click
            miniPlayerLayout.setOnClickListener(v -> {
                Intent intent = new Intent(HomePage.this, MusicPlayerActivity.class);
                intent.putExtra("title", MiniPlayerInfo.getTitle());
                intent.putExtra("artist", MiniPlayerInfo.getArtist());
                intent.putExtra("imageUrl", MiniPlayerInfo.getImageUrl());
                // You can also store audioUrl in MiniPlayerInfo if you want to resume playback
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMiniPlayer(); // Refresh mini player when coming back to home
    }
}
