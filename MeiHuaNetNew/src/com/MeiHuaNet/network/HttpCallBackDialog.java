package com.MeiHuaNet.network;

import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.NewRefreshListView;

/**
 * 
 * @description 带阻塞对话框 的回调抽象类
 * @author lee
 * @time 2013-10-17 下午2:26:28
 * 
 */
public abstract class HttpCallBackDialog implements HttpCallbackListener {

	NewRefreshListView listView;

	public HttpCallBackDialog() {

	}

	public HttpCallBackDialog(NewRefreshListView listView) {
		this.listView = listView;
	}

	/**
	 * 联网请求成功后的回调处理
	 * 
	 * @param result
	 *            从网络获取的数据转换成的字符串
	 */
	public abstract void onSuccessCallBack(String result);

	/**
	 * 联网请求出错时的回调处理
	 * 
	 * @param error
	 */
	public void onErrorCallBack() {
		if (listView != null) {
			listView.onHeadRefreshComplete();
			listView.onBottomRefreshComplete();
		}
		Utils.Toast(Utils.getResString(R.string.network_exception));
		UIManager.cancelAllProgressDialog();
	}

	/**
	 * 联网请求取消时回调处理
	 */
	public void onCancelCallBack() {
		if (listView != null) {
			listView.onHeadRefreshComplete();
			listView.onBottomRefreshComplete();
		}
		UIManager.cancelAllProgressDialog();
	}
}
