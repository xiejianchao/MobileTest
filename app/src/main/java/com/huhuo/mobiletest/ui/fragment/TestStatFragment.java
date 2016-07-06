package com.huhuo.mobiletest.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.huhuo.mobiletest.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.fragment_test_stat)
public class TestStatFragment extends BaseFragment {


    @ViewInject(R.id.iv_test)
    private TextView tvTips;

    private static final String TAG = TestStatFragment.class.getSimpleName();

    @Override
    protected void init(Bundle savedInstanceState) {

        tvTips.setText("Coming soon !");

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
