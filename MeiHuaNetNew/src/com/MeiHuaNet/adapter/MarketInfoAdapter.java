package com.MeiHuaNet.adapter;

import java.util.ArrayList;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.KnowledgeObject;
import com.MeiHuaNet.utils.StringUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.EllipsizingTextView;
import com.MeiHuaNet.view.TextViewFont;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 
 * @description 营销百科页面list的adapter
 * @author lee
 * @time 2013-10-31 下午1:27:58
 * 
 */
public class MarketInfoAdapter extends MyAdapter {

	private LayoutInflater mInflater;
	private ArrayList<KnowledgeObject> mListData;

	public MarketInfoAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	public void setListData(ArrayList<KnowledgeObject> listData) {
		mListData = listData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListData == null ? 0 : mListData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MarketViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_market_info_item,
					null);
			viewHolder = new MarketViewHolder();
			viewHolder.title = (TextViewFont) convertView
					.findViewById(R.id.marke_info_item_title);
			viewHolder.summary = (EllipsizingTextView) convertView
					.findViewById(R.id.marke_info_item_summary);
			viewHolder.date = (TextViewFont) convertView
					.findViewById(R.id.market_info_item_date);
			viewHolder.views = (TextViewFont) convertView
					.findViewById(R.id.market_info_item_views);
			viewHolder.allLayout = (LinearLayout) convertView
					.findViewById(R.id.market_info_all_layout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MarketViewHolder) convertView.getTag();
		}
		
		KnowledgeObject knowledgeObject = mListData.get(position);
		
		viewHolder.title.setText(knowledgeObject.Title);
		viewHolder.summary.setMaxLines(4);
		viewHolder.summary.setText(StringUtils
				.getSummaryStr(knowledgeObject.Description));
		viewHolder.date.setText(knowledgeObject.getDate());
		if (knowledgeObject.getViews() > 0) {
			viewHolder.views.setVisibility(View.VISIBLE);
			viewHolder.views.setText(Utils.getResString(R.string.read)
					+ knowledgeObject.Views);
		} else {
			viewHolder.views.setVisibility(View.GONE);
		}
		viewHolder.allLayout.setOnClickListener(getClickListener(position));
		return convertView;
	}

	static class MarketViewHolder {
		LinearLayout allLayout;
		TextViewFont title;
		EllipsizingTextView summary;
		TextViewFont date;
		TextViewFont views;
	}
}
