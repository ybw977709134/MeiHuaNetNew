package com.MeiHuaNet.entity;

import java.io.Serializable;

/**
 *
 * @description 有效活动，往期活动，已报名活动都是这个实体类（实际上他们有些字段是不同的） 
 * @author lee
 * @time  2013-11-5 下午5:32:15
 *
 */
public class EventObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 有效活动和往期活动中一个活动的id
	 */
	public String ID;
	/**
	 * 已报名活动中活动的id
	 */
	public String EventID;
	/**
	 * 活动名称
	 */
	public String Name;
	/**
	 * 活动举办的城市
	 */
	public String City;
	/**
	 * 活动举办场所
	 */
	public String Venue;
	/**
	 * 活动所属的分类
	 */
	public String Category;
	/**
	 * 活动价格
	 */
	public String PriceText;
	/**
	 * 活动简介
	 */
	public String Intro;
	/**
	 * 是否可报名（有些活动过期了就不能报名了）
	 */
	public String IsJoin;
	/**
	 * 活动的开始日期
	 */
	public String StartDate;
	/**
	 * 活动的结束日期
	 */
	public String EndDate;
	/**
	 * 还有多少天活动开始
	 */
	public String CountDown;
	/**
	 * 活动报名成功后的编号
	 */
	public String RegistrationCode;
	/**
	 * 报名成功后的id
	 */
	public String RegistrationID;
	/**
	 * 报名成功后的活动状态（是否确认座位等）
	 */
	public String EventStatus;
}
