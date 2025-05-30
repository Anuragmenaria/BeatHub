package com.example.firstproject.beathub20;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    Context context;
    ArrayList<Song> songs;
    ArrayList<Song> fullList; // 💡 To store original data for filtering

    public SongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = new ArrayList<>(songs);
        this.fullList = new ArrayList<>(songs);
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());

        String imageUrl = song.getImageUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            holder.albumArt.setImageResource(R.drawable.placeholder_album);
        } else {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_album)
                    .error(R.drawable.placeholder_album)
                    .into(holder.albumArt);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MusicPlayerActivity.class);
            intent.putExtra("title", song.getTitle());
            intent.putExtra("artist", song.getArtist());
            intent.putExtra("audioUrl", song.getAudioUrl());
            intent.putExtra("imageUrl", song.getImageUrl());
            context.startActivity(intent);
        });
    }
    // Add this method to update both lists
    public void updateFullList(ArrayList<Song> fullList) {
        this.fullList = new ArrayList<>(fullList); // update backup list
        this.songs = new ArrayList<>(fullList);    // update shown list
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return songs.size();
    }

    // ✅ Filter method
    public void filter(String query) {
        query = query.toLowerCase();
        songs.clear();
        if (query.isEmpty()) {
            songs.addAll(fullList);
        } else {
            for (Song song : fullList) {
                if (song.getTitle().toLowerCase().contains(query) ||
                        song.getArtist().toLowerCase().contains(query)) {
                    songs.add(song);
                }
            }
        }
        notifyDataSetChanged();
    }



    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        ImageView albumArt;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            artist = itemView.findViewById(R.id.textViewArtist);
            albumArt = itemView.findViewById(R.id.imageViewAlbum);
        }
    }
}

