package com.huhuo.mobiletest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.VideoTestDetailsAdapter;
import com.huhuo.mobiletest.adapter.VoiceTestDetailsAdapter;
import com.huhuo.mobiletest.constants.Constants;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.model.TestItemModel;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.utils.DateUtil;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.ToastUtil;
import com.j256.ormlite.dao.ForeignCollection;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.activity_video_test_details)
public class VideoTestDetailsActivity extends BaseActivity {

    private static final String TAG = VideoTestDetailsActivity.class.getSimpleName();

    @ViewInject(R.id.tv_test_level)
    private TextView tvTestLevel;

    @ViewInject(R.id.rb_test_level)
    private RatingBar rbTestLevel;

    @ViewInject(R.id.tv_test_net_type)
    private TextView tvTestNetType;

    @ViewInject(R.id.tv_test_date)
    private TextView tvTestDate;

    @ViewInject(R.id.tv_test_type)
    private TextView tvTestType;

    @ViewInject(R.id.tv_test_type2)
    private TextView tvTestType2;

    @ViewInject(R.id.lv_webpage_test)
    private ListView lvTestItem;

    @ViewInject(R.id.tv_addr)
    private TextView tvAddr;

    private VideoTestDetailsAdapter adapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            int id = intent.getIntExtra(Constants.Key.ID, -1);
            if (id > 0) {
                showData(id);
            }
        } else {
            ToastUtil.showMessage("数据非法");
        }
    }

    private void showData(int id) {
        TestResultSummaryModel summaryModel = DatabaseHelper.getInstance().testResultDao
                .queryById(id);

        if (summaryModel != null) {
            float testLevel = summaryModel.getTestLevel();
            tvTestLevel.setVisibility(View.GONE);

            rbTestLevel.setRating(testLevel);
            tvTestNetType.setText("网络类型：" + summaryModel.getNetType());
            String testDate = DateUtil.getFormatTime(summaryModel.getTestDate(), DateUtil
                    .PATTERN_STANDARD);
            tvTestDate.setText(testDate);

            int testType = summaryModel.getTestType();
            String testName = TestCode.getTestName(testType);
            tvTestType.setText(testName);
            tvTestType2.setText(testName);

            ForeignCollection<TestItemModel> testItemModels = summaryModel
                    .getTestItemModels();
            if (testItemModels != null && testItemModels.size() > 0) {

                ArrayList<TestItemModel> list = new ArrayList<>();
                for (TestItemModel model : testItemModels) {
                    list.add(model);
                    Logger.d(TAG, "TestItemModel item:" + model);
                }

                adapter = new VideoTestDetailsAdapter(list);
                lvTestItem.setAdapter(adapter);
            }
        }

        Logger.d(TAG,"summaryModel：" + summaryModel);
    }

    private String getLevel(float level) {
        if (level == 5) {
            return getString(R.string.test_speed_faster);
        } else if (level == 4) {
            return getString(R.string.test_speed_better);
        } else if (level == 3) {
            return getString(R.string.test_speed_general);
        } else {
            return getString(R.string.test_speed_slow);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobileTestApplication application = ((MobileTestApplication)getApplication());
        application.setLocationTextView(tvAddr, true);
    }


}
