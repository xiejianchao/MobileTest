package com.huhuo.mobiletest.net.callback;

import java.io.File;

/**
 * Created by xiejc on 15/12/4.
 */
public interface DownloadRequestCallback extends AsyncHttpCallback {

    public void onSuccess(int statusCode, File file);

    public void onFailure(int statusCode, Throwable e, File file);

    public void onProgress(long currentSize, long totalSize);

}
