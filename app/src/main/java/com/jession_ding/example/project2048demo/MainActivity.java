package com.jession_ding.example.project2048demo;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jession_ding.example.project2048demo.view.GameView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private TextView tv_mainactivity_scorepoint;
    private SharedPreferences sp;
    private TextView tv_mainactivity_recordpoint;
    private Button bt_mainactivity_revert;
    private Button bt_mainactivity_restart;
    private Button bt_mainactivity_options;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //先拿SharedPreferences
        sp = getSharedPreferences("info", MODE_PRIVATE);
        //打开界面进来
        int recordScoreSaved = sp.getInt("recordScore", 0); //得到

        RelativeLayout rl_mainactivity_center = (RelativeLayout) findViewById(R.id.rl_mainactivity_center);
        tv_mainactivity_scorepoint = (TextView) findViewById(R.id.tv_mainactivity_scorepoint);
        tv_mainactivity_recordpoint = (TextView) findViewById(R.id.tv_mainactivity_recordpoint);
        bt_mainactivity_revert = (Button) findViewById(R.id.bt_mainactivity_revert);
        bt_mainactivity_restart = (Button) findViewById(R.id.bt_mainactivity_restart);
        bt_mainactivity_options = (Button) findViewById(R.id.bt_mainactivity_options);

        bt_mainactivity_revert.setOnClickListener(this);
        bt_mainactivity_restart.setOnClickListener(this);
        bt_mainactivity_options.setOnClickListener(this);

        gameView = new GameView(this);
        rl_mainactivity_center.addView(gameView);

        tv_mainactivity_recordpoint.setText(recordScoreSaved + "");
    }

    public void setScore(int scorePoint) {
        tv_mainactivity_scorepoint.setText(scorePoint + "");
    }

    public void updateRecordScore(int recordScore) {
        int recordScoreSaved = sp.getInt("recordScore", 0); //得到
        if (recordScore > recordScoreSaved) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("recordScore", recordScore); //写入
            edit.commit();
            tv_mainactivity_recordpoint.setText(recordScore + "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_mainactivity_revert: {
                revert();
                break;
            }
            case R.id.bt_mainactivity_restart: {
                restart();
                break;
            }
            case R.id.bt_mainactivity_options: {
                options();
                break;
            }
        }
    }

    private void revert() {
        new AlertDialog.Builder(this)
                .setTitle("重新开始")
                .setMessage("确定要撤销上一步吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.revert();
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }

    private void restart() {
        new AlertDialog.Builder(this)
                .setTitle("重新开始")
                .setMessage("确定要重新开始新一局吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.restart();
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }

    private void options() {

    }
}