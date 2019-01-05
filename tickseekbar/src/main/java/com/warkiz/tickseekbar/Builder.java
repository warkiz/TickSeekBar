package com.warkiz.tickseekbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * created by zhuangguangquan on 2018/6/6
 */

public class Builder {
    final Context context;
    //seek bar
    float max = 100;
    float min = 0;
    float progress = 0;
    boolean progressValueFloat = false;
    boolean seekSmoothly = false;
    boolean r2l = false;
    boolean userSeekable = true;
    boolean onlyThumbDraggable = false;
    //track
    int trackBackgroundSize;
    int trackBackgroundColor = Color.parseColor("#D7D7D7");
    int trackProgressSize;
    int trackProgressColor = Color.parseColor("#FF4081");
    boolean trackRoundedCorners = false;
    //thumbText
    int thumbTextColor = Color.parseColor("#FF4081");
    int thumbTextShow = TextPosition.NONE;
    //thumb
    int thumbSize;
    int thumbColor = Color.parseColor("#FF4081");
    boolean thumbAutoAdjust = true;
    ColorStateList thumbColorStateList = null;
    Drawable thumbDrawable = null;
    //tickTexts
    int tickTextsShow = TextPosition.NONE;
    int tickTextsColor = Color.parseColor("#FF4081");
    int tickTextsSize;
    String[] tickTextsCustomArray = null;
    Typeface tickTextsTypeFace = Typeface.DEFAULT;
    ColorStateList tickTextsColorStateList = null;
    //tickMarks
    int tickCount = 0;
    int showTickMarksType = TickMarkType.NONE;
    int tickMarksColor = Color.parseColor("#FF4081");
    int tickMarksSize;
    Drawable tickMarksDrawable = null;
    boolean tickMarksEndsHide = false;
    boolean tickMarksSweptHide = false;
    ColorStateList tickMarksColorStateList = null;
    public boolean clearPadding = false;

    Builder(Context context) {
        this.context = context;
        this.trackBackgroundSize = SizeUtils.dp2px(context, 2);
        this.trackProgressSize = SizeUtils.dp2px(context, 2);
        this.tickMarksSize = SizeUtils.dp2px(context, 10);
        this.tickTextsSize = SizeUtils.sp2px(context, 13);
        this.thumbSize = SizeUtils.dp2px(context, 14);
    }

    /**
     * call this to new an TickSeekBar
     *
     * @return TickSeekBar
     */
    public TickSeekBar build() {
        return new TickSeekBar(this);
    }

    /**
     * Set the upper limit of this seek bar's range.
     */
    public Builder max(float max) {
        this.max = max;
        return this;
    }

    /**
     * Set the  lower limit of this seek bar's range.
     */
    public Builder min(float min) {
        this.min = min;
        return this;
    }

    /**
     * Sets the current progress to the specified value.
     */
    public Builder progress(float progress) {
        this.progress = progress;
        return this;
    }

    /**
     * make the progress in float type. default in int type.
     *
     * @param isFloatProgress true for float progress,default false.
     */
    public Builder progressValueFloat(boolean isFloatProgress) {
        this.progressValueFloat = isFloatProgress;
        return this;
    }

    /**
     * seek continuously or discrete
     *
     * @param seekSmoothly true for seek continuously ignore having tickMarks.
     */
    public Builder seekSmoothly(boolean seekSmoothly) {
        this.seekSmoothly = seekSmoothly;
        return this;
    }

    /**
     * right to left,compat local problem.
     *
     * @param r2l true for local which read text from right to left
     */
    public Builder r2l(boolean r2l) {
        this.r2l = r2l;
        return this;
    }

    /**
     * seek bar has a default padding left and right(16 dp) , call this method to set both to zero.
     *
     * @param clearPadding true to clear the default padding, false to keep.
     * @return Builder
     */
    public Builder clearPadding(boolean clearPadding) {
        this.clearPadding = clearPadding;
        return this;
    }

    /**
     * prevent user from touching to seek or not
     *
     * @param userSeekable true user can seek.
     * @return
     */
    public Builder userSeekable(boolean userSeekable) {
        this.userSeekable = userSeekable;
        return this;
    }

    /**
     * user change the thumb's location by touching thumb,touching track will not worked.
     *
     * @param onlyThumbDraggable true for seeking only by drag thumb. default false;
     * @return
     */
    public Builder onlyThumbDraggable(boolean onlyThumbDraggable) {
        this.onlyThumbDraggable = onlyThumbDraggable;
        return this;
    }

    /**
     * set the seek bar's background track's Stroke Width
     *
     * @param trackBackgroundSize The dp size.
     */
    public Builder trackBackgroundSize(int trackBackgroundSize) {
        this.trackBackgroundSize = SizeUtils.dp2px(context, trackBackgroundSize);
        return this;
    }

    /**
     * set the seek bar's background track's color.
     *
     * @param trackBackgroundColor colorInt
     */
    public Builder trackBackgroundColor(@ColorInt int trackBackgroundColor) {
        this.trackBackgroundColor = trackBackgroundColor;
        return this;
    }

