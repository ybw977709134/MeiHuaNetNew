package com.MeiHuaNet.utils;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.format.Time;

public class CalenderUtils {

	private static String calanderURL = "";
	private static String calanderEventURL = "";
    private static String calanderRemiderURL = "";
    
    static{
    	try{
    		if(Integer.parseInt(Build.VERSION.SDK) >= 8){
    			calanderURL = "content://com.android.calendar/calendars";
    			calanderEventURL = "content://com.android.calendar/events";
    			calanderRemiderURL = "content://com.android.calendar/reminders";

    		}else{
    			calanderURL = "content://calendar/calendars";
    			calanderEventURL = "content://calendar/events";
    			calanderRemiderURL = "content://calendar/reminders";		
    		}
    	} catch(Exception e){
    		e.printStackTrace();
    	}
	
	}
	
	/**
	 * 判断手机是否有自带的google日历应用
	 * 
	 * @return
	 */
	public static boolean isHaveCalendarApp(Context context) {
		try {
			List<PackageInfo> packList = context.getPackageManager()
					.getInstalledPackages(0);
			for (int i = 0; i < packList.size(); i++) {
				PackageInfo packageInfo = packList.get(i);
				if ("com.android.calendar".equals(packageInfo.packageName)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 增加活动到手机日历应用中
	 */
	public static void addToCalendar(Context context,String title,String description,long start, long end) {
		try{
			String calId = "";
			Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null, 
					null, null, null);
			if(userCursor.getCount() > 0){
				userCursor.moveToFirst();
				calId = userCursor.getString(userCursor.getColumnIndex("_id"));
				
			}
			ContentValues event = new ContentValues();
	    	event.put("title", title);
	    	event.put("description", description);
	    	//增加日程到日历中的第一个账号下面
	    	event.put("calendar_id",calId);
	    	
	    	event.put("dtstart", start);
	    	event.put("dtend", end);
	    	event.put("hasAlarm",1);
	    	event.put("eventTimezone", Time.getCurrentTimezone());
	    	
	    	Uri newEvent = context.getContentResolver().insert(Uri.parse(calanderEventURL), event);
	    	long id = Long.parseLong( newEvent.getLastPathSegment() );
	    	ContentValues values = new ContentValues();
	        values.put( "event_id", id );
	        //提前多长时间提醒
	        values.put( "minutes", 24*60 );
	        values.put("method", 1);
	        
	        context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
}
