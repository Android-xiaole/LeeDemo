package com.lee.leedemo.view;

import com.le.base.BaseContract;
import com.lee.leedemo.bean.AuthBankCardBean;

/**
 * Created by le on 2018/8/9.
 */

public interface MainView extends BaseContract.BaseView{

    void onCheckBankCard(AuthBankCardBean authBankCardBean);
    void onVisitBaidu(String res);
}
