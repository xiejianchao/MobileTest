package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.constants.Constants;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.model.VideoInfo;
import com.huhuo.mobiletest.utils.DisplayUtil;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.TrafficUtil;
import com.huhuo.mobiletest.video.util.VideoUtil;
import com.huhuo.mobiletest.video.VideoPlayer;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_video_test)
public class VideoTestActivity extends BaseActivity {

    private static final String TAG = VideoTestActivity.class.getSimpleName();

    @ViewInject(R.id.surfaceView1)
    private SurfaceView surfaceView;

    @ViewInject(R.id.tv_speed_info)
    private TextView tvSpeedInfo;

    @ViewInject(R.id.tv_speed)
    private TextView tvSpeed;

    @ViewInject(R.id.layout_suface)
    private RelativeLayout surfaceLayout;

    @ViewInject(R.id.tv_stop_seekbar)
    private TextView tvView;

    @ViewInject(R.id.tv_video_test_result)
    private TextView tvVideoTestResult;

    @ViewInject(R.id.skbProgress)
    private SeekBar skbProgress;

    @ViewInject(R.id.btn_stop_test)
    private Button btnStop;

    private VideoPlayer player;

    private static final int ADJUST_PARAMS = 1;
    private static final int TRAFFIC_UPDATE = 2;

    private Timer timer;
    private long newTraffic;
    private long oldTraffic;

    private DecimalFormat df;
    private boolean START_TEST = true;
    private static boolean START_TIMER = true;

    private ArrayList<Long> speedList = new ArrayList<Long>();

    @Override
    protected void init(Bundle savedInstanceState) {
        final long start = System.currentTimeMillis();
        updateHeight();
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player = new VideoPlayer(surfaceView, skbProgress);
        player.setOnBufferingCompletion(onBufferingCompletion);

        df = new DecimalFormat("#.##");

        timer = new Timer();
        oldTraffic = TrafficUtil.getMyRxBytes();

        tvView.setOnClickListener(handlerClickListern);

        long end = System.currentTimeMillis();
        Logger.e(TAG, "activity初始化耗时：" + (end - start));
    }

