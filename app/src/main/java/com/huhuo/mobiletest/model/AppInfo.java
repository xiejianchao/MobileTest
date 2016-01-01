package com.huhuo.mobiletest.model;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private String appPkgName;
	private String appLauncherClassName;
	private String appName;
	private Drawable appIcon;
	private String versionName;
	private int versionCode;

	public String getAppPkgName() {
		return appPkgName;
	}

	public void setAppPkgName(String appPkgName) {
		this.appPkgName = appPkgName;
	}

	public String getAppLauncherClassName() {
		return appLauncherClassName;
	}

	public void setAppLauncherClassName(String appLauncherClassName) {
		this.appLauncherClassName = appLauncherClassName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

}
