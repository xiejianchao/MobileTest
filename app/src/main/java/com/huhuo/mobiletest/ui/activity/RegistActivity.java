package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;

import com.huhuo.mobiletest.R;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_regist)
public class RegistActivity extends BaseActivity {


    @Override
    protected void init(Bundle savedInstanceState) {

        toolbar.setTitle(R.string.regist);
    }



}
