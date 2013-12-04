package hust.hgbk.vtio.vinafood.customview;

import hust.hgbk.vtio.vinafood.config.log;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ExpandableWebView extends WebView {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ExpandableWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandableWebView(Context context, AttributeSet attrs, int defStyle,
			boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
	}

	public ExpandableWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ExpandableWebView(Context context) {
		super(context);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		log.m("On scroll");
		super.onScrollChanged(l, t, oldl, oldt);
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
