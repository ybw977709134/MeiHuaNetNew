package com.MeiHuaNet.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * @description 为了使用自定义字体，自定义的textview类
 * @author lee
 * @createTime 2013-7-3下午2:30:59
 * 
 */
public class TextViewFont extends TextView {
	private static Typeface sTypeface;

	public TextViewFont(Context context) {
		super(context);
	}

	public TextViewFont(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public TextViewFont(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		if (sTypeface == null) {
			// 使用自定义的字体
			sTypeface = Typeface.createFromAsset(ctx.getAssets(),
					"lanting_black_jane.ttf");
		}
		setTypeface(sTypeface);
	}
	// TypedArray a = ctx.obtainStyledAttributes(attrs,
	// R.styleable.TextViewPlus);
	// String customFont = a.getString(R.styleable.TextViewPlus_customFont);
	// setCustomFont(ctx, customFont);
	// a.recycle();

	// public boolean setCustomFont(Context ctx, String asset) {
	// Typeface tf = null;
	// try {
	// tf = Typeface.createFromAsset(ctx.getAssets(), asset);
	// } catch (Exception e) {
	// Log.e(TAG, "Could not get typeface: " + e.getMessage());
	// return false;
	// }
	// setTypeface(tf);
	// return true;
	// }
}
