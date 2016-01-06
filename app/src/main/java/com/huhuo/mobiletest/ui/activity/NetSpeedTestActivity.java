package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.constants.Constants;
import com.huhuo.mobiletest.model.NetSpeedModel;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.ToastUtil;
import com.huhuo.mobiletest.view.DialChart03View;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


@ContentView(R.layout.activity_net_speed_test)
public class NetSpeedTestActivity extends BaseActivity {

    private static final String TAG = NetSpeedTestActivity.class.getSimpleName();
    @ViewInject(R.id.circle_view)
    private DialChart03View chartView;

    @ViewInject(R.id.tv_result)
    private TextView tvResult;

    @ViewInject(R.id.btn_xutils_test_speed)
    private Button btnTest;

    private Timer timer;
    private static final int SPEED_CODE = 100;

    private boolean startTest = false;

    private DecimalFormat df;
    private long currentSize = 0;
    private long lastTotalSize = 0;
    private long lastSpeed = 0;
    private long fileTotalSize = 0;
    private String showStr;

    private HashMap<Integer,Long> hashMap = new HashMap<Integer,Long>();


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SPEED_CODE) {
                long currentSpeed = (long) msg.obj;
                float bigMb = (float)currentSpeed / 1024;

                long kb = currentSpeed <= 0 ? 0 : currentSpeed;

                if (bigMb > 1) {//当前速度大于1MB
                    String bigMbStr = df.format(bigMb);
                    tvResult.setText(showStr + "\n当前网速：" + bigMbStr + "Mb/s");
                } else {
                    tvResult.setText(showStr + "\n当前网速：" + kb + "Kb");
                }

                final float percent = (bigMb * 8) / 100;
                Logger.w(TAG,"当前下载速度相当于：" + bigMb * 8 + "MB的宽带");
                Logger.w(TAG,"当前下载速度百分比为：" + percent + "MB");
                chartView.setCurrentStatus(percent <= 0 ? 0 : percent);

            }
        }
    };

    int speedCount = 0;
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            long currentDownloadSize = 0;
            if (lastTotalSize > 0) {
                currentDownloadSize = currentSize - lastTotalSize;
            } else {
                currentDownloadSize = currentSize;
            }
            long currentSpeed = (currentDownloadSize / 1024);
            speedCount ++;

            hashMap.put(speedCount,currentSpeed);

            if (currentSpeed <= 0) {
                Logger.e(TAG, "当前网速：" + 0 + "kb" + " , 上次网速：" + lastSpeed + "kb");
            } else {
                Logger.e(TAG,"当前网速：" + currentSpeed + "kb" + " , 上次网速：" + lastSpeed + "kb");
            }

            lastTotalSize = currentSize;
            lastSpeed = currentSpeed;

            Message msg = mHandler.obtainMessage();
            msg.what = SPEED_CODE;
            msg.obj = currentSpeed;
            mHandler.sendMessage(msg);
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {

        df = new DecimalFormat("#.##");
        chartView.setCurrentStatus(0.1f);

    }

    @Event(value = R.id.btn_xutils_test_speed,type = View.OnClickListener.class)
    private void btnDownloadTest(View view) {
        ToastUtil.showShortToast("hello click");
        RequestParams req = new RequestParams(Constants.TAOBAO_APK_URL);
        req.setAutoRename(false);
        req.setAutoResume(true);
        File saveFile = new File(Environment.getExternalStorageDirectory() + "/textXUtils3");
        if (!saveFile.exists()) {
            saveFile.mkdirs();
            Logger.e(TAG,"XUTILS目录不存在，创建...");
        }
        String savePath = saveFile.getAbsolutePath() + "/360.apk";
        Logger.d(TAG,"save path : " + savePath);
        req.setSaveFilePath(savePath);
        textXUtilsFinal(req);
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
                cancelTimer();
                startTest = false;
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
                tvResult.setText("正在建立连接...");
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

                if (!startTest) {
                    startTest = true;
                    startTimer();
                }
            }

            @Override
            public void onSuccess(File result) {
                ToastUtil.showShortToast("下载成功");
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
                        + " \n平均网速：" + avgSpeedStr
                        + " \n评估您的最高宽带为：" + (mbSpeed * 8) + "MB"
                        + " \n评估您的平均宽带为：" + (avgMbSpeed * 8) + "MB"
                ;
                tvResult.setText(showStr);
                cancelTimer();
                startTest = false;

                chartView.setCurrentStatus(0);
            }
        });
    }

    private NetSpeedModel getSpeedModel(HashMap<Integer,Long> map) {
        final NetSpeedModel model = new NetSpeedModel();
        float slowestSpeed = 1.1f;
        long fastestSpeed = 0;

        long totalSpeed = 0;
        Logger.w(TAG, "hashMap size : " + hashMap.size());
        final Set<Map.Entry<Integer, Long>> entries = hashMap.entrySet();
        final Iterator<Map.Entry<Integer, Long>> it = entries.iterator();
        while (it.hasNext()) {
            final Map.Entry<Integer, Long> next = it.next();
            final Long speed = next.getValue();
            if (speed > fastestSpeed) {
                fastestSpeed = speed;
            }

            if (speed > 0 && speed <= slowestSpeed) {
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

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            task.cancel();
        }
    }

    private void startTimer() {
        timer = new Timer();
        Logger.e(TAG, "开始计算当前网速！");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long currentDownloadSize = 0;
                if (lastTotalSize > 0) {
                    currentDownloadSize = currentSize - lastTotalSize;
                } else {
                    currentDownloadSize = currentSize;
                }
                long currentSpeed = (currentDownloadSize / 1024);
                speedCount++;

                hashMap.put(speedCount, currentSpeed);

                if (currentSpeed <= 0) {
                    Logger.e(TAG, "当前网速：" + 0 + "kb" + " , 上次网速：" + lastSpeed + "kb");
                } else {
                    Logger.e(TAG, "当前网速：" + currentSpeed + "kb" + " , 上次网速：" + lastSpeed + "kb");
                }

                lastTotalSize = currentSize;
                lastSpeed = currentSpeed;

                Message msg = mHandler.obtainMessage();
                msg.what = SPEED_CODE;
                msg.obj = currentSpeed;
                mHandler.sendMessage(msg);
            }
        }, 1000, 300);
    }


}
