package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * 作业1：
 * 打印出Activity屏幕切换 Activity会执行什么生命周期？
 * onPause:
 * onStop:
 * onDestroy
 * onCreate:
 * onStart:
 * onResume:
 */
public class Exercises1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("123", "onCreate: ");
    }

    protected void onStart(){
        super.onStart();
        Log.i("123", "onStart: ");
    }

    protected void onResume(){
        super.onResume();
        Log.i("123", "onResume: ");
    }

    protected void onPause(){
        super.onPause();
        Log.i("123", "onPause: ");
    }

    protected void onStop(){
        super.onStop();
        Log.i("123", "onStop: ");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i("123", "onDestroy: ");
    }

}
