package com.jession_ding.example.project2048demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ddd.eee.fff.nm.bn.BannerManager;
import ddd.eee.fff.nm.bn.BannerViewListener;

public class OptionsActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "OptionsActivity";
    private int lineNumber;
    private int targetScore;
    private SharedPreferences sp;
    private Button bt_optionsactivity_setlines;
    private Button bt_optionsactivity_settargetgoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        sp = getSharedPreferences("info", MODE_PRIVATE);
        lineNumber = sp.getInt("lineNumber", 4);
        targetScore = sp.getInt("targetScore", 2048);

        bt_optionsactivity_setlines = (Button) findViewById(R.id.bt_optionsactivity_setlines);
        bt_optionsactivity_settargetgoal = (Button) findViewById(R.id.bt_optionsactivity_settargetgoal);
        Button bt_optionsactivity_back = (Button) findViewById(R.id.bt_optionsactivity_back);
        Button bt_optionsactivity_done = (Button) findViewById(R.id.bt_optionsactivity_done);
        // 获取广告条
        View bannerView = BannerManager.getInstance(this)
                .getBannerView(this, new BannerViewListener() {
                    @Override
                    public void onRequestSuccess() {
                        Log.i(TAG,"onRequestSuccess==>>");
                    }

                    @Override
                    public void onSwitchBanner() {
                        Log.i(TAG,"onSwitchBanner==>>");
                    }

                    @Override
                    public void onRequestFailed() {
                        Log.i(TAG,"onRequestFailed==>>");
                    }
                });

        // 获取要嵌入广告条的布局
        LinearLayout bannerLayout = (LinearLayout) findViewById(R.id.ll_banner);
        // 将广告条加入到布局中
        bannerLayout.addView(bannerView);

        bt_optionsactivity_setlines.setText(lineNumber + "");
        bt_optionsactivity_settargetgoal.setText(targetScore + "");

        bt_optionsactivity_setlines.setOnClickListener(this);
        bt_optionsactivity_settargetgoal.setOnClickListener(this);
        bt_optionsactivity_back.setOnClickListener(this);
        bt_optionsactivity_done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_optionsactivity_setlines: {
                setLines();
                break;
            }
            case R.id.bt_optionsactivity_settargetgoal: {
                setTargetGoal();
                break;
            }
            case R.id.bt_optionsactivity_back: {
                back();
                break;
            }
            case R.id.bt_optionsactivity_done: {
                done();
                break;
            }
        }
    }

    private void back() {
        finish();
    }

    private void done() {
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("lineNumber", lineNumber);
        edit.putInt("targetScore", targetScore);
        edit.commit();
        //Toast.makeText(this, lineNumber + "==" + targetScore, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("lineNumber", lineNumber);
        intent.putExtra("targetScore", targetScore);
        setResult(200, intent);
        finish();
    }

    private int targetWhich = 0;

    private void setTargetGoal() {
        final String[] target = {"1024", "2048", "4096"};
        new AlertDialog.Builder(this)
                .setTitle("徒儿，要设置游戏的目标分数吗？调高啊，调高好玩")
                .setSingleChoiceItems(target, getTargetGoal(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        targetWhich = which;
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        targetScore = Integer.parseInt(target[targetWhich]);
                        bt_optionsactivity_settargetgoal.setText(targetScore + "");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        targetWhich = 0;
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private int choicesWhich = 0;

    private void setLines() {
        final String[] choices = {"4", "5", "6"};
        new AlertDialog.Builder(this)
                .setTitle("徒儿，要设置棋盘的行数吗？调低啊，调低好玩")
                .setSingleChoiceItems(choices, getLineNumber(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choicesWhich = which;
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lineNumber = Integer.parseInt(choices[choicesWhich]);
                        bt_optionsactivity_setlines.setText(lineNumber + "");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choicesWhich = 0;
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public int getLineNumber() {
        if (lineNumber == 4) {
            choicesWhich = 0;
            return 0;
        } else if (lineNumber == 5) {
            choicesWhich = 1;
            return 1;
        } else {
            choicesWhich = 2;
            return 2;
        }
    }

    public int getTargetGoal() {
        if (targetScore == 1024) {
            targetWhich = 0;
            return 0;
        } else if (targetScore == 2048) {
            targetWhich = 1;
            return 1;
        } else {
            targetWhich = 2;
            return 2;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 展示广告条窗口的 onDestroy() 回调方法中调用
        BannerManager.getInstance(this).onDestroy();
    }
}