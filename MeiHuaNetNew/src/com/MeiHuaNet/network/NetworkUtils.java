package com.MeiHuaNet.network;


import com.MeiHuaNet.R;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.CustomDialog.Builder;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *
 * @description 判断当前网络是否正常的类
 * @author lee
 * @createTime 2013-7-1下午3:39:21
 *
 */
public class NetworkUtils {
	private static Builder builder;

	public static boolean isAlived() {
		try {
			// 获取手机的所有连接管理对象（包括wifi,net等）
			ConnectivityManager connectivity = (ConnectivityManager) BaseActivity.currentActivity
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接的管理
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info == null || !info.isConnectedOrConnecting()) {
					if (builder == null) {
						builder = new Builder(BaseActivity.currentActivity);
						builder.setTitle(Utils.getResString(R.string.prompt));
						builder.setMessage(Utils.getResString(R.string.check_network));
						builder.setPositiveButton(BaseActivity.currentActivity.getResources()
								.getString(R.string.sure), new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										//这里设为空，如果不为空，程序不会再new一个新的builder,就不会在一个页面中弹出多个对话框了
										builder = null;
										dialog.dismiss();
									}
								});
						builder.create().show();
					} else {
						builder.create().show();
					}
					return false;
				}
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
