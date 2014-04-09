package com.directuc.android;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class DirectUCWebView extends WebView {

	public DirectUCWebView(Context context) {
		super(context);
	}
	
	public DirectUCWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			if (event.getAction() == MotionEvent.ACTION_DOWN || 
					event.getAction() == MotionEvent.ACTION_POINTER_DOWN || 
					event.getAction() == MotionEvent.ACTION_POINTER_1_DOWN ||
					event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN ||
					event.getAction() == MotionEvent.ACTION_POINTER_3_DOWN) {
				if (event.getPointerCount() > 1) {
					getSettings().setBuiltInZoomControls(true);
				} else {
					getSettings().setBuiltInZoomControls(false);
				}
			}
		}
		
		return super.onTouchEvent(event);
	}
}
