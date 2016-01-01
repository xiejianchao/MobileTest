package com.huhuo.mobiletest.utils;


import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;

public enum DateType {


	BEFORE(0, MobileTestApplication.getInstance().getApplicationContext().getString(R.string.common_before_yesterday_2)),
	YESTERDAY(1, MobileTestApplication.getInstance().getApplicationContext().getString(R.string.common_yesterday)),
	TODAY(2, MobileTestApplication.getInstance().getApplicationContext().getString(R.string.common_today)),
	TOMORROW(3, MobileTestApplication.getInstance().getApplicationContext().getString(R.string.common_tomorrow));
		
	private int value;
	private String text;
	
	private DateType(int value, String label) {
		this.value = value;
		this.text = label;
	}
	
	public Integer getValue() {
		return value;
	}

	public String getLabel() {
		return text;
	}
		
}