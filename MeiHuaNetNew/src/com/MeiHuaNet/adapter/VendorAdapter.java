package com.MeiHuaNet.adapter;

import java.util.ArrayList;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.VendorObject;
import com.MeiHuaNet.utils.Utils;
import com.MeiHuaNet.view.TextViewFont;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * @description 营销服务商列表的adapter
 * @author lee
 * @time 2013-12-11 下午4:04:12
 * 
 */
public class VendorAdapter extends MyAdapter {

	private Context mContext;
	private ArrayList<VendorObject> mListData;

	public VendorAdapter(Context context) {
		mContext = context;
	}

	public void setListData(ArrayList<VendorObject> listData) {
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
		VendorViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.layout_vendorlist_item, null);
			viewHolder = new VendorViewHolder();
			viewHolder.allLayout = (LinearLayout) convertView
					.findViewById(R.id.vendor_item_all_layout);
			viewHolder.title = (TextViewFont) convertView
					.findViewById(R.id.vendor_item_title);
			viewHolder.year = (TextViewFont) convertView
					.findViewById(R.id.vendor_item_year);
			viewHolder.peopleNumber = (TextViewFont) convertView
					.findViewById(R.id.vendor_item_number);
			viewHolder.logo = (ImageView) convertView
					.findViewById(R.id.vendor_item_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (VendorViewHolder) convertView.getTag();
		}

		VendorObject vendorObject = mListData.get(position);
		viewHolder.title.setText(vendorObject.VendorName);
		if (TextUtils.isEmpty(vendorObject.OpenYear)) {
			viewHolder.year.setVisibility(View.GONE);
		} else {
			viewHolder.year.setVisibility(View.VISIBLE);
			viewHolder.year.setText(Utils.getResString(R.string.openyear)
					+ vendorObject.OpenYear);
		}
		if (TextUtils.isEmpty(vendorObject.EmployeeNumber)) {
			viewHolder.peopleNumber.setVisibility(View.GONE);
		} else {
			viewHolder.peopleNumber.setVisibility(View.VISIBLE);
			viewHolder.peopleNumber.setText(Utils
					.getResString(R.string.employeeNumber)
					+ vendorObject.EmployeeNumber);
		}
		viewHolder.allLayout.setOnClickListener(getClickListener(position));
//		viewHolder.logo.setTag(vendorObject.LogoFile);
//		final Drawable drawable = AsyncImageLoader.getInstance(mContext)
//				.loadDrawable(vendorObject.LogoFile, new ImageCallback() {
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
		ImageLoader.getInstance().displayImage(vendorObject.LogoFile, viewHolder.logo);

		return convertView;
	}

	static class VendorViewHolder {
		LinearLayout allLayout;
		TextViewFont title;
		TextViewFont year;
		TextViewFont peopleNumber;
		ImageView logo;
	}

}
