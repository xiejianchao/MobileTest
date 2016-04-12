package com.huhuo.mobiletest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.model.TestItemModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by xiejianchao on 16/2/11.
 */
public class SynthesizeTestDetailsAdapter extends BaseAdapter {

    private ArrayList<TestItemModel> items;
    private DecimalFormat df;
    private Context context;

    public SynthesizeTestDetailsAdapter(ArrayList<TestItemModel> items) {
        this.items = items;
        df = new DecimalFormat("#.##");
        context = MobileTestApplication.getInstance();
    }

    @Override
    public int getCount() {
        return this.items == null ? 0 : this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return TestCode.TEST_TYPE_ARR.length;
    }

    @Override
    public int getItemViewType(int position) {
        final TestItemModel testItemModel = this.items.get(position);
        return testItemModel.getTestType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TestItemModel model = items.get(position);

        switch (model.getTestType()) {
            case TestCode.TEST_TYPE_WEBPAGE:
                WebpageViewHolder webpageHolder = null;
                if (convertView == null) {
                    webpageHolder = new WebpageViewHolder();
                    Context context = MobileTestApplication.getInstance()
                            .getApplicationContext();
                    LayoutInflater inflate = LayoutInflater.from(context);
                    convertView  = inflate.inflate(R.layout.listview_item_webpage, null);

                    webpageHolder.titleLayout = (LinearLayout) convertView.findViewById(R.id.title_layout);
                    webpageHolder.tvTestType = (TextView) convertView.findViewById(R.id.tv_test_type_title);
                    webpageHolder.tvNet = (TextView) convertView.findViewById(R.id.tv_net);
                    webpageHolder.tvTarget = (TextView) convertView.findViewById(R.id.tv_target);
                    webpageHolder.tvTotal = (TextView) convertView.findViewById(R.id.tv_total);
                    webpageHolder.tvDelay = (TextView) convertView.findViewById(R.id.tv_delay);
                    webpageHolder.tvSpeed = (TextView) convertView.findViewById(R.id.tv_speed);
                    webpageHolder.tvTestTypeTitle = (TextView) convertView.findViewById(R.id.tv_test_type_title);

                    convertView.setTag(webpageHolder);
                } else {
                    webpageHolder = (WebpageViewHolder) convertView.getTag();
                }
                setWebpageData(webpageHolder, model);
                int color = context.getResources().getColor(R.color.colorPrimaryDark);
                int colorWebPage = context.getResources().getColor(R.color.color_black);
                webpageHolder.tvTestTypeTitle.setBackgroundColor(color);
                webpageHolder.tvTestTypeTitle.setTextColor(colorWebPage);
                break;
            case TestCode.TEST_TYPE_VIDEO:
                VideoViewHolder videoHolder = null;
                if (convertView == null) {
                    videoHolder = new VideoViewHolder();
                    Context context = MobileTestApplication.getInstance()
                            .getApplicationContext();
                    LayoutInflater inflate = LayoutInflater.from(context);
                    convertView  = inflate.inflate(R.layout.listview_item_video, null);

                    videoHolder.titleLayout = (LinearLayout) convertView.findViewById(R.id.title_layout);
                    videoHolder.tvNet = (TextView) convertView.findViewById(R.id.tv_net);
                    videoHolder.tvPlayCount = (TextView) convertView.findViewById(R.id.tv_play);
                    videoHolder.tvTotal = (TextView) convertView.findViewById(R.id.tv_total_size);
                    videoHolder.tvDownloadSpeed = (TextView) convertView.findViewById(R.id.tv_download_speed);
                    videoHolder.tvBufferCount = (TextView) convertView.findViewById(R.id.tv_buffer_count);
                    videoHolder.tvDividerTitle = (View) convertView.findViewById(R.id.view_line_divider_type);
                    videoHolder.tvTestTypeTitle = (TextView) convertView.findViewById(R.id.tv_test_type_title);
                    convertView.setTag(videoHolder);
                } else {
                    videoHolder = (VideoViewHolder) convertView.getTag();
                }
                setVideoData(videoHolder, model);
                int colorBg = context.getResources().getColor(R.color.colorPrimaryDark);
                int colorVideo = context.getResources().getColor(R.color.color_black);
                videoHolder.tvTestTypeTitle.setBackgroundColor(colorBg);
                videoHolder.tvTestTypeTitle.setTextColor(colorVideo);
                break;
            case TestCode.TEST_TYPE_PING:
                PingViewHolder pingHolder = null;
                if (convertView == null) {
                    pingHolder = new PingViewHolder();
                    Context context = MobileTestApplication.getInstance()
                            .getApplicationContext();
                    LayoutInflater inflate = LayoutInflater.from(context);
                    convertView  = inflate.inflate(R.layout.listview_item_ping, null);

                    pingHolder.titleLayout = (LinearLayout) convertView.findViewById(R.id.title_layout);
                    pingHolder.tvNet = (TextView) convertView.findViewById(R.id.tv_net);
                    pingHolder.tvTarget = (TextView) convertView.findViewById(R.id.tv_target);
                    pingHolder.tvSendCount = (TextView) convertView.findViewById(R.id.tv_send_count);
                    pingHolder.tvReceiveCount = (TextView) convertView.findViewById(R.id.tv_receive_count);
                    pingHolder.tvSuccessRate = (TextView) convertView.findViewById(R.id.tv_success_rate);
                    pingHolder.tvDelay = (TextView) convertView.findViewById(R.id.tv_delay);

                    pingHolder.tvDividerTitle = (View) convertView.findViewById(R.id.view_line_divider_type);
                    pingHolder.tvTestTypeTitle = (TextView) convertView.findViewById(R.id.tv_test_type_title);
                    convertView.setTag(pingHolder);
                } else {
                    pingHolder = (PingViewHolder) convertView.getTag();
                }
                setPingData(pingHolder, model);
                int colorPingBg = context.getResources().getColor(R.color.colorPrimaryDark);
                int colorPing = context.getResources().getColor(R.color.color_black);
                pingHolder.tvTestTypeTitle.setBackgroundColor(colorPingBg);
                pingHolder.tvTestTypeTitle.setTextColor(colorPing);
                break;
        }

        return convertView;
    }
    private void setPingData(PingViewHolder holder, TestItemModel model) {
        holder.tvDividerTitle.setVisibility(View.VISIBLE);
        holder.tvTestTypeTitle.setVisibility(View.VISIBLE);

        int testType = model.getTestType();
        String testName = TestCode.getTestName(testType);
        holder.tvTestTypeTitle.setText(testName);

        holder.tvNet.setText(model.getNetType());
        holder.tvTarget.setText(model.getTarget());
        holder.tvSendCount.setText(model.getSendCount() + "");
        holder.tvReceiveCount.setText(model.getReceiveCount() + "");
        holder.tvSuccessRate.setText(model.getSuccessRate() + "%");
        holder.tvDelay.setText(model.getDelayTime() + "");
    }
    private void setVideoData(VideoViewHolder holder, TestItemModel model) {
        holder.tvDividerTitle.setVisibility(View.VISIBLE);
        holder.tvTestTypeTitle.setVisibility(View.VISIBLE);

        int testType = model.getTestType();
        String testName = TestCode.getTestName(testType);
        holder.tvTestTypeTitle.setText(testName);

        holder.tvNet.setText(model.getNetType());
        holder.tvPlayCount.setText(model.getPlayCount() + "");
        float size = model.getTotalSize() / 1024;
        if (size > 1024) {
            size /= 1024;
        }
        holder.tvTotal.setText(df.format(size) + "");
        holder.tvDownloadSpeed.setText(model.getAvgSpeed() / 1024 + "");
        holder.tvBufferCount.setText(model.getBufferCount() + "");

    }

