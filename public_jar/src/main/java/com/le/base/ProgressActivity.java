package com.le.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.le.R;

/**
 * Created by sahara on 2017/4/28.
 */

public class ProgressActivity extends Activity {

    private TextView tv_msg;

    public static ProgressActivity instance;
    private boolean cancel;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressactivity);
        instance = this;
        Intent intent = getIntent();
        msg = intent.getStringExtra("msg");
        cancel = intent.getBooleanExtra("cancel",true);//设置默认按返回键是可以取消的

        tv_msg = (TextView) findViewById(R.id.tv_msg);
        if (msg!=null){
            tv_msg.setText(msg);
        }
    }

    @Override
    public void onBackPressed() {
        if (cancel){//按返回键可以取消
            setResult(BaseUI.RESULT_CODE);
            super.onBackPressed();
            overridePendingTransition(R.anim.scale_out,R.anim.scale_out);
        }
    }
}
