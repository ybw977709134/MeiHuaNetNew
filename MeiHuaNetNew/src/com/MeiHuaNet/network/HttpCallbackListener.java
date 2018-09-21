package com.MeiHuaNet.network;


/**
 * http请求完成后的回调接口
 * @author Administrator
 *
 */
public interface HttpCallbackListener {

	/**
	 * 联网请求成功后的回调处理
	 * @param result 从网络获取的数据转换成的字符串
	 */
	public void onSuccessCallBack(String result);
	
	/**
	 * 联网请求出错时的回调处理
	 * @param error
	 */
	public void onErrorCallBack();
	
	/**
	 * 联网请求取消时回调处理
	 */
	public void onCancelCallBack();
}
