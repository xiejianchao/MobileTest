package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.model.WebPageTestModel;
import com.huhuo.mobiletest.net.callback.DefaultHttpRequestCallBack;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.ToastUtil;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ContentView(R.layout.activity_web_page_test)
public class WebPageTestActivity extends BaseActivity {

    private static final String TAG = WebPageTestActivity.class.getSimpleName();

    private DecimalFormat df;
    private String showStr;

    @ViewInject(R.id.tv_test_info)
    private TextView tvTestAllInfo;

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

    @Override
    protected void init(Bundle savedInstanceState) {
        df = new DecimalFormat("#.##");

        initTestItem();

        nextTestItem = 0;
        final WebPageTestModel model = list.get(nextTestItem);
        start = System.currentTimeMillis();
        testWebPage(model);
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
    @Event(value = R.id.btn_download)
    private void downloadClick(View view) {
        nextTestItem = 0;
        final WebPageTestModel model = list.get(nextTestItem);
        testWebPage(model);
    }

    private long allTime;

    private void testWebPage(WebPageTestModel model) {
        String url = model.getUrl();
        Logger.d(TAG,"测试URL:" + url);
        RequestParams params = new RequestParams(url);

        final ColorfulRingProgressView progressView = model.getProgressView();
        final TextView tvTestInfo = model.getTextView();

        tvTestAllInfo.setText("正在测试" + tvTestInfo.getText().toString());

        SystemClock.sleep(500);

        progressView.setPercent(3);

        x.http().get(params,new DefaultHttpRequestCallBack<String>(){

            String percentStr = null;
            long start = System.currentTimeMillis();
            @Override
            public void onStarted() {
                progressView.setPercent(15);
            }

            @Override
            public void onError(Throwable e, boolean isOnCallback) {
                ToastUtil.showShortToast("下载失败，error:" + e == null ? "" : e.getMessage());
                if (e != null) {
                    Logger.e(TAG, "onError:" + e.toString());
                } else {
                    Logger.e(TAG, "网络错误，下载失败");
                }
            }

            @Override
            public void onLoading(long totalSize, long bytesWritten, boolean isDownloading) {
                float percent = ((float) bytesWritten / totalSize) * 100;
                percentStr = df.format(percent) + "%";

                showStr = "总大小：" + totalSize + " \n当前下载：" + bytesWritten + " " + "\n进度："
                        + percentStr;

                Logger.w(TAG, "onLoading:" + showStr);
            }

            @Override
            public void onSuccess(String result) {
                progressView.setPercent(80);
                Logger.d(TAG, "下载网页成功1：" + result);
                long end = System.currentTimeMillis();

                float webPageSize = (float)result.length() / 1024;
                long loadPageTime = (end - start);
                float loadPageTimeSecond = (float)loadPageTime / 1000;

                float kbps = (float)webPageSize * 8 / loadPageTimeSecond;
                speedList.add(kbps);

                Logger.d(TAG, "网页大小：" + df.format(webPageSize) + "kb");

                allTime += loadPageTime;
                Logger.d(TAG, "加载网页耗时：" + loadPageTime + " 毫秒");
                Logger.d(TAG, "加载网页耗时：" + loadPageTimeSecond + " 秒");
                Logger.d(TAG, "加载网页速率：" + df.format(webPageSize / loadPageTimeSecond) + "KB/秒");

                tvTestInfo.append("\n " + df.format(kbps) + "kbps" + "");
            }

            @Override
            public void onFinished() {
                super.onFinished();
                try {
                    progressView.setPercent(100);
                    Logger.d(TAG, "onFinished");
                    if (nextTestItem <= list.size() - 1) {
                        nextTestItem ++;
                        if (nextTestItem == list.size()) {
                            Logger.e(TAG, "执行测试index超过总量");
                            cacuAvgSpeed();
                            return;
                        }
                        testWebPage(list.get(nextTestItem));
                    }
                } catch (Exception e) {
                    Logger.e(TAG,"onFinished",e);
                }
            }
        });
    }

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
            Logger.d(TAG, "所有网站测试完毕，一共测试了" + testCount +
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
        final TestResultSummaryModel summaryModel = new TestResultSummaryModel();
        long avgTime = allTime / testCount;
        Logger.w(TAG, "完成全部测试花费了：" + allTime + "毫秒");
        Logger.w(TAG, "完成每个测试平均花费：" + avgTime + "毫秒");

        //测试时间
        summaryModel.setTestDate(new Date());
        summaryModel.setTestLevel(testLevel);
        summaryModel.setTestType(TestCode.TEST_TYPE_WEBPAGE);
        summaryModel.setDelayTime(allTime);
        DatabaseHelper.getInstance().testResultDao.insert(summaryModel);

        final List<TestResultSummaryModel> models = DatabaseHelper.getInstance()
                .testResultDao.queryAll();

        if (models != null) {
            for (TestResultSummaryModel model : models) {
                Logger.d(TAG,"从数据库中取出的测试结果：" + model);
            }
        }

    }


}
