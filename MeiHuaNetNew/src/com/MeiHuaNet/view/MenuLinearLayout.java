package com.MeiHuaNet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 *
 * @description 菜单栏的侧边栏中使用，更方便实现侧边栏触摸时，主体部分回到界面 
 * @author lee
 * @time  2013-9-11 上午10:04:16
 *
 */
public class MenuLinearLayout extends LinearLayout {

	private OnTouchListener onTouchListener;

	public MenuLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MenuLinearLayout(Context context, AttributeSet attr) {
		super(context, attr);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (onTouchListener.onTouch(null, ev)){
			//返回true，让子视图不能继续接收触摸事件，防止子视图的单击事件触发
			return true;
		}
			
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		return super.onInterceptTouchEvent(ev);
	}

	public void setOnTouchEvent(OnTouchListener onTouchListener) {
		this.setOnTouchListener(onTouchListener);
		this.onTouchListener = onTouchListener;
	}

}
