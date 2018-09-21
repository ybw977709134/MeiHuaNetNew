package com.MeiHuaNet.activity;


import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.utils.DocDownloadTask;
import com.MeiHuaNet.utils.DownloadUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.view.MyWebView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.RelativeLayout;

/**
 * 
 * @description 在新闻的详情内容中点到网页链接后跳转到这里
 * @author lee
 * @time 2013-10-28 下午1:45:14
 * 
 */
public class WebviewActivity extends BaseActivity {

	private WebView mWebView;
	String url;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_webview);
		url = getIntent().getStringExtra("url");

		initView();
	}

	private void initView() {
		initTitle();

		mWebView = (WebView) findViewById(R.id.webview);
		setWebViewContent(url);
	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView("", -1);
		if (isback(url)) {
			handleBackTitle.setListener(new OnBackBtnListener(this), null);
		} else {
			handleBackTitle.setListener(onBackListener, null);
		}
	}

	private boolean isback(String url) {
		// 财新的网页总是退不出去，所以如果是显示的财新的内容时，左边的返回键就直接退出这个页面
		return url.contains("companies.caixin.com");
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setWebViewContent(String url) {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDefaultTextEncodingName("utf-8");
//		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
		// mWebView.getSettings().setLoadWithOverviewMode(false);
		// 让图片占满一个屏幕，宽度不会超过屏幕宽度
		// mWebView.getSettings()
		// .setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		// int density = (int) getResources().getDisplayMetrics().density;
		// if (density >= 2) {
		// mWebView.getSettings().setDefaultFontSize(22);
		// } else {
		// mWebView.getSettings().setDefaultFontSize(18);
		// }
		// webview的下载链接的监听，处理下载链接
		mWebView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				String suffixName = url.substring(url.lastIndexOf(".") + 1);
				if (suffixName.equalsIgnoreCase("jpg")
						|| suffixName.equalsIgnoreCase("jpeg")
						|| suffixName.equalsIgnoreCase("png")
						|| suffixName.equalsIgnoreCase("bmp")) {
					// 图片的处理
					Intent intent = new Intent();
					intent.setClass(WebviewActivity.this,
							AttachImageActivity.class);
					intent.putExtra("imageUrl", url);
					startActivity(intent);
					UIManager.cancelAllProgressDialog();
				} else {
					// 文档的处理
					DownloadUtil.downloadDoc(WebviewActivity.this, null, url,
							DocDownloadTask.PATH_VIEW,
							url.substring(url.lastIndexOf("=") + 1), 100);
				}

			}
		});
		// 在webview中加载新连接
		mWebView.setWebViewClient(new WebViewClient() {
			// 处理url。是在此weiview中加载，还是打开新的浏览器
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				int index = url.lastIndexOf(".");
				if (index != -1) {
					String type = url.substring(index + 1);
					if (type.equalsIgnoreCase("jpg")
							|| type.equalsIgnoreCase("jpeg")
							|| type.equalsIgnoreCase("png")
							|| type.equalsIgnoreCase("bmp")
							|| type.equalsIgnoreCase("pdf")
							|| type.equalsIgnoreCase("pdf+")
							|| type.equalsIgnoreCase("doc")
							|| type.equalsIgnoreCase("docx")
							|| type.equalsIgnoreCase("text")
							|| type.equalsIgnoreCase("ppt")
							|| type.equalsIgnoreCase("pptx")
							|| type.equalsIgnoreCase("xls")
							|| type.equalsIgnoreCase("xlsx")
							|| type.equalsIgnoreCase("mp4")
							|| type.equalsIgnoreCase("mpg")
							|| type.equalsIgnoreCase("avi")
							|| type.equalsIgnoreCase("rmvb")) {
						// 下载的链接就在本页面跳转，这样不会弹出选择使用浏览器的框。实际的下载操作在下载监听中进行
						UIManager.cancelAllProgressDialog();
						UIManager.showProgressDialog("", true);
						view.loadUrl(url);
						return true;
					}
				}
				UIManager.cancelAllProgressDialog();
				UIManager.showProgressDialog("", true);
				view.loadUrl(url);
				return true;
			}

			//
			// 加载错误时要做的工作
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				UIManager.cancelAllProgressDialog();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// 结束
				super.onPageFinished(view, url);
				UIManager.cancelAllProgressDialog();
			}
		});

		UIManager.cancelAllProgressDialog();
		UIManager.showProgressDialog("", true);
		mWebView.loadUrl(url);

	}

	OnClickListener onBackListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBack();
		}
	};

	private void onBack() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
