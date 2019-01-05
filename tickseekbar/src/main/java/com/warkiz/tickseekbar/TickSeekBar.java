package com.warkiz.tickseekbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * created by zhuangguangquan on 2018/6/6
 */

public class TickSeekBar extends View {
    private static final int THUMB_MAX_WIDTH = 30;
    private Context mContext;
    private Paint mStockPaint;//the paint for seek bar drawing
    private TextPaint mTextPaint;//the paint for mTickTextsArr drawing
    private OnSeekChangeListener mSeekChangeListener;
    private Rect mRect;
    private float mCustomDrawableMaxHeight;//the max height for custom drawable
    private float lastProgress;
    private float mFaultTolerance = -1;//the tolerance for user seek bar touching
    //seek bar
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mMeasuredWidth;
    private int mPaddingTop;
    private float mSeekLength;//the total length of seek bar
    private float mSeekBlockLength;//the length for each section part to seek
    private boolean mIsTouching;//user is touching the seek bar
    private float mMax;
    private float mMin;
    private float mProgress;
    private boolean mIsFloatProgress;// true for the mProgressArr value in float,otherwise in int.
    private boolean mUserSeekable;//true if the user can seek to change the mProgressArr,otherwise only can be changed by setProgress().
    private boolean mOnlyThumbDraggable;//only drag the seek bar's thumb can be change the mProgressArr
    private boolean mSeekSmoothly;//seek continuously
    private float[] mProgressArr;//save the progress which at tickMark position.
    private boolean mR2L;//right to left,compat local problem.
    //tick texts
    private int mTickTextsPosition;//the palace where the tick text show .
    private int mTickTextsHeight;//the height of text
    private String[] mTickTextsArr;//save the tick texts which at tickMark position.
    private float[] mTickTextsWidth;//save the tick texts width bounds.
    private float[] mTextCenterX;//the text's drawing X anchor
    private float mTickTextY;//the text's drawing Y anchor
    private int mTickTextsSize;
    private Typeface mTextsTypeface;//the tick texts and thumb texts' typeface
    private int mUnselectedTextsColor;//the color for the tick texts those thumb haven't reach.
    private int mSelectedTextsColor;//the color for the tick texts those thumb haven't reach.
    private int mHoveredTextColor;//the color for the tick texts which below/above thumb.
    private int mDefaultTickTextsHeight;
    private CharSequence[] mTickTextsCustomArray;
    //tick marks
    private float[] mTickMarksX;//the tickMark's drawing X anchor
    private int mTicksCount;//the num of tickMarks
    private int mUnSelectedTickMarksColor;//the color for the tickMarks those thumb haven't reach.
    private int mSelectedTickMarksColor;//the color for the tickMarks those thumb swept.
    private float mTickRadius;//the tick's radius
    private Bitmap mUnselectedTickMarksBitmap;//the drawable bitmap for tick
    private Bitmap mSelectTickMarksBitmap;//the drawable bitmap for tick
    private Drawable mTickMarksDrawable;
    private int mShowTickMarksType;
    private boolean mTickMarksEndsHide;//true if want to hide the tickMarks which in both side ends of seek bar
    private boolean mTickMarksSweptHide;//true if want to hide the tickMarks which on thumb left.
    private int mTickMarksSize;//the width of tickMark
    //track
    private boolean mTrackRoundedCorners;
    private RectF mProgressTrack;//the background track on the thumb start
    private RectF mBackgroundTrack;//the background track on the thumb ends
    private int mBackgroundTrackSize;
    private int mProgressTrackSize;
    private int mBackgroundTrackColor;
    private int mProgressTrackColor;
    private int[] mSectionTrackColorArray;//save the color for each section tracks.
    private boolean mCustomTrackSectionColorResult;//true to confirm to custom the section track color
    //thumb
    private float mThumbRadius;//the thumb's radius
    private float mThumbTouchRadius;//the thumb's radius when touching
    private Bitmap mThumbBitmap;//the drawable bitmap for thumb
    private int mThumbColor;
    private int mThumbSize;
    private Drawable mThumbDrawable;
    private Bitmap mPressedThumbBitmap;//the bitmap for pressing status
    private int mPressedThumbColor;//the color for pressing status
    //thumb text
    private int mThumbTextShowPos;//the palace where the thumb text show .
    private float mThumbTextY;//the thumb text's drawing Y anchor
    private int mThumbTextColor;
    private boolean mClearPadding;
    private SeekParams mSeekParams;
    private int mScale = 1;
    private boolean mAdjustAuto;

    public TickSeekBar(Context context) {
        this(context, null);
    }

