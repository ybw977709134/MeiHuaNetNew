package com.MeiHuaNet.adapter;

import java.util.ArrayList;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.EventObject;
import com.MeiHuaNet.utils.DateUtil;
import com.MeiHuaNet.utils.DensityUtil;
import com.MeiHuaNet.view.TextViewFont;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 *
 * @description 活动列表的adapter 
 * @author lee
 * @time  2013-12-27 下午5:20:19
 *
 */
public class EventAdapter extends MyAdapter{

	Context context;
	ArrayList<EventObject> mListData; 
	public EventAdapter(Context context){
		this.context = context;
	}
	
	public void setListData(ArrayList<EventObject> listData){
		mListData = listData;
	}
	
	@Override
	public int getCount() {
		return mListData==null ? 0 : mListData.size();
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
		EventViewHolder viewHolder = null;
		if(convertView == null){
			convertView = View.inflate(context, R.layout.layout_event_item, null);
			viewHolder = new EventViewHolder();
			viewHolder.allLayout = (RelativeLayout) convertView.findViewById(R.id.event_item_all_layout);
			viewHolder.title = (TextViewFont) convertView.findViewById(R.id.event_item_title);
			viewHolder.city = (TextViewFont) convertView.findViewById(R.id.event_item_city);
			viewHolder.date = (TextViewFont) convertView.findViewById(R.id.event_item_date);
			viewHolder.price = (TextViewFont) convertView.findViewById(R.id.event_item_price);
			viewHolder.hasSignUp = (TextViewFont) convertView.findViewById(R.id.event_item_have_sign);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (EventViewHolder) convertView.getTag();
		}
		EventObject eventObject = mListData.get(position);
		viewHolder.title.setText(eventObject.Name);
		viewHolder.city.setText(eventObject.City);
		viewHolder.date.setText(DateUtil.getEventDate(eventObject.StartDate, eventObject.EndDate));
		viewHolder.price.setText(eventObject.PriceText);
		viewHolder.allLayout.setOnClickListener(getClickListener(position));
		if(TextUtils.isEmpty(eventObject.RegistrationCode)){
			viewHolder.hasSignUp.setVisibility(View.GONE);
			((RelativeLayout.LayoutParams)viewHolder.price.getLayoutParams()).rightMargin = 0;
		} else {
			viewHolder.hasSignUp.setVisibility(View.VISIBLE);
			((RelativeLayout.LayoutParams)viewHolder.price.getLayoutParams()).rightMargin = DensityUtil.dip2px(context, 60);
		}
		
		return convertView;
	}
	
	static class EventViewHolder{
		RelativeLayout allLayout;
		TextViewFont title;
		TextViewFont city;
		TextViewFont date;
		TextViewFont price;
		TextViewFont hasSignUp;
	}

}
