package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 作业2：一个抖音笔试题：统计页面所有view的个数
 * Tips：ViewGroup有两个API
 * {@link android.view.ViewGroup #getChildAt(int) #getChildCount()}
 * 用一个TextView展示出来
 */
public class Exercises2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final TextView tv1 =findViewById(R.id.tv1);
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_chatroom, null);
        tv1.setText(""+getViewCount(inflate));
    }

    public static int getViewCount(View view) {
        int num = 0;
        if(view instanceof ViewGroup){   //判断view是不是ViewGroup的实例
            num++;
            for(int i = 0;i < ((ViewGroup)view).getChildCount();i++) {
                num += getViewCount(((ViewGroup) view).getChildAt(i));
            }
        }
        else{
            num++;
        }
        return num;
    }
}
