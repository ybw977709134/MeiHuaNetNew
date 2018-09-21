package com.MeiHuaNet.utils;

import com.MeiHuaNet.R;
import com.MeiHuaNet.view.TextViewFont;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class HandleSlidTitle {
	
	RelativeLayout relativeLayout ;
	ImageView leftBtn;
	TextViewFont midText;
	ImageView rightBtn;
	public HandleSlidTitle(RelativeLayout relativeLayout){
		this.relativeLayout = relativeLayout ;
		initView();
	}
	
	private void initView(){
		leftBtn = (ImageView) relativeLayout.findViewById(R.id.title_left_btn);
		midText = (TextViewFont) relativeLayout.findViewById(R.id.title_mid_text);
		rightBtn = (ImageView) relativeLayout.findViewById(R.id.title_right_btn);
	}
	/**
	 * 设置顶部显示的内容
	 * @param title 顶部的标题文字
	 * @param id 顶部右边的图片的资源id （右边不显示时，传入-1）
	 */
	public void setTitle(String title, int resId){
		midText.setText(title);
		if(resId==-1){
			rightBtn.setVisibility(View.GONE);
		} else {
			rightBtn.setBackgroundResource(resId);
		}
	}
	
	public void setListener(OnClickListener leftListener, OnClickListener rightListener){
		if(leftListener!=null){
			leftBtn.setOnClickListener(leftListener);
		}
		if(rightListener !=null){
			rightBtn.setOnClickListener(rightListener);
		}
	}

}
