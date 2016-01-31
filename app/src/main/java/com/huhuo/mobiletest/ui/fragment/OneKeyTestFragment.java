package com.huhuo.mobiletest.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.ui.activity.ConnectActivity;
import com.huhuo.mobiletest.ui.activity.NetSpeedTestActivity;
import com.huhuo.mobiletest.ui.activity.TestActivity;
import com.huhuo.mobiletest.ui.activity.VideoTestActivity;
import com.huhuo.mobiletest.ui.activity.VoiceTestActivity;
import com.huhuo.mobiletest.ui.activity.WebPageTestActivity;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.NetStateUtils;
import com.huhuo.mobiletest.utils.NetWorkUtil;
import com.huhuo.mobiletest.utils.SimCardUtil;
import com.huhuo.mobiletest.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.fragment_one_key_test)
public class OneKeyTestFragment extends BaseFragment {

    private static final String TAG = OneKeyTestFragment.class.getSimpleName();

    @ViewInject(R.id.tv_info)
    private TextView tvInfo;

    private TelephonyManager Tel;
    private MyPhoneStateListener    MyListener;
    private TelephonyManager telephoneManager;
    private int signal;
    private String signalLevel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initPhoneInfo();

        /* Update the listener, and start it */
        MyListener   = new MyPhoneStateListener();
        Tel       = ( TelephonyManager )context.getSystemService(Context.TELEPHONY_SERVICE);
        Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        telephoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    private void initPhoneInfo() {
        StringBuilder sb = new StringBuilder();
        final boolean netOk = NetStateUtils.isNetOk(getActivity());
        final boolean canUseSim = SimCardUtil.isCanUseSim();
        if (canUseSim) {
            if (netOk) {
                final String simType = SimCardUtil.getSimType();
                sb.append("信号强度：" + signal + "dBm " + "\n");
                //网络已连接，并且是移动网络，显示移动网络类型
                if (NetWorkUtil.isMobileAvailable()) {
                    String netType = NetWorkUtil.getCurrentNetWorkTypeName();
                    sb.append(simType + "：" + netType + "\n");
                    sb.append("网络类型：移动网络" + "\n");
                } else {//网络已连接，不是移动网络，必然是WIFI
                    sb.append(simType + "：" + "2G" + "\n");
                    sb.append("网络类型：WiFi" + "\n");
                }
            }
        } else {
            sb.append("SIM卡不可用" + "\n");
            if (netOk) {
                sb.append("网络类型：WiFi" + "\n");
            } else {
                sb.append("无可用网络\n");
            }
        }

        tvInfo.setText(sb.toString());
    }

    @Event(value = R.id.btn_speed_test)
    private void speedTestClick(View view) {
        startActivity(new Intent(context, NetSpeedTestActivity.class));
    }

    @Event(value = R.id.btn_webpage_test)
    private void webPageTestClick(View view) {
        startActivity(new Intent(context,WebPageTestActivity.class));
    }

    @Event(value = R.id.btn_connect)
    private void connectTestClick(View view) {
        startActivity(new Intent(context,ConnectActivity.class));
    }

    @Event(value = R.id.btn_video_test)
    private void videoTestClick(View view) {
        startActivity(new Intent(context,VideoTestActivity.class));
    }

    @Event(value = R.id.btn_voice_test)
    private void voiceTestClick(View view) {
        startActivity(new Intent(context,VoiceTestActivity.class));
    }
    @Event(value = R.id.synthesize_test)
    private void voiceSynthesizeClick(View view) {
        ToastUtil.showShortToast("尚未开发完毕");
    }

    /* —————————– */
    /* Start the PhoneState listener */
   /* —————————– */
    private class MyPhoneStateListener extends PhoneStateListener {
        /* Get the Signal strength from the provider, each tiome there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength)
        {
            super.onSignalStrengthsChanged(signalStrength);
            Logger.d(TAG, "Go to Firstdroid!!! GSM Cinr = "
                    + String.valueOf(signalStrength.getGsmSignalStrength()));
            int dBm = 0;
            if (!signalStrength.isGsm()) {
                dBm = signalStrength.getCdmaDbm();
                Logger.d(TAG,"cdma信号：" + dBm);
            } else {
                int asu = signalStrength.getGsmSignalStrength();
                dBm = (-113 + 2 * asu);
                Logger.d(TAG,"gsm信号：" + dBm);
            }

            signal = dBm;

            signalLevel = NetWorkUtil.getSignalLevel(signalStrength.getGsmSignalStrength());
            initPhoneInfo();
        }

    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
