package com.MeiHuaNet.activity.event;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.activity.event.EventPage.EventType;
import com.MeiHuaNet.activity.event.RegistrationActivity.RegistrationType;
import com.MeiHuaNet.activity.login.LoginActivity;
import com.MeiHuaNet.entity.EventDetailObject;
import com.MeiHuaNet.entity.EventObject;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.CalenderUtils;
import com.MeiHuaNet.utils.DateUtil;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.ShareSDKUtils;
import com.MeiHuaNet.utils.StringUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.TextViewFont;

/**
 * 
 * @description 活动详情页
 * @author lee
 * @time 2013-11-6 下午4:27:29
 * 
 */
public class EventDetailActivity extends BaseActivity implements
		OnClickListener {

	final String mimeType = "text/html";
	final String encoding = "utf-8";
	EventObject eventObject;
	EventType curType;
	EventDetailObject eventDetailObject;

	TextViewFont title;
	TextViewFont city;
	TextViewFont date;
	TextViewFont category;
	TextViewFont price;
	WebView introWebview;
	WebView agendaWebview;
	WebView attendWebview;
	WebView remindWebview;
	LinearLayout layout;
	TextViewFont signUp;
	TextViewFont addSchedule;
	ScrollView scrollView;
	TextViewFont attendTextView;
	TextViewFont agendaTextView;
	TextViewFont remindTextView;
	private Handler handler;

	@SuppressLint("HandlerLeak")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_event_detail);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (!TextUtils.isEmpty(eventDetailObject.Audience)) {
					attendTextView.setVisibility(View.VISIBLE);
				}
				if (!TextUtils.isEmpty(eventDetailObject.Agenda)) {
					agendaTextView.setVisibility(View.VISIBLE);
				}
				if (!TextUtils.isEmpty(eventDetailObject.Instruction)) {
					remindTextView.setVisibility(View.VISIBLE);
				}
			}
		};
		initView();
	}

	private void initView() {
		initTitle();

		LinearLayout allLayout = (LinearLayout) findViewById(R.id.event_detail_all_layout);
		GestureDetectorUtil.setGestureDetector(this, allLayout, null);

		eventObject = (EventObject) getIntent().getSerializableExtra(
				"detailObject");
		curType = (EventType) getIntent().getSerializableExtra("type");

		title = (TextViewFont) findViewById(R.id.event_detail_title);
		city = (TextViewFont) findViewById(R.id.event_detail_city);
		date = (TextViewFont) findViewById(R.id.event_detail_date);
		category = (TextViewFont) findViewById(R.id.event_detail_category);
		price = (TextViewFont) findViewById(R.id.event_detail_price);
		layout = (LinearLayout) findViewById(R.id.event_detail_extra_layout);
		signUp = (TextViewFont) findViewById(R.id.event_detail_sign_up);
		addSchedule = (TextViewFont) findViewById(R.id.event_detail_add_schedule);
		introWebview = (WebView) findViewById(R.id.event_intro_webview);
		agendaWebview = (WebView) findViewById(R.id.event_agenda_webview);
		attendWebview = (WebView) findViewById(R.id.event_attend_webview);
		remindWebview = (WebView) findViewById(R.id.event_remind_webview);
		attendTextView = (TextViewFont) findViewById(R.id.event_attend_text);
		agendaTextView = (TextViewFont) findViewById(R.id.event_agenda_text);
		remindTextView = (TextViewFont) findViewById(R.id.event_remind_text);
		scrollView = (ScrollView) findViewById(R.id.event_detail_scrollview);

		attendTextView.setVisibility(View.GONE);
		agendaTextView.setVisibility(View.GONE);
		remindTextView.setVisibility(View.GONE);
		scrollView.setVisibility(View.GONE);

		requestData();
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView("", R.drawable.share_btn);
		handleBackTitle.setListener(new OnBackBtnListener(this),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (eventDetailObject != null) {
							ShareSDKUtils.share(EventDetailActivity.this,
									eventDetailObject.Name,
									eventDetailObject.Intro,
									eventDetailObject.Url);
						}
					}
				});
	}

	private void requestData() {
		HttpCallBackDialog httpCallBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				eventDetailObject = Utils.parseJson(result,
						EventDetailObject.class);
				if (eventDetailObject != null) {
					setContent();
					scrollView.setVisibility(View.VISIBLE);
				}
				UIManager.cancelAllProgressDialog();
			}
		};

		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_EVENT;
		params.methodName = Constants.GetEventDetail;
		if (TextUtils.isEmpty(eventObject.ID)) {
			params.paramList.put("eventID", eventObject.EventID);
		} else {
			params.paramList.put("eventID", eventObject.ID);
		}

		WebService.requestAsynHttp(params, httpCallBackDialog, true,
				cancelObserver);
	}

	private void setContent() {
		title.setText(eventDetailObject.Name);
		city.setText(eventDetailObject.City);
		date.setText(DateUtil.getEventDate(eventDetailObject.StartDate,
				eventDetailObject.EndDate));
		category.setText(Utils.getResString(R.string.event_category)
				+ eventDetailObject.Category);
		price.setText(Utils.getResString(R.string.event_price)
				+ eventDetailObject.PriceText);
		switch (curType) {
		case effective:
			layout.setVisibility(View.VISIBLE);
			signUp.setOnClickListener(this);
			if (CalenderUtils.isHaveCalendarApp(this)) {
				addSchedule.setOnClickListener(this);
			} else {
				addSchedule.setVisibility(View.GONE);
			}
			break;
		case myevent:

			if (DateUtil.isBeforeNow(eventDetailObject.EndDate)
					|| !CalenderUtils.isHaveCalendarApp(this)) {
				layout.setVisibility(View.GONE);
			} else {
				layout.setVisibility(View.VISIBLE);
				signUp.setVisibility(View.GONE);
				addSchedule.setOnClickListener(this);
			}
			break;
		case pastevent:
			layout.setVisibility(View.GONE);
			break;
		}
		setWebViewAttribute(introWebview);
		setWebViewAttribute(agendaWebview);
		setWebViewAttribute(attendWebview);
		setWebViewAttribute(remindWebview);
		introWebview.loadDataWithBaseURL(null,
				StringUtils.GetEventDetailStr(eventDetailObject.Intro, false),
				mimeType.replaceAll("\\+", " "), encoding, null);
		if (!TextUtils.isEmpty(eventDetailObject.Audience)) {
			attendWebview.loadDataWithBaseURL(null, StringUtils
					.GetEventDetailStr(eventDetailObject.Audience, true),
					mimeType.replaceAll("\\+", " "), encoding, null);
		}
		if (!TextUtils.isEmpty(eventDetailObject.Agenda)) {
			agendaWebview.loadDataWithBaseURL(null, StringUtils
					.GetEventDetailStr(eventDetailObject.Agenda, true),
					mimeType.replaceAll("\\+", " "), encoding, null);
		}
		if (!TextUtils.isEmpty(eventDetailObject.Instruction)) {
			remindWebview.loadDataWithBaseURL(null, StringUtils
					.GetEventDetailStr(eventDetailObject.Instruction, true),
					mimeType.replaceAll("\\+", " "), encoding, null);
		}
		// 下面的布局延迟显示
		handler.sendMessageDelayed(handler.obtainMessage(), 1000);

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setWebViewAttribute(WebView mWebView) {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(false);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDefaultTextEncodingName("utf-8");
		mWebView.getSettings().setPluginState(PluginState.ON);
//		mWebView.getSettings().setDefaultFontSize(16);
		mWebView.getSettings()
				.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// mWebView.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// return true;
		// }
		// });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.event_detail_add_schedule:
			try {
				long start = DateUtil.getLongTime(eventDetailObject.StartDate);
				long end = DateUtil.getLongTime(eventDetailObject.EndDate);
				CalenderUtils.addToCalendar(this, eventDetailObject.Name,
						Utils.getResString(R.string.attend_event), start, end);
				Utils.Toast(Utils.getResString(R.string.add_schedule_success));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.event_detail_sign_up:
			if (Utils.isLogin(this) == null) {
				Intent intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(this, RegistrationActivity.class);
				int price = Integer.parseInt(eventDetailObject.PriceValue);
				intent.putExtra("eventObject", eventDetailObject);
				if (price > 0) {
					// 收费活动
					intent.putExtra("type", RegistrationType.charge);
				} else {
					// 免费活动
					intent.putExtra("type", RegistrationType.free);
				}
				startActivity(intent);
			}
			break;
		}
	}

}
