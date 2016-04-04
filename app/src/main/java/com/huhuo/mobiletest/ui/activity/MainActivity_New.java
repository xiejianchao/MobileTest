package com.huhuo.mobiletest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.MyFragmentPagerAdapter;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.ui.fragment.OneKeyTestFragment;
import com.huhuo.mobiletest.ui.fragment.ReportFragment;
import com.huhuo.mobiletest.ui.fragment.TestResultFragment;
import com.huhuo.mobiletest.ui.fragment.TestStatFragment;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.ToastUtil;
import com.huhuo.mobiletest.view.PagerSlidingTabStrip;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main_new)
public class MainActivity_New extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //save our header or result
    private Drawer result = null;

    private Toolbar toolbar = null;

    //save our header or result
    private List<Fragment> pages = new ArrayList<Fragment>();

    private OneKeyTestFragment oneKeyTestFragment;
    private TestResultFragment testResultFragment;
    private TestStatFragment testStatFragment;


    @Override
    public void init(Bundle savedInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enableSwipeBack(false);
        initDrawer(savedInstanceState);

        initFragment();

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        String[] titles = getResources().getStringArray(R.array.tab_arrays);
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), pages, titles));
        //初始化 默认显示哪个
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(onPageChangeListener);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        MobileTestApplication application = ((MobileTestApplication)getApplication());
        application.startLocation();
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            Logger.w(TAG,"initData position：" + position);
            if (position == 1) {
                if (testResultFragment != null) {
                    Logger.w(TAG, "initData testResultFragment：" + position);
                    Logger.w(TAG, "initData testResultFragment：" + position);
                    testResultFragment.refreshData();

                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void initFragment() {
        oneKeyTestFragment = new OneKeyTestFragment();
        testResultFragment = new TestResultFragment();
        testStatFragment = new TestStatFragment();
//        ReportFragment reportFragment = new ReportFragment();

        pages.add(oneKeyTestFragment);
        pages.add(testResultFragment);
        pages.add(testStatFragment);
//        pages.add(reportFragment);
    }


    private void initDrawer(Bundle savedInstanceState) {
        //Create the custome drawer
        View customView = LayoutInflater.from(this).inflate(R.layout.layout_drawer_custom_view, null);

        RelativeLayout sysInfo = (RelativeLayout) customView.findViewById(R.id.sys_info_layout);
        RelativeLayout shareLayout = (RelativeLayout) customView.findViewById(R.id.share_layout);

        sysInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MobileInfoActivity.class));
            }
        });
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMessage("你点击我了");
            }
        });


        result = new DrawerBuilder(this)
                //this layout have to contain child layouts
//                .withAccountHeader(headerResult)
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCustomView(customView)
                // add the items we want to use with our Drawer
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        Logger.d(TAG, "position : " + position);
//                        int id = drawerItem.getIdentifier();
//
//                        if (position == 1) {
//                            return true;
//                        } else if (position == 2) {
//                            toNewActivity(ReportActivity.class);
//                            return true;
//                        } else if (position == 3) {
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //if you have many different types of DrawerItems you can magically pre-cache those items
        // to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first
        // clear the cache to make sure no old elements are in
//        RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the cardview_item_talent with the identifier 11
            if (result != null) {
                result.setSelection(21, false);
            }

            //set the active profile
        }
//        if (result != null) {
//            result.updateBadge(4, new StringHolder(10 + ""));
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        if (result != null) {
            outState = result.saveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar cardview_item_talent clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 00) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the
        // activity
        if (result != null && result.isDrawerOpen()) {
            Logger.d(TAG, "drawer is open now closed");
            result.closeDrawer();
        } else {
            super.onBackPressed();
            finish();
            Logger.d(TAG,"finish activity");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHelper.getInstance().close();
        MobileTestApplication application = ((MobileTestApplication)getApplication());
        application.stopLocation();
    }
}
