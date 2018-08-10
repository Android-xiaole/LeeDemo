package com.le.utils;

import android.util.Log;

import com.le.base.BaseConfig;

/**
 * Created by sahara on 2017/4/27.
 */

public class MLog {

    public static void e(String tag,String msg){
        if (BaseConfig.DEBUG){
            Log.e(tag,msg);
        }
    }

    public static void e(String msg){
        if (BaseConfig.DEBUG){
            Log.e("test",msg);
        }
    }

    public static void w(String tag,String msg){
        if (BaseConfig.DEBUG){
            Log.w(tag,msg);
        }
    }

    public static void w(String msg){
        if (BaseConfig.DEBUG){
            Log.w("test",msg);
        }
    }

    public static void i(String tag,String msg){
        if (BaseConfig.DEBUG){
            Log.i(tag,msg);
        }
    }

    public static void i(String msg){
        if (BaseConfig.DEBUG){
            Log.i("test",msg);
        }
    }

    public static void d(String tag,String msg){
        if (BaseConfig.DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void d(String msg){
        if (BaseConfig.DEBUG){
            Log.d("test",msg);
        }
    }

}
