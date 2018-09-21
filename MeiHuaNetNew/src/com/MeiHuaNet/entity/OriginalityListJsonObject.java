package com.MeiHuaNet.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @description 创意列表返回的json数据解析对应的实体类 
 * @author lee
 * @time  2013-12-16 上午11:01:36
 *
 */
public class OriginalityListJsonObject {

	public String ResultCount;
	
	public ArrayList<OriginalityListObject> Creatives;
	
	
	/**
	 * @return the resultCount
	 */
	public int getResultCount() {
		int number = 0;
		try{
			number = Integer.parseInt(ResultCount);
		} catch(Exception e){
			e.printStackTrace();
		}
		return number;
	}


	/**
	 * @param resultCount the resultCount to set
	 */
	public void setResultCount(String resultCount) {
		ResultCount = resultCount;
	}


	/**
	 * @return the creatives
	 */
	public ArrayList<OriginalityListObject> getCreatives() {
		return Creatives;
	}


	/**
	 * @param creatives the creatives to set
	 */
	public void setCreatives(ArrayList<OriginalityListObject> creatives) {
		Creatives = creatives;
	}


	public static class OriginalityListObject implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public String ID;
		public String Title;
		public String ImgURL;
		/**
		 * 品牌名称
		 */
		public String BrandName;
		/**
		 * 国家或地区
		 */
		public String Country;
		/**
		 * 广告年份
		 */
		public String Year;
		/**
		 * 语种
		 */
		public String Language;
		/**
		 * 是否是免费视频
		 */
		public String IsFree;
		public String Date;
		/**
		 * 视频观看的url地址
		 */
		public String url;
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "OriginalityListObject [ID=" + ID + ", Title=" + Title
					+ ", ImgURL=" + ImgURL + ", BrandName=" + BrandName
					+ ", Country=" + Country + ", Year=" + Year + ", Language="
					+ Language + ", IsFree=" + IsFree + ", Date=" + Date
					+ ", url=" + url + "]";
		}
		
	}
}
