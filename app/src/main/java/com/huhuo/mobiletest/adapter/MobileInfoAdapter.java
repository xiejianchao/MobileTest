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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiejc on 16/1/14.
 */
public class MobileInfoAdapter extends RecyclerView.Adapter<MobileInfoAdapter.MyViewHolder> {

    private static final String TAG = MobileInfoAdapter.class.getSimpleName();

    private ArrayList<Map<String,String>> infoList;
    private DecimalFormat df;

    public MobileInfoAdapter(ArrayList<Map<String,String>> infoList) {
        this.infoList = infoList;
        df = new DecimalFormat("#.##");
    }

    public void update(int position) {
        notifyItemChanged(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果如果需要显示不同类型的网站，需要重写该方法，通知onCreateViewHolder 显示不同的布局

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_item_mobile_info, parent, false);
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
    public void onBindViewHolder(MobileInfoAdapter.MyViewHolder holder, int position) {
        Context context = MobileTestApplication.getInstance().getApplicationContext();
        Map<String, String> map = this.infoList.get(position);
        Set<Map.Entry<String, String>> entries = map.entrySet();
        Iterator<Map.Entry<String, String>> it = entries.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> next = it.next();
            String key = next.getKey();
            String value = next.getValue();
            holder.tvKey.setText(key);
            holder.tvValue.setText(value);
        }
    }

    @Override
    public int getItemCount() {
        return this.infoList == null ? 0 : this.infoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvKey;
        private TextView tvValue;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvKey = (TextView) itemView.findViewById(R.id.tv_key);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }
}
