package com.MeiHuaNet.entity;

import java.util.ArrayList;

/**
 *
 * @description 服务器返回的资讯列表的实体类
 * @author lee
 * @time  2013-10-17 上午9:50:50
 *
 */
public class InfoListJsonObject {

	/**
	 * 资讯的总条数
	 */
	public int ResultCount; 
	
	/**
	 * 存放资讯对象的数组列表
	 */
	public ArrayList<InfoJsonObject> Posts;
}
