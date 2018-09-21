package com.MeiHuaNet.utils;

import java.util.ArrayList;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.entity.LoginJsonObject;
import com.MeiHuaNet.view.SlidingMenu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

/**
 * 
 * @description 工具类
 * @author lee
 * @time 2013-8-6 下午5:31:38
 * 
 */
public class Utils {

	/**
	 * 传入Strings.xml中的资源Id，获取对应的字符窜
	 * 
	 * @param id
	 * @return
	 */
	public static String getResString(int id) {
		return BaseActivity.currentActivity.getResources().getString(id);
	}

	public static void Toast(String message) {
		if (BaseActivity.currentActivity != null && message != null
				&& message.length() > 0) {
			Toast.makeText(BaseActivity.currentActivity, message,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	 * @param message
	 *            提示消息
	 * @param type
	 *            提示显示的时间的长短（1：显示时间长 2：显示时间短）
	 */
	public static void Toast(String message, int type) {
		if (BaseActivity.currentActivity != null && message != null
				&& message.length() > 0) {
			if (type == 1) {
				Toast.makeText(BaseActivity.currentActivity, message,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(BaseActivity.currentActivity, message,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static void log(String message) {
		if (message != null && message.length() > 0) {
			Log.i("meihuanet", message);
		}
	}

	// 出错时，给用户的提示信息
	public static void showError(final String msg) {
		if (BaseActivity.currentActivity != null) {
			BaseActivity.currentActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Utils.Toast(msg);
				}
			});
		}
	}

	public static int getAndroidSDKVersion() {
		int version = 8;
		try {
			version = android.os.Build.VERSION.SDK_INT;
			Utils.log("android version is : " + version);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 
	 * @param jsonStr
	 *            解析的json字符串
	 * @param t
	 *            javabean的泛型
	 * @return 返回普通对象
	 */
	public static <T> T parseJson(String jsonStr, Class<T> t) {
		T object = null;
		try {
			object = JsonTranslator.deserialize(jsonStr, t);
		} catch (SerializeException e) {
			e.printStackTrace();
			showError(Utils.getResString(R.string.parse_exp));
		}
		return object;
	}

	/**
	 * 
	 * @param jsonStr
	 *            解析的json字符串
	 * @param t
	 *            javabean的泛型
	 * @return
	 */
	public static <T> ArrayList<T> parseJsonArray(String jsonStr, Class<T> t) {
		ArrayList<T> object = null;
		try {
			object = JsonTranslator.deserializeArray(jsonStr, t);
		} catch (SerializeException e) {
			e.printStackTrace();
			showError(Utils.getResString(R.string.parse_exp));
		}
		return object;
	}

	public static void sendEmail(Context context, String title, String bodyStr,
			String sendEmailStr) {
		String mailId = sendEmailStr == null ? "" : sendEmailStr;
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
				"mailto", mailId, null));
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, bodyStr);
		context.startActivity(emailIntent);
	}

	/**
	 * 判断用户是否已经登录了,如果已经登录，就返回用户的信息，否则返回null
	 * 
	 * @param context
	 */
	public static LoginJsonObject isLogin(Context context) {
		SharedPreferenceUtils spf = SharedPreferenceUtils.getInstance(context,
				SharedPreferenceUtils.FILE_USER_INFO);
		LoginJsonObject loginJsonObject = FileUtils.fetchDataFromFile(context,
				LoginJsonObject.class, Constants.fileName_Userinfo);
		if (TextUtils.isEmpty(spf.getString(SharedPreferenceUtils.USER_NAME))) {
			if (loginJsonObject != null) {
				spf.put(SharedPreferenceUtils.USER_NAME,
						loginJsonObject.UserName);
				spf.put(SharedPreferenceUtils.IS_VIP, loginJsonObject.IsVIP);
			}
		}
		return loginJsonObject;
	}

	/**
	 * android获取一个用于打开视频文件的intent
	 * 
	 * @param url
	 *            视频的观看地址
	 * @return
	 */
	public static Intent getVideoFileIntent(String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setType("video/*");
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	/**
	 * 菜单栏的布局的触摸监听，实现在菜单栏上手势横滑时控制右边主体部分的滑动
	 * 
	 * @param mSlidingMenu
	 * @return
	 */
	public static OnTouchListener getOntouchListener(
			final SlidingMenu mSlidingMenu) {
		OnTouchListener onTouchListener = new OnTouchListener() {
			private int type = 0;
			private int originX = -1;
			private int originY = -1;

			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (ev.getAction() == MotionEvent.ACTION_DOWN) {
						originX = (int) ev.getRawX();
						originY = (int) ev.getRawY();
					}
					type = 0;
					break;
				case MotionEvent.ACTION_MOVE:
					int difX = (int) Math.abs((ev.getRawX() - originX));
					int difY = (int) Math.abs(ev.getRawY() - originY);
					if (type == 1) {
						mSlidingMenu.scrollTo(
								0 - (int) ((ev.getRawX() - originX)),
								mSlidingMenu.getScrollY());
						return true;
					} else if (type == 2) {
						return false;
					}
					if (type == 0 && (difX > 15 || difY > 15)) {

						if (difX > difY) {
							type = 1;
							mSlidingMenu.scrollTo(
									0 - (int) ((ev.getRawX() - originX)),
									mSlidingMenu.getScrollY());
							return true;
						} else {
							type = 2;
							return false;
						}
					} else {
						return true;
					}
				case MotionEvent.ACTION_CANCEL:
					originX = -1;
					originY = -1;
					type = 0;
					break;
				case MotionEvent.ACTION_UP:
					originX = -1;
					originY = -1;
					if (type == 1) {
						type = 0;
						mSlidingMenu.menuSlide();
						return true;
					}
					type = 0;
					break;
				}
				return false;
			}
		};
		return onTouchListener;
	}

	public static OnTouchListener getBodyOntouchListener(
			final SlidingMenu mSlidingMenu) {

		OnTouchListener onTouchListener = new OnTouchListener() {

			private int type = 0;
			private int originX = -1;
			private int originY = -1;

			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (ev.getAction() == MotionEvent.ACTION_DOWN) {
						originX = (int) ev.getRawX();
						originY = (int) ev.getRawY();
					}
					type = 0;
					break;
				case MotionEvent.ACTION_MOVE:
					int difX = (int) Math.abs((ev.getRawX() - originX));
					int difY = (int) Math.abs(ev.getRawY() - originY);
					if (type == 1) {
						mSlidingMenu.requestDisallowInterceptTouchEvent(false);
						return true;
					} else if (type == 2) {
						mSlidingMenu.requestDisallowInterceptTouchEvent(true);
						return false;
					}
					if (type == 0 && (difX > 15 || difY > 15)) {

						if (difX > difY) {
							type = 1;
							return true;
						} else {
							type = 2;
							return false;
						}
					} else {
						return true;
					}
				case MotionEvent.ACTION_CANCEL:
					originX = -1;
					originY = -1;
					type = 0;
					mSlidingMenu.requestDisallowInterceptTouchEvent(false);
					break;
				case MotionEvent.ACTION_UP:
					originX = -1;
					originY = -1;
					if (type == 1) {
						type = 0;
						return true;
					}
					type = 0;
					mSlidingMenu.requestDisallowInterceptTouchEvent(false);
					break;
				}
				return false;
			}
		};
		return onTouchListener;
	}

}
