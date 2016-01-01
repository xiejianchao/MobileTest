package com.huhuo.mobiletest.utils;

import android.util.Log;

import com.huhuo.mobiletest.constants.AppConfig;


/**
 * log工具类
 * @author xiejc
 *
 */
public class Logger {

	public static void d(String tag,String msg) {
		if (AppConfig.IS_DEBUG) {
			Log.d(tag, msg);
		}
	}
	public static void d(String tag,String msg,Throwable e) {
		if (AppConfig.IS_DEBUG) {
			Log.d(tag, msg,e);
		}
	}
	public static void i(String tag,String msg) {
		if (AppConfig.IS_DEBUG) {
			Log.i(tag, msg);
		}
	}
	public static void v(String tag,String msg) {
		if (AppConfig.IS_DEBUG) {
			Log.v(tag, msg);
		}
	}
	public static void e(String tag,String msg) {
		if (AppConfig.IS_DEBUG) {
			Log.e(tag, msg);
		}
	}
	public static void e(String tag,String msg,Throwable e) {
		if (AppConfig.IS_DEBUG) {
			Log.e(tag, msg,e);
		}
	}
	public static void w(String tag,String msg) {
		if (AppConfig.IS_DEBUG) {
			Log.w(tag, msg);
		}
	}
	public static void w(String tag,String msg,Throwable e) {
		if (AppConfig.IS_DEBUG) {
			Log.w(tag, msg,e);
		}
	}
	
}
