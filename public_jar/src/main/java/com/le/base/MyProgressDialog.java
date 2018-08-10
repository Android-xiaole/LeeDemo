package com.le.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.le.R;


/**
 * Created by le on 2018/7/6.
 */

public class MyProgressDialog extends Dialog{

    private TextView tv_msg;

    public MyProgressDialog(@NonNull Context context) {
        super(context, R.style.dialog_progress);
        View contentView = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        ImageView iv_loading = (ImageView) contentView.findViewById(R.id.iv_loading);
        tv_msg = (TextView) contentView.findViewById(R.id.tv_msg);
        AnimationDrawable mAnimation = new AnimationDrawable();
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_01),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_02),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_03),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_04),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_05),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_06),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_07),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_08),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_09),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_10),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_11),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.drawable.loading_12),50);
        mAnimation.setOneShot(false);
        iv_loading.setBackground(mAnimation);
        if (mAnimation != null && !mAnimation.isRunning()) {
            mAnimation.start();
        }
        setContentView(contentView);
    }

    public void setMessage(String msg){
        tv_msg.setText(msg);
    }

}
