package com.le.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.le.R;
import com.le.utils.MyUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * Created by sahara on 2017/4/27.
 */

public abstract class BaseActivity<T extends BaseContract.BasePresenter> extends AutoLayoutActivity implements IBaseUI, BackHandledInterface {

    private BaseUI baseUI;
    protected ArrayList<Fragment> frags = new ArrayList<>();
    protected FragmentManager fragmentManger = getSupportFragmentManager();
    protected FragmentTransaction fragmentTransaction;
    protected Fragment currentFrg = null;
    private BackHandledFragment mBackHandedFragment;
    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init something
        baseUI = new BaseUI(this);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏 如果activity继承AppCompatActivity就无效
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//取消横屏
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(0x00, 0x00, 0x00));
        }
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        if (setContentViewID() != 0) {
            setContentView(setContentViewID());
        }
        ButterKnife.bind(this);
        presenter = getPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }

        initDatas();
        configViews();
    }

    protected abstract int setContentViewID();

    protected abstract T getPresenter();

    protected abstract void initDatas();

    protected abstract void configViews();

    protected void loadFragment(int position) {
        Fragment f = frags.get(position);
        fragmentTransaction = fragmentManger.beginTransaction();
        if (currentFrg != null) {
            fragmentTransaction.hide(currentFrg);
        }
        if (!f.isAdded()) {
            fragmentTransaction.add(R.id.frameLayout, f);
        } else {
            fragmentTransaction.show(f);
        }
        currentFrg = f;
        fragmentTransaction.commit();
    }

    protected void loadFragment(Fragment f) {
        fragmentTransaction = fragmentManger.beginTransaction();
        if (currentFrg != null) {
            fragmentTransaction.hide(currentFrg);
        }
        if (!f.isAdded()) {
            fragmentTransaction.add(R.id.frameLayout, f);
        } else {
            fragmentTransaction.show(f);
        }
        currentFrg = f;
        fragmentTransaction.commit();
    }

    protected void removeFragment(Fragment f) {
        fragmentTransaction.remove(f);//remove之后这个fragment事例会被销毁的，所以想重新显示需要new一个对象再调add方法
    }

    @Override
    public void onBackPressed() {//如果activity加载的是fragment写了这个方法就可以在fragment里面监听到返回键 事件的传递顺序是 先传递到父activity再传递到子fragment
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (fragmentManger.getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                fragmentManger.popBackStack();
            }
        }
    }

    /**
     * this.getCurrentFocus().getWindowToken() 有时拿的windowToken为空，没有获取到焦点，所以会抛出异常
     * @param view
     */
    public void hideSoftKeyBoard(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.cancelAll();
            presenter.detachView();
        }
        System.gc();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BaseUI.REQUEST_CODE && resultCode == BaseUI.RESULT_CODE) {
            BaseApplication.post(new Runnable() {
                @Override
                public void run() {
                    MyUtils.backgroundAlpha(BaseActivity.this, (float) 1.0);
                }
            }, 200);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void toActivity(Class<?> clas) {
        baseUI.toActivity(clas);
    }

    @Override
    public void showToast(String msg) {
        baseUI.showToast(msg);
    }

    @Override
    public void showLongToast(String msg) {
        baseUI.showLongToast(msg);
    }

    @Override
    public void showProgress() {
        baseUI.showProgress();
    }

    @Override
    public void showProgress(String msg) {
        baseUI.showProgress(msg);
    }

    @Override
    public void showProgress(boolean cancel) {
        baseUI.showProgress(cancel);
    }

    @Override
    public void showProgress(String msg, boolean cancel) {
        baseUI.showProgress(msg, cancel);
    }

    @Override
    public void dismissProgress() {
        baseUI.dismissProgress();
    }

}
