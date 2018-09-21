package com.MeiHuaNet.adapter;

import java.util.ArrayList;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.OriginalityObject;
import com.MeiHuaNet.view.TextViewFont;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FreeOriginalityAdapter extends MyAdapter{

	private Context mContext;
	private ArrayList<OriginalityObject> mListData;
	public FreeOriginalityAdapter(Context context,ArrayList<OriginalityObject> listData){
		mContext = context;
		mListData = listData; 
	}
	
	@Override
	public int getCount() {
		return mListData == null ? 0 :mListData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FreeViewHolder viewHolder = null;
		
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.layout_free_originality_item, null);
			viewHolder = new FreeViewHolder();
			viewHolder.allLayout = (LinearLayout) convertView.findViewById(R.id.free_originality_all_layout);
			viewHolder.title = (TextViewFont) convertView.findViewById(R.id.free_originality_item_title);
			viewHolder.line = (ImageView) convertView.findViewById(R.id.free_originality_item_line);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (FreeViewHolder) convertView.getTag();
		}
		OriginalityObject object = mListData.get(position);
		viewHolder.title.setText(object.Title);
		if(position== mListData.size()-1){
			viewHolder.line.setVisibility(View.GONE);
		} else {
			viewHolder.line.setVisibility(View.VISIBLE);
		}
		viewHolder.allLayout.setOnClickListener(getClickListener(position));
		return convertView;
	}

	static class FreeViewHolder{
		public LinearLayout allLayout;
		public TextViewFont title;
		public ImageView line;
	}
}
