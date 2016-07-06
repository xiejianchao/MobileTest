package com.huhuo.mobiletest.net;

import android.util.Log;

import com.huhuo.mobiletest.utils.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiejianchao on 16/6/1.
 */
public class HttpHelper2 {

    private static final String TAG = HttpHelper2.class.getSimpleName();

    public static void sendRequest(final String url, final String strJson) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String returnLine = "";
                try {
                    Logger.w(TAG,"**************开始http通讯**************");
                    Logger.w(TAG,"**************调用的接口地址为**************" + url);
                    Logger.w(TAG,"**************请求发送的数据为**************" + strJson);

                    URL my_url = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) my_url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setConnectTimeout(1000 * 10);
                    connection.connect();
                    DataOutputStream out = new DataOutputStream(connection
                            .getOutputStream());

                    byte[] content = strJson.getBytes("utf-8");

                    out.write(content, 0, content.length);
                    out.flush();
                    out.close(); // flush and close

                    int statusCode = connection.getResponseCode();
                    if (statusCode == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                        String line = "";
                        Logger.w(TAG,"Contents of post request start");

                        while ((line = reader.readLine()) != null) {
                            // line = new String(line.getBytes(), "utf-8");
                            returnLine += line;
                            System.out.println(line);
                        }
                        Logger.w(TAG,"Contents of post request ends");
                        reader.close();
                        connection.disconnect();
                        Logger.w(TAG,"========返回的结果的为========" + returnLine);
                    } else {
                        Logger.w(TAG,"服务器返回状态码：" + statusCode);
                    }
                } catch (Exception e) {
                    Log.e(TAG,"HttpHelper2 sendRequest:" + e.toString());
                }
            }
        }).start();
    }

}
