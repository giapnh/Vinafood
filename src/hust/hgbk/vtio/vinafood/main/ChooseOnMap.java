package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.maps.PlacesItemizedOverlay;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ChooseOnMap extends MapActivity {
	public static MapView mapView;
	MapController mapController;
	TextView placeLabelTextView;
	Context ctx;
	int chooserLat;
	int currentGeoLong;
	int currentGeoLat;
	List<Overlay> mapOverlays;

	PlacesItemizedOverlay itemOverlay;
	Button doneButton;
	Button cancelButton;

	public void onCreate(Bundle icicle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(icicle);
		setContentView(R.layout.choose_on_map);
		ctx = this;
		placeLabelTextView = (TextView) findViewById(R.id.report_text);
		mapView = (MapView) findViewById(R.id.place_map_view);
		doneButton = (Button) findViewById(R.id.done_button);
		cancelButton = (Button) findViewById(R.id.cancel_button);
		mapView.setTraffic(true);
		mapView.setBuiltInZoomControls(true);
		mapView.displayZoomControls(true);

		mapController = mapView.getController();
		mapController.setZoom(10);
		int currentGeoLat = (int) (1E6 * hust.hgbk.vtio.vinafood.constant.Location.GEO_LAT_DEFAULT);
		try {
			currentGeoLat = (int) (1E6 * hust.hgbk.vtio.vinafood.constant.Location.getInstance()
			        .getLatitude());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int currentGeoLong = (int) (1E6 * hust.hgbk.vtio.vinafood.constant.Location.GEO_LON_DEFAULT);
		try {
			currentGeoLong = (int) (1E6 * hust.hgbk.vtio.vinafood.constant.Location.getInstance()
			        .getLongtitude());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final GeoPoint geoPoint = new GeoPoint(currentGeoLat, currentGeoLong);
		if (geoPoint == null) {
			finish();
		}
		mapController.animateTo(geoPoint);
		mapController.setCenter(geoPoint);

		mapOverlays = mapView.getOverlays();

		// Log.v("TEST", "URI instance" + placeURI);
		// Log.v("TEST", "URI Concept" +URIConcept);

		itemOverlay = new PlacesItemizedOverlay(getResources().getDrawable(R.drawable.profile),
		        ctx, true);
		OverlayItem overlay = new OverlayItem(geoPoint, "Your position",
		        "Tap to choose your current position.");
		itemOverlay.addOverlay(overlay);
		mapOverlays.add(itemOverlay);

		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					float lon = (float) (itemOverlay.getGeoPoint().getLongitudeE6() / 1E6);
					float lat = (float) (itemOverlay.getGeoPoint().getLatitudeE6() / 1E6);
					hust.hgbk.vtio.vinafood.constant.Location.getInstance().setGeo(lat, lon);
					//					Toast.makeText(ctx,hust.se.vtio.constant.Location.GEO_LON_DEFAULT + " - " + hust.se.vtio.constant.Location.GEO_LAT_DEFAULT, Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(ctx, "Choose Failed!", Toast.LENGTH_SHORT).show();
				}
				finish();
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
