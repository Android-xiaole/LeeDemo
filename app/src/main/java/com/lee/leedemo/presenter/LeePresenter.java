package com.lee.leedemo.presenter;

import com.le.base.BaseContract;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by le on 2018/8/9.
 */

public class LeePresenter<T> implements BaseContract.BasePresenter<T> {

    protected T mView;
    private WeakReference<T> weakView;
    protected List<Disposable> listReqs = new ArrayList<>();

    @Override
    public void attachView(T view) {
        weakView = new WeakReference<>(view);
        mView = weakView.get();
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
            weakView.clear();
            weakView = null;
        }
    }

    @Override
    public void cancelAll() {
        for (Disposable disposable : listReqs) {
            disposable.dispose();
        }
    }

    protected void addReqs(Disposable disposable) {
        listReqs.add(disposable);
    }

}
