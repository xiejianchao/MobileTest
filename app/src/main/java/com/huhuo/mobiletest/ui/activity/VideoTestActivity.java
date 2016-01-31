package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.constants.Constants;
import com.huhuo.mobiletest.model.VideoInfo;
import com.huhuo.mobiletest.utils.DisplayUtil;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.TrafficUtil;
import com.huhuo.mobiletest.video.util.VideoUtil;
import com.huhuo.mobiletest.video.VideoPlayer;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;
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

    @ViewInject(R.id.view)
    private TextView tvView;

    @ViewInject(R.id.tv_video_test_result)
    private TextView tvVideoTestResult;

    @ViewInject(R.id.skbProgress)
    private SeekBar skbProgress;

    private VideoPlayer player;

    private static final int ADJUST_PARAMS = 1;
    private static final int TRAFFIC_UPDATE = 2;

    private Timer timer;
    private long newTraffic;
    private long oldTraffic;

    private ArrayList<Long> speedList = new ArrayList<Long>();

    @Override
    protected void init(Bundle savedInstanceState) {
        final long start = System.currentTimeMillis();
        updateHeight();
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player = new VideoPlayer(surfaceView, skbProgress);
        player.setOnBufferingCompletion(onBufferingCompletion);

        timer = new Timer();
        oldTraffic = TrafficUtil.getMyRxBytes();

        tvView.setOnClickListener(handlerClickListern);

        long end = System.currentTimeMillis();
        Logger.e(TAG, "activity初始化耗时：" + (end - start));
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
            cancelTimer();
            stopPlay();
            DecimalFormat df = new DecimalFormat("#.##");
            float time = ((float) bufferingTime / 1000);
            final String formatTime = df.format(time);

            Logger.e(TAG, "首播缓冲时间："
                    + (bufferingTime < 1000 ? bufferingTime : formatTime + "秒"));

            tvSpeedInfo.setText("测试结束.");
            tvSpeed.setText(null);

            skbProgress.setVisibility(View.GONE);
            surfaceView.setVisibility(View.GONE);
            tvVideoTestResult.setVisibility(View.VISIBLE);
            tvVideoTestResult.setText("视频测试\n");
            long speed = 0;
            if (speedList.size() > 0) {
                for (int i = 0; i < speedList.size(); i++) {
                    speed += speedList.get(i);
                }
                float avgSpeed = ((float)speed / speedList.size());
                final String avgSpeedStr = df.format(avgSpeed);
                tvVideoTestResult.append("平均速度：" + avgSpeedStr + "kbps" + "\n");
            }

            tvVideoTestResult.append("缓冲次数：" + bufferingCount + "\n");
            tvVideoTestResult.append("首播延时：" + formatTime + "\n");


        }
    };

    private void cancelTimer(){
        if (timer != null) {
            task.cancel();
            task = null;
            timer.cancel();
            timer = null;
        }
    }

    TimerTask task = new TimerTask() {

        @Override
        public void run() {
//            newTraffic = TrafficUtil.getUidRxBytes() - oldTraffic;
            newTraffic = TrafficUtil.getMyRxBytes();

            Message msg = handler.obtainMessage();
            msg.what = TRAFFIC_UPDATE;
            msg.obj = newTraffic - oldTraffic;
            handler.sendMessage(msg);

            Log.v(TAG, "old_kbp：" + oldTraffic + "，new_kbp:" + newTraffic);
            Log.v(TAG, "本次新获取的流量：" + (newTraffic - oldTraffic) + "kbp");
            oldTraffic = newTraffic;
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case TRAFFIC_UPDATE :
                    Long s_KB =(Long) msg.obj;
                    Log.d(TAG, "newTraffic：" + "++++++++++++++++++++++++++++" + s_KB);
                    Log.d(TAG, "视频加载中(" + s_KB + "kbp/S)...");
                    tvSpeedInfo.setText(R.string.common_video_test_start);
                    speedList.add(s_KB);
                    tvSpeed.setText(s_KB + "kbps");
                    break;
                case ADJUST_PARAMS:
                    VideoInfo videoInfo = (VideoInfo) msg.obj;
                    adjuestHeight(videoInfo);
                    break;
            }
        }
    };

    private void adjuestHeight(VideoInfo videoInfo) {
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

            player.playUrl(Constants.PATH2);
            timer.schedule(task, 1000, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
        cancelTimer();
    }

    private void stopPlay(){
        if (player != null) {
            player.stop();
        }
    }


}
