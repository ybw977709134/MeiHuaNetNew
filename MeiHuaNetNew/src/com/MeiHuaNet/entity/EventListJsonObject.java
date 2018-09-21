package com.MeiHuaNet.entity;

import java.util.ArrayList;

public class EventListJsonObject {

	/**
	 * 活动的总数
	 */
	public String ResultCount;
	/**
	 * 大展的Id(这个是梅花网传播业大展时才有的字段）
	 */
	public String MexpoID;
	/**
	 * 活动的列表
	 */
	public ArrayList<EventObject> EventList;
}
