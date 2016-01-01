package com.huhuo.mobiletest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;

@SuppressLint("SimpleDateFormat")
public class DateUtil {


	private static Context context = MobileTestApplication.getInstance().getApplicationContext();
	
	public static class ConstanDay {
		public static final String TODAY = context.getString(R.string.common_today);
		public static final String YESTERDAY = context.getString(R.string.common_yesterday);
		public static final String TOMORROW = context.getString(R.string.common_tomorrow);
		public static final String BEFORE_YESTERDAY = context.getString(R.string.common_before_yesterday);
		public static final String AFTER_TOMORROW = context.getString(R.string.common_after_tomorrow);
	}

	public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String PATTERN_HH_MM = "HH:mm";
	public static final String PATTERN_XY_COMMENT = "MM-dd HH:mm";
	public static final String PATTERN_XY_DETAIL = "MM/dd HH:ss";
	public static final String PATTERN_MM_DD = "MM-dd";

	public static SimpleDateFormat getSimpleDateFormat(String pattern) {
		return new SimpleDateFormat(pattern);
	}
	
	public static String getTime(long time) {
		return getSimpleDateFormat(PATTERN_STANDARD).format(new Date(time));
	}
	
	public static String getTime(Date time) {
		return getSimpleDateFormat(PATTERN_STANDARD).format(time);
	}

	public static String showDay(Date date) {
		String time = getSimpleDateFormat(PATTERN_STANDARD).format(date);
		return analyze(time);
	}

	public static String showDay(String date) {
		return analyze(date);
	}

	public static String getInterval(Date date)  {
		if (date == null) {
			return null;
		}
		String time = null;
		Date currentDate = new Date();
		String compareDateStr = getSimpleDateFormat(PATTERN_YYYY_MM_DD).format(date);
		String currentDateStr = getSimpleDateFormat(PATTERN_YYYY_MM_DD).format(currentDate);
		int compareDay = Integer.valueOf(compareDateStr.split("-")[2]);//获得要比较的日期的天
		int currentDay = Integer.valueOf(currentDateStr.split("-")[2]);
		
		long interval = currentDate.getTime() - date.getTime();
		long second = interval / 1000;
		
		if (second == 0 || second < 15) {
			time = "刚刚";
		} else if (second < 30) {
			time = second + " 秒以前";
		} else if (second >= 30 && second < 60) {
			time = " 半分钟前";
		} else if (second >= 60 && second < 60 * 60) { // xx分钟 - 59分钟前
			long minute = second / 60;
			time = minute + " 分钟前";
		} else if (second >= 60 * 60 && second < 60 * 60 * 24) {
			long hour = (second / 60) / 60;
			if (hour <= 3) {
				time = hour + " 小时前";
			} else {
				if (currentDateStr.equals(compareDateStr)) {//两个日期年月日完全一样才显示今天
					time = "今天 " + getFormatTime(date, PATTERN_HH_MM);
				} else if (currentDay - compareDay == 1){//今天的天数减去要比较的天数相减为1说明是昨天
					time = "昨天 " + getFormatTime(date, PATTERN_HH_MM);
				} else {//月初减去山个月月底，并且不到两天
					time = "昨天 " + getFormatTime(date, PATTERN_HH_MM);
				}
			}
		} else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) {
			if (currentDay - compareDay < 0) {//月初减去山个月月底，并且不到两天
				time = "昨天 " + getFormatTime(date, PATTERN_HH_MM);
			} else  if (currentDay - compareDay == 1){
				time = "昨天 " + getFormatTime(date, PATTERN_HH_MM);
			} else if (currentDay - compareDay > 1) {
				time = getFormatTime(date, PATTERN_XY_COMMENT);
			}
		} else {
			time = getFormatTime(date, PATTERN_XY_COMMENT);
		}
		// 支持更多时间 
//		else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) {
//			long day = ((second / 60) / 60) / 24;
//			time = day + " 天前";
//		} else if (second >= 60 * 60 * 24 * 7) {
//			time = getFormatTime(date, "MM-dd HH:mm");
//		} else if (second >= 60 * 60 * 24 * 365) {
//			time = getFormatTime(date, "YYYY-MM-dd HH:mm");
//		} else {
//			time = "0";
//		}
		
