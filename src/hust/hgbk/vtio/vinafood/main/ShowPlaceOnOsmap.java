package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.customDialog.MapsSettingDialog_Osm;
import hust.hgbk.vtio.vinafood.customDialog.PlaceAnnotationDialog;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RouteManager;

public class ShowPlaceOnOsmap extends MapActivity {
	VtioCoreService services = new VtioCoreService();
	private MapView mapView;
	String placeURI;
	String placeLabel;
	Bundle extra;

	GeoPoint placePoint;
	GeoPoint hustPositionPoint;
	ArrayList<GeoPoint> listPoint = new ArrayList<GeoPoint>();
	private DetectLocationTask detectLocationTask;

	String iconUrl;
	private int iconId = 0;
	private boolean isStartFromCommentActivity = false;

	private FullDataInstance fullDataInstance;

	Context ctx;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.osmap);
		ctx = this;

		mapView = (MapView) findViewById(R.id.map_osm);
		mapView.setBuiltInZoomControls(true);

		Double geoLat = 0.0;
		Double geoLong = 0.0;
		extra = getIntent().getExtras();
		fullDataInstance = (FullDataInstance) extra
				.getSerializable("fullinstance");

		geoLat = fullDataInstance.getLatitude();// extra.getDouble("lat", -1.0);
		geoLong = fullDataInstance.getLongitude();// extra.getDouble("long",
													// -1.0);
		placeURI = fullDataInstance.getUri();
		placeLabel = fullDataInstance.getLabel();
		isStartFromCommentActivity = extra.getBoolean("from_comment_activity");

		try {
			iconUrl = OntologyCache.uriOfIcon.get(
					OntologyCache.hashMapTypeLabelToUri.get(fullDataInstance
							.getType() + "@" + ServerConfig.LANGUAGE_CODE))
					.getIconUrl();
			iconId = OntologyCache.uriOfIcon.get(
					OntologyCache.hashMapTypeLabelToUri.get(fullDataInstance
							.getType() + "@" + ServerConfig.LANGUAGE_CODE))
					.getIconId();
		} catch (Exception e) {
		}

		/*
		 * iconId = extra.getInt("iconId"); placeURI = extra.getString("URI");
		 * placeLabel = extra.getString("label"); iconUrl =
		 * extra.getString("iconurl");
		 */

		placePoint = getPlacePoint(placeURI, geoLat, geoLong);
		hustPositionPoint = getPositionPoint();
		listPoint.add(hustPositionPoint);
		listPoint.add(placePoint);

		drawPath();
		addOverLay();
	}

	private void refresh() {
		hustPositionPoint = getPositionPoint();
		listPoint.clear();
		listPoint.add(hustPositionPoint);
		listPoint.add(placePoint);
		mapView.getOverlays().clear();
		drawPath();
		addOverLay();
	}

	private GeoPoint getPositionPoint() {
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

		GeoPoint returnValue = new GeoPoint(currentGeoLat, currentGeoLong);

		return returnValue;
	}

	private GeoPoint getPlacePoint(String placeURI, double geoLat,
			double geoLong) {
		if (geoLat == -1.0 || geoLong == -1.0) {
			String getLatLongQuery = "Select ?lat ?long where {<" + placeURI
					+ "> vtio:hasLatitude ?lat. <" + placeURI
					+ "> vtio:hasLongtitude ?long.}";
			ArrayList<ArrayList<String>> latLongResult = services.executeQuery(
					getLatLongQuery, false);

			try {
				String placeLat = latLongResult.get(0).get(0)
						.replace("^^" + NameSpace.xsd + "double", "");
				String placeLong = latLongResult.get(0).get(1)
						.replace("^^" + NameSpace.xsd + "double", "");
				geoLat = Double.parseDouble(placeLat);
				geoLong = Double.parseDouble(placeLong);
			} catch (Exception e) {
				geoLat = 0.0;
				geoLong = 0.0;
			}
		}

		GeoPoint returnValue = new GeoPoint(geoLat, geoLong);

		return returnValue;
	}

	private void addOverLay() {
		Drawable drawable = null;

		if (iconId != 0) {
			Log.v("CLASSURI", iconUrl);
			drawable = getResources().getDrawable(iconId);

		}

		if (drawable == null) {
			drawable = getResources().getDrawable(
					R.drawable.location_marker_osm);
		}
		final DefaultItemizedOverlay poiOverlay = new DefaultItemizedOverlay(
				drawable);
		OverlayItem poi1 = new OverlayItem(placePoint, placeURI, placeLabel);
		poiOverlay.addItem(poi1);

		OverlayItem poi2 = new OverlayItem(hustPositionPoint, "Your Position",
				"");
		poi2.setMarker(Overlay.setAlignment(
				getResources().getDrawable(R.drawable.profile),
				Overlay.CENTER_HORIZONTAL | Overlay.CENTER_VERTICAL));
		poiOverlay.addItem(poi2);

		// add a tap listener for the POI overlay
		poiOverlay.setTapListener(new ItemizedOverlay.OverlayTapListener() {
			@Override
			public void onTap(GeoPoint pt, MapView mapView) {
				// when tapped, show the annotation for the overlayItem
				int lastTouchedIndex = poiOverlay.getLastFocusedIndex();
				if (lastTouchedIndex > -1) {
					OverlayItem tapped = poiOverlay.getItem(lastTouchedIndex);
					PlaceAnnotationDialog dialog = new PlaceAnnotationDialog(
							ctx, tapped.getTitle());
					dialog.show();
				}
			}
		});
		mapView.getOverlays().add(poiOverlay);

	}

	private void drawPath() {
		RouteManager routeManager = new RouteManager(this);
		routeManager.setMapView(mapView);

		String start = "{latLng:{lat:"
				+ String.valueOf(hustPositionPoint.getLatitude()) + ",lng:"
				+ String.valueOf(hustPositionPoint.getLongitude()) + "}}";
		String end = "{latLng:{lat:" + String.valueOf(placePoint.getLatitude())
				+ ",lng:" + String.valueOf(placePoint.getLongitude()) + "}}";
		routeManager.createRoute(start, end);

		mapView.getController().setCenter(placePoint);
		mapView.getController().animateTo(placePoint);
		mapView.getController().setZoom(getZoomLevel(listPoint));
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuIf = getMenuInflater();
		menuIf.inflate(R.menu.map_view_option_menu_osm, menu);

		return true;
	}

	private int getZoomLevel(ArrayList<GeoPoint> listPoint) {
		int returnValue = 10;
		// tim khoang cach lon nhat giua cac tap diem
		float maxDistance = 0;
		Log.v("", "size  : " + listPoint.size());

		for (int i = 0; i < listPoint.size(); i++) {
			for (int j = i + 1; j < listPoint.size(); j++) {
				try {
					Location positionA = new Location("1");
					positionA.setLatitude(listPoint.get(i).getLatitude());
					positionA.setLongitude(listPoint.get(i).getLongitude());

					Location positionB = new Location("2");
					positionB.setLatitude(listPoint.get(j).getLatitude());
					positionB.setLongitude(listPoint.get(j).getLongitude());

					float temp = positionA.distanceTo(positionB);
					// temp = (int) temp / 1000.0f;
					// Log.v("",
					// "Geo A : "+listPoint.get(i).getLatitude()+" , "+listPoint.get(i).getLongitude());
					// Log.v("",
					// "Geo B : "+listPoint.get(j).getLatitude()+" , "+listPoint.get(j).getLongitude());
					// Log.v("", "distance : "+String.valueOf(temp));
					if (temp > maxDistance)
						maxDistance = temp;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		Log.v("", "Max distance : " + maxDistance);
		Display display = getWindowManager().getDefaultDisplay();
		int minDimensionScreen = Math.min(display.getWidth(),
				display.getHeight());
		minDimensionScreen = minDimensionScreen / 2;
		float ratio = maxDistance / (float) minDimensionScreen;

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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Chon setting
		if (item.getItemId() == R.id.map_setting) {
			MapsSettingDialog_Osm settingDialog = new MapsSettingDialog_Osm(
					ShowPlaceOnOsmap.this, this.mapView);
			settingDialog.show();
		} else if (item.getItemId() == R.id.refresh) {
			if (detectLocationTask != null) {
				detectLocationTask.cancel(true);
				detectLocationTask = null;
			}
			detectLocationTask = new DetectLocationTask(ShowPlaceOnOsmap.this);
			detectLocationTask.execute();
			// } else if (item.getItemId() == R.id.guide_osm){
			// double[] tmp=new double [4];
			// Intent intent = new Intent(ctx, DetailGuideOsm.class);
			//
			// tmp[0]=hustPositionPoint.getLatitude();
			// tmp[1]=hustPositionPoint.getLongitude();
			// tmp[2]=placePoint.getLatitude();
			// tmp[3]=placePoint.getLongitude();
			//
			// intent.putExtra("latlon", tmp);
			// intent.putExtra("label", placeLabel);
			//
			// ctx.startActivity(intent);
		} else if (item.getItemId() == R.id.gmap) {
			XmlAdapter.useMapQuess(getBaseContext(), false);
			Intent i = new Intent(ctx, ShowPlaceOnMapsActivity.class);
			i.putExtras(getIntent());
			startActivity(i);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void setGeoLocation(android.location.Location location) {
		String lat = String.valueOf(location.getLatitude());
		String lon = String.valueOf(location.getLongitude());
		// Log.v("LOCATION", lat + "-" + lon);
		try {
			lat = lat.substring(0, 10);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			lon = lon.substring(0, 10);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			hust.hgbk.vtio.vinafood.constant.Location.getInstance().setGeo(
					Float.valueOf(lat), Float.valueOf(lon));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class DetectLocationTask extends AsyncTask<Void, Void, Void> {

		private Context context;
		private String address;
		private LocationManager locationManager;
		private LocationListener locationListener;
		private boolean isDetecting = false;
		private ProgressDialog progressDialog;
		private int countTimeOut;
		private int count = 0;

		public DetectLocationTask(Context context) {
			this.context = context;
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle(R.string.update_location);
			progressDialog.setMessage(getString(R.string.please_wait));
			countTimeOut = 25;
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			while (isDetecting && countTimeOut > 0) {
				countTimeOut--;
				// Log.v("LOCATION", "count : " + countTimeOut);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		protected void onCancelled() {
			locationManager.removeUpdates(locationListener);
			try {
				progressDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		protected void onPreExecute() {
			progressDialog.show();
			detectLocation();
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			locationManager.removeUpdates(locationListener);
			if (countTimeOut == 0) {
				Toast.makeText(context, "Detect location time out!",
						Toast.LENGTH_SHORT).show();
			}
		}

		private void detectLocation() {
			isDetecting = true;
			count = 0;
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			locationListener = new LocationListener() {

				@Override
				public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

				}

				@Override
				public void onProviderEnabled(String arg0) {
				}

				@Override
				public void onProviderDisabled(String arg0) {

				}

				@Override
				public void onLocationChanged(android.location.Location location) {
					setGeoLocation(location);
					isDetecting = false;
					refresh();
				}

			};

			if (locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				try {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, 0, 0,
							locationListener);
					android.location.Location lastKnowLocation = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (lastKnowLocation != null) {
						setGeoLocation(lastKnowLocation);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			} else if (locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				try {
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, 0, 0,
							locationListener);

					android.location.Location lastKnowLocation = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (lastKnowLocation != null) {
						// Log.v("TEST", "last lat: " +
						// lastKnowLocation.getLatitude());
						setGeoLocation(lastKnowLocation);
					}
				} catch (IllegalArgumentException e) {
					Toast.makeText(
							context,
							"Your device does not support GPS Provider! Try NETWORK Provider.",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				hust.hgbk.vtio.vinafood.constant.Location
						.showSettingLocationDialog(context);
				isDetecting = false;
			}

		}
	}

	public void onStop() {
		try {
			if (detectLocationTask != null) {
				detectLocationTask.cancel(true);
				detectLocationTask = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onStop();
		// Log.v("KEN", "Stop result activity");

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

}
