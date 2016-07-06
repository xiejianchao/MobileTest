package com.huhuo.mobiletest.net;

import android.content.Context;


import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.net.callback.DefaultHttpRequestCallBack;
import com.huhuo.mobiletest.net.callback.SimpleHttpRequestCallBack;

import org.xutils.HttpManager;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by xiejc on 16/1/13.
 */
public class HttpHelper {

    final static HttpManager http = x.http();

    private static Context context = MobileTestApplication.getInstance().getApplicationContext();

    public static void post(RequestParams params,SimpleHttpRequestCallBack<String> callback){
//        params.setSslSocketFactory(SSLHelper.getSSLSocketFactory(context));
        http.post(params, callback);
    }

    public static void get(RequestParams params,SimpleHttpRequestCallBack<String> callback){
//        params.setSslSocketFactory(SSLHelper.getSSLSocketFactory(context));
        http.get(params, callback);
    }

    /**
     * 上传文件，默认使用multipart表单上传文件
     * @param params
     * @param callback
     */
    public static void upload(RequestParams params,SimpleHttpRequestCallBack<String> callback) {
        upload(params,true,callback);
    }

    public static void upload(RequestParams params,boolean isMultipart,Callback.CommonCallback.ProgressCallback callback) {
        // 使用multipart表单上传文件
//        params.setSslSocketFactory(SSLHelper.getSSLSocketFactory(context));
        params.setMultipart(isMultipart);
        http.post(params, callback);
    }

    /**
     * 下载文件到指定目录，同时监听下载状态
     * @param url
     * @param savePath
     * @param callback
     */
    public static void download(String url,String savePath, DefaultHttpRequestCallBack<File> callback) {
        RequestParams req = new RequestParams(url);
        req.setAutoRename(false);
        req.setAutoResume(true);
        req.setSaveFilePath(savePath);
        http.request(HttpMethod.GET, req, callback);
    }

}
