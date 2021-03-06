package com.MeiHuaNet.view;

import java.lang.reflect.Method;

import com.MeiHuaNet.R;
import com.MeiHuaNet.utils.DateUtil;
import com.MeiHuaNet.utils.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @description 
 *              菜单栏中左边的菜单用的listview（为了实现菜单栏拖动返回内容页，此listview与一般的可下拉listview的ontouch事件处理不同
 *              ）
 * @author lee
 * @createTime 2013-9-9上午9:36:38
 * 
 */
public class MenuListView extends ListView {

	private int type = 0;
	private int originX = -1;
	private int originY = -1;
	private SlidingMenu slidingMenu;

	protected int state;
	final static int RELEASE_To_REFRESH = 0;
	final static int PULL_To_REFRESH = 1;
	final static int REFRESHING = 2;
	protected final static int DONE = 3;
	final static int LOADING = 4;
	final static int RATIO = 3;
	protected LinearLayout headView;
	protected int headContentHeight;
	protected TextView tipsTextview;
	protected TextView lastUpdatedTextView;
	protected ImageView arrowImageView;
	protected ProgressBar progressBar;
	protected RotateAnimation animation;
	protected RotateAnimation reverseAnimation;
	boolean isRecored;
	int startY;
	int firstItemIndex;// listview第一个可见项的索引值
	boolean isBack;
	protected boolean isHeadRefreshable;
	protected boolean isBottomRefreshable;
	protected Context context;
	OnDownRefreshListener listener;
	OnScrollToBottomListener bottomListener;
	int totalItemCount;// listview的item项的数量

	/**
	 * @param context
	 */
	public MenuListView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public MenuListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public void setSlidingMenu(SlidingMenu slidingMenu) {
		this.slidingMenu = slidingMenu;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				originX = (int) ev.getRawX();
				originY = (int) ev.getRawY();
			}
			type = 0;
			Utils.log("menulist down");
			return super.onInterceptTouchEvent(ev);
		case MotionEvent.ACTION_MOVE:

			if (originX == -1 || originY == -1) {
				originX = (int) ev.getRawX();
				originY = (int) ev.getRawY();
			}
			int difX = (int) Math.abs((ev.getRawX() - originX));
			int difY = (int) Math.abs(ev.getRawY() - originY);
			Utils.log("menulist move" + " difX is :" + difX + " difY is :"
					+ difY + "type is :" + type);
			if (type == 0 && (difX > 15 || difY > 15)) {

				if (difX > difY) {
					type = 1;
					return true;
				} else {
					type = 2;
					return super.onInterceptTouchEvent(ev);
				}
			} else {
				return super.onInterceptTouchEvent(ev);
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 设置滚动到底部的监听器
	 * 
	 * @param listener
	 */
	public void setOnBottomRefreshListener(OnScrollToBottomListener listener) {
		this.bottomListener = listener;
	}

	/**
	 * 设置下拉刷新的监听器
	 * 
	 * @param listener
	 */
	public void setOnDownRefreshListener(OnDownRefreshListener listener) {
		this.listener = listener;
	}

	/**
	 * 刷新完成后在主线程中调用这个方法，隐藏刷新的头部
	 */
	public void onHeadRefreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText("上次更新于:" + DateUtil.getDateStr());
		changeHeaderViewByState();
	}

	/**
	 * 设置是否可以刷新头部
	 * 
	 * @param isHeadRefreshable
	 */
	public void setisHeadRefreshable(boolean isHeadRefreshable) {
		this.isHeadRefreshable = isHeadRefreshable;
	}

	public boolean isBottomRefreshable() {
		return isBottomRefreshable;
	}

	/**
	 * 设置底部是否可刷新
	 * 
	 * @param isBottomRefreshable
	 */
	public void setBottomRefreshable(boolean isBottomRefreshable) {
		this.isBottomRefreshable = isBottomRefreshable;
	}

