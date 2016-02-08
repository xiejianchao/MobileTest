package com.huhuo.mobiletest.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ImageView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.constants.Constants;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.db.MessageDao;
import com.huhuo.mobiletest.db.RecentMessageDao;
import com.huhuo.mobiletest.model.MessageModel;
import com.huhuo.mobiletest.model.RecentMessageModel;
import com.huhuo.mobiletest.utils.ImageUtil;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;
import java.util.List;


@ContentView(R.layout.fragment_report)
public class ReportFragment extends BaseFragment {

    private static final String TAG = ReportFragment.class.getSimpleName();
    @ViewInject(R.id.iv_test)
    private ImageView ivTest;

    private RecentMessageDao recentMessageDao;
    private MessageDao messageDao;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
