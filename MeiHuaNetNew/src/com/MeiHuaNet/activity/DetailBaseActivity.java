package com.MeiHuaNet.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.MeiHuaNet.R;
import com.MeiHuaNet.entity.KnowledgeObject;
import com.MeiHuaNet.listener.OnBackBtnListener;
import com.MeiHuaNet.utils.GestureDetectorUtil;
import com.MeiHuaNet.utils.HandleBackTitle;
import com.MeiHuaNet.utils.StringUtils;
import com.MeiHuaNet.view.MyWebView;
import com.MeiHuaNet.view.TextViewFont;

public abstract class DetailBaseActivity extends BaseActivity {

	protected TextViewFont titleView;
	protected TextViewFont dateView;
	protected TextViewFont countView;
//	protected WebView mWebView;
	protected MyWebView mWebView;
	final String mimeType = "text/html";
	final String encoding = "utf-8";
	protected KnowledgeObject knowledgeObject;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_info_detail);

		initView();
	}

	private void initView() {
		initTitle();

		LinearLayout layout = (LinearLayout) findViewById(R.id.info_detail_layout);
		GestureDetectorUtil.setGestureDetector(this, layout, null);
		
		titleView = (TextViewFont) findViewById(R.id.info_detail_title);
		dateView = (TextViewFont) findViewById(R.id.info_detail_date);
		countView = (TextViewFont) findViewById(R.id.info_detail_views);
		mWebView = (MyWebView) findViewById(R.id.info_detial_webview);
		
		setBodyContent();

	}

	private void initTitle() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.title_back_relative_layout);
		HandleBackTitle handleBackTitle = new HandleBackTitle(relativeLayout);
		handleBackTitle.setTitleView(null, R.drawable.share_btn);
		handleBackTitle.setListener(new OnBackBtnListener(this),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						share();
					}
				});
	}
	
	// 设置webview内容
		@SuppressLint("SetJavaScriptEnabled")
		public void setWebViewContent(String content) {
			mWebView.setVisibility(View.VISIBLE);
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setSupportZoom(false);
			mWebView.getSettings().setBuiltInZoomControls(true);
			mWebView.getSettings().setDefaultTextEncodingName("utf-8");
			mWebView.getSettings().setPluginState(PluginState.ON);
			mWebView.getSettings().setLoadWithOverviewMode(false);
			// 让图片占满一个屏幕，宽度不会超过屏幕宽度
			mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

//			int density = (int) getResources().getDisplayMetrics().density;
//			if (density >= 2) {
//				mWebView.getSettings().setDefaultFontSize(22);
//			} else {
				mWebView.getSettings().setDefaultFontSize(18);
//			}
			// // webview的下载链接的监听，处理下载链接
			// mWebView.setDownloadListener(new DownloadListener() {
			//
			// @Override
			// public void onDownloadStart(String url, String userAgent,
			// String contentDisposition, String mimetype,
			// long contentLength) {
			// String suffixName = url.substring(url.lastIndexOf(".") + 1);
			// if (suffixName.equalsIgnoreCase("jpg")
			// || suffixName.equalsIgnoreCase("jpeg")
			// || suffixName.equalsIgnoreCase("png")
			// || suffixName.equalsIgnoreCase("bmp")) {
			// // 图片的处理
			// Intent intent = new Intent();
			// intent.setClass(mContext, AttachImageActivity.class);
			// intent.putExtra("imageUrl", url);
			// mContext.startActivity(intent);
			// UIManager.cancelAllProgressDialog();
			// } else {
			// // 文档的处理
			// downloadDoc(url, DocDownloadTask.PATH_VIEW,
			// url.substring(url.lastIndexOf("=") + 1), 100);
			// }
			//
			// }
			// });
			// 在webview中加载新连接
			mWebView.setWebViewClient(new WebViewClient() {
				// 处理url。是在此weiview中加载，还是打开新的浏览器
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// int index = url.lastIndexOf(".");
					// if (index != -1) {
					// String type = url.substring(index + 1);
					// if (type.equalsIgnoreCase("jpg")
					// || type.equalsIgnoreCase("jpeg")
					// || type.equalsIgnoreCase("png")
					// || type.equalsIgnoreCase("bmp")
					// || type.equalsIgnoreCase("pdf")
					// || type.equalsIgnoreCase("pdf+")
					// || type.equalsIgnoreCase("doc")
					// || type.equalsIgnoreCase("docx")
					// || type.equalsIgnoreCase("text")
					// || type.equalsIgnoreCase("ppt")
					// || type.equalsIgnoreCase("pptx")
					// || type.equalsIgnoreCase("xls")
					// || type.equalsIgnoreCase("xlsx")
					// || type.equalsIgnoreCase("mp4")
					// || type.equalsIgnoreCase("mpg")
					// || type.equalsIgnoreCase("avi")
					// || type.equalsIgnoreCase("rmvb")) {
					// // 下载的链接就在本页面跳转，这样不会弹出选择使用浏览器的框。实际的下载操作在下载监听中进行
					// UIManager.cancelAllProgressDialog();
					// UIManager.showProgressDialog(Utils
					// .getResString(R.string.loading));
					// view.loadUrl(url);
					// return true;
					// }
					// }
					// Uri uri = Uri.parse(url);
					// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					// mContext.startActivity(intent);
					Intent intent = new Intent();
					intent.setClass(DetailBaseActivity.this, WebviewActivity.class);
					intent.putExtra("url", url);
					startActivity(intent);
					return true;
				}
				//
				// // 加载错误时要做的工作
				// @Override
				// public void onReceivedError(WebView view, int errorCode,
				// String description, String failingUrl) {
				// super.onReceivedError(view, errorCode, description, failingUrl);
				// UIManager.cancelAllProgressDialog();
				// }
				//
				// @Override
				// public void onPageFinished(WebView view, String url) {
				// // 结束
				// super.onPageFinished(view, url);
				// UIManager.cancelAllProgressDialog();
				// }
			});
			if (content != null) {
				try {
					// 设置webview中字体的颜色
					content = "<body style=\"color:#333333 ; line-height: 130%\">"
							+ StringUtils.handleEmbd(content) + "</body>";
					mWebView.loadDataWithBaseURL(null, content,
							mimeType.replaceAll("\\+", " "), encoding, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	protected abstract void setBodyContent();

	protected abstract void requestData();

	protected abstract void share();
}
