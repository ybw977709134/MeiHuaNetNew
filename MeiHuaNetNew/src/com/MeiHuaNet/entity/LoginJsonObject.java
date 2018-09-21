package com.MeiHuaNet.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @description 登录成功后返回的数据对应的实体类
 * @author lee
 * @time  2013-11-26 下午4:30:54
 *
 */
public class LoginJsonObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 登录后的返回消息（"登录成功","用户名不存在","密码不正确"）
	 */
	public String Message;
	/**
	 * 登录用户的名称（注册时填写的真实姓名）
	 */
	public String FullName;
	public String Email;
	public String Company;
	/**
	 * 在公司的职位
	 */
	public String JobTitle;
	/**
	 * 固定电话的号码
	 */
	public String Phone;
	/**
	 * 手机号
	 */
	public String MobilePhone;
	public String PostCode;
	public String Address;
	public String Gender;//性别
	public String CompanyType;
	public String Industry;
	public String Province;
	public String City;
	public String UserName;
	/**
	 * 父用户名
	 */
	public String ParentName;
	/**
	 * 是否是vip用户
	 */
	public String IsVIP;
	/**
	 * 用户的权限列表
	 */
	public ArrayList<LicenseObject> UserLicense; 
	
	public static class LicenseObject implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * 权限描述
		 */
		public String Description;
		/**
		 * 是否有权限下载或观看视频
		 */
		public String IsView;
		
		@Override
		public String toString() {
			return "LicenseObject [Description=" + Description + ", IsView="
					+ IsView + "]";
		}
		
	}

	@Override
	public String toString() {
		return "LoginJsonObject [Message=" + Message + ", FullName=" + FullName
				+ ", Email=" + Email + ", Company=" + Company + ", JobTitle="
				+ JobTitle + ", Phone=" + Phone + ", MobilePhone="
				+ MobilePhone + ", PostCode=" + PostCode + ", Address="
				+ Address + ", Gender=" + Gender + ", CompanyType="
				+ CompanyType + ", Industry=" + Industry + ", Province="
				+ Province + ", City=" + City + ", UserName=" + UserName
				+ ", ParentName=" + ParentName + ", IsVIP=" + IsVIP
				+ ", UserLicense=" + UserLicense + "]";
	}
	
}
