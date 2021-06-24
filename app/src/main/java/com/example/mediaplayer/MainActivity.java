package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.Toast;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    public Button button_music;
    public Button button_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_music = (Button) findViewById(R.id.music_button);
        button_video = (Button) findViewById(R.id.video_button);
        runtimePermission();
    }

    public void openMusic()
    {
        Intent intent = new Intent(this, MusicActivity.class);
        startActivity(intent);
    }

    public void openVideo()
    {
        Intent intent = new Intent(this, InternetVideoActivity.class);
        startActivity(intent);
    }

    public void showMessage()
    {
        Toast.makeText(this, "Нужно разрешение для доступа к файлам", Toast.LENGTH_SHORT).show();
    }

    public void runtimePermission()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        button_music.setOnClickListener(v -> openMusic());
                        button_video.setOnClickListener(v -> openVideo());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        button_music.setOnClickListener(v -> {showMessage(); runtimePermission();});
                        button_video.setOnClickListener(v -> {showMessage(); runtimePermission();});
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
}