    /**
     * set the seek bar's progress track's Stroke Width
     *
     * @param trackProgressSize The dp size.
     */
    public Builder trackProgressSize(int trackProgressSize) {
        this.trackProgressSize = SizeUtils.dp2px(context, trackProgressSize);
        return this;
    }

    /**
     * set the seek bar's progress track's color.
     *
     * @param trackProgressColor colorInt
     */
    public Builder trackProgressColor(@ColorInt int trackProgressColor) {
        this.trackProgressColor = trackProgressColor;
        return this;
    }

    /**
     * call this method to show the seek bar's ends to square corners.default rounded corners.
     *
     * @param trackRoundedCorners false to show square corners.
     */
    public Builder trackRoundedCorners(boolean trackRoundedCorners) {
        this.trackRoundedCorners = trackRoundedCorners;
        return this;
    }

    /**
     * set the seek bar's thumb's text color.
     *
     * @param thumbTextColor colorInt
     */
    public Builder thumbTextColor(@ColorInt int thumbTextColor) {
        this.thumbTextColor = thumbTextColor;
        return this;
    }

    /**
     * call this method to show the text below thumb in one place,
     * the text will slide with the thumb.
     *
     * @param thumbTextPosition see{@link TextPosition}
     */
    public Builder thumbTextPosition(int thumbTextPosition) {
        this.thumbTextShow = thumbTextPosition;
        return this;
    }

    /**
     * set the seek bar's thumb's color.
     *
     * @param thumbColor colorInt
     */
    public Builder thumbColor(@ColorInt int thumbColor) {
        this.thumbColor = thumbColor;
        return this;
    }

    /**
     * adjust thumb to tick position auto after touch up
     *
     * @param autoAdjust true to adjust thumb to tick position auto after touch up
     */
    public Builder thumbAutoAdjust(boolean autoAdjust) {
        this.thumbAutoAdjust = autoAdjust;
        return this;
    }

    /**
     * set the seek bar's thumb's selector color.
     *
     * @param thumbColorStateList color selector
     * @return Builder
     * selector format like:
     */
    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:color="@color/colorAccent" android:state_pressed="true" />  <!--this color is for thumb which is at pressing status-->
    //<item android:color="@color/color_blue" />                                <!--for thumb which is at normal status-->
    //</selector>
    public Builder thumbColorStateList(@NonNull ColorStateList thumbColorStateList) {
        this.thumbColorStateList = thumbColorStateList;
        return this;
    }

    /**
     * set the seek bar's thumb's Width.will be limited in 30dp.
     *
     * @param thumbSize The dp size.
     */
    public Builder thumbSize(int thumbSize) {
        this.thumbSize = SizeUtils.dp2px(context, thumbSize);
        return this;
    }

    /**
     * call this method to custom the thumb showing drawable.
     *
     * @param thumbDrawable the drawable show as Thumb.
     */
    public Builder thumbDrawable(@NonNull Drawable thumbDrawable) {
        this.thumbDrawable = thumbDrawable;
        return this;
    }

    /**
     * call this method to custom the thumb showing drawable by selector Drawable.
     *
     * @param thumbStateListDrawable the drawable show as Thumb.
     * @return Builder
     * <p>
     * selector format:
     */
    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:drawable="Your drawableA" android:state_pressed="true" />  <!--this drawable is for thumb when pressing-->
    //<item android:drawable="Your drawableB" />  < !--for thumb when normal-->
    //</selector>
    public Builder thumbDrawable(@NonNull StateListDrawable thumbStateListDrawable) {
        this.thumbDrawable = thumbStateListDrawable;
        return this;
    }

    /**
     * call this method to custom the thumb showing drawable.
     *
     * @param thumbDrawableId the drawableId for thumb drawable.
     */
    public Builder thumbDrawable(@DrawableRes int thumbDrawableId) {
        this.thumbDrawable = context.getResources().getDrawable(thumbDrawableId);
        return this;
    }

    /**
     * show the tick texts in one place
     *
     * @param textPosition the position for texts to show,
     *                     see{@link TextPosition}
     *                     TextPosition.NONE;
     *                     TextPosition.BELOW;
     *                     TextPosition.ABOVE;
     */
    public Builder showTickTextsPosition(int textPosition) {
        this.tickTextsShow = textPosition;
        return this;
    }

    /**
     * set the color for text below/above seek bar's tickText.
     *
     * @param tickTextsColor ColorInt
     */
    public Builder tickTextsColor(@ColorInt int tickTextsColor) {
        this.tickTextsColor = tickTextsColor;
        return this;
    }

    /**
     * set the selector color for text below/above seek bar's tickText.
     *
     * @param tickTextsColorStateList ColorInt
     * @return Builder
     * selector format like:
     */
    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:color="@color/colorAccent" android:state_selected="true" />  <!--this color is for texts those are at left side of thumb-->
    //<item android:color="@color/color_blue" android:state_hovered="true" />     <!--for thumb below text-->
    //<item android:color="@color/color_gray" />                                 <!--for texts those are at right side of thumb-->
    //</selector>
    public Builder tickTextsColorStateList(@NonNull ColorStateList tickTextsColorStateList) {
        this.tickTextsColorStateList = tickTextsColorStateList;
        return this;
    }

