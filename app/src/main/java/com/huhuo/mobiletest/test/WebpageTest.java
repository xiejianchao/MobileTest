package com.huhuo.mobiletest.test;

import android.os.SystemClock;

import com.huhuo.mobiletest.model.WebPageTestModel;
import com.huhuo.mobiletest.net.callback.DefaultHttpRequestCallBack;
import com.huhuo.mobiletest.utils.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by xiejianchao on 16/2/20.
 */
public class WebPageTest {

    private static final String TAG = WebPageTest.class.getSimpleName();

    private WebPageTestListener listener;

    private ArrayList<WebPageTestModel> list;
    private int nextTestItem = 0;

    public void setTestListener(WebPageTestListener listener){
        this.listener = listener;
    }

    public WebPageTest(ArrayList<WebPageTestModel> list) {
        this.list = list;
    }

    public void test(){
        nextTestItem = 0;
        execute(list.get(nextTestItem));
    }

    public void cancel(){
        nextTestItem = list.size();
    }

    private void execute(final WebPageTestModel model) {
        final String url = model.getUrl();
        Logger.d(TAG, "测试URL:" + url);
        RequestParams params = new RequestParams(url);

        if (listener != null) {
            listener.onPrepare(model);
        }

        SystemClock.sleep(500);

        x.http().get(params, new DefaultHttpRequestCallBack<String>() {

            @Override
            public void onStarted() {
                if (listener != null) {
                    listener.onStarted(model);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (listener != null) {
                    listener.onError(model,ex,isOnCallback);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (listener != null) {
                    listener.onLoading(model, total, current, isDownloading);
                }
            }

            @Override
            public void onSuccess(String result) {
                if (listener != null) {
                    listener.onSuccess(model, result);
                }

                if (nextTestItem <= list.size() - 1) {
                    nextTestItem ++;
                    if (nextTestItem == list.size()) {
                        if (listener != null) {
                            listener.onEnd();
                        }
                        return;
                    }
                    execute(list.get(nextTestItem));
                }

            }

            @Override
            public void onFinished() {
                super.onFinished();
                if (listener != null) {
                    listener.onFinished(model);
                }
            }
        });
    }

    public interface WebPageTestListener {
        void onPrepare(WebPageTestModel model);

        void onStarted(WebPageTestModel model);

        void onError(WebPageTestModel model, Throwable e, boolean isOnCallback);

        void onLoading(WebPageTestModel model, long totalSize, long bytesWritten, boolean isDownloading);

        void onSuccess(WebPageTestModel model, String result);

        void onFinished(WebPageTestModel model);

        void onEnd();

    }
}
