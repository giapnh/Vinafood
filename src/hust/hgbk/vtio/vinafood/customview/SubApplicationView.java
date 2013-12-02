package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.main.R;

public class SubApplicationView extends LinearLayout {
	Context context;
	ImageView appImageView;
	TextView appNameTextView;
	TextView appDescriptionTextView;

	public SubApplicationView(Context context) {
		super(context);
		this.context = context;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
		        Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.sub_app_item_layout, this, true);

		appImageView = (ImageView) findViewById(R.id.appImageView);
		appNameTextView = (TextView) findViewById(R.id.appNameTextView);
		appDescriptionTextView = (TextView) findViewById(R.id.appDescriptionTextView);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public ImageView getAppImageView() {
		return appImageView;
	}

	public void setAppImageView(ImageView appImageView) {
		this.appImageView = appImageView;
	}

	public TextView getAppNameTextView() {
		return appNameTextView;
	}

	public void setAppNameTextView(TextView appNameTextView) {
		this.appNameTextView = appNameTextView;
	}

	public TextView getAppDescriptionTextView() {
		return appDescriptionTextView;
	}

	public void setAppDescriptionTextView(TextView appDescriptionTextView) {
		this.appDescriptionTextView = appDescriptionTextView;
	}
}
