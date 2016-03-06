package com.huhuo.mobiletest.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.OnItemClickListener;
import com.huhuo.mobiletest.adapter.SynthesizeTestDetailsAdapter;
import com.huhuo.mobiletest.adapter.TestResultAdapter;
import com.huhuo.mobiletest.constants.Constants;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.model.TestItemModel;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.ui.activity.DownloadTestDetailsActivity;
import com.huhuo.mobiletest.ui.activity.PingTestDetailsActivity;
import com.huhuo.mobiletest.ui.activity.SynthesizeTestDetailsActivity;
import com.huhuo.mobiletest.ui.activity.VideoTestDetailsActivity;
import com.huhuo.mobiletest.ui.activity.VoiceTestDetailsActivity;
import com.huhuo.mobiletest.ui.activity.WebPageTestDetailsActivity;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.ToastUtil;

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
                        Logger.d(TAG,"item:" + item.toString());
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
                case TestCode.TEST_TYPE_VIDEO://综合测试
                    toTestDetailsActivity(VideoTestDetailsActivity.class,id);
                    break;
                case TestCode.TEST_TYPE_SYNTHESIZE:
                    toTestDetailsActivity(SynthesizeTestDetailsActivity.class,id);
                    break;
            }

            Logger.v(TAG, "item Click : " + model);
        }
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
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
