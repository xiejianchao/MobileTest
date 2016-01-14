package com.huhuo.mobiletest.ui.activity;

import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.PingTestAdapter;
import com.huhuo.mobiletest.model.CommonTestModel;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.view.DividerItemDecoration;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

@ContentView(R.layout.activity_connect)
public class ConnectActivity extends BaseActivity {

    private static final String TAG = ConnectActivity.class.getSimpleName();


    @ViewInject(R.id.rv_ping)
    private RecyclerView recyclerView;




    @ViewInject(R.id.tv_result)
    private TextView tvResult;

    @ViewInject(R.id.et_addr)
    private EditText etAddr;

    private StringBuffer sb = new StringBuffer();

    public final static int PING_COUNT = 10;
    public final static int PING_TIMEOUT = 10;

    @Override
    protected void init(Bundle savedInstanceState) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        ArrayList<CommonTestModel> list = new ArrayList<CommonTestModel>();
        for (int i = 0; i < 10; i ++) {
            final CommonTestModel model = new CommonTestModel();
            model.setName("百度" + i);
            list.add(model);
        }
        final PingTestAdapter adapter = new PingTestAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    Handler mHandler = new Handler(){
        public void dispatchMessage(Message msg) {
            Bundle bundle = msg.getData();
            Double min = bundle.getDouble("min");
            Double max = bundle.getDouble("max");
            Double avg = bundle.getDouble("avg");
            int send = bundle.getInt("send");
            int loss = bundle.getInt("loss");

            sb.append("数据包：已发送 = ").append(send)
                    .append("，已接收 = ").append(send - loss)
                    .append("，丢失 = ").append(loss).append("\n")
                    .append("往返行程的估计时间<以毫秒为单位>：\n")
                    .append("最短 = ").append(min).append("ms")
                    .append("，最长 = ").append(max).append("ms")
                    .append("，平均 = ").append(avg).append("ms")
                    .append("\n\n\n");
            tvResult.setText(sb.toString());
        }
    };

    @Event(value = R.id.btn_test_connect)
    private void connectTestClick(View view) {
        Logger.d(TAG, "PING CLICK");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = etAddr.getText().toString();
                Logger.d(TAG,"PING JAVA url:" + url);
                ping(url);
            }
        }).start();

    }

    private void ping(String address){
        long pingStartTime = 0;
        long pingEndTime = 0;
        ArrayList<Double> rrts = new ArrayList<Double>();

        try {
            HttpClient client = AndroidHttpClient.newInstance("Linux; Android");
            HttpHead headMethod = new HttpHead("http://" + address);
            headMethod.addHeader(new BasicHeader("Connection", "close"));
            headMethod.setParams(new BasicHttpParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 1000));
            int timeOut = (int) (1000 * (double) PING_TIMEOUT / PING_COUNT);
            HttpConnectionParams.setConnectionTimeout(headMethod.getParams(), timeOut);

            for (int i = 0; i < PING_COUNT; i++) {
                pingStartTime = System.currentTimeMillis();
                HttpResponse response = client.execute(headMethod);
                pingEndTime = System.currentTimeMillis();
                rrts.add((double) (pingEndTime - pingStartTime));
            }
            int packetLoss = PING_COUNT - rrts.size();
            disposeResult(packetLoss, rrts);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            pingJava(address);
        }
    }

    private void pingJava(String address) {
        long pingStartTime = 0;
        long pingEndTime = 0;
        ArrayList<Double> rrts = new ArrayList<Double>();

        try {
            int timeOut = (int) (1000 * (double) PING_TIMEOUT / PING_COUNT);
            for (int i = 0; i < PING_COUNT; i++) {
                Logger.d(TAG,"InetAddress exec");
                pingStartTime = System.currentTimeMillis();
                boolean status;
                status = InetAddress.getByName(address).isReachable(timeOut * 5);
                pingEndTime = System.currentTimeMillis();
                long rrtVal = pingEndTime - pingStartTime;

                Logger.d(TAG,"InetAddress exec end time:" + rrtVal + "，status：" + status);
                if (status) {
                    rrts.add((double) rrtVal);
                }
            }
            int packetLoss = PING_COUNT - rrts.size();
            disposeResult(packetLoss, rrts);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disposeResult(int packetLoss, ArrayList<Double> rrts){
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double avg;
        double total = 0;
        if (rrts.size() == 0) {
            return;
        }
        for (double rrt : rrts) {
            if (rrt < min) {
                min = rrt;
            }
            if (rrt > max) {
                max = rrt;
            }
            total += rrt;
        }
        avg = total / rrts.size();
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putDouble("min", min);
        bundle.putDouble("max", max);
        bundle.putDouble("avg", avg);
        bundle.putInt("send", PING_COUNT);
        bundle.putInt("loss", packetLoss);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }


}
