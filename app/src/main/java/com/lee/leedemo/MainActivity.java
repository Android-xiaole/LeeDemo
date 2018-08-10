package com.lee.leedemo;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.le.base.BaseActivity;
import com.lee.leedemo.bean.AuthBankCardBean;
import com.lee.leedemo.presenter.MainPresenter;
import com.lee.leedemo.utils.GetJsonDataUtil;
import com.lee.leedemo.view.MainView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    @BindView(R.id.et_cardNum)
    EditText et_cardNum;
    @BindView(R.id.tv_result)
    TextView tv_result;

    @Override
    protected int setContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void configViews() {

    }

    @OnClick({R.id.btn_baidu,R.id.btn_check})
    public void onClick_Main(View view){
        showProgress();
        switch (view.getId()){
            case R.id.btn_check://校验银行卡
                String cardNum = et_cardNum.getText().toString();
                if (StringUtils.isTrimEmpty(cardNum)){
                    presenter.checkBankCard("6228482112551221151");
                }else{
                    presenter.checkBankCard(cardNum);
                }
                break;
            case R.id.btn_baidu://访问百度
                presenter.visitBaidu();
                break;
        }
    }

    @Override
    public void onCheckBankCard(AuthBankCardBean authBankCardBean) {
        dismissProgress();
        if (authBankCardBean.isValidated()){//正确的银行卡
            String cardType = authBankCardBean.getBank();//银行卡类型
            String json = new GetJsonDataUtil().getJson(this, "bankcard.json");
            try {
                JSONObject jsonObject = new JSONObject(json);
                String bankName = jsonObject.getString(cardType);
                if (authBankCardBean.getCardType().equals("CC")){//判断银行卡是否是信用卡，提现不支持信用卡
                    tv_result.setText("卡号："+authBankCardBean.getKey()+"\n卡类型：信用卡\n"+"所属银行："+bankName);
                }else{
                    tv_result.setText("卡号："+authBankCardBean.getKey()+"\n卡类型：储蓄卡\n"+"所属银行："+bankName);
                }
            } catch (JSONException e) {
                ToastUtils.showShort("解析异常，请重试");
                e.printStackTrace();
            }
        }else{
            ToastUtils.showShort("请输入正确的卡号");
        }
    }

    @Override
    public void onVisitBaidu(String res) {
        tv_result.setText(res);
        dismissProgress();
    }

    @Override
    public void showError(int flag, Throwable e) {
        LogUtils.e(e);
        dismissProgress();
        switch (flag){
            case 0:
                ToastUtils.showShort("校验失败");
                break;
            case 1:
                ToastUtils.showShort("访问百度失败");
                break;
        }
    }

    @Override
    public void complete(int flag) {

    }

}
