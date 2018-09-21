package com.MeiHuaNet.activity;

import cn.sharesdk.framework.ShareSDK;

import com.MeiHuaNet.MyApplication;
import com.MeiHuaNet.listener.ActivityCancelObserver;
import com.umeng.analytics.MobclickAgent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;

/**
 * 
 * @description 应用的基础类
 * @author lee
 * @time 2013-8-6 下午4:34:38
 * 
 */
public class BaseActivity extends Activity {

	public static Activity currentActivity;

	protected MyApplication myApplication;
	/**
	 * 活动finish()的观察者，实现finish（）后取消当前activity中的所有网络请求
	 */
	protected ActivityCancelObserver cancelObserver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 友盟统计
		MobclickAgent.onError(this);
		// ShareSDK分享
		ShareSDK.initSDK(this);

		myApplication = (MyApplication) getApplication();
		currentActivity = this;

		cancelObserver = new ActivityCancelObserver();
	}

	@Override
	public void onResume() {
		super.onResume();
		currentActivity = this;
		ShareSDK.initSDK(this);
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
		cancelObserver.notifyCancel();
	}

	GestureDetector gestureDetector;

	/**
	 * 需要添加手势结束activity的页面就设置，否则不需要在这里设置
	 * 
	 * @param gestureDetector
	 */
	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}

	@SuppressLint("Recycle")
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (gestureDetector != null) {
			if (gestureDetector.onTouchEvent(ev)) {
				Log.i("mice", "gesture return true");
				MotionEvent cancelEvent = MotionEvent.obtain(ev);
				cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
				onTouchEvent(cancelEvent);
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}
}
