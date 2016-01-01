package com.huhuo.mobiletest.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.huhuo.mobiletest.MobileTestApplication;

/**
 * Created by xiejianchao on 15/10/22.
 */
public class ToastUtil {

    private static final String TAG = ToastUtil.class.getSimpleName();
    private static Toast toast = null;

    private static Handler mHandler = null;


    private static Handler handler = new Handler(Looper.getMainLooper());

    public static int mDuration = Toast.LENGTH_LONG;

    public static String mMsg = null;

    private static Context context;

    private static Object synObj = new Object();

    public static void showShortToast(int resID) {
        showMessage(resID, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(String msg) {

        showMessage(msg, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(int resID) {
        showMessage(resID, Toast.LENGTH_LONG);
    }

    public static void showLongToast(String msg) {
        showMessage(msg, Toast.LENGTH_LONG);
    }


    /**
     * 默认短时间显示吐司
     * @param msg
     */
    public static void showMessage(final String msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 根据设置的文本显示
     * @param msg
     */
    public static void showMessage(final int msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }


    /**
     * 显示一个文本并且设置时长
     * @param msg
     * @param len
     */
    public static void showMessage(final CharSequence msg, final int len) {
        if (msg == null || msg.equals("")) {
            Logger.w(TAG,"[ToastUtil] response message is null.");
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) { //加上同步是为了每个toast只要有机会显示出来
                    if (toast != null) {
                        //toast.cancel();
                        toast.setText(msg);
                        toast.setDuration(len);
                    } else {
                        toast = Toast.makeText(MobileTestApplication.getInstance().getApplicationContext
                                (), msg, len);
                    }
                    toast.show();
                }
            }
        });
    }

    /**
     * 资源文件方式显示文本
     * @param msg
     * @param len
     */
    public static void showMessage(final int msg, final int len) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) {
                    if (toast != null) {
                        //toast.cancel();
                        toast.setText(msg);
                        toast.setDuration(len);
                    } else {
                        toast = Toast.makeText(MobileTestApplication.getInstance().getApplicationContext
                                (), msg, len);
                    }
                    toast.show();
                }
            }
        });
    }

}
