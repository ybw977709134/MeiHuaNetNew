package com.MeiHuaNet.activity.menupage;

import com.MeiHuaNet.R;
import com.MeiHuaNet.activity.acepack.AcepackPage;
import com.MeiHuaNet.activity.event.EventPage;
import com.MeiHuaNet.activity.infomation.InformationPage;
import com.MeiHuaNet.activity.login.LoginActivity;
import com.MeiHuaNet.activity.originality.OriginalityPage;
import com.MeiHuaNet.activity.resource.ResourcePage;
import com.MeiHuaNet.activity.setting.SettingPage;
import com.MeiHuaNet.entity.LoginJsonObject;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.MenuLinearLayout;
import com.MeiHuaNet.view.SlidingMenu;
import com.MeiHuaNet.view.TextViewFont;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MenuPage implements OnClickListener {

	private Context mContext;
	private View me;
	private SlidingMenu mSlidingMenu;
	private LinearLayout unLoginLayout;
	private LinearLayout loginLayout;
	private TextViewFont userNameView;
	private ImageView vipImg;
	private ImageView settingBtn;
	private ImageView loginBtn;
	private RelativeLayout informationLayout;
	private RelativeLayout acePackLayout;
	private RelativeLayout resourceLayout;
	private RelativeLayout eventLayout;
	private RelativeLayout originalityLayout;
	private MenuLinearLayout allLayout;
	
	public static Handler handler;
	
	public static enum MenuType{
		information,acepack,resource,event,recruit,originality;
	}

	@SuppressLint("HandlerLeak")
	public MenuPage(SlidingMenu slidingMenu, Context context) {
		mSlidingMenu = slidingMenu;
		mContext = context;

		me = View.inflate(slidingMenu.getContext(), R.layout.layout_menu, null);

		initView();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				//登录或登出后，修改显示的内容
				setLoginLayout();
			}
		};
	}

	public View getView() {
		return me;
	}

	private void initView() {
		allLayout = (MenuLinearLayout) me.findViewById(R.id.menu_all_layout);
		unLoginLayout = (LinearLayout) me.findViewById(R.id.menu_no_login);
		loginLayout = (LinearLayout) me.findViewById(R.id.menu_login);
		userNameView = (TextViewFont) me.findViewById(R.id.menu_username);
		vipImg = (ImageView) me.findViewById(R.id.menu_is_vip_img);
		settingBtn = (ImageView) me.findViewById(R.id.menu_setting);
		loginBtn = (ImageView) me.findViewById(R.id.menu_login_btn);
		informationLayout = (RelativeLayout) me
				.findViewById(R.id.menu_infomation_item);
		acePackLayout = (RelativeLayout) me
				.findViewById(R.id.menu_ace_pack_item);
		resourceLayout = (RelativeLayout) me
				.findViewById(R.id.menu_resource_item);
		eventLayout = (RelativeLayout) me.findViewById(R.id.menu_event_item);
//		recruitLayout = (RelativeLayout) me
//				.findViewById(R.id.menu_recruit_item);
		originalityLayout = (RelativeLayout) me
				.findViewById(R.id.menu_originality_item);

		setLoginLayout();
		
		loginBtn.setOnClickListener(this);
		settingBtn.setOnClickListener(this);
		informationLayout.setOnClickListener(this);
		acePackLayout.setOnClickListener(this);
		resourceLayout.setOnClickListener(this);
		eventLayout.setOnClickListener(this);
		originalityLayout.setOnClickListener(this);
		allLayout.setLongClickable(true);
		allLayout.setOnTouchEvent(Utils.getOntouchListener(mSlidingMenu));
	}
	
	/**
	 * 设置菜单顶部左边的登录布局的显示内容
	 */
	private void setLoginLayout(){
		LoginJsonObject loginJsonObject = Utils.isLogin(mContext);
		String username = loginJsonObject == null ? "" : loginJsonObject.UserName;
		String isVip = loginJsonObject == null ? "" : loginJsonObject.IsVIP;
		if (username == null || username.length() == 0) {
			unLoginLayout.setVisibility(View.VISIBLE);
			loginLayout.setVisibility(View.GONE);
		} else {
			// 用户登录了账号
			unLoginLayout.setVisibility(View.GONE);
			loginLayout.setVisibility(View.VISIBLE);
			userNameView.setText(username);
			if ("true".equalsIgnoreCase(isVip)) {
//				// 用户是vip用户
				vipImg.setVisibility(View.VISIBLE);
			} else {
				vipImg.setVisibility(View.GONE);
			}

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_login_btn:
			Intent intent = new Intent();
			intent.setClass(mContext, LoginActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.menu_setting:
			SettingPage settingPage = new SettingPage(mSlidingMenu, mContext);
			mSlidingMenu.setBody(settingPage.getView());
			break;
		case R.id.menu_infomation_item:
			InformationPage informationPage = new InformationPage(mSlidingMenu, mContext);
			mSlidingMenu.setBody(informationPage.getView());
			break;
		case R.id.menu_ace_pack_item:
			AcepackPage acepackPage = new AcepackPage(mSlidingMenu, mContext);
			mSlidingMenu.setBody(acepackPage.getView());
			break;
		case R.id.menu_resource_item:
			ResourcePage resourcePage = new ResourcePage(mSlidingMenu, mContext);
			mSlidingMenu.setBody(resourcePage.getView());
			break;
		case R.id.menu_event_item:
			EventPage eventPage = new EventPage(mSlidingMenu, mContext);
			mSlidingMenu.setBody(eventPage.getView());
			break;
//		case R.id.menu_recruit_item:
//			RecruitPage recruitPage = new RecruitPage(mSlidingMenu, mContext);
//			mSlidingMenu.setBody(recruitPage.getView());
//			break;
		case R.id.menu_originality_item:
			OriginalityPage originalityPage = new OriginalityPage(mSlidingMenu, mContext);
			mSlidingMenu.setBody(originalityPage.getView());
			break;
		}
	}
	
}
