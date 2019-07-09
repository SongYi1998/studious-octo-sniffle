package com.bytedance.android.lesson.restapi.solution;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Intent intent=getIntent();
        String url=intent.getStringExtra("openurl");
        Uri uri=Uri.parse(url);
        final VideoView videoView=findViewById(R.id.videoView);
        videoView.setVideoPath(url);
        videoView.start();
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (videoView.isPlaying()){
                    videoView.pause();
                }
                else{
                    videoView.start();
                }
            }
        });
    }
}
