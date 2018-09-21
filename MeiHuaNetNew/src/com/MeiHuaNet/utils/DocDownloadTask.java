package com.MeiHuaNet.utils;

import com.MeiHuaNet.R;
import com.MeiHuaNet.UIManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.webkit.MimeTypeMap;

public class DocDownloadTask extends AsyncTask<String, Void, Integer>{
	private Context context;
	private DownFinishedListener listener;

	//正文下面的附件的存储路径
	public static String PATH = "meihuanewCache/doc";
	
	//webview中件的存放路径
	public static String PATH_VIEW = "meihuanewCache/webview";

	private String filePath;
	private String fileName;

	private String SDCARD = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	public static interface DownFinishedListener{
		void finish();
	}
	
	public DocDownloadTask(Context context){
		this.context = context;
	}
	
	public DocDownloadTask(Context context, DownFinishedListener listener){
		this.context = context;
		this.listener = listener ;
	}

	@Override
	protected Integer doInBackground(String... params) {
		//下载文档的url
		String fileUrl = params[0];
		//下载的文档保存在文件的目录
		filePath = params[1];
		//下载的文档保存的文件名
		fileName= params[2];
		
		int result = DownloadUtil.downloadFile(fileUrl, filePath, fileName);
		return result;
	}
	
	@Override
	protected void onPostExecute(Integer result){
		UIManager.cancelAllProgressDialog();
		if(result != -1){
			if(result == 0 && listener != null){
				listener.finish();
			}
			forword();
		} else {
			Utils.Toast(Utils.getResString(R.string.fail_download_file));
		}
		super.onPostExecute(result);
	}

	/**
	 * 页面跳转,根据扩展名读取对应文档
	 */
	private void forword() {
		// 扩展名
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String file = "file://" + SDCARD + "/" + filePath + "/" + fileName;
		// 扩展名对应文件类型map
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		suffix = suffix.toLowerCase();
		if (mimeTypeMap.hasExtension(suffix)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri data = Uri.parse(file);
			intent.setDataAndType(data,
					mimeTypeMap.getMimeTypeFromExtension(suffix));
			try {
				context.startActivity(intent);
			} catch (Exception e) {
				showCannotOpen(suffix);
				return;
			}
		} else {
			showCannotOpen(suffix);
			return;
		}
	}

	// 文件不能打开的弹出框
	private void showCannotOpen(String suffix) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					context);
			builder.setMessage(suffix + Utils.getResString(R.string.file_couldnot_open));
			builder.setPositiveButton(Utils.getResString(R.string.sure),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
			builder.show();
		} catch (Exception e) {
			Utils.Toast(suffix + Utils.getResString(R.string.file_couldnot_open));
		}
	}
}
