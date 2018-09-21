package com.MeiHuaNet.activity.resource;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.MeiHuaNet.Constants.Request_Type;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.SearchBackActivity;
import com.MeiHuaNet.adapter.MediaInfoAdapter;
import com.MeiHuaNet.entity.MediaListJsonObject;
import com.MeiHuaNet.entity.MediainfoObject;
import com.MeiHuaNet.listener.OnListItemClickListener;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.NewRefreshListView.OnBottomRefreshListener;
import com.MeiHuaNet.view.NewRefreshListView.OnHeadRefreshListener;

/**
 *
 * @description 媒体信息列表页面 
 * @author lee
 * @time  2013-12-11 下午3:53:52
 *
 */
public class MediainfoListActivity extends SearchBackActivity{

	MediaInfoAdapter mAdapter;
	ArrayList<MediainfoObject> mListData;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		curBack_Type = Back_Type.media_info;
		mediaType = getIntent().getStringExtra("mediaType");
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected boolean isAddSearchBar() {
		if (searchLayout == null && mListData != null && mListData.size() > 0)
			return true;
		return false;
	}

	@Override
	protected void isHaveCacheData() {
		mListData = FileUtils.fetchDataFromFile(this, MediainfoObject.class,
				fileName, 30);
		if (mListData != null && mListData.size() > 0) {
			pageindex++;
			InitListView();
		} else {
			requestData(Request_Type.init);
		}
	}

	@Override
	protected void onSuccessHandle(Request_Type request_Type, String result) {
		MediaListJsonObject mediaListJsonObject = Utils.parseJson(
				result, MediaListJsonObject.class);
		if (mediaListJsonObject != null) {
			totalCount = mediaListJsonObject.getResultCount();
			if (mListData == null) {
				mListData = new ArrayList<MediainfoObject>();
			}
			switch (request_Type) {
			case init:
			case search_init:
				mListData = mediaListJsonObject.List;
				curTotalCount = mListData.size();
				InitListView();
				break;
			case head_refresh:
				mListView.onHeadRefreshComplete();
				if (mediaListJsonObject.List != null
						|| mediaListJsonObject.List.size() > 0) {
					if(mListData.size()==0){
						mListData = mediaListJsonObject.List;
						curTotalCount = mListData.size();
						InitListView();
					} else {
						mListData.clear();
						mListData.addAll(mediaListJsonObject.List);
						curTotalCount = mListData.size();
						setBottomRefresh();
						if (mAdapter != null) {
							mAdapter.notifyDataSetChanged();
						}
					}
					
				}
				break;
			case search_bottom_refresh:
			case bottom_refresh:
				mListView.onBottomRefreshComplete();
				mListData.addAll(mediaListJsonObject.List);
				curTotalCount += mediaListJsonObject.List.size();
				setBottomRefresh();
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
			if (request_Type == Request_Type.init
					|| request_Type == Request_Type.head_refresh
					|| request_Type == Request_Type.bottom_refresh) {
				FileUtils.storeDataToFile(this, mListData, fileName, 10);
			}
		}
		UIManager.cancelAllProgressDialog();
	}

	@Override
	protected void InitListView() {
		if (mAdapter == null) {
			mAdapter = new MediaInfoAdapter(this);
		}
		mAdapter.setListData(mListData);
		mAdapter.setItemListener(new OnListItemClickListener() {

			@Override
			public void onclick(View v, int position) {
				MediainfoObject mediainfoObject = mListData.get(position);
				if (mediainfoObject == null) {
					return;
				}
				Intent i = new Intent();
				i.setClass(MediainfoListActivity.this,
						MediaDetailActivity.class);
				i.putExtra("mediaID", mediainfoObject.ID);
				startActivity(i);
			}
		});
		
		setBottomRefresh();
		setSearchBar();
		if(mListData!=null && mListData.size()>0){
			setSelection(2);
		}
		
		mListView.setAdapter(mAdapter);
		mListView.setOnHeadRefreshListener(new OnHeadRefreshListener() {
			
			@Override
			public void onRefresh() {
				searchStr ="";
				if(mSearchEditView!=null){
					mSearchEditView.setText("");
				}
				requestData(Request_Type.head_refresh);
			}
		});
		mListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
			
			@Override
			public void bottomRefresh() {
				if(searchStr!=null && searchStr.length()>0){
					requestData(Request_Type.bottom_refresh);
				} else {
					requestData(Request_Type.search_bottom_refresh);
				}
			}
		});
	}

	@Override
	protected void setBottomRefresh() {
		if ((curTotalCount < totalCount || (curTotalCount == -1 && totalCount == -1))
				&& mListData != null && mListData.size() > 0) {
			mListView.setBottomRefreshable(true);
		} else {
			mListView.setBottomRefreshable(false);
		}
	}
}
