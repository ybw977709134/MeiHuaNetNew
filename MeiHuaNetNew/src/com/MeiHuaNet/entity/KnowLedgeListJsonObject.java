package com.MeiHuaNet.entity;

import java.util.ArrayList;

public class KnowLedgeListJsonObject{

	/**
	 * 知识分类总的总条数
	 */
	public String ResultCount;
	
	/**
	 * 知识列表
	 */
	public ArrayList<KnowledgeObject> Articles;

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
	 * @return the articles
	 */
	public ArrayList<KnowledgeObject> getArticles() {
		return Articles;
	}

	/**
	 * @param articles the articles to set
	 */
	public void setArticles(ArrayList<KnowledgeObject> articles) {
		Articles = articles;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KnowLedgeListJsonObject [ResultCount=" + ResultCount
				+ ", Articles=" + Articles + "]";
	}
	
}
