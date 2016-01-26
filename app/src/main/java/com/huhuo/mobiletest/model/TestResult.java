package com.huhuo.mobiletest.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by xiejc on 16/1/25.
 */
@DatabaseTable(tableName = "tb_test_result")
public class TestResult {


    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String netType;

    //测试类型
    @DatabaseField
    private String testType;

    @DatabaseField
    private String addr;

    @DatabaseField
    private long lon;

    @DatabaseField
    private long lat;

    @DatabaseField(canBeNull = true, foreign = true, columnName = "test_result_id")
    private TestItemModel testItemModel;

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

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public TestItemModel getTestItemModel() {
        return testItemModel;
    }

    public void setTestItemModel(TestItemModel testItemModel) {
        this.testItemModel = testItemModel;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "id=" + id +
                ", netType='" + netType + '\'' +
                ", testType='" + testType + '\'' +
                ", addr='" + addr + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", testItemModel=" + testItemModel +
                '}';
    }
}
