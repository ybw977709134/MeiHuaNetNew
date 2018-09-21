package com.MeiHuaNet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *
 * @description 保存少量数据，一般是几个字段（大量数据用文件存储系统去处理）
 * @author lee
 * @createTime 2013-6-26上午11:11:40
 *
 */
public class SharedPreferenceUtils {

	/**
	 * 当前登录的用户名
	 */
	public static final String USER_NAME = "username";
	
	/**
	 * 当前登录用户名对应的密码
	 */
	public static final String PASSWORD = "password";
	
	/**
	 * 用户最后一次登录的时间
	 */
	public static final String LASTTIME = "lasttime";
	
	/**
	 * 当前登录的用户是否是Vip用户
	 */
	public static final String IS_VIP = "isvip";
	
	/**
	 * 保存用户登录信息的文件的文件名
	 */
	public static final String FILE_USER_INFO = "userInfo";
	public static final String FILE_FONT_SIZE = "fontSize"; 
	
	
	private static SharedPreferences spf;
	private static Editor editor;
	private static SharedPreferenceUtils sharedPreferenceUtils_USER;
	private static SharedPreferenceUtils sharedPreferenceUtils_Font;
	
	
	private SharedPreferenceUtils(Context context, String name){
		spf = context.getSharedPreferences(name, 0);
		editor = spf.edit();
	
	}
	
	public static SharedPreferenceUtils getInstance(Context context, String name){
		SharedPreferenceUtils sharedPreferenceUtils = null ;
		if(FILE_USER_INFO.equals(name)){
			if(sharedPreferenceUtils_USER == null){
				sharedPreferenceUtils_USER = new SharedPreferenceUtils(context,name);
			}
			sharedPreferenceUtils = sharedPreferenceUtils_USER;
		} else if(FILE_FONT_SIZE.equals(name)){
			if(sharedPreferenceUtils_Font == null){
				sharedPreferenceUtils_Font = new SharedPreferenceUtils(context, name);
			}
			sharedPreferenceUtils = sharedPreferenceUtils_Font;
		}
		return sharedPreferenceUtils;
	}
	
	public String getString(String key){
		return spf.getString(key, "");
	}
	
	public int getInt(String key){
		return spf.getInt(key, 1);
	}
	
	public boolean getBoolean(String key){
		return spf.getBoolean(key, false);
	}
	public long getLong(String key){
		return spf.getLong(key, 0);
	}
	
	public void put(String key, String value){
		editor.putString(key, value).commit();
	}
	public void put(String key, boolean value){
		editor.putBoolean(key, value).commit();
	}
	public void put(String key, int value){
		editor.putInt(key, value).commit();
	}
	public void put(String key, long value){
		editor.putLong(key, value).commit();
	}
	
	public void remove(String key){
		editor.remove(key).commit();
	}
	
	public void clear(){
		editor.clear().commit();
	}
}
