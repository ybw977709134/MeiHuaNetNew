package com.MeiHuaNet.entity;

import java.io.Serializable;

/**
 *
 * @description 请求今日免费视频接口时返回的数据的实体类对象 
 * @author lee
 * @time  2013-12-16 下午3:27:39
 *
 */
public class OriginalityObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String ID;
	public String Title;
	/**
	 * 创意视频的图片的url
	 */
	public String ImgURL;
	public String BrandName;
	public String CampaignInfo;
	/**
	 * 创意视频的url地址
	 */
	public String url;

}
