package com.MeiHuaNet.entity;

/**
 *
 * @description 营销商详情 
 * @author lee
 * @time  2013-11-1 下午4:23:09
 *
 */
public class VendorDetailObject {

	public String ID;
	/**
	 * 公司中文名
	 */
	public String VendorName;
	/**
	 * 公司英文名
	 */
	public String VendorTitleEn;
	/**
	 * 公司雇员的总人数
	 */
	public String EmployeeNumber;
	/**
	 * 公司开设的年份
	 */
	public String OpenYear;
	/**
	 * 公司的简介
	 */
	public String ProfileIntro;
	/**
	 * 公司的logo图片的 url
	 */
	public String LogoFile;
	/**
	 * 公司的联系方式的对象
	 */
	public ContactObject Contacts;
	
	/**
	 *
	 * @description  公司联系方式的实体类
	 * @author lee
	 * @time  2013-11-1 下午4:29:36
	 *
	 */
	public static class ContactObject{
		public String StreetAddress;
		public String PostCode;
		public String Phone;
		public String Fax;
		public String Email;
		public String Mobile;
	}
	
}
