package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.constants.Constants;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.model.CommonTestModel;
import com.huhuo.mobiletest.model.TestItemModel;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.utils.FileAccessor;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.NetWorkUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


@ContentView(R.layout.activity_download_test)
public class DownloadTestActivity extends BaseActivity {

    private static final String TAG = DownloadTestActivity.class.getSimpleName();

    @ViewInject(R.id.circle_view)
    private DialChart03View chartView;

    @ViewInject(R.id.tv_result)
    private TextView tvResult;

    @ViewInject(R.id.btn_test_speed)
    private Button btnTest;

    @ViewInject(R.id.tv_addr)
    private TextView tvAddr;

    private Timer timer;
    private static final int SPEED_CODE = 100;

    private boolean startTest = false;

    private DecimalFormat df;
    private long currentSize = 0;
    private long lastTotalSize = 0;
    private long lastSpeed = 0;
    private long fileTotalSize = 0;
    private String showStr;
    private long startTime = 0;
    private long endTime = 0;

    private TestResultSummaryModel summaryModel;

    private HashMap<Integer,Long> hashMap = new HashMap<Integer,Long>();

    @Override
    protected void init(Bundle savedInstanceState) {
        df = new DecimalFormat("#.##");
        chartView.setCurrentStatus(0.1f);

        summaryModel = new TestResultSummaryModel();
        DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);

        startTestDownloadSpeed();

        MobileTestApplication application = ((MobileTestApplication)getApplication());
        application.setLocationTextView(tvAddr, true);
    }

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


    @Event(value = R.id.btn_test_speed,type = View.OnClickListener.class)
    private void btnDownloadTest(View view) {
        startTestDownloadSpeed();
    }

    private void startTestDownloadSpeed() {
        RequestParams req = new RequestParams(Constants.TAOBAO_APK_URL);
        req.setAutoRename(false);
        req.setAutoResume(true);
        final String savePath = FileAccessor.getDownloadPathByUrl(Constants
                .TAOBAO_APK_URL);
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
                    Logger.e(TAG, "onError:" + e.toString());
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
                startTime = System.currentTimeMillis();
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
                final CommonTestModel speedModel = getSpeedModel(hashMap);
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
                        + " \n评估您的最高宽带为：" + df.format(mbSpeed * 8) + "MB"
                        + " \n评估您的平均宽带为：" + df.format(avgMbSpeed * 8) + "MB"
                ;
                tvResult.setText(showStr);
                cancelTimer();
                startTest = false;

                chartView.setCurrentStatus(0);

                endTime = System.currentTimeMillis();
                long downloadTime = (endTime - startTime);
                Logger.v(TAG, "下载测试耗时：" + downloadTime / 1000);

                save2Database(result, speedModel, downloadTime);
            }
        });
    }

    private void save2Database(File result, CommonTestModel speedModel, long
            downloadTime) {
        String networkType = NetWorkUtil.getCurrentNetworkType();
        TestItemModel testItemModel = new TestItemModel();
        testItemModel.setNetType(networkType);
        testItemModel.setTarget("北京服务器");
        testItemModel.setTotalSize(result.length());
        testItemModel.setDelayTime(downloadTime);
        testItemModel.setAvgSpeed(speedModel.getAvgSpeed());
        testItemModel.setTestResultSummaryModel(summaryModel);
        DatabaseHelper.getInstance().testItemDao.insertOrUpdate(testItemModel);

        //更新测试结果整体结果
        summaryModel.setTestDate(new Date());
        summaryModel.setTestLevel(getTestLevel(speedModel.getFastestSpeed() / 1024));
        summaryModel.setTestType(TestCode.TEST_TYPE_SPEED);
        summaryModel.setDelayTime(downloadTime);
        summaryModel.setNetType(networkType);
        DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);
    }

    private float getTestLevel(float mbSpeed) {
        int level = 1;
        if (mbSpeed >= 5) {
            level = 5;
        } else if (mbSpeed >= 2 && mbSpeed < 5) {
            level = 4;
        } else if (mbSpeed > 1 && mbSpeed < 2){
            level = 2;
        } else {
            level = 1;
        }
        return level;
    }


    private CommonTestModel getSpeedModel(HashMap<Integer,Long> map) {
        final CommonTestModel model = new CommonTestModel();
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

            if (speed > 0 && speed < slowestSpeed && speed != slowestSpeed) {
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
        Logger.d(TAG, "开始计算当前网速！");
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
                    Logger.d(TAG, "当前网速：" + 0 + "kb" + " , 上次网速：" + lastSpeed + "kb");
                } else {
                    Logger.d(TAG, "当前网速：" + currentSpeed + "kb" + " , 上次网速：" + lastSpeed + "kb");
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
