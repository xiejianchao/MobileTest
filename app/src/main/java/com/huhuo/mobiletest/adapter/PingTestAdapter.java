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

import java.util.ArrayList;

/**
 * Created by xiejc on 16/1/14.
 */
public class PingTestAdapter extends RecyclerView.Adapter<PingTestAdapter.MyViewHolder> {

    private static final String TAG = PingTestAdapter.class.getSimpleName();

    private ArrayList<CommonTestModel> models;

    public PingTestAdapter(ArrayList<CommonTestModel> models ) {
        this.models = models;
    }

    public void update(int position) {
        notifyItemChanged(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果如果需要显示不同类型的网站，需要重写该方法，通知onCreateViewHolder 显示不同的布局

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item_ping_test, parent, false);
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
    public void onBindViewHolder(PingTestAdapter.MyViewHolder holder, int position) {

        final CommonTestModel model = models.get(position);
        holder.tvPingName.setText(model.getName());
        final float delay = model.getDelay();
        Logger.d(TAG, "测试url:" + model.getUrl() + ",delay:" + delay);

        Context context = MobileTestApplication.getInstance().getApplicationContext();

        if (model.isStart()) {
            holder.tvDelay.setText(context.getString(R.string.common_delay_ms,delay));
            holder.progressBar.setProgress(100);
            holder.tvSuccRate.setText(model.getSuccessRate() + "%");
            holder.tvSpeedLevel.setVisibility(View.VISIBLE);
            holder.tvSuccRate.setVisibility(View.VISIBLE);

            if (delay < 60) {
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_faster));
            } else if (delay > 60 && delay < 100) {
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_general));
            } else if (delay > 100){
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_very_slow));
            } else {
                holder.tvDelay.setText(context.getString(R.string.common_delay_ms,"0.0"));
                holder.tvSpeedLevel.setText(context.getString(R.string.test_speed_level_timeout));
            }
        } else {
            holder.progressBar.setProgress(5);
            holder.tvDelay.setText(model.getName());
            holder.tvSuccRate.setVisibility(View.GONE);
            holder.tvSpeedLevel.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return this.models == null ? 0 : this.models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvPingName;
        private TextView tvDelay;
        private TextView tvSuccRate;
        private TextView tvSpeedLevel;
        private ProgressBar progressBar;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvPingName = (TextView) itemView.findViewById(R.id.tv_ping_name);
            tvDelay = (TextView) itemView.findViewById(R.id.tv_ping_delay);
            tvSuccRate = (TextView) itemView.findViewById(R.id.tv_ping_success_rate);
            tvSpeedLevel = (TextView) itemView.findViewById(R.id.tv_ping_speed_level);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);

        }
    }
}
