package com.MeiHuaNet.activity.event;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.activity.login.LoginActivity;
import com.MeiHuaNet.adapter.EventAdapter;
import com.MeiHuaNet.entity.EventListJsonObject;
import com.MeiHuaNet.entity.EventObject;
import com.MeiHuaNet.listener.ActivityCancelObserver;
import com.MeiHuaNet.listener.OnListItemClickListener;
import com.MeiHuaNet.listener.OnSlidingBtnClickListener;
import com.MeiHuaNet.network.HttpCallbackListener;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.HandleSlidTitle;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.NewRefreshListView;
import com.MeiHuaNet.view.SlidingMenu;
import com.MeiHuaNet.view.TextViewFont;

/**
 * 
 * @description 活动列表页
 * @author lee
 * @time 2013-11-6 下午4:27:12
 * 
 */
public class EventPage implements OnClickListener {

	private SlidingMenu mSlidingMenu;
	private Context mContext;
	private View me;
	private EventType curType;
	private String fileName;
	private NewRefreshListView mListView;
	private EventAdapter mAdapter;

	ArrayList<EventObject> mListData;
	EventListJsonObject eventListJsonObject;
	ActivityCancelObserver cancelObserver;

	PopupWindow popupWindow;
	TextViewFont upView;
	TextViewFont downView;

	public static enum EventType {
		effective, myevent, pastevent;
	}

	public EventPage(SlidingMenu slidingMenu, Context context) {
		mSlidingMenu = slidingMenu;
		mContext = context;
		curType = EventType.effective;
		cancelObserver = new ActivityCancelObserver();

		initView();
	}

	public View getView() {
		return me;
	}

	private void initView() {
		me = View.inflate(mContext, R.layout.layout_event, null);

		initPopuWindow();
		initTitle();
		mListView = (NewRefreshListView) me.findViewById(R.id.event_list);
		mListView.setisHeadRefreshable(false);

		getListData();
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) me
				.findViewById(R.id.title_relative_layout);
		HandleSlidTitle handleSlidTitle = new HandleSlidTitle(relativeLayout);
		handleSlidTitle.setTitle(Utils.getResString(R.string.event),
				R.drawable.right_event_top_btn);
		handleSlidTitle.setListener(
				new OnSlidingBtnClickListener(mSlidingMenu), this);
	}

	/**
	 * 获取listview中要显示的数据
	 */
	private void getListData() {
		switch (curType) {
		case effective:
			fileName = "event.object";
			mListData = FileUtils.fetchDataFromFile(mContext,
					EventObject.class, fileName, 30);
			if (mListData != null && mListData.size() > 0) {
				initListView();
			} else {
				requestData();
			}
			break;
		case myevent:
			requestData();
			break;
		case pastevent:
			
			requestData();
			break;
		}
	}

	/**
	 * 请求活动的列表数据
	 */
	private void requestData() {
		HttpCallbackListener httpCallbackListener = new HttpCallbackListener() {

			@Override
			public void onSuccessCallBack(String result) {
				eventListJsonObject = Utils.parseJson(result,
						EventListJsonObject.class);
				if (eventListJsonObject != null) {
					mListData = eventListJsonObject.EventList;
					initListView();
					if (curType == EventType.effective) {
						FileUtils.storeDataToFile(mContext, mListData,
								fileName, 0);
						if(mListData.size() ==0){
							Utils.Toast(Utils.getResString(R.string.no_event), 1);
						}
					}
				}
				UIManager.cancelAllProgressDialog();
			}

			@Override
			public void onErrorCallBack() {
				UIManager.cancelAllProgressDialog();
			}

			@Override
			public void onCancelCallBack() {
				UIManager.cancelAllProgressDialog();
			}
		};

		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL_EVENT;
		switch (curType) {
		case effective:
			params.methodName = Constants.GeEffectiveEventList;
			params.paramList.put("key", Constants.KEY);
			break;
		case myevent:
			params.methodName = Constants.GeMyEventList;
			params.paramList.put("username", Utils.isLogin(mContext).UserName);
			break;
		case pastevent:
			params.methodName = Constants.GePastEventList;
			params.paramList.put("key", Constants.KEY);
			break;
		}

		WebService.requestAsynHttp(params, httpCallbackListener, true,
				cancelObserver);

	}

	private void initListView() {
		if (mAdapter == null) {
			mAdapter = new EventAdapter(mContext);
		}
		mAdapter.setListData(mListData);
		mAdapter.setItemListener(new OnListItemClickListener() {

			@Override
			public void onclick(View v, int position) {
				EventObject eventObject = mListData.get(position);
				if (eventObject == null) {
					return;
				}
				Intent intent = new Intent();
				intent.setClass(mContext, EventDetailActivity.class);
				intent.putExtra("detailObject", eventObject);
				intent.putExtra("type", curType);
				mContext.startActivity(intent);
			}
		});
		mListView.setAdapter(mAdapter);
		mListView.setVisibility(View.VISIBLE);
	}

	/**
	 * 点击标题栏右边的图标弹出的选择框
	 */
	private void initPopuWindow() {
		View view = View.inflate(mContext, R.layout.layout_event_popuwindow,
				null);
		popupWindow = new PopupWindow(view,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		upView = (TextViewFont) view.findViewById(R.id.event_popu_up);
		downView = (TextViewFont) view.findViewById(R.id.event_popu_down);
		upView.setOnClickListener(this);
		downView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right_btn:
			switch (curType) {
			case effective:
				upView.setText(Utils.getResString(R.string.myevent));
				downView.setText(Utils.getResString(R.string.past_event));
				break;
			case myevent:
				upView.setText(Utils.getResString(R.string.event));
				downView.setText(Utils.getResString(R.string.past_event));
				break;
			case pastevent:
				upView.setText(Utils.getResString(R.string.event));
				downView.setText(Utils.getResString(R.string.myevent));
				break;
			}
			popupWindow.setFocusable(true);
			ColorDrawable cd = new ColorDrawable(-0000);
			popupWindow.setBackgroundDrawable(cd);
			popupWindow.setOutsideTouchable(true);
			popupWindow.showAsDropDown(v);
			break;
		case R.id.event_popu_up:
			switch (curType) {
			case effective:
				if (Utils.isLogin(mContext) != null) {
					curType = EventType.myevent;
				} else {
					popupWindow.dismiss();
					userLogin();
					return;
				}
				break;
			case myevent:
				curType = EventType.effective;
				break;
			case pastevent:
				curType = EventType.effective;
				break;
			}
			mListView.setVisibility(View.GONE);
			getListData();
			popupWindow.dismiss();
			break;
		case R.id.event_popu_down:
			switch (curType) {
			case effective:
				curType = EventType.pastevent;
				break;
			case myevent:
				curType = EventType.pastevent;
				break;
			case pastevent:
				if (Utils.isLogin(mContext) != null) {
					curType = EventType.myevent;
				} else {
					popupWindow.dismiss();
					userLogin();
					return;
				}
				break;
			}
			mListView.setVisibility(View.GONE);
			getListData();
			popupWindow.dismiss();
			break;
		}
	}

	private void userLogin() {
		Intent intent = new Intent();
		intent.setClass(mContext, LoginActivity.class);
		mContext.startActivity(intent);
	}
}
