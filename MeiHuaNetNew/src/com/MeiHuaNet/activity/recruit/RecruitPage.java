package com.MeiHuaNet.activity.recruit;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.Constants.Request_Type;
import com.MeiHuaNet.activity.BaseListPage;
import com.MeiHuaNet.adapter.RecruitAdapter;
import com.MeiHuaNet.entity.RecruitListJsonObject;
import com.MeiHuaNet.entity.RecruitObject;
import com.MeiHuaNet.listener.OnListItemClickListener;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.SlidingMenu;
import com.MeiHuaNet.view.NewRefreshListView.OnBottomRefreshListener;
import com.MeiHuaNet.view.NewRefreshListView.OnHeadRefreshListener;

public class RecruitPage extends BaseListPage{

	
	ArrayList<RecruitObject> mListData;
	RecruitAdapter mAdapter;
	
	public RecruitPage(SlidingMenu slidingMenu, Context context){
		super(slidingMenu, context, PageType.recruit);
	}
	
	
	@Override
	protected void isHaveData() {
		// TODO Auto-generated method stub
		mListData = FileUtils.fetchDataFromFile(mContext, RecruitObject.class, fileName, 30);
		if(mListData!=null && mListData.size()>0){
			initListView();
		} else {
			requestData(Request_Type.init);
		}
		searchStr = "";
	}


	@Override
	protected boolean isAddSearchBar() {
		// TODO Auto-generated method stub
		if (searchLayout == null && mListData != null && mListData.size() > 0)
			return true;
		return false;
	}


	@Override
	protected void onSuccessHandle(Request_Type request_Type, String result) {
		// TODO Auto-generated method stub
		RecruitListJsonObject recruitListJsonObject = Utils.parseJson(
				result, RecruitListJsonObject.class);
		if (recruitListJsonObject != null) {
			totalCount = recruitListJsonObject.getResultCount();
			if (mListData == null) {
				mListData = new ArrayList<RecruitObject>();
			}
			switch (request_Type) {
			case init:
			case search_init:
				mListData = recruitListJsonObject.Jobs;
				curtotalCount = mListData.size();
				initListView();
				break;
			case head_refresh:
				mListView.onHeadRefreshComplete();
				if (recruitListJsonObject.Jobs != null
						|| recruitListJsonObject.Jobs.size() > 0) {
					if(mListData.size()==0){
						mListData = recruitListJsonObject.Jobs;
						curtotalCount = mListData.size();
						initListView();
					} else {
						mListData.clear();
						mListData.addAll(recruitListJsonObject.Jobs);
						curtotalCount = mListData.size();
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
				mListData.addAll(recruitListJsonObject.Jobs);
				curtotalCount += recruitListJsonObject.Jobs.size();
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
				FileUtils.storeDataToFile(mContext, mListData, fileName, 10);
			}
		}
		UIManager.cancelAllProgressDialog();
	}


	@Override
	protected void initListView() {
		// TODO Auto-generated method stub
		if (mAdapter == null) {
			mAdapter = new RecruitAdapter(mContext);
		}
		mAdapter.setListData(mListData);
		mAdapter.setItemListener(new OnListItemClickListener() {

			@Override
			public void onclick(View v, int position) {
				// TODO Auto-generated method stub
				RecruitObject recruitObject = mListData.get(position);
				if (recruitObject == null) {
					return;
				}
				Intent i = new Intent();
				i.setClass(mContext,
						RecruitDetailActivity.class);
				i.putExtra("detailObject", recruitObject);
				mContext.startActivity(i);
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		if ((curtotalCount < totalCount || (curtotalCount == -1 && totalCount == -1))
				&& mListData != null && mListData.size() > 0) {
			Utils.log("bottom can refersh" + "cur is : " + curtotalCount
					+ " total is : " + totalCount);
			mListView.setBottomRefreshable(true);
		} else {
			Utils.log("bottom can't refersh" + "cur is : " + curtotalCount
					+ " total is : " + totalCount);
			mListView.setBottomRefreshable(false);
		}
	}


}
