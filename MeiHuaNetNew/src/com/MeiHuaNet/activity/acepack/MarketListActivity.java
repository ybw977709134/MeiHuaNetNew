package com.MeiHuaNet.activity.acepack;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.Constants.Request_Type;
import com.MeiHuaNet.activity.SearchBackActivity;
import com.MeiHuaNet.adapter.MarketInfoAdapter;
import com.MeiHuaNet.entity.KnowledgeObject;
import com.MeiHuaNet.entity.MarketInfoJsonObject;
import com.MeiHuaNet.listener.OnListItemClickListener;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.NewRefreshListView.OnBottomRefreshListener;
import com.MeiHuaNet.view.NewRefreshListView.OnHeadRefreshListener;

/**
 * 
 * @description 营销百科的列表页
 * @author lee
 * @time 2013-10-31 下午1:22:03
 * 
 */
public class MarketListActivity extends SearchBackActivity {

	MarketInfoAdapter mAdapter;
	ArrayList<KnowledgeObject> mListData;

	public void onCreate(Bundle savedInstanceState) {
		curBack_Type = Back_Type.marketing_info;
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
		mListData = FileUtils.fetchDataFromFile(this, KnowledgeObject.class,
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
		MarketInfoJsonObject knowLedgeListJsonObject = Utils.parseJson(result,
				MarketInfoJsonObject.class);
		if (knowLedgeListJsonObject != null) {
			totalCount = knowLedgeListJsonObject.getResultCount();
			if (mListData == null) {
				mListData = new ArrayList<KnowledgeObject>();
			}
			switch (request_Type) {
			case init:
			case search_init:
				mListData = knowLedgeListJsonObject.Wikis;
				curTotalCount = mListData.size();
				InitListView();
				break;
			case head_refresh:
				mListView.onHeadRefreshComplete();
				if (knowLedgeListJsonObject.Wikis != null
						|| knowLedgeListJsonObject.Wikis.size() > 0) {
					if (mListData.size() == 0) {
						mListData = knowLedgeListJsonObject.Wikis;
						curTotalCount = mListData.size();
						InitListView();
					} else {
						mListData.clear();
						mListData.addAll(knowLedgeListJsonObject.Wikis);
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
				mListData.addAll(knowLedgeListJsonObject.Wikis);
				curTotalCount += knowLedgeListJsonObject.Wikis.size();
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
			mAdapter = new MarketInfoAdapter(this);
		}
		mAdapter.setListData(mListData);
		mAdapter.setItemListener(new OnListItemClickListener() {

			@Override
			public void onclick(View v, int position) {
				KnowledgeObject knowledgeObject = mListData.get(position);
				if (knowledgeObject == null) {
					return;
				}
				Intent i = new Intent();
				i.setClass(MarketListActivity.this,
						MarketInfoDetailActivity.class);
				i.putExtra("detailObject", knowledgeObject);
				startActivity(i);
			}
		});

		setBottomRefresh();
		setSearchBar();
		if (mListData != null && mListData.size() > 0) {
			setSelection(2);
		}

		mListView.setAdapter(mAdapter);
		mListView.setOnHeadRefreshListener(new OnHeadRefreshListener() {

			@Override
			public void onRefresh() {
				searchStr = "";
				if(mSearchEditView!=null){
					mSearchEditView.setText("");
				}
				requestData(Request_Type.head_refresh);
			}
		});
		mListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {

			@Override
			public void bottomRefresh() {
				if (searchStr != null && searchStr.length() > 0) {
					requestData(Request_Type.search_bottom_refresh);
				} else {
					requestData(Request_Type.bottom_refresh);
				}
			}
		});
	}

	/**
	 * 设置底部是否可以刷新
	 */
	protected void setBottomRefresh() {
		if ((curTotalCount < totalCount || (curTotalCount == -1 && totalCount == -1))
				&& mListData != null && mListData.size() > 0) {
			Utils.log("bottom can refersh" + "cur is : " + curTotalCount
					+ " total is : " + totalCount);
			mListView.setBottomRefreshable(true);
		} else {
			Utils.log("bottom can't refersh" + "cur is : " + curTotalCount
					+ " total is : " + totalCount);
			mListView.setBottomRefreshable(false);
		}
	}
}
