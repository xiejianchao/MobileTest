package com.huhuo.mobiletest.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.MobileInfoAdapter;
import com.huhuo.mobiletest.utils.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_mobile_info)
public class MobileInfoActivity extends BaseActivity {

    private static final String TAG = MobileInfoActivity.class.getSimpleName();

    @ViewInject(R.id.rv_mobile_info)
    private RecyclerView recyclerView;

    private MobileInfoAdapter adapter;

    private ArrayList<Map<String,String>> infoList = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        initData();

    }

    private void initData() {
        //手机品牌
        String brandName = Build.MANUFACTURER;
        Map<String,String> brandMap = new HashMap<>();
        brandMap.put(getString(R.string.test_mobile_brand),brandName);
        infoList.add(brandMap);

        //手机型号
        String modelName = Build.MODEL;
        Map<String,String> modelMap = new HashMap<>();
        modelMap.put(getString(R.string.test_mobile_model),modelName);
        infoList.add(modelMap);

        //手机系统版本
        String versionRelase = Build.VERSION.RELEASE;
        Map<String,String> versionMap = new HashMap<>();
        versionMap.put(getString(R.string.test_mobile_version), versionRelase);
        infoList.add(versionMap);

        //手机识别码
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        Map<String,String> deviceIdMap = new HashMap<>();
        deviceIdMap.put(getString(R.string.test_mobile_device_id), deviceId);
        infoList.add(deviceIdMap);

        adapter = new MobileInfoAdapter(infoList);
        recyclerView.setAdapter(adapter);

        Logger.v(TAG, "infoList:" + infoList.toString());


    }


}
