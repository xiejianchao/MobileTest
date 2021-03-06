package com.huhuo.mobiletest.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.Date;

/**
 * Created by xiejc on 16/1/25.
 */
@DatabaseTable(tableName = "tb_test_result_summary")
public class TestResultSummaryModel {

    public TestResultSummaryModel(){

    }

    @DatabaseField(generatedId = true)
    private int id;

    //测试类型
    @DatabaseField
    private int testType;

    //测试发生的日期
    @DatabaseField
    private Date testDate;

    //测试结果的级别
    @DatabaseField
    private float testLevel;

    /**
     * 测试耗费的时间，就是延迟
     * 单位毫秒
     * @param delayTime
     */
    @DatabaseField
    private float delayTime;

    @DatabaseField
    private String netType;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<TestItemModel> testItemModels;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestType() {
        return testType;
    }

    public void setTestType(int testType) {
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

    /**
     * 单位毫秒
     * @param
     */
    public float getDelayTime() {
        return delayTime;
    }

    /**
     * 单位毫秒
     * @param delayTime
     */
    public void setDelayTime(float delayTime) {
        this.delayTime = delayTime;
    }

    public ForeignCollection<TestItemModel> getTestItemModels() {
        return testItemModels;
    }

    public void setTestItemModels(ForeignCollection<TestItemModel> testItemModels) {
        this.testItemModels = testItemModels;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    @Override
    public String toString() {
        return "TestResultSummaryModel{" +
                "id=" + id +
                ", testType=" + testType +
                ", testDate=" + testDate +
                ", testLevel=" + testLevel +
                ", delayTime=" + delayTime +
                ", netType='" + netType + '\'' +
                ", testItemModels=" + testItemModels +
                '}';
    }
}
