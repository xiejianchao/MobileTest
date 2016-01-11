package com.huhuo.mobiletest.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class HttpUtils {

	private static final AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象
	static {
		client.setTimeout(11000); // 设置链接超时，如果不设置，默认为10s
	}

	public static void get(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
	{
		client.get(urlString, res);
	}

	public static void get(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) // url里面带参数
	{
		client.get(urlString, params, res);
	}

	public static void get(String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
	{
		client.get(urlString, res);
	}

	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
	{
		client.get(urlString, params, res);
	}

	public static void get(String uString, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
	{
		client.get(uString, bHandler);
	}
	public static void get(String uString, FileAsyncHttpResponseHandler fileHandler) // 下载文件到指定目录
	{
		client.get(uString, fileHandler);
	}
	public static void get(String uString, TextHttpResponseHandler textHandler) // 返回结果为字符串的请求
	{
		client.get(uString, textHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}

	public static void cancelTask(Context context, boolean bool) {
		client.cancelRequests(context, bool);
	}

	public static void cancelTask(Context context) {
		cancelTask(context, true);
	}

}