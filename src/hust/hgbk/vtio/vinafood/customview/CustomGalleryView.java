package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

public class CustomGalleryView extends Gallery {

	public CustomGalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);
		return super.onInterceptTouchEvent(ev);
	}
}
