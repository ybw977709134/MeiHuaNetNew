package com.MeiHuaNet.activity.resource;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.MeiHuaNet.R;
import com.MeiHuaNet.listener.OnSlidingBtnClickListener;
import com.MeiHuaNet.utils.HandleSlidTitle;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.SlidingMenu;
import com.MeiHuaNet.view.TextViewFont;

public class ResourcePage implements OnClickListener {

	private SlidingMenu mSlidingMenu;
	private Context mContext;
	private View me;
	TextViewFont mediaTv;
	TextViewFont marketTv;

	public ResourcePage(SlidingMenu slidingMenu, Context context) {
		mSlidingMenu = slidingMenu;
		mContext = context;

		me = View.inflate(mContext, R.layout.layout_resource, null);
		initView();
	}

	public View getView() {
		return me;
	}

	private void initView() {
		initTitle();

		mediaTv = (TextViewFont) me.findViewById(R.id.resource_media_store);
		marketTv = (TextViewFont) me.findViewById(R.id.resource_market_vendor);

		mediaTv.setOnClickListener(this);
		marketTv.setOnClickListener(this);
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) me
				.findViewById(R.id.title_relative_layout);
		HandleSlidTitle handleSlidTitle = new HandleSlidTitle(relativeLayout);
		handleSlidTitle.setTitle(Utils.getResString(R.string.resource), -1);
		handleSlidTitle.setListener(
				new OnSlidingBtnClickListener(mSlidingMenu), null);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.resource_media_store:
			intent.setClass(mContext, MediaCategoryActivity.class);
			break;
		case R.id.resource_market_vendor:
			intent.setClass(mContext, MarketVendorListActivity.class);
			break;
		}
		mContext.startActivity(intent);
	}
}
