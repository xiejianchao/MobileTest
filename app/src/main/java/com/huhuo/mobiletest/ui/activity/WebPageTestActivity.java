package com.huhuo.mobiletest.ui.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.model.NetSpeedModel;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.ToastUtil;
import com.timqi.sectorprogressview.SectorProgressView;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


@ContentView(R.layout.activity_web_page_test)
public class WebPageTestActivity extends BaseActivity {

    private static final String TAG = WebPageTestActivity.class.getSimpleName();

    private DecimalFormat df;
    private long currentSize = 0;
    private long lastTotalSize = 0;
    private long lastSpeed = 0;
    private long fileTotalSize = 0;
    private String showStr;

    private HashMap<Integer,Long> hashMap = new HashMap<Integer,Long>();

    @Override
    protected void init(Bundle savedInstanceState) {

        df = new DecimalFormat("#.##");

    }


    @Event(value = R.id.btn_download)
    private void downloadClick(View view) {

        String url = "http://www.sohu.com";
        RequestParams req = new RequestParams(url);
//        req.setAutoRename(false);
//        req.setAutoResume(true);
        File saveFile = new File(Environment.getExternalStorageDirectory() + "/textXUtils3");
        if (!saveFile.exists()) {
            saveFile.mkdirs();
            Logger.e(TAG, "XUTILS目录不存在，创建...");
        }
        String savePath = saveFile.getAbsolutePath() + "/360.apk";
        Logger.d(TAG, "save path : " + savePath);
        req.setSaveFilePath(savePath);
//        textXUtilsFinal(req);


        testXUtils2();

    }

    private void testXUtils2() {
        RequestParams params = new RequestParams("http://www.youku.com");
//        x.http().get(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Logger.d(TAG,"CONTENT:" + result);
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Logger.d(TAG, "CONTENT:" + ex);
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                Logger.d(TAG, "CONTENT:" + cex);
//            }
//
//            @Override
//            public void onFinished() {
//                Logger.d(TAG, "onFinished:");
//            }
//        });


        x.http().get(params, new Callback.CommonCallback.ProgressCallback<String>() {

            String percentStr = null;

            @Override
            public void onError(Throwable e, boolean isOnCallback) {

                ToastUtil.showShortToast("下载失败，error:" + e == null ? "" : e
                        .getMessage());
                if (e != null) {
                    Logger.e(TAG, e.toString());
                } else {
                    Logger.e(TAG, "网络错误，下载失败");
                }
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
                Logger.d(TAG, "onCancelled");
            }

            @Override
            public void onFinished() {
                Logger.d(TAG, "onFinished");
            }

            @Override
            public void onWaiting() {
                Logger.d(TAG, "onWaiting");
            }

            @Override
            public void onStarted() {
                Logger.d(TAG, "onStarted");
            }

            @Override
            public void onLoading(long totalSize, long bytesWritten, boolean
                    isDownloading) {

                currentSize = bytesWritten;
                fileTotalSize = totalSize;
                float percent = ((float) bytesWritten / totalSize) * 100;
                percentStr = df.format(percent) + "%";

                showStr = "总大小：" + totalSize + " \n当前下载：" + bytesWritten + " " + "\n进度："
                        + percentStr;

                Logger.d(TAG,"onLoading:" + showStr);

            }

            @Override
            public void onSuccess(String result) {
                ToastUtil.showShortToast("下载成功");
                Logger.d(TAG, "下载网页成功：" + result);
                if (hashMap.size() <= 0) {
                    return;
                }
                final NetSpeedModel speedModel = getSpeedModel(hashMap);
                float mbSpeed = (float) speedModel.getFastestSpeed() / 1024;
                String speedStr = null;
                String avgSpeedStr = null;
                if (mbSpeed >= 1) {
                    String bigMbStr = df.format(mbSpeed);
                    speedStr = bigMbStr + "Mb/s";
                } else {
                    speedStr = speedModel.getFastestSpeed() + "Kb/s";
                }

                float avgMbSpeed = (float) speedModel.getAvgSpeed() / 1024;
                if (avgMbSpeed > 1) {
                    String avgSpeedMbStr = df.format(avgMbSpeed);
                    avgSpeedStr = avgSpeedMbStr + "Mb/s";
                } else {
                    avgSpeedStr = speedModel.getAvgSpeed() + "Kb/s";
                }

                showStr = "总大小：" + fileTotalSize
                        + " \n当前下载：" + currentSize
                        + " \n进度：" + percentStr
                        + " \n最快网速：" + speedStr
                        + " \n平均网速：" + avgSpeedStr;

                Logger.d(TAG, showStr);
            }
        });
    }

