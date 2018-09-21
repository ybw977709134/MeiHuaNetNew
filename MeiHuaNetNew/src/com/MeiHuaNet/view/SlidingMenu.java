package com.MeiHuaNet.view;

import java.lang.reflect.Method;

import com.MeiHuaNet.utils.DensityUtil;
import com.MeiHuaNet.utils.Utils;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ViewFlipper;

/**
 * 
 * @description 菜单栏框架中上面一层可滑动的视图的实现类
 * @author lee
 * @createTime 2013-6-26上午9:34:40
 * 
 */
public class SlidingMenu extends HorizontalScrollView {

	/* 当前控件 */
	private SlidingMenu me;

	/* 左侧菜单栏是否显示了 */
	public static boolean menuOut;

	/* 主界面中顶部栏的左边的按钮视图，点击后打开/关闭菜单栏 */
	// private View menuBtn;

	/* 左侧的菜单视图 */
	private View menuView;

	/* 菜单显示的宽度 */
	private int menuWidth;

	// /* 当前滑动视图的x坐标的位置 */
	// private int currentX;

	/* 手势动作最开始时的x坐标 */
	private float lastMotionX = -1;

	private int scrollToViewPos;

	/* 菜单栏框架中主体视图的视图组 */
	private ViewFlipper bodyViewGroup;

	public SlidingMenu(Context context) {
		super(context);
		init();
	}

	public SlidingMenu(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}

	public SlidingMenu(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		init();
	}

	private void init() {
		// 这段代码用于关闭2.3版本以后有些手机listview自带的下拉阴影,2.3之前的android包中没有下面的这个方法
		try {
			int version = Utils.getAndroidSDKVersion();
			if (version >= 10) {
				Method m = HorizontalScrollView.class.getDeclaredMethod(
						"setOverScrollMode", int.class);
				m.setAccessible(true);
				m.invoke(this, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setHorizontalFadingEdgeEnabled(false);
		this.setVerticalFadingEdgeEnabled(false);
		this.me = this;
		this.me.setVisibility(View.INVISIBLE);
		menuOut = false;
		// ENLARGE_WIDTH = dip2px(getContext(), -15);
	}

	public void initViews(View[] children, SizeCallBack sizeCallBack,
			View menuView) {
		this.menuView = menuView;
		ViewGroup parent = (ViewGroup) getChildAt(0);

		this.menuView.setVisibility(View.INVISIBLE);
		for (int i = 0; i < children.length; i++) {
			children[i].setVisibility(INVISIBLE);
			parent.addView(children[i]);
		}
		this.bodyViewGroup = (ViewFlipper) children[1];

		OnGlobalLayoutListener onGlLayoutistener = new MenuOnGlobalLayoutListener(
				parent, children, sizeCallBack);
		getViewTreeObserver().addOnGlobalLayoutListener(onGlLayoutistener);
	}

	public void setBody(View view) {
		clickMenuBtn();
		this.bodyViewGroup.removeAllViews();
		this.bodyViewGroup.addView(view);
	}


	public void clickMenuBtn() {

		if (!menuOut) {
			this.menuWidth = 0;// 菜单栏没有显示，宽度为0，scrollview滑动到0.也就是显示左边的透明视图
		} else {
			this.menuWidth = this.menuView.getMeasuredWidth();
		}
		menuSlide();
	}

	public void menuSlide() {
		if (menuWidth == 0) {
			menuOut = true;
		} else {
			menuOut = false;
		}
		// 左边透明视图的宽度和菜单的宽度是一样的。menuwidth=0,滚动到x=0,则透明视图全部显示，原来被遮盖的菜单就显示出来了
		me.smoothScrollTo(menuWidth, 0);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if (menuOut && l < DensityUtil.dip2px(getContext(), 40)) {
			// 判断滑动多少距离后主体部分回到屏幕，菜单栏隐藏
			this.menuWidth = 0;
		} else if (!menuOut
				&& this.menuView.getMeasuredWidth() - l > DensityUtil.dip2px(
						getContext(), 40)) {
			// 判断滑动多少距离后主体部分隐藏，菜单栏显示
			this.menuWidth = 0;
		} else {
			this.menuWidth = this.menuView.getWidth();// 菜单栏的宽度，scrollview滑动一个菜单栏宽度的距离后，菜单栏就隐藏了
		}

		// this.currentX = l;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// // TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			/* 手指按下时的x坐标 */
			this.lastMotionX = (int) ev.getRawX();
		}
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			menuSlide();
			return false;
		}
		if (menuOut) {

			if (lastMotionX < this.menuView.getWidth()) {
				return false;
			}
		}
		return super.onTouchEvent(ev);
	}

	public class MenuOnGlobalLayoutListener implements OnGlobalLayoutListener {

		private ViewGroup parent;
		private View[] children;
		private SizeCallBack sizeCallBack;

		public MenuOnGlobalLayoutListener(ViewGroup parent, View[] children,
				SizeCallBack sizeCallBack) {

			this.parent = parent;
			this.children = children;
			this.sizeCallBack = sizeCallBack;
		}

		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			me.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			this.sizeCallBack.onGlobalLayout();
			this.parent.removeViewsInLayout(0, children.length);
			int width = me.getMeasuredWidth();
			int height = me.getMeasuredHeight();

			int[] dims = new int[2];
			scrollToViewPos = 0;

			for (int i = 0; i < children.length; i++) {
				this.sizeCallBack.getViewSize(i, width, height, dims);
				children[i].setVisibility(View.VISIBLE);

				parent.addView(children[i], dims[0], dims[1]);
				if (i == 0) {
					scrollToViewPos += dims[0];
				}
			}
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					me.scrollBy(scrollToViewPos, 0);

					/* 视图不是中间视图 */
					me.setVisibility(View.VISIBLE);
					menuView.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	public static interface SizeCallBack {

		public void onGlobalLayout();

		public void getViewSize(int idx, int width, int height, int[] dims);
	}

	public static class SizeCallBackForMenu implements SizeCallBack {

		private int menuWidth;
		private Context context;

		public SizeCallBackForMenu(Context context) {
			super();
			this.context = context;
		}

		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			this.menuWidth = dip2px(context, 50);
		}

		@Override
		public void getViewSize(int idx, int width, int height, int[] dims) {
			// TODO Auto-generated method stub
			dims[0] = width;
			dims[1] = height;

			/* 视图不是中间视图 */
			if (idx != 1) {
				// 这个值其实就是左边的透明视图的宽度
				dims[0] = width - this.menuWidth;
			}
		}

	}

	/**
	 * 将dp转换成对应的px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
