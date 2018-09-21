package com.MeiHuaNet.activity;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.MeiHuaNet.Constants.Request_Type;
import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.listener.ActivityCancelObserver;
import com.MeiHuaNet.listener.OnSlidingBtnClickListener;
import com.MeiHuaNet.network.HttpCallbackListener;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.HandleSlidTitle;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.EditTextFont;
import com.MeiHuaNet.view.NewRefreshListView;
import com.MeiHuaNet.view.SlidingMenu;

/**
 * 带搜索框的listview显示页面的基类
 * 
 * @description
 * @author lee
 * @time 2013-10-15 上午10:54:45
 * 
 */
public abstract class BaseListPage {

	protected SlidingMenu mSlidingMenu;
	protected Context mContext;
	protected View me;
	protected NewRefreshListView mListView;
	protected int pageindex = 1;
	protected String fileName = "";
	protected ActivityCancelObserver cancelObserver;
	
	protected EditTextFont mSearchEditView;
	protected ImageView mSearchArrow;
	protected String searchStr;
	protected View searchLayout;
	
	/*
	 * 已加载的条数
	 */
	protected int curtotalCount = -1;
	/**
	 * 服务器端所有数据的总的条数
	 */
	protected int totalCount = -1;

	public static enum PageType {
		information, recruit;
	}
	protected PageType curType;

	public BaseListPage(SlidingMenu slidingMenu, Context context,
			PageType pageType) {
		mSlidingMenu = slidingMenu;
		mContext = context;
		cancelObserver = new ActivityCancelObserver();
		curType = pageType;

		initView(pageType);
	}

	public View getView() {
		return me;
	}

	private void initView(PageType pageType) {
		me = View.inflate(mContext, R.layout.layout_search_list_page, null);
		initTitle(pageType);

		mListView = (NewRefreshListView) me
				.findViewById(R.id.base_page_listview);
		switch (pageType) {
		case information:
			fileName = "information.obj";
			break;

		case recruit:
			fileName = "recruit.obj";
			break;
		}
		isHaveData();

	}

	private void initTitle(PageType pageType) {
		RelativeLayout relativeLayout = (RelativeLayout) me
				.findViewById(R.id.title_relative_layout);
		HandleSlidTitle handleSlidTitle = new HandleSlidTitle(relativeLayout);
		switch (pageType) {
		case information:
			handleSlidTitle.setTitle(Utils.getResString(R.string.information),
					-1);
			break;
		case recruit:
			handleSlidTitle.setTitle(Utils.getResString(R.string.recruit), -1);
			break;
		}
		handleSlidTitle.setListener(
				new OnSlidingBtnClickListener(mSlidingMenu), null);
	}
	
	/**
	 * 设置listview顶部搜索条
	 */
	protected void setSearchBar() {
		if (isAddSearchBar()) {
			searchLayout = View.inflate(mContext, R.layout.layout_search, null);
			mSearchEditView = (EditTextFont) searchLayout
					.findViewById(R.id.today_search_edittext);
			mSearchArrow = (ImageView) searchLayout
					.findViewById(R.id.today_search_arrow);
			mListView.addHeaderView(searchLayout, null, false);

			mSearchArrow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					searchStr = mSearchEditView.getText().toString();
					if (TextUtils.isEmpty(searchStr)) {
						return;
					}
					requestData(Request_Type.search_init);
				}
			});
		}
	}
	
	protected void requestData(final Request_Type request_Type) {
		// TODO Auto-generated method stub
		HttpCallbackListener httpCallbackListener = new HttpCallbackListener() {

			@Override
			public void onSuccessCallBack(String result) {
				// TODO Auto-generated method stub
				onSuccessHandle(request_Type, result);
			}

			@Override
			public void onErrorCallBack() {
				// TODO Auto-generated method stub
				onErrorHandle(request_Type);
			}

			@Override
			public void onCancelCallBack() {
				// TODO Auto-generated method stub
				onCancelHandle(request_Type);
			}
		};

		WebServiceParams params = new WebServiceParams();
		setRequestParams(request_Type, params);

		if (request_Type == Request_Type.init
				|| request_Type == Request_Type.search_init) {
			WebService.requestAsynHttp(params, httpCallbackListener, true,
					cancelObserver);
		} else {
			WebService.requestAsynHttp(params, httpCallbackListener, false,
					cancelObserver);
		}
	}
	
	/**
	 * 根据请求类型，设置请求参数
	 */
	protected void setRequestParams(Request_Type type, WebServiceParams params) {
		// TODO Auto-generated method stub

		if (type == Request_Type.init || type == Request_Type.head_refresh
				|| type == Request_Type.search_init) {
			pageindex = 1;
		}
		if(curType == PageType.information){
			params.webServiceUrl = Constants.WEBSERVICE_URL_INFO;
			params.methodName = Constants.GetPostByPage;
			params.paramList.put("pagesize", Constants.PAGE_SIZE);
		} else if (curType == PageType.recruit){
			params.webServiceUrl = Constants.WEBSERVICE_URL_RECRUIT;
			params.methodName = Constants.GetJobList;
			params.paramList.put("pageSize", Constants.PAGE_SIZE);
		}
		params.paramList.put("key", Constants.KEY);
		params.paramList.put("pageIndex", pageindex++);
		if (type == Request_Type.search_init
				|| type == Request_Type.search_bottom_refresh) {
			params.paramList.put("keywords", searchStr);
		} else {
			params.paramList.put("keywords", "");
		}
	}
	
	protected void setSelection(final int position) {
		// 不用post,则setselection()这个方法有时有效，有时无效，原因未知
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mListView.setSelection(position);
			}
		});
	}
	
	protected void onErrorHandle(Request_Type type) {
		// TODO Auto-generated method stub
		switch (type) {
		case init:
		case search_init:
			UIManager.cancelAllProgressDialog();
			break;
		case head_refresh:
			mListView.onHeadRefreshComplete();
			break;
		case search_bottom_refresh:
		case bottom_refresh:
			pageindex--;
			mListView.onBottomRefreshComplete();
			break;
		default:
			break;
		}

	}

	protected void onCancelHandle(Request_Type type) {
		// TODO Auto-generated method stub
		switch (type) {
		case init:
		case search_init:
			UIManager.cancelAllProgressDialog();
			break;
		case head_refresh:
			mListView.onHeadRefreshComplete();
			break;
		case bottom_refresh:
		case search_bottom_refresh:
			pageindex--;
			mListView.onBottomRefreshComplete();
			UIManager.cancelAllProgressDialog();
			break;
		default:
			break;
		}

	}
	
	

	/**
	 * 是否添加搜索栏
	 * @return
	 */
	protected abstract boolean isAddSearchBar();
	/**
	 * 判断是否有历史数据(有历史数据就使用，没有就去服务器请求)
	 */
	abstract protected void isHaveData();

	/**
	 * 获取数据成功时的处理
	 * 
	 * @param type
	 */
	abstract protected void onSuccessHandle(Request_Type type, String result);

	/**
	 * 设置listview的显示数据
	 */
	abstract protected void initListView();
	
	/**
	 * 设置listview的底部是否可以刷新
	 */
	protected abstract void setBottomRefresh();

}
