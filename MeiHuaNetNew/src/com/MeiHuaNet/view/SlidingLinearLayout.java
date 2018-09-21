package com.MeiHuaNet.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class SlidingLinearLayout extends LinearLayout {

//	/* 手势动作最开始时的x坐标 */
//	private float lastMotionX = -1;

	public SlidingLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (SlidingMenu.menuOut ) {
			//这里返回true,表示父视图拦截了这次触摸事件，子视图接受不到这次触摸事件了
			return true;
		} else {
			//子视图可以继续处理这次事件
			return false;
		}
	}

}
