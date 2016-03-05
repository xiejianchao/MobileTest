package com.huhuo.mobiletest.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by xiejc on 16/1/25.
 */
@DatabaseTable(tableName = "tb_test_item")
public class TestItemModel {

    public TestItemModel(){

    }

    @DatabaseField(generatedId = true)
    private int id;

    //测试类型
    @DatabaseField
    private int testType;

    //网络类型
    @DatabaseField
    private String netType;

    //测试的目标，比如网页测试时表示是哪个网站
    @DatabaseField
    private String target;

    //测试产生的总数据量
    @DatabaseField
    private long totalSize;

    //测试耗费的时间，就是延迟
    @DatabaseField
    private float delayTime;

    //测试的成功率
    @DatabaseField
    private float successRate;

    //发送次数
    @DatabaseField
    public int sendCount;

    //接收次数
    @DatabaseField
    private int receiveCount;

    //最快速率
    @DatabaseField
    private float fastestSpeed;

    //平均速率
    @DatabaseField
    private float avgSpeed;

    //缓冲次数
    @DatabaseField
    private int bufferCount;

    //拨打时间
    @DatabaseField
    private int callTime;

    //呼叫类型
    @DatabaseField
    private String callType;

    //结果
    @DatabaseField
    private boolean result;

    //播放次数
    @DatabaseField
    private int playCount;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
    private TestResultSummaryModel testResultSummaryModel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public float getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(float delayTime) {
        this.delayTime = delayTime;
    }

    public float getFastestSpeed() {
        return fastestSpeed;
    }

    public void setFastestSpeed(float fastestSpeed) {
        this.fastestSpeed = fastestSpeed;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getBufferCount() {
        return bufferCount;
    }

    public void setBufferCount(int bufferCount) {
        this.bufferCount = bufferCount;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public TestResultSummaryModel getTestResultSummaryModel() {
        return testResultSummaryModel;
    }

    public void setTestResultSummaryModel(TestResultSummaryModel testResultSummaryModel) {
        this.testResultSummaryModel = testResultSummaryModel;
    }

    public int getCallTime() {
        return callTime;
    }

    public void setCallTime(int callTime) {
        this.callTime = callTime;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(float successRate) {
        this.successRate = successRate;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getTestType() {
        return testType;
    }

    public void setTestType(int testType) {
        this.testType = testType;
    }

    @Override
    public String toString() {
        return "TestItemModel{" +
                "id=" + id +
                ", testType=" + testType +
                ", netType='" + netType + '\'' +
                ", target='" + target + '\'' +
                ", totalSize=" + totalSize +
                ", delayTime=" + delayTime +
                ", successRate=" + successRate +
                ", sendCount=" + sendCount +
                ", receiveCount=" + receiveCount +
                ", fastestSpeed=" + fastestSpeed +
                ", avgSpeed=" + avgSpeed +
                ", bufferCount=" + bufferCount +
                ", callTime=" + callTime +
                ", callType='" + callType + '\'' +
                ", result=" + result +
                ", playCount=" + playCount +
                ", testResultSummaryModel=" + testResultSummaryModel +
                '}';
    }
}
