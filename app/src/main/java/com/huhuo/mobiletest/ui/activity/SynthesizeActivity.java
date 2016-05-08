package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.SynthesizeTestAdapter;
import com.huhuo.mobiletest.constants.Constants;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.model.CommonTestModel;
import com.huhuo.mobiletest.model.TestItemModel;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.model.WebPageTestModel;
import com.huhuo.mobiletest.test.PingTest;
import com.huhuo.mobiletest.test.WebPageTest;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.NetWorkUtil;
import com.huhuo.mobiletest.utils.ToastUtil;
import com.huhuo.mobiletest.utils.TrafficUtil;
import com.huhuo.mobiletest.video.VideoPlayer;
import com.huhuo.mobiletest.video.util.VideoUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 综合测试包括网页测试，视频测试和ping测试
 */
@ContentView(R.layout.activity_synthesize_test)
public class SynthesizeActivity extends BaseActivity {

    private static final String TAG = SynthesizeActivity.class.getSimpleName();

    @ViewInject(R.id.rv_synthesize)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tv_test_info)
    private TextView tvTestInfo;

    @ViewInject(R.id.tv_addr)
    private TextView tvAddr;

    @ViewInject(R.id.surfaceView)
    private SurfaceView surfaceView;

    @ViewInject(R.id.btn_test_status)
    private Button btnTestStatus;

    @ViewInject(R.id.skbProgress)
    private SeekBar skbProgress;

    private ArrayList<WebPageTestModel> webPageTestList = new ArrayList<WebPageTestModel>();
    private ArrayList<CommonTestModel> testList = new ArrayList<CommonTestModel>();
    private ArrayList<CommonTestModel> pingTestList = new ArrayList<CommonTestModel>();
    private ArrayList<Float> speedList = new ArrayList<Float>();
    private ArrayList<Float> videoSpeedList = new ArrayList<Float>();
    private ArrayList<Float> pingDelayList = new ArrayList<Float>();

    private DecimalFormat df;
    private PingTest pingTest;

    private long allTime;
    private int allWebPageSize;

    private TestResultSummaryModel summaryModel;
    private String networkType;

    private SynthesizeTestAdapter adapter;
    private int currentTestIndex = 0;

    private VideoPlayer player;
    private Timer timer;
    private long newTraffic;
    private long oldTraffic;

    private static boolean START_TIMER = true;

    private static final int ADJUST_PARAMS = 1;
    private static final int TRAFFIC_UPDATE = 2;
    private int videoSize;
    private long startVideoTime = 0;

    private float videoTestLevel = 0;
    private float webpageTestLevel = 0;
    private boolean START_TEST = true;

    private WebPageTest test;

    @Override
    protected void init(Bundle savedInstanceState) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        timer = new Timer();
        networkType = NetWorkUtil.getCurrentNetworkType();

        VideoUtil.getVideoSize(Constants.PATH2, onVideoSizeListener);

        summaryModel = new TestResultSummaryModel();
        summaryModel.setNetType(networkType);
        summaryModel.setTestDate(new Date());
        summaryModel.setTestType(TestCode.TEST_TYPE_SYNTHESIZE);
        DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);

        initPlayer();

        df = new DecimalFormat("#.##");
        oldTraffic = TrafficUtil.getMyRxBytes();

        initTestItem();

        initWebPageTestItem();
        initPingTestData();
        test = new WebPageTest((ArrayList) webPageTestList);
        test.setTestListener(listener);
        test.test();
        tvTestInfo.setText(R.string.common_webpage_test_start);

        MobileTestApplication application = ((MobileTestApplication)getApplication());
        application.setLocationTextView(tvAddr, true);
    }

    private void initPlayer() {
        player = new VideoPlayer(surfaceView, skbProgress);
        player.setOnBufferingCompletion(onBufferingCompletion);
        player.setMute(true);
    }

    private VideoUtil.OnVideoSizeListener onVideoSizeListener = new VideoUtil.OnVideoSizeListener() {


        @Override
        public void onSize(int size) {
            videoSize = size;
        }
    };

    @Event(value = R.id.btn_test_status)
    private void btnTestStatusClick(View view) {
        if (START_TEST) {
            START_TEST = false;
            btnTestStatus.setText(R.string.test_start);

            if (test != null) {
                test.cancel();
            }

            closeTimer();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pingTest != null) {
                        pingTest.cancel();
                    }

                    if (player != null) {
                        player.pause();
                        player.stop();
                        player.destory();
                        player = null;
                    }

                    testList.clear();
                    webPageTestList.clear();
                    pingTestList.clear();

                    initWebPageTestItem();
                    initPingTestData();
                    initTestItem();

                    DatabaseHelper.getInstance().testResultDao.delete(summaryModel);

                    tvTestInfo.setText("准备开始测试");
                }
            },1000);
        } else {
            initPlayer();
            startTimer();
            START_TEST = true;
            summaryModel = new TestResultSummaryModel();
            DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);
            test.test();
            btnTestStatus.setText(R.string.test_stop);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case TRAFFIC_UPDATE :
                    Long traffic =(Long) msg.obj;
                    Logger.d(TAG, "newTraffic：" + traffic);
                    Logger.d(TAG, "视频加载中(" + traffic / 1024 + "KB/S)...");
                    tvTestInfo.setText(R.string.common_video_test_start);
                    videoSpeedList.add((float) traffic);

                    CommonTestModel allModel = testList.get(currentTestIndex);
                    allModel.setIsStart(true);
                    allModel.setPercent(2);
                    allModel.setAvgSpeed(traffic);
                    adapter.update(currentTestIndex);

                    break;
            }
        }
    };

    private void startTimer(){
        START_TIMER = true;
    }

    private void closeTimer(){
        START_TIMER = false;
    }

    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            if (START_TIMER) {
                newTraffic = TrafficUtil.getMyRxBytes();

                Message msg = handler.obtainMessage();
                msg.what = TRAFFIC_UPDATE;
                msg.obj = newTraffic - oldTraffic;
                handler.sendMessage(msg);

                Logger.v(TAG, "old_kbp：" + oldTraffic + "，new_kbp:" + newTraffic);
                Logger.v(TAG, "本次新获取的流量：" + (newTraffic - oldTraffic) + "kbp");
                oldTraffic = newTraffic;
            } else {
//                Logger.v(TAG,"现在暂停获取流量...");
            }

        }
    };

    private VideoPlayer.OnBufferingCompletion onBufferingCompletion = new VideoPlayer.OnBufferingCompletion(){

        @Override
        public void onBufferingCompletion(long bufferingTime,int bufferingCount) {
            closeTimer();
            Logger.e(TAG, "视频缓冲完毕...");
            videoTestClosed(bufferingTime, bufferingCount);
        }
    };



    private void videoTestClosed(long bufferingTime, int bufferingCount) {
        closeTimer();
        stopPlay();
        long endVideo = System.currentTimeMillis();
        float time = ((float) bufferingTime / 1000);
        final String playDelayTime = df.format(time);

        Logger.e(TAG, "首播缓冲时间："
                + (bufferingTime < 1000 ? bufferingTime : playDelayTime + "秒"));

//        tvTestInfo.setText("视频测试结束");

        skbProgress.setVisibility(View.GONE);
        surfaceView.setVisibility(View.GONE);

        String avgSpeedStr = getVideoAvgSpeed();

        float testLevel = 0;
        if (bufferingTime < 3000) {
            testLevel = 5;
        } else if (bufferingTime > 3000 && testLevel < 5000) {
            testLevel = 3;
        } else {
            testLevel = 1;
        }

        videoTestLevel = testLevel;

        String networkType = NetWorkUtil.getCurrentNetworkType();
        TestItemModel testItemModel = new TestItemModel();
        testItemModel.setTestType(TestCode.TEST_TYPE_VIDEO);
        testItemModel.setNetType(networkType);
        testItemModel.setTotalSize(videoSize);
        testItemModel.setPlayCount(1);
        testItemModel.setAvgSpeed(Float.valueOf(avgSpeedStr));
        testItemModel.setBufferCount(bufferingCount);
        testItemModel.setTestResultSummaryModel(summaryModel);
        DatabaseHelper.getInstance().testItemDao.insertOrUpdate(testItemModel);

        CommonTestModel allModel = testList.get(currentTestIndex);
        allModel.setSpeedLevel((int)testLevel);
        allModel.setIsStart(true);
        allModel.setPercent(100);
        allModel.setDelay((endVideo - startVideoTime));
        adapter.update(currentTestIndex);

        currentTestIndex ++;
        //开始执行第三项ping测试
        Logger.w(TAG,"开始执行第三项ping测试");
        pingTest = new PingTest(pingTestList);
        pingTest.setPingTestListener(pingTestListener);
        pingTest.test();
        tvTestInfo.setText(R.string.common_ping_test_start);
    }

    private PingTest.PingTestListener pingTestListener = new PingTest.PingTestListener() {
        @Override
        public void onStart(CommonTestModel model) {

        }

        @Override
        public void onUpdate(CommonTestModel model) {
            Logger.v(TAG,"onUpdate... " + model.getUrl() + "测试完毕," + model.toString());
            if (currentTestIndex < testList.size()) {
                CommonTestModel allModel = testList.get(currentTestIndex);
                final float delay = allModel.getDelay();
                pingDelayList.add(delay);
                allModel.setIsStart(true);
                allModel.setPercent(99);
                allModel.setUrl(model.getUrl());
                adapter.update(currentTestIndex);
            }
        }

        @Override
        public void onFinished(CommonTestModel model) {
            Logger.v(TAG,"onFinished ping测试结束,计算整个综合测试...");

            float delayTotal = 0;
            for (CommonTestModel m : pingTestList) {
                delayTotal += m.getDelay();
            }

            float avgTotalDelay = delayTotal / pingTestList.size();
            int level = 1;
            if (pingTest != null) {
                level = pingTest.getNetSpeedLevel(avgTotalDelay);
            }

            CommonTestModel allModel = testList.get(currentTestIndex);
            allModel.setIsStart(true);
            allModel.setSuccessRate(model.getSuccessRate());
            allModel.setPercent(100);
            final float delay = (float)delayTotal / pingTestList.size();
//            allModel.setDelay(delay);
            allModel.setDelay(model.getDelay());
            allModel.setAvgSpeed((long) avgTotalDelay);
            allModel.setSpeedLevel((int) level);
            adapter.update(currentTestIndex);

            String networkType = NetWorkUtil.getCurrentNetworkType();
            TestItemModel testItemModel = new TestItemModel();
            testItemModel.setTestType(TestCode.TEST_TYPE_PING);
            testItemModel.setNetType(networkType);
            testItemModel.setTarget("多网站");
            testItemModel.setSendCount(PingTest.PING_COUNT);
            testItemModel.setReceiveCount(model.getReceiveCount());

            testItemModel.setDelayTime(model.getDelay());
            testItemModel.setSuccessRate(model.getSuccessRate());
            testItemModel.setTestResultSummaryModel(summaryModel);
            DatabaseHelper.getInstance().testItemDao.insertOrUpdate(testItemModel);

            final int pingTestLevel = model.getSpeedLevel();
            float allLevel = pingTestLevel + webpageTestLevel + videoTestLevel;
            float avgLevel = allLevel / 3;
            Logger.w(TAG,"网页level:" + pingTestLevel);
            Logger.w(TAG,"视频level:" + videoTestLevel);
            Logger.w(TAG,"ping level:" + pingTestLevel);
            Logger.w(TAG,"网页，视频，ping三项测试，平均level:" + avgLevel);

            //测试时间
            summaryModel.setTestDate(new Date());
            summaryModel.setTestLevel(avgLevel);
            summaryModel.setTestType(TestCode.TEST_TYPE_SYNTHESIZE);
            summaryModel.setDelayTime(allTime);
            summaryModel.setNetType(networkType);

            DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);
            tvTestInfo.setText(R.string.common_test_finish);

            //三项测试完成，现在显示完整信息，将本次插入测试结果数据库为综合测试
        }

        @Override
        public void onError(Exception e,CommonTestModel model) {

        }
    };

    private String getVideoAvgSpeed(){
        long speed = 0;
        if (videoSpeedList.size() > 0) {
            for (int i = 0; i < videoSpeedList.size(); i++) {
                speed += videoSpeedList.get(i);
            }
            float avgSpeed = ((float)speed / videoSpeedList.size());
            final String avgSpeedStr = df.format(avgSpeed);
            return avgSpeedStr;
        }

        return null;
    }

    private WebPageTest.WebPageTestListener listener = new WebPageTest.WebPageTestListener() {

        long startWebPageTime = System.currentTimeMillis();
        long startWebPageTime1 = System.currentTimeMillis();

        @Override
        public void onPrepare(WebPageTestModel model) {
            Logger.w(TAG, "正在测试... " + model.getUrl());

        }

        @Override
        public void onStarted(WebPageTestModel model) {
            //通知RecyclerView的progressbar更新进度到15%
            CommonTestModel allModel = testList.get(currentTestIndex);
            allModel.setIsStart(true);
            allModel.setPercent(1);
            allModel.setUrl(model.getUrl());
            adapter.update(currentTestIndex);

            startWebPageTime = System.currentTimeMillis();
        }

        @Override
        public void onError(WebPageTestModel model,Throwable e, boolean isOnCallback) {
            ToastUtil.showShortToast("下载失败，error:" + e == null ? "" : e.getMessage());
            if (e != null) {
                Logger.e(TAG, "onError:" + e.toString());
            } else {
                Logger.e(TAG, "网络错误，下载失败");
            }
        }

        @Override
        public void onLoading(WebPageTestModel model,long total, long current, boolean isDownloading) {
            float percent = ((float) current / total) * 100;
            String percentStr = df.format(percent) + "%";

            Logger.d(TAG, model.getName() + "加载进度：" + percentStr);
        }



        @Override
        public void onSuccess(WebPageTestModel model, String result) {
            Logger.d(TAG, model.getName() + "下载网页成功：" + result);
            long end = System.currentTimeMillis();

            allWebPageSize += result.length();
            float webPageSize = (float)result.length() / 1024;
            long loadPageTime = (end - startWebPageTime);
            float loadPageTimeSecond = (float)loadPageTime / 1000;

            float speed = webPageSize / loadPageTimeSecond;
            String speedStr = df.format(speed);

            float kbps = (float)webPageSize * 8 / loadPageTimeSecond;
            speedList.add(kbps);

            Logger.d(TAG, "网页大小：" + df.format(webPageSize) + "kb");

            allTime += loadPageTime;
            Logger.d(TAG, "加载网页耗时：" + loadPageTime + " 毫秒");
            Logger.d(TAG, "加载网页耗时：" + loadPageTimeSecond + " 秒");
            Logger.d(TAG, "加载网页速率：" + speedStr + "KB/秒");

            CommonTestModel allModel = testList.get(currentTestIndex);
            allModel.setIsStart(true);
            allModel.setUrl(model.getUrl());
            allModel.setPercent(99);
            adapter.update(currentTestIndex);

        }

        @Override
        public void onFinished(WebPageTestModel model) {

        }

        @Override
        public void onEnd() {

            cacuLoadWebPageAvgSpeed();

            Logger.d(TAG, "开始进行第二项静音视频测试...");
            currentTestIndex ++;
            timer.schedule(task, 1000, 1000);
            startPlayOnlineVideo();

//            currentTestIndex = 2;
//            pingTest = new PingTest(pingTestList);
//            pingTest.setPingTestListener(pingTestListener);
//            pingTest.test();


//            String networkType = NetWorkUtil.getCurrentNetworkType();
//            TestItemModel testItemModel = new TestItemModel();
//            testItemModel.setNetType(networkType);
//            testItemModel.setTarget(getString(R.string.test_webpage));
//            testItemModel.setTotalSize(allWebPageSize);
////            testItemModel.setDelayTime(loadPageTime);
//            testItemModel.setAvgSpeed(allTime / speedList.size());
//            testItemModel.setTestResultSummaryModel(summaryModel);
//            DatabaseHelper.getInstance().testItemDao.insertOrUpdate(testItemModel);
        }

        private void cacuLoadWebPageAvgSpeed() {
            long testEndTime = System.currentTimeMillis();
            Logger.w(TAG, "w全部测试完毕，耗时：" + (testEndTime - startWebPageTime1));
            final int testCount = speedList.size();
            float allKbps = 0f;
            for (Float f : speedList) {
                allKbps += f;
            }
            float avgKbps = 0;
            float testLevel = 0;
            if (allKbps > 0) {
                avgKbps = (float)allKbps / testCount;
                Logger.d(TAG, "测试完毕，一共测试了" + testCount +
                        "个网站，平均速度：" + df.format(avgKbps) + "kbps");
                if (avgKbps >= 2000) {
                    testLevel = 5;
                } else if (avgKbps >= 800 && avgKbps < 2000) {
                    testLevel = 3;
                } else {
                    testLevel = 1;
                }
            }
            webpageTestLevel = testLevel;

            CommonTestModel allModel = testList.get(currentTestIndex);
            allModel.setIsStart(true);
            allModel.setPercent(100);
            allModel.setDelay(allTime);
            allModel.setAvgSpeed((long) avgKbps);
            allModel.setSpeedLevel((int) testLevel);
            adapter.update(currentTestIndex);

            long avgTime = allTime / testCount;
            Logger.w(TAG, "完成全部测试花费了：" + allTime + "毫秒");
            Logger.w(TAG, "完成每个测试平均花费：" + avgTime + "毫秒");
            Logger.w(TAG, "speedList.size()：" + speedList.size() + "");
            Logger.w(TAG, "allTime：" + allTime + "");

//            DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);

            String networkType = NetWorkUtil.getCurrentNetworkType();
            TestItemModel testItemModel = new TestItemModel();
            testItemModel.setTestType(TestCode.TEST_TYPE_WEBPAGE);
            testItemModel.setNetType(networkType);
            testItemModel.setTarget(TestCode.getTestName(TestCode.TEST_TYPE_WEBPAGE));
            testItemModel.setTotalSize(allWebPageSize);
            testItemModel.setDelayTime(allTime);
            testItemModel.setAvgSpeed(allKbps);
            testItemModel.setTestResultSummaryModel(summaryModel);
            DatabaseHelper.getInstance().testItemDao.insertOrUpdate(testItemModel);

        }
    };


    private void startPlayOnlineVideo(){
        player.playUrl(Constants.PATH2);
        startTimer();
        startVideoTime = System.currentTimeMillis();
    }

    private void initPingTestData(){
        CommonTestModel model = new CommonTestModel();

        model.setName(getString(R.string.test_website_10086));
        model.setUrl(getString(R.string.test_10086_url));
        pingTestList.add(model);

        model = new CommonTestModel();
        model.setName(getString(R.string.test_website_baidu));
        model.setUrl(getString(R.string.test_baidu_url));
        pingTestList.add(model);

        model = new CommonTestModel();
        model.setName(getString(R.string.test_website_tencent));
        model.setUrl(getString(R.string.test_tencent_url));
        pingTestList.add(model);

        model = new CommonTestModel();
        model.setName(getString(R.string.test_website_youku));
        model.setUrl(getString(R.string.test_youku_url));
        pingTestList.add(model);

        model = new CommonTestModel();
        model.setName(getString(R.string.test_website_sina));
        model.setUrl(getString(R.string.test_sina_url));
        pingTestList.add(model);

        model = new CommonTestModel();
        model.setName(getString(R.string.test_website_taobao));
        model.setUrl(getString(R.string.test_taobao_url));
        pingTestList.add(model);
    }

    private void initWebPageTestItem() {
        WebPageTestModel model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_10086_url));
        model.setName(getString(R.string.test_website_10086));
        webPageTestList.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_baidu_url));
        model.setName(getString(R.string.test_website_baidu));
        webPageTestList.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_tencent_url));
        model.setName(getString(R.string.test_website_tencent));
        webPageTestList.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_youku_url));
        model.setName(getString(R.string.test_website_youku));
        webPageTestList.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_sina_url));
        model.setName(getString(R.string.test_website_sina));
        webPageTestList.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_taobao_url));
        model.setName(getString(R.string.test_website_taobao));
        webPageTestList.add(model);
    }

    private void initTestItem() {
        //添加网页测试项
        CommonTestModel model = new CommonTestModel();
        model.setName(getString(R.string.test_webpage));
        testList.add(model);

        //添加视频测试项
        model = new CommonTestModel();
        model.setName(getString(R.string.test_video));
        testList.add(model);

        //添加连接(ping)测试项
        model = new CommonTestModel();
        model.setName(getString(R.string.test_ping));
        testList.add(model);

        adapter = new SynthesizeTestAdapter(testList);
        recyclerView.setAdapter(adapter);
    }

    private void cancelTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void cancelTask(){
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
        cancelTask();
        if (player != null) {
            player.stop();
            player.destory();
        }
    }

    private void stopPlay(){
        if (player != null) {
            player.stop();
        }
    }
}
