package com.warkiz.tickseekbar.sample.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warkiz.tickseekbar.TextPosition;
import com.warkiz.tickseekbar.TickMarkType;
import com.warkiz.tickseekbar.TickSeekBar;
import com.warkiz.tickseekbar.sample.R;

/**
 * created by zhuangguangquan on 2018/6/6
 */

public class JavaBuildFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.java_build, container, false);
        final LinearLayout content = (LinearLayout) root.findViewById(R.id.java_build);

        TextView textView1 = getTextView();
        textView1.setText("continuous");
        content.addView(textView1);
        //CONTINUOUS
        TickSeekBar continuous = TickSeekBar
                .with(getContext())
                .max(200)
                .min(10)
                .progress(33)
                .thumbColorStateList(getResources().getColorStateList(R.color.selector_thumb_color))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray))
                .trackBackgroundSize(2)
                .thumbTextPosition(TextPosition.BELOW)
                .thumbTextColor(getResources().getColor(R.color.color_gray))
                .build();

        content.addView(continuous);

        TextView textView2 = getTextView();
        textView2.setText("continuous_texts_ends");
        content.addView(textView2);
        //CONTINUOUS_TEXTS_ENDS
        TickSeekBar continuous2TickTexts = TickSeekBar
                .with(getContext())
                .max(200)
                .min(10)
                .progress(33)
                .tickCount(2)
                .showTickMarksType(TickMarkType.NONE)
                .showTickTextsPosition(TextPosition.ABOVE)
                .thumbDrawable(getResources().getDrawable(R./*mipmap.ic_launcher*/drawable.selector_thumb_drawable))
                .thumbSize(18)
                .trackProgressColor(getResources().getColor(R.color.colorAccent))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray))
                .trackBackgroundSize(2)
                .build();
        content.addView(continuous2TickTexts);

        TextView textView22 = getTextView();
        textView22.setText("continuous_texts_ends_custom_ripple_thumb");
        content.addView(textView22);
        //CONTINUOUS_TEXTS_ENDS
        TickSeekBar continuous_texts_ends_custom_thumb = TickSeekBar
                .with(getContext())
                .max(200)
                .min(10)
                .progress(33)
                .tickCount(2)
                .showTickMarksType(TickMarkType.NONE)
                .showTickTextsPosition(TextPosition.ABOVE)
                .thumbDrawable(getResources().getDrawable(R./*mipmap.ic_launcher*/drawable.selector_thumb_ripple_drawable))
                .thumbSize(22)
                .trackProgressColor(getResources().getColor(R.color.colorAccent))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray))
                .trackBackgroundSize(2)
                .build();
        content.addView(continuous_texts_ends_custom_thumb);

        TextView textView3 = getTextView();
        textView3.setText("continuous_texts_ends_custom");
        content.addView(textView3);
        TickSeekBar continuous2TickTexts1 = TickSeekBar
                .with(getContext())
                .max(200)
                .min(10)
                .progress(33)
                .tickCount(2)
                .showTickMarksType(TickMarkType.NONE)
                .showTickTextsPosition(TextPosition.ABOVE)
                .tickTextsArray(R.array.last_next_length_2)
                .thumbColor(Color.parseColor("#ff0000"))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray))
                .trackBackgroundSize(2)
                .build();
        content.addView(continuous2TickTexts1);

        TextView textView4 = getTextView();
        textView4.setText("discrete_ticks");
        content.addView(textView4);
        //DISCRETE_TICKS
        TickSeekBar discrete_ticks = TickSeekBar
                .with(getContext())
                .max(200)
                .min(10)
                .progress(33)
                .tickCount(7)
                .tickMarksSize(15)
                .tickMarksDrawable(getResources().getDrawable(R.drawable.selector_tick_marks_drawable))
                .thumbColor(Color.parseColor("#ff0000"))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray))
                .trackBackgroundSize(2)
                .build();
        content.addView(discrete_ticks);

        TextView textView5 = getTextView();
        textView5.setText("discrete_ticks_texts");
        content.addView(textView5);
        //DISCRETE_TICKS_TEXTS
        TickSeekBar discrete_ticks_texts = TickSeekBar
                .with(getContext())
                .max(200)
                .min(10.2f)
                .progressValueFloat(true)
                .progress(33)
                .tickCount(7)
                .showTickMarksType(TickMarkType.DIVIDER)
                .tickMarksColor(getResources().getColor(R.color.color_blue))
                .tickMarksSize(6)//dp
                .tickTextsSize(13)//sp
                .showTickTextsPosition(TextPosition.ABOVE)
                .tickTextsColorStateList(getResources().getColorStateList(R.color.selector_tick_texts_3_color))
                .thumbColor(Color.parseColor("#ff0000"))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray))
                .trackBackgroundSize(2)
                .build();
        content.addView(discrete_ticks_texts);


        TextView textView6 = getTextView();
        textView6.setText("discrete_ticks_texts_custom");
        content.addView(textView6);
        String[] array = {"A", "B", "C", "D", "E", "F", "G"};
        TickSeekBar discrete_ticks_texts1 = TickSeekBar
                .with(getContext())
                .max(200)
                .min(10)
                .progress(33)
                .tickCount(7)
                .showTickMarksType(TickMarkType.SQUARE)
                .tickTextsArray(array)
                .showTickTextsPosition(TextPosition.ABOVE)
                .tickTextsColorStateList(getResources().getColorStateList(R.color.selector_tick_texts_3_color))
                .thumbColor(Color.parseColor("#ff0000"))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray))
                .trackBackgroundSize(2)
                .build();
        content.addView(discrete_ticks_texts1);

        TextView textView8 = getTextView();
        textView8.setText("discrete_ticks_texts_with_divider_bellow");
        content.addView(textView8);
        //DISCRETE_TICKS_TEXTS
        TickSeekBar discrete_ticks_texts_with_divider_bellow = TickSeekBar
                .with(getContext())
                .max(200)
                .min(10.2f)
                .progressValueFloat(true)
                .progress(33)
                .tickCount(7)
                .showTickMarksType(TickMarkType.DIVIDER_BELOW)
                .tickMarksColor(getResources().getColor(R.color.color_blue))
                .tickMarksSize(10)//dp
                .tickTextsSize(13)//sp
                .showTickTextsPosition(TextPosition.BELOW)
                .tickTextsColorStateList(getResources().getColorStateList(R.color.selector_tick_texts_3_color))
                .thumbColor(Color.parseColor("#ff0000"))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray))
                .trackBackgroundSize(2)
                .build();
        content.addView(discrete_ticks_texts_with_divider_bellow);

        TextView textView7 = getTextView();
        textView7.setText("discrete_ticks_texts_ends");
        content.addView(textView7);

        String[] array_ends = {"A", "", "", "", "", "", "G"};
        TickSeekBar discrete_ticks_texts_ends = TickSeekBar
                .with(getContext())
                .max(200)
                .min(10)
                .progress(33)
                .tickCount(7)
                .showTickMarksType(TickMarkType.OVAL)
                .tickMarksColor(getResources().getColorStateList(R.color.selector_tick_marks_color))
                .tickTextsArray(array_ends)
                .showTickTextsPosition(TextPosition.BELOW)
                .tickTextsColorStateList(getResources().getColorStateList(R.color.selector_tick_texts_3_color))
                .thumbColor(Color.parseColor("#ff0000"))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.color_blue))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_pink))
                .trackBackgroundSize(2)
                .build();
        content.addView(discrete_ticks_texts_ends);

        return root;
    }

    @NonNull
    private TextView getTextView() {
        TextView textView = new TextView(getContext());
        int padding = dp2px(getContext(), 16);
        textView.setPadding(padding, padding, padding, 0);
        return textView;
    }

    public int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

}
