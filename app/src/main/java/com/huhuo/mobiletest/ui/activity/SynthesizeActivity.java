package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.model.WebPageTestModel;
import com.huhuo.mobiletest.test.WebPageTest;
import com.huhuo.mobiletest.utils.Logger;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 综合测试包括网页测试，视频测试和ping测试
 */
@ContentView(R.layout.activity_synthesize)
public class SynthesizeActivity extends BaseActivity {

    private static final String TAG = SynthesizeActivity.class.getSimpleName();

    @ViewInject(R.id.rv_synthesize)
    private RecyclerView recyclerView;

    private ArrayList<WebPageTestModel> list = new ArrayList<WebPageTestModel>();
    private DecimalFormat df;

    private long allTime;

    @Override
    protected void init(Bundle savedInstanceState) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        df = new DecimalFormat("#.##");
        initTestItem();
        WebPageTest test = new WebPageTest((ArrayList)list);
        test.setTestListener(listener);
        test.test();

    }

    private WebPageTest.WebPageTestListener listener = new WebPageTest.WebPageTestListener() {

        long start = System.currentTimeMillis();

        @Override
        public void onPrepare(WebPageTestModel model) {
            Logger.w(TAG, "正在测试... " + model.getUrl());
            start = System.currentTimeMillis();
        }

        @Override
        public void onStarted(WebPageTestModel model) {

        }

        @Override
        public void onLoading(WebPageTestModel model,long total, long current, boolean isDownloading) {
            float percent = ((float) current / total) * 100;
            String percentStr = df.format(percent) + "%";

            Logger.d(TAG,model.getName() + "加载进度：" + percentStr);
        }

        @Override
        public void onError(WebPageTestModel model,Throwable ex, boolean isOnCallback) {

        }

        @Override
        public void onSuccess(WebPageTestModel model, String result) {
            Logger.d(TAG, model.getName() + "下载网页成功：" + result);
            long end = System.currentTimeMillis();

            float webPageSize = (float)result.length() / 1024;
            long loadPageTime = (end - start);
            float loadPageTimeSecond = (float)loadPageTime / 1000;

            float speed = webPageSize / loadPageTimeSecond;
            String speedStr = df.format(speed);

            float kbps = (float)webPageSize * 8 / loadPageTimeSecond;
//            speedList.add(kbps);

            Logger.d(TAG, "网页大小：" + df.format(webPageSize) + "kb");

//            allTime += loadPageTime;
            Logger.d(TAG, "加载网页耗时：" + loadPageTime + " 毫秒");
            Logger.d(TAG, "加载网页耗时：" + loadPageTimeSecond + " 秒");
            Logger.d(TAG, "加载网页速率：" + speedStr + "KB/秒");
        }

        @Override
        public void onFinished(WebPageTestModel model) {

        }

        @Override
        public void onEnd() {
            Logger.w(TAG,"全部测试完毕...");
        }
    };

    private void initTestItem() {
        WebPageTestModel model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_10086_url));
        model.setName(getString(R.string.test_website_10086));
//        model.setProgressView(mobileProgressView);
//        model.setTextView(tv10086Info);
        list.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_baidu_url));
        model.setName(getString(R.string.test_website_baidu));
//        model.setProgressView(baiduProgressView);
//        model.setTextView(tvBaiduInfo);
        list.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_tencent_url));
        model.setName(getString(R.string.test_website_tencent));
//        model.setProgressView(tencentProgressView);
//        model.setTextView(tvTencentInfo);
        list.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_youku_url));
        model.setName(getString(R.string.test_website_youku));
//        model.setProgressView(youkuProgressView);
//        model.setTextView(tvYoukuInfo);
        list.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_sina_url));
        model.setName(getString(R.string.test_website_sina));
//        model.setProgressView(sinaProgressView);
//        model.setTextView(tvSinaInfo);
        list.add(model);

        model = new WebPageTestModel();
        model.setUrl(getString(R.string.test_taobao_url));
        model.setName(getString(R.string.test_website_taobao));
//        model.setProgressView(taobaoProgressView);
//        model.setTextView(tvTaobaoInfo);
        list.add(model);

    }

}
