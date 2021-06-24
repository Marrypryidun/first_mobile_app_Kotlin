package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {
    ImageView play, next, prev, stop;
    TextView txtname, txtstart, txtend;
    SeekBar seekMusic;

    String sname;
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateSeekbar;
    Bundle bundle;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        txtname = findViewById(R.id.txtsong);
        txtstart = findViewById(R.id.txtstart);
        txtend = findViewById(R.id.txtend);
        seekMusic = findViewById(R.id.seekbar);

        i = getIntent();
        bundle = i.getExtras();
        position = bundle.getInt("position", 0);
        String songName = i.getStringExtra("songname");
        initializePlayer(position);

        if (mediaPlayer.isPlaying())
            play.setBackgroundResource(R.drawable.play);

        play.setBackgroundResource(R.drawable.pause);

        play.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                play.setBackgroundResource(R.drawable.play);
                mediaPlayer.pause();
            } else {
                play.setBackgroundResource(R.drawable.pause);
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(mediaPlayer -> next.performClick());

        next.setOnClickListener(view -> {
            position = ((position + 1) % mySongs.size());
            initializePlayer(position);
            play.setBackgroundResource(R.drawable.pause);
        });

        prev.setOnClickListener(view -> {
            position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
            initializePlayer(position);
            play.setBackgroundResource(R.drawable.pause);
        });

        stop.setOnClickListener(view -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                Intent intent = new Intent(this, MusicActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initializePlayer(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        txtname.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        sname = mySongs.get(position).getName();
        txtname.setText(sname);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        changeSeekbar();
    }

    public void changeSeekbar() {
        updateSeekbar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentposition = 0;

                while (currentposition < totalDuration) {
                    try {
                        sleep(500);
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekMusic.setProgress(currentposition);
                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        seekMusic.setMax(mediaPlayer.getDuration());
        updateSeekbar.start();
        seekMusic.getProgressDrawable();
        seekMusic.getThumb();

        seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        txtend.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                txtstart.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public String createTime(int duration) {
        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        time += min + ":";

        if (sec < 10) {
            time += "0";
        }
        time += sec;

        return time;
    }
}