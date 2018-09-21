package com.MeiHuaNet.activity.resource;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
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
import com.MeiHuaNet.activity.login.LoginActivity;
import com.MeiHuaNet.activity.vip.VipUpdateActivity;
import com.MeiHuaNet.activity.vip.VipUpdateActivity.VipUpdateType;
import com.MeiHuaNet.adapter.RateCardAdapter;
import com.MeiHuaNet.entity.CheckDocDownResult;
import com.MeiHuaNet.entity.LoginJsonObject;
import com.MeiHuaNet.entity.MediaDetailObject;
import com.MeiHuaNet.entity.MediaDetailObject.ContectInfo;
import com.MeiHuaNet.entity.MediaDetailObject.RateCard;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.listener.OnListItemClickListener;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.AsyncImageLoader;
import com.MeiHuaNet.utils.DialogUtils;
import com.MeiHuaNet.utils.DocDownloadTask;
import com.MeiHuaNet.utils.DocDownloadTask.DownFinishedListener;
import com.MeiHuaNet.utils.DownloadUtil;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.utils.AsyncImageLoader.ImageCallback;
import com.MeiHuaNet.view.LinearListView;
import com.MeiHuaNet.view.TextViewFont;

public class MediaDetailActivity extends BaseActivity implements
		OnClickListener {

	private MediaDetailObject mediaDetailObject;
	private String mediaId;
	private ScrollView scrollView;
	private TextViewFont name;
	private TextViewFont nameEn;
	private ImageView logo;
	private TextViewFont intro;
	private LinearLayout basedataLayout;
	private LinearLayout emailLayout;
	private LinearLayout contactsLayout;
	private LinearLayout phoneLayout;
	private LinearLayout faxLayout;
	private TextViewFont emaliTv;
	private TextViewFont contactsTv;
	private TextViewFont phoneTv;
	private TextViewFont faxTv;
	private TextViewFont ratecardTv;
	private LinearListView ratecardListView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_media_detail);

		mediaId = getIntent().getStringExtra("mediaID");
		initView();
	}

	private void initView() {
		initTitle();
		LinearLayout layout = (LinearLayout) findViewById(R.id.media_detail_alllayout);
		GestureDetectorUtil.setGestureDetector(this, layout, null);

		scrollView = (ScrollView) findViewById(R.id.media_detail_scrollview);
		name = (TextViewFont) findViewById(R.id.media_detail_name);
		nameEn = (TextViewFont) findViewById(R.id.media_detail_name_en);
		logo = (ImageView) findViewById(R.id.media_detail_img);
		intro = (TextViewFont) findViewById(R.id.media_detail_intro);
		basedataLayout = (LinearLayout) findViewById(R.id.media_detail_basedata_layout);
		emailLayout = (LinearLayout) findViewById(R.id.media_detail_email_layout);
		emaliTv = (TextViewFont) findViewById(R.id.media_detail_email_tv);
		contactsLayout = (LinearLayout) findViewById(R.id.media_detail_contacts_layout);
		contactsTv = (TextViewFont) findViewById(R.id.media_detail_contacts_tv);
		phoneLayout = (LinearLayout) findViewById(R.id.media_detail_phone_layout);
		phoneTv = (TextViewFont) findViewById(R.id.media_detail_phone_tv);
		faxLayout = (LinearLayout) findViewById(R.id.media_detail_fax_layout);
		faxTv = (TextViewFont) findViewById(R.id.media_detail_fax_tv);
		ratecardTv = (TextViewFont) findViewById(R.id.media_detail_ratecard_tv);
		ratecardListView = (LinearListView) findViewById(R.id.media_detail_listview);

		scrollView.setVisibility(View.GONE);

		requestData();
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView(Utils.getResString(R.string.media_store),
				-1);
		handleBackTitle.setListener(new OnBackBtnListener(this), null);
	}

	private void requestData() {
		HttpCallBackDialog callBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				UIManager.cancelAllProgressDialog();
				mediaDetailObject = Utils.parseJson(result,
						MediaDetailObject.class);
				if (mediaDetailObject != null) {
					scrollView.setVisibility(View.VISIBLE);
					Utils.log(mediaDetailObject.toString());
					setContent();
				}
			}
		};

		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_MEDIA;
		params.methodName = Constants.getnewmediadetail;
		params.paramList.put("mediuID", mediaId);

		WebService
				.requestAsynHttp(params, callBackDialog, true, cancelObserver);
	}

	/**
	 * 获取到详细信息后，设置页面显示的内容
	 */
	private void setContent() {
		name.setText(mediaDetailObject.名称);
		nameEn.setText(mediaDetailObject.英文名称);
		logo.setTag(mediaDetailObject.Logo);
		final Drawable drawable = AsyncImageLoader.getInstance(this)
				.loadDrawable(mediaDetailObject.Logo, new ImageCallback() {

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
		}

		intro.setText(mediaDetailObject.媒体简介);

		// 设置联系方式
		ContectInfo contectInfo = mediaDetailObject.Contect;
		if (TextUtils.isEmpty(contectInfo.Email)) {
			emailLayout.setVisibility(View.GONE);
		} else {
			emaliTv.setText(contectInfo.Email);
			emaliTv.setOnClickListener(this);
		}
		if (TextUtils.isEmpty(contectInfo.联系人)) {
			contactsLayout.setVisibility(View.GONE);
		} else {
			contactsTv.setText(contectInfo.联系人);
		}
		if (TextUtils.isEmpty(contectInfo.电话)) {
			phoneLayout.setVisibility(View.GONE);
		} else {
			phoneTv.setText(contectInfo.电话);
			phoneTv.setOnClickListener(this);
		}
		if (TextUtils.isEmpty(contectInfo.传真)) {
			faxLayout.setVisibility(View.GONE);
		} else {
			faxTv.setText(contectInfo.传真);
		}

		// 设置基本数据
		setBaseDataView();

		// 设置刊例文档
		setRateCardView();
	}

	private void setBaseDataView() {
		ArrayList<String> dataList = mediaDetailObject.BaseMediamInfo
				.getListStr();
		if (dataList == null) {
			return;
		}
		for (int i = 0; i < dataList.size(); i++) {
			TextViewFont tv = new TextViewFont(this);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			tv.setTextColor(0xff000000);
			tv.setText(dataList.get(i));
			basedataLayout.addView(tv);
		}
	}

	private void setRateCardView() {
		if (mediaDetailObject.RateCards == null
				|| mediaDetailObject.RateCards.size() == 0) {
			ratecardTv.setVisibility(View.GONE);
			ratecardListView.setVisibility(View.GONE);
			return;
		}
		RateCardAdapter adapter = new RateCardAdapter(this,
				mediaDetailObject.RateCards);
		adapter.setItemListener(new OnListItemClickListener() {

			@Override
			public void onclick(View v, int position) {
				RateCard rateCard = mediaDetailObject.RateCards.get(position);
				LoginJsonObject loginJsonObject = Utils
						.isLogin(MediaDetailActivity.this);
				if (loginJsonObject != null) {
					if ("true".equalsIgnoreCase(loginJsonObject.IsVIP)) {
						downLoadDoc(rateCard, loginJsonObject.UserName);
					} else {
						checkIsCanDown(rateCard, loginJsonObject.UserName);
					}
				} else {
					Intent intent = new Intent();
					intent.setClass(MediaDetailActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
			}
		});
		ratecardListView.setAdapter(adapter);
	}

	/**
	 * 下载选中的文档，如果以前下载过且下载的文件没有删除，不会重复下载
	 * 
	 * @param rateCard
	 * @param userName
	 */
	public void downLoadDoc(final RateCard rateCard, final String userName) {
		String url = rateCard.Url;
		if (url == null) {
			return;
		}
		UIManager.showProgressDialog("", false);
		DownloadUtil.downloadDoc(this, new DownFinishedListener() {

			@Override
			public void finish() {
				submitLog(rateCard.刊例ID, userName);
			}
		}, url, DocDownloadTask.PATH, rateCard.刊例名称 + "." + rateCard.扩展名, 100);
	}

	/**
	 * 文档下载成功后，提交下载日志
	 * 
	 * @param ratecardID
	 * @param userName
	 */
	private void submitLog(String ratecardID, String userName) {
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_MEDIA;
		params.methodName = Constants.createnewdownratecardlog;
		params.paramList.put("rateCardID", ratecardID);
		params.paramList.put("userName", userName);

		WebService.requestAsynHttp(params, null, false, null);

	}

	/**
	 * 不是vip用户时检查是否能下载（免费用户每个月只能下载5份文档）
	 * 
	 * @param rateCardId
	 * @param userName
	 */
	private void checkIsCanDown(final RateCard rateCard, final String userName) {
		HttpCallBackDialog httpCallBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				UIManager.cancelAllProgressDialog();
				CheckDocDownResult checkDocDownResult = Utils.parseJson(result,
						CheckDocDownResult.class);
				if (checkDocDownResult != null) {
					if ("true".equals(checkDocDownResult.IsDown)) {
						downLoadDoc(rateCard, userName);
					} else {
						DialogUtils.createDialog(MediaDetailActivity.this,
								Utils.getResString(R.string.vip_update_prompt),
								Utils.getResString(R.string.update),
								Utils.getResString(R.string.cancel),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										// 进入vip升级页面
										Intent intent = new Intent();
										intent.setClass(
												MediaDetailActivity.this,
												VipUpdateActivity.class);
										intent.putExtra("id", rateCard.刊例ID);
										intent.putExtra("vipType",
												VipUpdateType.media);
										startActivity(intent);
									}
								}, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
					}
				}
			}
		};
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_MEDIA;
		params.methodName = Constants.checknewdownratecard;
		params.paramList.put("rateCardID", rateCard.刊例ID);
		params.paramList.put("userName", userName);

		WebService.requestAsynHttp(params, httpCallBackDialog, true,
				cancelObserver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.media_detail_email_tv:
			DialogUtils.createDialog(this,
					Utils.getResString(R.string.sure_send_email),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Utils.sendEmail(MediaDetailActivity.this, "", "",
									mediaDetailObject.Contect.Email);
						}
					}, null);
			break;
		case R.id.media_detail_phone_tv:
			DialogUtils.createDialog(this,
					Utils.getResString(R.string.sure_phone),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:"
											+ mediaDetailObject.Contect.电话));
							startActivity(intent);
						}
					}, null);
			break;
		}
	}

}
