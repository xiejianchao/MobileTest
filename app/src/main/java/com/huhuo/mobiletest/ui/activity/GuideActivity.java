package com.huhuo.mobiletest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.support.v4.view.ViewPager.OnPageChangeListener;


import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.GuideViewpagerAdapter;
import com.huhuo.mobiletest.ui.animation.Techniques;
import com.huhuo.mobiletest.ui.animation.YoYo;
import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;

public class GuideActivity extends Activity implements OnClickListener,
        OnPageChangeListener {

    // 定义ViewPager对象
    private ViewPager viewPager;
    // 定义ViewPager适配器
    private GuideViewpagerAdapter vpAdapter;
    // 底部小点的图片
    private ImageView[] points;
    // 记录当前选中位置
    private int currentIndex;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    // 定义各个界面View对象
    private View vpMain, vpBed, vpHouse, vpMan, vpWine;
    private Button vpSee, btnRegist, btnLogin;

    // viewpager的背景图片
    private ImageView ivOne, ivTwo;
    private int[] pics = {R.drawable.vp_guide_one, R.drawable.vp_guide_two, R.drawable.vp_guide_three, R.drawable.vp_guide_four
            , R.drawable.vp_guide_five};

    ScaleAnimation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
    }

    /**
     * 初始化组件
     */
    private void initView() {

        ivOne = (ImageView) findViewById(R.id.iv_one);
        ivOne.setBackgroundResource(R.drawable.vp_guide_one);
        // 设置ivone的背景扩放效果
        scaleOne();

        ivTwo = (ImageView) findViewById(R.id.iv_two);

        // 登录注册按钮
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegist = (Button) findViewById(R.id.btn_regist);
        btnLogin.setOnClickListener(this);
        btnRegist.setOnClickListener(this);


        LayoutInflater inflater = getLayoutInflater();
        vpMain = inflater.inflate(R.layout.layout_vp_item_main, null);
        // 加入落体动画
        initanimal(vpMain.findViewById(R.id.imageview), 300);

        vpBed = inflater.inflate(R.layout.layout_vp_item_bed, null);
        vpHouse = inflater.inflate(R.layout.layout_vp_item_house, null);
        vpMan = inflater.inflate(R.layout.layout_vp_item_man, null);
        vpWine = inflater.inflate(R.layout.layout_vp_item_wine, null);
        vpSee = (Button) vpWine.findViewById(R.id.btn_guide_see);
        vpSee.setOnClickListener(this);

        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // 实例化ArrayList对象
        views = new ArrayList<View>();

        // 实例化ViewPager适配器
        vpAdapter = new GuideViewpagerAdapter(views);
    }

    /**
     * vp的home背景扩放
     */
    private void scaleOne() {
        animation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2500);//设置动画持续时间
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        ivOne.startAnimation(animation);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 设置监听
        viewPager.setOnPageChangeListener(this);

        views.add(vpMain);
        views.add(vpBed);
        views.add(vpHouse);
        views.add(vpMan);
        views.add(vpWine);

        // 设置数据
        viewPager.setAdapter(vpAdapter); // 要先add再setadapter  不然报错  fk!!!

        // 初始化底部小点
        initPoint(views.size());
    }

    /**
     * 初始化底部小点
     */
    private void initPoint(int views) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.vp_point);

        points = new ImageView[views];

        // 循环取得小点图片
        for (int i = 0; i < views; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            points[i] = (ImageView) linearLayout.getChildAt(i);
            // 设为默认
            points[i].setEnabled(true);
            // 设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        // 设置当面默认的位置
        currentIndex = 0;
        // 设置选中状态
        points[currentIndex].setEnabled(false);
    }

    /**
     * 当前页面滑动时调用
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        ivOne.setBackgroundResource(pics[position]);
        ivOne.setAlpha(1 - positionOffset);
        Log.e("-------", "ivOne:" + ivOne.getAlpha());
        int pos = (position + 1 >= pics.length) ? pics.length - 1 : position + 1;
        ivTwo.setBackgroundResource(pics[pos]);
        ivTwo.setAlpha(positionOffset);
        Log.e("-------", "ivTwo:" + ivTwo.getAlpha());
    }

    /**
     * 新的页面被选中时调用
     */
    @Override
    public void onPageSelected(int position) {
        // 设置底部小点选中状态
        setCurDot(position);
        if (position != 0) {
            animation.cancel();
        }
    }

    /**
     * 滑动状态改变时调用
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                initPopLogin();
                break;
            case R.id.btn_regist:
                initPopRegist();
                break;
            case R.id.btn_guide_see:
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                GuideActivity.this.finish();
                break;
        }


    }


    /**
     * 弹出注册的界面
     */
    private void initPopRegist() {
        Intent intent = new Intent(GuideActivity.this, RegistActivity.class);
        startActivity(intent);
//        GuideActivity.this.finish();
    }

    /**
     * 弹出登录的界面
     */
    private void initPopLogin() {
        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
        startActivity(intent);
//        GuideActivity.this.finish();
    }


    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > views.size() - 1 || currentIndex == positon) {
            return;
        }
        points[positon].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 300:
                    vpMain.findViewById(R.id.imageview).setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void initanimal(final View view, final long delay) {
        YoYo.with(Techniques.BounCeInRight).duration(1800).delay(delay)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        handler.sendEmptyMessageDelayed((int) delay, delay + 50);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(view);
    }
}