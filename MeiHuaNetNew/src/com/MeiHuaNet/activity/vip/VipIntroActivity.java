package com.MeiHuaNet.activity.vip;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.MeiHuaNet.R;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.Utils;

/**
 *
 * @description vip会员权益介绍页面 
 * @author lee
 * @time  2013-12-25 下午1:18:28
 *
 */
public class VipIntroActivity extends BaseActivity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_vip_intro);
		
		initView();
	}
	
	private void initView(){
		initTitle();
		LinearLayout layout = (LinearLayout) findViewById(R.id.vip_intro_all_layout);
		GestureDetectorUtil.setGestureDetector(this, layout, null);
		
	}
	private void initTitle(){
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView(Utils.getResString(R.string.vip_member_equity), -1);
		handleBackTitle.setListener(new OnBackBtnListener(this), null);
	}
}
