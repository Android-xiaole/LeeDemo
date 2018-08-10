package com.lee.leedemo.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.lee.leedemo.bean.AuthBankCardBean;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by le on 2018/8/9.
 */

public class LeeApi {

    public static LeeApi instance;
    private LeeApiService service;

    public LeeApi(OkHttpClient okHttpClient){
        Gson gson = new GsonBuilder()
                //配置你的Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .registerTypeHierarchyAdapter(String.class,STRING)//设置解析的时候null转成""
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                //添加的fastjson解析器序列化数据
//                .addConverterFactory(new Retrofit2ConverterFactory())
                //添加转换器支持返回结果为String类型
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                //添加rxjava2适配器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(LeeApiService.class);
    }

    /**
     * 自定义TypeAdapter ,null对象将被解析成空字符串
     */
    public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        public String read(JsonReader reader) {
            try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return ""; // 原先是返回null，这里改为返回空字符串
                }
                return reader.nextString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
        public void write(JsonWriter writer, String value) {
            try {
                if (value == null) {
                    writer.nullValue();
                    return;
                }
                writer.value(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static LeeApi getInstance(){
        if (instance == null){
            instance = new LeeApi(new OkHttpClient());
        }
        return instance;
    }

    public Observable<AuthBankCardBean> authBankCard(String cardNo){
        return service.authBankCard(cardNo);
    }

    public Observable<String> visitBaidu(){
        return service.visitBaidu();
    }

}
