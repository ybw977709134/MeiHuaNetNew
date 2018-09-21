package com.MeiHuaNet.view;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextFont extends EditText {

	private static Typeface sTypeface;
	public EditTextFont(Context context) {
		super(context);
	}

	public EditTextFont(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public EditTextFont(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		if(sTypeface == null){
			sTypeface = Typeface.createFromAsset(ctx.getAssets(), "lanting_black_jane.ttf");
		}
		setTypeface(sTypeface);
//		TypedArray a = ctx.obtainStyledAttributes(attrs,
//				R.styleable.TextViewPlus);
//		String customFont = a.getString(R.styleable.TextViewPlus_customFont);
//		setCustomFont(ctx, customFont);
//		a.recycle();
	}

//	public boolean setCustomFont(Context ctx, String asset) {
//		Typeface tf = null;
//		try {
//			tf = Typeface.createFromAsset(ctx.getAssets(), asset);
//		} catch (Exception e) {
//			Log.e(TAG, "Could not get typeface: " + e.getMessage());
//			return false;
//		}
//		setTypeface(tf);
//		return true;
//	}
}

