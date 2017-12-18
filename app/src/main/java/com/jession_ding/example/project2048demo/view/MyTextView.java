package com.jession_ding.example.project2048demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Jession Ding
 * created at  2017/12/15 10:57
 * Description 自定义 TextView
 */
public class MyTextView extends TextView{
    //new TextView 的时候默认调用
    public MyTextView(Context context) {
        super(context);
    }
    //在 XML 布局中，如果有 TextView 控件，则系统默认调用这个构造，
    //AttributeSet 为此控件的属性集合
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //根据该控件的一些文字，背景颜色，把控件画在屏幕上
    @Override
    protected void onDraw(Canvas canvas) {
        //在显示文字之前，画一个红色边框
        Paint paint = new Paint();
        paint.setColor(Color.RED);  //红色
        paint.setStyle(Paint.Style.STROKE); //空心
        paint.setStrokeWidth(3);    //宽度
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint);

        //系统的 TextView，会去做原本 TextView 应该显示的东西
        super.onDraw(canvas);
    }
}
