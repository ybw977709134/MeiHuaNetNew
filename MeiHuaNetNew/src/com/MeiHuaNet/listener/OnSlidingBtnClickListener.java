package com.MeiHuaNet.listener;

import android.view.View;
import android.view.View.OnClickListener;

import com.MeiHuaNet.view.SlidingMenu;

public class OnSlidingBtnClickListener implements OnClickListener{

	SlidingMenu slidingMenu ;
	public OnSlidingBtnClickListener(SlidingMenu slidingMenu){
		this.slidingMenu = slidingMenu;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.slidingMenu.clickMenuBtn();
	}
}
