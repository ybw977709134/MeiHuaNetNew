package com.MeiHuaNet.utils;

import android.content.Context;

import com.MeiHuaNet.R;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShareSDKUtils {

	public static void share(Context context, String title, String text,
			String webUrl) {

		String handleTitle ="【" +StringUtils.handleContent(title)+"】";
		String handleContent = StringUtils.handleContent(text);
		// 新浪微博中2个英文算一个字长度
		int urlLength = webUrl == null ? 0 : webUrl.length() / 2;
		int titleLength = handleTitle == null ? 0 : title.length();
		String content = "";
		
		if((urlLength+titleLength)<140){
			int len = 140 -(urlLength+titleLength);
			if(len>6){
				content = handleTitle+handleContent.substring(0, len-6)+"......"+webUrl;
			} else {
				content = handleTitle+handleContent.substring(0, len)+webUrl;
			}
		} else {
			content = handleTitle + webUrl;
		}
		
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher,
				Utils.getResString(R.string.app_name));
		oks.setTitle(title);
		oks.setTitleUrl(webUrl);
		oks.setText(content);
		oks.setUrl(webUrl);
		oks.setComment("来自梅花网的分享");
		oks.setSite(Utils.getResString(R.string.app_name));
		oks.setSiteUrl("http://meihua.info");
		oks.setSilent(false);
		oks.show(context);

		// oks.setAddress("12345678901");
		// oks.setImagePath(MainActivity.TEST_IMAGE);
		// imageurl是分享网络图片。新浪微博没有开通这个功能接口，分享时会出错
		// oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
		// oks.setFilePath(MainActivity.TEST_IMAGE);
		// oks.setVenueName("Share SDK");
		// oks.setVenueDescription("This is a beautiful place!");
		// oks.setLatitude(23.056081f);
		// oks.setLongitude(113.385708f);
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());

	}
}
