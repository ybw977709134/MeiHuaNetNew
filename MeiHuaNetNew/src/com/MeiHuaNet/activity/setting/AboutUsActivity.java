package com.MeiHuaNet.activity.setting;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.MeiHuaNet.R;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.StringUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.TextViewFont;

public class AboutUsActivity extends BaseActivity{

	TextViewFont contactUs;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_about_us);
		
		initView();
	}
	
	private void initView(){
		initTitle();
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.about_us_all_layout);
		GestureDetectorUtil.setGestureDetector(this, layout, null);
		
		contactUs = (TextViewFont) findViewById(R.id.contact_us);
		contactUs.setText(StringUtils.handleStr(Utils.getResString(R.string.contact_us)));
	}
	
	private void initTitle(){
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView(Utils.getResString(R.string.about_us), -1);
		handleBackTitle.setListener(new OnBackBtnListener(this), null);
	}
	
}
