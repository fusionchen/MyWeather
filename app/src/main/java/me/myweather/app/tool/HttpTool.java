package me.myweather.app.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2017/8/11.
 */

public class HttpTool {
    private static HashSet<Call> links = new HashSet<>();
    private OkHttpClient client;
    private HttpTool.Callback callback;
    public HttpTool(){
        client = new OkHttpClient();
    }
    public static HttpTool getInstance(HttpTool.Callback callback) {
        HttpTool httpTool = new HttpTool();
        httpTool.callback = callback;
        return httpTool;
    }
    public void setCallback(HttpTool.Callback callback) {
        this.callback = callback;
    }
    public void sendGet(String url){
        if(callback == null)
            throw new NullPointerException("Callback should not be null.");
        Request request = getRequestBuilder().url(url).get().build();
        Call call = client.newCall(request);
        links.add(call);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                links.remove(call);
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                links.remove(call);
                String result = getStringByResponse(response);
                callback.onResponse(result);
            }
        });
    }
    private Request.Builder getRequestBuilder() {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder = requestBuilder
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                .addHeader("Cache-Control", "max-age=0");
        return requestBuilder;
    }
    private String getStringByResponse(Response response){
        try {
            String str = response.body().string();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void disconnectAll() {
        for(Call call : links){
            if(call.isExecuted())
                call.cancel();
        }
        links.clear();
    }

    public static interface Callback {
        public void onFailure(Call call, IOException e);
        public void onResponse(String response);
    }
}
