package com.MeiHuaNet.activity.login;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.activity.menupage.MenuPage;
import com.MeiHuaNet.activity.setting.SettingPage;
import com.MeiHuaNet.entity.LoginJsonObject;
import com.MeiHuaNet.listener.LoginSuccessObserver;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.network.HttpCallbackListener;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.SharedPreferenceUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.TextViewFont;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 
 * @description 用户登录的页面
 * @author lee
 * @time 2013-11-26 下午3:35:16
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	public static LoginSuccessObserver loginSuccessObserver;
	TextViewFont registTv;
	EditText userEdit;
	EditText passwordEdit;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_login);

		initView();
	}

	private void initView() {
		initTitle();

		LinearLayout layout = (LinearLayout) findViewById(R.id.login_all_layout);
		GestureDetectorUtil.setGestureDetector(this, layout, null);
		
		registTv = (TextViewFont) findViewById(R.id.login_register);
		userEdit = (EditText) findViewById(R.id.login_usernama_edit);
		passwordEdit = (EditText) findViewById(R.id.login_password_edit);

		registTv.setOnClickListener(this);
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView("", -1);
		handleBackTitle.setRightText(Utils.getResString(R.string.login), this);
		handleBackTitle.setListener(new OnBackBtnListener(this), null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_right_layout:
			login();
			break;
		case R.id.login_register:
			Intent intent = new Intent();
			intent.setClass(this, RegisterActivity.class);
			startActivity(intent);
			break;
		}
	}

	String userName;
	String password;

	private void login() {
		userName = userEdit.getText().toString();
		password = passwordEdit.getText().toString();
		if (TextUtils.isEmpty(userName)) {
			Utils.Toast(Utils.getResString(R.string.please_input_username));
			userEdit.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Utils.Toast(Utils.getResString(R.string.please_input_password));
			passwordEdit.requestFocus();
			return;
		}
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		HttpCallbackListener httpCallbackListener = new HttpCallbackListener() {

			@Override
			public void onSuccessCallBack(String result) {
				UIManager.cancelAllProgressDialog();
				onSuccessHandle(result);
			}

			@Override
			public void onErrorCallBack() {
				Utils.Toast(Utils.getResString(R.string.network_exception));
				UIManager.cancelAllProgressDialog();
			}

			@Override
			public void onCancelCallBack() {
				UIManager.cancelAllProgressDialog();
			}
		};

		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_EVENT;
		params.methodName = Constants.LoginUserInfo;
		params.paramList.put("userName", userName);
		params.paramList.put("pwd", password);
		params.paramList.put("key", Constants.KEY);
		params.paramList.put("mobileSys", "android");

		WebService.requestAsynHttp(params, httpCallbackListener, true,
				cancelObserver);
	}

	/**
	 * 联网请求成功后的处理
	 * 
	 * @param result
	 */
	public void onSuccessHandle(String result) {
		LoginJsonObject loginJsonObject = Utils.parseJson(result,
				LoginJsonObject.class);
		if (loginJsonObject != null) {
			if ("登录成功".equalsIgnoreCase(loginJsonObject.Message)) {
				FileUtils.storeDataToFile(this, loginJsonObject,
						Constants.fileName_Userinfo);
				SharedPreferenceUtils spfUtils = SharedPreferenceUtils
						.getInstance(this, SharedPreferenceUtils.FILE_USER_INFO);
				spfUtils.put(SharedPreferenceUtils.PASSWORD, password);
				spfUtils.put(SharedPreferenceUtils.USER_NAME, loginJsonObject.UserName);
				spfUtils.put(SharedPreferenceUtils.IS_VIP, loginJsonObject.IsVIP);
				if (SettingPage.handler != null) {
					SettingPage.handler.obtainMessage().sendToTarget();
				}
				if (MenuPage.handler != null) {
					MenuPage.handler.obtainMessage().sendToTarget();
				}
				Utils.Toast(loginJsonObject.Message, 1);
				finish();
			} else {
				Utils.Toast(loginJsonObject.Message, 1);
			}
		}
	}
}
