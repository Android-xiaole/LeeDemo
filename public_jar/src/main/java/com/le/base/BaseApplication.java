package com.le.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.le.BuildConfig;
import com.le.utils.MLog;
import com.le.utils.UtilFile;

/**
 * Created by sahara on 2017/4/27.
 */

public class BaseApplication extends Application{

    public static BaseApplication baseApplication;
    public static Context appContext;

//    public static SharedPreferences sPreferences;//共享参数对象
    public static Resources mResources;//方便获取资源id
    public static String mDeviceToken;//设备唯一ID
    public static String mAppVersion;//app版本号 1.0.1

    public static Handler mHandler;
    public static Toast mToast;
    public static Toast mLongToast;

    public static String mAppName = "xjd";//replace you app name
    public static String mCompany = "/chile";//replace you company name
    public static String mSdPath;// sd
    public static String mDownLoadPath;
    public static String mCachePath;
    public static String mErrLogPath;

    @Override
    public void onCreate() {
        super.onCreate();
//        //默认使用的高度是设备的可用高度，也就是不包括状态栏和底部的操作栏的，如果你希望拿设备的物理高度进行百分比化
//        AutoLayoutConifg.getInstance().useDeviceSize();

        Utils.init(this);
        LogUtils.Config config = LogUtils.getConfig()
                .setLogSwitch(BaseConfig.DEBUG)// 设置log总开关，包括输出到控制台和文件，默认开
//                .setConsoleSwitch(BuildConfig.DEBUG)// 设置是否输出到控制台开关，默认开
//                .setGlobalTag(null)// 设置log全局标签，默认为空
//                // 当全局标签不为空时，我们输出的log全部为该tag，
//                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
//                .setLogHeadSwitch(true)// 设置log头信息开关，默认为开
//                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
//                .setDir("")// 当自定义路径为空时，写入应用的/cache/log/目录中
//                .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
//                .setConsoleFilter(LogUtils.V)// log的控制台过滤器，和logcat过滤器同理，默认Verbose
//                .setFileFilter(LogUtils.V)// log文件过滤器，和logcat过滤器同理，默认Verbose
        ;
        LogUtils.d(config.toString());

        System.gc();
        baseApplication = this;
        appContext = baseApplication.getApplicationContext();

//        sPreferences = getSharedPreferences("Flag",MODE_PRIVATE);
        mResources = getResources();
        mDeviceToken = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            mAppVersion = info.versionName + "." + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            MLog.e("获取应用版本号失败！");
        }

        mHandler = new Handler(getMainLooper());
        mToast = Toast.makeText(appContext, "", Toast.LENGTH_SHORT);
        mLongToast = Toast.makeText(appContext,"",Toast.LENGTH_LONG);
        mSdPath = UtilFile.isSdcardExist();
        mErrLogPath = mSdPath + mCompany + "/" + mAppName
                + "/Log/";
        mDownLoadPath = mSdPath + mCompany + "/" + mAppName
                + "/DownLoad/";
        mCachePath = mSdPath + mCompany + "/" + mAppName
                + "/Cache/";
    }

    /**
     * handler.post() 这里为了try-catch
     */
    public static void post(Runnable r) {
        post(r, 0);
    }

    /**
     * handler.post() 这里为了try-catch
     */
    public static void post(Runnable r, int time) {
        try {
            mHandler.postDelayed(r, time);
        } catch (Exception e) {
            e.printStackTrace();
            MLog.e(baseApplication.getClass().getSimpleName()+":post-"+e);
        }
    }

    /**
     * 显示 Toast 放这里是为了在任何地方都可用
     *
     * @param msg
     * 可以为空
     */
    public static void showToast(final Object msg) {
        post(new Runnable() {
            @Override
            public void run() {
                mToast.setText(msg + "");
                mToast.show();
            }
        });
    }

    public static void showLongToast(final Object msg){
        post(new Runnable() {
            @Override
            public void run() {
                mLongToast.setText(msg + "");
                mLongToast.show();
            }
        });
    }
}
