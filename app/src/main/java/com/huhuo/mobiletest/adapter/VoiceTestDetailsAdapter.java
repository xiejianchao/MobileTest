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
import com.huhuo.mobiletest.model.TestItemModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by xiejianchao on 16/2/11.
 */
public class VoiceTestDetailsAdapter extends BaseAdapter {

    private ArrayList<TestItemModel> items;
    private DecimalFormat df;

    public VoiceTestDetailsAdapter(ArrayList<TestItemModel> items) {
        this.items = items;
        df = new DecimalFormat("#.##");
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            Context context = MobileTestApplication.getInstance()
                    .getApplicationContext();
            LayoutInflater inflate = LayoutInflater.from(context);
            convertView  = inflate.inflate(R.layout.listview_item_voice, null);

            holder.titleLayout = (LinearLayout) convertView.findViewById(R.id.title_layout);
            holder.tvNet = (TextView) convertView.findViewById(R.id.tv_net);
            holder.tvTarget = (TextView) convertView.findViewById(R.id.tv_target);
            holder.tvCallType = (TextView) convertView.findViewById(R.id.tv_call_type);
            holder.tvCallTime = (TextView) convertView.findViewById(R.id.tv_call_time);
            holder.tvResult = (TextView) convertView.findViewById(R.id.tv_result);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.titleLayout.setVisibility(View.VISIBLE);
        } else {
            holder.titleLayout.setVisibility(View.GONE);
        }

        TestItemModel model = items.get(position);
        holder.tvNet.setText(model.getNetType());
        holder.tvTarget.setText(model.getTarget());

        holder.tvCallType.setText(model.getCallType());
        holder.tvCallTime.setText(model.getCallTime() + "");
        holder.tvResult.setText(model.isResult() ? "成功" : "失败");

        return convertView;
    }

    private static final class ViewHolder {
        private LinearLayout titleLayout;

        private TextView tvNet;
        private TextView tvTarget;
        private TextView tvCallType;
        private TextView tvCallTime;
        private TextView tvResult;

    }
}
