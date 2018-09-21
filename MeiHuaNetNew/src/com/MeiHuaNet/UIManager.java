package com.MeiHuaNet;


import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.utils.RequestDialog;
import com.MeiHuaNet.utils.RequestThread;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

/**
 *
 * @description  管理应用中的加载对话框
 * @author lee
 * @time  2013-8-6 下午4:36:05
 *
 */
public class UIManager {

	private static RequestDialog progressDialog;
	private static ThreadTimeout threadTimeout;
	/* 请求网络的开始时间，也就是进度条对话框的弹出时间 */
	private static long sendStartTime;

	/**
	 * 请求网络时，调用这个方法显示进度条对话框阻塞界面，同时提醒用户在加载数据中
	 * 
	 * @param str
	 */
	public static void showProgressDialog(String str, boolean isCancel) {
		sendStartTime = System.currentTimeMillis();
		if (threadTimeout == null) {
			threadTimeout = new ThreadTimeout(handlerMessage);
			Thread t = new Thread(threadTimeout);
			t.start();
		}
		try {
			progressDialog = new RequestDialog(BaseActivity.currentActivity,
					R.style.dialog_notitle);
			View view = LayoutInflater.from(BaseActivity.currentActivity)
					.inflate(R.layout.layout_progressdialog, null);
			progressDialog.setContentView(view);
			if(isCancel){
				progressDialog.setCancelable(true);
			} else {
				progressDialog.setCancelable(false);
			}
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 请求网络时，调用这个方法显示进度条对话框阻塞界面，同时提醒用户在加载数据中，可以取消，取消的同时对应的请求线程也会停止
	 * 
	 * @param str 对话框提示文字
	 * @param curThread 与对话框对应的异步线程
	 * @param isCancel 对话框是否能通过返回键取消
	 */
	public static void showProgressDialog(String str,RequestThread curThread, boolean isCancel) {
		sendStartTime = System.currentTimeMillis();
		if (threadTimeout == null) {
			threadTimeout = new ThreadTimeout(handlerMessage);
			Thread t = new Thread(threadTimeout);
			t.start();
		}
		try {
			progressDialog = new RequestDialog(BaseActivity.currentActivity,
					R.style.dialog_notitle,curThread);
			View view = LayoutInflater.from(BaseActivity.currentActivity)
					.inflate(R.layout.layout_progressdialog, null);
			progressDialog.setContentView(view);
			if(isCancel){
				progressDialog.setCancelable(true);
			} else {
				progressDialog.setCancelable(false);
			}
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class ThreadTimeout extends Thread {

		private Handler handler;

		public ThreadTimeout(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			while (threadTimeout == this) {
				try {
					if (progressDialog != null
							&& progressDialog.isShowing()
							&& System.currentTimeMillis() - sendStartTime >= 60 * 1000
							&& sendStartTime > 0) {
						Message msg = new Message();
						msg.what = cancelDialog;
						handler.sendMessage(msg);
						sendStartTime = 0;
					} else {
						Thread.sleep(10 * 1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void cancelAllProgressDialog() {
		if (UIManager.progressDialog != null
				&& UIManager.progressDialog.isShowing()) {
			UIManager.progressDialog.dismiss();
		}
	}

	/* 从网络上加载完数据，或超过一定时间请求的数据还是没有加载完，取消进度条对话框 */
	public static final int cancelDialog = 1;
	public static Handler handlerMessage = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case cancelDialog:
				cancelAllProgressDialog();
				break;
			}
		}
	};
}
