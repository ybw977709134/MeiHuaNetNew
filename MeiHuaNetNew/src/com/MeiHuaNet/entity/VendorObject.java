package com.MeiHuaNet.entity;

import java.io.Serializable;

/**
 *
 * @description 营销服务商的实体类 
 * @author lee
 * @time  2013-10-31 下午5:52:54
 *
 */
public class VendorObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 服务商id
	 */
	public String ID;
	/**
	 * 服务商姓名
	 */
	public String VendorName;
	/**
	 * 服务商公司雇员人数
	 */
	public String EmployeeNumber;
	/**
	 * 服务商公司注册年份
	 */
	public String OpenYear;
	/**
	 * 服务商公司logo图片的url
	 */
	public String LogoFile;
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
	 * @return the vendorName
	 */
	public String getVendorName() {
		return VendorName;
	}
	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		VendorName = vendorName;
	}
	/**
	 * @return the employeeNumber
	 */
	public String getEmployeeNumber() {
		return EmployeeNumber;
	}
	/**
	 * @param employeeNumber the employeeNumber to set
	 */
	public void setEmployeeNumber(String employeeNumber) {
		EmployeeNumber = employeeNumber;
	}
	/**
	 * @return the openYear
	 */
	public String getOpenYear() {
		return OpenYear;
	}
	/**
	 * @param openYear the openYear to set
	 */
	public void setOpenYear(String openYear) {
		OpenYear = openYear;
	}
	/**
	 * @return the logoFile
	 */
	public String getLogoFile() {
		return LogoFile;
	}
	/**
	 * @param logoFile the logoFile to set
	 */
	public void setLogoFile(String logoFile) {
		LogoFile = logoFile;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VendorObject [ID=" + ID + ", VendorName=" + VendorName
				+ ", EmployeeNumber=" + EmployeeNumber + ", OpenYear="
				+ OpenYear + ", LogoFile=" + LogoFile + "]";
	}
	
}
