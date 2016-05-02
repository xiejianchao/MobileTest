package com.huhuo.mobiletest.video;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
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
        SurfaceHolder.Callback {

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
        if (surfaceView != null) {
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
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
            if (mediaPlayer.isPlaying() && skbProgress != null && skbProgress.isPressed() == false) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            if (mediaPlayer != null) {
                int position = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                if (duration > 0) {
                    if (skbProgress != null) {
                        long pos = skbProgress.getMax() * position / duration;
                        skbProgress.setProgress((int) pos);
                    }
                }
            }
            
        };
    };
    //*****************************************************

    public void play() {
        mediaPlayer.start();
    }



    public void playUrl(String videoUrl) {
        Logger.d(TAG,"playUrl");
        start = System.currentTimeMillis();
        bufferingCount = 0;
        this.videoUrl = videoUrl;
        //如果要求播放时，该mediaplayer尚未创建，那就等surfaceCreated回调执行后再播放
        if (mediaPlayer == null) {
            //等待SurfaceHolder.Callback回调执行后再播放
            notifyStartPlay = true;
            initMediaPlayer();
        } else {
            initPlay();
        }
    }
    private boolean mute = false;
    public void setMute(boolean isMute) {
        this.mute = isMute;
    }

    private void initPlay(){
        Logger.d(TAG,"initPlay");
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            if (mute) {
                mediaPlayer.setVolume(0,0);
            }
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
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void destory(){
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Logger.d(TAG, "surface changed");
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        try {
            initMediaPlayer();
            Logger.d(TAG,"surfaceCreated");
        } catch (Exception e) {
            Logger.d(TAG, "error", e);
        }
        Logger.d(TAG, "surface created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Logger.d(TAG, "surface destroyed");
    }

    private void initMediaPlayer(){
        Logger.d(TAG,"initMediaPlayer");
        mediaPlayer = new MediaPlayer();
        boolean creating = surfaceHolder.isCreating();
        if (creating) {
            mediaPlayer.setDisplay(surfaceHolder);
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);

        if (notifyStartPlay) {
            initPlay();
        }
    }

    int bufferingCount = 0;
    /**
     * 通过onPrepared播放
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        Logger.d(TAG,"onPrepared");
        videoWidth = this.mediaPlayer.getVideoWidth();
        videoHeight = this.mediaPlayer.getVideoHeight();
        if (videoHeight != 0 && videoWidth != 0) {
            mp.start();
        }
        bufferingCount ++;
        long end = System.currentTimeMillis();
        playDelay = (end - start);
        Logger.w(TAG,"videoWidth：" + videoWidth + ",videoHeight:" + videoHeight);
        Logger.d(TAG, "onPrepared 播放延迟：" + playDelay);
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        // TODO Auto-generated method stub

    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        if (skbProgress != null) {
            skbProgress.setSecondaryProgress(bufferingProgress);
            if (mediaPlayer.getDuration() != 0) {
                int currentProgress = skbProgress.getMax() * mediaPlayer.getCurrentPosition() /
                        mediaPlayer.getDuration();
                Logger.d(TAG,"时间：" + sdf.format(new Date()) + "," + currentProgress + "% play" +
                        bufferingProgress + "% buffer");
            }
        }
        if (this.onBufferingCompletion != null && bufferingProgress == 100) {

            onBufferingCompletion.onBufferingCompletion(playDelay, bufferingCount);
        }
    }

    public long getPlayDelay(){
        return playDelay;
    }

    public int getBufferingCount(){
        return bufferingCount;
    }

    public interface OnBufferingCompletion {
        /**
         *
         * @param bufferingTime 首次播放缓冲时间
         * @param bufferingCount 缓冲次数
         */
        void onBufferingCompletion(long bufferingTime,int bufferingCount);
    }

    private OnBufferingCompletion onBufferingCompletion;

    public void setOnBufferingCompletion(OnBufferingCompletion listener) {
        this.onBufferingCompletion = listener;
    }
}
