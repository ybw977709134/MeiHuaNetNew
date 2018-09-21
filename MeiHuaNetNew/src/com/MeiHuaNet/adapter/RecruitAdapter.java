package com.MeiHuaNet.adapter;

import java.util.ArrayList;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.RecruitObject;
import com.MeiHuaNet.view.TextViewFont;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 *
 * @description 招聘列表页的适配器 
 * @author lee
 * @time  2013-10-30 上午10:14:55
 *
 */
public class RecruitAdapter extends MyAdapter{

	private Context mContext;
	private ArrayList<RecruitObject> mListData;
	public RecruitAdapter(Context context){
		mContext = context;
	}
	
	public void setListData(ArrayList<RecruitObject> listData){
		mListData = listData;
	}
	
	@Override
	public int getCount() {
		return mListData== null ? 0 : mListData.size();
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
		recruitViewHolder viewHolder = null;
		 
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.layout_recruit_item, null);
			viewHolder = new recruitViewHolder();
			viewHolder.allLayout = (LinearLayout) convertView.findViewById(R.id.recruit_item_all_layout);
			viewHolder.jobName = (TextViewFont) convertView.findViewById(R.id.recruit_item_jobname);
			viewHolder.city = (TextViewFont) convertView.findViewById(R.id.recruit_item_city);
			viewHolder.employeName = (TextViewFont) convertView.findViewById(R.id.recruit_item_employename);
			viewHolder.logo = (ImageView) convertView.findViewById(R.id.recruit_item_logo);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (recruitViewHolder) convertView.getTag();
		}
		RecruitObject recruitObject = mListData.get(position);
		viewHolder.employeName.setText(recruitObject.EmployerName);
		viewHolder.jobName.setText(recruitObject.JobName);
		viewHolder.city.setText(recruitObject.City);
//		viewHolder.logo.setTag(recruitObject.LogoUrl);
//		final Drawable drawable = AsyncImageLoader.getInstance(mContext)
//				.loadDrawable(recruitObject.LogoUrl, new ImageCallback() {
//
//					@Override
//					public void imageLoaded(Drawable imageDrawable,
//							String imageUrl, ImageView view) {
//						if (imageDrawable != null) {
//							if (imageUrl.equals(view.getTag())) {
//								view.setImageDrawable(imageDrawable);
//							}
//
//						}
//					}
//				}, viewHolder.logo);
//		if (drawable != null) {
//			viewHolder.logo.setImageDrawable(drawable);
//		} else {
//			viewHolder.logo.setImageResource(R.drawable.default_img);
//		}
		ImageLoader.getInstance().displayImage(recruitObject.LogoUrl, viewHolder.logo);
		viewHolder.allLayout.setOnClickListener(getClickListener(position));
		
		return convertView;
	}

	static class recruitViewHolder{
		LinearLayout allLayout;
		ImageView logo;
		TextViewFont jobName;
		TextViewFont city;
		TextViewFont employeName;
	}
}
