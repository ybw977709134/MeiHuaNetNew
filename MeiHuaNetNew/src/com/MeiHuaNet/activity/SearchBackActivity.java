package com.MeiHuaNet.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.MeiHuaNet.Constants.Request_Type;
import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.entity.OriginalityCategory;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.network.HttpCallbackListener;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.EditTextFont;
import com.MeiHuaNet.view.NewRefreshListView;

/**
 * 
 * @description 顶部左边是返回按钮，主体是listview且能搜索信息的类的基类
 * @author lee
 * @time 2013-10-30 下午2:14:15
 * 
 */
public abstract class SearchBackActivity extends BaseActivity {

	/**
	 * 
	 * @description 继承的页面的类型
	 * @author lee
	 * @time 2013-12-11 下午4:15:49
	 * 
	 */
	public static enum Back_Type {
		knowledge_center, // 知识中心
		case_center, // 案例中心
		marketing_info, // 营销百科
		marketing_vendor, // 营销服务商
		media_info, // 中国传媒库
		originality;// 创意
	}

	/* 子类的类型 */
	protected Back_Type curBack_Type;
	protected NewRefreshListView mListView;
	protected int pageindex = 1;
	protected String fileName = "";

	/* 分类列表中数据的总条数 */
	protected int totalCount = -1;
	/* 当前已经获取的数据的条数 */
	protected int curTotalCount = -1;

	protected EditTextFont mSearchEditView;
	protected ImageView mSearchArrow;
	protected String searchStr;
	protected View searchLayout;

