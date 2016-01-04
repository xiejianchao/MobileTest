package com.huhuo.mobiletest.net.callback;

import org.xutils.common.Callback;

import java.io.File;

/**
 * Created by xiejc on 16/1/2.
 */
public abstract class DefaultHttpRequestCallBack<T> implements Callback.CommonCallback.ProgressCallback<T>{


    @Override
    public void onWaiting() {

    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
