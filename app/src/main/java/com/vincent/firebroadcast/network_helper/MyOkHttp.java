package com.vincent.firebroadcast.network_helper;

import android.app.Activity;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyOkHttp {
    // 宣告一個接收回傳結果的程式必須實作的介面
    public interface TaskListener { void onFinished(String result); }
    private TaskListener taskListener;

    private Activity activity;
    private OkHttpClient client;
    private Map<String, String> header = null;
    private Call call;

    public MyOkHttp(Activity activity, TaskListener taskListener) {
        this.activity = activity;
        this.taskListener = taskListener;
    }

    public void execute(String... sendingData) {
        client = new OkHttpClient();

        if (header == null)
            call = getCall(sendingData);
        else
            call = getHeaderCall(header, sendingData);

        sendRequest();
    }

    private Call getCall(String... sendingData) {
        Request request = null;
        if (sendingData.length == 1) { //POST一個帶參數的網址
            RequestBody formBody = new FormBody.Builder().build();
            request = new Request.Builder()
                    .url(sendingData[0])
                    .post(formBody)
                    .build();

        }else if (sendingData.length == 2) { //POST一組JSON字串到某網址
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sendingData[1]);
            request = new Request.Builder()
                    .url(sendingData[0])
                    .post(requestBody)
                    .build();
        }
        return client.newCall(request);
    }

    private Call getHeaderCall(Map<String, String> headerMap, String[] sendingData) {
        Headers header = Headers.of(headerMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sendingData[1]);
        Request request = new Request.Builder()
                .url(sendingData[0])
                .headers(header)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    private void sendRequest() {
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) {
                //連線成功
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                taskListener.onFinished(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (NetworkOnMainThreadException e) {
                    Toast.makeText(activity, "NetworkOnMainThreadException", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //連線失敗
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "連線失敗", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        });
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public void cancel() {
        client.dispatcher().cancelAll();
    }

}
