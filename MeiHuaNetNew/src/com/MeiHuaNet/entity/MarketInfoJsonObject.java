package com.MeiHuaNet.entity;

import java.util.ArrayList;

/**
 *
 * @description 营销列表的实体类 
 * @author lee
 * @time  2013-10-31 下午2:09:41
 *
 */
public class MarketInfoJsonObject {

	/**
	 * 营销百科分类的总条数
	 */
	public String ResultCount;
	
	/**
	 * 营销列表
	 */
	public ArrayList<KnowledgeObject> Wikis;

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
	 * @return the wikis
	 */
	public ArrayList<KnowledgeObject> getWikis() {
		return Wikis;
	}

	/**
	 * @param wikis the wikis to set
	 */
	public void setWikis(ArrayList<KnowledgeObject> wikis) {
		Wikis = wikis;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MarketInfoJsonObject [ResultCount=" + ResultCount + ", Wikis="
				+ Wikis + "]";
	}
	
}
