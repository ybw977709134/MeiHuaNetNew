package com.MeiHuaNet.adapter;

import java.util.ArrayList;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.OriginalityListJsonObject.OriginalityListObject;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.TextViewFont;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * @description 创意列表页面的adapter
 * @author lee
 * @time 2013-12-16 上午10:15:02
 * 
 */
public class OriginalityListAdapter extends MyAdapter {

	private Context mContext;
	private ArrayList<OriginalityListObject> mListData;

	public OriginalityListAdapter(Context context) {
		mContext = context;
	}

	public void setListData(ArrayList<OriginalityListObject> listData) {
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
		OriginalityListViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.layout_originality_listitem, null);
			viewHolder = new OriginalityListViewHolder();
			viewHolder.allLayout = (LinearLayout) convertView
					.findViewById(R.id.originality_item_all_layout);
			viewHolder.title = (TextViewFont) convertView
					.findViewById(R.id.originality_item_title);
			viewHolder.img = (ImageView) convertView
					.findViewById(R.id.originality_item_img);
			viewHolder.name = (TextViewFont) convertView
					.findViewById(R.id.originality_item_name);
			viewHolder.language = (TextViewFont) convertView
					.findViewById(R.id.originality_item_language);
			viewHolder.country = (TextViewFont) convertView
					.findViewById(R.id.originality_item_country);
			viewHolder.year = (TextViewFont) convertView
					.findViewById(R.id.originality_item_year);
			viewHolder.date = (TextViewFont) convertView
					.findViewById(R.id.originality_item_date);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (OriginalityListViewHolder) convertView.getTag();
		}
		OriginalityListObject object = mListData.get(position);
		viewHolder.title.setText(object.Title);
		// viewHolder.img.setTag(object.ImgURL);
		// final Drawable drawable = AsyncImageLoader.getInstance(mContext)
		// .loadDrawable(object.ImgURL, new ImageCallback() {
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
		// }, viewHolder.img);
		// if (drawable != null) {
		// viewHolder.img.setImageDrawable(drawable);
		// } else {
		// viewHolder.img.setImageResource(R.drawable.default_img);
		// }
		ImageLoader.getInstance().displayImage(object.ImgURL, viewHolder.img);
		viewHolder.name.setText(Utils.getResString(R.string.brandname) + " "
				+ object.BrandName);
		viewHolder.language.setText(Utils.getResString(R.string.language) + " "
				+ object.Language);
		viewHolder.country.setText(Utils.getResString(R.string.country) + " "
				+ object.Country);
		viewHolder.year.setText(Utils.getResString(R.string.year_of_advertise)
				+ " " + object.Year);
		viewHolder.date.setText(Utils.getResString(R.string.date_of_advertise)
				+ " " + object.Date);
		viewHolder.allLayout.setOnClickListener(getClickListener(position));

		return convertView;
	}

	static class OriginalityListViewHolder {
		TextViewFont title;
		ImageView img;
		TextViewFont name;
		TextViewFont language;
		TextViewFont country;
		TextViewFont year;
		TextViewFont date;
		LinearLayout allLayout;
	}

}
