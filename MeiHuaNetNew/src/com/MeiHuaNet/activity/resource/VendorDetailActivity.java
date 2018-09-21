package com.MeiHuaNet.activity.resource;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.entity.VendorDetailObject;
import com.MeiHuaNet.entity.VendorObject;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.AsyncImageLoader;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.utils.AsyncImageLoader.ImageCallback;
import com.MeiHuaNet.view.TextViewFont;

/**
 *
 * @description 营销服务商详情页面 
 * @author lee
 * @time  2013-12-11 下午2:02:52
 *
 */
public class VendorDetailActivity extends BaseActivity {

	VendorObject vendorObject;
	ScrollView scrollView;
	TextViewFont name_cn;
	TextViewFont name_en;
	TextViewFont openYear;
	TextViewFont employeeNumber;
	ImageView logo;
	TextViewFont profileIntro;
	TextViewFont address;
	TextViewFont phone;
	TextViewFont fax;
	TextViewFont email;
	TextViewFont post;
	TextViewFont mobile;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_vendor_detail);
		initView();
	}

	private void initView() {
		initTitle();
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.vendor_detail_all_layout);
		GestureDetectorUtil.setGestureDetector(this, linearLayout, null);
		
		vendorObject = (VendorObject) getIntent().getSerializableExtra(
				"detailObject");
		scrollView = (ScrollView) findViewById(R.id.vendor_detail_scrollview);
		scrollView.setVisibility(View.INVISIBLE);

		name_cn = (TextViewFont) findViewById(R.id.vendor_detail_title);
		name_en = (TextViewFont) findViewById(R.id.vendor_detail_title_english);
		openYear = (TextViewFont) findViewById(R.id.vendor_detail_openyear);
		employeeNumber = (TextViewFont) findViewById(R.id.vendor_detail_employeeNumber);
		logo = (ImageView) findViewById(R.id.vendor_detail_logoimg);
		profileIntro = (TextViewFont) findViewById(R.id.vendor_detail_ProfileIntro);
		address = (TextViewFont) findViewById(R.id.vendor_detail_address);
		phone = (TextViewFont) findViewById(R.id.vendor_detail_phone);
		fax = (TextViewFont) findViewById(R.id.vendor_detail_fax);
		email = (TextViewFont) findViewById(R.id.vendor_detail_email);
		post = (TextViewFont) findViewById(R.id.vendor_detail_post);
		mobile = (TextViewFont) findViewById(R.id.vendor_detail_mobile);

		requestData();
	}
	
	private void initTitle(){
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView("", -1);
		handleBackTitle.setListener(new OnBackBtnListener(this), null);
	}

	private void requestData() {
		HttpCallBackDialog httpCallBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				VendorDetailObject vendorDetailObject = Utils.parseJson(result,
						VendorDetailObject.class);
				if (vendorDetailObject != null) {
					setShowContent(vendorDetailObject);
					scrollView.setVisibility(View.VISIBLE);
				}
				UIManager.cancelAllProgressDialog();
			}
		};

		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_MARKET_SERVICE;
		params.methodName = Constants.GetVendorDetail;
		params.paramList.put("vendorID", vendorObject.ID);

		WebService.requestAsynHttp(params, httpCallBackDialog, true,
				cancelObserver);

	}

	private void setShowContent(VendorDetailObject vendorDetailObject) {
		showTop(vendorDetailObject);
		
		showContacts(vendorDetailObject);
	}
	private void showTop(VendorDetailObject vendorDetailObject){
		name_cn.setText(vendorDetailObject.VendorName);
		if(!TextUtils.isEmpty(vendorDetailObject.VendorTitleEn)){
			name_en.setText(vendorDetailObject.VendorTitleEn);
		}
		openYear.setText(Utils.getResString(R.string.openyear)+vendorDetailObject.OpenYear);
		employeeNumber.setText(Utils.getResString(R.string.employeeNumber)+vendorDetailObject.EmployeeNumber);
		logo.setTag(vendorObject.LogoFile);
		final Drawable drawable = AsyncImageLoader.getInstance(this)
				.loadDrawable(vendorObject.LogoFile, new ImageCallback() {

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
		
		profileIntro.setText(vendorDetailObject.ProfileIntro);
	}
	/**
	 * 设置公司联系方式显示内容
	 * @param vendorDetailObject
	 */
	private void showContacts(VendorDetailObject vendorDetailObject){
		VendorDetailObject.ContactObject contactObject = vendorDetailObject.Contacts;
		address.setText(Utils.getResString(R.string.StreetAddress)+ contactObject.StreetAddress);
		phone.setText(Utils.getResString(R.string.Phone_text)+contactObject.Phone);
		fax.setText(Utils.getResString(R.string.Fax)+contactObject.Fax);
		email.setText(Utils.getResString(R.string.Email)+contactObject.Email);
		post.setText(Utils.getResString(R.string.PostCode)+contactObject.PostCode);
		mobile.setText(Utils.getResString(R.string.Mobile)+contactObject.Mobile);
	}
}
