package com.MeiHuaNet.adapter;

import java.util.ArrayList;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.MediaDetailObject.RateCard;
import com.MeiHuaNet.view.TextViewFont;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RateCardAdapter extends MyAdapter{

	private Context mContext;
	private ArrayList<RateCard> mListData;
	public RateCardAdapter(Context context, ArrayList<RateCard> listData){
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
		RatecardViewholder viewholder = null;
		
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.layout_ratecard_item, null);
			viewholder = new RatecardViewholder();
			viewholder.allLayout = (LinearLayout) convertView.findViewById(R.id.ratecard_all_layout);
			viewholder.typeImg = (ImageView) convertView.findViewById(R.id.ratecard_img);
			viewholder.name = (TextViewFont) convertView.findViewById(R.id.ratecard_name);
			convertView.setTag(viewholder);
		} else {
			viewholder = (RatecardViewholder) convertView.getTag();
		}
		RateCard rateCard = mListData.get(position);
		if("doc".equals(rateCard.扩展名) || "docx".equals(rateCard.扩展名)){
			viewholder.typeImg.setBackgroundResource(R.drawable.docimg);
		} else if ("xls".equals(rateCard.扩展名) || "xlsx".equals(rateCard.扩展名)){
			viewholder.typeImg.setBackgroundResource(R.drawable.xlsimg);
		} else if ("ppt".equals(rateCard.扩展名) || "pptx".equals(rateCard.扩展名)){
			viewholder.typeImg.setBackgroundResource(R.drawable.pptimg);
		} else if ("pdf".equals(rateCard.扩展名)){
			viewholder.typeImg.setBackgroundResource(R.drawable.pdfimg);
		} else {
			viewholder.typeImg.setBackgroundResource(R.drawable.pngimg);
		}
		String s = rateCard.刊例名称+"("+rateCard.刊例大小+"K)";
		viewholder.name.setText(s);
		viewholder.allLayout.setOnClickListener(getClickListener(position));
		
		return convertView;
	}
	
	static class RatecardViewholder{
		public ImageView typeImg;
		public TextViewFont name;
		public LinearLayout allLayout;
	}

}
