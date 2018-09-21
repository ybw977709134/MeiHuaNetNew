package com.MeiHuaNet.activity.vip;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.entity.LoginJsonObject;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.DialogUtils;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.StringUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.EditTextFont;
import com.alipay.PayBaseHelper;
import com.alipay.Result;
import com.alipay.PayBaseHelper.RequestPayTye;

/**
 * 
 * @description 升级到vip会员的页面
 * @author lee
 * @time 2013-12-25 下午1:18:54
 * 
 */
@SuppressLint("HandlerLeak")
public class VipUpdateActivity extends BaseActivity implements OnClickListener {

	private RadioButton allYearMemberBtn;
	private EditTextFont emailEdit;
	private RadioButton notNeedInvoiceBtn;
	private EditTextFont addresseeEdit;
	private EditTextFont addressEdit;
	private EditTextFont postCodeEdit;
	private EditTextFont payTypeEdit;
	private LinearLayout needInvoiceLayout;
	private LinearLayout phoneLayout;
	private LinearLayout benefitLayout;
	private VipUpdateType curVipUpdateType;
	private VipOrderInfo orderInfo;
	private LoginJsonObject loginJsonObject;
	private Handler mHandler;

	/**
	 * 
	 * @description 升级vip的类型（中国传媒库升级vip,创意库升级vip）
	 * @author lee
	 * @time 2013-12-25 下午2:56:10
	 * 
	 */
	public enum VipUpdateType {
		media, originality;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_vip_update);
		curVipUpdateType = (VipUpdateType) getIntent().getSerializableExtra(
				"vipType");
		orderInfo = new VipOrderInfo();
		orderInfo.fromItems = getIntent().getStringExtra("id");
		loginJsonObject = Utils.isLogin(this);

		initView();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case PayBaseHelper.RQF_PAY:
					Result result = new Result((String) msg.obj);
					result.parseResult();
					if ("您已经成功支付".equals(result.getResultStatus())) {
						Utils.Toast(result.getResultStatus(), 1);
						handlerOrder();
					} else {
						Utils.Toast(result.getResultStatus(), 1);
					}
					break;

