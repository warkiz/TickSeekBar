package com.warkiz.tickseekbar;

/**
 * created by zhuangguangquan on 2018/6/6
 */

public interface TickMarkType {
    /**
     * don't show the tickMarks
     */
    int NONE = 0;
    /**
     * show tickMarks shape as regular oval
     */
    int OVAL = 1;
    /**
     * show tickMarks shape as regular square
     */
    int SQUARE = 2;
    /**
     * show tickMarks shape as vertical line , line'size is 2 dp.
     */
    int DIVIDER = 3;

    /**
     * show tickMarks shape as vertical line below barr , line'size is 2 dp.
     */
    int DIVIDER_BELOW = 4;


}