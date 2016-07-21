package com.huhuo.mobiletest.net;

import com.google.gson.Gson;
import com.huhuo.mobiletest.model.HttpPage;
import com.huhuo.mobiletest.net.callback.SimpleHttpRequestCallBack;
import com.huhuo.mobiletest.utils.Logger;

import org.xutils.http.RequestParams;

/**
 * Created by xiejianchao on 16/7/20.
 */
public class UploadUtil {

    private static Gson gson = new Gson();

    private static final String TAG = UploadUtil.class.getSimpleName();

    public static void upload(String url,HttpPage page) {

        String json = gson.toJson(page);

//        String url = "http://211.154.22.158/Handler/UpLoadDataHttpWork.ashx";

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("result",json);

        final long start = System.currentTimeMillis();
        HttpHelper.post(params, new SimpleHttpRequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                long end = System.currentTimeMillis();
                Logger.e(TAG,"提交数据状态:" + result);
                Logger.e(TAG,"提交数据耗时:" + (end - start));

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(TAG,"",ex);
            }
        });
    }

}
