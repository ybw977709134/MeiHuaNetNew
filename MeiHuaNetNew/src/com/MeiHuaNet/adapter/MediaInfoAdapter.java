package com.MeiHuaNet.adapter;

import java.util.ArrayList;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.MediainfoObject;
import com.MeiHuaNet.view.EllipsizingTextView;
import com.MeiHuaNet.view.TextViewFont;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MediaInfoAdapter extends MyAdapter {

	private Context mContext;
	private ArrayList<MediainfoObject> mListData;

	public MediaInfoAdapter(Context context) {
		mContext = context;
	}

	public void setListData(ArrayList<MediainfoObject> listData) {
		mListData = listData;
	}

	@Override
	public int getCount() {
		return mListData == null ? 0 : mListData.size();
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
		MediaInfoViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.layout_mediainfo_item, null);
			viewHolder = new MediaInfoViewHolder();
			viewHolder.alllayout = (LinearLayout) convertView
					.findViewById(R.id.media_item_all_layout);
			viewHolder.title = (TextViewFont) convertView
					.findViewById(R.id.media_item_title);
			viewHolder.titleEn = (TextViewFont) convertView
					.findViewById(R.id.media_item_title_en);
			viewHolder.logo = (ImageView) convertView
					.findViewById(R.id.media_item_img);
			viewHolder.category = (TextViewFont) convertView
					.findViewById(R.id.media_item_category);
			viewHolder.intro = (EllipsizingTextView) convertView
					.findViewById(R.id.media_item_intro);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MediaInfoViewHolder) convertView.getTag();
		}
		MediainfoObject mediainfoObject = mListData.get(position);
		viewHolder.title.setText(mediainfoObject.Name);
		if (!TextUtils.isEmpty(mediainfoObject.EnName)) {
			viewHolder.titleEn.setText(mediainfoObject.EnName);
		}
		// viewHolder.logo.setTag(mediainfoObject.Logo);
		// final Drawable drawable = AsyncImageLoader.getInstance(mContext)
		// .loadDrawable(mediainfoObject.Logo, new ImageCallback() {
		//
		// @Override
		// public void imageLoaded(Drawable imageDrawable,
		// String imageUrl, ImageView view) {
		// if (imageDrawable != null) {
		// if (imageUrl.equals(view.getTag())) {
		// view.setImageDrawable(imageDrawable);
		// }
		// }
		// }
		// }, viewHolder.logo);
		// if (drawable != null) {
		// viewHolder.logo.setImageDrawable(drawable);
		// } else {
		// viewHolder.logo.setImageResource(R.drawable.default_img);
		// }
		ImageLoader.getInstance().displayImage(mediainfoObject.Logo,
				viewHolder.logo);
		viewHolder.intro.setMaxLines(3);
		viewHolder.intro.setText(mediainfoObject.IntroText);
		if (mediainfoObject.Category != null
				&& mediainfoObject.Category.trim().length() > 0) {
			viewHolder.category.setText(mediainfoObject.Category);
			viewHolder.category.setVisibility(View.VISIBLE);
		} else {
			viewHolder.category.setVisibility(View.GONE);
		}
		viewHolder.alllayout.setOnClickListener(getClickListener(position));
		return convertView;
	}

	static class MediaInfoViewHolder {
		public LinearLayout alllayout;
		public TextViewFont title;
		public TextViewFont titleEn;
		public ImageView logo;
		public EllipsizingTextView intro;
		public TextViewFont category;
	}

}
