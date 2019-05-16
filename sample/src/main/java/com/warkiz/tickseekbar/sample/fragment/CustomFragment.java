package com.warkiz.tickseekbar.sample.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.warkiz.tickseekbar.AutoTickSeekBar;
import com.warkiz.tickseekbar.TickSeekBar;
import com.warkiz.tickseekbar.sample.R;

/**
 * created by zhuangguangquan on 2018/6/6
 */

public class CustomFragment extends BaseFragment {

    private AutoTickSeekBar mAutoTickSeekBar;
    private TickSeekBar mTickSeekBar;

    @Override
    protected int getLayoutId() {
        return R.layout.custom;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAutoTickSeekBar = view.findViewById(R.id.auto_layout);
        mTickSeekBar = view.findViewById(R.id.tick_seek_bar_auto);

        mAutoTickSeekBar.setOnActionListener(new AutoTickSeekBar.OnActionListener() {
            @Override
            public void onPlush(View view) {
                mTickSeekBar.setProgress(mTickSeekBar.getProgressFloat()+1);
            }

            @Override
            public void onMinus(View view) {
                mTickSeekBar.setProgress(mTickSeekBar.getProgressFloat()-1);
            }
        });
    }
}
