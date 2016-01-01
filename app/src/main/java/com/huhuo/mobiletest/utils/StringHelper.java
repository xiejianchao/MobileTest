package com.huhuo.mobiletest.utils;
import android.text.TextUtils;
import android.widget.EditText;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.UUID;

public class StringHelper {
	private static final String TAG = "StringHelper";


	public static String int2String(int str) {
		return str + "";
	}

	public static void deleteEmoticon(EditText editText) {
		String str = editText.getText().toString();
		Logger.d(TAG, "输入的表情为：" + str);
		int selectionStart = editText.getSelectionStart();// 获取光标的位置
		if (selectionStart > 0) {
			String body = editText.getText().toString();
			if (!TextUtils.isEmpty(body)) {
				String tempStr = body.substring(0, selectionStart);
				int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
				if (i != -1) {
					String cs = tempStr.substring(i, selectionStart);
					if (cs.startsWith("[") && cs.endsWith("]")) {// 判断是否是一个表情
						editText.getEditableText().delete(i, selectionStart);
						return;
					}
				}
				editText.getEditableText().delete(tempStr.length() - 1, selectionStart);
			}
		}
	}


	/**
	 * 根据下载url，得到下载的文件的文件名
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileNameByUrl(String url) {
		if (!TextUtils.isEmpty(url)) {
			String[] split = url.split("/");
			String name = split[split.length - 1];
			return name;
		}
		return null;
	}

	public static String getRealVoicePath(String audiourl) {
		String realAudioUrl = null;
		if (audiourl.endsWith(".aud")) {// iphone 录音后缀是.aud，需要改为.m4a下载播放
			realAudioUrl = audiourl.replace(".aud", ".m4a");
		}
		if (audiourl.endsWith(".m4a")) {// 安卓录音返回的直接就是.m4a
			realAudioUrl = audiourl;
		}
		return realAudioUrl;
	}


	/**
	 * 拼接控制超长图片显示的html代码，解决MX3等机型显示不正常的问题
	 * 
	 * @param url
	 * @return
	 */
	public static String createLargeImageHTML(String url) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!doctype html>");
		sb.append("<html class='no-js' lang='zh-CN'>");
		sb.append("<head>");
		sb.append("<meta charset='utf-8' />");
		sb.append("<meta id='viewport' name='viewport' content='width=640' />");
		sb.append("<style>");
		sb.append("body,div,img {margin:0;padding:0;}");
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div>");
		sb.append("<img src='" + url + "' width='640' >");
		sb.append("</div>");
		sb.append("</body>");
		sb.append("</html>");
		sb.append("<head>");
		Logger.d(TAG, "large image html:" + sb.toString());
		return sb.toString();
	}

	/**
	 * 数字格式化输出，主要把数字 以 2.3w 格式输出
	 *
	 * @tags @param count
	 * @tags @return
	 */
	public static String countToStringFormat(int count) {
		if (count == 0) {
			return "0";
		} else if (count > 10000) {
			String cou = String.valueOf(count);
			if ("0".equals(cou.substring(cou.length() - 4, cou.length() - 3))) {
				return cou.substring(0, cou.length() - 4) + "万";
			} else {
				return cou.substring(0, cou.length() - 4) + "." + cou.substring(cou.length() - 4, cou.length() - 3) + "万";
			}
		} else {
			return String.valueOf(count);
		}
	}

	public static boolean isGifImage(String path) {
		if (!TextUtils.isEmpty(path)) {
			String url = path.toLowerCase();
			if (url.endsWith(".gif")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}


	public static byte[] getUUID() {
		UUID uuid = UUID.randomUUID();
		ByteBuffer bf = ByteBuffer.allocate(16);
		bf.putLong(uuid.getLeastSignificantBits());
		bf.putLong(uuid.getMostSignificantBits());
		bf.flip();
		return bf.array();
	}

	public static String getStringUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static String bytes2String(byte[] value) {
		StringBuilder sb = new StringBuilder();
		for (byte b : value) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	/**
	 * 16进制的字符串表示转成字节数组
	 *
	 * @param hexString
	 *            16进制格式的字符串
	 * @return 转换后的字节数组
	 **/
	public static byte[] toByteArray(String hexString) {
		hexString = hexString.toLowerCase(Locale.getDefault());
		final byte[] byteArray = new byte[hexString.length() / 2];
		int k = 0;
		for (int i = 0; i < byteArray.length; i++) {
			// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
			byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
			byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
			byteArray[i] = (byte) (high << 4 | low);
			k += 2;
		}
		return byteArray;
	}

	public static byte[] mkRegKey(long userId, String domain, String password, byte[] serverKey) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA");
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			ByteBuffer source = ByteBuffer.allocate(24);
			byte[] userids = intToByteArray((int) userId);
			source.put(userids);
			byte[] pwds = sha.digest((domain + ":" + password).getBytes("UTF-8"));
			source.put(pwds);
			byte[] pwd = md5.digest(sha.digest(source.array()));
			ByteBuffer resp1 = ByteBuffer.allocate(40);
			resp1.put(md5.digest((userId + "@" + domain + ":" + password).getBytes("UTF-8")));
			resp1.put(serverKey);
			byte[] pwd2 = md5.digest(resp1.array());
			ByteBuffer resp = ByteBuffer.allocate(32);
			resp.put(pwd);
			resp.put(pwd2);
			return resp.array();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) (i & 0xFF);
		result[1] = (byte) ((i >> 8) & 0xFF);
		result[2] = (byte) ((i >> 16) & 0xFF);
		result[3] = (byte) ((i >> 24) & 0xFF);
		return result;
	}

}
