package com.MeiHuaNet.utils;

import android.app.Dialog;
import android.content.Context;

/**
 * 
 * @description 联网请求时使用的对话框
 * @author lee
 * @createTime 2013-7-26下午5:54:58
 * 
 */
public class RequestDialog extends Dialog {

	RequestThread t;

	public RequestDialog(Context context) {
		super(context);
	}

	public RequestDialog(Context context, int theme) {
		super(context, theme);
		
	}

	public RequestDialog(Context context, int theme, RequestThread t) {
		super(context, theme);
		this.t = t;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (t != null) {
//			t.obsever.is = true;
			t.obsever.notifyCancel();
			t.interrupt();
		}
	}

}
