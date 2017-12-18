package com.jession_ding.example.project2048demo.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jession Ding
 * created at  2017/12/15 14:02
 * Description 自定义 GridLayout
 */
public class GameView extends GridLayout {
    private static final String TAG = "GameView";
    private int rowNumber = 4;
    private int columnNumber = 4;

    //记录当前棋盘上，空白位置的一个数组
    //放一个（x,y），表示空白的棋盘的位置，表示行和列
    List<Point> blankItemList;

    NumberItem[][] itemMatrix;

    public GameView(Context context) {
        super(context);
        //初始化，增加 NumberItem 的帧布局
        init(context);
    }
    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }
    private void init(Context context) {
        setRowCount(rowNumber); //行
        setColumnCount(columnNumber); //列
        itemMatrix = new NumberItem[rowNumber][columnNumber];
        blankItemList = new ArrayList<Point>();

        //求出手机屏幕的宽度，除以几个格子，得出每个格子的大小
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int windowWidth = metrics.widthPixels;
        Log.i(TAG,windowWidth + "");
        for(int i=0;i<rowNumber;i++) {
            for(int j=0;j<columnNumber;j++) {
/*                //封装的思想：
                MyTextView textView = new MyTextView(this);
                textView.setText(1*i+j + "");
                textView.setTextColor(Color.BLUE);
                textView.setGravity(Gravity.CENTER);
                gl_mainactivity_container.addView(textView,windowWidth/4,windowWidth/4);*/
                NumberItem numberItem = new NumberItem(context);
                itemMatrix[i][j] = numberItem;  //存放一个 NumberItem 的引用
                //初始化 blankItemList
                Point point = new Point();
                point.x = i;
                point.y = j;
                blankItemList.add(point);

                addView(numberItem,windowWidth/4,windowWidth/4);
            }
        }
        //随机找个位置，去显示一个不为0的数字
        addRandomNumber();
    }

    private void addRandomNumber() {
        //随机产生一个 i，随机产生一个 j，i 0-3 j 0-3
        //要求是在棋盘上，没有被占用的地方随机找一个位置
        int blankLength = blankItemList.size(); //16
        int randomLocation = (int) Math.floor(Math.random()*blankLength);    //向下取整
        Log.i(TAG,"randomLocation = " + randomLocation);
        Point point = blankItemList.get(randomLocation);
        Log.i(TAG,"point.x = " + point.x + ";" + "point.y = " + point.y);
        NumberItem numberItem = itemMatrix[point.x][point.y];
        //numberItem.setText("2");
        numberItem.setNumber(2);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