	protected void init() {

		Utils.log("super init");
		this.setFadingEdgeLength(0);
		Context con = context;
		this.setCacheColorHint(0x00000000);

		// 这段代码用于关闭2.3版本以后有些手机listview自带的下拉阴影
		try {
			Method m = AbsListView.class.getDeclaredMethod("setOverScrollMode",
					int.class);
			m.setAccessible(true);
			m.invoke(this, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		headView = (LinearLayout) View.inflate(con, R.layout.downrefresh_head,
				null);

		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		this.addHeaderView(headView, null, false);
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		// 默认是头部可刷新，底部不可刷新的
		isHeadRefreshable = true;
		isBottomRefreshable = false;

		this.setOnScrollListener(ssListScroll);
		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				// TODO Auto-generated method stub
				switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					handleHeadEvent(v, ev);
					if (ev.getAction() == MotionEvent.ACTION_DOWN) {
						originX = (int) ev.getRawX();
						originY = (int) ev.getRawY();
					}
					type = 0;
					Utils.log("menulist down");
					return true;
				case MotionEvent.ACTION_MOVE:

					if (originX == -1 || originY == -1) {
						originX = (int) ev.getRawX();
						originY = (int) ev.getRawY();
					}
					int difX = (int) Math.abs((ev.getRawX() - originX));
					int difY = (int) Math.abs(ev.getRawY() - originY);
					Utils.log("menulist move" + " difX is :" + difX
							+ " difY is :" + difY + "type is :" + type);
					if (type == 1) {
						slidingMenu.scrollTo(
								0 - (int) ((ev.getRawX() - originX)),
								slidingMenu.getScrollY());
						return true;
					} else if (type == 2) {
						handleHeadEvent(v, ev);
						return false;
					}
					if (type == 0 && (difX > 15 || difY > 15)) {

						if (difX > difY) {
							type = 1;
							slidingMenu.scrollTo(
									0 - (int) ((ev.getRawX() - originX)),
									slidingMenu.getScrollY());
							return true;
						} else {
							type = 2;
							return false;
						}
					} else {
						return true;
					}
				case MotionEvent.ACTION_CANCEL:
					originX = -1;
					originY = -1;
					break;
				case MotionEvent.ACTION_UP:
					Utils.log("menulist up");
					originX = -1;
					originY = -1;
					if (type == 1) {
						type = 0;
						slidingMenu.menuSlide();
						return true;
					}
					handleHeadEvent(v, ev);
					type = 0;
					break;
				}
				return false;
			}
		});

		// this.setOnTouchListener(new OnTouchListener() {
		// public boolean onTouch(View v, MotionEvent event) {
		// if (isHeadRefreshable) {
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// if (firstItemIndex == 0 && !isRecored) {
		// isRecored = true;
		// startY = (int) event.getY();
		// }
		// break;
		// case MotionEvent.ACTION_UP:
		// if (state != REFRESHING && state != LOADING) {
		// if (state == DONE) {
		// }
		// if (state == PULL_To_REFRESH) {
		// state = DONE;
		// changeHeaderViewByState();
		// }
		// if (state == RELEASE_To_REFRESH) {
		// state = REFRESHING;
		// changeHeaderViewByState();
		// if (listener != null) {
		// listener.onRefresh();
		// }
		// }
		// }
		// startY = -1;
		// isRecored = false;
		// isBack = false;
		// break;
		//
		// case MotionEvent.ACTION_MOVE:
		// int tempY = (int) event.getY();
		//
		// if (startY == -1) {
		// startY = tempY;
		// }
		// if (!isRecored && firstItemIndex == 0) {
		// isRecored = true;
		// startY = tempY;
		// }
		//
		// int offset = (tempY - startY) / RATIO;
		//
		// if (state != REFRESHING && isRecored
		// && state != LOADING) {
		//
		// if (state == RELEASE_To_REFRESH) {
		//
		// MenuListView.this.setSelection(0);
		// if ((offset < headContentHeight)
		// && (tempY - startY) > 0) {
		// state = PULL_To_REFRESH;
		// changeHeaderViewByState();
		// } else if (tempY - startY <= 0) {
		// state = DONE;
		// changeHeaderViewByState();
		// }
		// }
		//
		// if (state == PULL_To_REFRESH) {
		// MenuListView.this.setSelection(0);
		// if (offset >= headContentHeight) {
		// state = RELEASE_To_REFRESH;
		// isBack = true;
		// changeHeaderViewByState();
		// } else if (tempY - startY <= 0) {
		// state = DONE;
		// changeHeaderViewByState();
		// }
		// }
		//
		// if (state == DONE) {
		// if (tempY - startY > 0) {
		// state = PULL_To_REFRESH;
		// changeHeaderViewByState();
		// }
		// }
		//
		// if (state == PULL_To_REFRESH) {
		// headView.setPadding(0, -1 * headContentHeight
		// + offset, 0, 0);
		//
		// }
		//
		// if (state == RELEASE_To_REFRESH) {
		// headView.setPadding(0, offset
		// - headContentHeight, 0, 0);
		// }
		//
		// }
		//
		// // 下面这个值是用来判断是否刷新底部的
		// if (isBottomRefreshable
		// && getLastVisiblePosition() == totalItemCount - 1
		// && tempY - startY < 0) {
		// if (bottomListener != null) {
		// bottomListener.bottomRefresh();
		// }
		// }
		//
		// break;
		// }
		// }
		// return false;
		// }
		// });
	}

	private void handleHeadEvent(View v, MotionEvent event) {
		if (isHeadRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						if (listener != null) {
							listener.onRefresh();
						}
					}
				}
				startY = -1;
				isRecored = false;
				isBack = false;
				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (startY == -1) {
					startY = tempY;
				}
				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}

				int offset = (tempY - startY) / RATIO;

				if (state != REFRESHING && isRecored && state != LOADING) {

					if (state == RELEASE_To_REFRESH) {

						MenuListView.this.setSelection(0);
						if ((offset < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}

					if (state == PULL_To_REFRESH) {
						MenuListView.this.setSelection(0);
						if (offset >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight + offset,
								0, 0);

					}

					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, offset - headContentHeight, 0, 0);
					}

				}

				// 下面这个值是用来判断是否刷新底部的
				if (isBottomRefreshable
						&& getLastVisiblePosition() == totalItemCount - 1
						&& tempY - startY < 0) {
					if (bottomListener != null) {
						bottomListener.bottomRefresh();
					}
				}
			}
		}
	}

	void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText(Utils.getResString(R.string.loosen_refresh));
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);

			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText(Utils.getResString(R.string.down_refresh));
			} else {
				tipsTextview.setText(Utils.getResString(R.string.down_refresh));
			}
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText(Utils.getResString(R.string.refreshing));
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			tipsTextview.setText(Utils.getResString(R.string.done));
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		}
	}

	protected void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * ListView滚动用于记录firstItemIndex
	 */
	AbsListView.OnScrollListener ssListScroll = new OnScrollListener() {
		public void onScrollStateChanged(AbsListView abslistview,
				int scrollState) {
			// 判断滚动到底部 ,是否刷新底部的判断，应该也可以在使用这个listview的activity中设置
			// if (isBottomRefreshable
			// && abslistview.getLastVisiblePosition() == (abslistview
			// .getCount() - 1)) {
			// // 然后 经行一些业务操作
			// if (bottomListener != null) {
			// bottomListener.bottomRefresh();
			// }
			// }
		}

		public void onScroll(AbsListView abslistview, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 这一句用于下拉刷新
			firstItemIndex = firstVisibleItem;
			MenuListView.this.totalItemCount = totalItemCount;

			if (onScrollingListener != null) {
				onScrollingListener.onScroll(abslistview, firstVisibleItem,
						visibleItemCount, totalItemCount);
			}
		}

	};

	/**
	 * 下拉刷新的列表，下拉时的监听器
	 * 
	 * @author Administrator
	 * 
	 */
	public static interface OnDownRefreshListener {
		public void onRefresh();
	}

	/**
	 * listview滚动到底部时的刷新监听
	 * 
	 * @author Administrator
	 * 
	 */
	public static interface OnScrollToBottomListener {

		public void bottomRefresh();
	}

	private OnScrollingListener onScrollingListener;

	// 提供一个接口可以处理滚动事件，因为listview自己监听了scrolllistener,不能再在别处直接调用scrolllistener
	public void setOnScrollingListener(OnScrollingListener listener) {
		this.onScrollingListener = listener;
	}

	/**
	 * 
	 * @description listview滚动中的处理
	 * @author lee
	 * @createTime 2013-7-17下午1:38:56
	 * 
	 */
	public static interface OnScrollingListener {
		public void onScroll(AbsListView abslistview, int firstVisibleItem,
				int visibleItemCount, int totalItemCount);
	}

}
