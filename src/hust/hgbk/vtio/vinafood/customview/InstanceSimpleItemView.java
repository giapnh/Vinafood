package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.main.PlaceDetails;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.ontology.simple.InstanceDataSimple;

public class InstanceSimpleItemView extends LinearLayout {
	Context context;
	TextView textViewLabel;
	TextView textViewType;
	Button buttonDetails;
	Button buttonViewMap;

	InstanceDataSimple instance;

	String uri;
	String label;
	Double longitude;
	Double latitude;

	public InstanceSimpleItemView(Context ctx) {
		super(ctx);
		this.context = ctx;

		LayoutInflater li = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.instance_simple_layout, this, true);

		textViewLabel = (TextView) findViewById(R.id.textViewInstanceLabel);
		textViewType = (TextView) findViewById(R.id.textViewInstanceType);
		buttonDetails = (Button) findViewById(R.id.buttonInstanceDetails);
		buttonViewMap = (Button) findViewById(R.id.buttonViewMap);

		buttonDetails.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (instance != null) {
					//					String instanceUri = instance.getURI();
					Log.v("KEN", "data label: " + uri);
					Intent intent = new Intent(InstanceSimpleItemView.this.context,
					        PlaceDetails.class);
					intent.putExtra("instanceURI", uri);
					InstanceSimpleItemView.this.context.startActivity(intent);
				}
			}
		});

		buttonViewMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(context, XmlAdapter.getShowOnMapActivity(context));
				intent.putExtra("URI", uri);
				intent.putExtra("label", label);
				intent.putExtra("long", longitude);
				intent.putExtra("lat", latitude);
				context.startActivity(intent);
			}
		});
	}

	public void createContent(InstanceDataSimple instance) {
		this.instance = instance;
		textViewLabel.setText(instance.getLabel().replace("^^" + NameSpace.xsd + "string", ""));
		Log.v("TEST2", instance.getInstanceType());
		textViewType.setText(instance.getInstanceType().substring(NameSpace.vtio.length()));
		uri = instance.getURI();
		label = instance.getLabel();
		longitude = instance.getLongitude();
		latitude = instance.getLatitude();
		Log.v("TEST2", "long: " + longitude + "lat: " + latitude);
		if (longitude == null || latitude == null || longitude == 0.0 || latitude == 0.0) {
			buttonViewMap.setVisibility(INVISIBLE);
		}
	}
}
