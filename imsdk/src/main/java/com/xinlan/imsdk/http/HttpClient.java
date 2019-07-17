package com.xinlan.imsdk.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.xinlan.imsdk.Config;
import com.xinlan.imsdk.IMClient;
import com.xinlan.imsdk.util.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 发起Http请求
 *
 */
public class HttpClient {
    public static final String UA = "User-Agent";
    public static final String UA_VALUE = "Android;xinlan_imclient";
    public static final String AUTH_TOKEN = "auth_token";

    public static final long TIMEOUT = 30 * 1000;

    public static OkHttpClient client;

    private static IUpload uploader;

    static {
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS) //连接超时
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS) //读取超时
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS) //写超时
                .build();
        //uploader = new UploadImpl();
    }

    private static Map<Context, Set<ICallback>> callbackRecords = new HashMap<Context, Set<ICallback>>();
    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    public interface ICallback<T> {

        void onError(int errorCode, Exception e);

        void onSuccess(T response);
    }

    public static <T> void sendPostRequest(String path, Map<String, Object> params,
                                           final Context ctx, final ICallback<T> callback, final Class<T> type) {
        addCallback(ctx, callback);

        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                formBuilder.add(key, params.get(key) != null ? params.get(key).toString() : "");
            }//end for each
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(Config.HTTP_SERVER + path).
                        post(formBuilder.build());

        addHeaders(requestBuilder);
        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onError(ctx, StatusCode.NET_ERROR, e, callback);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    String resp = response.body().string();
                    LogUtil.log("http resp = " + resp);
                    try {
                        HttpResp respObj = JSON.parseObject(resp, HttpResp.class);
                        if (respObj.getCode() == StatusCode.SUCCESS) {
                            if(isJsonStr(respObj.getData())){
                                T data = JSON.parseObject(respObj.getData(), type);
                                onSuccess(ctx, callback, data);
                            }else{
                                T data = genBaseType(respObj.getData() , type);
                                onSuccess(ctx, callback, data);
                            }
                        } else {
                            onError(ctx, respObj.getCode(), new Exception(respObj.getMsg()), callback);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onError(ctx, StatusCode.PARSE_DATA_ERROR, e, callback);
                    }
                } else {
                    onError(ctx, response.code(), null, callback);
                }
            }
        });
    }

    private static  <T> T genBaseType(String resp , Class T){
        if(T == String.class){
            return (T)resp;
        }else if(T == Integer.class){
            return (T)(new Integer(resp));
        }else if(T == Float.class){
            return (T)(new Float(resp));
        }else if(T == Boolean.class){
            return (T)(new Boolean(resp));
        }else if(T == Byte.class){
            return (T)(new Byte(resp));
        }else{
            return (T)resp;
        }
    }

    private static boolean isJsonStr(final String str){
        if(TextUtils.isEmpty(str))
            return false;

        String strTrim = str.trim();
        return strTrim.startsWith("{") && strTrim.endsWith("}") || strTrim.startsWith("[") && strTrim.endsWith("]");
    }

    private static <T> void onSuccess(final Context ctx, final ICallback<T> callback, final T data) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callbackRecords.get(ctx) != null && callback != null) {
                    callback.onSuccess(data);
                }
                removeCallback(ctx, callback);
            }
        });
    }

    private static void onError(final Context ctx, final int code,
                                final Exception e, final ICallback callback) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callbackRecords.get(ctx) != null && callback != null) {
                    callback.onError(code, e);
                }
                removeCallback(ctx, callback);
            }
        });
    }

    public static void removeCallback(Context destoryCtx, ICallback cb) {
        if (destoryCtx == null)
            return;

        final Set<ICallback> cbs = callbackRecords.get(destoryCtx);
        if (cbs != null) {
            if (cb == null) {
                LogUtil.log("remove callback " + destoryCtx);
                for (ICallback c : cbs) {
                    LogUtil.log("remove callback cb=  " + c);
                }//end for each
                callbackRecords.remove(destoryCtx);
            } else if (cbs.contains(cb)) {
                cbs.remove(cb);
            }
        }
    }

    public static void addCallback(Context ctx, ICallback cb) {
        if (ctx == null)
            return;

        if (cb != null) {
            Set cbSets = callbackRecords.get(ctx);
            if (cbSets == null) {
                cbSets = new HashSet();
            }
            cbSets.add(cb);
            callbackRecords.put(ctx, cbSets);
        }
    }

    private final static void addHeaders(Request.Builder builder) {
        if (builder == null)
            return;

        builder.addHeader(UA, UA_VALUE);
        if(!TextUtils.isEmpty(IMClient.getInstance().getToken())){
            builder.addHeader(AUTH_TOKEN, IMClient.getInstance().getToken());
        }
    }

    public static IUpload getUploader() {
        if (uploader == null) {
            uploader = new UploadImpl();
        }
        return uploader;
    }
}
