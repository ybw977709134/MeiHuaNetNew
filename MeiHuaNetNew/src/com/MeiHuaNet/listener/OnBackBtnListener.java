package com.MeiHuaNet.listener;

import com.MeiHuaNet.activity.BaseActivity;

import android.view.View;
import android.view.View.OnClickListener;

public class OnBackBtnListener implements OnClickListener{

	BaseActivity activity;
	public OnBackBtnListener(BaseActivity activity){
		this.activity = activity;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.activity.finish();
	}

}
