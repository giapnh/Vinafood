package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.entities.Topic;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DiscoveryCuisineWithHelthViewTopicActivity extends Activity {

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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Topic topic = (Topic) getIntent().getSerializableExtra("topic");
		setContentView(R.layout.discovery_amthuc_suckhoe_view_topic);
		ImageView imgIcon = (ImageView) this.findViewById(R.id.img_icon);
		Picasso.with(this).load(topic.imgLink).centerCrop().resize(130, 130)
				.into(imgIcon);
		TextView title = (TextView) this.findViewById(R.id.title);
		TextView description = (TextView) this.findViewById(R.id.description);
		WebView webView = (WebView) this.findViewById(R.id.webview);
		title.setText(topic.title);
		description.setText(topic.description);
		webView.loadData(topic.content, "text/html", "utf-8");
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
