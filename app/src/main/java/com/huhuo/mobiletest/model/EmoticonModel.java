package com.huhuo.mobiletest.model;

public class EmoticonModel {

	public static final int Shop = -1;
	public static final int Default = 1;
	public static final int GifBlue = 4;

	public int type = 0;
	public int resourceId;
	public String emoStr;
	public String token = null;

	public EmoticonModel(int emoType) {
		type = emoType;
	}

	public EmoticonModel(int resId, String emoStr) {
		resourceId = resId;
		this.emoStr = emoStr;
	}

	public EmoticonModel(int resId, String emoStr, int emoType) {
		resourceId = resId;
		this.emoStr = emoStr;
		type = emoType;
	}

	public EmoticonModel(int resId, String token, String emoStr) {
		resourceId = resId;
		this.token = token;
		this.emoStr = emoStr;
		type = EmoticonModel.GifBlue;
	}

	public void setType(int emoType) {
		type = emoType;
	}
}
