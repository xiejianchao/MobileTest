package com.huhuo.mobiletest.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import com.huhuo.mobiletest.MobileTestApplication;

/**
 * Created by xiejc on 16/1/31.
 */
public class TrafficUtil {

    private static final String TAG = TrafficUtil.class.getSimpleName();

    /**
     * 获得当前应用的流量，通过计算上一秒和下一秒的流量差，得到当前应用的实时流量信息
     * @return
     */
    public static long getMyRxBytes(){
        //获取总的接受字节数，包含Mobile和WiFi等
        PackageManager pm = MobileTestApplication.getInstance().getPackageManager();
        ApplicationInfo ai = null;
        try {
            String pkgName = MobileTestApplication.getInstance().getPackageName();
            ai = pm.getApplicationInfo(pkgName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return TrafficStats.getUidRxBytes(ai.uid)==TrafficStats.UNSUPPORTED ? 0:(TrafficStats.getTotalRxBytes());
    }

}
