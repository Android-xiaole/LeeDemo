package com.le.utils;

import com.le.net.HttpManagerWithCookie;
import com.le.net.IHttpHandler;

import java.util.Map;

/**
 * Created by admin on 2017/9/2.
 */

public class HttpManager {

    public static HttpManager manager;
    public static HttpManagerWithCookie httpManager;

    public static HttpManager getInstance(){
        if (manager == null){
            manager = new HttpManager();
            httpManager = new HttpManagerWithCookie();
        }
        return manager;
    }

    public void get(String url,Map<String,String> parames,final StringCallBack callBack){
        StringBuilder finalUrl = new StringBuilder(url+"?");
        if (parames!=null){
            for (Map.Entry<String,String> entry:parames.entrySet()) {
                finalUrl.append(entry.getKey()+"="+entry.getValue());
            }
        }
        httpManager.makeRequest(finalUrl.toString(), new IHttpHandler() {
            @Override
            public void start() throws Exception {

            }

            @Override
            public void done() throws Exception {
                callBack.onResponse(res.getString());
            }

            @Override
            public void error(Exception e) throws Exception {
                callBack.onError(e);
                super.error(e);
            }
        });
    }

    public void postForm(String url, final Map<String,String> parames, final StringCallBack callBack){
        httpManager.makeRequest(url, new IHttpHandler() {
            @Override
            public void start() throws Exception {
                req.writeForm(parames);
            }

            @Override
            public void done() throws Exception {
                callBack.onResponse(res.getString());
            }

            @Override
            public void error(Exception e) throws Exception {
                callBack.onError(e);
                super.error(e);
            }
        });
    }

    public void postJson(String url, final Object json, final StringCallBack callBack){
        httpManager.makeRequest(url, new IHttpHandler() {
            @Override
            public void start() throws Exception {
                req.writeJson(json);
            }

            @Override
            public void done() throws Exception {
                callBack.onResponse(res.getString());
            }

            @Override
            public void error(Exception e) throws Exception {
                callBack.onError(e);
                super.error(e);
            }
        });
    }

    public static HttpManager setHeaders(Map<String,String> headers){
        httpManager.setHeaders(headers);
        return manager;
    }

}
