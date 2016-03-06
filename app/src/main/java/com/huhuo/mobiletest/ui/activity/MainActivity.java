package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.ui.fragment.OneKeyTestFragment;
import com.huhuo.mobiletest.ui.fragment.TaskTestFragment;
import com.huhuo.mobiletest.ui.fragment.ReportFragment;
import com.huhuo.mobiletest.ui.fragment.TestResultFragment;
import com.huhuo.mobiletest.ui.fragment.TestStatFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    
    private static final String TAG = MainActivity.class.getSimpleName();

    private FragmentManager fragmentManager;

    // 一键测试
    private OneKeyTestFragment oneKeyTestFragment;

    // 任务测试
    private TaskTestFragment taskTestFragment;

    // 测试结果
    private TestResultFragment testResultFragment;

    // 测试统计
    private TestStatFragment testStatFragment;

    // 问题报告
    private ReportFragment reportFragment;
    
    //一键测试tab的TextView
    @ViewInject(R.id.tv_one_key_test)
    private TextView tvOneKeyTest;
    
    //任务测试tab的TextView
    @ViewInject(R.id.tv_task_test)
    private TextView tvTaskTest;
    
    //测试结果tab的TextView
    @ViewInject(R.id.tv_test_result)
    private TextView tvTestResult;

    //测试统计tab的TextView
    @ViewInject(R.id.tv_test_stat)
    private TextView tvTestStat;

    //问题上报tab的TextView
    @ViewInject(R.id.tv_report)
    private TextView tvReport;

    @ViewInject(R.id.layout_one_key_test)
    private RelativeLayout layoutOneKeyTest;

    @ViewInject(R.id.layout_task_test)
    private RelativeLayout layoutTaskTest;

    @ViewInject(R.id.layout_test_result)
    private RelativeLayout layoutTestResult;

    @ViewInject(R.id.layout_test_stat)
    private RelativeLayout layoutTestStat;

    @ViewInject(R.id.layout_report)
    private RelativeLayout layoutReport;

    @Override
    protected void init(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
//        updateTitleText(R.string.app_name);
        // 默认选中首屏
        setTabSelection(0);
        enableSwipeBack(false);//首页禁止开启右滑返回
    }

    @Event(value = {
            R.id.layout_one_key_test,
            R.id.layout_task_test,
            R.id.layout_test_result,
            R.id.layout_test_stat,
            R.id.layout_report})
    private void tabLayoutClick(View view) {

        switch (view.getId()) {
            case R.id.layout_one_key_test:
                setTabSelection(0);
                updateTitleText(R.string.app_name);
                break;
            case R.id.layout_task_test:
                setTabSelection(1);
                updateTitleText(R.string.hezu_message);
                break;
            case R.id.layout_test_result:
                setTabSelection(2);
                updateTitleText(R.string.hezu_myself);
                break;
            case R.id.layout_test_stat:
                setTabSelection(3);
                updateTitleText(R.string.test_type_test_stat);
            break;
            case R.id.layout_report:
                setTabSelection(4);
                updateTitleText(R.string.test_type_report);
            break;

        }

    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示合租吧，1表示消息，2表示我的
     */
    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 先清除掉上次的选中状态
        clearSelection();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switchSelectedTabImg(index);
        switch (index) {
            case 0:
                if (oneKeyTestFragment == null) {
                    oneKeyTestFragment = new OneKeyTestFragment();
                    transaction.add(R.id.content, oneKeyTestFragment);
                } else {
                    transaction.show(oneKeyTestFragment);
                }
                break;
            case 1:
                // 当点击了一键测试tab时，改变控件的图片和文字颜色
                if (taskTestFragment == null) {
                    // 如果OrderFragment为空，则创建一个并添加到界面上
                    taskTestFragment = new TaskTestFragment();
                    transaction.add(R.id.content, taskTestFragment);
                } else {
                    // 如果OrderFragment不为空，则直接将它显示出来
                    transaction.show(taskTestFragment);
                }
                break;

            case 2:
                if (testResultFragment == null) {
                    testResultFragment = new TestResultFragment();
                    transaction.add(R.id.content, testResultFragment);
                } else {
                    transaction.show(testResultFragment);
                }

                testResultFragment.refreshData();
                break;

            case 3:
                if (testStatFragment == null) {
                    testStatFragment = new TestStatFragment();
                    transaction.add(R.id.content, testStatFragment);
                } else {
                    transaction.show(testStatFragment);
                }
                break;

            case 4:
                if (reportFragment == null) {
                    reportFragment = new ReportFragment();
                    transaction.add(R.id.content, reportFragment);
                } else {
                    // 如果MyFragment不为空，则直接将它显示出来
                    transaction.show(reportFragment);
                }
                break;

        }
        transaction.commit();
    }

    private void switchSelectedTabImg(int index) {
        layoutOneKeyTest.setSelected(true);
        layoutTaskTest.setSelected(true);
        layoutTestResult.setSelected(true);
        layoutTestStat.setSelected(true);
        layoutReport.setSelected(true);

        switch (index) {
            case 0:
                tvOneKeyTest.setTextColor(getResources().getColor(R.color.color_yellow));
                layoutOneKeyTest.setBackgroundResource(R.drawable.tab_one_key_test_pressed);
//                layoutOneKeyTest.setPressed(true);

                layoutTaskTest.setBackgroundResource(R.drawable.selector_task_test);
                layoutTestResult.setBackgroundResource(R.drawable.selector_test_result);
                layoutTestStat.setBackgroundResource(R.drawable.selector_test_stat);
                layoutReport.setBackgroundResource(R.drawable.selector_error_report);
                break;
            case 1:
                tvTaskTest.setTextColor(getResources().getColor(R.color.color_yellow));
                layoutTaskTest.setBackgroundResource(R.drawable.tab_task_test_pressed);

                layoutOneKeyTest.setBackgroundResource(R.drawable.selector_one_key_test);
                layoutTestResult.setBackgroundResource(R.drawable.selector_test_result);
                layoutTestStat.setBackgroundResource(R.drawable.selector_test_stat);
                layoutReport.setBackgroundResource(R.drawable.selector_error_report);
                break;

            case 2:
                tvTestResult.setTextColor(getResources().getColor(R.color.color_yellow));
                layoutTestResult.setBackgroundResource(R.drawable.tab_test_result_pressed);

                layoutOneKeyTest.setBackgroundResource(R.drawable.selector_one_key_test);
                layoutTaskTest.setBackgroundResource(R.drawable.selector_task_test);
                layoutTestStat.setBackgroundResource(R.drawable.selector_test_stat);
                layoutReport.setBackgroundResource(R.drawable.selector_error_report);
                break;

            case 3:
                tvTestStat.setTextColor(getResources().getColor(R.color.color_yellow));
                layoutTestStat.setBackgroundResource(R.drawable.tab_test_stat_pressed);

                layoutOneKeyTest.setBackgroundResource(R.drawable.selector_one_key_test);
                layoutTaskTest.setBackgroundResource(R.drawable.selector_task_test);
                layoutTestResult.setBackgroundResource(R.drawable.selector_test_result);
                layoutReport.setBackgroundResource(R.drawable.selector_error_report);
                break;

            case 4:
                tvReport.setTextColor(getResources().getColor(R.color.color_yellow));
                layoutReport.setBackgroundResource(R.drawable.tab_error_report_pressed);

                layoutOneKeyTest.setBackgroundResource(R.drawable.selector_one_key_test);
                layoutTaskTest.setBackgroundResource(R.drawable.selector_task_test);
                layoutTestResult.setBackgroundResource(R.drawable.selector_test_result);
                layoutTestStat.setBackgroundResource(R.drawable.selector_test_stat);
                break;

        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tvOneKeyTest.setTextColor(getResources().getColor(
                R.color.color_white));
        tvTaskTest.setTextColor(getResources().getColor(
                R.color.color_white));
        tvTestResult.setTextColor(getResources().getColor(
                R.color.color_white));
        tvTestStat.setTextColor(getResources().getColor(
                R.color.color_white));
        tvReport.setTextColor(getResources().getColor(
                R.color.color_white));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (oneKeyTestFragment != null) {
            transaction.hide(oneKeyTestFragment);
        }
        if (taskTestFragment != null) {
            transaction.hide(taskTestFragment);
        }
        if (testResultFragment != null) {
            transaction.hide(testResultFragment);
        }
        if (testStatFragment != null) {
            transaction.hide(testStatFragment);
        }
        if (reportFragment != null) {
            transaction.hide(reportFragment);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHelper.getInstance().close();
    }
}
