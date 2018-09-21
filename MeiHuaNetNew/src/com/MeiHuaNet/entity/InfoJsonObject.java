package com.MeiHuaNet.entity;

import java.io.Serializable;

/**
 *
 * @description 资讯列表页中一条资讯对象的实体类 
 * @author lee
 * @time  2013-10-16 下午4:53:41
 *
 */
public class InfoJsonObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 资讯的id
	 */
	public String PostID;
	
	/**
	 * 资讯的标题
	 */
	public String Title;
	
	/**
	 * 资讯的阅读数量
	 */
	public String Views;
	/**
	 * 资讯的发布日期
	 */
	public String Date;
	
	/**
	 * 图片的url
	 */
	public String ImgURL;
	
	/**
	 * 资讯的简介
	 */
	public String Description;
	
	
	/**
	 * @return the postID
	 */
	public String getPostID() {
		return PostID;
	}


	/**
	 * @param postID the postID to set
	 */
	public void setPostID(String postID) {
		PostID = postID;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return Title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		Title = title;
	}


	/**
	 * @return the views
	 */
	public int getViews() {
		int i = 0;
		try {
			i = Integer.parseInt(Views);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}


	/**
	 * @param views the views to set
	 */
	public void setViews(String views) {
		Views = views;
	}


	/**
	 * @return the date
	 */
	public String getDate() {
		String[] dates = Date.split(" ");
		if(dates.length>=1){
			return dates[0];
		}
		return Date;
	}


	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		Date = date;
	}


	/**
	 * @return the imgURL
	 */
	public String getImgURL() {
		return ImgURL;
	}


	/**
	 * @param imgURL the imgURL to set
	 */
	public void setImgURL(String imgURL) {
		ImgURL = imgURL;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InfoJsonObject [PostID=" + PostID + ", Title=" + Title
				+ ", Views=" + Views + ", Date=" + Date + ", ImgURL=" + ImgURL
				+ ", Description=" + Description + "]";
	}
	
}