    private void textXUtilsFinal(RequestParams req) {
        x.http().request(HttpMethod.GET, req, new Callback.CommonCallback.ProgressCallback<File>() {

            String percentStr = null;

            @Override
            public void onError(Throwable e, boolean isOnCallback) {

                ToastUtil.showShortToast("下载失败，error:" + e == null ? "" : e
                        .getMessage());
                if (e != null) {
                    Logger.e(TAG, e.toString());
                } else {
                    Logger.e(TAG, "网络错误，下载失败");
                }
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
                Logger.d(TAG, "onCancelled");
            }

            @Override
            public void onFinished() {
                Logger.d(TAG, "onFinished");
            }

            @Override
            public void onWaiting() {
                Logger.d(TAG, "onWaiting");
            }

            @Override
            public void onStarted() {
                Logger.d(TAG, "onStarted");
            }

            @Override
            public void onLoading(long totalSize, long bytesWritten, boolean
                    isDownloading) {

                currentSize = bytesWritten;
                fileTotalSize = totalSize;
                float percent = ((float) bytesWritten / totalSize) * 100;
                percentStr = df.format(percent) + "%";

                showStr = "总大小：" + totalSize + " \n当前下载：" + bytesWritten + " " + "\n进度："
                        + percentStr;

                Logger.d(TAG,"onLoading:" + showStr);

            }

            @Override
            public void onSuccess(File result) {
                ToastUtil.showShortToast("下载成功");
                if (hashMap.size() <= 0) {
                    return;
                }
                final NetSpeedModel speedModel = getSpeedModel(hashMap);
                float mbSpeed = (float) speedModel.getFastestSpeed() / 1024;
                String speedStr = null;
                String avgSpeedStr = null;
                if (mbSpeed >= 1) {
                    String bigMbStr = df.format(mbSpeed);
                    speedStr = bigMbStr + "Mb/s";
                } else {
                    speedStr = speedModel.getFastestSpeed() + "Kb/s";
                }

                float avgMbSpeed = (float) speedModel.getAvgSpeed() / 1024;
                if (avgMbSpeed > 1) {
                    String avgSpeedMbStr = df.format(avgMbSpeed);
                    avgSpeedStr = avgSpeedMbStr + "Mb/s";
                } else {
                    avgSpeedStr = speedModel.getAvgSpeed() + "Kb/s";
                }

                showStr = "总大小：" + fileTotalSize
                        + " \n当前下载：" + currentSize
                        + " \n进度：" + percentStr
                        + " \n最快网速：" + speedStr
                        + " \n平均网速：" + avgSpeedStr;

                Logger.d(TAG, showStr);
            }
        });
    }

    private NetSpeedModel getSpeedModel(HashMap<Integer,Long> map) {
        final NetSpeedModel model = new NetSpeedModel();
        float slowestSpeed = 1.1f;
        long fastestSpeed = 0;

        long totalSpeed = 0;
        Logger.w(TAG,"hashMap size : " + hashMap.size());
        final Set<Map.Entry<Integer, Long>> entries = hashMap.entrySet();
        final Iterator<Map.Entry<Integer, Long>> it = entries.iterator();
        while (it.hasNext()) {
            final Map.Entry<Integer, Long> next = it.next();
            final Long speed = next.getValue();
            if (speed > fastestSpeed) {
                fastestSpeed = speed;
            }

            if (speed <= slowestSpeed) {
                slowestSpeed = speed;
            }
            totalSpeed += speed;
        }

        long avgSpeed = totalSpeed / hashMap.size();
        model.setFastestSpeed(fastestSpeed);
        model.setSlowestSpeed(slowestSpeed);
        model.setAvgSpeed(avgSpeed);

        Logger.d(TAG, "最快网速：" + fastestSpeed + "kb/s");
        Logger.d(TAG, "最慢网速：" + slowestSpeed + "kb/s");
        Logger.d(TAG, "平均网速：" + totalSpeed / hashMap.size() + "kb/s");

        return model;
    }




}
