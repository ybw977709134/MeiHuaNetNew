package com.MeiHuaNet.activity.setting;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.ShareCore;

import com.MeiHuaNet.R;
import com.MeiHuaNet.activity.BaseActivity;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.Utils;

/**
 *
 * @description 分享的社交平台的账号管理 
 * @author lee
 * @time  2013-12-20 上午11:13:18
 *
 */
public class AuthManagerActivity extends BaseActivity implements Callback,
OnClickListener, PlatformActionListener{

	Handler handler;
	AuthAdapter adapter;
	ListView listView;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_authorize);
		handler = new Handler(this);
		
		initView();
	}
	
	private void initView(){
		initTitle();
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.authorize_all_layout);
		GestureDetectorUtil.setGestureDetector(this, layout, null);
		
		listView = (ListView) findViewById(R.id.authorize_listview);
		adapter = new AuthAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(adapter);
	}
	
	private void initTitle(){
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView(Utils.getResString(R.string.authorize_manager), -1);
		handleBackTitle.setListener(new OnBackBtnListener(this), null);
	}
	
	/**
	 * 处理操作结果
	 * <p>
	 * 如果获取到用户的名称，则显示名称；否则如果已经授权，则显示
	 *平台名称
	 */
	public boolean handleMessage(Message msg) {
		Platform plat = (Platform) msg.obj;
		String text = actionToString(msg.arg2);
		switch (msg.arg1) {
			case 1: {
				// 成功
				text = plat.getName() + " completed at " + text;
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}
			break;
			case 2: {
				// 失败
				text = plat.getName() + " caught error at " + text;
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
				return false;
			}
			case 3: {
				// 取消
				text = plat.getName() + " canceled at " + text;
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		adapter.notifyDataSetChanged();
		return false;
	}
	
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		handler.sendMessage(msg);
	}

	public void onError(Platform plat, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = plat;
		handler.sendMessage(msg);
	}

	public void onCancel(Platform plat, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = plat;
		handler.sendMessage(msg);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
			case Platform.ACTION_AUTHORIZING: return "ACTION_AUTHORIZING";
			case Platform.ACTION_GETTING_FRIEND_LIST: return "ACTION_GETTING_FRIEND_LIST";
			case Platform.ACTION_FOLLOWING_USER: return "ACTION_FOLLOWING_USER";
			case Platform.ACTION_SENDING_DIRECT_MESSAGE: return "ACTION_SENDING_DIRECT_MESSAGE";
			case Platform.ACTION_TIMELINE: return "ACTION_TIMELINE";
			case Platform.ACTION_USER_INFOR: return "ACTION_USER_INFOR";
			case Platform.ACTION_SHARE: return "ACTION_SHARE";
			default: {
				return "UNKNOWN";
			}
		}
	}
	
	private static class AuthAdapter extends BaseAdapter implements OnItemClickListener {
		private ArrayList<Platform> platforms;

		AuthManagerActivity activity;
		public AuthAdapter(AuthManagerActivity activity) {
			this.activity = activity;

			// 获取平台列表
			Platform[] tmp = ShareSDK.getPlatformList(activity);
			platforms = new ArrayList<Platform>();
			for (Platform p : tmp) {
				String name = p.getName();
				if (!ShareCore.canAuthorize(p.getContext(), name)) {
					continue;
				}
				platforms.add(p);
			}
		}

		public int getCount() {
			return platforms == null ? 0 : platforms.size();
		}

		public Platform getItem(int position) {
			return platforms.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(activity, R.layout.auth_page_item, null);
			}

			int count = getCount();
			View llItem = convertView.findViewById(R.id.llItem);
			int dp_10 = cn.sharesdk.framework.utils.R.dipToPx(parent.getContext(), 10);
			if (count == 1) {
				llItem.setBackgroundResource(R.drawable.list_item_single_normal);
				llItem.setPadding(0, 0, 0, 0);
				convertView.setPadding(dp_10, dp_10, dp_10, dp_10);
			}
			else if (position == 0) {
				llItem.setBackgroundResource(R.drawable.list_item_first_normal);
				llItem.setPadding(0, 0, 0, 0);
				convertView.setPadding(dp_10, dp_10, dp_10, 0);
			}
			else if (position == count - 1) {
				llItem.setBackgroundResource(R.drawable.list_item_last_normal);
				llItem.setPadding(0, 0, 0, 0);
				convertView.setPadding(dp_10, 0, dp_10, dp_10);
			}
			else {
				llItem.setBackgroundResource(R.drawable.list_item_middle_normal);
				llItem.setPadding(0, 0, 0, 0);
				convertView.setPadding(dp_10, 0, dp_10, 0);
			}

			Platform plat = getItem(position);
			ImageView ivLogo = (ImageView) convertView.findViewById(R.id.ivLogo);
			Bitmap logo = getIcon(plat);
			if (logo != null && !logo.isRecycled()) {
				ivLogo.setImageBitmap(logo);
			}
			CheckedTextView ctvName = (CheckedTextView) convertView.findViewById(R.id.ctvName);
			ctvName.setChecked(plat.isValid());
			if (plat.isValid()) {
				String userName = plat.getDb().get("nickname");
				if (userName == null || userName.length() <= 0
						|| "null".equals(userName)) {
					// 如果平台已经授权却没有拿到帐号名称，则自动获取用户资料，以获取名称
					userName = getName(plat);
					plat.setPlatformActionListener(activity);
					plat.showUser(null);
				}
				ctvName.setText(userName);
			}
			else {
				ctvName.setText(R.string.not_yet_authorized);
			}
			return convertView;
		}

		private Bitmap getIcon(Platform plat) {
			if (plat == null) {
				return null;
			}

			String name = plat.getName();
			if (name == null) {
				return null;
			}

			String resName = "logo_" + plat.getName();
			int resId = cn.sharesdk.framework.utils.R.getResId(R.drawable.class, resName);
			return BitmapFactory.decodeResource(activity.getResources(), resId);
		}

		private String getName(Platform plat) {
			if (plat == null) {
				return "";
			}

			String name = plat.getName();
			if (name == null) {
				return "";
			}

			int resId = cn.sharesdk.framework.utils.R.getStringRes(activity, plat.getName());
			return activity.getString(resId);
		}

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Platform plat = getItem(position);
			CheckedTextView ctvName = (CheckedTextView) view.findViewById(R.id.ctvName);
			if (plat == null) {
				ctvName.setChecked(false);
				ctvName.setText(R.string.not_yet_authorized);
				return;
			}

			if (plat.isValid()) {
				plat.removeAccount();
				ctvName.setChecked(false);
				ctvName.setText(R.string.not_yet_authorized);
				return;
			}

			plat.setPlatformActionListener(activity);
			plat.showUser(null);
		}

	}
}
