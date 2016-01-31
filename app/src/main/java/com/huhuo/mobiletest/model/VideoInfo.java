package com.huhuo.mobiletest.model;

/**
 * Created by xiejc on 16/1/31.
 */
public class VideoInfo {

    private long duration;
    private int width;
    private int height;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "duration=" + duration +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
