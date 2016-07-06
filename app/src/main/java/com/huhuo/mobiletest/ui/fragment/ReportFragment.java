package com.huhuo.mobiletest.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@ContentView(R.layout.fragment_report)
public class ReportFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener{

    private static final String TAG = ReportFragment.class.getSimpleName();
    private AlertDialog problemDialog;

    private CheckBox cbNoNet;
    private CheckBox cbCantStartNet;
    private CheckBox cbDataStop;
    private CheckBox cbWebsiteCantOpen;
    private CheckBox cbAppCantConn;
    private CheckBox cbOther;

    //语音业务问题
    private CheckBox cbNoSignal;
    private CheckBox cbHaveSignalCantCall;
    private CheckBox cbHaveSignalCantCall2;
    private CheckBox cbCallStop;
    private CheckBox cbSilenceCall;
    private CheckBox cbEcho;
    private CheckBox cbCrosstalk;
    private CheckBox cbCallQualityLow;
    private CheckBox cbOther2;

    private View dialogView;

    /**
     * 保存当前选中状态的map
     */
    private HashMap<CheckBox,Boolean> cbMap = new HashMap<CheckBox,Boolean>();
    /**
     * 保存上一次的选中状态的map
     */
    private HashMap<CheckBox,Boolean> lastMap = new HashMap<CheckBox,Boolean>();

    @Override
    protected void init(Bundle savedInstanceState) {
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_problem_layout,null);

