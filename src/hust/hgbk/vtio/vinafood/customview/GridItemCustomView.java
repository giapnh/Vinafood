package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.main.R;

public class GridItemCustomView extends LinearLayout {
	private ImageView imageView;
	private TextView textView;

	public GridItemCustomView(Context context) {
		super(context);
		LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(
		        Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.vtio_app_gridview_item, this, true);

		imageView = (ImageView) findViewById(R.id.imageApp);
		textView = (TextView) findViewById(R.id.titleApp);
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}

}
