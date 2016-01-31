package com.huhuo.mobiletest.video;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

import com.huhuo.mobiletest.utils.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPlayer implements OnBufferingUpdateListener,
        OnCompletionListener, MediaPlayer.OnPreparedListener,
        SurfaceHolder.Callback, MediaPlayer.OnInfoListener {

    private static final String TAG = VideoPlayer.class.getSimpleName();

    private int videoWidth;
    private int videoHeight;
    public MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SeekBar skbProgress;
    private String videoUrl = null;
    private boolean notifyStartPlay = false;

    private long start = 0;
    private long playDelay = 0;

    private Timer mTimer = new Timer();

    public VideoPlayer(SurfaceView surfaceView, SeekBar skbProgress) {
        this.skbProgress = skbProgress;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    /*******************************************************
     * 通过定时器和Handler来更新进度条
     ******************************************************/
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            if (duration > 0) {
                long pos = skbProgress.getMax() * position / duration;
                skbProgress.setProgress((int) pos);
            }
        }

        ;
    };
    //*****************************************************

    public void play() {
        mediaPlayer.start();
    }



    public void playUrl(String videoUrl) {
        start = System.currentTimeMillis();
        this.videoUrl = videoUrl;
        //如果要求播放时，孩mediaplayer尚未创建，那就等surfaceCreated回调执行后再播放
        if (mediaPlayer == null) {
            //等待SurfaceHolder.Callback回调执行后再播放
            notifyStartPlay = true;
        } else {
            initPlay();
        }
    }

    private void initPlay(){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();//prepare之后自动播放
            //mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.e(TAG, "surface changed");
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        try {
            initMediaPlayer();
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
        Log.e(TAG, "surface created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.e(TAG, "surface destroyed");
    }

    private void initMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDisplay(surfaceHolder);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnInfoListener(this);

        if (notifyStartPlay) {
            initPlay();
        }
    }

    int bufferingCount = 0;
    /**
     * 通过onPrepared播放
     */
    @Override
    public void onPrepared(MediaPlayer arg0) {
        videoWidth = mediaPlayer.getVideoWidth();
        videoHeight = mediaPlayer.getVideoHeight();
        if (videoHeight != 0 && videoWidth != 0) {
            arg0.start();
        }
        bufferingCount ++;
        long end = System.currentTimeMillis();
        playDelay = (end - start);
        Logger.w(TAG,"videoWidth：" + videoWidth + ",videoHeight:" + videoHeight);
        Logger.e(TAG, "onPrepared 播放延迟：" + playDelay);
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        // TODO Auto-generated method stub

    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        skbProgress.setSecondaryProgress(bufferingProgress);
        int currentProgress = skbProgress.getMax() * mediaPlayer.getCurrentPosition() /
                mediaPlayer.getDuration();
        Logger.e(TAG,"时间：" + sdf.format(new Date()) + "," + currentProgress + "% play" +
				bufferingProgress + "% buffer");
        if (this.onBufferingCompletion != null && bufferingProgress == 100) {

            onBufferingCompletion.onBufferingCompletion(playDelay,bufferingCount);
        }


    }

    public interface OnBufferingCompletion {
        void onBufferingCompletion(long bufferingTime,int bufferingCount);
    }

    private OnBufferingCompletion onBufferingCompletion;

    public void setOnBufferingCompletion(OnBufferingCompletion listener) {
        this.onBufferingCompletion = listener;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                //开始缓存，暂停播放
                Logger.w(TAG, "开始缓冲" + extra);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                //缓存完成，继续播放
                Logger.w(TAG, "缓冲完成 extra:" + extra);
                break;
//			case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//				//显示 下载速度
//				Log.w(TAG, "download rate:" + extra);
//				break;
        }
        return true;
    }
}
