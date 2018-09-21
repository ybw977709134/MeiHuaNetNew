package com.MeiHuaNet.activity.acepack;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.DetailBaseActivity;
import com.MeiHuaNet.entity.InfoDetailObject;
import com.MeiHuaNet.entity.KnowledgeObject;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.ShareSDKUtils;
import com.MeiHuaNet.utils.Utils;

import android.os.Bundle;
import android.text.TextUtils;

public class KnowledgeDetailActivity extends DetailBaseActivity{

	InfoDetailObject infoDetailObject;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void setBodyContent() {
		knowledgeObject = (KnowledgeObject) getIntent().getSerializableExtra("detailObject");
		if(knowledgeObject !=null){
			titleView.setText(knowledgeObject.Title);
			dateView.setText(knowledgeObject.getDate());
			if(knowledgeObject.getViews()>0){
				countView.setText(Utils.getResString(R.string.read)+knowledgeObject.Views);
			}
			
			requestData();
		}
	}

	@Override
	protected void requestData() {
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
		params.webServiceUrl = Constants.WEBSERVICE_URL;
		params.methodName = Constants.GetArticleDetail;
		params.paramList.put("key", Constants.KEY);
		params.paramList.put("id", knowledgeObject.ID);
		params.paramList.put("mobileSys", "Android");

		WebService.requestAsynHttp(params, httpCallBackDialog, true,
				cancelObserver);
	}

	@Override
	protected void share() {
		if (infoDetailObject != null) {
			String shareContent = TextUtils
					.isEmpty(infoDetailObject.Description) ? infoDetailObject.Content
					: infoDetailObject.Description;
			ShareSDKUtils.share(this, infoDetailObject.Title, shareContent, infoDetailObject.Url);
			
		}
	}

}