    /**
     * set the size for tickText which below/above seek bar's tick .
     *
     * @param tickTextsSize The scaled pixel size.
     */
    public Builder tickTextsSize(int tickTextsSize) {
        this.tickTextsSize = SizeUtils.sp2px(context, tickTextsSize);
        return this;
    }

    /**
     * call this method to replace the seek bar's tickMarks' below/above tick texts.
     *
     * @param tickTextsArray the length should same as tickCount.
     */
    public Builder tickTextsArray(String[] tickTextsArray) {
        this.tickTextsCustomArray = tickTextsArray;
        return this;
    }


    /**
     * call this method to replace the seek bar's tickMarks' below/above tick texts.
     *
     * @param tickTextsArray the length should same as tickNum.
     */
    public Builder tickTextsArray(@ArrayRes int tickTextsArray) {
        this.tickTextsCustomArray = context.getResources().getStringArray(tickTextsArray);
        return this;
    }

    /**
     * set the tick text's / thumb text textTypeface .
     *
     * @param tickTextsTypeFace The text textTypeface.
     */
    public Builder tickTextsTypeFace(Typeface tickTextsTypeFace) {
        this.tickTextsTypeFace = tickTextsTypeFace;
        return this;
    }

    /**
     * set the tickMarks number.
     *
     * @param tickCount the tickMarks count show on seek bar.
     *                  if you want the seek bar's block size is N , this tickCount should be N+1.
     */
    public Builder tickCount(int tickCount) {
        this.tickCount = tickCount;
        return this;
    }

    /**
     * call this method to show different tickMark shape.
     *
     * @param tickMarksType see{@link TickMarkType}
     *                      TickMarkType.NONE;
     *                      TickMarkType.OVAL;
     *                      TickMarkType.SQUARE;
     *                      TickMarkType.DIVIDER;
     *                      TickMarkType.CUSTOM;
     */
    public Builder showTickMarksType(int tickMarksType) {
        this.showTickMarksType = tickMarksType;
        return this;
    }

    /**
     * set the seek bar's tick's color.
     *
     * @param tickMarksColor colorInt
     */
    public Builder tickMarksColor(@ColorInt int tickMarksColor) {
        this.tickMarksColor = tickMarksColor;
        return this;
    }

    /**
     * set the seek bar's tick's color.
     *
     * @param tickMarksColorStateList colorInt
     * @return Builder
     * selector format like:
     */
    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:color="@color/colorAccent" android:state_selected="true" />  <!--this color is for marks those are at left side of thumb-->
    //<item android:color="@color/color_gray" />                                 <!--for marks those are at right side of thumb-->
    //</selector>
    public Builder tickMarksColor(@NonNull ColorStateList tickMarksColorStateList) {
        this.tickMarksColorStateList = tickMarksColorStateList;
        return this;
    }

    /**
     * set the seek bar's tick width , if tick type is divider, call this method will be not worked(tick type is divider,has a regular value 2dp).
     *
     * @param tickMarksSize the dp size.
     */
    public Builder tickMarksSize(int tickMarksSize) {
        this.tickMarksSize = SizeUtils.dp2px(context, tickMarksSize);
        return this;
    }

    /**
     * call this method to custom the tick showing drawable.
     *
     * @param tickMarksDrawable the drawable show as tickMark.
     */
    public Builder tickMarksDrawable(@NonNull Drawable tickMarksDrawable) {
        this.tickMarksDrawable = tickMarksDrawable;
        return this;
    }

    /**
     * call this method to custom the tick showing drawable by selector.
     *
     * @param tickMarksStateListDrawable the StateListDrawable show as tickMark.
     * @return Builder
     * selector format like :
     */
    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:drawable="@mipmap/ic_launcher_round" android:state_pressed="true" />  <!--this drawable is for thumb when pressing-->
    //<item android:drawable="@mipmap/ic_launcher" />  <!--for thumb when normal-->
    //</selector>
    public Builder tickMarksDrawable(@NonNull StateListDrawable tickMarksStateListDrawable) {
        this.tickMarksDrawable = tickMarksStateListDrawable;
        return this;
    }

    /**
     * call this method to hide the tickMarks which show in the both ends sides of seek bar.
     *
     * @param tickMarksEndsHide true for hide.
     */
    public Builder tickMarksEndsHide(boolean tickMarksEndsHide) {
        this.tickMarksEndsHide = tickMarksEndsHide;
        return this;
    }

    /**
     * call this method to hide the tickMarks on seekBar's thumb left;
     *
     * @param tickMarksSweptHide true for hide.
     */
    public Builder tickMarksSweptHide(boolean tickMarksSweptHide) {
        this.tickMarksSweptHide = tickMarksSweptHide;
        return this;
    }

}