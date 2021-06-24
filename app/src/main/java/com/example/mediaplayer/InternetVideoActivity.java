package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Objects;

public class InternetVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("VideoPlayer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_internet_video);

        String videoSource = "https://sites.google.com/site/androidexample9/download/RunningClock.mp4";

        VideoView videoView = (VideoView) findViewById(R.id.videoview);


        videoView.setVideoURI(Uri.parse(videoSource));
        //videoView.setVideoPath(videoSource);
        videoView.setMediaController(new MediaController(this));

        videoView.setOnCompletionListener(myVideoViewCompletionListener);
        videoView.setOnPreparedListener(MyVideoViewPreparedListener);
        videoView.setOnErrorListener(myVideoViewErrorListener);

        videoView.requestFocus();
        videoView.start();
    }

    MediaPlayer.OnCompletionListener myVideoViewCompletionListener
            = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer arg0) {
            Toast.makeText(getApplicationContext(),
                    "End of Video",
                    Toast.LENGTH_LONG).show();
        }
    };

    MediaPlayer.OnPreparedListener MyVideoViewPreparedListener
            = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer arg0) {
            Toast.makeText(getApplicationContext(),
                    "Media file is loaded and ready to go",
                    Toast.LENGTH_LONG).show();

        }
    };

    MediaPlayer.OnErrorListener myVideoViewErrorListener
            = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
            Toast.makeText(getApplicationContext(),
                    "Error!!!",
                    Toast.LENGTH_LONG).show();
            return true;
        }
    };
}