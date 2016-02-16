package com.huhuo.mobiletest.ui.activity;

import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.adapter.PingTestAdapter;
import com.huhuo.mobiletest.constants.TestCode;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.model.CommonTestModel;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.utils.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import java.util.Date;

@ContentView(R.layout.activity_connect)
public class ConnectActivity extends BaseActivity {

    private static final String TAG = ConnectActivity.class.getSimpleName();


    @ViewInject(R.id.rv_ping)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tv_result)
    private TextView tvResult;

    @ViewInject(R.id.tv_ping_info)
    private TextView tvAllInfo;

    @ViewInject(R.id.et_addr)
    private EditText etAddr;

    private StringBuffer sb = new StringBuffer();

    private ArrayList<CommonTestModel> list = new ArrayList<CommonTestModel>();
    private PingTestAdapter adapter;

    public final static int PING_COUNT = 10;
    public final static int PING_TIMEOUT = 10;

    private long startTime;

    @Override
    protected void init(Bundle savedInstanceState) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));

        //初始化待测试数据
        initData2RecycleView();

        tvAllInfo.setText(getString(R.string.common_test_start_ping));
        //开始测试网站连接响应时间，并逐条更新到recycleview上
        startTestRefreshUI(list.get(testIndex));

    }

    private int testIndex = 0;

    private void startTestRefreshUI(final CommonTestModel model) {
        startTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ping(model.getUrl());
            }
        }).start();
    }

    private void initData2RecycleView() {

        CommonTestModel model = new CommonTestModel();

        model.setName(getString(R.string.test_website_10086));
        model.setUrl(getString(R.string.test_10086_url));
        list.add(model);

        model = new CommonTestModel();
        model.setName(getString(R.string.test_website_baidu));
        model.setUrl(getString(R.string.test_baidu_url));
        list.add(model);

        model = new CommonTestModel();
        model.setName(getString(R.string.test_website_tencent));
        model.setUrl(getString(R.string.test_tencent_url));
        list.add(model);

        model = new CommonTestModel();
        model.setName(getString(R.string.test_website_youku));
        model.setUrl(getString(R.string.test_youku_url));
        list.add(model);

        model = new CommonTestModel();
        model.setName(getString(R.string.test_website_sina));
        model.setUrl(getString(R.string.test_sina_url));
        list.add(model);

        model = new CommonTestModel();
        model.setName(getString(R.string.test_website_taobao));
        model.setUrl(getString(R.string.test_taobao_url));
        list.add(model);

        adapter = new PingTestAdapter(list);
        recyclerView.setAdapter(adapter);

    }

    Handler mHandler = new Handler(){
        public void dispatchMessage(Message msg) {
            Bundle bundle = msg.getData();
            float min = bundle.getFloat("min");
            float max = bundle.getFloat("max");
            float avg = bundle.getFloat("avg");
            int send = bundle.getInt("send");
            int loss = bundle.getInt("loss");
            String url = bundle.getString("url");

            Logger.d(TAG, "第" + (testIndex + 1) + "项测试完毕");

            sb.append("数据包：已发送 = ").append(send)
                    .append("，已接收 = ").append(send - loss)
                    .append("，丢失 = ").append(loss).append("\n")
                    .append("往返行程的估计时间<以毫秒为单位>：\n")
                    .append("最短 = ").append(min).append("ms")
                    .append("，最长 = ").append(max).append("ms")
                    .append("，平均 = ").append(avg).append("ms")
                    .append("\n\n\n");
            tvResult.setText(sb.toString());

            final CommonTestModel model = getTestModel(url);
            model.setDelay(avg);
            model.setIsStart(true);
            if (loss <= 0) {
                model.setSuccessRate(100);
            } else {
                model.setSuccessRate((1 - (loss / 10)) * 100);
            }

            adapter.update(testIndex);

            Logger.d(TAG, "测试结果：" + sb.toString());


            if (testIndex == list.size() - 1) {
                Logger.d(TAG,"第" + (testIndex + 1) + "项测试完毕，一共有：" + (list.size()) + "项");
                float delayTotal = 0;
                for (CommonTestModel m : list) {
                    delayTotal += m.getDelay();
                }

                int level = 0;
                float avgTotalDelay = delayTotal / list.size();
                if (avgTotalDelay < 60) {
                    level = 5;
                    tvAllInfo.setText(context.getString(R.string.test_speed_level_customer
                            ,getString(R.string.test_speed_level_faster)));
                } else if (avgTotalDelay > 60 && avgTotalDelay < 100) {
                    level = 4;
                    tvAllInfo.setText(context.getString(R.string.test_speed_level_customer
                            ,getString(R.string.test_speed_level_general)));
                } else if (avgTotalDelay > 100){
                    level = 3;
                    tvAllInfo.setText(context.getString(R.string.test_speed_level_customer
                            ,getString(R.string.test_speed_level_very_slow)));
                } else {
                    level = 1;
                    tvAllInfo.setText(context.getString(R.string.test_speed_level_customer
                            ,getString(R.string.test_speed_level_timeout)));
                }

                long endTime = System.currentTimeMillis();

                TestResultSummaryModel summaryModel = new TestResultSummaryModel();
                summaryModel.setTestDate(new Date());
                summaryModel.setTestLevel(level);
                summaryModel.setTestType(TestCode.TEST_TYPE_CONNECTION);
                summaryModel.setDelayTime((endTime - startTime));
                DatabaseHelper.getInstance().testResultDao.insertOrUpdate(summaryModel);
                return;
            }

            testIndex ++;
            Logger.d(TAG, "现在开始测试第" + (testIndex + 1) + "项");
            final CommonTestModel testModel = list.get(testIndex);
            startTestRefreshUI(testModel);

            tvAllInfo.setText(getString(R.string.common_test_item,testModel.getName()));
        }
    };

    private CommonTestModel getTestModel(String url) {
        CommonTestModel retVal = null;
        for (CommonTestModel model : list) {
            if (model.getUrl().equals(url)) {
                retVal = model;
                break;
            }
        }
        return retVal;
    }

    @Event(value = R.id.btn_test_connect)
    private void connectTestClick(View view) {
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
        ArrayList<Float> rrts = new ArrayList<Float>();
        AndroidHttpClient client = AndroidHttpClient.newInstance("Linux; Android");
        try {

//            HttpHead headMethod = new HttpHead("http://" + address);
            HttpHead headMethod = new HttpHead(address);
            headMethod.addHeader(new BasicHeader("Connection", "close"));
            headMethod.setParams(new BasicHttpParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 1000));
            int timeOut = (int) (1000 * (double) PING_TIMEOUT / PING_COUNT);
            HttpConnectionParams.setConnectionTimeout(headMethod.getParams(), timeOut);

            for (int i = 0; i < PING_COUNT; i++) {
                pingStartTime = System.currentTimeMillis();
                HttpResponse response = client.execute(headMethod);
                pingEndTime = System.currentTimeMillis();
                rrts.add((float) (pingEndTime - pingStartTime));
            }
            int packetLoss = PING_COUNT - rrts.size();
            disposeResult(address, packetLoss, rrts);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            pingJava(address);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    private void pingJava(String address) {
        long pingStartTime = 0;
        long pingEndTime = 0;
        ArrayList<Float> rrts = new ArrayList<Float>();

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
                    rrts.add((float) rrtVal);
                }
            }
            int packetLoss = PING_COUNT - rrts.size();
            disposeResult(address, packetLoss, rrts);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disposeResult(String url, int packetLoss, ArrayList<Float> rrts){
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        float avg;
        float total = 0;
        if (rrts.size() == 0) {
            return;
        }
        for (float rrt : rrts) {
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
        bundle.putFloat("min", min);
        bundle.putFloat("max", max);
        bundle.putFloat("avg", avg);
        bundle.putInt("send", PING_COUNT);
        bundle.putInt("loss", packetLoss);
        bundle.putString("url",url);
        msg.setData(bundle);
        SystemClock.sleep(300);
        mHandler.sendMessage(msg);
    }


}
