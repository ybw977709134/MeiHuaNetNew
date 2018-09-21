package com.MeiHuaNet.activity.setting;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.entity.FeedbackResult;
import com.MeiHuaNet.entity.LoginJsonObject;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.DialogUtils;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.StringUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.EditTextFont;
import com.MeiHuaNet.view.TextViewFont;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * @description 意见反馈页面
 * @author lee
 * @time 2013-12-20 下午5:27:55
 * 
 */
public class FeedbackActivity extends BaseActivity {

	EditTextFont mEdittext;
	TextViewFont phoneView;
	EditTextFont emailEdit;
	LoginJsonObject loginJsonObject;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_feedback);

		initView();
	}

	private void initView() {
		initTitle();

		LinearLayout alllayout = (LinearLayout) findViewById(R.id.feedback_all_layout);
		GestureDetectorUtil.setGestureDetector(FeedbackActivity.this,
				alllayout, null);

		emailEdit = (EditTextFont) findViewById(R.id.feedback_email);
		loginJsonObject = Utils.isLogin(this);
		if (loginJsonObject != null) {
			emailEdit.setText(loginJsonObject.Email);
		}

		mEdittext = (EditTextFont) findViewById(R.id.send_content);
		mEdittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					mEdittext.setGravity(Gravity.TOP);
					mEdittext.setCursorVisible(true);
				} else {
					// 没有输入时，显示默认提示文字，且文字居中
					mEdittext.setGravity(Gravity.CENTER);

					mEdittext.setCursorVisible(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mEdittext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEdittext.setCursorVisible(true);
			}
		});

		phoneView = (TextViewFont) findViewById(R.id.service_phone);
		phoneView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogUtils.createDialog(FeedbackActivity.this,
						Utils.getResString(R.string.sure_call),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(Intent.ACTION_CALL,
										Uri.parse("tel:4008804636"));
								startActivity(intent);
							}
						}, null);
			}
		});
	}

	private void initTitle() {
		ImageView leftBtn = (ImageView) findViewById(R.id.title_back_left);
		TextViewFont rightTv = (TextViewFont) findViewById(R.id.title_back_right);
		leftBtn.setOnClickListener(new OnBackBtnListener(FeedbackActivity.this));
		rightTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				send();
			}
		});

	}

	/**
	 * 提交反馈的内容
	 */
	private void send() {
		String content = mEdittext.getText().toString();
		if (content == null || content.length() == 0) {
			Utils.Toast(Utils.getResString(R.string.submit_content_is_null));
			return;
		}
		String email = emailEdit.getText().toString();
		if (email == null || email.length() == 0) {
			Utils.Toast(Utils.getResString(R.string.email_null));
			return;
		} else {
			if (!StringUtils.isEmail(email)) {
				Utils.Toast(Utils.getResString(R.string.input_right_email));
				return;
			}
		}
		HttpCallBackDialog httpCallBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				FeedbackResult result2 = Utils.parseJson(result,
						FeedbackResult.class);
				if (result2 != null && result2.Message !=null) {
					Utils.Toast(result2.Message, 1);
					finish();
				}
			}
		};
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_EVENT;
		params.methodName = Constants.SendFeedbackEmail;
		params.paramList.put("fromEmail", email);
		params.paramList.put("message", content);
		String phone = loginJsonObject == null ? "" : loginJsonObject.Phone;
		phone = phone == null ? "" : phone;
		params.paramList.put("mobileSPhone", phone);
		params.paramList.put("key", Constants.KEY);
		params.paramList.put("mobileSys", "Android");

		WebService.requestAsynHttp(params, httpCallBackDialog, true,
				cancelObserver);
	}
}
