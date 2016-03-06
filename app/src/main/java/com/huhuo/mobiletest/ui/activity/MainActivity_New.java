package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.MyFragmentPagerAdapter;
import com.huhuo.mobiletest.ui.fragment.OneKeyTestFragment;
import com.huhuo.mobiletest.ui.fragment.ReportFragment;
import com.huhuo.mobiletest.ui.fragment.TestResultFragment;
import com.huhuo.mobiletest.ui.fragment.TestStatFragment;
import com.huhuo.mobiletest.utils.Logger;
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
//        View customView = LayoutInflater.from(this).inflate(R.layout.layout_drawer_custom_view, null);
//
//        Button btn = (Button) customView.findViewById(R.id.btn_test);
//        TextView tv = (TextView) customView.findViewById(R.id.tv_test);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"你点击我了2",Toast.LENGTH_SHORT).show();
//            }
//        });
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"你点击我了1",Toast.LENGTH_SHORT).show();
//            }
//        });


        result = new DrawerBuilder(this)
                //this layout have to contain child layouts
//                .withAccountHeader(headerResult)
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withActionBarDrawerToggleAnimated(true)
//                .withCustomView(customView)
                .addDrawerItems(
//                        new PrimaryDrawerItem().withName(R.string.plus_profile)
//                                .withIcon(GoogleMaterial.Icon.gmd_wb_sunny).withIdentifier(1)
//                                .withSelectable(false),
//                        new DividerDrawerItem(),
//                        new PrimaryDrawerItem().withName(R.string.plus_notify_set)
//                                .withIcon(FontAwesome.Icon.faw_bullhorn).withIdentifier(2)
//                                .withSelectable(false).withBadge("10")
//                                .withBadgeStyle(new BadgeStyle()
//                                        .withTextColor(Color.WHITE)
//                                        .withColorRes(R.color.md_red_700)),
                        new PrimaryDrawerItem().withName(R.string.plus_report)
                                .withIcon(GoogleMaterial.Icon.gmd_style).withIdentifier(3)
                                .withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.plus_contacts_us)
                                .withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(4)
                                .withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.plus_app_update)
                                .withIcon(FontAwesome.Icon.faw_home).withIdentifier(5)
                                .withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.plus_settings)
                                .withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(6)
                                .withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.plus_about)
                                .withIcon(FontAwesome.Icon.faw_user).withIdentifier(7)
                                .withSelectable(false)

                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Logger.d(TAG, "position : " + position);
                        int id = drawerItem.getIdentifier();

                        if (position == 1) {
                            return true;
                        } else if (position == 2) {
                            toNewActivity(ReportActivity.class);
                            return true;
                        } else if (position == 3) {
                            return true;
                        } else {
                            return false;
                        }

//                        if (id == 5 || id == 6) {
//                            return true;
//                        } else {
//                            return false;
//                        }

                    }
                })
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
            result.setSelection(21, false);

            //set the active profile
        }

        result.updateBadge(4, new StringHolder(10 + ""));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
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
}
