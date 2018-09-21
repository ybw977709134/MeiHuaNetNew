package com.MeiHuaNet.entity;

import java.util.ArrayList;

/**
 *
 * @description 案例分类列表的实体类 
 * @author lee
 * @time  2013-10-30 下午5:31:28
 *
 */
public class CaseListJsonObject {

	/**
	 * 案例分类总的总条数
	 */
	public String ResultCount;
	
	/**
	 * 知识列表
	 */
	public ArrayList<KnowledgeObject> Cases;

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
	 * @return the cases
	 */
	public ArrayList<KnowledgeObject> getCases() {
		return Cases;
	}

	/**
	 * @param cases the cases to set
	 */
	public void setCases(ArrayList<KnowledgeObject> cases) {
		Cases = cases;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CaseListJsonObject [ResultCount=" + ResultCount + ", Cases="
				+ Cases + "]";
	}
	
}