				default:
					break;
				}
			}
		};
	}

	private void initView() {
		initTitle();

		LinearLayout layout = (LinearLayout) findViewById(R.id.vip_update_all_layout);
		GestureDetectorUtil.setGestureDetector(this, layout, null);

		allYearMemberBtn = (RadioButton) findViewById(R.id.vip_allyear_btn);
		notNeedInvoiceBtn = (RadioButton) findViewById(R.id.vip_not_need_btn);
		emailEdit = (EditTextFont) findViewById(R.id.vip_email);
		addresseeEdit = (EditTextFont) findViewById(R.id.vip_addressee);
		addressEdit = (EditTextFont) findViewById(R.id.vip_address);
		postCodeEdit = (EditTextFont) findViewById(R.id.vip_postcode);
		payTypeEdit = (EditTextFont) findViewById(R.id.vip_invoice_type);
		phoneLayout = (LinearLayout) findViewById(R.id.consult_phone_layout);
		benefitLayout = (LinearLayout) findViewById(R.id.member_benefit_layout);
		needInvoiceLayout = (LinearLayout) findViewById(R.id.vip_need_invoice_layout);
		needInvoiceLayout.setVisibility(View.GONE);

		if (loginJsonObject != null && loginJsonObject.Email != null) {
			emailEdit.setText(loginJsonObject.Email);
		}
		orderInfo.price = "498";
		allYearMemberBtn
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							orderInfo.price = "498";
						} else {
							orderInfo.price = "300";
						}
					}
				});
		orderInfo.needInvoice = false;
		notNeedInvoiceBtn
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							orderInfo.needInvoice = false;
							needInvoiceLayout.setVisibility(View.GONE);
						} else {
							orderInfo.needInvoice = true;
							needInvoiceLayout.setVisibility(View.VISIBLE);
						}
					}
				});
		phoneLayout.setOnClickListener(this);
		benefitLayout.setOnClickListener(this);
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView(Utils.getResString(R.string.vip_update),
				-1);
		handleBackTitle.setRightText(Utils.getResString(R.string.sure), this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.consult_phone_layout:
			DialogUtils.createDialog(this,
					Utils.getResString(R.string.call_consult_phone),
					Utils.getResString(R.string.sure),
					Utils.getResString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:400-880-4636"));
							startActivity(intent);
							dialog.dismiss();
						}
					}, null);
			break;
		case R.id.member_benefit_layout:
			Intent intent = new Intent();
			intent.setClass(this, VipIntroActivity.class);
			startActivity(intent);
			break;
		case R.id.title_back_right_layout:
			updateVip();
			break;
		}
	}

	/**
	 * 确定升级vip
	 */
	private void updateVip() {
		orderInfo.email = emailEdit.getText().toString();
		if (TextUtils.isEmpty(orderInfo.email)) {
			Utils.Toast(Utils.getResString(R.string.email_null));
			return;
		} else {
			if (!StringUtils.isEmail(orderInfo.email)) {
				Utils.Toast(Utils.getResString(R.string.input_right_email));
				return;
			}
		}
		if (orderInfo.needInvoice) {
			// 需要发票时
			orderInfo.fullName = addresseeEdit.getText().toString();
			if (TextUtils.isEmpty(orderInfo.fullName)) {
				Utils.Toast(Utils.getResString(R.string.vip_addressee_null));
				return;
			}
			orderInfo.address = addressEdit.getText().toString();
			if (TextUtils.isEmpty(orderInfo.address)) {
				Utils.Toast(Utils.getResString(R.string.vip_address_null));
				return;
			}
			orderInfo.postCode = postCodeEdit.getText().toString();
			if (TextUtils.isEmpty(orderInfo.postCode)) {
				Utils.Toast(Utils.getResString(R.string.vip_postCode_null));
				return;
			} else {
				if (!StringUtils.checkPostCode(orderInfo.postCode)) {
					Utils.Toast(Utils
							.getResString(R.string.input_right_postCode));
					return;
				}
			}
			orderInfo.companyTitle = payTypeEdit.getText().toString();
			if (TextUtils.isEmpty(orderInfo.companyTitle)) {
				Utils.Toast(Utils.getResString(R.string.vip_invoice_type_null));
				return;
			}
		} else {
			orderInfo.address = "";
			orderInfo.companyTitle = "";
			orderInfo.postCode = "";
			orderInfo.fullName = "";
		}
		if (loginJsonObject != null) {
			orderInfo.userName = loginJsonObject.UserName;
		}
		orderInfo.orderType = "2";
		if (curVipUpdateType == VipUpdateType.media) {
			orderInfo.fromProduct = "mediasearch";
		} else {
			orderInfo.fromProduct = "creative";
		}
		orderInfo.matchPwd = "1C7DF98B-1344-4A2F-9559-34F2CA2D4F1E";

		createOrder();
	}

	String orderID;

	private void createOrder() {
		HttpCallBackDialog callBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				UIManager.cancelAllProgressDialog();
				if (result != null) {
					orderID = result;
					PayBaseHelper payBaseHelper = new PayBaseHelper();
					if (curVipUpdateType == VipUpdateType.media) {
						payBaseHelper.pay(RequestPayTye.mediaVip,
								"0.01", VipUpdateActivity.this,
								mHandler);
					} else {
						payBaseHelper.pay(RequestPayTye.originalityVip,
								orderInfo.price, VipUpdateActivity.this,
								mHandler);
					}

				}
			}
		};

		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_VIP;
		params.methodName = Constants.CreateOrderByString;
		params.paramList.put("userName", orderInfo.userName);
		params.paramList.put("orderType", orderInfo.orderType);
		params.paramList.put("fullName", orderInfo.fullName);
		params.paramList.put("email", orderInfo.email);
		params.paramList.put("address", orderInfo.address);
		params.paramList.put("needInvoice", orderInfo.needInvoice);
		params.paramList.put("postCode", orderInfo.postCode);
		params.paramList.put("companyTitle", orderInfo.companyTitle);
		params.paramList.put("fromProduct", orderInfo.fromProduct);
		params.paramList.put("price", orderInfo.price);
		params.paramList.put("fromItems", orderInfo.fromItems);
		params.paramList.put("matchPwd", orderInfo.matchPwd);

		WebService
				.requestAsynHttp(params, callBackDialog, true, cancelObserver);
	}

	static class VipOrderInfo {
		String userName;// 梅花网账号
		String orderType;// 订单的类型 2：支付宝支付
		String fullName;// 收件人姓名
		String email;// 用户email接收开通vip的通知
		String address;// 用户发票邮寄地址
		boolean needInvoice;// 是否需要发票
		String postCode;// 邮编
		String companyTitle;// 发票抬头
		String fromProduct;// 升级的是什么产品的vip（传媒库和创意视频的两种vip升级）
		String price;// 升级vip的加个（半年和全年会员加个不同）
		String fromItems;// 媒体库文件id或创意视频的id
		String matchPwd;// 服务器提供的值
	}
	
	/**
	 * 支付宝支付成功后处理订单
	 */
	private void handlerOrder(){
//		HttpCallBackDialog callBackDialog = new HttpCallBackDialog() {
//			
//			@Override
//			public void onSuccessCallBack(String result) {
//				
//			}
//		};
		
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_VIP;
		params.methodName = Constants.EditOrderStatus;
		params.paramList.put("orderNumber", orderID);
		params.paramList.put("isFinished", true);
		params.paramList.put("matchPwd", orderInfo.matchPwd);
		
		WebService.requestAsynHttp(params, null, false, null);		
	}
}
