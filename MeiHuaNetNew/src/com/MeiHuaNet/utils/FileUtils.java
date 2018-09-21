package com.MeiHuaNet.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

/**
 * 
 * @description 处理文件存储和获取文件中数据的工具类
 * @author lee
 * @createTime 2013-6-26上午9:55:30
 * 
 */
public class FileUtils {

	public static String sdDir = Environment.getExternalStorageDirectory()
			+ "/";
	/*
	 * 应用的默认存储路径目录名
	 */
	public static String dir = "meihuanew";

	/**
	 * 存储数据（数组对象）到文件（每次都是重写文件，有SD卡时优先存储在sd卡，没有时存储在手机内存中）
	 * 
	 * @param listData
	 *            要存储的数据对象的数组
	 * @param fileName
	 *            存储的文件名
	 * @param totalCount
	 *            指定存储数据对象的条数（totalcount大于0时，保存totalcount数值指定条数的数据，
	 *            totalcount等于0时 保存listData中的所有的数据）
	 * 
	 */
	public static void storeDataToFile(Context context,
			ArrayList<? extends Object> listData, String fileName,
			int totalCount) {
		if (listData == null) {
			return;
		}
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 存储卡可用，就存储到存储卡中
			storeDataToSD(listData, fileName, totalCount);
		} else {
			storeDataToAPP(context, listData, fileName, totalCount);
		}
	}

	/**
	 * 存储单个对象到文件中
	 * 
	 * @param context
	 * @param object
	 *            要存储的对象
	 * @param fileName
	 *            存储的文件名
	 */
	public static void storeDataToFile(Context context, Object object,
			String fileName) {
		if (object == null) {
			return;
		}
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 存储卡可用，就存储到存储卡中
			storeDataToSD(object, fileName);
		} else {
			storeDataToAPP(context, object, fileName);
		}
	}

	/**
	 * 存储数据到SD卡
	 * 
	 * @param listData
	 * @param fileName
	 */
	private static void storeDataToSD(ArrayList<? extends Object> listData,
			String fileName, int totalCount) {
		// 文件在 存储卡中保存的路径的名称
		File dirFile = createDir(dir);
		File saveFile = new File(dirFile, fileName);
		try {
			FileOutputStream fOutStream = new FileOutputStream(saveFile);
			ObjectOutputStream objOutStream = new ObjectOutputStream(fOutStream);

			// 对象写入文件的时间
			long time = System.currentTimeMillis();
			objOutStream.writeLong(time);
			int count;
			if (totalCount > 0) {
				if (totalCount <= listData.size()) {
					count = totalCount;
				} else {
					count = listData.size();
				}
			} else {
				count = listData.size();
			}

			// 所写对象的数量
			objOutStream.writeInt(count);
			for (int i = 0; i < count; i++) {
				objOutStream.writeObject(listData.get(i));
			}
			objOutStream.close();
			fOutStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 存储单个对象到SD卡中
	 * 
	 * @param object
	 * @param fileName
	 */
	private static void storeDataToSD(Object object, String fileName) {
		File dirFile = createDir(dir);
		File saveFile = new File(dirFile, fileName);
		try {
			FileOutputStream fOutStream = new FileOutputStream(saveFile);
			ObjectOutputStream objOutStream = new ObjectOutputStream(fOutStream);

			// 对象写入文件的时间
			long time = System.currentTimeMillis();
			objOutStream.writeLong(time);
			objOutStream.writeObject(object);
			objOutStream.close();
			fOutStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 存储数据到手机内部存储器中（应用的data目录下）
	 * 
	 * @param listData
	 * @param fileName
	 */
	private static void storeDataToAPP(Context context,
			ArrayList<? extends Object> listData, String fileName,
			int totalCount) {
		try {
			FileOutputStream fileOutputStream = context.openFileOutput(
					fileName, Context.MODE_PRIVATE);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					fileOutputStream);
			// 对象写入文件的时间
			long time = System.currentTimeMillis();
			objectOutputStream.writeLong(time);
			int count;
			if (totalCount > 0) {
				if (totalCount <= listData.size()) {
					count = totalCount;
				} else {
					count = listData.size();
				}
			} else {
				count = listData.size();
			}
			// 所写对象的数量
			objectOutputStream.writeInt(count);
			for (int i = 0; i < count; i++) {
				objectOutputStream.writeObject(listData.get(i));
			}
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将单个对象写入到手机内存中
	 * 
	 * @param context
	 * @param object
	 * @param fileName
	 */
	private static void storeDataToAPP(Context context, Object object,
			String fileName) {
		try {
			FileOutputStream fileOutputStream = context.openFileOutput(
					fileName, Context.MODE_PRIVATE);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					fileOutputStream);
			// 对象写入文件的时间
			long time = System.currentTimeMillis();
			objectOutputStream.writeLong(time);
			objectOutputStream.writeObject(object);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取存储在文件中的数据（数组对象）
	 * 
	 * @param T
	 *            对象的类型
	 * @param fileName
	 *            文件名
	 * @param outTime
	 *            文件的过期时间，单位为minute（0表示没有过期时间，大于0时表示过期的分钟数）
	 * @return
	 */
	public static <T> ArrayList<T> fetchDataFromFile(Context context,
			Class<? extends Object> T, String fileName, int outTime) {
		ArrayList<T> listData = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd卡可用时，从sd卡中去取数据
			listData = fetchDataFromSD(T, fileName, outTime);
			if (listData == null) {
				// 从sd卡没有取到数据时，再试着从内存中去取数据
				listData = fetchDataFromApp(context, T, fileName, outTime);
			}
		} else {
			listData = fetchDataFromApp(context, T, fileName, outTime);
		}
		return listData;
	}

	/**
	 * 获取存储在文件中的数据（单个对象）
	 * 
	 * @param context
	 * @param T
	 * @param fileName
	 * @return
	 */
	public static <T> T fetchDataFromFile(Context context,
			Class<? extends Object> T, String fileName) {
		T object = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd卡可用时，从sd卡中去取数据
			object = fetchDataFromSD(T, fileName);
			if (object == null) {
				// 从sd卡没有取到数据时，再试着从内存中去取数据
				object = fetchDataFromApp(context, T, fileName);
			}
		} else {
			object = fetchDataFromApp(context, T, fileName);
		}
		return object;
	}

	/**
	 * 从SD卡中去取文件数据(数组对象)
	 * 
	 * @param T
	 * @param fileName
	 * @param outTime
	 * @return
	 */
	private static <T> ArrayList<T> fetchDataFromSD(Class<? extends Object> T,
			String fileName, int outTime) {
		ArrayList<T> listData = null;
		File dirFile = createDir(dir);
		File saveFile = new File(dirFile, fileName);
		if (!saveFile.exists()) {
			// 如果文件不存在，就退出
			return listData;
		}
		FileInputStream finStream;
		ObjectInputStream objInStream;
		try {
			finStream = new FileInputStream(saveFile);
			objInStream = new ObjectInputStream(finStream);
			// 文件中保存的文件最后一次的保存修改的时间
			long lasttime = objInStream.readLong();
			int count = objInStream.readInt();
			if (outTime == 0
					|| System.currentTimeMillis() - lasttime < outTime * 60 * 1000) {
				// 保存的数据没有超过30分钟，就使用，超过30分钟就重写向服务器请求最新的数据
				listData = new ArrayList<T>();
				for (int i = 0; i < count; i++) {
					@SuppressWarnings("unchecked")
					T t = (T) objInStream.readObject();
					listData.add(t);
				}
			}
			objInStream.close();
			finStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return listData;
	}

	/**
	 * 从SD卡中去数据（单个对象）
	 * 
	 * @param T
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T fetchDataFromSD(Class<? extends Object> T,
			String fileName) {
		T object = null;
		File dirFile = createDir(dir);
		File saveFile = new File(dirFile, fileName);
		if (!saveFile.exists()) {
			// 如果文件不存在，就退出
			return object;
		}
		FileInputStream finStream;
		ObjectInputStream objInStream;
		try {
			finStream = new FileInputStream(saveFile);
			objInStream = new ObjectInputStream(finStream);
			// 文件中保存的文件最后一次的保存修改的时间
			long lasttime = objInStream.readLong();
			// if (outTime == 0
			// || System.currentTimeMillis() - lasttime < outTime * 60 * 1000) {
			// // 保存的数据没有超过30分钟，就使用，超过30分钟就重写向服务器请求最新的数据
			// }
			object = (T) objInStream.readObject();
			objInStream.close();
			finStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * 从手机内存中取数据
	 * 
	 * @param context
	 * @param T
	 * @param fileName
	 * @param outTime
	 * @return
	 */
	private static <T> ArrayList<T> fetchDataFromApp(Context context,
			Class<? extends Object> T, String fileName, int outTime) {
		ArrayList<T> listData = null;
		FileInputStream finStream;
		ObjectInputStream objInStream;
		try {
			finStream = context.openFileInput(fileName);
			objInStream = new ObjectInputStream(finStream);
			// 文件中保存的文件最后一次的保存修改的时间
			long lasttime = objInStream.readLong();
			int count = objInStream.readInt();
			if (outTime == 0
					|| System.currentTimeMillis() - lasttime < outTime * 60 * 1000) {
				// 保存的数据没有超过30分钟，就使用，超过30分钟就重写向服务器请求最新的数据
				listData = new ArrayList<T>();
				for (int i = 0; i < count; i++) {
					@SuppressWarnings("unchecked")
					T t = (T) objInStream.readObject();
					listData.add(t);
				}
			}
			objInStream.close();
			finStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return listData;
	}

	/**
	 * 从手机内存中去数据（单个对象）
	 * 
	 * @param context
	 * @param T
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T fetchDataFromApp(Context context,
			Class<? extends Object> T, String fileName) {
		T object = null;
		FileInputStream finStream;
		ObjectInputStream objInStream;
		try {
			finStream = context.openFileInput(fileName);
			objInStream = new ObjectInputStream(finStream);
			// 文件中保存的文件最后一次的保存修改的时间
			long lasttime = objInStream.readLong();
			object = (T) objInStream.readObject();
			objInStream.close();
			finStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * 创建存储卡上的目录
	 * 
	 * @param dirpath
	 *            目录的名称
	 * @return
	 */
	public static File createDir(String dirpath) {
		String sdPath = Environment.getExternalStorageDirectory() + "/";
		File file = new File(sdPath + dirpath);
		if (!file.exists()) {
			// mkdirs()可以创建多级目录，即可以在没有的目录下面创建目录。mkdir()只能在已经存在的目录下面创建文件夹
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容之后才能删除文件夹
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			Utils.log("删除文件夹操作出错");
			e.printStackTrace();

		}
	}

	/**
	 * 删除一个文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 删除指定的文件
	 * 
	 * @param path
	 *            文件的路径
	 * @param fileName
	 *            文件名
	 */
	public static void delfile(String path, String fileName) {
		File file = new File(path, fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 删除应用中保存的所有文件
	 * 
	 * @param context
	 */
	public static void delAppAllfile(Context context) {
		// 先删除手机内存中保存的文件
		delAllFile(context.getFilesDir() + "");
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 如果存储卡可用，就删除存储卡中的内容
			delAllFile(Environment.getExternalStorageDirectory() + "/" + dir);
		}
	}

	/**
	 * 存储图片到文件中
	 * 
	 * @param bitmap
	 *            要存储的图片
	 * @param path
	 *            存储的路径即文件目录名（为空时，默认存储在mice文件夹下）
	 * @param fileName
	 *            存储的图片的文件名
	 * @param isOverride
	 *            如果文件已经存在是否覆盖
	 */
	public static void storeBitmapToFile(Bitmap bitmap, String path,
			String fileName, boolean isOverride) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				&& bitmap != null) {
			File dirFile;
			if (path == null) {
				dirFile = createDir(dir);
			} else {
				dirFile = createDir(path);
			}
			File saveFile = new File(dirFile, fileName);
			if (saveFile.exists()) {
				if (!isOverride) {
					// 如果文件已经存在，且不覆盖已存在的文件，则放弃本次保存操作
					return;
				}
			}
			FileOutputStream fOutStream = null;
			try {
				fOutStream = new FileOutputStream(saveFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				fOutStream.flush();
				fOutStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据路径名和文件名从文件中得到Drawable
	 * 
	 * @param path
	 *            图片存储的文件目录
	 * @param fileName
	 *            图片存储的文件名
	 * @return
	 */
	public static Drawable fetchDrawableFromFile(String path, String fileName) {
		Drawable drawable = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dirFile;
			if (path == null) {
				dirFile = createDir(dir);
			} else {
				dirFile = createDir(path);
			}
			File getFile = new File(dirFile, fileName);
			FileInputStream fInputStream = null;
			try {
				fInputStream = new FileInputStream(getFile);
				drawable = Drawable.createFromStream(fInputStream, "");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return drawable;
	}

	/**
	 * 根据路径名和文件名从文件中得到Drawable
	 * 
	 * @param path
	 *            图片存储的文件目录
	 * @param fileName
	 *            图片存储的文件名
	 * @return
	 */
	public static byte[] fetchBytesFromFile(String path, String fileName) {
		byte[] images = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dirFile;
			if (path == null) {
				dirFile = createDir(dir);
			} else {
				dirFile = createDir(path);
			}
			File getFile = new File(dirFile, fileName);
			FileInputStream fInputStream = null;
			try {
				fInputStream = new FileInputStream(getFile);
				byte[] buff = new byte[1024];
				StringBuffer sb = new StringBuffer();
				while ( fInputStream.read(buff, 0, buff.length) != -1) {
					sb.append(buff);
				}
				images = sb.toString().getBytes();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return images;
	}

	/**
	 * @Methods: getFileSize
	 * @Description: 获取文件夹的大小，包含子文件夹也可以
	 * @param f
	 *            File 实例
	 * @return 文件夹大小，单位：M(兆)
	 * @throws Exception
	 * @throws
	 */
	public static long getFileSize(File f) {
		long size = 0;
		try {
			File flist[] = f.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
			size = size / 1024 / 1024;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}
}
