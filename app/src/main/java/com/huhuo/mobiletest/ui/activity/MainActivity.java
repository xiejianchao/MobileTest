package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.ui.fragment.OneKeyTestFragment;
import com.huhuo.mobiletest.ui.fragment.TaskTestFragment;
import com.huhuo.mobiletest.ui.fragment.ReportFragment;
import com.huhuo.mobiletest.ui.fragment.TestResultFragment;
import com.huhuo.mobiletest.ui.fragment.TestStatFragment;
import com.huhuo.mobiletest.utils.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

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
                updateTitleText(R.string.fragment_title_test_stat);
            break;
            case R.id.layout_report:
                setTabSelection(4);
                updateTitleText(R.string.fragment_title_report);
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
        switch (index) {
            case 0:
                tvOneKeyTest.setTextColor(getResources().getColor(R.color.color_blue_default));
                tvOneKeyTest.setSelected(true);
                tvTaskTest.setSelected(false);
                tvTestResult.setSelected(false);
                tvTestStat.setSelected(false);
                tvReport.setSelected(false);
                break;
            case 1:
                tvTaskTest.setTextColor(getResources().getColor(R.color.color_blue_default));
                tvOneKeyTest.setSelected(false);
                tvTaskTest.setSelected(true);
                tvTestResult.setSelected(false);
                tvTestStat.setSelected(false);
                tvReport.setSelected(false);
                break;

            case 2:
                tvTestResult.setTextColor(getResources().getColor(R.color.color_blue_default));
                tvOneKeyTest.setSelected(false);
                tvTaskTest.setSelected(false);
                tvTestResult.setSelected(true);
                tvTestStat.setSelected(false);
                tvReport.setSelected(false);
                break;

            case 3:
                tvTestStat.setTextColor(getResources().getColor(R.color.color_blue_default));
                tvOneKeyTest.setSelected(false);
                tvTaskTest.setSelected(false);
                tvTestResult.setSelected(false);
                tvTestStat.setSelected(true);
                tvReport.setSelected(false);
                break;

            case 4:
                tvReport.setTextColor(getResources().getColor(R.color.color_blue_default));
                tvOneKeyTest.setSelected(false);
                tvTaskTest.setSelected(false);
                tvTestResult.setSelected(false);
                tvTestStat.setSelected(false);
                tvReport.setSelected(true);
                break;

        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tvOneKeyTest.setTextColor(getResources().getColor(
                R.color.color_guide_background_press));
        tvTaskTest.setTextColor(getResources().getColor(
                R.color.color_guide_background_press));
        tvTestResult.setTextColor(getResources().getColor(
                R.color.color_guide_background_press));
        tvTestStat.setTextColor(getResources().getColor(
                R.color.color_guide_background_press));
        tvReport.setTextColor(getResources().getColor(
                R.color.color_guide_background_press));
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

}
