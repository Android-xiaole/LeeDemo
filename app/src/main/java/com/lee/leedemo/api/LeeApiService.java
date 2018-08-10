package com.lee.leedemo.api;

import com.lee.leedemo.bean.AuthBankCardBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by le on 2018/8/9.
 */

public interface LeeApiService {

    @GET("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardBinCheck=true")
    Observable<AuthBankCardBean> authBankCard(@Query("cardNo") String cardNo);

    @GET("https://www.baidu.com/")
    Observable<String> visitBaidu();
}
