package com.MeiHuaNet.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * 继承linearlayout实现listview显示数据的功能
 * @author Administrator
 *
 */
public class LinearListView extends LinearLayout {

//	private Context mContext;
	private ListAdapter mAdapter;

	public LinearListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
//		mContext = context;
	}

	public LinearListView(Context context, AttributeSet attr) {
		super(context, attr);
//		mContext = context;
	}


	public void setAdapter(ListAdapter adapter) {
		mAdapter = adapter;
		reset();
	}
	
	
	
	private void reset() {
		removeAllViews();//移除已有的视图
		addViewFromAdapter();//添加适配器中的视图
		setObserver();//给适配器设置监听器
	}
	
	/**
	 * 给adapter设置观察者，这样适配器调用notifydatachangge()时这里会自动处理
	 */
	private void setObserver(){
		mAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged(){
				removeAllViews();
				addViewFromAdapter();
			}
		});
	}

	/**
	 * 将adapter中的view项取出来添加到linearlayout中
	 */
	private void addViewFromAdapter() {
		if (mAdapter != null && mAdapter.getCount() > 0) {
			for (int i = 0; i < mAdapter.getCount(); i++) {
				View view = mAdapter.getView(i, null, null);
				this.addView(view, i);
			}
		}
	}
	

}
