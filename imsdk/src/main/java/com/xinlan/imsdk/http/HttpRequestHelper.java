package com.xinlan.imsdk.http;

import com.xinlan.imsdk.Config;
import com.xinlan.imsdk.util.LogUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequestHelper {
    public static final String UA = "User-Agent";
    public static final String UA_VALUE = "Android;xinlan_imclient";
    public static final String TOKEN = "token";

    public static OkHttpClient client = new OkHttpClient();

    public static void sendPostRequest(final String path , Map<String,Object> params){
        FormBody.Builder formBuilder = new FormBody.Builder();
        if(params != null){
            for(String key :params.keySet()){
                formBuilder.add(key , params.get(key).toString());
            }//end for each
        }
        Request.Builder requestBuilder = new Request.Builder().url(Config.HTTP_SERVER + path).
                post(formBuilder.build());

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String resp = response.body().string();
                    LogUtil.log("http resp = " +resp);
                }else{

                }
            }
        });
    }


    private final static void addHeaders(Request.Builder builder){
        if(builder == null)
            return;
        builder.addHeader(UA , UA_VALUE);
        builder.addHeader(TOKEN , null);
    }
}
