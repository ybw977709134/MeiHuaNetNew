package com.MeiHuaNet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;

public class MyWebView extends WebView{

    public MyWebView(Context context) {
        super(context, null);
    }

    public MyWebView(Context context, AttributeSet attrs) {
    	super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    }

	@Override
	public void addView(View child, int index)
	{
	    if (child.getClass().getName().equals("com.adobe.flashplayer.FlashPaintSurface"))
	    {
	        ((SurfaceView)child).setZOrderOnTop(false);
//	    	((SurfaceView)child).setZOrderMediaOverlay(true);
	    }

	    super.addView(child, index);
	}
}