	protected String mediaType = "";// 传媒库分类的id
	protected OriginalityCategory originalityCategory;// 创意的分类的id

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_search_list_back);

		initView(curBack_Type);
	}

	private void initView(Back_Type back_Type) {
		initTitle(back_Type);
		setFileName(back_Type);

		LinearLayout layout = (LinearLayout) findViewById(R.id.base_all_layout);
		GestureDetectorUtil.setGestureDetector(this, layout, null);

		mListView = (NewRefreshListView) findViewById(R.id.base_page_listview);
		isHaveCacheData();
	}

	/**
	 * 不同类型标题不同
	 * 
	 * @param back_Type
	 */
	private void initTitle(Back_Type back_Type) {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		switch (back_Type) {
		case knowledge_center:
			handleBackTitle.setTitleView(
					Utils.getResString(R.string.knowledge_center), -1);
			break;
		case case_center:
			handleBackTitle.setTitleView(
					Utils.getResString(R.string.case_center), -1);
			break;
		case marketing_info:
			handleBackTitle.setTitleView(
					Utils.getResString(R.string.marketing_info), -1);
			break;
		case marketing_vendor:
			handleBackTitle.setTitleView(
					Utils.getResString(R.string.market_vendor), -1);
			break;
		case media_info:
			handleBackTitle.setTitleView(
					Utils.getResString(R.string.media_store), -1);
			break;
		case originality:
			handleBackTitle.setTitleView(
					Utils.getResString(R.string.originality) + "—"
							+ originalityCategory.Description, -1);
			break;
		}

		handleBackTitle.setListener(new OnBackBtnListener(this), null);
	}

	/**
	 * 不同类型页面，保存数据的文件名不同
	 * 
	 * @param back_Type
	 */
	private void setFileName(Back_Type back_Type) {
		switch (back_Type) {
		case knowledge_center:
			fileName = "knowledgeList.obj";
			break;
		case case_center:
			fileName = "caseList.obj";
			break;
		case marketing_info:
			fileName = "marketList.obj";
			break;
		case marketing_vendor:
			fileName = "marketVendorList.obj";
			break;
		case media_info:
			fileName = "mediainfolist.obj";
			break;
		case originality:
			fileName = "originalitylist" + originalityCategory.ID + ".obj";
			break;
		}
	}

	/**
	 * 设置listview顶部搜索条
	 */
	protected void setSearchBar() {
		if (isAddSearchBar()) {
			searchLayout = View.inflate(this, R.layout.layout_search, null);
			mSearchEditView = (EditTextFont) searchLayout
					.findViewById(R.id.today_search_edittext);
			mSearchArrow = (ImageView) searchLayout
					.findViewById(R.id.today_search_arrow);
			mListView.addHeaderView(searchLayout, null, false);

			mSearchArrow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					searchStr = mSearchEditView.getText().toString();
					if (TextUtils.isEmpty(searchStr)) {
						return;
					}
					requestData(Request_Type.search_init);
				}
			});
		}
	}

	protected void setSelection(final int position) {
		// 不用post,则setselection()这个方法有时有效，有时无效，原因未知
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				mListView.setSelection(position);
			}
		});
	}

	/**
	 * 请求列表数据
	 * @param request_Type 请求的类型
	 */
	protected void requestData(final Request_Type request_Type) {
		HttpCallbackListener httpCallbackListener = new HttpCallbackListener() {

			@Override
			public void onSuccessCallBack(String result) {
				onSuccessHandle(request_Type, result);
			}

			@Override
			public void onErrorCallBack() {
				onErrorHandle(request_Type);
			}

			@Override
			public void onCancelCallBack() {
				onCancelHandle(request_Type);
			}
		};

		WebServiceParams params = new WebServiceParams();
		setParams(params, request_Type);

		if (request_Type == Request_Type.init
				|| request_Type == Request_Type.search_init) {
			WebService.requestAsynHttp(params, httpCallbackListener, true,
					cancelObserver);
		} else {
			WebService.requestAsynHttp(params, httpCallbackListener, false,
					cancelObserver);
		}
	}

	protected void setParams(WebServiceParams params, Request_Type request_Type) {
		// 设置webservice请求的weburl
		if (curBack_Type == Back_Type.marketing_vendor) {
			params.webServiceUrl = Constants.WEBSERVICE_URL_MARKET_SERVICE;
		} else if (curBack_Type == Back_Type.media_info) {
			params.webServiceUrl = Constants.WEBSERVICE_URL_MEDIA;
		} else {
			params.webServiceUrl = Constants.WEBSERVICE_URL;
		}

		// 设置请求的方法名
		switch (curBack_Type) {
		case knowledge_center:
			params.methodName = Constants.GetArticleByPage;
			break;
		case case_center:
			params.methodName = Constants.GetCaseByPage;
			break;
		case marketing_info:
			params.methodName = Constants.GetWikiByPage;
			break;
		case marketing_vendor:
			params.methodName = Constants.GetVendorList;
			break;
		case media_info:
			params.methodName = Constants.getnewmedialist;
			break;
		case originality:
			params.methodName = Constants.CreativeList;
			break;
		}

		// 设置请求的参数
		if (request_Type == Request_Type.init
				|| request_Type == Request_Type.head_refresh
				|| request_Type == Request_Type.search_init) {
			pageindex = 1;
		}
		params.paramList.put("key", Constants.KEY);
		if (curBack_Type == Back_Type.marketing_vendor
				|| curBack_Type == Back_Type.media_info
				|| curBack_Type == Back_Type.originality) {
			params.paramList.put("pageSize", Constants.PAGE_SIZE);
		} else {
			params.paramList.put("pagesize", Constants.PAGE_SIZE);
		}

		params.paramList.put("pageIndex", pageindex++);
		if (request_Type == Request_Type.search_bottom_refresh
				|| request_Type == Request_Type.search_init) {
			if (curBack_Type == Back_Type.marketing_vendor) {
				params.paramList.put("keywords", searchStr);
			} else {
				params.paramList.put("keyword", searchStr);
			}
		} else {
			if (curBack_Type == Back_Type.marketing_vendor) {
				params.paramList.put("keywords", "");
			} else {
				params.paramList.put("keyword", "");
			}
		}

		if (curBack_Type == Back_Type.media_info) {
			params.paramList.put("mediuType", mediaType);
		}
		if (curBack_Type == Back_Type.originality) {
			params.paramList.put("catID", originalityCategory.ID);
		}
	}

	/**
	 * 网络请求出错时的处理
	 * @param type
	 */
	protected void onErrorHandle(Request_Type type) {
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

	/**
	 * 取消网络请求后的处理
	 * @param type
	 */
	protected void onCancelHandle(Request_Type type) {
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
	 * 
	 * @return
	 */
	protected abstract boolean isAddSearchBar();

	/**
	 * 在本地文件中查看是否有可用数据存储
	 */
	protected abstract void isHaveCacheData();

	/**
	 * 联网请求成功返回数据后，处理数据
	 * @param request_Type 网络请求的类型
	 * @param result 请求成功后返回的json数据
	 */
	protected abstract void onSuccessHandle(Request_Type request_Type,
			String result);

	/**
	 * 初始化listview显示内容
	 */
	protected abstract void InitListView();

	/**
	 * 设置listview底部是否可刷新
	 */
	protected abstract void setBottomRefresh();
}
