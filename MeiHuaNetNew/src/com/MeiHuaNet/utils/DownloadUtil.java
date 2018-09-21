package com.MeiHuaNet.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.utils.DocDownloadTask.DownFinishedListener;

import android.content.Context;
import android.os.Environment;

public class DownloadUtil {

	public static final int IS_EXSIT = 1;

	public static final int DOWNLOAD_SUCCESS = 0;

	public static final int DOWNLOAD_FAILURE = -1;

	/**
	 * 下载并保存文件
	 * 
	 * @param fileUrl
	 *            文件网路地址
	 * @param filePath
	 *            文件在sdcard下的目录
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static int downloadFile(String fileUrl, String filePath,
			String fileName) {
		OutputStream output = null;
		InputStream input = null;
		File file = null;
		try {
			// 获取SDcard路径
			String SDCard = Environment.getExternalStorageDirectory() + "";
			// 获取文件全路径
			String pathName = SDCard + File.separator + filePath
					+ File.separator + fileName;
			file = new File(pathName);
			if (file.exists()) {
				return IS_EXSIT;
			} else {
				// 目录路径
				String dir = SDCard + File.separator + filePath;
				new File(dir).mkdirs();// 新建文件夹
				file.createNewFile();// 新建文件
				URL url = new URL(fileUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// 取得inputStream，并进行读取
				input = conn.getInputStream();
				output = new FileOutputStream(file);
				// 读取文件
				byte[] buffer = DownloadUtil.read(input, -1);
				output.write(buffer);
				output.flush();
				return DOWNLOAD_SUCCESS;
			}

		} catch (MalformedURLException e) {
			UIManager.cancelAllProgressDialog();
			if (file != null) {
				file.delete();
			}
			return DOWNLOAD_FAILURE;
		} catch (IOException e) {
			UIManager.cancelAllProgressDialog();
			if (file != null) {
				file.delete();
			}
			return DOWNLOAD_FAILURE;
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			UIManager.cancelAllProgressDialog();
		}
	}

	public static byte[] read(InputStream in, int length) throws IOException {
		if (length == 0)
			return new byte[0];
		byte[] buf;
		int read = 0;
		if (length < 0) {
			ByteArrayDataOutputStream dos = new ByteArrayDataOutputStream(2048);
			buf = new byte[2048];
			while ((read = in.read(buf)) > 0) {
				dos.write(buf, 0, read);
			}
			buf = dos.toByteArray();
			try {
				dos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			dos = null;
		} else {
			buf = new byte[length];
			while (read < length)
				read += in.read(buf, read, length - read);
		}
		return buf;
	}

	/**
	 * 
	 * 
	 * @param url
	 *            文件的下载地址
	 * @param Path
	 *            文件存放在手机的目录
	 * @param fileName
	 *            文件存放时的文件名
	 * @param size
	 *            当存放文件的目录下的文件的大小超过这个值就清空文件夹
	 */
	/**
	 * 下载文件到手机,下载完后自动打开
	 * 
	 * @param context
	 * @param listener
	 *            下载完成后在自动打开下载的文件之前进行的监听操作（可为空）
	 * @param url
	 *            文件的下载地址
	 * @param Path
	 *            文件存放在手机的目录
	 * @param fileName
	 *            文件存放时的文件名
	 * @param size
	 *            当存放文件的目录下的文件的大小超过这个值就清空文件夹
	 */
	public static void downloadDoc(Context context,
			DownFinishedListener listener, String url, String Path,
			String fileName, long sizeDel) {
		long size = FileUtils.getFileSize(FileUtils.createDir(Path));
		if (size > sizeDel) {
			// 文件夹大于300M时，清空一次文件夹
			FileUtils.delAllFile(Environment.getExternalStorageDirectory()
					+ File.separator + Path);
		}
		try {
			if (fileName.endsWith("+")) {// 有些url的最后会有“+”，文件名中要去掉
				fileName = fileName.substring(0, fileName.length() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (listener == null) {
			new DocDownloadTask(context).execute(url, Path, fileName);
		} else {
			new DocDownloadTask(context, listener).execute(url, Path, fileName);
		}
	}
}
