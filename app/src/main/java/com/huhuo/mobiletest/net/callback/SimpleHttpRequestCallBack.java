package com.huhuo.mobiletest.net.callback;

import org.xutils.common.Callback;

/**
 * Created by xiejc on 16/1/2.
 */
public abstract class SimpleHttpRequestCallBack<T> implements Callback.CommonCallback.ProgressCallback<T>{


    @Override
    public void onStarted() {

    }

    @Override
    public void onWaiting() {

    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {

    }
}
