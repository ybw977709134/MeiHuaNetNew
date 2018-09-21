package com.MeiHuaNet.activity.event;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import com.MeiHuaNet.entity.EventDetailObject;
import com.MeiHuaNet.entity.LoginJsonObject;
import com.MeiHuaNet.entity.RegistrationResult;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.DialogUtils;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.SharedPreferenceUtils;
import com.MeiHuaNet.utils.StringUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.EditTextFont;
import com.alipay.PayBaseHelper;
import com.alipay.PayBaseHelper.RequestPayTye;
import com.alipay.Result;

/**
 * 
 * @description 活动报名页面（包含免费和付费活动）
 * @author lee
 * @time 2013-12-6 上午9:43:20
 * 
 */
@SuppressLint("HandlerLeak")
public class RegistrationActivity extends BaseActivity implements
		OnClickListener {

	/**
	 * 
	 * @description 报名的类型（免费，付费）
	 * @author lee
	 * @time 2013-12-6 上午9:44:13
	 * 
	 */
	public static enum RegistrationType {
		free, // 免费
		charge; // 付费
	}

	private RegistrationType registrationType;
	private EventDetailObject eventDetailObject;
	private SharedPreferenceUtils spfUtils;
	private EditTextFont nameEdit;
	private EditTextFont companyEdit;
	private EditTextFont jobEdit;
	private EditTextFont mobileEdit;
	private EditTextFont emailEdit;
	private EditTextFont sinaweiboEdit;
	private EditTextFont weixinEdit;
	private String nameStr;
	private String companyStr;
	private String jobStr;
	private String mobileStr;
	private String emailStr;
	private String sinaweiboStr;
	private String weixinStr;
	private LinearLayout payLayout;
	private Handler mHandler;
	private String orderId;
	private String price;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_event_registration);

		registrationType = (RegistrationType) getIntent().getSerializableExtra(
				"type");
		eventDetailObject = (EventDetailObject) getIntent()
				.getSerializableExtra("eventObject");
		spfUtils = SharedPreferenceUtils.getInstance(this,
				SharedPreferenceUtils.FILE_USER_INFO);
		initView();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case PayBaseHelper.RQF_PAY:
					Result result = new Result((String) msg.obj);
					result.parseResult();
					if ("操作成功".equals(result.getResultStatus())) {
						Utils.Toast(result.getResultStatus(), 1);
						requestDoOrder();
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

		nameEdit = (EditTextFont) findViewById(R.id.registration_name);
		companyEdit = (EditTextFont) findViewById(R.id.registration_company);
		jobEdit = (EditTextFont) findViewById(R.id.registration_jobtitle);
		mobileEdit = (EditTextFont) findViewById(R.id.registration_mobilephone);
		emailEdit = (EditTextFont) findViewById(R.id.registration_email);
		sinaweiboEdit = (EditTextFont) findViewById(R.id.registration_sinaweibo);
		weixinEdit = (EditTextFont) findViewById(R.id.registration_weixin);

		LoginJsonObject loginJsonObject = FileUtils.fetchDataFromFile(this,
				LoginJsonObject.class, Constants.fileName_Userinfo);
		nameEdit.setText(loginJsonObject.FullName);
		companyEdit.setText(loginJsonObject.Company);
		jobEdit.setText(loginJsonObject.JobTitle);
		mobileEdit.setText(loginJsonObject.MobilePhone);
		emailEdit.setText(loginJsonObject.Email);

		initInvoice();
		payLayout = (LinearLayout) findViewById(R.id.registration_pay_linearlayout);
		if (registrationType == RegistrationType.charge) {
			payLayout.setVisibility(View.VISIBLE);
		} else {
			payLayout.setVisibility(View.GONE);
		}
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		switch (registrationType) {
		case free:
			handleBackTitle.setTitleView(
					Utils.getResString(R.string.free_event), -1);
			break;
		case charge:
			handleBackTitle.setTitleView(
					Utils.getResString(R.string.pay_event), -1);
			break;
		}
		handleBackTitle
				.setRightText(Utils.getResString(R.string.sign_up), this);
		handleBackTitle.setListener(new OnBackBtnListener(this), null);

	}

	private RadioButton alipayBtn;
	private RadioButton needInvoiceBtn;
	private LinearLayout needInvoiceLayout;
	private RadioButton regularInvoiceBtn;
	private LinearLayout regularLayout;
	private LinearLayout addValueLayout;
	private RadioButton personalInvoiceBtn;
	private EditTextFont editRegularFirm;
	private LinearLayout firmLayout;
	private EditTextFont editRegularReceiver;
	private EditTextFont editRegularPhone;
	private EditTextFont editRegularAddress;
	private EditTextFont editRegularPostCode;
	private EditTextFont editAddFirm;
	private EditTextFont editAddTaxCode;
	private EditTextFont editAddRegistAddress;
	private EditTextFont editAddRegistPhone;
	private EditTextFont editAddDepositBank;
	private EditTextFont editAddBankAccount;
	private EditTextFont editAddReceiver;
	private EditTextFont editAddContactPhone;
	private EditTextFont editAddAddress;
	private EditTextFont editAddPostCode;
	private String payType = "2";// 2:支付宝 4：电汇
	// 以下是发票信息
	private boolean isInvoice = false;// 是否需要发票
	private String invoiceType = "1";// 发票类型 1:普通发票 2：增值发票
	private String invoicePayableTo = "1"; // 发票抬头 1:个人 2：公司
	private String firmName = "";// 单位名称
	private String taxCode = ""; // 纳税人识别号
	private String registAddress = "";// 注册地址
	private String registPhone = "";// 注册电话
	private String depositBank = "";// 开户银行
	private String bankAccount = "";// 银行账号
	private String addressee = "";// 收件人
	private String contactPhone = "";// 联系电话
	private String postingAddress = "";// 收件地址
	private String postCode = ""; // 邮编

	/**
	 * 初始化发票信息的布局
	 */
	private void initInvoice() {
		alipayBtn = (RadioButton) findViewById(R.id.radiobtn_alipay);
		needInvoiceBtn = (RadioButton) findViewById(R.id.registration_need);
		needInvoiceLayout = (LinearLayout) findViewById(R.id.registration_isneed_invoice_layout);
		regularInvoiceBtn = (RadioButton) findViewById(R.id.registration_regular_invoice);
		regularLayout = (LinearLayout) findViewById(R.id.registration_regular_invoice_layout);
		addValueLayout = (LinearLayout) findViewById(R.id.registration_valueadd_invoice_layout);
		personalInvoiceBtn = (RadioButton) findViewById(R.id.registration_personal_invoice);
		editRegularFirm = (EditTextFont) findViewById(R.id.invoice_regular_firm_edit);
		firmLayout = (LinearLayout) findViewById(R.id.invoice_regular_firm_layout);
		editRegularReceiver = (EditTextFont) findViewById(R.id.invoice_regular_receiver_edit);
		editRegularPhone = (EditTextFont) findViewById(R.id.invoice_regular_contact_phone_edit);
		editRegularAddress = (EditTextFont) findViewById(R.id.invoice_regular_posting_address_edit);
		editRegularPostCode = (EditTextFont) findViewById(R.id.invoice_regular_postcode_edit);
		editAddFirm = (EditTextFont) findViewById(R.id.invoice_valueadd_firm_edit);
		editAddTaxCode = (EditTextFont) findViewById(R.id.invoice_valueadd_taxcode_edit);
		editAddRegistAddress = (EditTextFont) findViewById(R.id.invoice_valueadd_regist_address_edit);
		editAddRegistPhone = (EditTextFont) findViewById(R.id.invoice_valueadd_regist_phone_edit);
		editAddDepositBank = (EditTextFont) findViewById(R.id.invoice_valueadd_deposit_bank_edit);
		editAddBankAccount = (EditTextFont) findViewById(R.id.invoice_valueadd_bank_account_edit);
		editAddReceiver = (EditTextFont) findViewById(R.id.invoice_valueadd_receiver_edit);
		editAddContactPhone = (EditTextFont) findViewById(R.id.invoice_valueadd_contact_phone_edit);
		editAddAddress = (EditTextFont) findViewById(R.id.invoice_valueadd_posting_address_edit);
		editAddPostCode = (EditTextFont) findViewById(R.id.invoice_valueadd_postcode_edit);

		alipayBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					payType = "2";// 支付宝支付
				} else {
					payType = "4";// 电汇转账
				}
			}
		});
		needInvoiceBtn
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// 需要发票
							needInvoiceLayout.setVisibility(View.VISIBLE);
							isInvoice = true;
						} else {
							// 不需要发票
							needInvoiceLayout.setVisibility(View.GONE);
							isInvoice = false;
						}
					}
				});
		regularInvoiceBtn
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// 普通发票
							invoiceType = "1";
							regularLayout.setVisibility(View.VISIBLE);
							addValueLayout.setVisibility(View.GONE);
						} else {
							// 增值发票
							invoiceType = "2";
							regularLayout.setVisibility(View.GONE);
							addValueLayout.setVisibility(View.VISIBLE);
						}
					}
				});
		personalInvoiceBtn
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// 个人发票
							invoicePayableTo = "1";
							firmLayout.setVisibility(View.GONE);
						} else {
							// 公司发票
							invoicePayableTo = "2";
							firmLayout.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_right_layout:
			if (registrationType == RegistrationType.free) {
				freeRegistration();
			} else {
				chargeRegistration();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 免费报名
	 */
	private void freeRegistration() {
		if (!checkUserInputIfno()) {
			return;
		}
		HttpCallBackDialog httpCallBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				UIManager.cancelAllProgressDialog();
				RegistrationResult registrationResult = Utils.parseJson(result,
						RegistrationResult.class);
				if (registrationResult != null) {
					if (registrationResult.Message.indexOf("报名成功") != -1) {
						DialogUtils.createSureDialog(RegistrationActivity.this,
								registrationResult.Message,
								Utils.getResString(R.string.sure),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								});
					} else {
						Utils.Toast(registrationResult.Message, 1);
					}
				}
			}
		};
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_EVENT;
		params.methodName = Constants.ApplicationEvent;
		params.paramList.put("eventID", eventDetailObject.ID);
		params.paramList.put("price",
				Integer.parseInt(eventDetailObject.PriceValue));
		params.paramList.put("userName",
				spfUtils.getString(SharedPreferenceUtils.USER_NAME));
		params.paramList.put("pwd",
				spfUtils.getString(SharedPreferenceUtils.PASSWORD));
		params.paramList.put("FullName", nameStr);
		params.paramList.put("company", companyStr);
		params.paramList.put("email", emailStr);
		params.paramList.put("jobTitle", jobStr);
		params.paramList.put("phone", "");
		params.paramList.put("mobilePhone", mobileStr);
		params.paramList.put("payType", "5");
		params.paramList.put("weibo", sinaweiboStr);
		params.paramList.put("weixin", weixinStr);
		params.paramList.put("key", Constants.KEY);
		params.paramList.put("mobileSys", "Android");

		WebService.requestAsynHttp(params, httpCallBackDialog, true,
				cancelObserver);
	}

	/**
	 * 付费报名
	 */
	private void chargeRegistration() {
		if (!checkUserInputIfno()) {
			return;
		}
		HttpCallBackDialog httpCallBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				UIManager.cancelAllProgressDialog();
				RegistrationResult registrationResult = Utils.parseJson(result,
						RegistrationResult.class);
				if (registrationResult != null) {
					if (registrationResult.Message.indexOf("报名成功") != -1) {
						if ("4".equals(payType)) {
							DialogUtils.createSureDialog(
									RegistrationActivity.this,
									registrationResult.Message,
									Utils.getResString(R.string.sure),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									});
						} else if ("2".equals(payType)) {
							if (registrationResult.Order != null) {
								orderId = registrationResult.Order.OrderID;
								price = registrationResult.Order.Price == null ? ""
										: registrationResult.Order.Price;
								PayBaseHelper payBaseHelper = new PayBaseHelper();
								payBaseHelper.pay(RequestPayTye.eventRegistrat,
										price, RegistrationActivity.this,
										mHandler);
							} else {
								Utils.Toast(Utils
										.getResString(R.string.registration_fail));
							}
						}
					} else {
						Utils.Toast(registrationResult.Message, 1);
					}
				}
			}
		};
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_EVENT;
		params.methodName = Constants.ApplicationPayEvent;
		params.paramList.put("eventID", eventDetailObject.ID);
		params.paramList.put("price",
				Integer.parseInt(eventDetailObject.PriceValue));
		params.paramList.put("userName",
				spfUtils.getString(SharedPreferenceUtils.USER_NAME));
		params.paramList.put("pwd",
				spfUtils.getString(SharedPreferenceUtils.PASSWORD));
		params.paramList.put("FullName", nameStr);
		params.paramList.put("company", companyStr);
		params.paramList.put("email", emailStr);
		params.paramList.put("jobTitle", jobStr);
		params.paramList.put("phone", "");
		params.paramList.put("mobilePhone", mobileStr);
		params.paramList.put("payType", "5");
		params.paramList.put("weibo", sinaweiboStr);
		params.paramList.put("weixin", weixinStr);
		params.paramList.put("key", Constants.KEY);
		params.paramList.put("mobileSys", "Android");
		params.paramList.put("payType", payType);
		params.paramList.put("isInvoice", isInvoice);
		params.paramList.put("invoiceType", invoiceType);
		params.paramList.put("invoicePayableTo", invoicePayableTo);
		params.paramList.put("companyName", firmName);
		params.paramList.put("taxCode", taxCode);
		params.paramList.put("regAddress", registAddress);
		params.paramList.put("regPhone", registPhone);
		params.paramList.put("bank", depositBank);
		params.paramList.put("bankAccount", bankAccount);
		params.paramList.put("addressee", addressee);
		params.paramList.put("address", postingAddress);
		params.paramList.put("postCode", postCode);
		params.paramList.put("contactPhone", contactPhone);

		WebService.requestAsynHttp(params, httpCallBackDialog, true,
				cancelObserver);

	}

	/**
	 * 检查用户是否正确的填写了信息
	 * 
	 * @return 正确填写了信息返回true，否则返回false
	 */
	private boolean checkUserInputIfno() {
		nameStr = nameEdit.getText().toString();
		if (TextUtils.isEmpty(nameStr)) {
			Utils.Toast(Utils.getResString(R.string.name_null));
			return false;
		}
		companyStr = companyEdit.getText().toString();
		if (TextUtils.isEmpty(companyStr)) {
			Utils.Toast(Utils.getResString(R.string.company_null));
			return false;
		}
		jobStr = jobEdit.getText().toString();
		if (TextUtils.isEmpty(jobStr)) {
			Utils.Toast(Utils.getResString(R.string.position_null));
			return false;
		}
		mobileStr = mobileEdit.getText().toString();
		if (TextUtils.isEmpty(mobileStr)) {
			Utils.Toast(Utils.getResString(R.string.phone_null));
			return false;
		} else {
			if (!StringUtils.isMobileNO(mobileStr)) {
				Utils.Toast(Utils.getResString(R.string.input_right_phone));
				return false;
			}
		}
		emailStr = emailEdit.getText().toString();
		if (TextUtils.isEmpty(emailStr)) {
			Utils.Toast(Utils.getResString(R.string.email_null));
			return false;
		} else {
			if (!StringUtils.isEmail(emailStr)) {
				Utils.Toast(Utils.getResString(R.string.input_right_email));
				return false;
			}
		}
		sinaweiboStr = sinaweiboEdit.getText().toString();
		weixinStr = weixinEdit.getText().toString();
		if (registrationType != RegistrationType.charge) {
			if (isInvoice) {
				if ("1".equals(invoiceType)) {
					// 普通发票
					if ("2".equals(invoicePayableTo)) {
						// 公司抬头
						firmName = editRegularFirm.getText().toString();
						if (!checkEditNotnull(editRegularFirm)) {
							return false;
						}
					}
					addressee = editRegularReceiver.getText().toString();
					if (!checkEditNotnull(editRegularReceiver)) {
						return false;
					}
					contactPhone = editRegularPhone.getText().toString();
					if (!checkEditNotnull(editRegularPhone)) {
						return false;
					}
					postingAddress = editRegularAddress.getText().toString();
					if (!checkEditNotnull(editRegularAddress)) {
						return false;
					}
					postCode = editRegularPostCode.getText().toString();
					if (!checkEditNotnull(editRegularPostCode)) {
						return false;
					}

				} else {
					// 增值发票
					firmName = editAddFirm.getText().toString();
					if (!checkEditNotnull(editAddFirm)) {
						return false;
					}
					taxCode = editAddTaxCode.getText().toString();
					if (!checkEditNotnull(editAddTaxCode)) {
						return false;
					}
					registAddress = editAddRegistAddress.getText().toString();
					if (!checkEditNotnull(editAddRegistAddress)) {
						return false;
					}
					registPhone = editAddRegistPhone.getText().toString();
					if (!checkEditNotnull(editAddRegistPhone)) {
						return false;
					}
					depositBank = editAddDepositBank.getText().toString();
					if (!checkEditNotnull(editAddDepositBank)) {
						return false;
					}
					bankAccount = editAddBankAccount.getText().toString();
					if (!checkEditNotnull(editAddBankAccount)) {
						return false;
					}
					addressee = editAddReceiver.getText().toString();
					if (!checkEditNotnull(editAddReceiver)) {
						return false;
					}
					contactPhone = editAddContactPhone.getText().toString();
					if (!checkEditNotnull(editAddContactPhone)) {
						return false;
					}
					postingAddress = editAddAddress.getText().toString();
					if (!checkEditNotnull(editAddAddress)) {
						return false;
					}
					postCode = editAddPostCode.getText().toString();
					if (!checkEditNotnull(editAddPostCode)) {
						return false;
					}
				}
			} else {
				firmName = "";
				taxCode = "";
				registAddress = "";
				registPhone = "";
				depositBank = "";
				bankAccount = "";
				addressee = "";
				contactPhone = "";
				postingAddress = "";
				postCode = "";
			}
		}
		return true;
	}

	// 检查所给的编辑框是否有值
	private boolean checkEditNotnull(EditTextFont editTextFont) {
		String s = editTextFont.getText().toString();
		if (TextUtils.isEmpty(s)) {
			Utils.Toast(Utils.getResString(R.string.input_whole_info));
			editTextFont.requestFocus();
			return false;
		}
		return true;
	}

	private void requestDoOrder() {
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_EVENT;
		params.methodName = Constants.DoOrder;
		params.paramList.put("orderID", orderId);
		params.paramList.put("price", price);
		params.paramList.put("key", Constants.KEY);

		WebService.requestAsynHttp(params, null, false, null);
	}
}
