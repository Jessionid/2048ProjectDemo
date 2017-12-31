package com.jession_ding.example.project2048demo.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.GridLayout;

import com.jession_ding.example.project2048demo.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jession Ding
 *         created at  2017/12/15 14:02
 *         Description 自定义 GridLayout
 */
public class GameView extends GridLayout {
    private static final String TAG = "GameView";
    private int rowNumber = 4;
    private int columnNumber = 4;
    private int targetScore = 32;
    private int currentScore = 0;
    private Context context;
    private MainActivity activity;
    private boolean canRevert = false;

    //记录当前棋盘上，空白位置的一个数组
    //放一个（x,y），表示空白的棋盘的位置，表示行和列
    List<Point> blankItemList;

    NumberItem[][] itemMatrix;
    int[][] historyMatrix;

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
        this.context = context;
        this.activity = (MainActivity) context;
        SharedPreferences sp = context.getSharedPreferences("info", context.MODE_PRIVATE);
        int lineNumber = sp.getInt("lineNumber", 4);
        int targetScore = sp.getInt("targetScore", 2048);
        rowNumber = lineNumber;
        columnNumber = lineNumber;
        this.targetScore = targetScore;
        initView(context);
    }

    private void initView(Context context) {
        //干掉之前的控件
        removeAllViews();
        setRowCount(rowNumber); //行
        setColumnCount(columnNumber); //列
        itemMatrix = new NumberItem[rowNumber][columnNumber];
        historyMatrix = new int[rowNumber][columnNumber];
        blankItemList = new ArrayList<Point>();
        updateCurrentScore(0);
        //求出手机屏幕的宽度，除以几个格子，得出每个格子的大小
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int windowWidth = metrics.widthPixels;
        //Log.i(TAG, windowWidth + "");
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
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
                addView(numberItem, windowWidth / rowNumber, windowWidth / rowNumber);
            }
        }
        //随机找个位置，去显示一个不为0的数字
        addRandomNumber();
        addRandomNumber();
    }

    private void addRandomNumber() {
        updateBlankNumberList();
        //随机产生一个 i，随机产生一个 j，i 0-3 j 0-3
        //要求是在棋盘上，没有被占用的地方随机找一个位置
        int blankLength = blankItemList.size(); //16
        Log.i(TAG, "blankLength = " + blankLength);
        if (blankLength != 0) {
            int randomLocation = (int) Math.floor(Math.random() * blankLength);    //向下取整
            Log.i(TAG, "randomLocation = " + randomLocation);
            Point point = blankItemList.get(randomLocation);
            Log.i(TAG, "point.x = " + point.x + ";" + "point.y = " + point.y);
            NumberItem numberItem = itemMatrix[point.x][point.y];
            numberItem.setNumber(2);
        }
    }

    //每次去找一个随机的产生数字的位置之前，需要去更新一下产生的随机数的范围
    private void updateBlankNumberList() {
        blankItemList.clear();  //重新置空
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                int number = itemMatrix[i][j].getNumber();
                if (number == 0) {
                    blankItemList.add(new Point(i, j));
                }
            }
        }
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //这里会调用 OnTouchListener 的 onTouch callback
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //Log.i(TAG, "ACTION_DOWN");
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                //Log.i(TAG, "ACTION_UP");
                endX = (int) event.getX();
                endY = (int) event.getY();
                judgeDirection();
                updateCurrentScore(currentScore);
                handleNext(judgeIsOver());
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //Log.i(TAG, "ACTION_MOVE");
                break;
            }
        }
        //返回 false，表示后续事件不需要处理，则系统不会将后续的事件报给当前的控件
        //这里需要返回 true，表示当前控件需要处理
        return true;    //super.onTouchEvent(event);
    }

    private void updateCurrentScore(int currentScore) {
        this.currentScore = currentScore;   //每次重开一局，currentScore 也要置 0
        activity.setScore(currentScore);
    }

    //去处理下一步应该怎么做
    private void handleNext(int over) {
        if (over == 0) {
            //提示用户已经赢了
            activity.updateRecordScore(currentScore);
            new AlertDialog.Builder(context)
                    .setTitle("恭喜您")
                    .setMessage("徒儿，挺厉害的啊，要不再来一局？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();  //关闭当前activity
                        }
                    })
                    .show();
        } else if (over == -1) {
            //提示用户 game over
            new AlertDialog.Builder(context)
                    .setTitle("很遗憾")
                    .setMessage("啊哈哈，徒儿，挑战失败啦,再来一局吗？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();  //关闭当前activity
                        }
                    })
                    .show();
        } else {
            //可以正常继续
            //canSlide = true;
            //若上下左右滑动，没有合并，则不产生新的item
            if (canSlide) {
                addRandomNumber();  //滑动之后，增加一个棋盘
                canSlide = false;
                //每次滑动完之后，进行判断
                int judge = judgeIsOver();
                if (judge == 0) {
                    //提示用户已经赢了
                    activity.updateRecordScore(currentScore);
                    new AlertDialog.Builder(context)
                            .setTitle("恭喜您")
                            .setMessage("徒儿，挺厉害的啊，要不再来一局？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    restart();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();  //关闭当前activity
                                }
                            })
                            .show();
                }
                if (judge == -1) {
                    //提示用户 game over
                    new AlertDialog.Builder(context)
                            .setTitle("很遗憾")
                            .setMessage("啊哈哈，徒儿，挑战失败啦,再来一局吗？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    restart();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();  //关闭当前activity
                                }
                            })
                            .show();
                }
            }
        }
    }

    public void restart() {
        canRevert = false;
        init(activity);
    }

    // -1 fail (over) 0 win 1 not over
    private int judgeIsOver() {
        int over = 1;
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                if (itemMatrix[i][j].getNumber() == targetScore) {
                    return 0;   //成功
                }
            }
        }
        //查看水平方向，有没有相同的数字
        for (int i = 0; i < rowNumber; i++) {
            int preNumber = -1;
            for (int j = 0; j < columnNumber; j++) {
                int currentNumber = itemMatrix[i][j].getNumber();
                if (preNumber != -1 && currentNumber == preNumber) {
                    return over;
                } else {
                    preNumber = currentNumber;
                }
            }
        }
        //查看竖直方向，有没有相同的数字
        for (int j = 0; j < columnNumber; j++) {
            int preNumber = -1;
            for (int i = 0; i < rowNumber; i++) {
                int currentNumber = itemMatrix[i][j].getNumber();
                if (preNumber != -1) {
                    if (currentNumber == preNumber) { //2和2
                        return over;
                    }
                    // 2和4
                    preNumber = currentNumber;
                } else {
                    preNumber = currentNumber;
                }
            }
        }
        updateBlankNumberList();
        if (blankItemList.size() == 0) {
            return -1;  //失败
        }
        return over;
    }

    private void judgeDirection() {
        int dX = Math.abs(endX - startX); //横坐标的绝对值
        int dY = Math.abs(endY - startY); //纵坐标的绝对值
        savedToHistory();
        if (dX > dY) { //左右滑动
            if (endX > startX) {   //右滑
                slideRight();
            } else {    //左滑
                slideLeft();
            }
        } else {    //上下滑动
            if (endY > startY) {   //下滑
                slideDown();
            } else {    //上滑
                slideUp();
            }
        }
    }

    boolean canSlide = false;

    private void slideDown() {
        Log.i(TAG, "下滑");
        List<Integer> cacuList = new ArrayList<>(columnNumber);
        for (int j = 0; j < columnNumber; j++) {
            int preNumber = -1;
            for (int i = rowNumber - 1; i >= 0; i--) {
                int currentItemNumber = itemMatrix[i][j].getNumber();
                if (currentItemNumber == 0) {  //判断并放入 cacuList 中
                    continue;
                } else {    //当前数字不为0
                    if (preNumber == currentItemNumber) {
                        cacuList.add(currentItemNumber * 2);
                        currentScore += currentItemNumber * 2;
                        //canSlide = true;
                        preNumber = -1;
                    } else {    //-1和2，2和4
                        if (preNumber == -1) {
                            //cacuList.add(currentItemNumber);
                            preNumber = currentItemNumber;
                        } else {    // 2和4
                            cacuList.add(preNumber);
                            preNumber = currentItemNumber;
                        }
                    }
                }
            }
            if (preNumber != -1) {
                cacuList.add(preNumber);
            }
            //先放数字，后放0
            for (int k = 0; k < cacuList.size(); k++) {
                int newNumber = cacuList.get(k);
                itemMatrix[rowNumber - 1 - k][j].setNumber(newNumber);
                canSlide = true;
            }
            for (int p = rowNumber - 1 - cacuList.size(); p >= 0; p--) {
                itemMatrix[p][j].setNumber(0);
            }
            cacuList.clear();
        }
    }

    private void slideUp() {
        Log.i(TAG, "上滑");
        ArrayList<Integer> cacuList = new ArrayList<Integer>(columnNumber);
        for (int j = 0; j < columnNumber; j++) {
            int preNumber = -1;
            for (int i = 0; i < rowNumber; i++) {
                int currentItemNumber = itemMatrix[i][j].getNumber();
                if (currentItemNumber == 0) {  //判断并放入 cacuList 中
                    continue;
                } else {    //当前数字不为0
                    if (currentItemNumber == preNumber) {
                        cacuList.add(currentItemNumber * 2);
                        currentScore += currentItemNumber * 2;
                        //canSlide = true;
                        preNumber = -1;
                    } else {    //-1和2，2和4
                        if (preNumber == -1) {
                            //cacuList.add(currentItemNumber);
                            preNumber = currentItemNumber;
                        } else {    // 2和4
                            cacuList.add(preNumber);
                            preNumber = currentItemNumber;
                        }
                    }
                }
            }
            if (preNumber != -1) {  // 这里又加了一次
                cacuList.add(preNumber);
            }
            //先放数字，后放0
            for (int k = 0; k < cacuList.size(); k++) {
                int newNumber = cacuList.get(k);
                //Log.i(TAG, "slideUp:itemMatrix[k][j].setNumber(newNumber)");
                itemMatrix[k][j].setNumber(newNumber);
                canSlide = true;
            }
            for (int p = cacuList.size(); p < rowNumber; p++) {
                //Log.i(TAG, "slideUp:itemMatrix[p][j].setNumber(0)");
                itemMatrix[p][j].setNumber(0);
            }
            cacuList.clear();
        }
    }

    private void slideLeft() {
        Log.i(TAG, "左滑");
        ArrayList<Integer> cacuList = new ArrayList<Integer>(columnNumber);
        for (int i = 0; i < rowNumber; i++) {
            int preNumber = -1; //当前item 的前一个 item 的数字
            for (int j = 0; j < columnNumber; j++) {
                int currentItemNumber = itemMatrix[i][j].getNumber();
                //若当前拿到的数字为 0，直接忽略掉
                if (currentItemNumber == 0) {
                    continue;
                } else {
                    //当前拿出来的 number 不是0
                    if (currentItemNumber == preNumber) {  //如果当前的 number 跟前一个一样的时候
                        cacuList.add(currentItemNumber * 2);
                        currentScore += currentItemNumber * 2;
                        //canSlide = true;
                        preNumber = -1;
                    } else {    //当前的 number 和前一个不一样
                        if (preNumber == -1) {  //若是前一个保持的是-1，则需要进一步去看一下这个数字
                            preNumber = currentItemNumber;
                        } else {    //前一个不是-1（这个和前一个不相等）
                            cacuList.add(preNumber);
                            preNumber = currentItemNumber;
                        }
                    }
                }
            }
            // 如果某一行只有最后一个 item 上有数字，那么会漏掉最后一个 item
            if (preNumber != -1) {
                cacuList.add(preNumber);
            }
            //计算的结果可能小于这一行的长度
            for (int k = 0; k < cacuList.size(); k++) {
                int newNumber = cacuList.get(k);
                itemMatrix[i][k].setNumber(newNumber);
                canSlide = true;
            }
            //剩下的部分，这一行应该填充0
            for (int p = cacuList.size(); p < columnNumber; p++) {
                itemMatrix[i][p].setNumber(0);
            }
            cacuList.clear();
        }
    }

    private void slideRight() {
        Log.i(TAG, "右滑");
        ArrayList<Integer> cacuList = new ArrayList<Integer>(columnNumber);
        for (int i = 0; i < rowNumber; i++) {
            int preNumber = -1;
            for (int j = columnNumber - 1; j >= 0; j--) {
                int currentItemNumber = itemMatrix[i][j].getNumber();
                //historyMatrix[i][j] = currentItemNumber;
                if (currentItemNumber == 0) {  //为 0 的情况
                    continue;
                } else {    //相等的情况
                    if (preNumber == currentItemNumber) {
                        cacuList.add(currentItemNumber * 2);
                        currentScore += currentItemNumber * 2;
                        //canSlide = true;
                        preNumber = -1;
                    } else {
                        if (preNumber == -1) {  // -1和其他数的情况
                            preNumber = currentItemNumber;
                        } else {    //2 和 4 的情况
                            cacuList.add(preNumber);
                            preNumber = currentItemNumber;
                        }
                    }
                }
            }
            if (preNumber != -1) {
                cacuList.add(preNumber);
            }
            //先放有数字的，再放置0
            for (int k = 0; k < cacuList.size(); k++) {
                int newNumber = cacuList.get(k);
                itemMatrix[i][columnNumber - 1 - k].setNumber(newNumber);
                canSlide = true;
            }
            for (int p = columnNumber - cacuList.size() - 1; p >= 0; p--) {
                itemMatrix[i][p].setNumber(0);
            }
            cacuList.clear();
        }
    }

    //回到上一次棋盘面的样子
    public void revert() {
        if (canRevert) {
            for (int i = 0; i < rowNumber; i++) {
                for (int j = 0; j < columnNumber; j++) {
                    itemMatrix[i][j].setNumber(historyMatrix[i][j]);
                }
            }
        }
    }

    private void savedToHistory() {
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                historyMatrix[i][j] = itemMatrix[i][j].getNumber();
            }
        }
        canRevert = true;
    }
}