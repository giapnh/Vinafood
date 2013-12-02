package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.ontology.simple.PlaceDataSimple;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

public class PlaceAnnotationView extends LinearLayout {
	Context context;
	VtioCoreService services;

	String placeURI;

	WebView webviewImage;
	TextView textViewTitle;
	TextView textViewAbstract;

	public PlaceAnnotationView(Context context, String placeURI) {
		super(context);
		services = new VtioCoreService();
		this.context = context;
		this.placeURI = placeURI;

		LayoutInflater li = (LayoutInflater) this.context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.place_annotation_dialog_layout, this, true);

		webviewImage = (WebView) findViewById(R.id.webViewImage);
		textViewTitle = (TextView) findViewById(R.id.textViewPlaceTitle);
		textViewAbstract = (TextView) findViewById(R.id.textViewAbstract);

		PlaceDataSimple placeData = services.getPlaceDataSimple(placeURI);
		Log.v("TEST", "info size: " + placeData.getImageURL());
		String data = "<img src=\"" + placeData.getImageURL() + "\" width='70dip'/>";
		webviewImage.loadData(data, "text/html", "utf-8");
		textViewTitle.setText(placeData.getLabel());
		if (placeData.getHasAbstract().contains("anyType")) {
			textViewAbstract.setText("Abstract not available!");
		} else {
			textViewAbstract.setText(placeData.getHasAbstract().replace("@en", "")
			        .replace("@vn", ""));
		}

	}
}
