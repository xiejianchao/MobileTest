package com.huhuo.mobiletest.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;

/**
 * Created by xiejc on 16/1/1.
 */
public class SimCardUtil {


    static final Context context = MobileTestApplication.getInstance().getApplicationContext();
    /**
     * sim卡是否可读
     * @return
     */
    public static boolean isCanUseSim() {
        try {
            TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获得sim卡的所属的运营商，调用前需要先确保SIM正常，先调用isCanUseSim()方法检查sim状态
     * @return
     */
    public static String getSimType() {
        final Context context = MobileTestApplication.getInstance().getApplicationContext();
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        /** 获取SIM卡的IMSI码
         * SIM卡唯一标识：IMSI 国际移动用户识别码（IMSI：International Mobile Subscriber Identification Number）是区别移动用户的标志，
         * 储存在SIM卡中，可用于区别移动用户的有效信息。IMSI由MCC、MNC、MSIN组成，其中MCC为移动国家号码，由3位数字组成，
         * 唯一地识别移动客户所属的国家，我国为460；MNC为网络id，由2位数字组成，
         * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。
         * 唯一地识别国内GSM移动通信网中移动客户。所以要区分是移动还是联通，只需取得SIM卡中的MNC字段即可
         */
        final String unknown = context.getString(R.string.common_unknown);
        String simType = unknown;
        String imsi = telManager.getSubscriberId();
        if(imsi!=null){
            //因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002等编号，134/159号段使用了此编号
            if(imsi.startsWith("46000")
                    || imsi.startsWith("46002")
                    || imsi.startsWith("46007")){
                //中国移动
                simType = context.getString(R.string.common_china_mobile);
            }else if(imsi.startsWith("46001")){
                simType = context.getString(R.string.common_china_unicom);
                //中国联通
            }else if(imsi.startsWith("46003")){
                //中国电信
                simType = context.getString(R.string.common_china_telecom);
            } else {

                simType = unknown;
            }
        }
        return simType;
    }

    /**
     * 检查sim卡状态
     *
     * @param
     * @return
     */
    public static boolean checkSimState() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT
                || tm.getSimState() == TelephonyManager.SIM_STATE_UNKNOWN) {
            return false;
        }

        return true;
    }

    /**
     * 获取imei
     */
    public static String getImei() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        if (imei == null) {
            imei = "000000000000000";
        }
        return imei;
    }

    public static String getPhoneImsi() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getSubscriberId();
    }

    /**
     * 获取电话号码
     */
    public static String getPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = null;
        phoneNumber = telephonyManager.getLine1Number();
        return phoneNumber;
    }

}
