package com.MeiHuaNet.activity.originality;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.MeiHuaNet.Constants;
import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.Constants.Request_Type;
import com.MeiHuaNet.activity.SearchBackActivity;
import com.MeiHuaNet.activity.login.LoginActivity;
import com.MeiHuaNet.activity.vip.VipUpdateActivity;
import com.MeiHuaNet.activity.vip.VipUpdateActivity.VipUpdateType;
import com.MeiHuaNet.adapter.OriginalityListAdapter;
import com.MeiHuaNet.entity.LoginJsonObject;
import com.MeiHuaNet.entity.OriginalityCategory;
import com.MeiHuaNet.entity.OriginalityListJsonObject;
import com.MeiHuaNet.entity.MediainfoObject;
import com.MeiHuaNet.entity.OriginalityListJsonObject.OriginalityListObject;
import com.MeiHuaNet.entity.PurchaseJsonObject;
import com.MeiHuaNet.listener.OnListItemClickListener;
import com.MeiHuaNet.network.HttpCallBackDialog;
import com.MeiHuaNet.network.WebService;
import com.MeiHuaNet.network.WebServiceParams;
import com.MeiHuaNet.utils.DialogUtils;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.NewRefreshListView.OnBottomRefreshListener;
import com.MeiHuaNet.view.NewRefreshListView.OnHeadRefreshListener;

/**
 * 
 * @description 创意资源列表页面
 * @author lee
 * @time 2013-12-16 上午9:53:45
 * 
 */
public class OriginalityListActivity extends SearchBackActivity {

	private OriginalityListAdapter mAdapter;
	private ArrayList<OriginalityListObject> mListData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		curBack_Type = Back_Type.originality;
		originalityCategory = (OriginalityCategory) getIntent()
				.getSerializableExtra("originalityObject");
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
		OriginalityListJsonObject originalityListJsonObject = Utils.parseJson(
				result, OriginalityListJsonObject.class);
		if (originalityListJsonObject != null) {
			totalCount = originalityListJsonObject.getResultCount();
			if (mListData == null) {
				mListData = new ArrayList<OriginalityListObject>();
			}
			switch (request_Type) {
			case init:
			case search_init:
				mListData = originalityListJsonObject.Creatives;
				curTotalCount = mListData.size();
				InitListView();
				break;
			case head_refresh:
				mListView.onHeadRefreshComplete();
				if (originalityListJsonObject.Creatives != null
						|| originalityListJsonObject.Creatives.size() > 0) {
					if (mListData.size() == 0) {
						mListData = originalityListJsonObject.Creatives;
						curTotalCount = mListData.size();
						InitListView();
					} else {
						mListData.clear();
						mListData.addAll(originalityListJsonObject.Creatives);
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
				mListData.addAll(originalityListJsonObject.Creatives);
				curTotalCount += originalityListJsonObject.Creatives.size();
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
			mAdapter = new OriginalityListAdapter(this);
		}
		mAdapter.setListData(mListData);
		mAdapter.setItemListener(new OnListItemClickListener() {

			@Override
			public void onclick(View v, int position) {
				final OriginalityListObject object = mListData.get(position);
				if (object == null) {
					return;
				}
				// 免费视频直接观看
				if (object.IsFree.equalsIgnoreCase("true")) {
					// requestCreativeDetail(object.ID);
					seeVideo(object.url);
					return;
				}

				// 收费视频要用户登录，检查用户是否有权限观看
				LoginJsonObject loginJsonObject = Utils
						.isLogin(OriginalityListActivity.this);
				if (loginJsonObject == null) {
					Intent i = new Intent();
					i.setClass(OriginalityListActivity.this,
							LoginActivity.class);
					startActivity(i);
					return;
				}
				if ("true".equals(loginJsonObject.UserLicense.get(0).IsView)) {
					checkTypePurchased(2, loginJsonObject, object);
				} else {
					if ("true".equalsIgnoreCase(loginJsonObject.IsVIP)) {
						checkTypePurchased(1, loginJsonObject, object);
					} else {
						DialogUtils.createDialog(OriginalityListActivity.this,
								Utils.getResString(R.string.update_vip_prompt),
								Utils.getResString(R.string.update),
								Utils.getResString(R.string.cancel),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										// 升级到vip的逻辑
										Intent intent = new Intent();
										intent.setClass(
												OriginalityListActivity.this,
												VipUpdateActivity.class);
										intent.putExtra("id", object.ID);
										intent.putExtra("vipType",
												VipUpdateType.originality);
										startActivity(intent);
									}
								}, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
					}
				}
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
				if (mSearchEditView != null) {
					mSearchEditView.setText("");
				}
				requestData(Request_Type.head_refresh);
			}
		});
		mListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {

			@Override
			public void bottomRefresh() {
				if (searchStr != null && searchStr.length() > 0) {
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

	/**
	 * 检查用户是否超过了观看的数量限制
	 * 
	 * @param type
	 *            1：CheckVIPPurchased 2：CheckTrailPurchased
	 */
	private void checkTypePurchased(int type,
			final LoginJsonObject loginJsonObject,
			final OriginalityListObject object) {
		HttpCallBackDialog callBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				UIManager.cancelAllProgressDialog();
				PurchaseJsonObject purchaseJsonObject = Utils.parseJson(result,
						PurchaseJsonObject.class);
				if (purchaseJsonObject != null) {
					if ("true".equalsIgnoreCase(purchaseJsonObject.IsView)) {
						if ("试用版授权"
								.equals(loginJsonObject.UserLicense.get(0).Description)) {
							DialogUtils.createDialog(
									OriginalityListActivity.this,
									Utils.getResString(R.string.see_intro),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											seeVideo(object.url);
										}
									}, null);
						} else {
							seeVideo(object.url);
						}
					} else {
						checkPurchase(object, loginJsonObject.UserName);
					}
				}
			}
		};
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL;
		if (type == 1) {
			params.methodName = Constants.CheckVIPPurchased;
		} else {
			params.methodName = Constants.CheckTrailPurchased;
		}
		params.paramList.put("userName", loginJsonObject.UserName);

		WebService
				.requestAsynHttp(params, callBackDialog, true, cancelObserver);

	}

	private void checkPurchase(final OriginalityListObject Listobject,
			String userName) {
		HttpCallBackDialog callBackDialog = new HttpCallBackDialog() {

			@Override
			public void onSuccessCallBack(String result) {
				UIManager.cancelAllProgressDialog();
				PurchaseJsonObject object = Utils.parseJson(result,
						PurchaseJsonObject.class);
				if (object != null) {
					if ("true".equalsIgnoreCase(object.IsPurchased)) {
						seeVideo(Listobject.url);
					} else {
						DialogUtils.createSureDialog(
								OriginalityListActivity.this,
								Utils.getResString(R.string.no_authorize_see),
								Utils.getResString(R.string.sure), null);
					}
				}
			}
		};
		WebServiceParams params = new WebServiceParams();
		params.webServiceUrl = Constants.WEBSERVICE_URL;
		params.methodName = Constants.CheckPurchased;
		params.paramList.put("creativeID", Listobject.ID);
		params.paramList.put("userName", userName);

		WebService
				.requestAsynHttp(params, callBackDialog, true, cancelObserver);
	}

	// 打开对应url的视频
	private void seeVideo(String url) {
		Intent i = Utils.getVideoFileIntent(url);
		startActivity(i);
	}
}