		return time;
	}
	
	/**
	 * 
	 * 日期格式为 MM/dd HH:ss
	 * @param date
	 * @return
	 */
	public static String getDynamicDate(Date date)  {
		if (date == null) {
			return null;
		}
		String time = null;
		Date currentDate = new Date();
		String compareDateStr = getSimpleDateFormat(PATTERN_YYYY_MM_DD).format(date);
		String currentDateStr = getSimpleDateFormat(PATTERN_YYYY_MM_DD).format(currentDate);
		int compareDay = Integer.valueOf(compareDateStr.split("-")[2]);//获得要比较的日期的天
		int currentDay = Integer.valueOf(currentDateStr.split("-")[2]);
		
		long interval = currentDate.getTime() - date.getTime();
		long second = interval / 1000;
		
		if (second == 0 || second < 15) {
			time = "刚刚";
		} else if (second < 30) {
			time = second + " 秒以前";
		} else if (second >= 30 && second < 60) {
			time = " 半分钟前";
		} else if (second >= 60 && second < 60 * 60) { // xx分钟 - 59分钟前
			long minute = second / 60;
			time = minute + " 分钟前";
		} else if (second >= 60 * 60 && second < 60 * 60 * 24) {
			long hour = (second / 60) / 60;
			if (hour <= 3) {
				time = hour + " 小时前";
			} else {
				if (currentDateStr.equals(compareDateStr)) {//两个日期年月日完全一样才显示今天
					time = "今天 " + getFormatTime(date, PATTERN_HH_MM);
				} else if (currentDay - compareDay == 1){//今天的天数减去要比较的天数相减为1说明是昨天
					time = "昨天 " + getFormatTime(date, PATTERN_HH_MM);
				} else {//月初减去山个月月底，并且不到两天
					time = "昨天 " + getFormatTime(date, PATTERN_HH_MM);
				}
			}
		} else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) {
			if (currentDay - compareDay < 0) {//月初减去山个月月底，并且不到两天
				time = "昨天 " + getFormatTime(date, PATTERN_HH_MM);
			} else  if (currentDay - compareDay == 1){
				time = "昨天 " + getFormatTime(date, PATTERN_HH_MM);
			} else if (currentDay - compareDay > 1) {
				time = getFormatTime(date, PATTERN_XY_DETAIL);
			}
		} else {
			time = getFormatTime(date, PATTERN_XY_DETAIL);
		}
		return time;
	}
	/**
	 * 根据不同的时间，决定私信对话中显示的时间格式
	 * @param date
	 * @return
	 */
	public static String getMsgTime(Date date)  {
		if (date == null) {
			return null;
		}
		String time = null;
		DateType dateType = getDateType(date);
		switch (dateType) {
		case BEFORE:
			time = getFormatTime(date, PATTERN_MM_DD);
			break;
		case YESTERDAY:
			time = ConstanDay.YESTERDAY + " " + getFormatTime(date, PATTERN_HH_MM);
			break;
		case TODAY:
			time = getFormatTime(date, PATTERN_HH_MM);
			break;
		case TOMORROW:
			time = getFormatTime(date, PATTERN_STANDARD);
			break;

		default:
			break;
		}
		
		return time;
	}
	
	public static DateType getDateType(Date date) {
		DateType dateType = null;
		
		boolean yesterday = isYesterday(date);
		boolean today = isToday(date);
		boolean tomorrow = isTomorrow(date);
		
		if (yesterday) {
			dateType = DateType.YESTERDAY;
		} else if (today) {
			dateType = DateType.TODAY;
		} else if (tomorrow) {
			dateType = DateType.TOMORROW;
		} else {
			dateType = DateType.BEFORE;
		}
		
		return dateType;
	}
	
	public static boolean isYesterday(Date a ){
	    Calendar c = Calendar.getInstance();
	    c.set( Calendar.DATE, c.get(Calendar.DATE ) - 1 );
	    Date today = c.getTime();
	    SimpleDateFormat format = new SimpleDateFormat( PATTERN_YYYY_MM_DD );
	 
	    return format.format( today ).equals(format.format( a ) );
	}
	
	public static boolean isToday(Date a ){
		Calendar c = Calendar.getInstance();
		c.set( Calendar.DATE, c.get(Calendar.DATE ));
		Date today = c.getTime();
		SimpleDateFormat format = new SimpleDateFormat( PATTERN_YYYY_MM_DD );
		
		return format.format( today ).equals(format.format( a ) );
	}
	
	public static boolean isTomorrow(Date a ){
		Calendar c = Calendar.getInstance();
		c.set( Calendar.DATE, c.get(Calendar.DATE ) + 1);
		Date today = c.getTime();
		SimpleDateFormat format = new SimpleDateFormat( PATTERN_YYYY_MM_DD );
		
		return format.format( today ).equals(format.format( a ) );
	}

	public static String getFormatTime(Date date, String sdf) {
		return (new SimpleDateFormat(sdf)).format(date);
	}

	private static String analyze(String date) {
		Calendar today = Calendar.getInstance();
		Calendar target = Calendar.getInstance();

		SimpleDateFormat df = getSimpleDateFormat(PATTERN_STANDARD);
		try {
			today.setTime(df.parse(getNowDateToStr()));
			today.set(Calendar.HOUR, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			target.setTime(df.parse(date));
			target.set(Calendar.HOUR, 0);
			target.set(Calendar.MINUTE, 0);
			target.set(Calendar.SECOND, 0);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		long intervalMilli = target.getTimeInMillis() - today.getTimeInMillis();
		int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
		return showDateDetail(xcts, target);
	}

	/**
	 * 将日期差显示为日期
	 * 
	 * @param xcts
	 * @param target
	 * @return
	 */
	private static String showDateDetail(int xcts, Calendar target) {
		switch (xcts) {
		case 0:
			return ConstanDay.TODAY;
		case 1:
			return ConstanDay.TOMORROW;
		case 2:
			return ConstanDay.AFTER_TOMORROW;
		case -1:
			return ConstanDay.YESTERDAY;
		case -2:
			return ConstanDay.BEFORE_YESTERDAY;
		default:
			Date time = target.getTime();
			return getSimpleDateFormat(PATTERN_XY_COMMENT).format(time);

			// int dayForWeek = 0;
			// dayForWeek = target.get(Calendar.DAY_OF_WEEK);
			//
			// switch (dayForWeek) {
			// case 1:
			// return Constants.SUNDAY;
			// case 2:
			// return Constants.MONDAY;
			// case 3:
			// return Constants.TUESDAY;
			// case 4:
			// return Constants.WEDNESDAY;
			// case 5:
			// return Constants.THURSDAY;
			// case 6:
			// return Constants.FRIDAY;
			// case 7:
			// return Constants.SATURDAY;
			// default:
			// return null;
			// }

		}
	}

	private static String getNowDateToStr() {
		return getSimpleDateFormat(PATTERN_STANDARD).format(new Date());
	}
	

}
