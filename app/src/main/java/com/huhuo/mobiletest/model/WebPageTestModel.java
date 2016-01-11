package com.huhuo.mobiletest.model;

import android.widget.TextView;

import com.timqi.sectorprogressview.ColorfulRingProgressView;

/**
 * Created by xiejc on 16/1/10.
 */
public class WebPageTestModel {

    private String url;
    private String name;
    private long speed;
    private ColorfulRingProgressView progressView;
    private TextView textView;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public ColorfulRingProgressView getProgressView() {
        return progressView;
    }

    public void setProgressView(ColorfulRingProgressView progressView) {
        this.progressView = progressView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
