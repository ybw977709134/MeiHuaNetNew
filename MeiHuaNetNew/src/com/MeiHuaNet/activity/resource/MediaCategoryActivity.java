package com.MeiHuaNet.activity.resource;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.adapter.MediaCategoryAdapter;
import com.MeiHuaNet.entity.MediaCategoryObject;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.Utils;

/**
 * 
 * @description 中国传媒库分类列表
 * @author lee
 * @time 2013-12-11 下午2:05:45
 * 
 */
public class MediaCategoryActivity extends BaseActivity {

	private ListView mListView;
	private MediaCategoryAdapter mAdapter;
	private ArrayList<MediaCategoryObject> mediaCategoryList;
	private String fileName = "mediacategory.obj";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_media_category);

		initView();
	}

	private void initView() {
		initTitle();

		LinearLayout layout = (LinearLayout) findViewById(R.id.media_category_alllayout);
		GestureDetectorUtil.setGestureDetector(this, layout, null);

		mListView = (ListView) findViewById(R.id.media_category_listview);
		mediaCategoryList = FileUtils.fetchDataFromFile(
				MediaCategoryActivity.this, MediaCategoryObject.class,
				fileName, 60 * 3);
		if(mediaCategoryList == null || mediaCategoryList.size()==0){
			requestMediaCategory();
		} else {
			setListView(); 
		}
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView(Utils.getResString(R.string.media_store),
				-1);
		handleBackTitle.setListener(new OnBackBtnListener(this), null);
	}

	private void requestMediaCategory() {
		HttpCallBackDialog callBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				UIManager.cancelAllProgressDialog();
				mediaCategoryList = Utils.parseJsonArray(result,
						MediaCategoryObject.class);
				if (mediaCategoryList != null && mediaCategoryList.size() > 0) {
					FileUtils.storeDataToFile(MediaCategoryActivity.this,
							mediaCategoryList, fileName, 0);
					setListView();
				}
			}
		};
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_MEDIA;
		params.methodName = Constants.GetMediaType;
		params.paramList.put("key", Constants.KEY);

		WebService
				.requestAsynHttp(params, callBackDialog, true, cancelObserver);
	}

	private void setListView() {
		if (mAdapter == null) {
			mAdapter = new MediaCategoryAdapter(this, mediaCategoryList);
		}
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MediaCategoryObject mediaCategoryObject = mediaCategoryList
						.get(position);
				Intent i = new Intent();
				i.setClass(MediaCategoryActivity.this,
						MediainfoListActivity.class);
				i.putExtra("mediaType", mediaCategoryObject.ID);
				startActivity(i);
			}
		});
	}
}
