package com.huhuo.mobiletest.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.huhuo.mobiletest.R;


public class DialogBuilder {

	/**
	 * create dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param negText
	 * @param negClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatDialog(Context context, String title,
			String message, String posText,
			DialogInterface.OnClickListener posClickListener, String negText,
			DialogInterface.OnClickListener negClickListener, boolean cancelable) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(posText, posClickListener);
		builder.setNegativeButton(negText, negClickListener);
		builder.setCancelable(cancelable);
		return builder.create();
	}

	/**
	 * show the dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param negText
	 * @param negClickListener
	 * @param cancelable
	 */
	public static void showDialog(Context context, String title,
			String message, String posText,
			DialogInterface.OnClickListener posClickListener, String negText,
			DialogInterface.OnClickListener negClickListener, boolean cancelable) {
		creatDialog(context, title, message, posText, posClickListener,
				negText, negClickListener, cancelable).show();
	}

	/**
	 * create dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param negText
	 * @param negClickListener
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatDialog(Context context, String title,
			String message, String posText,
			DialogInterface.OnClickListener posClickListener, String negText,
			DialogInterface.OnClickListener negClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(posText, posClickListener);
		builder.setNegativeButton(negText, negClickListener);
		builder.setCancelable(true);
		return builder.create();
	}

	/**
	 * show the dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param negText
	 * @param negClickListener
	 */
	public static void showDialog(Context context, String title,
			String message, String posText,
			DialogInterface.OnClickListener posClickListener, String negText,
			DialogInterface.OnClickListener negClickListener) {
		creatDialog(context, title, message, posText, posClickListener,
				negText, negClickListener).show();
	}

	/**
	 * create default dialog with title
	 * "提示"，posText-"确定"，negText-"取消",cancelable-true
	 * 
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param negClickListener
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatDialog(Context context, String message,
			DialogInterface.OnClickListener posClickListener,
			DialogInterface.OnClickListener negClickListener) {
		return creatDialog(context, context.getString(R.string.common_prompt),
				message, context.getString(R.string.common_ok), posClickListener,
				context.getString(R.string.common_cancel), negClickListener, true);
	}

	/**
	 * show default dialog with title
	 * "提示"，posText-"确定"，negText-"取消",cancelable-true
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param negClickListener
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */

	public static void showDialog(Context context, String message,
			DialogInterface.OnClickListener posClickListener,
			DialogInterface.OnClickListener negClickListener) {
		creatDialog(context, message, posClickListener, negClickListener).show();
	}

	/**
	 * create default dialog with title "提示"，posText-"确定"，negText-"取消",
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param negClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatDialog(Context context, String message,
			DialogInterface.OnClickListener posClickListener,
			DialogInterface.OnClickListener negClickListener, boolean cancelable) {
		return creatDialog(context, context.getString(R.string.common_prompt),
				message, context.getString(R.string.common_ok), posClickListener,
				context.getString(R.string.common_cancel), negClickListener,
				cancelable);
	}

	/**
	 * show default dialog with title "提示"，posText-"确定"，negText-"取消",
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param negClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static void showDialog(Context context, String message,
			DialogInterface.OnClickListener posClickListener,
			DialogInterface.OnClickListener negClickListener, boolean cancelable) {
		creatDialog(context, message, posClickListener, negClickListener,
				cancelable);
	}

	/**
	 * create single button dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatSingleDialog(Context context, String title,
			String message, String posText,
			DialogInterface.OnClickListener posClickListener, boolean cancelable) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(posText, posClickListener);
		builder.setCancelable(cancelable);
		return builder.create();
	}

	/**
	 * show single button dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static void showSingleDialog(Context context, String title,
			String message, String posText,
			DialogInterface.OnClickListener posClickListener, boolean cancelable) {
		creatSingleDialog(context, title, message, posText, posClickListener,
				cancelable).show();
	}

	/**
	 * create default dialog with title "提示"，posText-"确定"
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatSingleDialog(Context context,
			String message, DialogInterface.OnClickListener posClickListener) {
		return creatSingleDialog(context, context.getString(R.string.common_prompt),
				message, context.getString(R.string.common_ok), posClickListener, true);
	}

	/**
	 * show default dialog with title "提示"，posText-"确定"
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static void showSingleDialog(Context context, String message,
			DialogInterface.OnClickListener posClickListener) {
		creatSingleDialog(context, context.getString(R.string.common_prompt), message,
				context.getString(R.string.common_ok), posClickListener, true).show();
	}

	/**
	 * create default dialog with title "提示"，posText-"确定"
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatSingleDialog(Context context,
			String message, DialogInterface.OnClickListener posClickListener,
			boolean cancelable) {
		return creatSingleDialog(context, context.getString(R.string.common_prompt),
				message, context.getString(R.string.common_ok), posClickListener,
				cancelable);
	}

	/**
	 * show default dialog with title "提示"，posText-"确定"
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static void showSingleDialog(Context context, String message,
			DialogInterface.OnClickListener posClickListener, boolean cancelable) {
		creatSingleDialog(context, context.getString(R.string.common_prompt), message,
				context.getString(R.string.common_ok), posClickListener, cancelable)
				.show();
	}

	// ///////////////////////////////////////////////重载参数，传int/////////////////////////////////////////////////////////////////
	/**
	 * create dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param negText
	 * @param negClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatDialog(Context context, int title,
			int message, int posText,
			DialogInterface.OnClickListener posClickListener, int negText,
			DialogInterface.OnClickListener negClickListener, boolean cancelable) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(posText, posClickListener);
		builder.setNegativeButton(negText, negClickListener);
		builder.setCancelable(cancelable);
		return builder.create();
	}

	/**
	 * show the dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param negText
	 * @param negClickListener
	 * @param cancelable
	 */
	public static void showDialog(Context context, int title, int message,
			int posText, DialogInterface.OnClickListener posClickListener,
			int negText, DialogInterface.OnClickListener negClickListener,
			boolean cancelable) {
		creatDialog(context, title, message, posText, posClickListener,
				negText, negClickListener, cancelable).show();
	}

