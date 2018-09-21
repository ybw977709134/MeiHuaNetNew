package com.MeiHuaNet.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * @description 解决maxlines属性和ellipsize属性不能很好的一起使用的问题(在有些机型上有冲突)
 * @author lee
 * @time 2013-10-25 下午2:11:32
 * 
 */
public class EllipsizingTextView extends TextView {
	private static final String ELLIPSIS = "...";
	private static Typeface sTypeface;

	public interface EllipsizeListener {
		void ellipsizeStateChanged(boolean ellipsized);
	}

	private final List<EllipsizeListener> ellipsizeListeners = new ArrayList<EllipsizeListener>();
	private boolean isEllipsized;
	private boolean isStale;
	private boolean programmaticChange;
	private String fullText;
	private int maxLines = -1;
	private float lineSpacingMultiplier = 1.0f;
	private float lineAdditionalVerticalPadding = 0.0f;

	public EllipsizingTextView(Context context) {
		super(context);
		setCustomFont(context);
	}

	public EllipsizingTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context);
	}

	public EllipsizingTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context);
	}

	private void setCustomFont(Context ctx) {
		if (sTypeface == null) {
			// 使用自定义的字体
			sTypeface = Typeface.createFromAsset(ctx.getAssets(),
					"lanting_black_jane.ttf");
		}
		setTypeface(sTypeface);
	}

	public void addEllipsizeListener(EllipsizeListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		ellipsizeListeners.add(listener);
	}

	public void removeEllipsizeListener(EllipsizeListener listener) {
		ellipsizeListeners.remove(listener);
	}

	public boolean isEllipsized() {
		return isEllipsized;
	}

	@Override
	public void setMaxLines(int maxLines) {
		super.setMaxLines(maxLines);
		this.maxLines = maxLines;
		isStale = true;
	}

	public int getMyMaxLines() {
		return maxLines;
	}

	@Override
	public void setLineSpacing(float add, float mult) {
		this.lineAdditionalVerticalPadding = add;
		this.lineSpacingMultiplier = mult;
		super.setLineSpacing(add, mult);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int before,
			int after) {
		super.onTextChanged(text, start, before, after);
		if (!programmaticChange) {
			fullText = text.toString();
			isStale = true;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isStale) {
			super.setEllipsize(null);
			resetText();
		}
		super.onDraw(canvas);
	}

	private void resetText() {
		int maxLines = getMyMaxLines();
		String workingText = fullText;
		boolean ellipsized = false;
		if (maxLines != -1) {
			Layout layout = createWorkingLayout(workingText);
			if (layout.getLineCount() > maxLines) {
				workingText = fullText.substring(0,
						layout.getLineEnd(maxLines) - 1).trim();
				while (layout.getLineCount() > maxLines
						|| layout.getHeight() > getHeight()) {
					if (workingText != null) {
						workingText = workingText.trim();
						workingText = workingText.substring(0,
								workingText.length() - 1);
					}
					layout = createWorkingLayout(workingText + ELLIPSIS);
				}
				workingText = workingText + ELLIPSIS;
				ellipsized = true;

			}
		}
		if (!workingText.equals(getText())) {
			programmaticChange = true;
			try {
				setText(workingText);
			} finally {
				programmaticChange = false;
			}
		}
		isStale = false;
		if (ellipsized != isEllipsized) {
			isEllipsized = ellipsized;
			for (EllipsizeListener listener : ellipsizeListeners) {
				listener.ellipsizeStateChanged(ellipsized);
			}
		}
	}

	private Layout createWorkingLayout(String workingText) {
		return new StaticLayout(workingText, getPaint(), getWidth()
				- getPaddingLeft() - getPaddingRight(), Alignment.ALIGN_NORMAL,
				lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
	}

	@Override
	public void setEllipsize(TruncateAt where) {
		// Ellipsize settings are not respected
	}
}