    private void setWebpageData(WebpageViewHolder holder, TestItemModel model) {
        int testType = model.getTestType();
        String testName = TestCode.getTestName(testType);
        holder.tvTestType.setText(testName);

        holder.tvNet.setText(model.getNetType());
        holder.tvTarget.setText(model.getTarget());
        float size = (float)model.getTotalSize() / 1024;
        holder.tvTotal.setText(df.format(size) + "");

        float delay = (float)model.getDelayTime() / 1000;
        String delayTime = df.format(delay);

        holder.tvDelay.setText(delayTime);
        holder.tvSpeed.setText(df.format(model.getAvgSpeed()) + "");
    }

    private static final class WebpageViewHolder {
        private LinearLayout titleLayout;

        private TextView tvTestType;
        private TextView tvNet;
        private TextView tvTarget;
        private TextView tvTotal;
        private TextView tvDelay;
        private TextView tvSpeed;
        private TextView tvTestTypeTitle;
    }

    private static final class VideoViewHolder {
        private LinearLayout titleLayout;

        private TextView tvNet;
        private TextView tvPlayCount;
        private TextView tvTotal;
        private TextView tvDownloadSpeed;
        private TextView tvBufferCount;
        private View tvDividerTitle;
        private TextView tvTestTypeTitle;

    }

    private static final class PingViewHolder {
        private LinearLayout titleLayout;

        private TextView tvNet;
        private TextView tvTarget;
        private TextView tvSendCount;
        private TextView tvReceiveCount;
        private TextView tvSuccessRate;
        private TextView tvDelay;

        private View tvDividerTitle;
        private TextView tvTestTypeTitle;

    }
}
