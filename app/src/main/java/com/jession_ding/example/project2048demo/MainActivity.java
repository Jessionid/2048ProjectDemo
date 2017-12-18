package com.jession_ding.example.project2048demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.jession_ding.example.project2048demo.view.GameView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout rl_mainactivity_center = (RelativeLayout) findViewById(R.id.rl_mainactivity_center);
        GameView gameView = new GameView(this);
        rl_mainactivity_center.addView(gameView);

/*        GridLayout gl_mainactivity_container = (GridLayout) findViewById(R.id.gl_mainactivity_container);
        gl_mainactivity_container.setRowCount(4);
        gl_mainactivity_container.setColumnCount(4);

        //求出手机屏幕的宽度，除以几个格子，得出每个格子的大小
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int windowWidth = metrics.widthPixels;
        Log.i(TAG,windowWidth + "");

        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
               *//* //封装的思想：
                MyTextView textView = new MyTextView(this);
                textView.setText(1*i+j + "");
                textView.setTextColor(Color.BLUE);
                textView.setGravity(Gravity.CENTER);
                gl_mainactivity_container.addView(textView,windowWidth/4,windowWidth/4);*//*
                NumberItem numberItem = new NumberItem(this);
                gl_mainactivity_container.addView(numberItem,windowWidth/4,windowWidth/4);
            }
        }*/
    }
}