package com.MeiHuaNet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.KnowledgeObject;
import com.MeiHuaNet.utils.StringUtils;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.EllipsizingTextView;
import com.MeiHuaNet.view.TextViewFont;
import com.nostra13.universalimageloader.core.ImageLoader;

public class KnowledgeAdapter extends MyAdapter {

	LayoutInflater mInflater;
	ArrayList<KnowledgeObject> listData;
	Context context;

	public KnowledgeAdapter(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setListData(ArrayList<KnowledgeObject> listData) {
		this.listData = listData;
	}

	@Override
	public int getCount() {
		return listData == null ? 0 : listData.size();
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
		KnowledgeHolderView holderView = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_info_item, null);
			holderView = new KnowledgeHolderView();
			holderView.title = (TextViewFont) convertView
					.findViewById(R.id.info_item_title);
			holderView.imgView = (ImageView) convertView
					.findViewById(R.id.info_item_img);
			holderView.summary = (EllipsizingTextView) convertView
					.findViewById(R.id.info_item_summary);
			holderView.date = (TextViewFont) convertView
					.findViewById(R.id.info_item_date);
			holderView.views = (TextViewFont) convertView
					.findViewById(R.id.info_item_views);
			holderView.alllayout = (LinearLayout) convertView
					.findViewById(R.id.info_item_all_layout);
			convertView.setTag(holderView);
		} else {
			holderView = (KnowledgeHolderView) convertView.getTag();
		}
		KnowledgeObject knowledgeObject = listData.get(position);
		holderView.title.setText(knowledgeObject.Title);
		holderView.summary.setMaxLines(5);
		holderView.summary.setText(StringUtils
				.getSummaryStr(knowledgeObject.Description));
		holderView.date.setText(knowledgeObject.getDate());
		int views = knowledgeObject.getViews();
		if (views > 0) {
			holderView.views.setText(Utils.getResString(R.string.read) + views);
			holderView.views.setVisibility(View.VISIBLE);
		} else {
			holderView.views.setVisibility(View.GONE);
		}
		holderView.alllayout.setOnClickListener(getClickListener(position));
		// holderView.imgView.setTag(knowledgeObject.ImgURL);
		// final Drawable drawable =
		// AsyncImageLoader.getInstance(context).loadDrawable(knowledgeObject.ImgURL,
		// new ImageCallback() {
		//
		// @Override
		// public void imageLoaded(Drawable imageDrawable, String imageUrl,
		// ImageView view) {
		// if(imageDrawable !=null){
		// if(imageUrl.equals(view.getTag())){
		// view.setImageDrawable(imageDrawable);
		// }
		//
		// }
		// }
		// }, holderView.imgView);
		// if(drawable != null){
		// holderView.imgView.setImageDrawable(drawable);
		// } else {
		// holderView.imgView.setImageResource(R.drawable.default_img);
		// }
		ImageLoader.getInstance().displayImage(knowledgeObject.ImgURL,
				holderView.imgView);
		return convertView;
	}

	static class KnowledgeHolderView {
		public LinearLayout alllayout;
		public TextViewFont title;
		public ImageView imgView;
		public EllipsizingTextView summary;
		public TextViewFont date;
		public TextViewFont views;
	}
}
