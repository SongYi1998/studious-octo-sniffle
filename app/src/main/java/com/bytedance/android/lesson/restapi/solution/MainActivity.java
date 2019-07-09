package com.bytedance.android.lesson.restapi.solution;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int SELF_CAMERA_AUDIO_SD_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Solution2C1(View view) {
        Intent intent=new Intent(MainActivity.this, Solution2C2Activity.class);
        intent.putExtra("post",false);
        startActivity(intent);
    }

    public void Solution2C2(View view) {
        Intent intent=new Intent(MainActivity.this, Solution2C2Activity.class);
        intent.putExtra("post",true);
        startActivity(intent);

    }

    public void Button(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
        ){
            String[] permission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(this,permission,SELF_CAMERA_AUDIO_SD_REQUEST_CODE);
        }
        else{
            startActivity(new Intent(MainActivity.this, CustomCameraActivity.class));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode) {
            case SELF_CAMERA_AUDIO_SD_REQUEST_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED|| grantResults[2]!=PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请给予相关权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
