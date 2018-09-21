package com.MeiHuaNet.utils;

import com.MeiHuaNet.R;
import com.MeiHuaNet.view.CustomDialog.Builder;

//import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

/**
 * 提供不同样式的对话框
 * 
 * @author Administrator
 * 
 */
public class DialogUtils {

	/**
	 * 
	 * @param context
	 * @param message
	 *            对话框显示的内容
	 * @param positiveStr
	 *            确定按钮上的文字
	 * @param positiveListener
	 *            确定按钮的点击监听事件
	 */
	public static void createSureDialog(Context context, String message,
			String positiveStr, OnClickListener positiveListener) {
		Builder builder = new Builder(context);
		builder.setTitle(Utils.getResString(R.string.prompt));
		builder.setMessage(message);
		builder.setPositiveButton(positiveStr, positiveListener);
		builder.create().show();
	}

	/**
	 * 
	 * @param context
	 * @param message
	 *            对话框显示的消息
	 * @param positiveListener
	 *            确定按钮的监听事件
	 * @param negativeListener
	 *            取消按钮的监听事件
	 */
	public static void createDialog(Context context, String message,
			OnClickListener positiveListener, OnClickListener negativeListener) {
		Builder builder = new Builder(context);
		builder.setTitle(Utils.getResString(R.string.prompt));
		builder.setMessage(message);
		builder.setPositiveButton(Utils.getResString(R.string.sure),
				positiveListener);
		builder.setNegativeButton(Utils.getResString(R.string.cancel),
				negativeListener);
		builder.create().show();
	}

	/**
	 * 
	 * @param context
	 * @param message
	 *            对话框显示的消息
	 * @param positiveStr
	 *            确定按钮上显示的文字
	 * @param negativeStr
	 *            取消按钮上显示的文字
	 * @param positiveListener
	 *            确定按钮的监听事件
	 * @param negativeListener
	 *            取消按钮的监听事件
	 */
	public static void createDialog(Context context, String message,
			String positiveStr, String negativeStr,
			OnClickListener positiveListener, OnClickListener negativeListener) {
		Builder builder = new Builder(context);
		builder.setTitle(Utils.getResString(R.string.prompt));
		builder.setMessage(message);
		builder.setPositiveButton(positiveStr,
				positiveListener);
		builder.setNegativeButton(negativeStr,
				negativeListener);
		builder.create().show();
	}

}
