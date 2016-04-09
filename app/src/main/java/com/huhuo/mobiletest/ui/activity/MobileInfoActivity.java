package com.huhuo.mobiletest.ui.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.View;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.MobileInfoAdapter;
import com.huhuo.mobiletest.constants.StaticValue;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.NetWorkUtil;
import com.huhuo.mobiletest.utils.SimCardUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.imid.swipebacklayout.lib.ViewDragHelper;

@ContentView(R.layout.activity_mobile_info)
public class MobileInfoActivity extends BaseActivity {

    private static final String TAG = MobileInfoActivity.class.getSimpleName();

    @ViewInject(R.id.rv_mobile_info)
    private RecyclerView recyclerView;

    private MobileInfoAdapter adapter;

    private ArrayList<Map<String,String>> infoList = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        initData();

    }

    private void initData() {
        //手机品牌
        String brandName = Build.MANUFACTURER;
        Map<String,String> brandMap = new HashMap<>();
        brandMap.put(getString(R.string.test_mobile_brand),brandName);
        infoList.add(brandMap);

        //手机型号
        String modelName = Build.MODEL;
        Map<String,String> modelMap = new HashMap<>();
        modelMap.put(getString(R.string.test_mobile_model),modelName);
        infoList.add(modelMap);

        //手机系统版本
        String versionRelase = Build.VERSION.RELEASE;
        Map<String,String> versionMap = new HashMap<>();
        versionMap.put(getString(R.string.test_mobile_version), versionRelase);
        infoList.add(versionMap);

        //手机识别码
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        Map<String,String> deviceIdMap = new HashMap<>();
        deviceIdMap.put(getString(R.string.test_mobile_device_id), deviceId);
        infoList.add(deviceIdMap);

        //当前网络
        String netType = NetWorkUtil.getCurrentNetWorkTypeName();
        Map<String,String> currentMobileNetMap = new HashMap<>();
        currentMobileNetMap.put(getString(R.string.test_mobile_net_type),netType);
        infoList.add(currentMobileNetMap);

        //运营商
        final String simType = SimCardUtil.getSimType();
        Map<String,String> operatorsMap = new HashMap<>();
        operatorsMap.put(getString(R.string.test_mobile_operators),simType);
        infoList.add(operatorsMap);

        //信号强度
        Map<String,String> signalsMap = new HashMap<>();
        signalsMap.put(getString(R.string.test_mobile_signals), StaticValue.SIGNALS + "dbm");
        infoList.add(signalsMap);

        //LAC
        Map<String,String> lacMap = new HashMap<>();
        lacMap.put(getString(R.string.test_mobile_lac), StaticValue.LAC + "");
        infoList.add(lacMap);
        //CI
        Map<String,String> ciMap = new HashMap<>();
        ciMap.put(getString(R.string.test_mobile_ci), StaticValue.CI + "");
        infoList.add(ciMap);

        //手机总内存
        String[] totalMemory = getTotalMemory();
        if (totalMemory.length > 0) {
            String totalMem = totalMemory[0];
            Map<String,String> totalMemMap = new HashMap<>();
            totalMemMap.put(getString(R.string.test_mobile_total_mem),totalMem);
            infoList.add(totalMemMap);
        }

        //手机可用内存
        if (totalMemory.length > 0) {
            String avaiMem = totalMemory[1];
            Map<String,String> availMemMap = new HashMap<>();
            availMemMap.put(getString(R.string.test_mobile_avail_mem),avaiMem);
            infoList.add(availMemMap);
        }

        adapter = new MobileInfoAdapter(infoList);
        recyclerView.setAdapter(adapter);

        Logger.v(TAG, "infoList:" + infoList.toString());




    }

    private String[] getTotalMemory() {
        long start = System.currentTimeMillis();
        String[] result = {"",""};  //1-total 2-avail
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();

        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.getMemoryInfo(mi);

        long mTotalMem = 0;
        long mAvailMem = mi.availMem;
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            mTotalMem = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result[0] = Formatter.formatFileSize(this, mTotalMem);
        result[1] = Formatter.formatFileSize(this, mAvailMem);
        Logger.i(TAG, "meminfo total:" + result[0] + " used:" + result[1]);

        long end = System.currentTimeMillis();
        Logger.w(TAG,"获取系统可用内存耗时：" + (end - start));

        return result;
    }


}