    @Event(value = R.id.btn_stop_test)
    private void stopTestClick(View view) {
        long playDelay = player.getPlayDelay();
        int bufferingCount = player.getBufferingCount();

        if (START_TEST) {
            tvVideoTestResult.setVisibility(View.VISIBLE);
            skbProgress.setVisibility(View.GONE);
            surfaceView.setVisibility(View.GONE);

            videoTestClosed(playDelay, bufferingCount);
            Logger.d(TAG,"结束视频播放测试...");
            START_TEST = false;
        } else {
            btnStop.setText("停止测试");
            tvVideoTestResult.setVisibility(View.GONE);
            skbProgress.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.VISIBLE);

            startPlayOnlineVideo();
            Logger.d(TAG, "开始视频播放测试...");
            START_TEST = true;
        }

    }

    private View.OnClickListener handlerClickListern = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //nothing to do
        }
    };

    private void updateHeight() {
        final long start = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final VideoInfo videoInfo = VideoUtil.get(Constants.PATH);
                final Message msg = handler.obtainMessage();
                msg.what = ADJUST_PARAMS;
                msg.obj = videoInfo;
                handler.sendMessage(msg);
                long end = System.currentTimeMillis();
                Logger.e(TAG,"调整宽高耗时：" + (end - start));
            }
        }).start();


    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }
    }

    private VideoPlayer.OnBufferingCompletion onBufferingCompletion = new VideoPlayer.OnBufferingCompletion(){

        @Override
        public void onBufferingCompletion(long bufferingTime,int bufferingCount) {
            videoTestClosed(bufferingTime, bufferingCount);
        }
    };

    private void startTimer(){
        START_TIMER = true;
    }

    private void closeTimer(){
        START_TIMER = false;
    }

    private void videoTestClosed(long bufferingTime, int bufferingCount) {
        closeTimer();
        stopPlay();

        float time = ((float) bufferingTime / 1000);
        final String formatTime = df.format(time);

        Logger.e(TAG, "首播缓冲时间："
                + (bufferingTime < 1000 ? bufferingTime : formatTime + "秒"));

        tvSpeedInfo.setText("测试结束");
        tvSpeed.setText(null);

        tvVideoTestResult.setText("视频测试\n");

        String avgSpeedStr = getAvgSpeed();
        tvVideoTestResult.append("平均速度：" + avgSpeedStr + "kbps" + "\n");
        tvVideoTestResult.append("缓冲次数：" + bufferingCount + "\n");
        tvVideoTestResult.append("首播延时：" + formatTime + "\n");

        btnStop.setText("重新测试");


        final TestResultSummaryModel summaryModel = new TestResultSummaryModel();

        float testLevel = 0;
        if (bufferingTime < 3000) {
            testLevel = 5;
        } else if (bufferingTime > 3000 && testLevel < 5000) {
            testLevel = 3;
        } else {
            testLevel = 1;
        }

        //测试时间
        summaryModel.setTestDate(new Date());
        summaryModel.setTestLevel(testLevel);
        summaryModel.setTestType(TestCode.TEST_TYPE_VIDEO);
        summaryModel.setDelayTime(bufferingTime);
        DatabaseHelper.getInstance().testResultDao.insert(summaryModel);
    }

    private String getAvgSpeed(){
        long speed = 0;
        if (speedList.size() > 0) {
            for (int i = 0; i < speedList.size(); i++) {
                speed += speedList.get(i);
            }
            float avgSpeed = ((float)speed / speedList.size());
            final String avgSpeedStr = df.format(avgSpeed);
            return avgSpeedStr;
        }

        return null;
    }

    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            if (START_TIMER) {
                newTraffic = TrafficUtil.getMyRxBytes();

                Message msg = handler.obtainMessage();
                msg.what = TRAFFIC_UPDATE;
                msg.obj = newTraffic - oldTraffic;
                handler.sendMessage(msg);

                Logger.v(TAG, "old_kbp：" + oldTraffic + "，new_kbp:" + newTraffic);
                Logger.v(TAG, "本次新获取的流量：" + (newTraffic - oldTraffic) + "kbp");
                oldTraffic = newTraffic;
            } else {
//                Logger.v(TAG,"现在暂停获取流量...");
            }

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case TRAFFIC_UPDATE :
                    Long s_KB =(Long) msg.obj;
                    Logger.d(TAG, "newTraffic：" + "++++++++++++++++++++++++++++" + s_KB);
                    Logger.d(TAG, "视频加载中(" + s_KB + "kbp/S)...");
                    tvSpeedInfo.setText(R.string.common_video_test_start);
                    speedList.add(s_KB);
                    tvSpeed.setText(s_KB + "kbps");
                    break;
                case ADJUST_PARAMS:
                    VideoInfo videoInfo = (VideoInfo) msg.obj;
                    adjuestHeightAndStartText(videoInfo);
                    break;
            }
        }
    };

    private void adjuestHeightAndStartText(VideoInfo videoInfo) {
        if (videoInfo != null) {
            final int width = videoInfo.getWidth();
            final int height = videoInfo.getHeight();

            final int sw = DisplayUtil.getScreenWidth(context);

            float rate =  (float)sw / width;
            int adjustingHeight = (int) (height * rate);
            Logger.d(TAG, "调整后的宽：" + sw + ",调整后的高：" + adjustingHeight);

            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(sw,
                    adjustingHeight);
            params.addRule(RelativeLayout.BELOW, R.id.test_info_layout);
            surfaceLayout.setLayoutParams(params);
            surfaceView.setBackgroundColor(getResources().getColor(R.color.transparent));

            timer.schedule(task, 1000, 1000);
            startPlayOnlineVideo();
        }
    }

    private void startPlayOnlineVideo(){
        player.playUrl(Constants.PATH2);
        startTimer();
    }

    private void cancelTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void cancelTask(){
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryPlayer();
        cancelTimer();
        cancelTask();
    }

    private void stopPlay(){
        if (player != null) {
            player.stop();
        }
    }

    private void destoryPlayer(){
        if (player != null) {
            player.destory();
        }
    }


}
