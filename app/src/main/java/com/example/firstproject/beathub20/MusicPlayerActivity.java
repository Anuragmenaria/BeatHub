package com.example.firstproject.beathub20;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import com.google.firebase.Timestamp;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewArtist, textViewCurrentTime, textViewTotalTime;
    private ImageView imageViewAlbum;
    private ImageButton buttonPlayPause, buttonNext, buttonPrev;
    private SeekBar seekBar, seekBarVolume;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    private String title, artist, audioUrl, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // Initialize views
        textViewTitle = findViewById(R.id.textViewPlayerTitle);
        textViewArtist = findViewById(R.id.textViewPlayerArtist);
        imageViewAlbum = findViewById(R.id.imageViewPlayerAlbum);
        buttonPlayPause = findViewById(R.id.buttonPlayPause);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrev = findViewById(R.id.buttonPrev);
        seekBar = findViewById(R.id.seekBar);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        textViewCurrentTime = findViewById(R.id.textViewCurrentTime);
        textViewTotalTime = findViewById(R.id.textViewTotalTime);

        // Get data from Intent
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        artist = intent.getStringExtra("artist");
        audioUrl = intent.getStringExtra("audioUrl");
        imageUrl = intent.getStringExtra("imageUrl");

        MiniPlayerInfo.setSong(title, artist, imageUrl);


        // Set text and image
        textViewTitle.setText(title);
        textViewArtist.setText(artist);
        Glide.with(this).load(imageUrl).placeholder(R.drawable.placeholder_album).into(imageViewAlbum);

        // MediaPlayer setup
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(mp -> {
            mediaPlayer.start();
            buttonPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            seekBar.setMax(mediaPlayer.getDuration());
            textViewTotalTime.setText(formatTime(mediaPlayer.getDuration()));

            updateSeekBar = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        textViewCurrentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                        handler.postDelayed(this, 1000);
                    }
                }
            };
            handler.post(updateSeekBar);
        });

        // SeekBar change
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Volume control
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarVolume.setMax(maxVolume);
        seekBarVolume.setProgress(currentVolume);

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Play/Pause toggle
        buttonPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                buttonPlayPause.setImageResource(android.R.drawable.ic_media_play);
            } else {
                mediaPlayer.start();
                buttonPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });

        // Dummy next/prev (can implement playlist later)
        buttonNext.setOnClickListener(v -> {
            // Implement next song logic
        });

        buttonPrev.setOnClickListener(v -> {
            // Implement previous song logic
        });
    }

    private String formatTime(int millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
