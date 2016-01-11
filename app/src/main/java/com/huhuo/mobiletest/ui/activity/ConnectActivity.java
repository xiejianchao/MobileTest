package com.huhuo.mobiletest.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.utils.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@ContentView(R.layout.activity_connect)
public class ConnectActivity extends BaseActivity {


    private static final String TAG = ConnectActivity.class.getSimpleName();

    @Override
    protected void init(Bundle savedInstanceState) {



    }

    @Event(value = R.id.btn_test_connect)
    private void connectTestClick(View view) {
        try {
//            Runtime runtime = Runtime.getRuntime();
//            Process p = runtime.exec("ping www.baidu.com");

//            String ipAddress = "www.baidu.com";
//            Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + ipAddress);

//            String lost = new String();
//            String delay = new String();
//            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//            String str = new String();
//            StringBuilder sb = new StringBuilder();
//
//            //读出所有信息并显示
//            while((str=buf.readLine())!=null){
//                str = str + "\r\n";
//                sb.append(str);
//            }
//
//            Logger.d(TAG,"info:" + sb.toString());


            pingIpAddr();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static final boolean pingIpAddr() {
        String mPingIpAddrResult;
        try {

            int pingNum = 5;
            String m_strForNetAddress = "www.baidu.com";
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c "+ pingNum + " " + m_strForNetAddress);

            int status = p.waitFor();
            String result = null;
            if (status == 0) {
                result="success";
            }
            else
            {
                result="failed";
            }

            String lost = new String();
            String delay = new String();
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String str = new String();
            StringBuilder sb = new StringBuilder();

            //读出所有信息并显示
            while((str=buf.readLine())!=null){
                str = str + "\r\n";
                sb.append(str);
            }

            Logger.d(TAG,"info:" + sb.toString());

        } catch (IOException e) {
            mPingIpAddrResult = "Fail: IOException";
        } catch (Exception e) {
            mPingIpAddrResult = "Fail: InterruptedException";
        }
        return false;
    }

}
