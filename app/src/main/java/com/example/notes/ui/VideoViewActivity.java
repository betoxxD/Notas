package com.example.notes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.notes.R;

import java.io.File;

public class VideoViewActivity extends AppCompatActivity {

    MediaSessionCompat mediaSession;
    PlaybackStateCompat.Builder stateBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        String res = extras.getString("videoPath");
        File videoFile = new File(res);
        VideoView videoView = findViewById(R.id.videoViewActivity);
        videoView.setVideoPath(res);
        videoView.start();
        if (videoFile.exists()) {

        }
    }
}