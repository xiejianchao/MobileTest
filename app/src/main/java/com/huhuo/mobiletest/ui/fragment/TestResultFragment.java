package com.huhuo.mobiletest.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.OnItemClickListener;
import com.huhuo.mobiletest.adapter.SynthesizeTestDetailsAdapter;
import com.huhuo.mobiletest.adapter.TestResultAdapter;
import com.huhuo.mobiletest.constants.Constants;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.model.HttpPage;
import com.huhuo.mobiletest.model.TestItemModel;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.net.HttpHelper;
import com.huhuo.mobiletest.net.HttpHelper2;
import com.huhuo.mobiletest.net.callback.SimpleHttpRequestCallBack;
import com.huhuo.mobiletest.ui.activity.DownloadTestDetailsActivity;
import com.huhuo.mobiletest.ui.activity.PingTestDetailsActivity;
import com.huhuo.mobiletest.ui.activity.SynthesizeTestDetailsActivity;
import com.huhuo.mobiletest.ui.activity.VideoTestDetailsActivity;
import com.huhuo.mobiletest.ui.activity.VoiceTestDetailsActivity;
import com.huhuo.mobiletest.ui.activity.WebPageTestDetailsActivity;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.ToastUtil;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

@ContentView(R.layout.fragment_test_result)
public class TestResultFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnItemClickListener{

    private static final String TAG = TestResultFragment.class.getSimpleName();

    @ViewInject(R.id.rv_test_result)
    private RecyclerView recyclerView;

    @ViewInject(R.id.swipe_layout)
    private SwipeRefreshLayout swipeRefreshLayout;

    private TestResultAdapter adapter;

    private ArrayList<TestResultSummaryModel> models;

