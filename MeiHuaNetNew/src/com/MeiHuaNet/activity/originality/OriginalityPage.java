package com.MeiHuaNet.activity.originality;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.adapter.FreeOriginalityAdapter;
import com.MeiHuaNet.adapter.OriginalityCategoryAdapter;
import com.MeiHuaNet.entity.OriginalityCategory;
import com.MeiHuaNet.entity.OriginalityObject;
import com.MeiHuaNet.listener.OnListItemClickListener;
import com.MeiHuaNet.listener.OnSlidingBtnClickListener;
import com.MeiHuaNet.network.HttpCallbackListener;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.HandleSlidTitle;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.LinearListView;
import com.MeiHuaNet.view.SlidingMenu;
import com.MeiHuaNet.view.TextViewFont;

public class OriginalityPage {

	private View me;
	private Context mContext;
	private SlidingMenu mSlidingMenu;
	private RelativeLayout freelayout;
	private LinearListView freeList;
	private LinearListView categoryList;
	private TextViewFont categoryLayout;
	private ScrollView scrollView;
	private String freeFileName = "freeOriginality.obj";
	private String categoryFileName = "categoryOriginality.obj";
	private boolean freeCompleted = true;
	private boolean categoryCompleted = true;
	private ArrayList<OriginalityCategory> categoryListData;
	private ArrayList<OriginalityObject> freeListData;

	public OriginalityPage(SlidingMenu slidingMenu, Context context) {
		mSlidingMenu = slidingMenu;
		mContext = context;

		me = View.inflate(context, R.layout.layout_originality, null);

		initView();
	}

	public View getView() {
		return me;
	}

	private void initView() {
		initTitle();

		scrollView = (ScrollView) me.findViewById(R.id.originality_scroll);
		freelayout = (RelativeLayout) me
				.findViewById(R.id.originality_free_layout);
		freeList = (LinearListView) me
				.findViewById(R.id.originality_free_listview);
		categoryLayout = (TextViewFont) me
				.findViewById(R.id.originality_category_layout);
		categoryList = (LinearListView) me
				.findViewById(R.id.originality_listview);
		scrollView.setVisibility(View.GONE);
		scrollView.setOnTouchListener(Utils.getBodyOntouchListener(mSlidingMenu));

		freeListData = FileUtils.fetchDataFromFile(mContext,
				OriginalityObject.class, freeFileName, 60 * 3);
		categoryListData = FileUtils.fetchDataFromFile(mContext,
				OriginalityCategory.class, categoryFileName, 60 * 24);
		if (freeListData != null && freeListData.size() > 0
				&& categoryListData != null && categoryListData.size() > 0) {
			// 如果今日免费和创意分类列表的对象都不为空，就显示页面
			showContent();
		} else {
			if (freeListData == null || freeListData.size() == 0) {
				requestFreeOriginality();
			}
			if (categoryListData == null || categoryListData.size() == 0) {
				requestOriginalityCategory();
			}
		}
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) me
				.findViewById(R.id.title_relative_layout);
		HandleSlidTitle handleSlidTitle = new HandleSlidTitle(relativeLayout);
		handleSlidTitle.setTitle(Utils.getResString(R.string.originality), -1);
		handleSlidTitle.setListener(
				new OnSlidingBtnClickListener(mSlidingMenu), null);
	}

	// 请求今日免费创意列表
	private void requestFreeOriginality() {
		freeCompleted = false;
		HttpCallbackListener callbackListener = new HttpCallbackListener() {

			@Override
			public void onSuccessCallBack(String result) {
				freeCompleted = true;
				freeListData = Utils.parseJsonArray(result,
						OriginalityObject.class);
				if (freeListData != null) {
					FileUtils.storeDataToFile(mContext, freeListData,
							freeFileName, 0);
				}

				if (freeCompleted && categoryCompleted) {
					showContent();
					UIManager.cancelAllProgressDialog();
				}
			}

			@Override
			public void onErrorCallBack() {
				freeCompleted = true;
				if (freeCompleted && categoryCompleted) {
					UIManager.cancelAllProgressDialog();
				}
			}

			@Override
			public void onCancelCallBack() {
				freeCompleted = true;
				if (freeCompleted && categoryCompleted) {
					UIManager.cancelAllProgressDialog();
				}
			}
		};
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL;
		params.methodName = Constants.CreativeTodayFrees;
		WebService.requestAsynHttp(params, callbackListener, true, null);
	}

	// 请求创意分类列表
	private void requestOriginalityCategory() {
		categoryCompleted = false;
		HttpCallbackListener callbackListener = new HttpCallbackListener() {

			@Override
			public void onSuccessCallBack(String result) {
				categoryCompleted = true;
				categoryListData = Utils.parseJsonArray(result,
						OriginalityCategory.class);
				if (categoryListData != null) {
					FileUtils.storeDataToFile(mContext, categoryListData,
							categoryFileName, 0);
				}

				if (freeCompleted && categoryCompleted) {
					showContent();
					UIManager.cancelAllProgressDialog();
				}

			}

			@Override
			public void onErrorCallBack() {
				categoryCompleted = true;
				if (freeCompleted && categoryCompleted) {
					UIManager.cancelAllProgressDialog();
				}
			}

			@Override
			public void onCancelCallBack() {
				categoryCompleted = true;
				if (freeCompleted && categoryCompleted) {
					UIManager.cancelAllProgressDialog();
				}
			}
		};
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL;
		params.methodName = Constants.CreativeCategory;
		WebService.requestAsynHttp(params, callbackListener, true, null);
	}

	/**
	 * 显示页面内容
	 */
	private void showContent() {
		scrollView.setVisibility(View.VISIBLE);
		if (freeListData == null || freeListData.size() == 0) {
			freelayout.setVisibility(View.GONE);
			freeList.setVisibility(View.GONE);
		} else {
			setFreeListView();
		}
		if (categoryListData == null || categoryListData.size() == 0) {
			categoryLayout.setVisibility(View.GONE);
			categoryList.setVisibility(View.GONE);
		} else {
			setCategoryListView();
		}
	}

	// 设置免费创意列表
	private void setFreeListView() {
		FreeOriginalityAdapter adapter = new FreeOriginalityAdapter(mContext,
				freeListData);
		adapter.setItemListener(new OnListItemClickListener() {

			@Override
			public void onclick(View v, int position) {
				OriginalityObject object = freeListData.get(position);
				if (object == null || object.url == null) {
					return;
				}
				Intent i = Utils.getVideoFileIntent(object.url);
				mContext.startActivity(i);
			}
		});
		freeList.setAdapter(adapter);
	}

	// 设置创意分裂列表
	private void setCategoryListView() {
		OriginalityCategoryAdapter adapter = new OriginalityCategoryAdapter(
				mContext, categoryListData);
		adapter.setItemListener(new OnListItemClickListener() {

			@Override
			public void onclick(View v, int position) {
				OriginalityCategory originalityCategory = categoryListData
						.get(position);
				if (originalityCategory == null
						|| originalityCategory.ID == null) {
					return;
				}
				Intent i = new Intent();
				i.setClass(mContext, OriginalityListActivity.class);
				i.putExtra("originalityObject", originalityCategory);
				mContext.startActivity(i);
			}
		});
		categoryList.setAdapter(adapter);
	}
	
	
}
