package com.MeiHuaNet.entity;

import java.io.Serializable;

public class MediainfoObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String ID;
	public String Name;
	public String EnName;
	public String IntroText;
	public String Logo;
	public String Category;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MediainfoObject [ID=" + ID + ", Name=" + Name + ", EnName="
				+ EnName + ", IntroText=" + IntroText + ", Logo=" + Logo
				+ ", Category=" + Category + "]";
	}
	
	

}
