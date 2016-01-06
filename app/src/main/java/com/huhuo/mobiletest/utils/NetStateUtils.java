package com.huhuo.mobiletest.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;

public class NetStateUtils {

	static final Context context = MobileTestApplication.getInstance().getApplicationContext();

	/**
	 * WIFI是否已打开
	 * 
	 * @return
	 */
	public static boolean isWifiEnable(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conMan
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return networkInfo.isConnectedOrConnecting();
	}

	/**
	 * 是否已启动移动网络
	 * 
	 * @return
	 */
	public static boolean isMobileNetworkEnable() {
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		networkInfo.getSubtype();
		return networkInfo.isConnectedOrConnecting();
	}

	/**
	 * 获取移动网络类型，是2G/3G/4G
	 *
	 * 只有在当前网络连接为移动网络连接时，调用该方法才有意义
	 */
	public static String getMobileNetType() {

		String type = null;
		if (isMobileNetworkEnable()) {
			ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//			TelephonyManager
			final String subtypeName = networkInfo.getSubtypeName();
			final int subtype = networkInfo.getSubtype();
			type = subtypeName + " || " + subtype;
		}
		return type;
	}

	/**
	 * 当前网络状况是否为2G
	 * 
	 * @return
	 */
	public static boolean isNetworkGprs(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return "GPRS".equals(ni.getSubtypeName());
	}

	/**
	 * 是否有网络可用,默认可以跳转系统设置界面
	 * 
	 * @return
	 */
	public static boolean isNetOk(Context context) {
		return isNetOk(context, true);
	}

	/**
	 * 是否有网络可用
	 * 
	 * @return
	 */
	public static boolean isNetOk(Context context, boolean bool) {
		if (!isWifiEnable(context) && !isMobileNetworkEnable()) {
			if (bool) {
				setNetwork(context);
			}
			return false;
		} else {
			return true;
		}
	}

	static AlertDialog dialog;

	/**
	 * setNetwork if the net is unuse,open the system setting
	 */
	public static void setNetwork(final Context context) {
		if (!(context instanceof Activity)) {
			throw new RuntimeException("context must be Activity");
		}
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		dialog = DialogBuilder.creatDialog((Activity)context, R.string.test_setting_net_title,
				R.string.test_net_setting, R.string.common_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						gotoSetting(context);

					}
				}, R.string.common_cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();

					}
				});
		dialog.show();
	}

	/**
	 * goto setting of sysytem
	 * 
	 * @param context
	 */
	private static void gotoSetting(final Context context) {
		Intent mIntent = new Intent("/");
		String sdkVersion = android.os.Build.VERSION.SDK;
		if (Integer.valueOf(sdkVersion) > 10) {
			mIntent = new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		} else {
			mIntent = new Intent();
			ComponentName comp = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			mIntent.setComponent(comp);
			mIntent.setAction("android.intent.action.VIEW");
		}
		context.startActivity(mIntent);
	}
}
