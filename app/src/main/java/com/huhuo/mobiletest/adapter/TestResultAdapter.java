package com.huhuo.mobiletest.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.utils.DateUtil;
import com.huhuo.mobiletest.utils.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by xiejc on 16/1/14.
 */
public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.MyViewHolder> {

    private static final String TAG = TestResultAdapter.class.getSimpleName();

    private DecimalFormat df = new DecimalFormat("#.##");

    private ArrayList<TestResultSummaryModel> models;

    public TestResultAdapter() {
    }

    public TestResultAdapter(ArrayList<TestResultSummaryModel> models) {
        this.models = models;
    }

    public void updateAll(ArrayList<TestResultSummaryModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    public void update(int position) {
        notifyItemChanged(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果如果需要显示不同类型的网站，需要重写该方法，通知onCreateViewHolder 显示不同的布局

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item_test_result, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * 如果需要显示不同类型，需要重写该方法，通知onCreateViewHolder 显示不同的布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final TestResultAdapter.MyViewHolder holder, final int position) {
        final TestResultSummaryModel model = models.get(position);
        float time = (model.getDelayTime() / 1000);
        final String testDelay = df.format(time);

        holder.tvTestTypeName.setText(TestCode.getTestName(model.getTestType()));
        if (model.getTestDate() != null) {
            String testDate = DateUtil.getFormatTime(model.getTestDate(), DateUtil.PATTERN_STANDARD);
            holder.tvTestDate.setText(testDate);
        } else {
            Logger.w(TAG,"测试日期字段为null");
        }
        holder.tvTestDelay.setText(testDelay);
        holder.rbTestLevel.setRating(model.getTestLevel());

        switch (model.getTestType()) {
            case TestCode.TEST_TYPE_WEBPAGE:
                holder.ivTestType.setImageResource(R.drawable.icon_test_webpage);
                break;
            case TestCode.TEST_TYPE_VOICE:
                holder.ivTestType.setImageResource(R.drawable.icon_test_voice);
                break;
            case TestCode.TEST_TYPE_VIDEO:
                holder.ivTestType.setImageResource(R.drawable.icon_test_video);
                break;
            case TestCode.TEST_TYPE_PING:
                holder.ivTestType.setImageResource(R.drawable.icon_test_ping);
                break;
            default:
                holder.ivTestType.setImageResource(R.drawable.icon_test_signal);
                break;
        }

        if (onItemClickListener != null) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemclick(holder.cardView,position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return this.models == null ? 0 : this.models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView ivTestType;
        private TextView tvTestTypeName;
        private TextView tvTestDate;
        private TextView tvTestDelay;
        private RatingBar rbTestLevel;


        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            ivTestType = (ImageView) itemView.findViewById(R.id.iv_test_item_icon);
            tvTestTypeName = (TextView) itemView.findViewById(R.id.tv_test_type_name);
            tvTestDate = (TextView) itemView.findViewById(R.id.tv_test_date);
            tvTestDelay = (TextView) itemView.findViewById(R.id.tv_ping_delay);
            rbTestLevel = (RatingBar) itemView.findViewById(R.id.rb_test_level);
        }
    }
}
