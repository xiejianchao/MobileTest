package com.huhuo.mobiletest.utils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.huhuo.mobiletest.R;

public class CommonUtils {

	public static boolean isChars(String letters) {
		for (int i = 0; i < letters.length(); i++) {
			if (letters.charAt(i) >= 'a' && letters.charAt(i) <= 'z') {
				continue;
			} else if (letters.charAt(i) >= 'A' && letters.charAt(i) <= 'Z') {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumber(String numbers) {
		if (numbers == null || numbers.length() == 0) {
			return false;
		}
		for (int i = 0; i < numbers.length(); i++) {
			if (!(numbers.charAt(i) >= '0' && numbers.charAt(i) <= '9')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将value前面补0,直到达到指定的长度
	 * 
	 * @param length
	 * @param value
	 * @return
	 */
	public static String formatNumber(int length, int value) {
		StringBuffer sb = new StringBuffer();
		sb.append(value);
		while (sb.length() < length) {
			sb.insert(0, "0");
		}
		return sb.toString();
	}

	/**
	 * 判断SDcard空间是否小于某值
	 * 
	 * @param sizeMb
	 * @return
	 */
	public static boolean isAvaiableSpace(int sizeMb) {
		boolean ishasSpace = false;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(sdcard);
			long blockSize = statFs.getBlockSize();
			long blocks = statFs.getAvailableBlocks();
			long availableSpare = (blocks * blockSize) / (1024 * 1024);
			if (availableSpare < sizeMb) {
				ishasSpace = true;
			}
		}
		return ishasSpace;
	}

	/**
	 * 将语音message时间格式化 , 格式为 x′ m″
	 * 
	 * @param time
	 * @return
	 */
	public static String voiceTimeFormat(int time) {
		if (time > 59) {
			int s = time / 60;
			int m = time % 60;
			return String.valueOf(s) + "′ " + String.valueOf(m) + "″";
		}
		return String.valueOf(time) + "″";
	}

	/**
	 * 判断是否有SD卡, 并弹Toast提示
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasSDToast(Context context) {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			ToastUtil.showShortToast(R.string.common_no_sdcard);
			return false;
		}
		return true;
	}

	/**
	 * 判断是否有SD卡
	 * 
	 * @param
	 * @return
	 */
	public static boolean hasSDCard() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否符合手机号码
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean validateMobileNum(String strEmail) {
		String strPattern = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail.trim());
		if (m.matches()) {
			return true;
		}
		return false;
	}

	public static boolean validateEmail(String email) {
		// Pattern pattern =
		// Pattern.compile("[0-9a-zA-Z]*.[0-9a-zA-Z]*@[a-zA-Z]*.[a-zA-Z]*",
		// Pattern.LITERAL);
		if (email == null) {
			return false;
		}
		// 验证开始
		// 不能有连续的.
		if (email.indexOf("..") != -1) {
			return false;
		}
		// 必须带有@
		int atCharacter = email.indexOf("@");
		if (atCharacter == -1) {
			return false;
		}
		// 最后一个.必须在@之后,且不能连续出现
		if (atCharacter > email.lastIndexOf('.') || atCharacter + 1 == email.lastIndexOf('.')) {
			return false;
		}
		// 不能以.,@结束和开始
		if (email.endsWith(".") || email.endsWith("@") || email.startsWith(".")
				|| email.startsWith("@")) {
			return false;
		}
		return true;
	}

	/**
	 * 克隆一个新的对象 PS：必须是相同类型的对象, 克隆的属性如果是另一个Object, 则该属性也是克隆的只是地址, 与源对象的属性指向同一个对象
	 * 
	 * @param source
	 * @param target
	 */
	@SuppressWarnings("rawtypes")
	public static void clone(Object source, Object target) {
		Class sourceClass = source.getClass();
		Class targetClass = target.getClass();

		if (!sourceClass.getSimpleName().equals(targetClass.getSimpleName())) {
			return;
		}

		Field[] sourceField = sourceClass.getDeclaredFields();
		for (int i = 0; i < sourceField.length; i++) {
			try {
				sourceField[i].setAccessible(true);
				String fieldName = sourceField[i].getName();
				Field field = targetClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(target, sourceField[i].get(source));

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
	}

}