	/**
	 * create dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param negText
	 * @param negClickListener
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatDialog(Activity context, int title,
			int message, int posText,
			DialogInterface.OnClickListener posClickListener, int negText,
			DialogInterface.OnClickListener negClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(posText, posClickListener);
		builder.setNegativeButton(negText, negClickListener);
		builder.setCancelable(true);
		return builder.create();
	}

	/**
	 * show the dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param negText
	 * @param negClickListener
	 */
	public static void showDialog(Activity context, int title, int message,
			int posText, DialogInterface.OnClickListener posClickListener,
			int negText, DialogInterface.OnClickListener negClickListener) {
		creatDialog(context, title, message, posText, posClickListener,
				negText, negClickListener).show();
	}

	/**
	 * create default dialog with title
	 * "��ʾ"��posText-"ȷ��"��negText-"ȡ��",cancelable-true
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param negClickListener
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatDialog(Context context, int message,
			DialogInterface.OnClickListener posClickListener,
			DialogInterface.OnClickListener negClickListener) {
		return creatDialog(context, R.string.common_prompt, message, R.string.common_ok,
				posClickListener, R.string.common_cancel, negClickListener, true);
	}

	/**
	 * show default dialog with title "提示"，posText-"确定"
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param negClickListener
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */

	public static void showDialog(Context context, int message,
			DialogInterface.OnClickListener posClickListener,
			DialogInterface.OnClickListener negClickListener) {
		creatDialog(context, message, posClickListener, negClickListener)
				.show();
	}

	/**
	 * create default dialog with title "��ʾ"��posText-"ȷ��"��negText-"ȡ��",
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param negClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatDialog(Context context, int message,
			DialogInterface.OnClickListener posClickListener,
			DialogInterface.OnClickListener negClickListener, boolean cancelable) {
		return creatDialog(context, R.string.common_prompt, message, R.string.common_ok,
				posClickListener, R.string.common_cancel, negClickListener, cancelable);
	}

	/**
	 * show default dialog with title "��ʾ"��posText-"ȷ��"��negText-"ȡ��",
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param negClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static void showDialog(Context context, int message,
			DialogInterface.OnClickListener posClickListener,
			DialogInterface.OnClickListener negClickListener, boolean cancelable) {
		creatDialog(context, message, posClickListener, negClickListener,
				cancelable).show();
	}

	/**
	 * create single button dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatSingleDialog(Context context, int title,
			int message, int posText,
			DialogInterface.OnClickListener posClickListener, boolean cancelable) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(posText, posClickListener);
		builder.setCancelable(cancelable);
		return builder.create();
	}

	/**
	 * show single button dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param posText
	 * @param posClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static void showSingleDialog(Context context, int title,
			int message, int posText,
			DialogInterface.OnClickListener posClickListener, boolean cancelable) {
		creatSingleDialog(context, title, message, posText, posClickListener,
				cancelable).show();
	}

	/**
	 * create default dialog with title "��ʾ"��posText-"ȷ��"
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatSingleDialog(Context context, int message,
			DialogInterface.OnClickListener posClickListener) {
		return creatSingleDialog(context, R.string.common_prompt, message,
				R.string.common_ok, posClickListener, true);
	}

	/**
	 * show default dialog with title "��ʾ"��posText-"ȷ��"
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static void showSingleDialog(Context context, int message,
			DialogInterface.OnClickListener posClickListener) {
		creatSingleDialog(context, R.string.common_prompt, message, R.string.common_ok,
				posClickListener, true).show();
	}

	/**
	 * create default dialog with title "��ʾ"��posText-"ȷ��"
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static AlertDialog creatSingleDialog(Context context, int message,
			DialogInterface.OnClickListener posClickListener, boolean cancelable) {
		return creatSingleDialog(context, R.string.common_prompt, message,
				R.string.common_ok, posClickListener, cancelable);
	}

	/**
	 * show default dialog with title "��ʾ"��posText-"ȷ��"
	 *
	 * @param context
	 * @param message
	 * @param posClickListener
	 * @param cancelable
	 *            dialog isCan cancel,true-can cancel,false-can't
	 * @return
	 */
	public static void showSingleDialog(Context context, int message,
			DialogInterface.OnClickListener posClickListener, boolean cancelable) {
		creatSingleDialog(context, R.string.common_prompt, message, R.string.common_ok,
				posClickListener, cancelable).show();
	}

}
