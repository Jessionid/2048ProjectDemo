package com.jession_ding.example.project2048demo.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author Jession Ding
 * created at  2017/12/15 11:43
 * Description 作为棋盘
 */
public class NumberItem extends FrameLayout{
    private int number;
    private TextView textView;

    public NumberItem(Context context) {
        super(context);
        //初始化TextView
        init(context);
    }

    private void init(Context context) {
        textView = new TextView(context);
        textView.setText("");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        setNumber(0);
        textView.setBackgroundColor(0xFFBFBFBF);    //颜色两种表示方法
        //给每个TextView设置一个margin
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(5,5,5,5);
        //要把textview增加到FrameLayout，所以必须给一个FrameLayout的LayoutParams
        addView(textView,params);
    }
    public void setNumber(int number) {
        this.number = number;
        if(number == 2) {
            setText("2");
            textView.setBackgroundColor(Color.BLUE);
        }
    }
    public int getNumber() {
        return this.number;
    }
    public void setText(String numberString) {
        textView.setText(numberString);
    }
    public NumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}