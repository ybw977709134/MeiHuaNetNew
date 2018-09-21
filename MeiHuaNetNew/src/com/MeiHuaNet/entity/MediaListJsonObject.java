package com.MeiHuaNet.entity;

import java.util.ArrayList;

public class MediaListJsonObject {

	public String ResultCount;
	
	public ArrayList<MediainfoObject> List;
	

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
	 * @return the list
	 */
	public ArrayList<MediainfoObject> getList() {
		return List;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(ArrayList<MediainfoObject> list) {
		List = list;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MediaListJsonObject [ResultCount=" + ResultCount + ", List="
				+ List + "]";
	}
	
}
