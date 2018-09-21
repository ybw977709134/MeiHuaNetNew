package com.MeiHuaNet.entity;

import java.io.Serializable;

/**
 *
 * @description 媒体类型列表的实体类 
 * @author lee
 * @time  2013-12-11 下午3:08:34
 *
 */
public class MediaCategoryObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 媒体类型对应的ID
	 */
	public String ID;
	/**
	 * 媒体类型的名称
	 */
	public String Name;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MediaCategoryObject [ID=" + ID + ", Name=" + Name + "]";
	}
	

}
