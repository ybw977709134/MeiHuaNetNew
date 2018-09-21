package com.MeiHuaNet.activity.setting;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.activity.login.LoginActivity;
import com.MeiHuaNet.activity.menupage.MenuPage;
import com.MeiHuaNet.listener.OnSlidingBtnClickListener;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.HandleSlidTitle;
import com.MeiHuaNet.utils.SharedPreferenceUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.SlidingMenu;
import com.MeiHuaNet.view.TextViewFont;

public class SettingPage implements OnClickListener {

	public static MyHandler handler;
	private SlidingMenu mSlidingMenu;
	private Context mContext;
	private View me;
	private TextViewFont userNameTv;
	private ImageView vipImageView;
	private RelativeLayout loginLayout;
	// private TextViewFont fontTv;
	private RelativeLayout fontLayout;
	private RelativeLayout authorizeLayout;
	private TextViewFont advanceLayout;
	private TextViewFont aboutUsLayout;
	private SharedPreferenceUtils spfUtils;

	public SettingPage(SlidingMenu slidingMenu, Context context) {
		mSlidingMenu = slidingMenu;
		mContext = context;
		spfUtils = SharedPreferenceUtils.getInstance(mContext,
				SharedPreferenceUtils.FILE_USER_INFO);

		me = View.inflate(context, R.layout.layout_setting, null);
		initView();

		handler = new MyHandler(this) {
			@Override
			public void handleMessage(Message msg) {
				mActivity.get().setUsernameLayout();
			}
		};
	}

	public View getView() {
		return me;
	}

	private void initView() {
		initTitle();

		userNameTv = (TextViewFont) me.findViewById(R.id.set_login_tv);
		vipImageView = (ImageView) me.findViewById(R.id.set_vip_img);
		loginLayout = (RelativeLayout) me
				.findViewById(R.id.set_login_right_layout);
		fontLayout = (RelativeLayout) me
				.findViewById(R.id.set_content_font_layout);
		// fontTv = (TextViewFont) me.findViewById(R.id.set_cotent_font_tv);
		authorizeLayout = (RelativeLayout) me
				.findViewById(R.id.set_authorize_layout);
		advanceLayout = (TextViewFont) me
				.findViewById(R.id.set_advance_feedback);
		aboutUsLayout = (TextViewFont) me.findViewById(R.id.set_about_us);
		setUsernameLayout();

		loginLayout.setOnClickListener(this);
		fontLayout.setOnClickListener(this);
		authorizeLayout.setOnClickListener(this);
		advanceLayout.setOnClickListener(this);
		aboutUsLayout.setOnClickListener(this);
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) me
				.findViewById(R.id.title_relative_layout);
		HandleSlidTitle handleSlidTitle = new HandleSlidTitle(relativeLayout);
		handleSlidTitle.setTitle(Utils.getResString(R.string.setting), -1);
		handleSlidTitle.setListener(
				new OnSlidingBtnClickListener(mSlidingMenu), null);
	}

	/**
	 * 设置用户名这项的显示内容
	 */
	private void setUsernameLayout() {
		if (userNameTv == null) {
			return;
		}
		if (Utils.isLogin(mContext) != null) {
			String username = spfUtils
					.getString(SharedPreferenceUtils.USER_NAME);
			String isVip = spfUtils.getString(SharedPreferenceUtils.IS_VIP);
			userNameTv.setText(username);
			if ("true".equalsIgnoreCase(isVip)) {
				vipImageView.setVisibility(View.VISIBLE);
			} else {
				vipImageView.setVisibility(View.GONE);
			}
		} else {
			userNameTv.setText(Utils.getResString(R.string.unsign_up));
			vipImageView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.set_login_right_layout:
			if (Utils.isLogin(mContext) != null) {
				spfUtils.clear();
				FileUtils.delfile(FileUtils.sdDir + FileUtils.dir,
						Constants.fileName_Userinfo);
				setUsernameLayout();
				MenuPage.handler.obtainMessage().sendToTarget();
			} else {
				intent.setClass(mContext, LoginActivity.class);
				mContext.startActivity(intent);
			}
			break;
		case R.id.set_content_font_layout:
			break;
		case R.id.set_authorize_layout:
			intent.setClass(mContext, AuthManagerActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.set_advance_feedback:
			intent.setClass(mContext, FeedbackActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.set_about_us:
			intent.setClass(mContext, AboutUsActivity.class);
			mContext.startActivity(intent);
			break;
		}
	}

	public static class MyHandler extends Handler {
		WeakReference<SettingPage> mActivity;

		public MyHandler(SettingPage activity) {
			mActivity = new WeakReference<SettingPage>(activity);
		}
	}
}
