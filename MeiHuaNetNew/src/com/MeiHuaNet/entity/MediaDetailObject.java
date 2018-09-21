package com.MeiHuaNet.entity;

import java.util.ArrayList;


public class MediaDetailObject {

	public String ID;
	public String 名称;
	public String 英文名称;
	public String Logo;
	public String 媒体简介;
	public ContectInfo Contect;
	public MediaInfo BaseMediamInfo;
	/**
	 * 刊例文档
	 */
	public ArrayList<RateCard> RateCards;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MediaDetailObject [ID=" + ID + ", 名称=" + 名称 + ", 英文名称=" + 英文名称
				+ ", Logo=" + Logo + ", 媒体简介=" + 媒体简介 + ", Contect=" + Contect
				+ ", BaseMediamInfo=" + BaseMediamInfo + ", RateCards="
				+ RateCards + "]";
	}

	public static class ContectInfo {
		public String 联系人;
		public String 电话;
		public String 传真;
		public String Email;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ContectInfo [联系人=" + 联系人 + ", 电话=" + 电话 + ", 传真=" + 传真
					+ ", Email=" + Email + "]";
		}
	}

	public static class MediaInfo {
		public String 媒体类型;
		public String 基础刊例价;
		public String 报纸类型;
		public String 创报时间;
		public String 报纸发行日;
		public String 开本;
		public String 整版净尺寸;
		public String 性质;
		public String 发行量;
		public String 出版城市;
		public String 平均版数;
		public String 零售单价;
		public String 发行周期;
		public String 广告截止时间;

		public ArrayList<String> getListStr() {
			ArrayList<String> resutlList = new ArrayList<String>();
			if (!isEmpty(媒体类型)) {
				resutlList.add("媒体类型：" + 媒体类型);
			}
			if (!isEmpty(基础刊例价)) {
				resutlList.add("基础刊例价：" + 基础刊例价);
			}
			if (!isEmpty(报纸类型)) {
				resutlList.add("报纸类型：" + 报纸类型);
			}
			if (!isEmpty(创报时间)) {
				resutlList.add("创报时间：" + 创报时间);
			}
			if (!isEmpty(报纸发行日)) {
				resutlList.add("报纸发行日：" + 报纸发行日);
			}
			if (!isEmpty(开本)) {
				resutlList.add("开本：" + 开本);
			}
			if (!isEmpty(整版净尺寸)) {
				resutlList.add("整版净尺寸：" + 整版净尺寸);
			}
			if (!isEmpty(性质)) {
				resutlList.add("性质：" + 性质);
			}

			if (!isEmpty(发行量)) {
				resutlList.add("发行量：" + 发行量);
			}
			if (!isEmpty(出版城市)) {
				resutlList.add("出版城市：" + 出版城市);
			}
			if (!isEmpty(平均版数)) {
				resutlList.add("平均版数：" + 平均版数);
			}
			if (!isEmpty(零售单价)) {
				resutlList.add("零售单价：" + 零售单价);
			}
			if (!isEmpty(发行周期)) {
				resutlList.add("发行周期：" + 发行周期);
			}
			if (!isEmpty(广告截止时间)) {
				resutlList.add("广告截止时间：" + 广告截止时间);
			}
			return resutlList;
		}
		
		private boolean isEmpty(String s){
			if(s == null || s.length() == 0|| s.contains("N/A")){
				return true;
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "MediaInfo [媒体类型=" + 媒体类型 + ", 基础刊例价=" + 基础刊例价 + ", 报纸类型="
					+ 报纸类型 + ", 创报时间=" + 创报时间 + ", 报纸发行日=" + 报纸发行日 + ", 开本="
					+ 开本 + ", 整版净尺寸=" + 整版净尺寸 + ", 性质=" + 性质 + ", 发行量=" + 发行量
					+ ", 出版城市=" + 出版城市 + ", 平均版数=" + 平均版数 + ", 零售单价=" + 零售单价
					+ ", 发行周期=" + 发行周期 + ", 广告截止时间=" + 广告截止时间 + "]";
		}
	}

	public static class RateCard {
		public String 刊例名称;
		public String 刊例ID;
		public String 刊例大小;
		public String 时间;
		public String 扩展名;
		public String Url;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "RateCard [刊例名称=" + 刊例名称 + ", 刊例ID=" + 刊例ID + ", 刊例大小="
					+ 刊例大小 + ", 时间=" + 时间 + ", 扩展名=" + 扩展名 + ", Url=" + Url
					+ "]";
		}
	}
}
