package com.le.net_thread;

/**
 * Created by sahara on 2016/11/30.
 */

public abstract class MyCallBack {
    public abstract void finish(String result);
    public abstract void inProgress(int current,int total,String fileName);
    public abstract void error(Exception e, String fileName);
}
