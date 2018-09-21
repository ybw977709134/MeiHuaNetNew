package com.MeiHuaNet.adapter;

import com.MeiHuaNet.listener.OnListItemClickListener;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

public abstract class MyAdapter extends BaseAdapter {

	OnListItemClickListener listener;

	public void setItemListener(OnListItemClickListener listener) {
		this.listener = listener;
	}

	public OnClickListener getClickListener(final int position) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener!=null){
					listener.onclick(v, position);
				}
			}
		};
	}
}
