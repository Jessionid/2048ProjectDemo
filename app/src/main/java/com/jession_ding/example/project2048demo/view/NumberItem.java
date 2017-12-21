package com.jession_ding.example.project2048demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author Jession Ding
 *         created at  2017/12/15 11:43
 *         Description 作为棋盘
 */
public class NumberItem extends FrameLayout {
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
        params.setMargins(5, 5, 5, 5);
        //要把textview增加到FrameLayout，所以必须给一个FrameLayout的LayoutParams
        addView(textView, params);
    }

    public void setNumber(int number) {
        this.number = number;
        if (number == 0) {
            textView.setText("");
        } else {
            textView.setText(number + "");
        }
        switch (number) {
            case 0: {
                textView.setBackgroundColor(0xFFBFBFBF);
                break;
            }
            case 2: {
                textView.setBackgroundColor(0xFFFFF5EE);
                break;
            }
            case 4: {
                textView.setBackgroundColor(0xFFFFEC8B);
                break;
            }
            case 8: {
                textView.setBackgroundColor(0xFFFFE4C4);
                break;
            }
            case 16: {
                textView.setBackgroundColor(0xFFFFDAB9);
                break;
            }
            case 32: {
                textView.setBackgroundColor(0xFFFFC125);
                break;
            }
            case 64: {
                textView.setBackgroundColor(0xFFFFB6C1);
                break;
            }
            case 128: {
                textView.setBackgroundColor(0xFFFFA500);
                break;
            }
            case 256:
                textView.setBackgroundColor(0xFFFF83FA);
                break;
            case 512:
                textView.setBackgroundColor(0xFFFF7F24);
                break;
            case 1024:
                textView.setBackgroundColor(0xFFFF6A6A);
                break;
            case 2048:
                textView.setBackgroundColor(0xFFFF1493);
                break;
            case 4096:
                textView.setBackgroundColor(0xFFFF3030);
                break;
        }
    }

    public int getNumber() {
        return this.number;
    }

    public NumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}