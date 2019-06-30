package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int num = 0;
    boolean ok = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        final TextView tv1 = findViewById(R.id.tv1);
        final Switch sw1 = findViewById(R.id.sw1);

        btn1.setOnClickListener(new View.OnClickListener(){
            public void  onClick(View v){
                num = ok ? num + 1 : num;
                tv1.setText(String.format("number is %d",num));
                Log.d("add","add succeed");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            public void  onClick(View v){
                num = ok? 0 : num;
                tv1.setText(String.format("number is %d",num));
                Log.d("clear","clear succeed");
            }
        });

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sw1.setText("数字可更改");
                    ok = true;
                }
                else{
                    sw1.setText("数字不可更改");
                    ok = false;
                }
            }
        });
    }
}
