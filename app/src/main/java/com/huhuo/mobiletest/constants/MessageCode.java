package com.huhuo.mobiletest.constants;

public class MessageCode {
	public static final int NONE = 0;
	public static final int TXT = 1;
	public static final int VOICE = 2;
	public static final int VIDEO = 3;
	public static final int IMAGE = 4;
	public static final int LOCATION = 5;
	public static final int FILE = 6;
	
	/**
	 * 目前支持发送1、文字（包括emoji表情），2、语音，3、图片，4、位置，4种类型
	 * <br>
	 * 私信消息类型数量，如果类型数量有修改，该值必须被修改，否则crash
	 */
	public static final int MESSAGE_TYPE_COUNT = 4;
	

	public static final int SEND_TYPE = 1;
	public static final int RECEIVE_TYPE = 2;

	public static final int STATUS_SENT = 1;
	public static final int STATUS_ARRIVED = 2;
	public static final int STATUS_READ = 3;
	public static final int STATUS_FAILED = 4;
	public static final int STATUS_UNREAD = 5;
	public static final int STATUS_SENDING = 6;
	public static final int STATUS_NET_ERROR = 7;
	public static final int STATUS_SEND_SUCCESS = 8;

	
	public static final long MESSAGE_MAX_COUNT = 10;
	
	
	public static final int RESEND_MSG = 1;
	
	
	public static final int TOP_CHAT = 1;
}
