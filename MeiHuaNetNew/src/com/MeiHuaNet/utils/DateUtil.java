package com.MeiHuaNet.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;

public class DateUtil {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	// private static SimpleDateFormat format = new
	// SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 将日期转换成年月日的字符串形式
	 */
	public static String getDateStr() {
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 判断给定的日期字符串是否是当前系统时间之前的时间
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isBeforeNow(String date) {
		boolean isBefore = true;
		try {
			Date date2 = simpleDateFormat.parse(date);
			Date now = new Date();
			if (date2.getTime() >= now.getTime()) {
				isBefore = false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return isBefore;
	}

	/**
	 * 根据开始日期和结束日期返回活动的时间字符串（活动时间只有一天返回格式：2013.02.03 活动时间有几天返回格式：02.03-02.05）
	 * 
	 * @param startDate
	 *            开始日期的字符串（yyyy-MM-dd hh:mm:ss）
	 * @param endDate
	 *            结束日期的字符串（yyyy-MM-dd hh:mm:ss）
	 * @return
	 */
	public static String getEventDate(String startDate, String endDate) {
		String result = "";
		try {
			String[] starts = startDate.split(" ");
			String[] ends = endDate.split(" ");
			if (starts[0].equals(ends[0])) {
				result = starts[0].replaceAll("-", ".");
			} else {
				String starttime = starts[0].substring(5).replace("-", ".");
				String endtime = ends[0].substring(5).replace("-", ".");
				result = starttime + "-" + endtime;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据所给的日期字符串返回long型的时间值
	 * 
	 * @param date
	 *            （格式：2013-11-22 11:30:35)
	 * @return
	 */
	public static long getLongTime(String date) {
		long result = System.currentTimeMillis();
		if (!TextUtils.isEmpty(date)) {
			String[] dates = date.split(" ");
			if (dates != null && dates.length > 1) {
				Calendar mCalendar = Calendar.getInstance();
				String[] years = dates[0].split("-");
				if (years != null && years.length > 2) {
					mCalendar.set(Calendar.YEAR, Integer.valueOf(years[0]));
					mCalendar
							.set(Calendar.MONTH, Integer.valueOf(years[1]) - 1);
					mCalendar.set(Calendar.DAY_OF_MONTH,
							Integer.valueOf(years[2]));

				}
				String[] hours = dates[1].split(":");
				if (hours != null && hours.length > 1) {
					mCalendar.set(Calendar.HOUR_OF_DAY,
							Integer.valueOf(hours[0]));
					mCalendar.set(Calendar.MINUTE, Integer.valueOf(hours[1]));
				}
				result = mCalendar.getTimeInMillis();
			}
		}
		return result;
	}

	// /**
	// * 以yyyy-MM-dd格式返回当前日期3个月以前的日期的字符串
	// * @return
	// */
	// public static String get3MonthAgoDate(){
	// String nowDate = format.format(new Date());
	// String[] dates = nowDate.split("-");
	// String year = dates[0];
	// String month = dates[1];
	// String day = dates[2];
	// if(Integer.parseInt(month) < 4){
	// if(Integer.parseInt(month)==1){
	// month = "10";
	// } else if (Integer.parseInt(month)==2){
	// month= "11";
	// if(Integer.parseInt(day)>30){
	// day= "30";
	// }
	// } else if( Integer.parseInt(month)==3){
	// month = "12";
	// }
	// year = String.valueOf(Integer.parseInt(year)-1);
	// } else {
	// //月份大于等于4
	// int afterMonth = Integer.parseInt(month)-3;
	// if(afterMonth==2){
	// if(!isLeapYear(Integer.parseInt(year))){
	// if(Integer.parseInt(day)>28){
	// //不是闰年，2月最多只有28天
	// day = "28";
	// }
	// }
	// } else if(afterMonth==4 || afterMonth==6|| afterMonth==9 ){
	// if(Integer.parseInt(day)>30){
	// day= "30";
	// }
	// }
	// month = String.valueOf(afterMonth);
	// }
	// String result = year+"-" + month+"-"+day;
	// return result;
	// }
	//
	// /**
	// * 以yyyy-MM-dd格式返回当前日期1年以前的日期的字符串
	// * @return
	// */
	// public static String get1YearAgoDate(){
	// String nowDate = format.format(new Date());
	// String[] dates = nowDate.split("-");
	// String year = dates[0];
	// String month = dates[1];
	// String day = dates[2];
	// if(Integer.parseInt(month)==2){
	// if(!isLeapYear(Integer.parseInt(year))){
	// if(Integer.parseInt(day)>28){
	// day = "28";
	// }
	// }
	// }
	// year = String.valueOf(Integer.parseInt(year)-1);
	// return year+"-"+month+"-"+day;
	// }
	//
	// /**
	// * 判断所给的年份是否是闰年
	// * @param year
	// * @return 是闰年返回true, 否则返回false
	// */
	// public static boolean isLeapYear(int year){
	// boolean isLeap = false;
	// if(year%4==0){
	// if(year%400 == 0){
	// isLeap = true;
	// } else if(year%100!=0){
	// isLeap = true;
	// }
	// }
	// return isLeap;
	// }
}
