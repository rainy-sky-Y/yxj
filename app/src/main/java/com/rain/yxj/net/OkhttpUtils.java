package com.rain.yxj.net;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpUtils {
    private OkhttpUtils(){}
    private static OkhttpUtils instance = new OkhttpUtils();
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler(Looper.getMainLooper());

    public static OkhttpUtils getInstance() {
        return instance;
    }

    public void doGet(String url, CallBack callBack){
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = null;
                try {
                    string = response.body().string();
                }catch (Exception e){
                    e.printStackTrace();
                }
                String finalString = string;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(finalString);
                    }
                });
            }
        });
    }
}
