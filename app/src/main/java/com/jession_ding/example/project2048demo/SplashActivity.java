package com.jession_ding.example.project2048demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import ddd.eee.fff.AdManager;
import ddd.eee.fff.nm.sp.SplashViewSettings;
import ddd.eee.fff.nm.sp.SpotListener;
import ddd.eee.fff.nm.sp.SpotManager;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RelativeLayout rl_splashactivity_top = (RelativeLayout) findViewById(R.id.rl_splashactivity_top);
        //初始化有米 SDK
        //appId:88de17839c0ce64f  appSecret:f26a758fbba2bb94
        //插屏广告  上传到应用市场，关闭有米广告 SDK 的 Log，把 true 改成 false
        AdManager.getInstance(this).init("88de17839c0ce64f", "f26a758fbba2bb94", false);

        SplashViewSettings splashViewSettings = new SplashViewSettings();
        splashViewSettings.setTargetClass(MainActivity.class);
        // 使用默认布局参数
        splashViewSettings.setSplashViewContainer(rl_splashactivity_top);
        SpotManager.getInstance(this).showSplash(this,
                splashViewSettings, new SpotListener() {
                    @Override
                    public void onShowSuccess() {
                        Log.i(TAG, "插屏展示成功");
                    }

                    @Override
                    public void onShowFailed(int i) {
                        Log.i(TAG, "插屏展示失败");
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.i(TAG, "插屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean b) {
                        Log.i(TAG, "插屏被点击");
                    }
                });

        /*SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);
        SpotManager.getInstance(this).setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);
        SpotManager.getInstance(this).showSpot(this, new SpotListener() {
            @Override
            public void onShowSuccess() {
                Log.i(TAG,"插屏展示成功");
            }

            @Override
            public void onShowFailed(int i) {
                Log.i(TAG,"插屏展示失败");

            }

            @Override
            public void onSpotClosed() {
                Log.i(TAG,"插屏被关闭");

            }

            @Override
            public void onSpotClicked(boolean b) {
                Log.i(TAG,"插屏被点击");

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //三秒钟之后，跳入到 main 页面
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }).start();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 如果有需要，可以点击后退关闭插播广告。
        if (SpotManager.getInstance(this).isSpotShowing()) {
            SpotManager.getInstance(this).hideSpot();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 插屏广告
        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy==>");
        // 插屏广告
        SpotManager.getInstance(this).onDestroy();
    }
}