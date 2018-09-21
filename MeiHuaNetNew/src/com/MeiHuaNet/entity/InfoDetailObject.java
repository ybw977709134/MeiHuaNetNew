package com.MeiHuaNet.entity;

/**
 * 
 * @description 资讯(知识，案例，营销百科)详情页返回的json对象对应的实体类
 * @author lee
 * @time 2013-10-28 上午11:04:33
 * 
 */
public class InfoDetailObject {

	public String PostID;
	public String ID;
	public String Title;
	/**
	 * 新闻被阅读的次数
	 */
	public String Views;
	public String ImgURL;
	/**
	 * 新闻的摘要
	 */
	public String Description;
	public String Date;
	/**
	 * 新闻所在的网址
	 */
	public String Url;
	/**
	 * 新闻的详细内容
	 */
	public String Content;
}
