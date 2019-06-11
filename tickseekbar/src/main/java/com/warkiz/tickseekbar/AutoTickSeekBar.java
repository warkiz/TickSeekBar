package com.warkiz.tickseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * 自带增减按钮的TickSeekBar
 * {@use just lick an relativeLayout to combine an TickSeekBar .}
 * @author poe 2019/05/14.
 */
public class AutoTickSeekBar extends RelativeLayout {
    private static final String TAG  = "AutoTickSeekBar";

    private Button mMinusBtn;
    private Button mPlusBtn;
    private RelativeLayout mContainer;

    private OnActionListener mActionListener;


    public AutoTickSeekBar(Context context) {
        this(context,null);
    }

    public AutoTickSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoTickSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AutoTickSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化试图属性和一些layout.
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        if(getChildCount() >0 ){
            Log.i(TAG,"poe view : before init has view count: "+getChildCount());
        }

        LayoutInflater.from(context).inflate(R.layout.view_auto_tick_seek_bar,this,true);

        if(!isInEditMode()){
            mMinusBtn = findViewById(R.id.left_btn);
            mPlusBtn = findViewById(R.id.right_btn);
            mContainer = findViewById(R.id.auto_content);
            if(null != attrs){
                TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.AutoTickSeekBar, 0,0);

                int mainColor = attributes.getColor(R.styleable.AutoTickSeekBar_action_color, Color.BLUE);

                mMinusBtn.setBackgroundColor(mainColor);
                mPlusBtn.setBackgroundColor(mainColor);
            }


            mMinusBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mActionListener) mActionListener.onMinus(v);
                }
            });

            mPlusBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mActionListener) mActionListener.onPlush(v);
                }
            });
        }


        if(getChildCount() >0 ){
            Log.i(TAG,"poe view : after init merge has view count: "+getChildCount());
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        Log.i(TAG,"poe view: onViewAdded(View child)~");
        if(getChildCount() >0 && child instanceof TickSeekBar){
            Log.i(TAG,"poe view : find tick seek bar !."+getChildCount());

            if(mContainer != null){
                //先从root删除tickseekbar
                removeView(child);
                //添加进二级容器.
                mContainer.addView(child);
                Log.i(TAG,"poe view : find tick seek bar added it to container!."+getChildCount());
                this.invalidate();
            }
        }
    }

    /**
     * to listen the pus or minus action .
     * @param listener
     */
    public void setOnActionListener(OnActionListener listener){
        this.mActionListener = listener;
    }

    /**
     * the listener of action for the AutoTickSeekBar.
     */
    public interface OnActionListener{
        /**
         * 增加一个进度
         * @param view 出发的按钮.
         */
        void onPlush(View view);

        /**
         * 减少一个进度
         * @param view 被点击按钮.
         */
        void onMinus(View view);
    }
}
