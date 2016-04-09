package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.model.TestItemModel;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.model.WebPageTestModel;
import com.huhuo.mobiletest.test.WebPageTest;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.NetWorkUtil;
import com.huhuo.mobiletest.utils.ToastUtil;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ContentView(R.layout.activity_web_page_test)
public class    WebPageTestActivity extends BaseActivity {

    private static final String TAG = WebPageTestActivity.class.getSimpleName();

    private DecimalFormat df;
    private String showStr;

    @ViewInject(R.id.tv_test_info)
    private TextView tvTestAllInfo;

    @ViewInject(R.id.tv_addr)
    private TextView tvAddr;

    @ViewInject(R.id.btn_test_status)
    private Button btnTestStatus;

    @ViewInject(R.id.circle_view_10086)
    private ColorfulRingProgressView mobileProgressView;

    @ViewInject(R.id.tv_10086_test_info)
    private TextView tv10086Info;

    @ViewInject(R.id.circle_view_baidu)
    private ColorfulRingProgressView baiduProgressView;

    @ViewInject(R.id.tv_baidu_test_info)
    private TextView tvBaiduInfo;

    @ViewInject(R.id.circle_view_tencent)
    private ColorfulRingProgressView tencentProgressView;

    @ViewInject(R.id.tv_tencent_test_info)
    private TextView tvTencentInfo;

    @ViewInject(R.id.circle_view_youku)
    private ColorfulRingProgressView youkuProgressView;

    @ViewInject(R.id.tv_youku_test_info)
    private TextView tvYoukuInfo;

    @ViewInject(R.id.circle_view_sina)
    private ColorfulRingProgressView sinaProgressView;

    @ViewInject(R.id.tv_sina_test_info)
    private TextView tvSinaInfo;

    @ViewInject(R.id.circle_view_taobao)
    private ColorfulRingProgressView taobaoProgressView;

    @ViewInject(R.id.tv_taobao_test_info)
    private TextView tvTaobaoInfo;

    private ArrayList<WebPageTestModel> list = new ArrayList<WebPageTestModel>();

    private int nextTestItem = 0;

    private List<Float> speedList = new ArrayList<Float>();

    private long start;

    private long allTime;

    private TestResultSummaryModel summaryModel;

    private static final int WEB_TESTING = 0;
    private static final int WEB_STOP = 1;

    private static int TEST_STATUS = WEB_TESTING;

    private WebPageTest test;


    @Override
    protected void init(Bundle savedInstanceState) {
        df = new DecimalFormat("#.##");
        initTestItem();

        nextTestItem = 0;
        final WebPageTestModel model = list.get(nextTestItem);
        start = System.currentTimeMillis();

        summaryModel = new TestResultSummaryModel();
        DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);

//        testWebPage(model);

