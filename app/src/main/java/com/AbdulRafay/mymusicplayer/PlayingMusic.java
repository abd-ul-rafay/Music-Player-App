package com.AbdulRafay.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.AbdulRafay.mymusicplayer.databinding.ActivityPlayingMusicBinding;

import java.io.File;
import java.util.ArrayList;

public class PlayingMusic extends AppCompatActivity {

    ActivityPlayingMusicBinding binding;
    Thread thread;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        thread.interrupt();
    }

    private ArrayList<File> songs;
    private TextView textView;
    private MediaPlayer mediaPlayer;
    private int position;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MyMusicPlayer);


        binding = ActivityPlayingMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textView = (TextView) findViewById(R.id.textViewSongName);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        songs = (ArrayList) bundle.getParcelableArrayList("MySongsList");

        position = intent.getIntExtra("Position", 0);

        textView.setText(intent.getStringExtra("CurrentSong"));
        binding.textViewSongName.setSelected(true);

        uri = Uri.parse(songs.get(position).toString());

        try {
            mediaPlayer = MediaPlayer.create(this, uri);
            mediaPlayer.start();
        } catch (Exception e) {
            Toast.makeText(this, "We can't play music", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        binding.seekBar.setMax(mediaPlayer.getDuration());

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(binding.seekBar.getProgress());
            }
        });

        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    int currentPosition = 0;
                    while (currentPosition < mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        binding.seekBar.setProgress(currentPosition);

                        sleep(750);
//
//                        if (currentPosition == mediaPlayer.getDuration()) {
//                            currentPosition = 0;
//                            mediaPlayer.start();
//                            binding.seekBar.setProgress(0);
//                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }; thread.start();

        binding.imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    binding.imageButton2.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                } else {
                    binding.imageButton2.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });

        binding.imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != 0) {
                    position -= 1;
                } else {
                    position = (songs.size() - 1);
                }

                textView = (TextView) findViewById(R.id.textViewSongName);
                textView.setText(intent.getStringExtra("CurrentSong"));

                uri = Uri.parse(songs.get(position).toString());

                textView.setText(songs.get(position).getName().toString());

                mediaPlayer.stop();
                mediaPlayer.release();
//                mediaPlayer = null;

                try {
                    mediaPlayer = MediaPlayer.create(PlayingMusic.this, uri);
                    mediaPlayer.start();
                } catch (Exception e) {
                    Toast.makeText(PlayingMusic.this, "We can't play music", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                binding.seekBar.setMax(mediaPlayer.getDuration());
            }
        });

        binding.imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != (songs.size() - 1)) {
                    position += 1;
                } else {
                    position = 0;
                }

                textView = (TextView) findViewById(R.id.textViewSongName);
                textView.setText(intent.getStringExtra("CurrentSong"));

                textView.setText(songs.get(position).getName().toString());

                uri = Uri.parse(songs.get(position).toString());

                mediaPlayer.stop();
                mediaPlayer.release();
//                mediaPlayer = null;

                try {
                    mediaPlayer = MediaPlayer.create(PlayingMusic.this, uri);
                    mediaPlayer.start();
                } catch (Exception e) {
                    Toast.makeText(PlayingMusic.this, "We can't play music", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                binding.seekBar.setMax(mediaPlayer.getDuration());
            }
        });

    }
}