        initViews();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setPositiveButton("确定", confirmClickListener);
        builder.setNegativeButton("取消", cancelClickListener);
        problemDialog = builder.create();
    }


    private void initViews() {
        //数据业务问题
        cbNoNet = (CheckBox) dialogView.findViewById(R.id.cb_no_net);
        cbCantStartNet = (CheckBox) dialogView.findViewById(R.id.cb_cant_start_data);
        cbDataStop = (CheckBox) dialogView.findViewById(R.id.cb_data_stop);
        cbWebsiteCantOpen = (CheckBox) dialogView.findViewById(R.id.cb_website_cant_open);
        cbAppCantConn = (CheckBox) dialogView.findViewById(R.id.cb_app_cant_connection);
        cbOther = (CheckBox) dialogView.findViewById(R.id.cb_other);

        //语音业务问题
        cbNoSignal = (CheckBox) dialogView.findViewById(R.id.cb_no_signal);
        cbHaveSignalCantCall = (CheckBox) dialogView.findViewById(R.id.cb_have_signal_cant_call);
        cbHaveSignalCantCall2 = (CheckBox) dialogView.findViewById(R.id.cb_have_signal_cant_call_2);
        cbCallStop = (CheckBox) dialogView.findViewById(R.id.cb_calling_stop);
        cbSilenceCall = (CheckBox) dialogView.findViewById(R.id.cb_silence_call);
        cbEcho = (CheckBox) dialogView.findViewById(R.id.cb_echo);
        cbCrosstalk = (CheckBox) dialogView.findViewById(R.id.cb_crosstalk);
        cbCallQualityLow = (CheckBox) dialogView.findViewById(R.id.cb_voice_quality_low);
        cbOther2 = (CheckBox) dialogView.findViewById(R.id.cb_other_2);

        cbMap.put(cbNoNet, false);
        cbMap.put(cbCantStartNet,false);
        cbMap.put(cbDataStop,false);
        cbMap.put(cbWebsiteCantOpen,false);
        cbMap.put(cbAppCantConn,false);
        cbMap.put(cbOther, false);

        cbMap.put(cbNoSignal, false);
        cbMap.put(cbHaveSignalCantCall, false);
        cbMap.put(cbHaveSignalCantCall2, false);
        cbMap.put(cbCallStop, false);
        cbMap.put(cbSilenceCall, false);
        cbMap.put(cbEcho, false);
        cbMap.put(cbCrosstalk, false);
        cbMap.put(cbCallQualityLow, false);
        cbMap.put(cbOther2, false);

        cbNoNet.setOnCheckedChangeListener(this);
        cbCantStartNet.setOnCheckedChangeListener(this);
        cbDataStop.setOnCheckedChangeListener(this);
        cbWebsiteCantOpen.setOnCheckedChangeListener(this);
        cbAppCantConn.setOnCheckedChangeListener(this);
        cbOther.setOnCheckedChangeListener(this);
        //
        cbNoSignal.setOnCheckedChangeListener(this);
        cbHaveSignalCantCall.setOnCheckedChangeListener(this);
        cbHaveSignalCantCall2.setOnCheckedChangeListener(this);
        cbCallStop.setOnCheckedChangeListener(this);
        cbSilenceCall.setOnCheckedChangeListener(this);
        cbEcho.setOnCheckedChangeListener(this);
        cbCrosstalk.setOnCheckedChangeListener(this);
        cbCallQualityLow.setOnCheckedChangeListener(this);
        cbOther2.setOnCheckedChangeListener(this);
    }

    private DialogInterface.OnClickListener cancelClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //将选中的更新到TextView
            cbMap.clear();
            if (lastMap != null && lastMap.size() > 0) {
                Iterator<Map.Entry<CheckBox, Boolean>> it = lastMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<CheckBox, Boolean> next = it.next();
                    if (next != null) {
                        cbMap.put(next.getKey(),next.getValue());
                    }
                }
                Logger.v(TAG,"点击了取消，将之前保存的map的值重新赋值给当前map");
            }
            Logger.v(TAG, "点击了取消 cbMap size:" + cbMap.size() + ",last cbMap size:" + lastMap.size());

        }
    };

    private DialogInterface.OnClickListener confirmClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Logger.v(TAG,"点击了确定");
        }
    };

    @Event(value = R.id.btn_send_problem)
    private void sendProblem(View view) {
        ToastUtil.showMessage("等待接口");
    }

    @Event(value = R.id.ib_add_problem)
    private void addProblemClick(View view) {
        problemDialog.show();
        setDialogHeight();

        cbNoNet.setChecked(cbMap.get(cbNoNet) == null ? false : cbMap.get(cbNoNet));
        cbCantStartNet.setChecked(cbMap.get(cbCantStartNet) == null ? false : cbMap.get
                (cbCantStartNet));
        cbDataStop.setChecked(cbMap.get(cbDataStop) == null ? false : cbMap.get(cbDataStop));
        cbWebsiteCantOpen.setChecked(cbMap.get(cbWebsiteCantOpen) == null ? false : cbMap.get(cbWebsiteCantOpen));
        cbAppCantConn.setChecked(cbMap.get(cbAppCantConn) == null ? false : cbMap.get(cbAppCantConn));
        cbOther.setChecked(cbMap.get(cbOther) == null ? false : cbMap.get(cbOther));

        cbNoSignal.setChecked(cbMap.get(cbNoSignal) == null ? false : cbMap.get(cbNoSignal));
        cbHaveSignalCantCall.setChecked(cbMap.get(cbHaveSignalCantCall) == null ? false : cbMap.get(cbHaveSignalCantCall));
        cbHaveSignalCantCall2.setChecked(cbMap.get(cbHaveSignalCantCall2) == null ? false : cbMap.get(cbHaveSignalCantCall2));
        cbCallStop.setChecked(cbMap.get(cbCallStop) == null ? false : cbMap.get(cbCallStop));
        cbSilenceCall.setChecked(cbMap.get(cbSilenceCall) == null ? false : cbMap.get(cbSilenceCall));
        cbEcho.setChecked(cbMap.get(cbEcho) == null ? false : cbMap.get(cbEcho));
        cbCrosstalk.setChecked(cbMap.get(cbCrosstalk) == null ? false : cbMap.get(cbCrosstalk));
        cbCallQualityLow.setChecked(cbMap.get(cbCallQualityLow) == null ? false : cbMap.get(cbCallQualityLow));
        cbOther2.setChecked(cbMap.get(cbOther2) == null ? false : cbMap.get(cbOther2));

        //必须迭代保存到lastMap中，不能lastMap = cbMap的方式赋值，因为两者将指向同一地址。
        Iterator<Map.Entry<CheckBox, Boolean>> it = cbMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<CheckBox, Boolean> next = it.next();
            if (next != null) {
                CheckBox key = next.getKey();
                Boolean value = next.getValue();
                lastMap.put(key,value);
            }
        }
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        CheckBox cb = (CheckBox) buttonView;
        cbMap.put(cb, isChecked);
        Logger.d(TAG,buttonView.getId() + " " + isChecked);

        Boolean b1 = cbMap.get(cbCantStartNet);
        Boolean b2 = cbMap.get(cbDataStop);
        Boolean b3 = lastMap.get(cbCantStartNet);
        Boolean b4 = lastMap.get(cbDataStop);

        Logger.v(TAG,"现在保存的无法开启数据业务：" + b1);
        Logger.v(TAG,"现在保存的数据业务中断：" + b2);

        Logger.v(TAG,"之前保存的无法开启数据业务：" + b3);
        Logger.v(TAG,"之前保存的数据业务中断：" + b4);
    }

    private void setDialogHeight() {
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = problemDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8);   //高度设置为屏幕的0.3
//        p.width = (int) (d.getWidth() * 0.5);    //宽度设置为屏幕的1.0
        problemDialog.getWindow().setAttributes(p);     //设置生效
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (problemDialog != null && problemDialog.isShowing()) {
            problemDialog.dismiss();
            problemDialog.cancel();
        }
    }


}
