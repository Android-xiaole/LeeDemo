package com.le.base;

/**
 * Created by sahara on 2017/4/28.
 */

public interface IBaseUI {
    void toActivity(Class<?> clas);

    void showToast(String msg);

    void showLongToast(String msg);

    void showProgress();

    void showProgress(String msg);

    void showProgress(boolean cancel);

    void showProgress(String msg,boolean cancel);

    void dismissProgress();
}
