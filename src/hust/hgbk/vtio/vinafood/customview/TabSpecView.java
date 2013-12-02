package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.main.R;

public class TabSpecView extends LinearLayout {
	ImageView tabSpecIconImageView;
	TextView  tabSpecNameTextView;
	public TabSpecView(Context context) {
		super(context);
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.tab_spec_layout, this, true);
		tabSpecIconImageView = (ImageView) findViewById(R.id.tabSpecIconImageView);
		tabSpecNameTextView = (TextView) findViewById(R.id.tabSpecNameTextView);
	}
	public ImageView getTabSpecIconImageView() {
		return tabSpecIconImageView;
	}
	public void setTabSpecIconImageView(ImageView tabSpecIconImageView) {
		this.tabSpecIconImageView = tabSpecIconImageView;
	}
	public TextView getTabSpecNameTextView() {
		return tabSpecNameTextView;
	}
	public void setTabSpecNameTextView(TextView tabSpecNameTextView) {
		this.tabSpecNameTextView = tabSpecNameTextView;
	}
}
