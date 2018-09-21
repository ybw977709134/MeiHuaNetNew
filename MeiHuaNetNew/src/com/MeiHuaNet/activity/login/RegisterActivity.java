package com.MeiHuaNet.activity.login;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.entity.RegisterJsonObject;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.network.HttpCallbackListener;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.StringUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.EditTextFont;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 *
 * @description 用户注册的页面 
 * @author lee
 * @time  2013-11-26 下午3:35:41
 *
 */
public class RegisterActivity extends BaseActivity implements OnClickListener{

	EditTextFont EditUserName;
	EditText editPsw;
	EditText editSurePsw;
	EditTextFont editEmail;
	EditTextFont editFullName;
	EditTextFont editCompany;
	EditTextFont editJobTitle;
	EditTextFont editPhone;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_register);
		
		initView();
	}
	
	private void initView(){
		initTitle();
		LinearLayout layout = (LinearLayout) findViewById(R.id.register_all_layout);
		GestureDetectorUtil.setGestureDetector(this, layout, null);
		
		EditUserName = (EditTextFont) findViewById(R.id.register_username);
		editPsw = (EditText) findViewById(R.id.register_password);
		editSurePsw = (EditText) findViewById(R.id.register_sure_password);
		editEmail = (EditTextFont) findViewById(R.id.register_email);
		editFullName = (EditTextFont) findViewById(R.id.register_full_name);
		editCompany = (EditTextFont) findViewById(R.id.register_company);
		editJobTitle = (EditTextFont) findViewById(R.id.register_jobtitle);
		editPhone = (EditTextFont) findViewById(R.id.register_phone);
				
	}
	
	private void initTitle(){
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setRightText(Utils.getResString(R.string.register), this);
		handleBackTitle.setListener(new OnBackBtnListener(this), null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_right_layout:
			register();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 填好信息后，提交注册
	 */
	private void register(){
		String username = EditUserName.getText().toString();
		if(TextUtils.isEmpty(username)){
			Utils.Toast(Utils.getResString(R.string.username_null));
			EditUserName.requestFocus();
			return;
		} else {
			if(!StringUtils.isNumericAndLetters(username)){
				Utils.Toast(Utils.getResString(R.string.input_right_username));
				EditUserName.requestFocus();
				return;
			}
		}
		String password = editPsw.getText().toString();
		if(TextUtils.isEmpty(password)){
			Utils.Toast(Utils.getResString(R.string.password));
			editPsw.requestFocus();
			return;
		} else {
			if(!StringUtils.isNumericAndLetters(password)){
				Utils.Toast(Utils.getResString(R.string.input_right_password));
				editPsw.requestFocus();
				return;
			}
		}
		String surePsw = editSurePsw.getText().toString();
		if(TextUtils.isEmpty(surePsw)){
			Utils.Toast(Utils.getResString(R.string.sure_password_null));
			editSurePsw.requestFocus();
			return;
		} else {
			if(!password.equals(surePsw)){
				Utils.Toast(Utils.getResString(R.string.password_different));
				return ;
			}
		}
		String email = editEmail.getText().toString();
		if(TextUtils.isEmpty(email)){
			Utils.Toast(Utils.getResString(R.string.email_null));
			editEmail.requestFocus();
			return;
		} else {
			if(!StringUtils.isEmail(email)){
				Utils.Toast(Utils.getResString(R.string.input_right_email));
				editEmail.requestFocus();
				return;
			}
		}
		String fullName = editFullName.getText().toString();
		if(TextUtils.isEmpty(fullName)){
			Utils.Toast(Utils.getResString(R.string.name_null));
			editFullName.requestFocus();
			return;
		}
		String company = editCompany.getText().toString();
		if(TextUtils.isEmpty(company)){
			Utils.Toast(Utils.getResString(R.string.company_null));
			editCompany.requestFocus();
			return;
		}
		String jobtitle = editJobTitle.getText().toString();
		if(TextUtils.isEmpty(jobtitle)){
			Utils.Toast(Utils.getResString(R.string.position_null));
			editJobTitle.requestFocus();
			return;
		}
		String phone = editPhone.getText().toString();
		if(TextUtils.isEmpty(phone)){
			Utils.Toast(Utils.getResString(R.string.phone_null));
			editPhone.requestFocus();
			return;
		}
		HttpCallbackListener callbackListener = new HttpCallbackListener() {
			
			@Override
			public void onSuccessCallBack(String result) {
				UIManager.cancelAllProgressDialog();
				RegisterJsonObject registerJsonObject = Utils.parseJson(result, RegisterJsonObject.class);
				if(registerJsonObject != null){
					if("注册成功".equalsIgnoreCase(registerJsonObject.Message)){
						Utils.Toast(registerJsonObject.Message, 1);
						finish();
					}  else {
						Utils.Toast(registerJsonObject.Message, 1);
					}
				}
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
		params.methodName = Constants.SignIn;
		params.paramList.put("userName", username);
		params.paramList.put("pwd", password);
		params.paramList.put("FullName", fullName);
		params.paramList.put("company", company);
		params.paramList.put("email", email);
		params.paramList.put("jobTitle", jobtitle);
		params.paramList.put("phone", "");
		params.paramList.put("key", Constants.KEY);
		params.paramList.put("mobileSys", "Android");
		params.paramList.put("mobilePhone", phone);
		
		WebService.requestAsynHttp(params, callbackListener, true, cancelObserver);
	}
}
