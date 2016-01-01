package com.huhuo.mobiletest.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.utils.NetStateUtils;
import com.huhuo.mobiletest.utils.NetWorkUtil;
import com.huhuo.mobiletest.utils.SimCardUtil;
import com.huhuo.mobiletest.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


@ContentView(R.layout.fragment_one_key_test)
public class OneKeyTestFragment extends BaseFragment {


    @ViewInject(R.id.tv_info)
    private TextView tvInfo;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StringBuilder sb = new StringBuilder();
        final boolean canUseSim = SimCardUtil.isCanUseSim();
        sb.append(canUseSim ? "SIM卡可用" : "SIM卡不可用" + "\n");
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
