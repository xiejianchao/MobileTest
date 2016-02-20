package com.huhuo.mobiletest.net.callback;

import com.huhuo.mobiletest.model.WebPageTestModel;

import org.xutils.common.Callback;

/**
 * Created by xiejianchao on 16/2/20.
 */
public abstract class WebPageTestListener<T> extends DefaultHttpRequestCallBack<T> {

    public void onPrepare(WebPageTestModel model) {

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
}
