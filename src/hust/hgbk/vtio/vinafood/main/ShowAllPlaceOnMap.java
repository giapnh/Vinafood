package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.customDialog.MapsSettingDialog;
import hust.hgbk.vtio.vinafood.maps.PlacesItemizedOverlay;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ShowAllPlaceOnMap extends MapActivity {
	public static MapView mapView;
	// VtioService services = new VtioService();
	GeoPoint hustPositionPoint;
	GeoPoint placePoint;
	Context ctx;
	private boolean isStartFromCommentActivity = false;

	@Override
	protected void onCreate(Bundle icicle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(icicle);
		this.ctx = ShowAllPlaceOnMap.this;
		XmlAdapter.synConfig(this);
		setContentView(R.layout.geo_search_on_map);

		TextView placeLabelTextView = (TextView) findViewById(R.id.placeTitleTextView);
		mapView = (MapView) findViewById(R.id.place_map_view);

		mapView.setTraffic(true);

		// Set thong tin cho mapView
		// Cai dat nut zoom tren man hinh
		mapView.setBuiltInZoomControls(true);
		mapView.displayZoomControls(true);

		MapController mapController = mapView.getController();

		// Lay URI place truyen vao
		// Lay GeoPoint place
		Bundle extra = getIntent().getExtras();
		int currentGeoLat = (int) (1E6 * hust.hgbk.vtio.vinafood.constant.Location.GEO_LON_DEFAULT);
		;
		try {
			currentGeoLat = (int) (1E6 * hust.hgbk.vtio.vinafood.constant.Location
					.getInstance().getLatitude());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int currentGeoLong = (int) (1E6 * hust.hgbk.vtio.vinafood.constant.Location.GEO_LON_DEFAULT);
		try {
			currentGeoLong = (int) (1E6 * hust.hgbk.vtio.vinafood.constant.Location
					.getInstance().getLongtitude());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int maxLat = currentGeoLat;
		int minLat = currentGeoLat;
		int maxLon = currentGeoLong;
		int minLon = currentGeoLong;
		hustPositionPoint = new GeoPoint(currentGeoLat, currentGeoLong);

		for (int i = 0; extra.getString("URI" + i) != null; i++) {
			String placeURI = extra.getString("URI" + i);
			Log.v("POSITION", "map: " + placeURI);
			String placeLabel = extra.getString("label" + i);
			// added by Dungct - Lay long - lat sang hien thi. Khong truy van
			double geoLat = 0.0;
			double geoLong = 0.0;
			String placeLong = extra.getString("longtitude" + i);
			String placeLat = extra.getString("latitude" + i);
			String type = extra.getString("type" + i);
			// Log.v("LongLat-Show", placeLat + "--" + placeLong);
			try {
				geoLat = Double.parseDouble(placeLat);
				geoLong = Double.parseDouble(placeLong);
			} catch (Exception e) {
				System.out.println("Loi chuyen kieu:" + placeURI + ":"
						+ placeLat + "---" + placeLong);

			}
			// Log.v("Dungct", geoLat + "--" + geoLong);
			// Set noi dung cho label
			// placeLabelTextView.setText("Show all place on this map");
			placeLabelTextView.setText(getResources().getString(
					R.string.show_all_on_map));
			// Lay toa do hien tai cua SmartPhone thong qua GPS
			if (geoLat == 0.0 || geoLong == 0.0) {
				placeLabelTextView
						.setText("Can't found GeoPoint. Show your current place");
				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Location location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				if (location != null) {
					geoLat = location.getLatitude();
					geoLong = location.getLongitude();
				} else {
					placeLabelTextView.setText("Can't found GeoPoint.");
					// Log.d("GET LOCATION ERRORS", "GPS not available!!!");
				}
			}

			placePoint = new GeoPoint((int) (1E6 * geoLat),
					(int) (1E6 * geoLong));
			maxLat = maxLat > (int) (1E6 * geoLat) ? maxLat
					: (int) (1E6 * geoLat);
			minLat = minLat < (int) (1E6 * geoLat) ? minLat
					: (int) (1E6 * geoLat);
			maxLon = maxLon > (int) (1E6 * geoLong) ? maxLon
					: (int) (1E6 * geoLong);
			minLon = minLon < (int) (1E6 * geoLong) ? minLon
					: (int) (1E6 * geoLong);
			// Map overlays
			List<Overlay> mapOverlays = mapView.getOverlays();
			String iconUri = "";
			try {

				iconUri = OntologyCache.hashMapTypeLabelToUri.get(type + "@"
						+ ServerConfig.LANGUAGE_CODE);
			} catch (Exception e) {
			}
			Drawable drawable = null;

			if (iconUri != null && iconUri.length() > 0) {
				Log.v("CLASSURI", iconUri);
				drawable = getResources().getDrawable(
						OntologyCache.uriOfIcon.get(iconUri).getIconId());

			}
			if (drawable == null) {
				drawable = getResources().getDrawable(R.drawable.maps_point);
			}

			PlacesItemizedOverlay itemOverlay = new PlacesItemizedOverlay(
					drawable, this);
			OverlayItem overlay = new OverlayItem(placePoint, placeLabel,
					placeURI);
			itemOverlay.addOverlay(overlay);

			mapOverlays.add(itemOverlay);
		}

		List<Overlay> mapOverlays = mapView.getOverlays();

		try {
			Drawable drawable = getResources().getDrawable(R.drawable.profile);
			Log.v("POSITION", "hustPositionPoint: "
					+ (hustPositionPoint == null));
			PlacesItemizedOverlay itemOverlay = new PlacesItemizedOverlay(
					drawable, this);
			OverlayItem overlay = new OverlayItem(hustPositionPoint,
					"Your current position", "");
			itemOverlay.addOverlay(overlay);

			mapOverlays.add(itemOverlay);
			Float range = (Float) extra.getFloat("range", 0F);
			Log.v("KEN", "range: " + range);
			try {
				mapController.zoomToSpan(maxLat - minLat, maxLon - minLon);
			} catch (Exception e) {
				// TODO: handle exception
				mapController.setZoom(16);
			}
			mapController.animateTo(new GeoPoint((maxLat + minLat) / 2,
					(maxLon + minLon) / 2));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static byte zoomLevel(double distance) {
		byte zoom = 1;
		double E = 40075;
		// Log.i("Astrology", "result: "+ (Math.log(E/distance)/Math.log(2)+1));
		zoom = (byte) Math.round(Math.log(E / distance) / Math.log(2) + 1);
		// to avoid exeptions
		if (zoom > 17)
			zoom = 17;
		if (zoom < 8)
			zoom = 8;

		return zoom;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuIf = getMenuInflater();
		menuIf.inflate(R.menu.map_view_no_direction, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.map_setting) {
			MapsSettingDialog settingDialog = new MapsSettingDialog(
					ShowAllPlaceOnMap.this, this.mapView);
			settingDialog.show();
		} else if (item.getItemId() == R.id.mapquess) {
			XmlAdapter.useMapQuess(getBaseContext(), true);
			Intent i = new Intent(getBaseContext(), ShowAllPlace_Osm.class);
			i.putExtras(getIntent());
			startActivity(i);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

}
