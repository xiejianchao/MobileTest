package com.huhuo.mobiletest.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.utils.InputMethodUtil;

import org.xutils.x;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by xiejc on 15/12/11.
 */
public abstract class BaseActivity extends SwipeBackActivity {

    protected Context context;
    protected SwipeBackLayout mSwipeBackLayout;

    protected Toolbar toolbar;
    private TextView tvToolbarTitle;

    protected Handler handler = new Handler();


    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        setSwipeBackEnable(true);
        x.view().inject(this);
        initToolbar();
        init(savedInstanceState);
        //去掉ToolBar的阴影
        getSupportActionBar().setElevation(0);
    }

    private void initToolbar() {
        View v = findViewById(R.id.toolbar);
        if (v != null) {
            toolbar = (Toolbar) v;
            setSupportActionBar(toolbar);
            tvToolbarTitle = (TextView) v.findViewById(R.id.tv_toolbar_title);
            if (tvToolbarTitle != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
    }

    protected Toolbar getToolbar(){
        return toolbar;
    }

    protected void updateTitleText(int title) {
//        if (tvToolbarTitle != null) {
//            tvToolbarTitle.setText(getResources().getText(title));
//        }
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    protected void updateTitleText(String title) {
//        if (tvToolbarTitle != null) {
//            tvToolbarTitle.setText(title);
//        }

        if (toolbar != null) {
            toolbar.setTitle(title);
        }

    }

    /**
     *
     * @param enable true 开启滑动，false 关闭滑动返回，默认开启
     */
    protected void enableSwipeBack(boolean enable) {
        setSwipeBackEnable(enable);
    }


    protected void addFragment(int layoutId, Fragment fragment) {
        addFragment(layoutId, fragment, false);
    }

    protected void addFragment(int layoutId, Fragment fragment, boolean addToBackStack) {
        addFragment(layoutId, fragment, addToBackStack, null);
    }

    protected void addFragment(int layoutId, Fragment fragment, boolean addToBackStack,
                               String fragmentTag) {
        if (!TextUtils.isEmpty(fragmentTag)) {
            if (isExistFragment(fragmentTag)) {
                return;
            }
        }
        InputMethodUtil.closeInputMethod(context);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // transaction.setCustomAnimations(R.anim.slide_right_in,
        // R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
        transaction.add(layoutId, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(fragmentTag);
        }
        transaction.commit();
    }

    public boolean isExistFragment(String tag) {
        boolean exist = false;
        int count = getStackCount();
        for (int i = 0; i < count; i++) {
            String findTag = getSupportFragmentManager().getBackStackEntryAt(i).getName();
            if (tag.equals(findTag)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public int getStackCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }


    protected void toNewActivity(Class<?> clazz) {
        startActivity(new Intent(this,clazz));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            SystemClock.sleep(150);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
