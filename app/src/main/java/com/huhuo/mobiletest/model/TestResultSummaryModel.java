package com.huhuo.mobiletest.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by xiejc on 16/1/25.
 */
@DatabaseTable(tableName = "tb_test_result_summary")
public class TestResultSummaryModel {

    @DatabaseField(generatedId = true)
    private int id;

    //测试类型
    @DatabaseField
    private String testType;

    //测试发生的日期
    @DatabaseField
    private Date testDate;

    //测试结果的级别
    @DatabaseField
    private float testLevel;

    //测试耗费的时间，就是延迟
    @DatabaseField
    private float delayTime;

    @DatabaseField(canBeNull = true, foreign = true, columnName = "test_result_id")
    private TestResult testResult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public float getTestLevel() {
        return testLevel;
    }

    public void setTestLevel(float testLevel) {
        this.testLevel = testLevel;
    }

    public float getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(float delayTime) {
        this.delayTime = delayTime;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    @Override
    public String toString() {
        return "TestResultSummaryModel{" +
                "id=" + id +
                ", testType='" + testType + '\'' +
                ", testDate=" + testDate +
                ", testLevel=" + testLevel +
                ", delayTime=" + delayTime +
                ", testResult=" + testResult +
                '}';
    }
}
