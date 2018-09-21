package com.MeiHuaNet.activity.acepack;

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

/**
 *
 * @description 锦囊页面 
 * @author lee
 * @time  2013-10-30 上午10:45:22
 *
 */
public class AcepackPage implements OnClickListener{
	
	private SlidingMenu msliSlidingMenu;
	private Context mContext;
	private View me;
	TextViewFont knowledgeTv;
	TextViewFont caseTv;
	TextViewFont marketingTv;
	
	public AcepackPage(SlidingMenu slidingMenu, Context context){
		msliSlidingMenu = slidingMenu;
		mContext = context;
		
		initView();
	}
	
	public View getView(){
		return me;
	}
	private void initView(){
		me = View.inflate(mContext, R.layout.layout_acepack, null);
		
		initTitle();
		
		knowledgeTv = (TextViewFont) me.findViewById(R.id.acepack_knowledge_center);
		caseTv = (TextViewFont) me.findViewById(R.id.acepack_case_center);
		marketingTv = (TextViewFont) me.findViewById(R.id.acepack_marketing_info);
		
		knowledgeTv.setOnClickListener(this);
		caseTv.setOnClickListener(this);
		marketingTv.setOnClickListener(this);
	}
	
	private void initTitle(){
		RelativeLayout relativeLayout = (RelativeLayout) me.findViewById(R.id.title_relative_layout);
		HandleSlidTitle handleSlidTitle = new HandleSlidTitle(relativeLayout);
		handleSlidTitle.setTitle(Utils.getResString(R.string.acepack), -1);
		handleSlidTitle.setListener(new OnSlidingBtnClickListener(msliSlidingMenu), null);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.acepack_knowledge_center:
			intent.setClass(mContext, KnowledgeListActivity.class);
			break;
		case R.id.acepack_case_center:
			intent.setClass(mContext, CaseListActivity.class);
			break;
		case R.id.acepack_marketing_info:
			intent.setClass(mContext, MarketListActivity.class);
			break;
		}
		mContext.startActivity(intent);
	}
}
