package com.directuc.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

public class DirectUCViewFlipper extends ViewFlipper {

	public DirectUCViewFlipper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public DirectUCViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		onTouchEvent(ev);
		return false;
	}
}
