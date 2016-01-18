package com.huhuo.mobiletest.ui.activity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.model.RecordEntity;
import com.huhuo.mobiletest.utils.DateUtil;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.NetWorkUtil;
import com.huhuo.mobiletest.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@ContentView(R.layout.activity_voice_test)
public class VoiceTestActivity extends BaseActivity {

    private static final String TAG = VoiceTestActivity.class.getSimpleName();

    @ViewInject(R.id.tv_status)
    private TextView tvStatus;


    @ViewInject(R.id.tv_test_number)
    private TextView tvTestPhoneNumber;

    @ViewInject(R.id.tv_test_delay)
    private TextView tvTestDelay;

    @ViewInject(R.id.tv_test_duration)
    private TextView tvTestCallDuration;

    @ViewInject(R.id.tv_test_call_type)
    private TextView tvTestCallType;

    @ViewInject(R.id.tv_test_call_result)
    private TextView tvTestCallResult;

    private PhoneReceive receive;

    private static final String TEST_PHONE_NUMBER = "10010";
    private StringBuilder sb = new StringBuilder();

    private Date startCallTime;
    private Date endCallTime;

    @Override
    protected void init(Bundle savedInstanceState) {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);

        registerBroadcastReceive();

        Uri uri = Uri.parse("tel:" + TEST_PHONE_NUMBER);
        Intent call = new Intent(Intent.ACTION_CALL, uri); //直接播出电话
        startActivity(call);
    }

    private void registerBroadcastReceive() {
        receive = new PhoneReceive();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receive,filter);
    }

    private boolean startCall = false;

    private class PhoneReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //如果是去电
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
                String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Logger.d(TAG, "call OUT:" + phoneNumber);

                startCallTime = new Date();
                String formatTime = DateUtil.getFormatTime(startCallTime, DateUtil.PATTERN_STANDARD);

                tvStatus.setText("拨打号码：" + phoneNumber + "\n拨打时间：" + formatTime + "\n");
                startCall = true;
            } else {
                Logger.d(TAG,"不监听来电电话");
                String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Logger.d(TAG, "来电电话:" + phoneNumber);
            }
        }
    }

    private class PhoneListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://来电状态
                    Logger.d(TAG, "来电了");
                    tvStatus.append("来电了\n");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接听状态
                    Logger.d(TAG,"接听来电");
                    sb.append("接听来电\n");
                    tvStatus.append("接听来电\n");
                    break;
                case TelephonyManager.CALL_STATE_IDLE://挂断后回到空闲状态
                    Logger.d(TAG, "挂断后回到空闲状态");
                    endCallTime = new Date();
                    final String formatTime = DateUtil.getFormatTime(endCallTime, DateUtil
                            .PATTERN_STANDARD);
                    if (startCall) {
                        sb.append("挂断后回到空闲状态 "+ formatTime +"\n");
                        tvStatus.append("挂断后回到空闲状态 "+ formatTime +"\n");

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getCallRecord();
                            }
                        },1000);
                    }
                    break;

                default:
                    break;
            }
        }

    }

    private void getCallRecord() {
        ContentResolver contentResolver = this.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(
                    // CallLog.Calls.CONTENT_URI, Columns, null,
                    // null,CallLog.Calls.DATE+" desc");
                    CallLog.Calls.CONTENT_URI, null, null, null,
                    CallLog.Calls.DATE + " desc");
            if (cursor == null)
                return;

            List<RecordEntity> mRecordList = new ArrayList<RecordEntity>();
            while (cursor.moveToFirst()) {
                RecordEntity record = new RecordEntity();
                record.name = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.CACHED_NAME));
                record.number = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NUMBER));
                record.type = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.TYPE));
                record.lDate = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DATE));
                record.duration = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DURATION));
                record._new = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.NEW));
                Logger.e(TAG, record.toString());
//						int photoIdIndex = cursor.getColumnIndex(CACHED_PHOTO_ID);
//						if (photoIdIndex >= 0) {
//							record.cachePhotoId = cursor.getLong(photoIdIndex);
//						}

                mRecordList.add(record);
                break;
            }

            final RecordEntity entity = mRecordList.get(0);

            updateTestResult(entity);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void updateTestResult(RecordEntity entity) {
        if (startCall && entity.number.equals(TEST_PHONE_NUMBER)) {
            startCall = false;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(entity.lDate);
            final Date time = calendar.getTime();
            final String format = sdf.format(time);

            calendar.setTime(endCallTime);
            final Date endCallDate = calendar.getTime();

            calendar.add(Calendar.SECOND, -(int) entity.duration);
            final Date realStartCallDate = calendar.getTime();

            //接通时间 - 拨打时间
            calendar.setTimeInMillis(entity.lDate);
            Date startCallDate = calendar.getTime();

            tvStatus.setText(null);
            sb.append("通话号码：" + entity.number + "\n");

            tvTestPhoneNumber.append(entity.number);

            long intevalLong  = realStartCallDate.getTime() - startCallDate.getTime();
            float intevalFloat = (float)intevalLong / 1000;
            int inteval = (int)Math.floor(intevalFloat);

            String intervalStr = entity.duration == 0 ? "呼叫失败" : inteval + "";
            tvTestDelay.append(intervalStr);

            String duration = entity.duration == 0 ? "呼叫失败" : entity.duration + "";
            tvTestCallDuration.append(duration);


            final String callType = getCallType(entity.duration);
            String callTypeStr = entity.duration == 0 ? "呼叫失败" : callType;
            tvTestCallType.append(callTypeStr);

            String testResult = entity.duration == 0 ? "失败" : "成功";
            tvTestCallResult.append(testResult);

            sb.append("接通时延long：" + intevalLong + "毫秒\n");
            sb.append("接通时延2：" + intevalFloat + "float秒\n");
            sb.append("接通时延3：" + inteval + "int秒\n");
            sb.append("拨打时间：" + format + "\n");
            sb.append("接通延时：" + inteval + "\n");
            sb.append("接通时间：" + sdf.format(realStartCallDate) + "\n");
            sb.append("通话时长：" + entity.duration + "\n");
            sb.append("结束时间：" + sdf.format(endCallTime) + "\n");

            tvStatus.append(sb.toString());
            Logger.d(TAG,sb.toString());
        } else {
            ToastUtil.showShortToast("startCall:" + startCall
                    + "phoneNumber:" + entity.number.equals(TEST_PHONE_NUMBER));
        }

    }

    private String getCallType(long duration) {

        String type = null;

        if (duration <= 0) {
            type = "呼叫失败";
        }

        final boolean mobileAvailable = NetWorkUtil.isMobileAvailable();
        if (!mobileAvailable) {
            type = "其他";
        } else {
            final String typeName = NetWorkUtil.getCurrentNetworkType();
            if (typeName.equalsIgnoreCase("4G")) {
                //TODO 这里需要详细区分是,暂时先这样，SRLTE，SGLTE，SVLTE，CSFB，VoLTE

                final String provider = NetWorkUtil.getProvider();
                if (!TextUtils.isEmpty(provider)) {
                    if (provider.equals("中国电信")) {
                        type = "SRLTE";
                    }
                    if (provider.equals("中国移动")) {
                        type = "CSFB";
                    }

                    if (provider.equals("中国联通")) {
                        type = "CSFB";
                    }
                } else {
                    type = "其他";
                }
            }

            if (typeName.equalsIgnoreCase("3G")) {
                type = "GSM";
            }

            if (typeName.equalsIgnoreCase("2G")) {
                type = "GSM";
            }
        }
        return type;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receive != null) {
            unregisterReceiver(receive);
        }
    }
}