    @Override
    protected void init(Bundle savedInstanceState) {
        models = initData();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new TestResultAdapter(models);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        if (models != null) {
            for (TestResultSummaryModel model : models) {
                Collection<TestItemModel> testItemModels = model.getTestItemModels();
                if (testItemModels != null && testItemModels.size() > 0) {
                    for (TestItemModel item : testItemModels) {
//                        Logger.d(TAG,"item:" + item.toString());
                    }
                }
                Logger.d(TAG,"测试结果项：" + model);
            }
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setEnabled(false);

        ArrayList<TestResultSummaryModel> models = initData();
        if (models != null) {
            adapter.updateAll(models);
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public void onItemclick(View view, int position) {
        if (models != null) {
            TestResultSummaryModel model = models.get(position);
            final int testType = model.getTestType();
            int id = model.getId();
            switch (testType) {
                case TestCode.TEST_TYPE_WEBPAGE://网页测试
                    toTestDetailsActivity(WebPageTestDetailsActivity.class,id);
                    break;
                case TestCode.TEST_TYPE_SPEED://速度测试
                    toTestDetailsActivity(DownloadTestDetailsActivity.class,id);
                    break;
                case TestCode.TEST_TYPE_VOICE://语音测试
                    toTestDetailsActivity(VoiceTestDetailsActivity.class,id);
                    break;
                case TestCode.TEST_TYPE_PING://PING测试
                    toTestDetailsActivity(PingTestDetailsActivity.class,id);
                    break;
                case TestCode.TEST_TYPE_VIDEO://视频测试
                    toTestDetailsActivity(VideoTestDetailsActivity.class,id);
                    break;
                case TestCode.TEST_TYPE_SYNTHESIZE://综合测试
                    toTestDetailsActivity(SynthesizeTestDetailsActivity.class,id);
                    break;
            }

            Logger.v(TAG, "item Click : " + model);
        }

        Gson gson = new Gson();
        String json = gson.toJson(getHttpModel());

        String url = "http://211.154.22.158/Handler/UpLoadDataHttpWork.ashx";

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("result",json);

        final long start = System.currentTimeMillis();
        HttpHelper.post(params, new SimpleHttpRequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                long end = System.currentTimeMillis();
                Logger.e(TAG,"result:" + result);
                Logger.e(TAG,"提交数据耗时:" + (end - start));

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e(TAG,"",ex);
            }
        });

//        HttpHelper2.sendRequest(url,json);

    }

    private static HttpPage getHttpModel() {
        HttpPage ht=new HttpPage();
        ht.APP_AVERAGE = 0.1f;
        ht.APP_BASEBAND = "jatest";
        ht.APP_CI = "jatest";
        ht.APP_CON_ETIME = new Date();
        ht.APP_CON_STIME = new Date();
        ht.APP_CON_TYPE = "jatest";
        ht.APP_COUNTY_TOWN_NAME = "jatest";
        ht.APP_CRS_SINR = 13;
        ht.APP_CUSHION_NUMBER = 13;
        ht.APP_CUSHION_TIME = 12;
        ht.APP_CUSHION_TOTAL_TIME = 12;
        ht.APP_DATA_SIZE = 12;
        ht.APP_DOWNLOAD_TIME = 12;
        ht.APP_ECI = "jatest";
        ht.APP_ENCODING_INFORMATION = "jatest"; ;
        ht.APP_FAC_VER = "jatest";
        ht.APP_FILE_BIT_RATE = "jatest";
        ht.APP_GPS_COORDINATE = "jatest";
        ht.APP_GROUP_ID = "jatest";
        ht.APP_GROUP_NAME = "jatest";
        ht.APP_IMEI = "jatest";
        ht.APP_IMSI = "jatest";
        ht.APP_IS_PUBLIC_LOG = "jatest";
        ht.APP_KERNEL = "jatest";
        ht.APP_LAC = "jatest";
        ht.APP_LAT = 12;
        ht.APP_LOCATION = "jatest";
        ht.APP_LOGIN_ETIME = new Date();
        ht.APP_LOGIN_STIME = new Date();
        ht.APP_LOGIN_TYPE = "jatest";
        ht.APP_LOGTIME = new Date();
        ht.APP_LON = 12;
        ht.APP_MANUFACTURE = "jatest";
        ht.APP_MAX = 12;
        ht.APP_MCC = "jatest";
        ht.APP_MIN = 12;
        ht.APP_MNC = "jatest";
        ht.APP_MSISDN = "jatest";
        ht.APP_NET_NAME = "jatest";
        ht.APP_NET_TYPE = "jatest";
        ht.APP_OPEN_TIME = 14;
        ht.APP_OS = "jatest";
        ht.APP_PLAY_TIME = 14;
        ht.APP_PROBLEM = 12;
        ht.APP_PROGRAM_TIME = 12;
        ht.APP_QUALITY_CAUSE = "jatest";
        ht.APP_QUALITY_STATE = 12;
        ht.APP_REQ_TIME = new Date();
        ht.APP_RES_TIME = new Date();
        ht.APP_RES_TYPE = "jatest";
        ht.APP_RSRP = -100;
        ht.APP_RSRQ = -12;
        ht.APP_RXLEVEL = -10;
        ht.APP_SCELL_PCI = 100;
        ht.APP_SENT_NUMBER = 10;
        ht.APP_SERVICE_IP = "jatest";
        ht.APP_SERVICE_NAME = "jatest";
        ht.APP_ServiceRequest = "jatest";
        ht.APP_SHAKE_NUMBER = 12;
        ht.APP_SOFT_VERSION = "jatest";
        ht.APP_STREET_NAME = "jatest";
        ht.APP_STREET_NUMBERL = "jatest";
        ht.APP_SUBGROUP_ID = "jatest";
        ht.APP_SUBGROUP_NAME = "jatest";
        ht.APP_SUCC_RECEIVED_NUMBER = 12;
        ht.APP_TAC = "jatest";
        ht.APP_TEST_LOCATION = "jatest";
        ht.APP_TEST_SCENARIO = "jatest";
        ht.APP_TTL_MAX = 12;
        ht.APP_TTL_MIN = 12;
        ht.APP_TYPECODE = "jatest";
        ht.APP_URL = "jatest";
        ht.APP_USER_FEELING_TIME = 12;
        ht.APP_VERSION = 122;
        ht.APP_VERSION_CODE = "jatest";
        return ht;
    }

    private void toTestDetailsActivity(Class<?> clazz,int id) {
        Intent intent = new Intent();
        intent.setClass(getActivity(),clazz);
        intent.putExtra(Constants.Key.ID,id);
        startActivity(intent);
    }

    public void refreshData(){
        ArrayList<TestResultSummaryModel> models = initData();
        if (models != null) {
            if (adapter == null) {
                Logger.v(TAG,"adapter == null,now init...");
                adapter = new TestResultAdapter();
            }
            adapter.updateAll(models);
        }

    }

    public ArrayList<TestResultSummaryModel> initData(){
        Logger.w(TAG,"initData");
        models = (ArrayList<TestResultSummaryModel>) DatabaseHelper.getInstance().testResultDao.
                queryAll();

        if (models == null) {
            return null;
        }
        ArrayList<TestResultSummaryModel> models2 = new ArrayList<TestResultSummaryModel>();
        for (TestResultSummaryModel model : models) {
            Logger.w(TAG,"test type:" + model.getTestType());
            if (model.getTestType() != TestCode.TEST_TYPE_UNKNOW) {
                models2.add(model);
            }
        }

        models.clear();
        models.addAll(models2);

        Collections.sort(models, new Comparator<TestResultSummaryModel>() {
            @Override
            public int compare(TestResultSummaryModel lhs, TestResultSummaryModel rhs) {
                Date d1 = lhs.getTestDate();
                Date d2 = rhs.getTestDate();

                if (d1 == null && d2 == null) {
                    return 0;
                }

                if (d1 == null) {
                    return -1;
                }

                if (d2 == null) {
                    return 1;
                }

                if (d1.getTime() > d2.getTime()) {
                    return -1;
                } else if (d1.getTime() < d2.getTime()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        return models;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




}
