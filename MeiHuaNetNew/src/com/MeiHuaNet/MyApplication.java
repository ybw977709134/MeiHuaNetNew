package com.MeiHuaNet;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Application;
import android.content.Context;

/**
 * 
 * @description 自定义的Application类（自己处理未处理的exception）
 * @author lee
 * @time 2013-8-6 下午4:35:03
 * 
 */
public class MyApplication extends Application {

	/* 程序是否完全退出 */
	private boolean mIsExit = false;

	@Override
	public void onCreate() {
		super.onCreate();
		MycrashHandler mycrashHandler = MycrashHandler
				.getInstance(getApplicationContext());
		Thread.currentThread().setUncaughtExceptionHandler(mycrashHandler);

		initImageLoader(getApplicationContext());
	}

	public boolean ismIsExit() {
		return mIsExit;
	}

	public void setmIsExit(boolean mIsExit) {
		this.mIsExit = mIsExit;
	}

	public static void initImageLoader(Context context) {
		// Create default options which will be used for every
		// displayImage(...) call if no options will be passed to this method
		@SuppressWarnings("deprecation")
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.placeholder)
				// 设置正在加载图片
				.showImageOnLoading(R.drawable.placeholder)
				// 1.8.7新增
				.showImageForEmptyUri(R.drawable.placeholder)
				.showImageOnFail(R.drawable.placeholder)
				// 设置加载失败图片
//				.displayer(new RoundedBitmapDisplayer(20))
				.displayer(new FadeInBitmapDisplayer(400))
				.cacheInMemory(true)
				.cacheOnDisc(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPoolSize(5)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.memoryCacheSizePercentage(13) // default
				.defaultDisplayImageOptions(defaultOptions).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
