package com.MeiHuaNet.entity;

import java.util.ArrayList;

import com.MeiHuaNet.utils.StringUtils;

/**
 *
 * @description 创意列表页的json实体类 
 * @author lee
 * @time  2013-10-30 上午10:18:14
 *
 */
public class RecruitListJsonObject {
	
	public String ResultCount;
	
	public ArrayList<RecruitObject> Jobs;

	/**
	 * @return the resultCount
	 */
	public int getResultCount() {
		return StringUtils.stringToInteger(ResultCount);
	}

	/**
	 * @param resultCount the resultCount to set
	 */
	public void setResultCount(String resultCount) {
		ResultCount = resultCount;
	}

	/**
	 * @return the jobs
	 */
	public ArrayList<RecruitObject> getJobs() {
		return Jobs;
	}

	/**
	 * @param jobs the jobs to set
	 */
	public void setJobs(ArrayList<RecruitObject> jobs) {
		Jobs = jobs;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RecruitListJsonObject [ResultCount=" + ResultCount + ", Jobs="
				+ Jobs + "]";
	}
	

}
