package com.MeiHuaNet.activity.recruit;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.entity.RecruitDetailObject;
import com.MeiHuaNet.entity.RecruitObject;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.AsyncImageLoader;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.StringUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.utils.AsyncImageLoader.ImageCallback;
import com.MeiHuaNet.view.TextViewFont;

public class RecruitDetailActivity extends BaseActivity {

	private ScrollView scrollView;
	private RecruitObject recruitObject;
	private TextViewFont jobName;
	private TextViewFont city;
	private TextViewFont date;
	private TextViewFont employeName;
	private ImageView logo;
	private TextViewFont positionIntro;
	private TextViewFont profileIntro;
	private TextViewFont phone;
	private TextViewFont mobile;
	private TextViewFont address;
	private RecruitDetailObject recruitDetailObject;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_recruit_detail);

		initView();
	}

	private void initView() {
		initTitle();
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.recruit_detail_all_layout);
		GestureDetectorUtil.setGestureDetector(this, linearLayout, null);

		recruitObject = (RecruitObject) getIntent().getSerializableExtra(
				"detailObject");
		scrollView = (ScrollView) findViewById(R.id.recruit_detail_scrollview);
		scrollView.setVisibility(View.INVISIBLE);

		jobName = (TextViewFont) findViewById(R.id.recruit_detail_jobname);
		city = (TextViewFont) findViewById(R.id.recruit_detail_city);
		date = (TextViewFont) findViewById(R.id.recruit_detail_time);
		employeName = (TextViewFont) findViewById(R.id.recruit_detail_employename);
		logo = (ImageView) findViewById(R.id.recruit_detail_logo);
		positionIntro = (TextViewFont) findViewById(R.id.recruit_detail_position_intro);
		profileIntro = (TextViewFont) findViewById(R.id.recruit_detail_profileintro);
		phone = (TextViewFont) findViewById(R.id.recruit_detail_phone);
		mobile = (TextViewFont) findViewById(R.id.recruit_detail_mobile);
		address = (TextViewFont) findViewById(R.id.recruit_detail_address);

		requestData();
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView("", R.drawable.email);
		handleBackTitle.setListener(new OnBackBtnListener(this),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						sendToEmail();
					}
				});
	}

	private void requestData() {
		HttpCallBackDialog httpCallBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				recruitDetailObject = Utils.parseJson(result,
						RecruitDetailObject.class);
				if (recruitDetailObject != null) {
					setShowContent(recruitDetailObject);
					scrollView.setVisibility(View.VISIBLE);
				}
				UIManager.cancelAllProgressDialog();
			}
		};

		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_RECRUIT;
		params.methodName = Constants.GetJobDetail;
		params.paramList.put("id", recruitObject.ID);

		WebService.requestAsynHttp(params, httpCallBackDialog, true,
				cancelObserver);
	}

	private void setShowContent(RecruitDetailObject recruitDetailObject) {
		jobName.setText(recruitDetailObject.JobName);
		city.setText(recruitDetailObject.City);
		date.setText(recruitDetailObject.Date + "-"
				+ recruitDetailObject.ExpireDate);
		employeName.setText(recruitDetailObject.EmployerName);
		logo.setTag(recruitObject.LogoUrl);
		final Drawable drawable = AsyncImageLoader.getInstance(this)
				.loadDrawable(recruitObject.LogoUrl, new ImageCallback() {

					@Override
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl, ImageView view) {
						if (imageDrawable != null) {
							if (imageUrl.equals(view.getTag())) {
								view.setImageDrawable(imageDrawable);
							}

						}
					}
				}, logo);
		if (drawable != null) {
			logo.setImageDrawable(drawable);
		} else {
			logo.setImageResource(R.drawable.default_img);
		}
		positionIntro.setText(StringUtils
				.getDetailStr(recruitDetailObject.Description));
		profileIntro.setText(StringUtils
				.getDetailStr(recruitDetailObject.EmployerDescription));
		address.setText(Utils.getResString(R.string.StreetAddress)
				+ recruitDetailObject.Address);
		String phoneStr = recruitDetailObject.EmployerPhone;
		if (phoneStr != null) {
			String[] phones = phoneStr.split(" ");
			Utils.log(phones.toString());
			if (phones.length > 1) {
				phone.setText(Utils.getResString(R.string.Phone_text)
						+ phones[0]);
				for (int i = 1; i < phones.length; i++) {
					if (phones[i] != null && phones[i].length() > 0) {
						mobile.setText(phones[i]);
						break;
					}
				}
			}
		}
	}

	private void sendToEmail() {
		if (recruitDetailObject == null) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("【" + recruitDetailObject.JobName + "】" + "  "
				+ recruitDetailObject.EmployerName + "\n");
		sb.append(StringUtils.getDetailStr(recruitDetailObject.Description)
				+ "\n");
		sb.append(Utils.getResString(R.string.application_position)
				+ "http://www.meihua.info/today/job/JobDetail.aspx?itemID="
				+ recruitDetailObject.ID);
		Utils.sendEmail(this, Utils.getResString(R.string.recruit_email_title),
				StringUtils.getSummaryStr(sb.toString()),null);

	}
}