        test = new WebPageTest((ArrayList)list);
        test.setTestListener(listener);
        test.test();
    }

    private WebPageTest.WebPageTestListener listener = new WebPageTest.WebPageTestListener() {
        long start = System.currentTimeMillis();
        @Override
        public void onPrepare(WebPageTestModel model) {
            final ColorfulRingProgressView progressView = model.getProgressView();
            final TextView tvTestInfo = model.getTextView();
            tvTestAllInfo.setText("正在测试" + tvTestInfo.getText().toString());
            progressView.setPercent(3);
        }

        @Override
        public void onStarted(WebPageTestModel model) {
            model.getProgressView().setPercent(15);
            start = System.currentTimeMillis();
        }

        @Override
        public void onError(WebPageTestModel model,Throwable e, boolean isOnCallback) {
            ToastUtil.showShortToast("error:" + e == null ? "" : e.getMessage());
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

            showStr = "总大小：" + total + " \n当前下载：" + current + " " + "\n进度："
                    + percentStr;

            Logger.w(TAG, "onLoading:" + showStr);
        }

        @Override
        public void onSuccess(WebPageTestModel model, String result) {
            model.getProgressView().setPercent(80);
            Logger.d(TAG, "下载网页成功1：" + result);
            long end = System.currentTimeMillis();

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

            model.getTextView().append("\n " + df.format(kbps) + "kbps" + "");

            String networkType = NetWorkUtil.getCurrentNetworkType();
            TestItemModel testItemModel = new TestItemModel();
            testItemModel.setNetType(networkType);
            testItemModel.setTarget(model.getName());
            testItemModel.setTotalSize(result.length());
            testItemModel.setDelayTime(loadPageTime);
            testItemModel.setAvgSpeed(speed);
            testItemModel.setTestResultSummaryModel(summaryModel);
            DatabaseHelper.getInstance().testItemDao.insertOrUpdate(testItemModel);
        }

        @Override
        public void onFinished(WebPageTestModel model) {
            try {
                model.getProgressView().setPercent(100);
                Logger.d(TAG, "onFinished");

            } catch (Exception e) {
                Logger.e(TAG,"onFinished",e);
            }
        }

        @Override
        public void onEnd() {
            cacuAvgSpeed();
        }
    };



    private void cacuAvgSpeed() {
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
                tvTestAllInfo.setText("测试完毕，您的网络速度很快，令人神往！");
            } else if (avgKbps >= 800 && avgKbps < 2000) {
                testLevel = 3;
                tvTestAllInfo.setText("测试完毕，您的网络速度一般，还需加油！");
            } else {
                testLevel = 1;
                tvTestAllInfo.setText("测试完毕，您的网络速度很慢！");
            }
        }

        long avgTime = allTime / testCount;
        Logger.w(TAG, "完成全部测试花费了：" + allTime + "毫秒");
        Logger.w(TAG, "完成每个测试平均花费：" + avgTime + "毫秒");

        //测试时间
        summaryModel.setTestDate(new Date());
        summaryModel.setTestLevel(testLevel);
        summaryModel.setTestType(TestCode.TEST_TYPE_WEBPAGE);
        summaryModel.setDelayTime(allTime);
        String networkType = NetWorkUtil.getCurrentNetworkType();
        summaryModel.setNetType(networkType);

        DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);
    }

    private void initTestItem() {
        WebPageTestModel model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_10086_url));
        model.setName(getString(R.string.test_website_10086));
        model.setProgressView(mobileProgressView);
        model.setTextView(tv10086Info);
        list.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_baidu_url));
        model.setName(getString(R.string.test_website_baidu));
        model.setProgressView(baiduProgressView);
        model.setTextView(tvBaiduInfo);
        list.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_tencent_url));
        model.setName(getString(R.string.test_website_tencent));
        model.setProgressView(tencentProgressView);
        model.setTextView(tvTencentInfo);
        list.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_youku_url));
        model.setName(getString(R.string.test_website_youku));
        model.setProgressView(youkuProgressView);
        model.setTextView(tvYoukuInfo);
        list.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_sina_url));
        model.setName(getString(R.string.test_website_sina));
        model.setProgressView(sinaProgressView);
        model.setTextView(tvSinaInfo);
        list.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_taobao_url));
        model.setName(getString(R.string.test_website_taobao));
        model.setProgressView(taobaoProgressView);
        model.setTextView(tvTaobaoInfo);
        list.add(model);
    }

    private void resetTestItem(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mobileProgressView.setPercent(1);
                tv10086Info.setText(R.string.test_website_10086);

                baiduProgressView.setPercent(1);
                tvBaiduInfo.setText(R.string.test_website_baidu);

                tencentProgressView.setPercent(1);
                tvTencentInfo.setText(R.string.test_website_tencent);

                youkuProgressView.setPercent(1);
                tvYoukuInfo.setText(R.string.test_website_youku);

                sinaProgressView.setPercent(1);
                tvSinaInfo.setText(R.string.test_website_sina);

                taobaoProgressView.setPercent(1);
                tvTaobaoInfo.setText(R.string.test_website_taobao);
            }
        },1000);

    }

    @Event(value = R.id.btn_test_status)
    private void testStatusClick(View v) {
        if (TEST_STATUS == WEB_TESTING) {
            TEST_STATUS = WEB_STOP;
            btnTestStatus.setText(R.string.test_start);
            if (test != null) {
                test.cancel();
                DatabaseHelper.getInstance().testResultDao.delete(summaryModel);
                resetTestItem();
                tvTestAllInfo.setText("准备开始测试");
            }
        } else {
            TEST_STATUS = WEB_TESTING;
            summaryModel = new TestResultSummaryModel();
            DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);
            test.test();
            btnTestStatus.setText(R.string.test_stop);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobileTestApplication application = ((MobileTestApplication)getApplication());
        application.setLocationTextView(tvAddr,true);
    }
}
