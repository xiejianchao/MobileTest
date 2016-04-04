package com.huhuo.mobiletest.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.utils.Logger;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_mobile_info)
public class MobileInfoActivity extends BaseActivity {

    private static final String TAG = MobileInfoActivity.class.getSimpleName();

    private ArrayList<Map<String,String>> infoList = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) {

        initData();


    }

    private void initData() {
        //手机品牌
        String brandName = android.os.Build.MANUFACTURER;
        Map<String,String> brandMap = new HashMap<>();
        brandMap.put(getString(R.string.test_mobile_brand),brandName);
        infoList.add(brandMap);

        //手机型号
        String modelName = android.os.Build.MODEL;
        Map<String,String> modelMap = new HashMap<>();
        modelMap.put(getString(R.string.test_mobile_model),modelName);
        infoList.add(modelMap);

        //手机系统版本
        String versionRelase = Build.VERSION.RELEASE;
        Map<String,String> versionMap = new HashMap<>();
        versionMap.put(getString(R.string.test_mobile_version), versionRelase);
        infoList.add(versionMap);

        Logger.v(TAG,"infoList:" + infoList.toString());


    }


}
