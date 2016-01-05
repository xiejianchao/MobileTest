package com.huhuo.mobiletest.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.ui.activity.NetSpeedTestActivity;
import com.huhuo.mobiletest.ui.activity.TestActivity;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StringBuilder sb = new StringBuilder();
        final boolean canUseSim = SimCardUtil.isCanUseSim();
        sb.append(canUseSim ? "SIM卡可用\n" : "SIM卡不可用" + "\n");
        final String simType = SimCardUtil.getSimType();
        sb.append("运营商：" + simType + "\n");

        final boolean netOk = NetStateUtils.isNetOk(context);
        sb.append(netOk ? "有可用网络\n" : "无可用网络\n");

//        if (NetStateUtils.isMobileNetworkEnable()) {
        if (NetWorkUtil.isMobileAvailable()) {
            final String mobileNetType = NetStateUtils.getMobileNetType();
//            sb.append("移动网络可用，移动网络类型为：" + mobileNetType + "\n");
            sb.append("移动网络可用，移动网络类型为：" + NetWorkUtil.getCurrentNetworkType() + "\n");
        } else {
            ToastUtil.showShortToast("当前连接的网络不是移动网络");
        }

        tvInfo.setText(sb.toString());
    }

    @Event(value = R.id.btn_speed_test)
    private void speedTestClick(View view) {
        startActivity(new Intent(context,NetSpeedTestActivity.class));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
