package com.huhuo.mobiletest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.model.CommonTestModel;
import com.huhuo.mobiletest.utils.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by xiejc on 16/1/14.
 */
public class SynthesizeTestAdapter extends RecyclerView.Adapter<SynthesizeTestAdapter.MyViewHolder> {

    private static final String TAG = SynthesizeTestAdapter.class.getSimpleName();

    private ArrayList<CommonTestModel> models;
    private DecimalFormat df;

    public SynthesizeTestAdapter(ArrayList<CommonTestModel> models) {
        this.models = models;
        df = new DecimalFormat("#.##");
    }

    public void update(int position) {
        notifyItemChanged(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果如果需要显示不同类型的网站，需要重写该方法，通知onCreateViewHolder 显示不同的布局

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_item_synthesize_test, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * 如果需要显示不同类型的网站，需要重写该方法，通知onCreateViewHolder 显示不同的布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(SynthesizeTestAdapter.MyViewHolder holder, int position) {
        final CommonTestModel model = models.get(position);
        holder.tvtestName.setText(model.getName());
        final float delay = model.getDelay();
        Logger.d(TAG, "测试url:" + model.getUrl() + ",delay:" + delay);

        Context context = MobileTestApplication.getInstance().getApplicationContext();

        if (model.isStart()) {
            if (position == 0) {//网页测试
                setItemData(holder, model, delay, context, position);
            } else if (position == 1) {//视频测试
                setVideoItemData(holder, model, context);
            } else if (position == 2) {//ping测试
                setItemData(holder, model, delay, context, position);
            }
            holder.progressBar.setProgress((int) model.getPercent());
        } else {
            holder.progressBar.setProgress(5);
            holder.tvAvgSpeed.setText(model.getName());
            holder.tvTimeSuccRate.setVisibility(View.GONE);
            holder.tvSpeedLevel.setVisibility(View.GONE);
        }


    }

    private void setVideoItemData(MyViewHolder holder, CommonTestModel model, Context context) {
        if ((int)model.getPercent() < 100) {
            holder.tvSpeedLevel.setVisibility(View.GONE);
            holder.tvTimeSuccRate.setVisibility(View.VISIBLE);
            holder.tvAvgSpeed.setText("视频测试服务开始...");
            holder.tvTimeSuccRate.setText(model.getAvgSpeed() / 1024 * 8 + "kbps");
        } else {
            holder.tvTimeSuccRate.setVisibility(View.VISIBLE);
            holder.tvAvgSpeed.setVisibility(View.VISIBLE);
            holder.tvAvgSpeed.setText("均速：" + model.getAvgSpeed() / 1024 + "KB/s");
            float delayTime = model.getDelay() / 1024;
            String videoDelayTime = df.format(delayTime);
            holder.tvTimeSuccRate.setText(videoDelayTime+ "秒");
            holder.tvSpeedLevel.setVisibility(View.VISIBLE);

            if (model.getSpeedLevel() == 5) {
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_faster));
            } else if (model.getSpeedLevel() == 4 || model.getSpeedLevel() == 3) {
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_general));
            } else if (model.getSpeedLevel() == 2){
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_very_slow));
            } else {
                holder.tvAvgSpeed.setText(context.getString(R.string.common_delay_ms, "0.0"));
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_timeout));
            }
        }
    }

    private void setItemData(MyViewHolder holder, CommonTestModel model, float delay, Context
            context, int position) {
        holder.tvAvgSpeed.setText(context.getString(R.string.common_delay_ms, delay));
        if ((int)model.getPercent() < 100) {
            holder.tvTimeSuccRate.setVisibility(View.GONE);
            holder.tvSpeedLevel.setVisibility(View.GONE);
            holder.tvAvgSpeed.setVisibility(View.VISIBLE);
            Logger.v(TAG, "测试url:" + model.getUrl());
            if (model.getUrl().contains("1688")) {
                model.setUrl("http://www.taobao.com");
            }
            holder.tvAvgSpeed.setText(model.getUrl());
        } else {
            holder.tvTimeSuccRate.setVisibility(View.VISIBLE);
            holder.tvSpeedLevel.setVisibility(View.VISIBLE);

            if (position == 0) {
                holder.tvAvgSpeed.setText("均速：" + model.getAvgSpeed() + "kbp/s");
            } else if (position == 2) {
                holder.tvAvgSpeed.setText("时延：" + df.format(model.getDelay()) + "ms");
            }

            holder.tvSpeedLevel.setText(model.getSpeedLevel() + "kbps");

            if (position == 0) {
                if (model.getDelay() > 0) {
                    float delayS = model.getDelay() / 1000;
                    final String delayStr = df.format(delayS);
                    holder.tvTimeSuccRate.setText(delayStr + "秒");
                } else {
                    holder.tvTimeSuccRate.setText(0 + "秒");
                }
            } else {
                holder.tvTimeSuccRate.setText("成功率：" + model.getSuccessRate() + "%");
            }

            if (model.getSpeedLevel() == 5) {
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_faster));
            } else if (model.getSpeedLevel() == 4 || model.getSpeedLevel() == 3) {
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_general));
            } else if (model.getSpeedLevel() == 2 || model.getSpeedLevel() == 1){
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_very_slow));
            } else {
                holder.tvAvgSpeed.setText(context.getString(R.string.common_delay_ms, "0.0"));
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_timeout));
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.models == null ? 0 : this.models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvtestName;
        private TextView tvAvgSpeed;
        private TextView tvTimeSuccRate;
        private TextView tvSpeedLevel;
        private ProgressBar progressBar;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvtestName = (TextView) itemView.findViewById(R.id.tv_test_name);
            tvAvgSpeed = (TextView) itemView.findViewById(R.id.tv_avg_speed);
            tvTimeSuccRate = (TextView) itemView.findViewById(R.id.tv_test_delay_time);
            tvSpeedLevel = (TextView) itemView.findViewById(R.id.tv_test_speed_level);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }
}
