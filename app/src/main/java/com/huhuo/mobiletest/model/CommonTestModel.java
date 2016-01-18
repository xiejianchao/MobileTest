package com.huhuo.mobiletest.model;

/**
 * Created by xiejc on 15/12/4.
 */
public class CommonTestModel {

    private float slowestSpeed;
    private long fastestSpeed;
    private long avgSpeed;
    private long currentSpeed;
    private String name;
    private float delay;
    private float successRate;
    private int speedLevel;
    private String url;

    private boolean isStart;

    public float getSlowestSpeed() {
        return slowestSpeed;
    }

    public void setSlowestSpeed(float slowestSpeed) {
        this.slowestSpeed = slowestSpeed;
    }

    public long getFastestSpeed() {
        return fastestSpeed;
    }

    public void setFastestSpeed(long fastestSpeed) {
        this.fastestSpeed = fastestSpeed;
    }

    public long getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(long avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public long getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(long currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(float successRate) {
        this.successRate = successRate;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(int speedLevel) {
        this.speedLevel = speedLevel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }
}
