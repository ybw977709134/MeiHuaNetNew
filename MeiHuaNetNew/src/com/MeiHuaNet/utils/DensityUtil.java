package com.MeiHuaNet.utils;

import android.app.Activity;
import android.content.Context;   
import android.util.DisplayMetrics;

public class DensityUtil {   
   
    /**  
     * 将dp转换成对应的px
     */   
    public static int dip2px(Context context, float dpValue) {   
        final float scale = context.getResources().getDisplayMetrics().density;   
        return (int) (dpValue * scale + 0.5f);   
    }   
   
    /**  
     * 将px转换成对应的dp
     */   
    public static int px2dip(Context context, float pxValue) {   
        final float scale = context.getResources().getDisplayMetrics().density;   
        return (int) (pxValue / scale + 0.5f);   
    }   
    
    /**
     * 取得屏幕的宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Activity context){
    	DisplayMetrics displaysMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
		int width = context.getWindowManager().getDefaultDisplay().getWidth();//大小是px
		width = px2dip(context, width);
		return width;
    }
} 