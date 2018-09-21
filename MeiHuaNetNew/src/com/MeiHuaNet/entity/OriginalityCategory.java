package com.MeiHuaNet.entity;

import java.io.Serializable;

/**
 *
 * @description 创意分类的实体类 
 * @author lee
 * @time  2013-12-13 下午3:35:40
 *
 */
public class OriginalityCategory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 创意分类的名称
	 */
	public String Description;
	/**
	 * 创意分类的数量
	 */
	public String Count;
	/**
	 * 创意分类的id
	 */
	public String ID;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OriginalityCategory [Description=" + Description + ", Count="
				+ Count + ", ID=" + ID + "]";
	}
	
	
}
