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
	 * ���ز������ļ�
	 * 
	 * @param fileUrl
	 *            �ļ���·��ַ
	 * @param filePath
	 *            �ļ���sdcard�µ�Ŀ¼
	 * @param fileName
	 *            �ļ���
	 * @return
	 */
	public static int downloadFile(String fileUrl, String filePath,
			String fileName) {
		OutputStream output = null;
		InputStream input = null;
		File file = null;
		try {
			// ��ȡSDcard·��
			String SDCard = Environment.getExternalStorageDirectory() + "";
			// ��ȡ�ļ�ȫ·��
			String pathName = SDCard + File.separator + filePath
					+ File.separator + fileName;
			file = new File(pathName);
			if (file.exists()) {
				return IS_EXSIT;
			} else {
				// Ŀ¼·��
				String dir = SDCard + File.separator + filePath;
				new File(dir).mkdirs();// �½��ļ���
				file.createNewFile();// �½��ļ�
				URL url = new URL(fileUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// ȡ��inputStream�������ж�ȡ
				input = conn.getInputStream();
				output = new FileOutputStream(file);
				// ��ȡ�ļ�
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
	 *            �ļ������ص�ַ
	 * @param Path
	 *            �ļ�������ֻ���Ŀ¼
	 * @param fileName
	 *            �ļ����ʱ���ļ���
	 * @param size
	 *            ������ļ���Ŀ¼�µ��ļ��Ĵ�С�������ֵ������ļ���
	 */
	/**
	 * �����ļ����ֻ�,��������Զ���
	 * 
	 * @param context
	 * @param listener
	 *            ������ɺ����Զ������ص��ļ�֮ǰ���еļ�����������Ϊ�գ�
	 * @param url
	 *            �ļ������ص�ַ
	 * @param Path
	 *            �ļ�������ֻ���Ŀ¼
	 * @param fileName
	 *            �ļ����ʱ���ļ���
	 * @param size
	 *            ������ļ���Ŀ¼�µ��ļ��Ĵ�С�������ֵ������ļ���
	 */
	public static void downloadDoc(Context context,
			DownFinishedListener listener, String url, String Path,
			String fileName, long sizeDel) {
		long size = FileUtils.getFileSize(FileUtils.createDir(Path));
		if (size > sizeDel) {
			// �ļ��д���300Mʱ�����һ���ļ���
			FileUtils.delAllFile(Environment.getExternalStorageDirectory()
					+ File.separator + Path);
		}
		try {
			if (fileName.endsWith("+")) {// ��Щurl�������С�+�����ļ�����Ҫȥ��
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
