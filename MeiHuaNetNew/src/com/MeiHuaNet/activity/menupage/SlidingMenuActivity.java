package com.MeiHuaNet.activity.menupage;

import com.MeiHuaNet.R;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.activity.infomation.InformationPage;
import com.MeiHuaNet.activity.menupage.MenuPage.MenuType;
import com.MeiHuaNet.utils.DialogUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.SlidingMenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ViewFlipper;

/**
 * 
 * @description 程序的主体侧边栏框架activity
 * @author lee
 * @createTime 2013-9-2下午2:45:25
 * 
 */
public class SlidingMenuActivity extends BaseActivity {

//	public static MyHandler handler;
	private SlidingMenu mSlidingMenu;
	private RelativeLayout leftMenulayout;
	private MenuPage menuPage;
	private View menuView;
	public static MenuPage.MenuType curType;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.menu_scroll_view);
		mSlidingMenu = (SlidingMenu) findViewById(R.id.mScrollView);
		leftMenulayout = (RelativeLayout) findViewById(R.id.leftMenuView);

//		handler = new MyHandler(this) {
//			@Override
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 0:
//					mActivity.get().finish();
//					break;
//				}
//			}
//		};
		setMainView();

		curType = MenuType.information;
		// sendUDID();
//		UmengUpdateAgent.update(this);
	}

	private void setMainView() {
		ViewFlipper vf = new ViewFlipper(this);
		View leftView = LayoutInflater.from(this).inflate(
				R.layout.layout_shadow, null);
		// 初始化主体部分显示的视图
		InformationPage informationPage = new InformationPage(mSlidingMenu,
				this);
		vf.addView(informationPage.getView());

		leftView.setBackgroundColor(Color.TRANSPARENT);
		View[] children = new View[] { leftView, vf };
		setMenuView();
		mSlidingMenu.initViews(children, new SlidingMenu.SizeCallBackForMenu(
				this), menuView);
	}

	private void setMenuView() {
		menuPage = new MenuPage(mSlidingMenu, SlidingMenuActivity.this);
		menuView = menuPage.getView();
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		int margin = dip2px(this, 50);
		params.rightMargin = margin;// 这个值也是主体布局滑动到右边时在屏幕上剩余显示的宽度
		menuView.setLayoutParams(params);
		leftMenulayout.addView(menuView);
	}

	/**
	 * 将dp转换成对应的px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (SlidingMenu.menuOut == false) {
				this.mSlidingMenu.clickMenuBtn();
			} else {
				DialogUtils.createDialog(SlidingMenuActivity.this,
						Utils.getResString(R.string.sure_exit_app),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								exitApp();
							}
						}, null);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data){
	// menuPage.onActivityResult(requestCode, resultCode, data);
	// }

	private void exitApp() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		System.exit(0);
	}

	// /**
	// * 登录成功后，将Jpush的UDID和登录的用户名发送到服务器绑定在一起
	// */
	// private void sendUDID(){
	// String udid = spfUtils.getString(SharedPreferenceUtils.UDID);
	// String userName = spfUtils.getString(SharedPreferenceUtils.USER_NAME);
	//
	// WebServiceParams params = new WebServiceParams(SlidingMenuActivity.this,
	// false);
	//
	// WebService.requestAsynHttp(params, null, false);
	// }

//	static class MyHandler extends Handler {
//		WeakReference<BaseActivity> mActivity;
//
//		public MyHandler(BaseActivity activity) {
//			mActivity = new WeakReference<BaseActivity>(activity);
//		}
//	}

}
