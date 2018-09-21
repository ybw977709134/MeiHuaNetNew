package com.MeiHuaNet.utils;

import com.MeiHuaNet.R;
import com.MeiHuaNet.view.TextViewFont;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class HandleBackTitle {

	RelativeLayout leftBtn;
	TextViewFont midTv;
	ImageView rightBtn;
	RelativeLayout rightLayout;
	TextViewFont rightTv;

	public HandleBackTitle(RelativeLayout relativeLayout) {
		leftBtn = (RelativeLayout) relativeLayout
				.findViewById(R.id.title_back_left);
		midTv = (TextViewFont) relativeLayout.findViewById(R.id.title_back_mid);
		rightBtn = (ImageView) relativeLayout
				.findViewById(R.id.title_back_right);
		rightLayout = (RelativeLayout) relativeLayout
				.findViewById(R.id.title_back_right_layout);
		rightTv = (TextViewFont) relativeLayout
				.findViewById(R.id.title_back_right_text);
	}

	/**
	 * 设置右边按钮(右边时图片时调用这个方法)的布局宽高(这个方法最好在setTitleView()之前调用)
	 * 
	 * @param width
	 *            宽 单位是px
	 * @param height
	 *            高 单位是px
	 */
	public void setrightBtnLayout(int width, int height) {
		rightBtn.getLayoutParams().width = width;
		rightBtn.getLayoutParams().height = height;
	}

	/**
	 * 设置标题栏的显示内容
	 * 
	 * @param title
	 *            标题文字，为null时隐藏
	 * @param resId
	 *            右边图片的资源Id, 为-1时隐藏
	 */
	public void setTitleView(String title, int resId) {
		if (title == null || title.length() == 0) {
			midTv.setVisibility(View.GONE);
		} else {
			midTv.setText(title);
		}
		if (resId == -1) {
			rightBtn.setVisibility(View.GONE);
		} else {
			rightBtn.setBackgroundResource(resId);
		}

	}

	/**
	 * 当右边是文字时，调用这个方法设置右边显示的文字和单击监听事件
	 * 
	 * @param str
	 */
	public void setRightText(String str, OnClickListener onClickListener){
		rightLayout.setVisibility(View.VISIBLE);
		rightTv.setText(str);
		rightLayout.setOnClickListener(onClickListener);
	}

	/**
	 * 设置左右两边按钮的单击事件监听（右边是文字时，监听事件不在这里设置）
	 * 
	 * @param leftListener
	 * @param rightListener
	 */
	public void setListener(OnClickListener leftListener,
			OnClickListener rightListener) {
		if (leftListener != null) {
			leftBtn.setOnClickListener(leftListener);
		}
		if (rightListener != null) {
			rightBtn.setOnClickListener(rightListener);
		}
	}
}
