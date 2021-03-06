package com.MeiHuaNet.activity.infomation;


import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.DetailBaseActivity;
import com.MeiHuaNet.entity.InfoDetailObject;
import com.MeiHuaNet.entity.InfoJsonObject;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.ShareSDKUtils;
import com.MeiHuaNet.utils.Utils;

import android.os.Bundle;
import android.text.TextUtils;

public class InfoDetailActivity extends DetailBaseActivity {

	String postID;
	InfoJsonObject infoJsonObject;
	InfoDetailObject infoDetailObject;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void setBodyContent() {
		infoJsonObject = (InfoJsonObject) getIntent().getSerializableExtra(
				"detailObject");
		if (infoJsonObject != null) {
			titleView.setText(infoJsonObject.Title);
			dateView.setText(infoJsonObject.getDate());
			if (infoJsonObject.getViews() > 0) {
				countView.setText(Utils.getResString(R.string.read)
						+ infoJsonObject.Views);
			}

			requestData();
		}
	}

	public void requestData() {
		HttpCallBackDialog httpCallBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				infoDetailObject = Utils.parseJson(result,
						InfoDetailObject.class);
				if (infoDetailObject != null) {
					setWebViewContent(infoDetailObject.Content);
				}
				UIManager.cancelAllProgressDialog();
			}
		};
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_INFO;
		params.methodName = Constants.GetPostDetail;
		params.paramList.put("key", Constants.KEY);
		params.paramList.put("postID", infoJsonObject.PostID);
		params.paramList.put("mobileSys", "Android");

		WebService.requestAsynHttp(params, httpCallBackDialog, true,
				cancelObserver);
	}

	@Override
	protected void share() {
		if (infoDetailObject != null) {
			String content = TextUtils.isEmpty(infoDetailObject.Description) ? infoDetailObject.Content
					: infoDetailObject.Description;
			ShareSDKUtils.share(this, infoDetailObject.Title, content,
					infoDetailObject.Url);
		}
	}
}
