package com.MeiHuaNet.activity.infomation;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.MeiHuaNet.Constants.Request_Type;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.BaseListPage;
import com.MeiHuaNet.adapter.InfoAdapter;
import com.MeiHuaNet.entity.InfoJsonObject;
import com.MeiHuaNet.entity.InfoListJsonObject;
import com.MeiHuaNet.listener.OnListItemClickListener;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.NewRefreshListView.OnBottomRefreshListener;
import com.MeiHuaNet.view.NewRefreshListView.OnHeadRefreshListener;
import com.MeiHuaNet.view.SlidingMenu;

public class InformationPage extends BaseListPage {

	ArrayList<InfoJsonObject> mListData;
	InfoAdapter mAdapter;

	public InformationPage(SlidingMenu slidingMenu, Context context) {
		super(slidingMenu, context, PageType.information);

	}

	@Override
	protected void isHaveData() {
		mListData = FileUtils.fetchDataFromFile(mContext, InfoJsonObject.class,
				fileName, 30);
		if (mListData != null && mListData.size() > 0) {
			pageindex++;
			initListView();
		} else {
			requestData(Request_Type.init);
		}
		searchStr = "";
	}

	@Override
	protected void initListView() {
		if (mAdapter == null) {
			mAdapter = new InfoAdapter(mContext);
		}
		mAdapter.setListData(mListData);
		mAdapter.setItemListener(new OnListItemClickListener() {

			@Override
			public void onclick(View v, int position) {
				switch (v.getId()) {
				case R.id.info_item_all_layout:
					InfoJsonObject infoJsonObject = mListData.get(position);
					if (infoJsonObject == null || infoJsonObject.PostID == null) {
						return;
					}
					Intent intent = new Intent();
					intent.setClass(mContext, InfoDetailActivity.class);
					intent.putExtra("detailObject", infoJsonObject);
					mContext.startActivity(intent);
					break;
				}
			}
		});
		setSearchBar();
		setBottomRefresh();
		mListView.setAdapter(mAdapter);
		if (mListData != null && mListData.size() > 0) {
			// 不用post,则setselection()这个方法有时有效，有时无效，原因未知
			new Handler().post(new Runnable() {

				@Override
				public void run() {
					mListView.setSelection(2);
				}
			});

		}

		mListView.setOnHeadRefreshListener(new OnHeadRefreshListener() {

			@Override
			public void onRefresh() {
				searchStr = "";
				if(mSearchEditView!= null){
					mSearchEditView.setText("");
				}
				requestData(Request_Type.head_refresh);
			}
		});
		mListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {

			@Override
			public void bottomRefresh() {
				if (TextUtils.isEmpty(searchStr)) {
					requestData(Request_Type.bottom_refresh);
				} else {
					requestData(Request_Type.search_bottom_refresh);
				}
			}
		});
	}

	/**
	 * 设置底部是否可以刷新
	 */
	protected void setBottomRefresh() {
		if ((curtotalCount < totalCount || (curtotalCount == -1 && totalCount == -1))
				&& mListData != null && mListData.size() > 0) {
			mListView.setBottomRefreshable(true);
		} else {
			mListView.setBottomRefreshable(false);
		}
	}
	
	@Override
	protected boolean isAddSearchBar() {
		if (searchLayout == null && mListData != null && mListData.size() > 0)
			return true;
		return false;
	}


	/**
	 * 从服务器获取数据成功后的处理
	 */
	@Override
	protected void onSuccessHandle(Request_Type type, String result) {
		InfoListJsonObject infoListJsonObject = Utils.parseJson(result,
				InfoListJsonObject.class);
		if (infoListJsonObject != null) {
			totalCount = infoListJsonObject.ResultCount;
			if (mListData == null) {
				mListData = new ArrayList<InfoJsonObject>();
			}
			switch (type) {
			case init:
			case search_init:
				mListData = infoListJsonObject.Posts;
				curtotalCount = infoListJsonObject.Posts.size();
				initListView();
				break;
			case head_refresh:
				mListView.onHeadRefreshComplete();
				if (infoListJsonObject.Posts != null
						|| infoListJsonObject.Posts.size() > 0) {
					if (mListData.size() == 0) {
						mListData = infoListJsonObject.Posts;
						curtotalCount = infoListJsonObject.Posts.size();
						initListView();
					} else {
						mListData.clear();
						mListData.addAll(infoListJsonObject.Posts);
						curtotalCount = infoListJsonObject.Posts.size();
						setBottomRefresh();
						if (mAdapter != null) {
							mAdapter.notifyDataSetChanged();
						}
					}
				}
				break;
			case bottom_refresh:
			case search_bottom_refresh:
				mListView.onBottomRefreshComplete();
				mListData.addAll(infoListJsonObject.Posts);
				curtotalCount += infoListJsonObject.Posts.size();
				setBottomRefresh();
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
			if (type == Request_Type.search_init
					|| type == Request_Type.search_bottom_refresh) {

			} else {
				FileUtils.storeDataToFile(mContext, mListData, fileName, 10);
			}
		}
		UIManager.cancelAllProgressDialog();
	}

}
