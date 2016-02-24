package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

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
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 综合测试包括网页测试，视频测试和ping测试
 */
@ContentView(R.layout.activity_synthesize)
public class SynthesizeActivity extends BaseActivity {

    private static final String TAG = SynthesizeActivity.class.getSimpleName();

    @ViewInject(R.id.rv_synthesize)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tv_test_info)
    private TextView tvTestInfo;

    @ViewInject(R.id.surfaceView)
    private SurfaceView surfaceView;

    @ViewInject(R.id.skbProgress)
    private SeekBar skbProgress;

    private ArrayList<WebPageTestModel> webPageTestList = new ArrayList<WebPageTestModel>();
    ArrayList<CommonTestModel> testList = new ArrayList<CommonTestModel>();
    ArrayList<CommonTestModel> pingTestList = new ArrayList<CommonTestModel>();
    private DecimalFormat df;

    private ArrayList<Float> speedList = new ArrayList<Float>();
    private ArrayList<Float> videoSpeedList = new ArrayList<Float>();

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

        player = new VideoPlayer(surfaceView, skbProgress);
        player.setOnBufferingCompletion(onBufferingCompletion);
        player.setMute(true);

        df = new DecimalFormat("#.##");
        oldTraffic = TrafficUtil.getMyRxBytes();

        initTestItem();

        initWebPageTestItem();
        initPingTestData();
        WebPageTest test = new WebPageTest((ArrayList) webPageTestList);
        test.setTestListener(listener);
        test.test();
    }

    private VideoUtil.OnVideoSizeListener onVideoSizeListener = new VideoUtil.OnVideoSizeListener() {


        @Override
        public void onSize(int size) {
            videoSize = size;
        }
    };

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
                Logger.v(TAG,"现在暂停获取流量...");
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

        tvTestInfo.setText("视频测试结束");

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

        String networkType = NetWorkUtil.getCurrentNetworkType();
        TestItemModel testItemModel = new TestItemModel();
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
        PingTest pingTest = new PingTest(pingTestList);
        pingTest.setPingTestListener(pingTestListener);
        pingTest.test();
    }

    private PingTest.PingTestListener pingTestListener = new PingTest.PingTestListener() {
        @Override
        public void onUpdate(CommonTestModel model) {
            Logger.v(TAG,"onUpdate... " + model.getUrl() + "测试完毕," + model.toString());
        }

        @Override
        public void onFinished(CommonTestModel model) {
            Logger.v(TAG,"onFinished ping测试结束,计算整个综合测试...");
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

        long start = System.currentTimeMillis();

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

            start = System.currentTimeMillis();
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

            Logger.d(TAG,model.getName() + "加载进度：" + percentStr);
        }



        @Override
        public void onSuccess(WebPageTestModel model, String result) {
            Logger.d(TAG, model.getName() + "下载网页成功：" + result);
            long end = System.currentTimeMillis();

            allWebPageSize += result.length();
            float webPageSize = (float)result.length() / 1024;
            long loadPageTime = (end - start);
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
            Logger.w(TAG, "全部测试完毕...");
            cacuLoadWebPageAvgSpeed();

            currentTestIndex ++;
            if (currentTestIndex == 1) {//如果currentTestIndex == 1，进行静音视频测试
                Logger.d(TAG, "开始进行视频测试...");
                timer.schedule(task, 1000, 1000);
                startPlayOnlineVideo();
            }
        }
    };

    private void startPlayOnlineVideo(){
        player.playUrl(Constants.PATH2);
        startTimer();
        startVideoTime = System.currentTimeMillis();
    }

    private void cacuLoadWebPageAvgSpeed() {
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

        DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);

        String networkType = NetWorkUtil.getCurrentNetworkType();
        TestItemModel testItemModel = new TestItemModel();
        testItemModel.setNetType(networkType);
        testItemModel.setTarget(TestCode.getTestName(TestCode.TEST_TYPE_WEBPAGE));
        testItemModel.setTotalSize(allWebPageSize);
        testItemModel.setDelayTime(allTime);
        testItemModel.setAvgSpeed(allKbps);
        testItemModel.setTestResultSummaryModel(summaryModel);
        DatabaseHelper.getInstance().testItemDao.insertOrUpdate(testItemModel);

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