    public TickSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(mContext, attrs);
        initParams();
    }

    public TickSeekBar(Builder builder) {
        super(builder.context);
        this.mContext = builder.context;
        int defaultPadding = SizeUtils.dp2px(mContext, 16);
        setPadding(defaultPadding, getPaddingTop(), defaultPadding, getPaddingBottom());
        this.apply(builder);
        initParams();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        Builder builder = new Builder(context);
        if (attrs == null) {
            //setDefaultParams
            this.apply(builder);
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TickSeekBar);
        //seekBar
        mMax = ta.getFloat(R.styleable.TickSeekBar_tsb_max, builder.max);
        mMin = ta.getFloat(R.styleable.TickSeekBar_tsb_min, builder.min);
        mProgress = ta.getFloat(R.styleable.TickSeekBar_tsb_progress, builder.progress);
        mIsFloatProgress = ta.getBoolean(R.styleable.TickSeekBar_tsb_progress_value_float, builder.progressValueFloat);
        mUserSeekable = ta.getBoolean(R.styleable.TickSeekBar_tsb_user_seekable, builder.userSeekable);
        mClearPadding = ta.getBoolean(R.styleable.TickSeekBar_tsb_clear_default_padding, builder.clearPadding);
        mOnlyThumbDraggable = ta.getBoolean(R.styleable.TickSeekBar_tsb_only_thumb_draggable, builder.onlyThumbDraggable);
        mSeekSmoothly = ta.getBoolean(R.styleable.TickSeekBar_tsb_seek_smoothly, builder.seekSmoothly);
        mR2L = ta.getBoolean(R.styleable.TickSeekBar_tsb_r2l, builder.r2l);
        //track
        mBackgroundTrackSize = ta.getDimensionPixelSize(R.styleable.TickSeekBar_tsb_track_background_size, builder.trackBackgroundSize);
        mProgressTrackSize = ta.getDimensionPixelSize(R.styleable.TickSeekBar_tsb_track_progress_size, builder.trackProgressSize);
        mBackgroundTrackColor = ta.getColor(R.styleable.TickSeekBar_tsb_track_background_color, builder.trackBackgroundColor);
        mProgressTrackColor = ta.getColor(R.styleable.TickSeekBar_tsb_track_progress_color, builder.trackProgressColor);
        mTrackRoundedCorners = ta.getBoolean(R.styleable.TickSeekBar_tsb_track_rounded_corners, builder.trackRoundedCorners);
        //thumb
        mThumbSize = ta.getDimensionPixelSize(R.styleable.TickSeekBar_tsb_thumb_size, builder.thumbSize);
        mThumbDrawable = ta.getDrawable(R.styleable.TickSeekBar_tsb_thumb_drawable);
        initThumbColor(ta.getColorStateList(R.styleable.TickSeekBar_tsb_thumb_color), builder.thumbColor);
        mAdjustAuto = ta.getBoolean(R.styleable.TickSeekBar_tsb_thumb_adjust_auto, builder.thumbAutoAdjust);
        //thumb text
        mThumbTextShowPos = ta.getInt(R.styleable.TickSeekBar_tsb_show_thumb_text, builder.thumbTextShow);
        mThumbTextColor = ta.getColor(R.styleable.TickSeekBar_tsb_thumb_text_color, builder.thumbTextColor);
        //tickMarks
        mTicksCount = ta.getInt(R.styleable.TickSeekBar_tsb_ticks_count, builder.tickCount);
        mShowTickMarksType = ta.getInt(R.styleable.TickSeekBar_tsb_show_tick_marks_type, builder.showTickMarksType);
        mTickMarksSize = ta.getDimensionPixelSize(R.styleable.TickSeekBar_tsb_tick_marks_size, builder.tickMarksSize);
        initTickMarksColor(ta.getColorStateList(R.styleable.TickSeekBar_tsb_tick_marks_color), builder.tickMarksColor);
        mTickMarksDrawable = ta.getDrawable(R.styleable.TickSeekBar_tsb_tick_marks_drawable);
        mTickMarksSweptHide = ta.getBoolean(R.styleable.TickSeekBar_tsb_tick_marks_swept_hide, builder.tickMarksSweptHide);
        mTickMarksEndsHide = ta.getBoolean(R.styleable.TickSeekBar_tsb_tick_marks_ends_hide, builder.tickMarksEndsHide);
        //tickTexts
        mTickTextsPosition = ta.getInt(R.styleable.TickSeekBar_tsb_show_tick_texts, TextPosition.NONE);
        mTickTextsSize = ta.getDimensionPixelSize(R.styleable.TickSeekBar_tsb_tick_texts_size, builder.tickTextsSize);
        initTickTextsColor(ta.getColorStateList(R.styleable.TickSeekBar_tsb_tick_texts_color), builder.tickTextsColor);
        mTickTextsCustomArray = ta.getTextArray(R.styleable.TickSeekBar_tsb_tick_texts_array);
        initTextsTypeface(ta.getInt(R.styleable.TickSeekBar_tsb_tick_texts_typeface, -1), builder.tickTextsTypeFace);
        ta.recycle();
    }

    private void initParams() {
        if (mTicksCount < 0 || mTicksCount > 50) {
            throw new IllegalArgumentException("the Argument: TICK COUNT must be limited between 0-50, " +
                    "Now is " + mTicksCount);
        }
        initProgressRangeValue();
        if (mBackgroundTrackSize > mProgressTrackSize) {
            mBackgroundTrackSize = mProgressTrackSize;
        }
        if (mThumbDrawable == null) {
            mThumbRadius = mThumbSize / 2.0f;
            mThumbTouchRadius = mThumbRadius * 1.2f;
        } else {
            mThumbRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mThumbSize) / 2.0f;
            mThumbTouchRadius = mThumbRadius;
        }
        if (mTickMarksDrawable == null) {
            mTickRadius = mTickMarksSize / 2.0f;
        } else {
            mTickRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mTickMarksSize) / 2.0f;
        }
        mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f;
        initStrokePaint();
        measureTickTextsBonds();
        lastProgress = mProgress;

        collectTicksInfo();

        mProgressTrack = new RectF();
        mBackgroundTrack = new RectF();
        initDefaultPadding();

    }

    private void collectTicksInfo() {
        if (mTicksCount < 0 || mTicksCount > 50) {
            throw new IllegalArgumentException("the Argument: TICK COUNT must be limited between (0-50), Now is " + mTicksCount);
        }
        if (mTicksCount != 0) {
            mTickMarksX = new float[mTicksCount];
            if (mTickTextsPosition != TextPosition.NONE) {
                mTextCenterX = new float[mTicksCount];
                mTickTextsWidth = new float[mTicksCount];
            }
            mProgressArr = new float[mTicksCount];
            for (int i = 0; i < mProgressArr.length; i++) {
                mProgressArr[i] = mMin + i * (mMax - mMin) / ((mTicksCount - 1) > 0 ? (mTicksCount - 1) : 1);
            }

        }
    }

    private void initDefaultPadding() {
        if (!mClearPadding) {
            int normalPadding = SizeUtils.dp2px(mContext, 16);
            if (getPaddingLeft() == 0) {
                setPadding(normalPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
            }
            if (getPaddingRight() == 0) {
                setPadding(getPaddingLeft(), getPaddingTop(), normalPadding, getPaddingBottom());
            }
        }
    }

    private void initProgressRangeValue() {
        if (mMax < mMin) {
            throw new IllegalArgumentException("the Argument: MAX's value must be larger than MIN's.");
        }
        if (mProgress < mMin) {
            mProgress = mMin;
        }
        if (mProgress > mMax) {
            mProgress = mMax;
        }
    }

    private void initStrokePaint() {
        if (mStockPaint == null) {
            mStockPaint = new Paint();
        }
        if (mTrackRoundedCorners) {
            mStockPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        mStockPaint.setAntiAlias(true);
        if (mBackgroundTrackSize > mProgressTrackSize) {
            mProgressTrackSize = mBackgroundTrackSize;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = Math.round(mCustomDrawableMaxHeight + getPaddingTop() + getPaddingBottom());
        if (isAboveBelowText()) {
            setMeasuredDimension(resolveSize(SizeUtils.dp2px(mContext, 170), widthMeasureSpec), height + 2 * mTickTextsHeight);
        } else {
            setMeasuredDimension(resolveSize(SizeUtils.dp2px(mContext, 170), widthMeasureSpec), height + mTickTextsHeight);
        }
        initSeekBarInfo();
        refreshSeekBarLocation();
    }

    private void initSeekBarInfo() {
        mMeasuredWidth = getMeasuredWidth();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mPaddingLeft = getPaddingLeft();
            mPaddingRight = getPaddingRight();
        } else {
            mPaddingLeft = getPaddingStart();
            mPaddingRight = getPaddingEnd();
        }
        mPaddingTop = getPaddingTop();
        mSeekLength = mMeasuredWidth - mPaddingLeft - mPaddingRight;
        mSeekBlockLength = mSeekLength / (mTicksCount - 1 > 0 ? mTicksCount - 1 : 1);
    }

    private void refreshSeekBarLocation() {
        initTrackLocation();
        initTickTextsYLocation();
//        initTickXAndTextXLocation();
        //init tick's X and text's X location;
        if (mTickMarksX == null) {
            return;
        }
        initTextsArray();
        //adjust thumb auto,so find out the closest mProgressArr in the mProgressArr array and replace it.
        //it is not necessary to adjust thumb while count is less than 3.
        if (mTicksCount > 2) {
            mProgress = mProgressArr[getClosestIndex()];
            lastProgress = mProgress;
        }
        refreshThumbCenterXByProgress(mProgress);
    }

    private void initTextsArray() {
        if (mTickMarksX == null) {
            return;
        }
        if (mTickTextsPosition != TextPosition.NONE) {
            mTickTextsArr = new String[mTicksCount];
        }
        for (int i = 0; i < mTickMarksX.length; i++) {
            if (mTickTextsPosition != TextPosition.NONE) {
                mTickTextsArr[i] = getTickTextByPosition(i);
                mTextPaint.getTextBounds(mTickTextsArr[i], 0, mTickTextsArr[i].length(), mRect);
                mTickTextsWidth[i] = mRect.width();
                mTextCenterX[i] = mPaddingLeft + mSeekBlockLength * i;
            }
            mTickMarksX[i] = mPaddingLeft + mSeekBlockLength * i;
        }

    }

    private void initTrackLocation() {
        if (mR2L) {
            mBackgroundTrack.left = mPaddingLeft;
            if (hasAboveText()) {
                mBackgroundTrack.top = mPaddingTop + mThumbTouchRadius + mDefaultTickTextsHeight + SizeUtils.dp2px(mContext, 3);
            } else {
                mBackgroundTrack.top = mPaddingTop + mThumbTouchRadius;
            }
            //ThumbCenterX
            mBackgroundTrack.right = mPaddingLeft + mSeekLength * (1.0f - (mProgress - mMin) / (mMax - mMin));
            mBackgroundTrack.bottom = mBackgroundTrack.top;
            //ThumbCenterX
            mProgressTrack.left = mBackgroundTrack.right;
            mProgressTrack.top = mBackgroundTrack.top;
            mProgressTrack.right = mMeasuredWidth - mPaddingRight;
            mProgressTrack.bottom = mBackgroundTrack.bottom;
        } else {
            mProgressTrack.left = mPaddingLeft;
            if (hasAboveText()) {
                mProgressTrack.top = mPaddingTop + mThumbTouchRadius + mDefaultTickTextsHeight + SizeUtils.dp2px(mContext, 3);
            } else {
                mProgressTrack.top = mPaddingTop + mThumbTouchRadius;
            }
            //ThumbCenterX
            mProgressTrack.right = (mProgress - mMin) * mSeekLength / (mMax - mMin) + mPaddingLeft;
            mProgressTrack.bottom = mProgressTrack.top;
            //ThumbCenterX
            mBackgroundTrack.left = mProgressTrack.right;
            mBackgroundTrack.top = mProgressTrack.bottom;
            mBackgroundTrack.right = mMeasuredWidth - mPaddingRight;
            mBackgroundTrack.bottom = mProgressTrack.bottom;
        }
    }

    private String getTickTextByPosition(int index) {
        if (mTickTextsCustomArray == null) {
            return getProgressString(mProgressArr[index]);
        }
        if (index < mTickTextsCustomArray.length) {
            return String.valueOf(mTickTextsCustomArray[index]);
        }
        return "";
    }

    /**
     * calculate the thumb's centerX by the changing mProgressArr.
     */
    void refreshThumbCenterXByProgress(float progress) {
        //ThumbCenterX
        if (mR2L) {
            mBackgroundTrack.right = mPaddingLeft + mSeekLength * (1.0f - (progress - mMin) / (mMax - mMin));//ThumbCenterX
            mProgressTrack.left = mBackgroundTrack.right;
        } else {
            mProgressTrack.right = (progress - mMin) * mSeekLength / (mMax - mMin) + mPaddingLeft;
            mBackgroundTrack.left = mProgressTrack.right;
        }
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        drawTrack(canvas);
        drawTickMarks(canvas);
        drawTickTexts(canvas);
        drawThumb(canvas);
        drawThumbText(canvas);
    }

    private void drawTrack(Canvas canvas) {
        if (mCustomTrackSectionColorResult) {//the track has custom the section track color
            int sectionSize = mTicksCount - 1 > 0 ? mTicksCount - 1 : 1;
            for (int i = 0; i < sectionSize; i++) {
                if (mR2L) {
                    mStockPaint.setColor(mSectionTrackColorArray[sectionSize - i - 1]);
                } else {
                    mStockPaint.setColor(mSectionTrackColorArray[i]);
                }
                float thumbPosFloat = getThumbPosOnTickFloat();
                if (i < thumbPosFloat && thumbPosFloat < (i + 1)) {
                    //the section track include the thumb,
                    // set the ProgressTrackSize for thumb's left side track ,
                    // BGTrackSize for the right's.
                    float thumbCenterX = getThumbCenterX();
                    mStockPaint.setStrokeWidth(getLeftSideTrackSize());
                    canvas.drawLine(mTickMarksX[i], mProgressTrack.top, thumbCenterX, mProgressTrack.bottom, mStockPaint);
                    mStockPaint.setStrokeWidth(getRightSideTrackSize());
                    canvas.drawLine(thumbCenterX, mProgressTrack.top, mTickMarksX[i + 1], mProgressTrack.bottom, mStockPaint);
                } else {
                    if (i < thumbPosFloat) {
                        mStockPaint.setStrokeWidth(getLeftSideTrackSize());
                    } else {
                        mStockPaint.setStrokeWidth(getRightSideTrackSize());
                    }
                    canvas.drawLine(mTickMarksX[i], mProgressTrack.top, mTickMarksX[i + 1], mProgressTrack.bottom, mStockPaint);
                }
            }
        } else {
            //draw mProgressArr track
            mStockPaint.setColor(mProgressTrackColor);
            mStockPaint.setStrokeWidth(mProgressTrackSize);
            canvas.drawLine(mProgressTrack.left, mProgressTrack.top, mProgressTrack.right, mProgressTrack.bottom, mStockPaint);
            //draw BG track
            mStockPaint.setColor(mBackgroundTrackColor);
            mStockPaint.setStrokeWidth(mBackgroundTrackSize);
            canvas.drawLine(mBackgroundTrack.left, mBackgroundTrack.top, mBackgroundTrack.right, mBackgroundTrack.bottom, mStockPaint);
        }

    }

    private void drawTickMarks(Canvas canvas) {
        if (mTicksCount == 0 || (mShowTickMarksType == TickMarkType.NONE && mTickMarksDrawable == null)) {
            return;
        }
        float thumbCenterX = getThumbCenterX();
        for (int i = 0; i < mTickMarksX.length; i++) {
            float thumbPosFloat = getThumbPosOnTickFloat();
            if (mTickMarksSweptHide) {
                if (thumbCenterX >= mTickMarksX[i]) {
                    continue;
                }
            }
            if (mTickMarksEndsHide) {
                if (i == 0 || i == mTickMarksX.length - 1) {
                    continue;
                }
            }
            if (i == getThumbPosOnTick() && mTicksCount > 2 && !mSeekSmoothly) {
                continue;
            }
            if (i <= thumbPosFloat) {
                mStockPaint.setColor(getLeftSideTickColor());
            } else {
                mStockPaint.setColor(getRightSideTickColor());
            }
            if (mTickMarksDrawable != null) {
                if (mSelectTickMarksBitmap == null || mUnselectedTickMarksBitmap == null) {
                    initTickMarksBitmap();
                }
                if (mSelectTickMarksBitmap == null || mUnselectedTickMarksBitmap == null) {
                    //please check your selector drawable's format and correct.
                    throw new IllegalArgumentException("the format of the selector TickMarks drawable is wrong!");
                }
                if (i <= thumbPosFloat) {
                    canvas.drawBitmap(mSelectTickMarksBitmap, mTickMarksX[i] - mUnselectedTickMarksBitmap.getWidth() / 2.0f, mProgressTrack.top - mUnselectedTickMarksBitmap.getHeight() / 2.0f, mStockPaint);
                } else {
                    canvas.drawBitmap(mUnselectedTickMarksBitmap, mTickMarksX[i] - mUnselectedTickMarksBitmap.getWidth() / 2.0f, mProgressTrack.top - mUnselectedTickMarksBitmap.getHeight() / 2.0f, mStockPaint);
                }
                continue;
            }
            if (mShowTickMarksType == TickMarkType.OVAL) {
                canvas.drawCircle(mTickMarksX[i], mProgressTrack.top, mTickRadius, mStockPaint);
            } else if (mShowTickMarksType == TickMarkType.DIVIDER) {
                float dividerTickHeight;
                int rectWidth = SizeUtils.dp2px(mContext, 1);
                if (thumbCenterX >= mTickMarksX[i]) {
                    dividerTickHeight = getLeftSideTrackSize();
                } else {
                    dividerTickHeight = getRightSideTrackSize();
                }
                canvas.drawRect(mTickMarksX[i] - rectWidth, mProgressTrack.top - dividerTickHeight / 2.0f, mTickMarksX[i] + rectWidth, mProgressTrack.top + dividerTickHeight / 2.0f, mStockPaint);
            } else if (mShowTickMarksType == TickMarkType.SQUARE) {
                canvas.drawRect(mTickMarksX[i] - mTickMarksSize / 2.0f, mProgressTrack.top - mTickMarksSize / 2.0f, mTickMarksX[i] + mTickMarksSize / 2.0f, mProgressTrack.top + mTickMarksSize / 2.0f, mStockPaint);
            }
        }
    }

    private void drawTickTexts(Canvas canvas) {
        if (mTickTextsArr == null) {
            return;
        }
        float thumbPosFloat = getThumbPosOnTickFloat();
        for (int i = 0; i < mTickTextsArr.length; i++) {
            if (i == getThumbPosOnTick()) {
                mTextPaint.setColor(mHoveredTextColor);
            } else if (i < thumbPosFloat) {
                mTextPaint.setColor(getLeftSideTickTextsColor());
            } else {
                mTextPaint.setColor(getRightSideTickTextsColor());
            }
            int index = i;
            if (mR2L) {
                index = mTickTextsArr.length - 1 - i;
            }
            if (i == 0) {
                canvas.drawText(mTickTextsArr[index], mTextCenterX[i] + mTickTextsWidth[index] / 2.0f, mTickTextY, mTextPaint);
            } else if (i == mTickTextsArr.length - 1) {
                canvas.drawText(mTickTextsArr[index], mTextCenterX[i] - mTickTextsWidth[index] / 2.0f, mTickTextY, mTextPaint);
            } else {
                canvas.drawText(mTickTextsArr[index], mTextCenterX[i], mTickTextY, mTextPaint);
            }
        }
    }

    private void drawThumb(Canvas canvas) {
        float thumbCenterX = getThumbCenterX();
        if (mThumbDrawable != null) {//check user has set thumb drawable or not.ThumbDrawable first, thumb color for later.
            if (mThumbBitmap == null || mPressedThumbBitmap == null) {
                initThumbBitmap();
            }
            if (mThumbBitmap == null || mPressedThumbBitmap == null) {
                //please check your selector drawable's format and correct.
                throw new IllegalArgumentException("the format of the selector thumb drawable is wrong!");
            }
            mStockPaint.setAlpha(255);
            if (mIsTouching) {
                canvas.drawBitmap(mPressedThumbBitmap, thumbCenterX - mPressedThumbBitmap.getWidth() / 2.0f, mProgressTrack.top - mPressedThumbBitmap.getHeight() / 2.0f, mStockPaint);
            } else {
                canvas.drawBitmap(mThumbBitmap, thumbCenterX - mThumbBitmap.getWidth() / 2.0f, mProgressTrack.top - mThumbBitmap.getHeight() / 2.0f, mStockPaint);
            }
        } else {
            if (mIsTouching) {
                mStockPaint.setColor(mPressedThumbColor);
            } else {
                mStockPaint.setColor(mThumbColor);
            }
            canvas.drawCircle(thumbCenterX, mProgressTrack.top, mIsTouching ? mThumbTouchRadius : mThumbRadius, mStockPaint);
        }
    }

    private void drawThumbText(Canvas canvas) {
        if (mThumbTextShowPos == TextPosition.NONE) {
            return;
        }
        //don't show thumb text in this conflict text position
        if (mTickTextsPosition == mThumbTextShowPos) {
            return;
        }
        mTextPaint.setColor(mThumbTextColor);
        canvas.drawText(getProgressString(mProgress), getThumbCenterX(), mThumbTextY, mTextPaint);
    }

    /**
     * get the track size which on the thumb right in R2L/L2R case.
     */
    private int getRightSideTrackSize() {
        if (mR2L) {
            return mProgressTrackSize;
        }
        return mBackgroundTrackSize;
    }

    private int getLeftSideTickColor() {
        if (mR2L) {
            return mUnSelectedTickMarksColor;
        }
        return mSelectedTickMarksColor;
    }

    private int getRightSideTickColor() {
        if (mR2L) {
            return mSelectedTickMarksColor;
        }
        return mUnSelectedTickMarksColor;
    }

    /**
     * get the track size which on the thumb left in R2L/L2R case.
     */
    private int getLeftSideTrackSize() {
        if (mR2L) {
            return mBackgroundTrackSize;
        }
        return mProgressTrackSize;
    }

    private int getThumbPosOnTick() {
        if (mTicksCount != 0) {
            return Math.round((getThumbCenterX() - mPaddingLeft) / mSeekBlockLength);
        }
        return 0;
    }

    private float getThumbPosOnTickFloat() {
        if (mTicksCount != 0) {
            return (getThumbCenterX() - mPaddingLeft) / mSeekBlockLength;
        }
        return 0;
    }

    private Bitmap getDrawBitmap(Drawable drawable, boolean isThumb) {
        if (drawable == null) {
            return null;
        }
        int width;
        int height;
        int maxRange = SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH);
        int intrinsicWidth = drawable.getIntrinsicWidth();
        if (intrinsicWidth > maxRange) {
            if (isThumb) {
                width = mThumbSize;
            } else {
                width = mTickMarksSize;
            }
            height = getHeightByRatio(drawable, width);

            if (width > maxRange) {
                width = maxRange;
                height = getHeightByRatio(drawable, width);
            }
        } else {
            width = drawable.getIntrinsicWidth();
            height = drawable.getIntrinsicHeight();
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private int getHeightByRatio(Drawable drawable, int width) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        return Math.round(1.0f * width * intrinsicHeight / intrinsicWidth);
    }

    /**
     * initial the color for the thumb.
     * <p>
     * <p>
     * NOTICE: make sure the format of color selector you set is right.
     * int[][] states = colorStateList.getStates();
     * (1) if the states.length == 1,the way you set the thumb color like :
     * app:isb_thumb_color="#XXXXXX"  or
     * app:isb_thumb_color="@color/color_name" ;
     * <p>
     * (2) if the states.length == 3,the way you set the thumb color like :
     * app:isb_thumb_color="@color/selector_color_file_name". the file(located at res/color/)'s format should like:
     * <p>
     * <?xml version="1.0" encoding="utf-8"?>
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     * <item android:color="#555555" android:state_pressed="true" />  <!--this color is for thumb which is at pressing status-->
     * <item android:color="#555555"/>                                <!--for thumb which is at normal status-->
     * </selector>
     * <p>
     * (3) if the states.length == other, the color's format you set is not support.
     */
    private void initThumbColor(ColorStateList colorStateList, int defaultColor) {
        //if you didn't set the thumb color, set a default color.
        if (colorStateList == null) {
            mThumbColor = defaultColor;
            mPressedThumbColor = mThumbColor;
            return;
        }
        int[][] states = null;
        int[] colors = null;
        Class<? extends ColorStateList> aClass = colorStateList.getClass();
        try {
            Field[] f = aClass.getDeclaredFields();
            for (Field field : f) {
                field.setAccessible(true);
                if ("mStateSpecs".equals(field.getName())) {
                    states = (int[][]) field.get(colorStateList);
                }
                if ("mColors".equals(field.getName())) {
                    colors = (int[]) field.get(colorStateList);
                }
            }
            if (states == null || colors == null) {
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something wrong happened when parseing thumb selector color.");
        }
        if (states.length == 1) {
            mThumbColor = colors[0];
            mPressedThumbColor = mThumbColor;
        } else if (states.length == 2) {
            for (int i = 0; i < states.length; i++) {
                int[] attr = states[i];
                if (attr.length == 0) {//didn't have state,so just get color.
                    mPressedThumbColor = colors[i];
                    continue;
                }
                switch (attr[0]) {
                    case android.R.attr.state_pressed:
                        mThumbColor = colors[i];
                        break;
                    default:
                        //the color selector file was set by a wrong format , please see above to correct.
                        throw new IllegalArgumentException("the selector color file you set for the argument: isb_thumb_color is in wrong format.");
                }
            }
        } else {
            //the color selector file was set by a wrong format , please see above to correct.
            throw new IllegalArgumentException("the selector color file you set for the argument: isb_thumb_color is in wrong format.");
        }

    }

    /**
     * initial the color for the tick masks
     * <p>
     * <p>
     * NOTICE: make sure the format of color selector you set is right.
     * int[][] states = colorStateList.getStates();
     * (1) if the states.length == 1,the way you set the tick marks' color like :
     * app:isb_tick_marks_color="#XXXXXX"  or
     * app:isb_tick_marks_color="@color/color_name" ;
     * <p>
     * (2) if the states.length == 2,the way you set the tick marks' color like :
     * app:isb_tick_marks_color="@color/selector_color_file_name". the file(located at res/color/)'s format should like:
     * <p>
     * <?xml version="1.0" encoding="utf-8"?>
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     * <item android:color="#555555" android:state_selected="true" />  <!--this color is for marks those are at left side of thumb-->
     * <item android:color="#555555"/>                                 <!--for marks those are at right side of thumb-->
     * </selector>
     * <p>
     * (3) if the states.length == other, the way you set is not support.
     */
    private void initTickMarksColor(ColorStateList colorStateList, int defaultColor) {
        //if you didn't set the tick's text color, set a default selector color file.
        if (colorStateList == null) {
            mSelectedTickMarksColor = defaultColor;
            mUnSelectedTickMarksColor = mSelectedTickMarksColor;
            return;
        }
        int[][] states = null;
        int[] colors = null;
        Class<? extends ColorStateList> aClass = colorStateList.getClass();
        try {
            Field[] f = aClass.getDeclaredFields();
            for (Field field : f) {
                field.setAccessible(true);
                if ("mStateSpecs".equals(field.getName())) {
                    states = (int[][]) field.get(colorStateList);
                }
                if ("mColors".equals(field.getName())) {
                    colors = (int[]) field.get(colorStateList);
                }
            }
            if (states == null || colors == null) {
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something wrong happened when parsing thumb selector color." + e.getMessage());
        }
        if (states.length == 1) {
            mSelectedTickMarksColor = colors[0];
            mUnSelectedTickMarksColor = mSelectedTickMarksColor;
        } else if (states.length == 2) {
            for (int i = 0; i < states.length; i++) {
                int[] attr = states[i];
                if (attr.length == 0) {//didn't have state,so just get color.
                    mUnSelectedTickMarksColor = colors[i];
                    continue;
                }
                switch (attr[0]) {
                    case android.R.attr.state_selected:
                        mSelectedTickMarksColor = colors[i];
                        break;
                    default:
                        //the color selector file was set by a wrong format , please see above to correct.
                        throw new IllegalArgumentException("the selector color file you set for the argument: isb_tick_marks_color is in wrong format.");
                }
            }
        } else {
            //the color selector file was set by a wrong format , please see above to correct.
            throw new IllegalArgumentException("the selector color file you set for the argument: isb_tick_marks_color is in wrong format.");
        }
    }

    /**
     * initial the color for the tick texts.
     * <p>
     * <p>
     * NOTICE: make sure the format of color selector you set is right.
     * int[][] states = colorStateList.getStates();
     * (1) if the states.length == 1,the way you set the tick texts' color like :
     * app:isb_tick_text_color="#XXXXXX"  or
     * app:isb_tick_text_color="@color/color_name" ;
     * <p>
     * (2) if the states.length == 3,the way you set the tick texts' color like :
     * app:isb_tick_text_color="@color/selector_color_file_name". the file(located at res/color/)'s format should like:
     * <p>
     * <?xml version="1.0" encoding="utf-8"?>
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     * <item android:color="#555555" android:state_selected="true" />  <!--this color is for texts those are at left side of thumb-->
     * <item android:color="#FF4081" android:state_hovered="true" />     <!--for thumb text-->
     * <item android:color="#555555"/>                                 <!--for texts those are at right side of thumb-->
     * </selector>
     * <p>
     * (3) if the states.length == other, the way you set is not support.
     */
    private void initTickTextsColor(ColorStateList colorStateList, int defaultColor) {
        //if you didn't set the tick's texts color, will be set a selector color file default.
        if (colorStateList == null) {
            mUnselectedTextsColor = defaultColor;
            mSelectedTextsColor = mUnselectedTextsColor;
            mHoveredTextColor = mUnselectedTextsColor;
            return;
        }
        int[][] states = null;
        int[] colors = null;
        Class<? extends ColorStateList> aClass = colorStateList.getClass();
        try {
            Field[] f = aClass.getDeclaredFields();
            for (Field field : f) {
                field.setAccessible(true);
                if ("mStateSpecs".equals(field.getName())) {
                    states = (int[][]) field.get(colorStateList);
                }
                if ("mColors".equals(field.getName())) {
                    colors = (int[]) field.get(colorStateList);
                }
            }
            if (states == null || colors == null) {
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something wrong happened when parseing thumb selector color.");
        }
        if (states.length == 1) {
            mUnselectedTextsColor = colors[0];
            mSelectedTextsColor = mUnselectedTextsColor;
            mHoveredTextColor = mUnselectedTextsColor;
        } else if (states.length == 3) {
            for (int i = 0; i < states.length; i++) {
                int[] attr = states[i];
                if (attr.length == 0) {//didn't have state,so just get color.
                    mUnselectedTextsColor = colors[i];
                    continue;
                }
                switch (attr[0]) {
                    case android.R.attr.state_selected:
                        mSelectedTextsColor = colors[i];
                        break;
                    case android.R.attr.state_hovered:
                        mHoveredTextColor = colors[i];
                        break;
                    default:
                        //the color selector file was set by a wrong format , please see above to correct.
                        throw new IllegalArgumentException("the selector color file you set for the argument: isb_tick_texts_color is in wrong format.");
                }
            }
        } else {
            //the color selector file was set by a wrong format , please see above to correct.
            throw new IllegalArgumentException("the selector color file you set for the argument: isb_tick_texts_color is in wrong format.");
        }
    }

    /**
     * initial both the tick texts' and thumb text's typeface,just has 4 type to choose,
     * but you can set the CUSTOM typeface you want by java code.
     * <p>
     * usage like:
     * indicatorSeekbar.customTickTextsTypeface(Typeface.xxx);
     */
    private void initTextsTypeface(int typeface, Typeface defaultTypeface) {
        switch (typeface) {
            case 0:
                mTextsTypeface = Typeface.DEFAULT;
                break;
            case 1:
                mTextsTypeface = Typeface.MONOSPACE;
                break;
            case 2:
                mTextsTypeface = Typeface.SANS_SERIF;
                break;
            case 3:
                mTextsTypeface = Typeface.SERIF;
                break;
            default:
                if (defaultTypeface == null) {
                    mTextsTypeface = Typeface.DEFAULT;
                    break;
                }
                mTextsTypeface = defaultTypeface;
                break;
        }
    }

    /**
     * initial the bitmap for the thumb.
     * <p>
     * <p>
     * NOTICE: make sure the format of drawable selector file you set is right.
     * int stateCount = listDrawable.getStateCount();
     * (1) if the drawable instanceof BitmapDrawable,the way you set like :
     * app:isb_thumb_drawable="@drawable/ic_launcher"
     * <p>
     * (2) if the drawable instanceof StateListDrawable,the way you set like :
     * app:isb_thumb_drawable="@drawable/selector_thumb_drawable". the file(located at res/drawable/)'s format should like:
     * <p>
     * <?xml version="1.0" encoding="utf-8"?>
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     * <item android:drawable="@drawable/ic_launcher" android:state_pressed="true" />  <!--this drawable is for thumb when pressing-->
     * <item android:drawable="@drawable/ic_launcher_round" />  <!--for thumb when normal-->
     * </selector>
     */
    private void initThumbBitmap() {
        if (mThumbDrawable == null) {
            return;
        }
        if (mThumbDrawable instanceof StateListDrawable) {
            try {
                StateListDrawable listDrawable = (StateListDrawable) mThumbDrawable;
                Class<? extends StateListDrawable> aClass = listDrawable.getClass();
                int stateCount = (int) aClass.getMethod("getStateCount").invoke(listDrawable);
                if (stateCount == 2) {
                    Method getStateSet = aClass.getMethod("getStateSet", int.class);
                    Method getStateDrawable = aClass.getMethod("getStateDrawable", int.class);
                    for (int i = 0; i < stateCount; i++) {
                        int[] stateSet = (int[]) getStateSet.invoke(listDrawable, i);
                        if (stateSet.length > 0) {
                            if (stateSet[0] == android.R.attr.state_pressed) {
                                Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                                mPressedThumbBitmap = getDrawBitmap(stateDrawable, true);
                            } else {
                                //please check your selector drawable's format, please see above to correct.
                                throw new IllegalArgumentException("the state of the selector thumb drawable is wrong!");
                            }
                        } else {
                            Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                            mThumbBitmap = getDrawBitmap(stateDrawable, true);
                        }
                    }
                } else {
                    //please check your selector drawable's format, please see above to correct.
                    throw new IllegalArgumentException("the format of the selector thumb drawable is wrong!");
                }
            } catch (Exception e) {
                mThumbBitmap = getDrawBitmap(mThumbDrawable, true);
                mPressedThumbBitmap = mThumbBitmap;
            }
        } else {
            mThumbBitmap = getDrawBitmap(mThumbDrawable, true);
            mPressedThumbBitmap = mThumbBitmap;
        }
    }

    /**
     * initial the bitmap for the thickMarks.
     * <p>
     * <p>
     * NOTICE: make sure the format of drawable selector file you set is right.
     * int stateCount = listDrawable.getStateCount();
     * (1) if the drawable instanceof BitmapDrawable,the way you set like :
     * app:isb_tick_marks_drawable="@drawable/ic_launcher"
     * <p>
     * (2) if the drawable instanceof StateListDrawable,the way you set like :
     * app:isb_tick_marks_drawable="@drawable/selector_thumb_drawable". the file(located at res/drawable/)'s format should like:
     * <p>
     * <?xml version="1.0" encoding="utf-8"?>
     * <selector xmlns:android="http://schemas.android.com/apk/res/android">
     * <item android:drawable="@drawable/ic_launcher" android:state_selected="true" />  <!--this drawable is for thickMarks which thumb swept-->
     * <item android:drawable="@drawable/ic_launcher_round" />  <!--for thickMarks which thumb haven't reached-->
     * </selector>
     */
    private void initTickMarksBitmap() {
        if (mTickMarksDrawable instanceof StateListDrawable) {
            StateListDrawable listDrawable = (StateListDrawable) mTickMarksDrawable;
            try {
                Class<? extends StateListDrawable> aClass = listDrawable.getClass();
                Method getStateCount = aClass.getMethod("getStateCount");
                int stateCount = (int) getStateCount.invoke(listDrawable);
                if (stateCount == 2) {
                    Method getStateSet = aClass.getMethod("getStateSet", int.class);
                    Method getStateDrawable = aClass.getMethod("getStateDrawable", int.class);
                    for (int i = 0; i < stateCount; i++) {
                        int[] stateSet = (int[]) getStateSet.invoke(listDrawable, i);
                        if (stateSet.length > 0) {
                            if (stateSet[0] == android.R.attr.state_selected) {
                                Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                                mSelectTickMarksBitmap = getDrawBitmap(stateDrawable, false);
                            } else {
                                //please check your selector drawable's format, please see above to correct.
                                throw new IllegalArgumentException("the state of the selector TickMarks drawable is wrong!");
                            }
                        } else {
                            Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                            mUnselectedTickMarksBitmap = getDrawBitmap(stateDrawable, false);
                        }
                    }
                } else {
                    //please check your selector drawable's format, please see above to correct.
                    throw new IllegalArgumentException("the format of the selector TickMarks drawable is wrong!");
                }
            } catch (Exception e) {
                mUnselectedTickMarksBitmap = getDrawBitmap(mTickMarksDrawable, false);
                mSelectTickMarksBitmap = mUnselectedTickMarksBitmap;
            }
        } else {
            mUnselectedTickMarksBitmap = getDrawBitmap(mTickMarksDrawable, false);
            mSelectTickMarksBitmap = mUnselectedTickMarksBitmap;
        }

    }


    private void measureTickTextsBonds() {
        if (needDrawText()) {
            initTextPaint();
            mTextPaint.setTypeface(mTextsTypeface);
            mTextPaint.getTextBounds("j", 0, 1, mRect);
            mTickTextsHeight = mRect.height() + SizeUtils.dp2px(mContext, 3);//with the gap(3dp) between tickTexts and track.
        }
    }

    private boolean needDrawText() {
        return (mTickTextsPosition != TextPosition.NONE && mTicksCount != 0) || mThumbTextShowPos != 0;
    }

    private void initTextPaint() {
        if (mTextPaint == null) {
            mTextPaint = new TextPaint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(mTickTextsSize);
        }
        if (mRect == null) {
            mRect = new Rect();
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    private void initTickTextsYLocation() {
        if (needDrawText()) {
            mTextPaint.getTextBounds("j", 0, 1, mRect);
            mDefaultTickTextsHeight = mRect.height();
            if (isAboveBelowText()) {//one of the text is below seek bar, the other is above.
                if (mTickTextsPosition == TextPosition.BELOW) {
                    mThumbTextY = mPaddingTop + Math.round(mDefaultTickTextsHeight - mTextPaint.descent()) + SizeUtils.dp2px(mContext, 3);
                    mTickTextY = mTickTextsHeight + mPaddingTop + mCustomDrawableMaxHeight + Math.round(mDefaultTickTextsHeight - mTextPaint.descent()) + SizeUtils.dp2px(mContext, 3);
                } else {
                    mTickTextY = mPaddingTop + Math.round(mDefaultTickTextsHeight - mTextPaint.descent()) + SizeUtils.dp2px(mContext, 3);
                    mThumbTextY = mTickTextsHeight + mPaddingTop + mCustomDrawableMaxHeight + Math.round(mDefaultTickTextsHeight - mTextPaint.descent()) + SizeUtils.dp2px(mContext, 3);
                }
                return;
            }
            if (hasBelowText()) {//both text below seek bar
                mTickTextY = mPaddingTop + mCustomDrawableMaxHeight + Math.round(mDefaultTickTextsHeight - mTextPaint.descent()) + SizeUtils.dp2px(mContext, 3);
            } else if (hasAboveText()) {//both text above seek bar
                mTickTextY = mPaddingTop + Math.round(mDefaultTickTextsHeight - mTextPaint.descent()) + SizeUtils.dp2px(mContext, 3);
            }
            mThumbTextY = mTickTextY;
        }
    }

    private boolean hasBelowText() {
        return (mTicksCount != 0 && mTickTextsPosition == TextPosition.BELOW)
                || mThumbTextShowPos == TextPosition.BELOW;
    }

    private boolean hasAboveText() {
        return (mTicksCount != 0 && mTickTextsPosition == TextPosition.ABOVE)
                || mThumbTextShowPos == TextPosition.ABOVE;
    }

    private boolean isAboveBelowText() {
        return ((mTicksCount != 0 && mTickTextsPosition == TextPosition.ABOVE)
                && mThumbTextShowPos == TextPosition.BELOW)
                || ((mTicksCount != 0 && mTickTextsPosition == TextPosition.BELOW)
                && mThumbTextShowPos == TextPosition.ABOVE);
    }

    private float getThumbCenterX() {
        if (mR2L) {
            return mBackgroundTrack.right;
        }
        return mProgressTrack.right;
    }


    private int getLeftSideTickTextsColor() {
        if (mR2L) {
            return mUnselectedTextsColor;
        }
        return mSelectedTextsColor;
    }

    private int getRightSideTickTextsColor() {
        if (mR2L) {
            return mUnselectedTextsColor;
        }
        return mUnselectedTextsColor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mUserSeekable || !isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performClick();
                float mX = event.getX();
                if (isTouchSeekBar(mX, event.getY())) {
                    if ((mOnlyThumbDraggable && !isTouchThumb(mX))) {
                        return false;
                    }
                    if (mSeekChangeListener != null) {
                        mSeekChangeListener.onStartTrackingTouch(this);
                    }
                    mIsTouching = true;
                    refreshSeekBar(event);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                refreshSeekBar(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mSeekChangeListener != null) {
                    mSeekChangeListener.onStopTrackingTouch(this);
                }
                mIsTouching = false;
                if (!autoAdjustThumb()) {
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void refreshSeekBar(MotionEvent event) {
        refreshThumbCenterXByProgress(calculateProgress(calculateTouchX(adjustTouchX(event))));
        setSeekListener(true);
        invalidate();
    }

    private boolean autoAdjustThumb() {
        if (mTicksCount < 3 || !mSeekSmoothly) {//it is not necessary to adjust while count less than 2.
            return false;
        }
        if (!mAdjustAuto) {
            return false;
        }
        final int closestIndex = getClosestIndex();
        final float touchUpProgress = mProgress;
        ValueAnimator animator = ValueAnimator.ofFloat(0, Math.abs(touchUpProgress - mProgressArr[closestIndex]));
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lastProgress = mProgress;
                if (touchUpProgress - mProgressArr[closestIndex] > 0) {
                    mProgress = touchUpProgress - (Float) animation.getAnimatedValue();
                } else {
                    mProgress = touchUpProgress + (Float) animation.getAnimatedValue();
                }
                refreshThumbCenterXByProgress(mProgress);
                //the auto adjust was happened after user touched up, so from user is false.
                setSeekListener(false);
                invalidate();
            }
        });
        return true;
    }

    private int getClosestIndex() {
        int closestIndex = 0;
        float amplitude = Math.abs(mMax - mMin);
        for (int i = 0; i < mProgressArr.length; i++) {
            float amplitudeTemp = Math.abs(mProgressArr[i] - mProgress);
            if (amplitudeTemp <= amplitude) {
                amplitude = amplitudeTemp;
                closestIndex = i;
            }
        }
        return closestIndex;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void setSeekListener(boolean formUser) {
        if (mSeekChangeListener == null) {
            return;
        }
        if (progressChange()) {
            mSeekChangeListener.onSeeking(collectParams(formUser));
        }
    }

    private boolean progressChange() {
        if (mIsFloatProgress) {
            return lastProgress != mProgress;
        } else {
            return Math.round(lastProgress) != Math.round(mProgress);
        }
    }

    private SeekParams collectParams(boolean formUser) {
        if (mSeekParams == null) {
            mSeekParams = new SeekParams(this);
        }
        mSeekParams.progress = getProgress();
        mSeekParams.progressFloat = getProgressFloat();
        mSeekParams.fromUser = formUser;
        //for discrete series seek bar
        if (mTicksCount > 2) {
            int rawThumbPos = getThumbPosOnTick();
            if (mTickTextsPosition != TextPosition.NONE && mTickTextsArr != null) {
                mSeekParams.tickText = mTickTextsArr[rawThumbPos];
            }
            if (mR2L) {
                mSeekParams.thumbPosition = mTicksCount - rawThumbPos - 1;
            } else {
                mSeekParams.thumbPosition = rawThumbPos;
            }
        }
        return mSeekParams;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        ViewParent parent = getParent();
        if (parent == null) {
            return super.dispatchTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parent.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                parent.requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("tsb_instance_state", super.onSaveInstanceState());
        bundle.putFloat("tsb_progress", mProgress);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mProgress = bundle.getFloat("tsb_progress");
            setProgress(mProgress);
            super.onRestoreInstanceState(bundle.getParcelable("tsb_instance_state"));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }
        super.setEnabled(enabled);
        if (isEnabled()) {
            setAlpha(1.0f);
        } else {
            setAlpha(0.3f);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    private float adjustTouchX(MotionEvent event) {
        float mTouchXCache;
        if (event.getX() < mPaddingLeft) {
            mTouchXCache = mPaddingLeft;
        } else if (event.getX() > mMeasuredWidth - mPaddingRight) {
            mTouchXCache = mMeasuredWidth - mPaddingRight;
        } else {
            mTouchXCache = event.getX();
        }
        return mTouchXCache;
    }

    private float calculateProgress(float touchX) {
        lastProgress = mProgress;
        mProgress = mMin + (mMax - mMin) * (touchX - mPaddingLeft) / mSeekLength;
        return mProgress;
    }

    private float calculateTouchX(float touchX) {
        float touchXTemp = touchX;
        //make sure the seek bar to seek smoothly always
        // while the tick's count is less than 3(tick's count is 1 or 2.).
        if (mTicksCount > 2 && !mSeekSmoothly) {
            int touchBlockSize = Math.round((touchX - mPaddingLeft) / mSeekBlockLength);
            touchXTemp = mSeekBlockLength * touchBlockSize + mPaddingLeft;
        }
        if (mR2L) {
            return mSeekLength - touchXTemp + 2 * mPaddingLeft;
        }
        return touchXTemp;
    }

    private boolean isTouchSeekBar(float mX, float mY) {
        if (mFaultTolerance == -1) {
            mFaultTolerance = SizeUtils.dp2px(mContext, 5);
        }
        boolean inWidthRange = mX >= (mPaddingLeft - 2 * mFaultTolerance) && mX <= (mMeasuredWidth - mPaddingRight + 2 * mFaultTolerance);
        boolean inHeightRange = mY >= mProgressTrack.top - mThumbTouchRadius - mFaultTolerance && mY <= mProgressTrack.top + mThumbTouchRadius + mFaultTolerance;
        return inWidthRange && inHeightRange;
    }


    /**
     * transfer the progress value to string type
     */
    private String getProgressString(float progress) {
        if (mIsFloatProgress) {
            return FormatUtils.fastFormat(progress, mScale);
        }
        return String.valueOf(Math.round(progress));
    }

    private boolean isTouchThumb(float mX) {
        float rawTouchX = getTouchX();
        return rawTouchX - mThumbSize / 2f <= mX && mX <= rawTouchX + mThumbSize / 2f;
    }

    synchronized float getTouchX() {
        refreshThumbCenterXByProgress(mProgress);
        if (mR2L) {
            return mBackgroundTrack.right;
        }
        return mProgressTrack.right;
    }

    private void apply(Builder builder) {
        //seek bar
        this.mMax = builder.max;
        this.mMin = builder.min;
        this.mProgress = builder.progress;
        this.mIsFloatProgress = builder.progressValueFloat;
        this.mSeekSmoothly = builder.seekSmoothly;
        this.mR2L = builder.r2l;
        this.mUserSeekable = builder.userSeekable;
        this.mClearPadding = builder.clearPadding;
        this.mOnlyThumbDraggable = builder.onlyThumbDraggable;
        //track
        this.mBackgroundTrackSize = builder.trackBackgroundSize;
        this.mBackgroundTrackColor = builder.trackBackgroundColor;
        this.mProgressTrackSize = builder.trackProgressSize;
        this.mProgressTrackColor = builder.trackProgressColor;
        this.mTrackRoundedCorners = builder.trackRoundedCorners;
        //thumb
        this.mThumbSize = builder.thumbSize;
        this.mThumbDrawable = builder.thumbDrawable;
        this.mThumbTextColor = builder.thumbTextColor;
        initThumbColor(builder.thumbColorStateList, builder.thumbColor);
        this.mThumbTextShowPos = builder.thumbTextShow;
        //tickMarks
        this.mTicksCount = builder.tickCount;
        this.mShowTickMarksType = builder.showTickMarksType;
        this.mTickMarksSize = builder.tickMarksSize;
        this.mTickMarksDrawable = builder.tickMarksDrawable;
        this.mTickMarksEndsHide = builder.tickMarksEndsHide;
        this.mTickMarksSweptHide = builder.tickMarksSweptHide;
        initTickMarksColor(builder.tickMarksColorStateList, builder.tickMarksColor);
        //tickTexts
        this.mTickTextsPosition = builder.tickTextsShow;
        this.mTickTextsSize = builder.tickTextsSize;
        this.mTickTextsCustomArray = builder.tickTextsCustomArray;
        this.mTextsTypeface = builder.tickTextsTypeFace;
        initTickTextsColor(builder.tickTextsColorStateList, builder.tickTextsColor);
    }

    /****************** API START ******************/

    /**
     * call this to new a builder with default params.
     *
     * @param context context environment
     * @return Builder
     */
    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    /**
     * get the tick' count
     *
     * @return tick' count
     */
    public int getTickCount() {
        return mTicksCount;
    }

    /**
     * Get the seek bar's current level of progress in float type.
     *
     * @return current progress in float type.
     */
    public synchronized float getProgressFloat() {
        BigDecimal bigDecimal = BigDecimal.valueOf(mProgress);
        return bigDecimal.setScale(mScale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * Get the seek bar's current level of progress in int type.
     *
     * @return progress in int type.
     */
    public int getProgress() {
        return Math.round(mProgress);
    }

    /**
     * @return the upper limit of this seek bar's range.
     */
    public float getMax() {
        return mMax;
    }

    /**
     * the lower limit of this seek bar's range.
     *
     * @return the seek bar min value
     */
    public float getMin() {
        return mMin;
    }

    /**
     * the listener to listen the seeking params changing.
     *
     * @return seeking listener.
     */
    public OnSeekChangeListener getOnSeekChangeListener() {
        return mSeekChangeListener;
    }

    /**
     * Sets the current progress to the specified value.Also,
     * if the seek bar's tick'count is larger than 2,the progress will adjust to the closest tick's progress auto.
     *
     * @param progress a new progress value , if the new progress is less than min ,
     *                 it will set to min;
     *                 if over max ,will be max.
     */
    public synchronized void setProgress(float progress) {
        lastProgress = mProgress;
        mProgress = progress < mMin ? mMin : (progress > mMax ? mMax : progress);
        //adjust to the closest tick's progress
        if (mTicksCount > 2) {
            mProgress = mProgressArr[getClosestIndex()];
        }
        setSeekListener(false);
        refreshThumbCenterXByProgress(mProgress);
        postInvalidate();
    }

    /**
     * Set the upper range of the seek bar
     *
     * @param max the upper range of this mProgressArr bar.
     */
    public synchronized void setMax(float max) {
        this.mMax = Math.max(mMin, max);
        initProgressRangeValue();
        refreshSeekBarLocation();
        invalidate();
    }

    /**
     * Set the min value for SeekBar
     *
     * @param min the min value , if is larger than max, will set to max.
     */
    public synchronized void setMin(float min) {
        this.mMin = Math.min(mMax, min);
        initProgressRangeValue();
        refreshSeekBarLocation();
        invalidate();
    }

    /**
     * compat app local change
     *
     * @param isR2L True if see form right to left on the screen.
     */
    public void setR2L(boolean isR2L) {
        this.mR2L = isR2L;
        requestLayout();
        invalidate();
    }

    /**
     * Set a new thumb drawable.
     *
     * @param drawable the drawable for thumb,selector drawable is ok.
     *                 selector format:
     */
    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:drawable="@drawable/ic_launcher" android:state_pressed="true" />  <!--this drawable is for thumb when pressing-->
    //<item android:drawable="@drawable/ic_launcher_round" />  < !--for thumb when normal-->
    //</selector>
    public void setThumbDrawable(@NonNull Drawable drawable) {
        this.mThumbDrawable = drawable;
        mThumbRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mThumbSize) / 2.0f;
        mThumbTouchRadius = mThumbRadius;
        mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f;
        initThumbBitmap();
        requestLayout();
        invalidate();
    }

    /**
     * set the seek bar's thumb's color.
     *
     * @param thumbColor colorInt
     */
    public void thumbColor(@ColorInt int thumbColor) {
        this.mThumbColor = thumbColor;
        this.mPressedThumbColor = thumbColor;
        invalidate();
    }

    /**
     * set the seek bar's thumb's selector color.
     *
     * @param thumbColorStateList color selector
     *                            selector format like:
     */
    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:color="@color/colorAccent" android:state_pressed="true" />  <!--this color is for thumb which is at pressing status-->
    //<item android:color="@color/color_blue" />                                <!--for thumb which is at normal status-->
    //</selector>
    public void thumbColorStateList(@NonNull ColorStateList thumbColorStateList) {
        initThumbColor(thumbColorStateList, mThumbColor);
        invalidate();
    }

    /**
     * Set a new tick marks drawable.
     *
     * @param drawable the drawable for marks,selector drawable is ok.
     *                 selector format:
     */
    //< ?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:drawable="@drawable/ic_launcher" android:state_selected="true" />  < !--this drawable is for thickMarks which thumb swept-->
    //<item android:drawable="@drawable/ic_launcher_round" />  < !--for thickMarks which thumb haven't reached-->
    //</selector>
    public void setTickMarksDrawable(@NonNull Drawable drawable) {
        this.mTickMarksDrawable = drawable;
        mTickRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mTickMarksSize) / 2.0f;
        mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f;
        initTickMarksBitmap();
        invalidate();
    }

    /**
     * set the seek bar's tick's color.
     *
     * @param tickMarksColor colorInt
     */
    public void tickMarksColor(@ColorInt int tickMarksColor) {
        this.mSelectedTickMarksColor = tickMarksColor;
        this.mUnSelectedTickMarksColor = tickMarksColor;
        invalidate();
    }

    /**
     * set the seek bar's tick's color.
     *
     * @param tickMarksColorStateList colorInt
     *                                selector format like:
     */
    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:color="@color/colorAccent" android:state_selected="true" />  <!--this color is for marks those are at left side of thumb-->
    //<item android:color="@color/color_gray" />                                 <!--for marks those are at right side of thumb-->
    //</selector>
    public void tickMarksColor(@NonNull ColorStateList tickMarksColorStateList) {
        initTickMarksColor(tickMarksColorStateList, mSelectedTickMarksColor);
        invalidate();
    }

    /**
     * set the color for text below/above seek bar's tickText.
     *
     * @param tickTextsColor ColorInt
     */
    public void tickTextsColor(@ColorInt int tickTextsColor) {
        mUnselectedTextsColor = tickTextsColor;
        mUnselectedTextsColor = tickTextsColor;
        mHoveredTextColor = tickTextsColor;
        invalidate();
    }

    /**
     * set the selector color for text below/above seek bar's tickText.
     *
     * @param tickTextsColorStateList ColorInt
     *                                selector format like:
     */
    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:color="@color/colorAccent" android:state_selected="true" />  <!--this color is for texts those are at left side of thumb-->
    //<item android:color="@color/color_blue" android:state_hovered="true" />     <!--for thumb below text-->
    //<item android:color="@color/color_gray" />                                 <!--for texts those are at right side of thumb-->
    //</selector>
    public void tickTextsColorStateList(@NonNull ColorStateList tickTextsColorStateList) {
        initTickTextsColor(tickTextsColorStateList, mSelectedTextsColor);
        invalidate();
    }

    /**
     * The specified scale for the progress value,
     * make sure you had chosen the float progress type
     * <p>
     * such as:
     * scale = 3; progress: 1.78627347 to 1.786
     * scale = 4; progress: 1.78627347 to 1.7863
     * <p>
     * make sure you have call the attr progress_value_float=true before, otherwise no change.
     *
     * @param scale scale for the float type progress value.
     */
    public void setDecimalScale(int scale) {
        this.mScale = scale;
    }

    /**
     * Collect and custom the color for each of section track.
     * <p>
     * usage :
     * <p>
     * indicatorSeekBar.customSectionTrackColor(new ColorCollector() {
     * <p>
     * public boolean collectSectionTrackColor(int[] colorIntArr) {
     * colorIntArr[0] = getResources().getColor(R.color.color_blue);
     * colorIntArr[1] = getResources().getColor(R.color.color_gray);
     * colorIntArr[2] = Color.parseColor("#FFFF00");
     * ......
     * return true; // True if apply color , otherwise no change.
     * }
     * });
     *
     * @param collector The container for section track's color
     */
    public void customSectionTrackColor(@NonNull ColorCollector collector) {
        int[] colorArray = new int[mTicksCount - 1 > 0 ? mTicksCount - 1 : 1];
        for (int i = 0; i < colorArray.length; i++) {
            //set the default section color
            colorArray[i] = mBackgroundTrackColor;
        }
        this.mCustomTrackSectionColorResult = collector.collectSectionTrackColor(colorArray);
        this.mSectionTrackColorArray = colorArray;
        invalidate();
    }

    /**
     * Replace the number ticks' texts with your's by String[].
     * Usually, the text array's length your set should equals seek bar's tickMarks' count.
     *
     * @param tickTextsArr The array contains the tick text
     */
    public void customTickTexts(@NonNull String[] tickTextsArr) {
        this.mTickTextsCustomArray = tickTextsArr;
        if (mTickTextsArr != null) {
            for (int i = 0; i < mTickTextsArr.length; i++) {
                String tickText;
                if (i < tickTextsArr.length) {
                    tickText = String.valueOf(tickTextsArr[i]);
                } else {
                    tickText = "";
                }
                int index = i;
                if (mR2L) {
                    index = mTicksCount - 1 - i;
                }
                mTickTextsArr[index] = tickText;
                if (mTextPaint != null && mRect != null) {
                    mTextPaint.getTextBounds(tickText, 0, tickText.length(), mRect);
                    mTickTextsWidth[index] = mRect.width();
                }
            }
            invalidate();
        }
    }

    /**
     * Set the custom tick texts' typeface you want.
     *
     * @param typeface The typeface for tickTexts.
     */
    public void customTickTextsTypeface(@NonNull Typeface typeface) {
        this.mTextsTypeface = typeface;
        measureTickTextsBonds();
        requestLayout();
        invalidate();
    }

    /**
     * Set the listener to listen the seeking params changing.
     *
     * @param listener OnSeekChangeListener
     */
    public void setOnSeekChangeListener(@NonNull OnSeekChangeListener listener) {
        this.mSeekChangeListener = listener;
    }

    /**
     * Sets the tick count
     *
     * @param tickCount
     */
    public synchronized void setTickCount(int tickCount) {
        if (mTicksCount < 0 || mTicksCount > 50) {
            throw new IllegalArgumentException("the Argument: TICK COUNT must be limited between (0-50), Now is " + mTicksCount);
        }
        mTicksCount = tickCount;
        collectTicksInfo();
        initTextsArray();
        initSeekBarInfo();
        refreshSeekBarLocation();
        invalidate();
    }

    /**
     * Sets the thumb move to the closed tick after touched up automatically, default true
     *
     * @param adjustAuto true if auto move after touched up.
     */
    public void setThumbAdjustAuto(boolean adjustAuto) {
        mAdjustAuto = adjustAuto;
    }


    /****************** API END ******************/

}