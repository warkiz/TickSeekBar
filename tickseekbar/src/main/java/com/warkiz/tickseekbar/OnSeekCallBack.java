package com.warkiz.tickseekbar;

/**
 * call back progress
 * main destination: get the thumb text data.
 */
public interface OnSeekCallBack {

    /**
     * @param progress the seekbar progress
     * @return the thumb text .
     */
    String getThumbText(float progress);

}
