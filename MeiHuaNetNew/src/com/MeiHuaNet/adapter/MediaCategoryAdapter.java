package com.MeiHuaNet.adapter;

import java.util.ArrayList;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.MediaCategoryObject;
import com.MeiHuaNet.view.TextViewFont;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MediaCategoryAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<MediaCategoryObject> mListData;
	public MediaCategoryAdapter(Context context , ArrayList<MediaCategoryObject> listData){
		mContext = context ;
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
		MediaCategoryViewHolder viewHolder = null;
		
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.layout_media_category_list_item, null);
			viewHolder = new MediaCategoryViewHolder();
			viewHolder.name = (TextViewFont) convertView.findViewById(R.id.media_category_item_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MediaCategoryViewHolder) convertView.getTag();
		}
		MediaCategoryObject mediaCategoryObject = mListData.get(position);
		viewHolder.name.setText(mediaCategoryObject.Name);
		
		return convertView;
	}
	
	static class MediaCategoryViewHolder{
		public TextViewFont name;
	}

}
