package com.MeiHuaNet.entity;

import java.util.ArrayList;

/**
 *
 * @description 营销服务商的列表实体类 
 * @author lee
 * @time  2013-10-31 下午5:50:32
 *
 */
public class VendorListJsonObject {

	/**
	 * 营销服务商列表中数据的总条数
	 */
	public String ResultCount;
	
	/**
	 * 营销服务商列表数据
	 */
	public ArrayList<VendorObject> Vendors;

	/**
	 * @return the resultCount
	 */
	public int getResultCount() {
		int count = 0;
		try {
			count = Integer.valueOf(ResultCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * @param resultCount the resultCount to set
	 */
	public void setResultCount(String resultCount) {
		ResultCount = resultCount;
	}

	/**
	 * @return the vendors
	 */
	public ArrayList<VendorObject> getVendors() {
		return Vendors;
	}

	/**
	 * @param vendors the vendors to set
	 */
	public void setVendors(ArrayList<VendorObject> vendors) {
		Vendors = vendors;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VendorListJsonObject [ResultCount=" + ResultCount
				+ ", Vendors=" + Vendors + "]";
	}
	
}
