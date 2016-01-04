package com.huhuo.mobiletest.net.callback;

/**
 * Created by xiejc on 15/12/9.
 */
public interface TextHttpCallback extends AsyncHttpCallback {

    public void onStart();

    public void onSuccess(String responseInfo);

    public void onFailure(Exception error, String msg);
}
