package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.customDialog.PlaceAnnotationDialog;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.OverlayItem;

public class ShowAllPlace_Osm extends MapActivity {
	public MapView mapView;
	VtioCoreService services = new VtioCoreService();
	GeoPoint hustPositionPoint;
	GeoPoint centerPoint;
	Context ctx;
	private static final int CHOOSE_GOOGLE_MAP = 0;
	ArrayList<GeoPoint> listPoint = new ArrayList<GeoPoint>();

	@Override
	protected void onCreate(Bundle icicle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(icicle);
		XmlAdapter.synConfig(this);
		setContentView(R.layout.osmap);
		ctx = this;

		mapView = (MapView) findViewById(R.id.map_osm);
		mapView.setBuiltInZoomControls(true);

		setCurrentPoint();

		Bundle extra = getIntent().getExtras();

		Drawable icon = getResources().getDrawable(
				R.drawable.location_marker_osm);
		DefaultItemizedOverlay overlay = new DefaultItemizedOverlay(icon);

		for (int i = 0; extra.getString("URI" + i) != null; i++) {
			String placeURI = extra.getString("URI" + i);
			String placeLabel = extra.getString("label" + i);

			// added by Dungct - Lay long - lat sang hien thi. Khong truy van
			double geoLat = 0.0;
			double geoLong = 0.0;
			String placeLong = extra.getString("longtitude" + i);
			String placeLat = extra.getString("latitude" + i);
			String type = extra.getString("type" + i);
			// Log.v("LongLat-Show", placeLat + "--" + placeLong);

			geoLat = Double.parseDouble(placeLat);
			geoLong = Double.parseDouble(placeLong);
			//
			// Log.v("Dungct", geoLat + "--" + geoLong);

			GeoPoint placePoint = new GeoPoint((int) (1E6 * geoLat),
					(int) (1E6 * geoLong));
			// String URIConcept = new
			// VtioService().getURIClassFromURIInstance(placeURI).get(0).get(0);
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

			} else {
				drawable = getResources().getDrawable(
						R.drawable.location_marker_osm);
			}

			OverlayItem item = new OverlayItem(placePoint, placeURI, placeLabel);
			item.setMarker(Overlay.setAlignment(drawable,
					Overlay.CENTER_HORIZONTAL | Overlay.CENTER_VERTICAL));
			overlay.addItem(item);

			listPoint.add(placePoint);
		}
		// add vi tri hien tai
		OverlayItem itemPosition = new OverlayItem(hustPositionPoint, "", "");
		itemPosition.setMarker(Overlay.setAlignment(
				getResources().getDrawable(R.drawable.profile),
				Overlay.CENTER_HORIZONTAL | Overlay.CENTER_VERTICAL));
		overlay.addItem(itemPosition);

		listPoint.add(hustPositionPoint);
		// add a focus change listener
		overlay.setOnFocusChangeListener(new ItemizedOverlay.OnFocusChangeListener() {
			@Override
			public void onFocusChanged(ItemizedOverlay overlay,
					OverlayItem newFocus) {
				// when focused item changes, recenter map and show info
				mapView.getController().animateTo(newFocus.getPoint());
				PlaceAnnotationDialog dialog = new PlaceAnnotationDialog(ctx,
						newFocus.getTitle());
				dialog.show();

			}
		});
		// tim diem center
		double latCenter = 0;
		double lngCenter = 0;
		for (int i = 0; i < listPoint.size(); i++) {
			latCenter += listPoint.get(i).getLatitude();
			lngCenter += listPoint.get(i).getLongitude();
		}
		latCenter = latCenter / listPoint.size();
		lngCenter = lngCenter / listPoint.size();

		centerPoint = new GeoPoint(latCenter, lngCenter);

		// khoang cach cac diem
		mapView.getOverlays().add(overlay);
		mapView.invalidate();
		mapView.getController().setCenter(centerPoint);
		mapView.getController().setZoom(getZoomLevel(listPoint));

	}

	private void setCurrentPoint() {
		int currentGeoLat = (int) (1E6 * hust.hgbk.vtio.vinafood.constant.Location.GEO_LAT_DEFAULT);
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

		hustPositionPoint = new GeoPoint(currentGeoLat, currentGeoLong);

	}

	private int getZoomLevel(ArrayList<GeoPoint> listPoint) {
		int returnValue = 10;
		// tim khoang cach lon nhat giua cac tap diem
		float maxDistance = 0;
		Log.v("", "size  : " + listPoint.size());

		Location positionA = new Location("1");
		positionA.setLatitude(centerPoint.getLatitude());
		positionA.setLongitude(centerPoint.getLongitude());
		Log.v("", "centerPoint : " + centerPoint.getLatitude() + " : "
				+ centerPoint.getLongitude());
		for (int i = 0; i < listPoint.size(); i++) {

			Location positionB = new Location("2");
			positionB.setLatitude(listPoint.get(i).getLatitude());
			positionB.setLongitude(listPoint.get(i).getLongitude());

			float temp = positionA.distanceTo(positionB);
			if (temp > maxDistance)
				maxDistance = temp;

		}

		Log.v("", "Max distance : " + maxDistance);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// Display display = getWindowManager().getDefaultDisplay();
		int minDimensionScreen = Math.min(metrics.widthPixels,
				metrics.heightPixels);
		minDimensionScreen = minDimensionScreen / 2;
		float ratio = maxDistance / (float) minDimensionScreen;
		Log.v("", "resolution : " + minDimensionScreen);
		Log.v("", "ratio : " + ratio);
		float[] standRatio = { (float) 1.194, (float) 2.388, (float) 4.777,
				(float) 9.554, (float) 19.109, (float) 38.218, (float) 76.437,
				(float) 152.874, (float) 305.748, (float) 611.496,
				Float.MAX_VALUE };
		int[] response = { 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7 };

		for (int i = 0; i < standRatio.length; i++) {
			if (ratio < standRatio[i]) {
				returnValue = response[i];
				break;
			}
		}

		return returnValue;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(Menu.NONE, CHOOSE_GOOGLE_MAP, Menu.NONE, getResources()
				.getString(R.string.use_gmap));
		return super.onCreateOptionsMenu(pMenu);
	}

	@Override
	public boolean onMenuItemSelected(final int pFeatureId, final MenuItem pItem) {
		switch (pItem.getItemId()) {
		case CHOOSE_GOOGLE_MAP:
			XmlAdapter.useMapQuess(getBaseContext(), false);
			Intent i = new Intent(ctx, ShowAllPlaceOnMap.class);
			i.putExtras(getIntent());
			startActivity(i);
			finish();
			return true;
		default:
			return super.onMenuItemSelected(pFeatureId, pItem);
		}
	}

}
