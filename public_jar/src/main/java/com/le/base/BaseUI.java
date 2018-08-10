package com.le.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.le.R;
import com.le.utils.MyUtils;

/**
 * Created by sahara on 2017/4/27.
 */

public class BaseUI implements IBaseUI,DialogInterface.OnDismissListener{

    public Context ctx;
    public static final int REQUEST_CODE = 99;
    public static final int RESULT_CODE = 100;
    private MyProgressDialog progressDialog;

    public BaseUI(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public void toActivity(Class<?> clas) {
        ctx.startActivity(new Intent(ctx,clas));
    }

    @Override
    public void showToast(String msg) {
        BaseApplication.showToast(msg);
    }

    @Override
    public void showLongToast(String msg) {
        BaseApplication.showLongToast(msg);
    }

    @Override
    public void showProgress() {
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyUtils.backgroundAlpha((Activity) ctx, (float) 0.5);
//        Intent intent = new Intent(ctx,ProgressActivity.class);
//        ((Activity) ctx).startActivityForResult(intent,REQUEST_CODE);
                if (progressDialog == null){
                    progressDialog = new MyProgressDialog(ctx);
                    progressDialog.setOnDismissListener(BaseUI.this);
                }
                if (!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
        });
    }

    @Override
    public void showProgress(final String msg) {
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyUtils.backgroundAlpha((Activity) ctx, (float) 0.5);
//        Intent intent = new Intent(ctx,ProgressActivity.class);
//        intent.putExtra("msg",msg);
//        ((Activity) ctx).startActivityForResult(intent,REQUEST_CODE);
                if (progressDialog == null){
                    progressDialog = new MyProgressDialog(ctx);
                    progressDialog.setOnDismissListener(BaseUI.this);
                }
                progressDialog.setMessage(msg);
                if (!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
        });
    }

    @Override
    public void showProgress(final boolean cancel) {
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyUtils.backgroundAlpha((Activity) ctx, (float) 0.5);
//        Intent intent = new Intent(ctx,ProgressActivity.class);
//        intent.putExtra("cancel",cancel);
//        ((Activity) ctx).startActivityForResult(intent,REQUEST_CODE);
                if (progressDialog == null){
                    progressDialog = new MyProgressDialog(ctx);
                    progressDialog.setOnDismissListener(BaseUI.this);
                }
                progressDialog.setCanceledOnTouchOutside(cancel);
                if (!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
        });
    }

    @Override
    public void showProgress(final String msg,final boolean cancel) {
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyUtils.backgroundAlpha((Activity) ctx, (float) 0.5);
//        Intent intent = new Intent(ctx,ProgressActivity.class);
//        intent.putExtra("msg",msg);
//        intent.putExtra("cancel",cancel);
//        ((Activity) ctx).startActivityForResult(intent,REQUEST_CODE);
                if (progressDialog == null){
                    progressDialog = new MyProgressDialog(ctx);
                    progressDialog.setOnDismissListener(BaseUI.this);
                }
                progressDialog.setMessage(msg);
                progressDialog.setCanceledOnTouchOutside(cancel);
                if (!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
        });
    }

    @Override
    public void dismissProgress() {
//        if (ProgressActivity.instance!=null){
//            ProgressActivity.instance.finish();
//            ProgressActivity.instance.overridePendingTransition(R.anim.scale_out,R.anim.scale_out);
//            ProgressActivity.instance = null;
//            System.gc();
//        }
//        BaseApplication.post(new Runnable() {
//            @Override
//            public void run() {
//                MyUtils.backgroundAlpha((Activity) ctx, (float) 1.0);
//            }
//        },200);
//        MyUtils.backgroundAlpha((Activity) ctx, (float) 1.0);
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseApplication.post(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog!=null&&progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                },500);//延时500ms是为了增加用户体验，防止响应太快用户看不见
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        MyUtils.backgroundAlpha((Activity) ctx, (float) 1.0);
    }
}
