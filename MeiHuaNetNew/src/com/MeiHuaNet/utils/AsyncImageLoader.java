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
 * �첽����ͼƬ
 * 
 * @author wangshuilong
 * 
 */
public class AsyncImageLoader {
	// ���ص�ͼƬ�������� ��SoftReference �ᾡ���ܳ��ı�������ֱ�� JVM �ڴ治��ʱ�Żᱻ����
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
	 * ������Դ
	 * 
	 * @param imageUrl
	 *            ��Դ��ַ
	 * @param imageCallback
	 *            �ص��ӿ�
	 * @param fileName
	 *            ͼƬ�������ļ��е��ļ�����Ϊ��ʱ���������ļ��У�
	 * @return
	 */
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback, final ImageView view) {
		if (imageUrl != null && imageUrl.length() > 0) {
			if (imageCache.containsKey(imageUrl)) {// ������˾�ֱ�Ӷ�����
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
							// ���سɹ���ص�
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
	 * ���سɹ������ظ���Դ
	 * 
	 * @param url
	 * @return
	 */
	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		// url��������ʱ����������Ҫ�ȱ���
		String urls = null;
		try {
//			Utils.log("imaurl is : "+url);
			urls = URLEncoder.encode(url, "utf-8");
			
//			Utils.log("imgurl is : "+urls);
			urls = urls.replaceAll("\\+", "%20");// ����֮��ո��ɡ�+�����ո�ı����ʾ�ǡ�%20��
													// ���Խ����еġ�+���滻�ɡ�%20���Ϳ�����
			urls = urls.replaceAll("%3A", ":");// ����֮���·���еġ�:��Ҳ��ɱ���Ķ�����
												// ���л��н����滻���� ��������������·��
			urls = urls.replaceAll("%2F", "/");// ����֮���·���еġ�/��Ҳ��ɱ���Ķ�����
												// ���л��н����滻���� ��������������·��
			urls = urls.replaceAll("%28", "(");
			urls = urls.replaceAll("%29", ")");
			urls = urls.replaceAll("%3D", "=");
			urls = urls.replaceAll("%3F", "?");
//			Utils.log("imageurl after is��"+urls);
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
		} catch (Error error) {// ͼƬ̫��outofmemory����ʱ����ȡ��ͼƬ�Ĵ�С��Ϊԭ��С��10��֮һ���ٴλ�ȡͼƬ
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
				options.inSampleSize = 5; // width��hight��Ϊԭ����5��һ
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

	static Bitmap drawableToBitmap(Drawable drawable)// drawable ת����bitmap
	{
		int width = drawable.getIntrinsicWidth(); // ȡdrawable�ĳ���
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // ȡdrawable����ɫ��ʽ
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // ������Ӧbitmap
		Canvas canvas = new Canvas(bitmap); // ������Ӧbitmap�Ļ���
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // ��drawable���ݻ���������
		return bitmap;
	}

	static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);// drawableת����bitmap
		Matrix matrix = new Matrix(); // ��������ͼƬ�õ�Matrix����
		float scaleWidth = ((float) w / width); // �������ű���
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight); // �������ű���
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true); // �����µ�bitmap���������Ƕ�ԭbitmap�����ź��ͼ
		return new BitmapDrawable(newbmp); // ��bitmapת����drawable������
	}

	public interface ImageCallback {
		/**
		 * 
		 * @param imageDrawable
		 *            ���غõ�ͼƬ��Drawable����
		 * @param imageUrl
		 *            ͼƬ���ص�URL
		 * @param view
		 *            ���غõ�ͼƬ���õ���ImageView����Ϊ��
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
