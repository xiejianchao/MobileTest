package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;


import com.huhuo.mobiletest.R;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @Override
    protected void init(Bundle savedInstanceState) {

        toolbar.setTitle(R.string.guide_login);

    }

}
