package com.MeiHuaNet.utils;

import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.BaseActivity;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 
 * @description 为页面添加手势返回上一页面的工具类
 * @author lee
 * @createTime 2013-9-12下午2:05:16
 * 
 */
public class GestureDetectorUtil {

	/**
	 * 
	 * @description 设置页面的手势监听，右滑时关闭页面
	 * @param activity
	 *            要添加手势监听的页面activity
	 * @param layout
	 *            页面的最外层的布局(LinearLayout)
	 * @param onFlingListener
	 *            手势滑动成功后，在onfling事件中自己去处理（默认的是结束当前的activity），可为null
	 */
	public static void setGestureDetector(BaseActivity activity,
			LinearLayout layout, OnFlingListener onFlingListener) {
		GestureDetector gestureDetector = new GestureDetector(
				getOnGestureListener(activity, onFlingListener));
		activity.setGestureDetector(gestureDetector);
		layout.setLongClickable(true);
		layout.setOnTouchListener(getOnTouchListener(gestureDetector));
	}

	/**
	 * 
	 * @param activity
	 *            要添加手势监听的页面activity
	 * @param layout
	 *            页面的最外层的布局（RelativeLayout）
	 * @param onFlingListener
	 *            手势滑动成功后，在onfling事件中自己去处理（默认的是结束当前的activity）,可为null
	 */
	public static void setGestureDetector(BaseActivity activity,
			RelativeLayout layout, OnFlingListener onFlingListener) {
		GestureDetector gestureDetector = new GestureDetector(
				getOnGestureListener(activity, onFlingListener));
		activity.setGestureDetector(gestureDetector);
		layout.setLongClickable(true);
		layout.setOnTouchListener(getOnTouchListener(gestureDetector));
	}

	/**
	 * 手势监听器
	 * 
	 * @param activity
	 *            手势监听要结束的activity
	 * @return
	 */
	public static OnGestureListener getOnGestureListener(
			final BaseActivity activity, final OnFlingListener onFlingListener) {
		return new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {

			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {

			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if (e2.getX() - e1.getX() > 100
						&& Math.abs(e2.getY() - e1.getY()) < 100) {
					UIManager.cancelAllProgressDialog();
					if (onFlingListener != null) {
						onFlingListener.onFling();
					} else {
						activity.finish();
					}
					return true;
				}
				return false;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		};
	}

	/**
	 * 将touchevent传给手势监听
	 * 
	 * @param gestureDetector
	 * @return
	 */
	public static OnTouchListener getOnTouchListener(
			final GestureDetector gestureDetector) {
		return new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};
	}

	public static interface OnFlingListener {
		void onFling();
	}
}
