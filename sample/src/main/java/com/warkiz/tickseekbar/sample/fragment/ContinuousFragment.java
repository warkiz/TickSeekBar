package com.warkiz.tickseekbar.sample.fragment;

import android.view.View;
import android.widget.TextView;

import com.warkiz.tickseekbar.OnSeekCallBack;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;
import com.warkiz.tickseekbar.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhuangguangquan on 2018/6/6
 */

public class ContinuousFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.continuous;
    }


    @Override
    protected void initView(View root) {
        //scale
        TickSeekBar scale = root.findViewById(R.id.scale);
        scale.setDecimalScale(4);
        //set listener
        TickSeekBar listenerSeekBar = root.findViewById(R.id.listener);
        final TextView states = root.findViewById(R.id.states);
        states.setText("states: ");
        final TextView progress = root.findViewById(R.id.progress);
        progress.setText("progress: " + listenerSeekBar.getProgress());
        final TextView progress_float = root.findViewById(R.id.progress_float);
        progress_float.setText("progress_float: " + listenerSeekBar.getProgressFloat());
        final TextView from_user = root.findViewById(R.id.from_user);
        from_user.setText("from_user: ");
        listenerSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                states.setText("states: onSeeking");
                progress.setText("progress: " + seekParams.progress);
                progress_float.setText("progress_float: " + seekParams.progressFloat);
                from_user.setText("from_user: " + seekParams.fromUser);
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {
                states.setText("states: onStart");
                progress.setText("progress: " + seekBar.getProgress());
                progress_float.setText("progress_float: " + seekBar.getProgressFloat());
                from_user.setText("from_user: true");
            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
                states.setText("states: onStop");
                progress.setText("progress: " + seekBar.getProgress());
                progress_float.setText("progress_float: " + seekBar.getProgressFloat());
                from_user.setText("from_user: false");
            }

        });

        //test delegate for thumb text .
        TickSeekBar delegateSeekBar = root.findViewById(R.id.delegate);
        final List<String> rateArray = new ArrayList();
        rateArray.add("5");
        rateArray.add("15");
        rateArray.add("55");
        rateArray.add("115");
        rateArray.add("225");
        rateArray.add("255");

        if(rateArray!=null && rateArray.size()>0){
            delegateSeekBar.setOnSeekCallback(new OnSeekCallBack() {
                @Override
                public String getThumbText(float progress) {
                    int index = Math.round(progress);
                    if(index<rateArray.size()){
                        return rateArray.get(index);
                    }
                    return rateArray.get(rateArray.size()-1);
                }
            });
            //为了保持对齐 : index会滑到size最大值，而数组是从0开始的
            delegateSeekBar.setMax(rateArray.size()-1);
            delegateSeekBar.setProgress(0);
        }

    }
}
