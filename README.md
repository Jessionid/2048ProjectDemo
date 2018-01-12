# 2048Numberpuzzlegame

> 一个小练习

### Part Ⅰ

1. `drawable`中`shape`和`selector`的使用及注意点

2. `GridLayout`的使用（两个方法或属性：`rowCount = "4"`和`columnCount = "4"`；`layout_rowSpan = "2"`、`layout_columnSpan = "2"`和`layout_gravity = "fill"`填充）

3. 一些基础的自定义控件（`MyTextView`继承自`TextView`；`NumberItem`继承自`FrameLayout`；`GameView`继承自`GridLayout`），在各自的控件中，进行初始化。减少 `mainactivity` 中的代码

4. 方法的设置与传递

5. 使用`ArrayList`维护一个`Point`类，其中`Point` 类中，包含一个坐标`（x,y）`；`NumberItem[][]`数组，包含所有的`NumberItem` 的引用。数组的行和列与`Point`类中的`(x,y)`对应


### Part Ⅱ

滑动之后之数字显示的算法罗列：（滑动完之后，能合并的合并，不能合并的结尾处填 0）

```
1. 先遍历结果集，做相应的操作
2. 把处理的数据，填放在每一行
3. 剩余部分，去补 0
4. 清空链表，并随机产生棋盘
```

- 向上滑

部分代码：
```java
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
```

图解示意：
![上滑](http://wx1.sinaimg.cn/mw690/89195e42gy1fndwjohhqqj20jk08pglm.jpg)

- 向下滑
部分代码：
```java
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
```

图解示意：
![下滑](http://wx3.sinaimg.cn/mw690/89195e42gy1fndwjlpiibj20le0900sr.jpg)

- 向左滑

部分代码：
```java
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
```

图解示意：
![左滑](http://wx4.sinaimg.cn/mw690/89195e42gy1fndwjm5dqpj20jp08emx5.jpg)

- 向右滑

部分代码：
```java
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
```

图解示意：
![右滑](http://wx1.sinaimg.cn/mw690/89195e42gy1fndwjo3021j20m408a3yj.jpg)


### Part Ⅲ

OptionsActivity 之设置页面的界面搭建和功能书写，`SharedPreferences`的使用


### Part Ⅳ

集成有米广告 SDK，配置流程如下：

http://blog.csdn.net/jession_ding/article/details/78942129