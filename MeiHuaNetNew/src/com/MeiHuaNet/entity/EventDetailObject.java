package com.MeiHuaNet.entity;

import java.io.Serializable;

public class EventDetailObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String ID;
	public String Name;
	public String City;
	public String Venue;
	public String Category;
	public String PriceText;
	public String Url;
	public String Description;
	/**
	 * 活动简介
	 */
	public String Intro;
	public String IsJoin;
	public String StartDate;
	public String EndDate;
	/**
	 * 应该来的观众
	 */
	public String Audience;
	/**
	 * 议程与安排
	 */
	public String Agenda;
	/**
	 * 友情提示
	 */
	public String Instruction;
	/**
	 * 本次活动的价格
	 */
	public String PriceValue;
	public String CountDown;
	public String Comment;
	/**
	 * @return the priceValue
	 */
	public double getPriceValue() {
		double price = 0;
		try {
			price = Double.valueOf(PriceValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return price;
	}
	/**
	 * @param priceValue the priceValue to set
	 */
	public void setPriceValue(String priceValue) {
		PriceValue = priceValue;
	}
	
	
	
}
