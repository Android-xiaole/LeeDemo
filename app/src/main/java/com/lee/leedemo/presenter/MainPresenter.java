package com.lee.leedemo.presenter;

import com.lee.leedemo.api.LeeApi;
import com.lee.leedemo.bean.AuthBankCardBean;
import com.lee.leedemo.view.MainView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by le on 2018/8/9.
 */

public class MainPresenter extends LeePresenter<MainView>{

    public void checkBankCard(String cardNum){
        LeeApi.getInstance().authBankCard(cardNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .subscribe(new Observer<AuthBankCardBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addReqs(d);
                    }

                    @Override
                    public void onNext(AuthBankCardBean authBankCardBean) {
                        mView.onCheckBankCard(authBankCardBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(0,e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void visitBaidu(){
        LeeApi.getInstance().visitBaidu()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addReqs(d);
                    }

                    @Override
                    public void onNext(String s) {
                        mView.onVisitBaidu(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(1,e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
