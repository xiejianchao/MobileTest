package com.huhuo.mobiletest.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputMethodUtil {

	public static boolean isShow(Context context) {
		InputMethodManager im = ServiceUtil.getInputMethodManager(context);
		boolean active = im.isActive();
		return active;
	}

	public static void showInputMehtod(Context context) {
		InputMethodManager inputMethodManager = ServiceUtil.getInputMethodManager(context);
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void closeInputMethod(Context context) {
		if (context != null) {
			if (isShow(context)) {
				InputMethodManager inputMethodManager = ServiceUtil
						.getInputMethodManager(context);
				View cf = ((Activity) context).getCurrentFocus();
				if (inputMethodManager != null && cf != null && cf.getWindowToken() != null) {
					inputMethodManager.hideSoftInputFromWindow(((Activity) context)
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
	}

	/**
	 * 强制关闭软键盘？
	 * 
	 * @param context
	 */
	public static void forceCloseSoftInputKeyboard(Activity context) {
		if (context != null) {
			InputMethodManager inputMethodManager = ServiceUtil.getInputMethodManager(context);
			if (context.getCurrentFocus() != null
					&& context.getCurrentFocus().getWindowToken() != null) {
				inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
	}
}
