package com.MeiHuaNet.entity;

import java.io.Serializable;

/**
 * 
 * @description 一个创意对象的实体类
 * @author lee
 * @time 2013-10-30 上午10:19:51
 * 
 */
public class RecruitObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String ID;
	/**
	 * 职位名称
	 */
	public String JobName;
	/**
	 * 公司的logo图片的url
	 */
	public String LogoUrl;
	/**
	 * 雇佣公司的名称
	 */
	public String EmployerName;
	/**
	 * 公司所在地
	 */
	public String City;
	public String Date;
	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}
	/**
	 * @return the jobName
	 */
	public String getJobName() {
		return JobName;
	}
	/**
	 * @param jobName the jobName to set
	 */
	public void setJobName(String jobName) {
		JobName = jobName;
	}
	/**
	 * @return the logoUrl
	 */
	public String getLogoUrl() {
		return LogoUrl;
	}
	/**
	 * @param logoUrl the logoUrl to set
	 */
	public void setLogoUrl(String logoUrl) {
		LogoUrl = logoUrl;
	}
	/**
	 * @return the employerName
	 */
	public String getEmployerName() {
		return EmployerName;
	}
	/**
	 * @param employerName the employerName to set
	 */
	public void setEmployerName(String employerName) {
		EmployerName = employerName;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return City;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		City = city;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return Date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		Date = date;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RecruitObject [ID=" + ID + ", JobName=" + JobName
				+ ", LogoUrl=" + LogoUrl + ", EmployerName=" + EmployerName
				+ ", City=" + City + ", Date=" + Date + "]";
	}

}
