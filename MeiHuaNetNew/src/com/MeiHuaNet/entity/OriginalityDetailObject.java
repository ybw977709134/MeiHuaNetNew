package com.MeiHuaNet.entity;

public class OriginalityDetailObject {

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
	public String Price;
	public String CreativeTypeName;
	public String CampaignInfo;
	public String GraphicStoryFile;
	public String url;
	public String Date;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OriginalityDetailObject [ID=" + ID + ", Title=" + Title
				+ ", ImgURL=" + ImgURL + ", BrandName=" + BrandName
				+ ", Country=" + Country + ", Year=" + Year + ", Language="
				+ Language + ", Price=" + Price + ", CreativeTypeName="
				+ CreativeTypeName + ", CampaignInfo=" + CampaignInfo
				+ ", GraphicStoryFile=" + GraphicStoryFile + ", url=" + url
				+ ", Date=" + Date + "]";
	}

}
