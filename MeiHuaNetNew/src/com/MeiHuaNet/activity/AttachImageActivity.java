package com.MeiHuaNet.activity;

import java.io.File;

import com.MeiHuaNet.R;
import com.MeiHuaNet.listener.MulitPointTouchListener;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.utils.AsyncImageLoader;
import com.MeiHuaNet.utils.DensityUtil;
import com.MeiHuaNet.utils.FileUtils;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.Utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * 
 * @description 查看附件中图片的activity
 * @author lee
 * @createTime 2013-7-19下午4:25:16
 * 
 */
public class AttachImageActivity extends BaseActivity {

	private String imageUrl;
	private ImageView imageView;
//	private boolean isLoad = false;
	private static final String picCachePath = "miceCache/pic";
	private AsyncImageLoader asyncImageLoader;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.layout_attach_image);

		imageUrl = getIntent().getStringExtra("imageUrl");
		initView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (asyncImageLoader != null) {
			asyncImageLoader.isUsing = false;
		}
	}

	private void initView() {
		initTitle();

		imageView = (ImageView) findViewById(R.id.attach_activity_image);
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.attach_activity_ProgressBar);
		Drawable cachedImage;
		cachedImage = FileUtils.fetchDrawableFromFile("dcim/camera",
				imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
		if (cachedImage == null) {
			cachedImage = FileUtils.fetchDrawableFromFile(picCachePath,
					imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
		}
		if (cachedImage == null) {
			asyncImageLoader = AsyncImageLoader.getInstance(this);
			cachedImage = asyncImageLoader.loadDrawable(imageUrl,
					new AsyncImageLoader.ImageCallBackInLiveActivity() {

						@Override
						public void ImageLoaded(Drawable imageDrawable,
								String imageUrl, ImageView view, Context context) {
							if (imageDrawable != null) {
								imageView.setImageDrawable(imageDrawable);
//								isLoad = true;
								cacheImage();
							} else {
								if (asyncImageLoader != null
										&& asyncImageLoader.isUsing)
									Utils.Toast(Utils
											.getResString(R.string.download_image_fail));
							}
							progressBar.setVisibility(View.GONE);
						}
					}, null);
		}
		if (cachedImage != null) {
			imageView.setImageDrawable(cachedImage);
//			isLoad = true;
			progressBar.setVisibility(View.GONE);
		}

		imageView.setOnTouchListener(new MulitPointTouchListener());
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleTitle = new HandleBackTitle(relativeLayout);
		handleTitle.setrightBtnLayout(DensityUtil.dip2px(this, 65),
				DensityUtil.dip2px(this, 40));
		handleTitle.setTitleView(Utils.getResString(R.string.image), -1);
		handleTitle.setListener(
				new OnBackBtnListener(AttachImageActivity.this), null);
	}

//	/**
//	 * 用户点了存储按钮后，将图片保存到相册中，保存的图片程序不会删除，需要用户自己手动删除
//	 */
//	private void savePicture() {
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//			String SDCard = Environment.getExternalStorageDirectory()
//					.getAbsolutePath();
//			String filePath = "dcim/camera";
//			// 目录路径
//			String dir = SDCard + File.separator + filePath;
//			String fileName = dir + File.separator
//					+ imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
//			FileOutputStream fileOutputStream = null;
//			File file = new File(fileName);
//			if (file.exists()) {
//				return;
//			} else {
//				try {
//					new File(dir).mkdir();// 新建文件夹
//					file.createNewFile();// 新建文件
//					fileOutputStream = new FileOutputStream(file);
//					Drawable drawable = imageView.getDrawable();
//					BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//					if (bitmapDrawable.getBitmap().compress(
//							Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
//						fileOutputStream.flush();
//					}
//					Intent intent = new Intent(
//							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//					intent.setData(Uri.fromFile(file));
//					sendBroadcast(intent);
//				} catch (Exception e) {
//				} finally {
//					try {
//						fileOutputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//
//	}

	/**
	 * 将用户看过的图片缓存在文件中，方便下次打开时不用从网络获取（缓存图片文件的文件夹大小超过300M时，清空一次文件夹）
	 */
	private void cacheImage() {
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		Drawable drawable = imageView.getDrawable();
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

		File file = FileUtils.createDir(picCachePath);
		long size = FileUtils.getFileSize(file);
		if (size > 100) {
			FileUtils.delAllFile(Environment.getExternalStorageDirectory()
					+ File.separator + picCachePath);
		}
		FileUtils.storeBitmapToFile(bitmapDrawable.getBitmap(), picCachePath,
				fileName, false);
	}
}
