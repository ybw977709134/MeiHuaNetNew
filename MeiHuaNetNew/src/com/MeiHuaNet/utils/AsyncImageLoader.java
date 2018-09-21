package com.MeiHuaNet.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/****
 * 异步下载图片
 * 
 * @author wangshuilong
 * 
 */
public class AsyncImageLoader {
	// 下载的图片用若缓存 。SoftReference 会尽可能长的保留引用直到 JVM 内存不足时才会被回收
	private HashMap<String, SoftReference<Drawable>> imageCache;
	private static Context mContext;
	public boolean isUsing = true;
	private static AsyncImageLoader asyncImageLoader;
	
	private AsyncImageLoader(){
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}
	public static AsyncImageLoader getInstance(Context context){
		mContext = context;
		if(asyncImageLoader ==null){
			asyncImageLoader = new AsyncImageLoader();
		}
		return asyncImageLoader;
	}

	/***
	 * 下载资源
	 * 
	 * @param imageUrl
	 *            资源地址
	 * @param imageCallback
	 *            回调接口
	 * @param fileName
	 *            图片保存在文件中的文件名（为空时不保存在文件中）
	 * @return
	 */
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback, final ImageView view) {
		if (imageUrl != null && imageUrl.length() > 0) {
			if (imageCache.containsKey(imageUrl)) {// 如果有了就直接读缓存
				SoftReference<Drawable> softReference = imageCache
						.get(imageUrl);
				Drawable drawable = softReference.get();
				if (drawable != null) {
					return drawable;
				}
			}
			final MyHandler handler = new MyHandler(this, imageCallback, imageUrl, view, mContext);
			new Thread() {
				@Override
				public void run() {
					try {
						if (imageUrl != null) {
							Drawable drawable = loadImageFromUrl(imageUrl);
							imageCache.put(imageUrl,
									new SoftReference<Drawable>(drawable));
							Message message = handler
									.obtainMessage(0, drawable);
							// 下载成功后回调
							handler.sendMessage(message);
						}
					} catch (Error e) {
						handler.sendEmptyMessage(1);
					}
				}
			}.start();
			return null;
		} else {
			return null;
		}
	}

	public static class MyHandler extends Handler {
		WeakReference<AsyncImageLoader> weakref;
		ImageCallback imageCallback;
		String imageUrl;
		ImageView view;
		Context context;

		public MyHandler(AsyncImageLoader asyncImageLoader,
				ImageCallback imageCallback, String imageUrl, ImageView view,
				Context context) {
			weakref = new WeakReference<AsyncImageLoader>(asyncImageLoader);
			this.imageCallback = imageCallback;
			this.imageUrl = imageUrl;
			this.view = view;
			this.context = context;
		}

		public void handleMessage(Message message) {
			switch (message.what) {
			case 0:
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl,
						view);
				if (imageCallback instanceof ImageCallBackInLiveActivity) {
					((ImageCallBackInLiveActivity) imageCallback).ImageLoaded(
							(Drawable) message.obj, imageUrl, view, context);
				}
				break;
			case 1:
				imageCallback.imageLoaded(null, "", null);
				if (imageCallback instanceof ImageCallBackInLiveActivity) {
					((ImageCallBackInLiveActivity) imageCallback).ImageLoaded(
							null, "", null, context);
				}
				break;
			default:
				break;
			}
		}
	}

	/***
	 * 下载成功并返回该资源
	 * 
	 * @param url
	 * @return
	 */
	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		// url中有中文时，请求会出错，要先编码
		String urls = null;
		try {
//			Utils.log("imaurl is : "+url);
			urls = URLEncoder.encode(url, "utf-8");
			
//			Utils.log("imgurl is : "+urls);
			urls = urls.replaceAll("\\+", "%20");// 编码之后空格变成“+”而空格的编码表示是“%20”
													// 所以将所有的“+”替换成“%20”就可以了
			urls = urls.replaceAll("%3A", ":");// 编码之后的路径中的“:”也变成编码的东西了
												// 所有还有将其替换回来 这样才是完整的路径
			urls = urls.replaceAll("%2F", "/");// 编码之后的路径中的“/”也变成编码的东西了
												// 所有还有将其替换回来 这样才是完整的路径
			urls = urls.replaceAll("%28", "(");
			urls = urls.replaceAll("%29", ")");
			urls = urls.replaceAll("%3D", "=");
			urls = urls.replaceAll("%3F", "?");
//			Utils.log("imageurl after is："+urls);
			m = new URL(urls);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Drawable d = null;
		try {
			d = Drawable.createFromStream(i, "src");
		} catch (Error error) {// 图片太大，outofmemory，此时将获取的图片的大小设为原大小的10分之一，再次获取图片
			try {
				m = new URL(urls);
				i = (InputStream) m.getContent();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inSampleSize = 5; // width，hight设为原来的5分一
				Bitmap btp = BitmapFactory.decodeStream(i, null, options);
				d = new BitmapDrawable(btp);
			} catch (Error e) {
				throw new Error();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (i != null) {
				try {
					i.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return d;
	}

	static Bitmap drawableToBitmap(Drawable drawable)// drawable 转换成bitmap
	{
		int width = drawable.getIntrinsicWidth(); // 取drawable的长宽
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取drawable的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把drawable内容画到画布中
		return bitmap;
	}

	static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);// drawable转换成bitmap
		Matrix matrix = new Matrix(); // 创建操作图片用的Matrix对象
		float scaleWidth = ((float) w / width); // 计算缩放比例
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true); // 建立新的bitmap，其内容是对原bitmap的缩放后的图
		return new BitmapDrawable(newbmp); // 把bitmap转换成drawable并返回
	}

	public interface ImageCallback {
		/**
		 * 
		 * @param imageDrawable
		 *            下载好的图片的Drawable对象
		 * @param imageUrl
		 *            图片下载的URL
		 * @param view
		 *            下载好的图片设置到的ImageView，可为空
		 */
		public void imageLoaded(Drawable imageDrawable, String imageUrl,
				ImageView view);
	}

	public static abstract class ImageCallBackInLiveActivity implements
			ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl,
				ImageView view) {

		}

		public abstract void ImageLoaded(Drawable imageDrawable,
				String imageUrl, ImageView view, Context context);
	}

}
