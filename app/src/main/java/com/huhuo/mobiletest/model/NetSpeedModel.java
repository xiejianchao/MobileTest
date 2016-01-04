package com.huhuo.mobiletest.model;

/**
 * Created by xiejc on 15/12/4.
 */
public class NetSpeedModel {

    private float slowestSpeed;
    private long fastestSpeed;
    private long avgSpeed;

    private long currentSpeed;

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
}
