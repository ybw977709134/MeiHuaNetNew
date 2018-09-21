package com.alipay;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.MeiHuaNet.R;
import com.MeiHuaNet.utils.Utils;
import com.alipay.android.app.sdk.AliPay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class PayBaseHelper {

	public static final int RQF_PAY =1;
	public PayBaseHelper(){
	}
	
	/**
	 *
	 * @description 从哪里发来的支付请求 
	 * @author lee
	 * @time  2013-12-26 下午5:53:15
	 *
	 */
	public static enum RequestPayTye{
		mediaVip,//传媒库升级vip
		originalityVip,//创意库升级vip
		eventRegistrat;//活动报名
	}
	
	RequestPayTye type;
	public void pay(RequestPayTye type, String money,final Activity activity, final Handler handler){
		try {
			this.type = type;
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo(money);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Utils.log("start pay");
			Utils.log("info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(activity, handler);
					
					//设置为沙箱模式，不设置默认为线上环境
					//alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);

					Utils.log(result);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					handler.sendMessage(msg);
				}
			}.start();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(activity, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private String getNewOrderInfo(String money) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(getOutTradeNo());
		if(type == RequestPayTye.eventRegistrat){
			sb.append("\"&subject=\"");
			sb.append("梅花网活动报名");
			sb.append("\"&body=\"");
			sb.append("B2B营销专场活动");
		} else {
			String subject = "";
			if(money.equals("498")){
				subject = "升级为梅花网VIP（全年)";
			} else {
				subject = "升级为梅花网VIP（半年)";
			}
			sb.append("\"&subject=\"");
			sb.append(subject);
			sb.append("\"&body=\"");
			if(type == RequestPayTye.mediaVip){
				sb.append("升级后可以查看更多的传媒库下刊例文件");
			} else if(type == RequestPayTye.originalityVip){
				sb.append("升级后可以观看更多的创意视频");
			}
			
		}
		sb.append("\"&total_fee=\"");
		sb.append(money);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
//		sb.append("\"&return_url=\"");
//		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
		
	}

	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
