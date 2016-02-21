package com.huhuo.mobiletest.video.util;

import android.util.Log;

import com.huhuo.mobiletest.model.VideoInfo;
import com.huhuo.mobiletest.utils.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by xiejc on 16/1/31.
 */
public class VideoUtil {

    private static final String TAG = VideoUtil.class.getSimpleName();

    public static VideoInfo get(String path) {
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        try {
            if (path != null) {
                HashMap<String, String> headers = null;
                if (headers == null) {
                    headers = new HashMap<String, String>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                }
                mmr.setDataSource(path, headers);
            }

            String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
            String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);

            final VideoInfo info = new VideoInfo();
            info.setDuration(Long.valueOf(duration));
            info.setWidth(Integer.valueOf(width));
            info.setHeight(Integer.valueOf(height));

            Log.v(TAG, "playtime:" + duration + "w=" + width + "h=" + height);
            return info;
        } catch (Exception ex) {
            Log.e(TAG, "MediaMetadataRetriever exception " + ex);
        } finally {
            mmr.release();
        }
        return null;
    }

    private OnVideoSizeListener listener;

    public interface OnVideoSizeListener {
        void onSize(int size);
    }

    public static void getVideoSize(final String videoUrl, final OnVideoSizeListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Logger.v(TAG, "现在开始计算在线视频大小...");
                    URL url = new URL(videoUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10 * 1000);
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        int videoSize = conn.getContentLength();
                        if (listener != null) {
                            listener.onSize(videoSize);
                        }
                        float size = (float) videoSize / 1024;
                        Logger.d(TAG, "状态正常，在线播放视频大小为：" + size + "kb");
                    } else {
                        int length = conn.getContentLength();
                        Logger.d(TAG, "服务器返回状态不正常，code:，" + code + ",在线播放视频大小为：" + length);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Logger.v(TAG, "计算在线视频大小结束");
            }
        }).start();

    }

}
