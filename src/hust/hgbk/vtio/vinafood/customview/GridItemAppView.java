package hust.hgbk.vtio.vinafood.customview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.entities.SubApplication;
import hust.hgbk.vtio.vinafood.main.R;

public class GridItemAppView extends LinearLayout {
	TextView textView;
	ImageView imageView;
	LinearLayout linearLayout;
	Context ctx;
	SubApplication subApp;

	public GridItemAppView(Context context) {
		super(context);
		this.ctx = context;
		LayoutInflater li = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.grid_item_app_layout, this, true);
		textView = (TextView) findViewById(R.id.textview);
		imageView = (ImageView) findViewById(R.id.imageview);
		linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
	}

	public void setUI(SubApplication subApplication) {
		subApp = subApplication;
		textView.setText(subApplication.getAppName());
		imageView.setBackgroundResource(subApplication.getAppIconId());
		linearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity) ctx).startActivity(subApp.getIntent());
			}
		});
	}

}
