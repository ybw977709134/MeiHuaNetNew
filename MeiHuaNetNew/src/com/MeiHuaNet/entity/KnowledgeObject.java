package com.MeiHuaNet.entity;

import java.io.Serializable;

/**
 * 
 * @description 知识，案例,营销百科都是用的这个实体类
 * @author lee
 * @time 2013-10-30 下午5:29:11
 * 
 */
public class KnowledgeObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 知识的id
	 */
	public String ID;

	/**
	 * 知识的标题
	 */
	public String Title;

	/**
	 * 知识的阅读数量
	 */
	public String Views;
	/**
	 * 知识的发布日期
	 */
	public String Date;

	/**
	 * 图片的url
	 */
	public String ImgURL;

	/**
	 * 知识的简介
	 */
	public String Description;

	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}

	/**
	 * @param iD
	 *            the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return Title;
	}

	/**
	 * @param title
	 *            the title to set
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
			i = Integer.valueOf(Views);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * @param views
	 *            the views to set
	 */
	public void setViews(String views) {
		Views = views;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		String[] dates = Date.split(" ");
		if (dates.length >= 1) {
			return dates[0];
		}
		return Date;
	}

	/**
	 * @param date
	 *            the date to set
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
	 * @param imgURL
	 *            the imgURL to set
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
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KnowledgeObject [ID=" + ID + ", Title=" + Title + ", Views="
				+ Views + ", Date=" + Date + ", ImgURL=" + ImgURL
				+ ", Description=" + Description + "]";
	}

}
