package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.view.DialChart03View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_net_speed_test)
public class NetSpeedTestActivity extends BaseActivity {

    @ViewInject(R.id.circle_view)
    private DialChart03View chartView;

    @Override
    protected void init(Bundle savedInstanceState) {

        chartView.setCurrentStatus(0.9f);

    }


}
