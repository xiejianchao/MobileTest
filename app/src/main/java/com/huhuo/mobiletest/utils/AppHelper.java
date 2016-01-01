package com.huhuo.mobiletest.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.CountDownTimer;
import android.text.TextUtils;
import com.huhuo.mobiletest.model.AppInfo;

public class AppHelper {

	private static final String TAG = AppHelper.class.getSimpleName();

	private static final String CHANNEL_KEY = "UMENG_CHANNEL";

	static QuitApp quitApp = null;
	static final long SHUTDOWN_TIME = 1000 * 3600;

	/**
	 * 获取当前app的版本号信息
	 * @param context
	 * @return
	 */
	public static AppInfo getAppVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			String versionName = info.versionName;
			int versionCode = info.versionCode;
			AppInfo appInfo = new AppInfo();
			appInfo.setVersionName(versionName);
			appInfo.setVersionCode(versionCode);
			return appInfo;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从Mainifest.xml中获取当前安装的app是哪个渠道的
	 * @param context
	 * @return
	 */
	public static String getChannelCode(Context context) {
		String code = getMetaData(context, CHANNEL_KEY);
		if (!TextUtils.isEmpty(code)) {
			return code;
		}
		return null;
	}

	public static String getMetaData(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context
					.getPackageName(), PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public static List<ResolveInfo> getShareApps(Context context) {
		List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		// intent.setMsgType("text/plain");
		intent.setType("*/*");
		PackageManager pManager = context.getPackageManager();
		mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		return mApps;
	}

	public static List<AppInfo> getShareAppList(Context context) {
		List<AppInfo> shareAppInfos = new ArrayList<AppInfo>();
		PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> resolveInfos = getShareApps(context);
		if (null == resolveInfos) {
			return null;
		} else {
			for (ResolveInfo resolveInfo : resolveInfos) {
				AppInfo appInfo = new AppInfo();
				appInfo.setAppPkgName(resolveInfo.activityInfo.packageName);
				Logger.d(TAG, "pkg>" + resolveInfo.activityInfo.packageName + ";name>" + resolveInfo.activityInfo.name);
				appInfo.setAppLauncherClassName(resolveInfo.activityInfo.name);
				appInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());
				Logger.d(TAG, "share app name:" + resolveInfo.loadLabel(packageManager).toString());
				appInfo.setAppIcon(resolveInfo.loadIcon(packageManager));
				shareAppInfos.add(appInfo);
			}
		}
		return shareAppInfos;
	}

	/**
	 * 获得栈顶的Activity
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getTaskTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
		if (runningTaskInfos != null) {
			ComponentName baseActivity = runningTaskInfos.get(0).baseActivity;
			String className = baseActivity.getClassName();
			return (runningTaskInfos.get(0).topActivity).toString();
		} else {
			return null;
		}
	}

	public static boolean isBackground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	private static class QuitApp extends CountDownTimer {

		private Activity activity;

		public QuitApp(Activity activity, long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			this.activity = activity;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			Logger.d(TAG, (millisUntilFinished / 1000) + " 秒后退出应用");
		}

		@Override
		public void onFinish() {
			if (activity != null) {
				activity.finish();
			}
			Logger.d(TAG, "退出应用");
			System.exit(0);
		}

	}

	public static void quitAppTimer() {
		quitApp = new QuitApp(null, SHUTDOWN_TIME, 1000);
		quitApp.start();
	}

	public static void cancelQuitAppTimer() {
		Logger.d(TAG, "取消定时关闭App");
		if (quitApp != null) {
			quitApp.cancel();
			quitApp = null;
		}
	}


	public enum Orientation {
		LEFT, RIGHT
	}

	/**
	 * 判断是否安装了目标应用
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	public static boolean isInstallApp(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	public static boolean isAppInstalled(Context context, String packagename) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			// System.out.println("没有安装");
			return false;
		} else {
			// System.out.println("已经安装");
			return true;
		}
	}


}
