package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.LanguageCode;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.customDialog.MapsSettingDialog;
import hust.hgbk.vtio.vinafood.maps.MyItemizedOverlay;
import hust.hgbk.vtio.vinafood.maps.MyOverLay;
import hust.hgbk.vtio.vinafood.maps.PlacesItemizedOverlay;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ShowPlaceOnMapsActivity extends MapActivity {
	public MapView mapView;
	MapController mapController;
	TextView placeLabelTextView;
	VtioCoreService services = new VtioCoreService();
	GeoPoint hustPositionPoint;
	GeoPoint placePoint;
	String placeLabel;
	private PlacesItemizedOverlay myPosItemOverlay;
	private String iconUrl = "";
	// private String type;
	private int iconId = 0;
	private DetectLocationTask detectLocationTask;
	private final int SHOW_NO_DATA_DIALOG = 0;
	boolean isStartFromCommentActivity = false;

	private FullDataInstance fullDataInstance;

	@Override
	protected void onCreate(Bundle icicle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(icicle);
		setContentView(R.layout.show_place_on_maps_layout);

		placeLabelTextView = (TextView) findViewById(R.id.placeTitleTextView);
		mapView = (MapView) findViewById(R.id.place_map_view);

		// Che do street view
		// mapView.setStreetView(true);
		// mapView.setSatellite(true);
		mapView.setTraffic(true);

		// Set thong tin cho mapView
		// Cai dat nut zoom tren man hinh
		mapView.setBuiltInZoomControls(true);
		mapView.displayZoomControls(true);

		mapController = mapView.getController();

		// Lay URI place truyen vao
		// Lay GeoPoint place
		Double geoLat = 0.0;
		Double geoLong = 0.0;

		Bundle extra = getIntent().getExtras();
		fullDataInstance = (FullDataInstance) extra
				.getSerializable("fullinstance");
		placeLabel = fullDataInstance.getLabel();
		geoLat = fullDataInstance.getLatitude();
		geoLong = fullDataInstance.getLongitude();
		isStartFromCommentActivity = extra.getBoolean("from_comment_activity");
		String placeURI = fullDataInstance.getUri();

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
		 * String placeURI = extra.getString("URI"); placeLabel =
		 * extra.getString("label"); geoLat = extra.getDouble("lat", -1.0);
		 * geoLong = extra.getDouble("long", -1.0); type =
		 * extra.getString("type"); iconUrl = extra.getString("iconurl"); iconId
		 * = extra.getInt("iconId"); isStartFromCommentActivity =
		 * extra.getBoolean("from_comment_activity"); if(type != null) { iconUrl
		 * =
		 * OntologyCache.uriOfIcon.get(OntologyCache.hashMapTypeLabelToUri.get(
		 * type+"@"+ServerConfig.LANGUAGE_CODE)).getIconUrl(); iconId =
		 * OntologyCache
		 * .uriOfIcon.get(OntologyCache.hashMapTypeLabelToUri.get(type
		 * +"@"+ServerConfig.LANGUAGE_CODE)).getIconId(); }
		 */

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

		// Set noi dung cho label

		Log.v("TEST", "Geo: " + geoLat + "," + geoLong);
		// Lay toa do hien tai cua SmartPhone thong qua GPS
		if (geoLat == 0.0 || geoLong == 0.0) {
			placeLabel = "Can't found GeoPoint. Show your current place!";
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				geoLat = location.getLatitude();
				geoLong = location.getLongitude();
			} else {
				placeLabel = getResources().getString(R.string.data_not_exist);
				showDialog(SHOW_NO_DATA_DIALOG);
				// Log.d("GET LOCATION ERRORS", "GPS not available!!!");
			}
		}
		placeLabelTextView.setText(placeLabel);
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
		mapController.setZoom(16);
		mapController.animateTo(hustPositionPoint);

		placePoint = new GeoPoint((int) (1E6 * geoLat), (int) (1E6 * geoLong));

		try {
			Location currentLocation = new Location("current");
			currentLocation.setLatitude(currentGeoLat / (1E6));
			currentLocation.setLongitude(currentGeoLong / (1E6));
			Location placeLocation = new Location("Place");
			placeLocation.setLatitude(geoLat);
			placeLocation.setLongitude(geoLong);
			float distance = currentLocation.distanceTo(placeLocation);
			distance = (int) distance / 1000.0f;
			placeLabelTextView.setText(placeLabel + " - " + distance + " km ("
					+ getResources().getString(R.string.the_crow_flies) + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}

		int maxLat = currentGeoLat > (int) (1E6 * geoLat) ? currentGeoLat
				: (int) (1E6 * geoLat);
		int minLat = currentGeoLat < (int) (1E6 * geoLat) ? currentGeoLat
				: (int) (1E6 * geoLat);
		int maxLon = currentGeoLong > (int) (1E6 * geoLong) ? currentGeoLong
				: (int) (1E6 * geoLong);
		int minLon = currentGeoLong < (int) (1E6 * geoLong) ? currentGeoLong
				: (int) (1E6 * geoLong);
		mapController.zoomToSpan(maxLat - minLat, maxLon - minLon);
		mapController.animateTo(new GeoPoint((maxLat + minLat) / 2,
				(maxLon + minLon) / 2));
		// mapController.setCenter(placePoint);

		// Map overlays
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = null;

		if (iconId != 0) {
			Log.v("CLASSURI", iconUrl);
			drawable = getResources().getDrawable(iconId);

		}

		if (drawable == null) {
			drawable = getResources().getDrawable(R.drawable.maps_point);
		}
		PlacesItemizedOverlay itemOverlay = new PlacesItemizedOverlay(drawable,
				this);
		OverlayItem overlay = new OverlayItem(placePoint, placeLabel, placeURI);
		itemOverlay.addOverlay(overlay);

		mapOverlays.add(itemOverlay);

		try {
			Drawable profile = getResources().getDrawable(R.drawable.profile);
			Log.v("POSITION", "hustPositionPoint: "
					+ (hustPositionPoint == null));
			myPosItemOverlay = new PlacesItemizedOverlay(profile, this);
			OverlayItem pOverlay = new OverlayItem(hustPositionPoint,
					"Your current position", "");
			myPosItemOverlay.addOverlay(pOverlay);

			mapOverlays.add(myPosItemOverlay);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			drawPath_x(hustPositionPoint, placePoint, Color.BLUE, mapView);
		} catch (Exception e) {
			String msgtext = "";
			if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.ENGLISH))
				msgtext = "Can't route! Try again, please!!";
			else if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.VIETNAMESE))
				msgtext = "Không tìm được đường. Hãy thử lại!!!!";
			Toast t = Toast.makeText(getBaseContext(), msgtext, 8000);
			t.show();
			e.printStackTrace();
		}
	}

	private void refresh() {
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
		mapController.setZoom(16);
		mapController.animateTo(hustPositionPoint);

		try {
			Location currentLocation = new Location("current");
			currentLocation.setLatitude(currentGeoLat / (1E6));
			currentLocation.setLongitude(currentGeoLong / (1E6));
			Location placeLocation = new Location("Place");
			placeLocation.setLatitude(placePoint.getLatitudeE6() / (1E6));
			placeLocation.setLongitude(placePoint.getLongitudeE6() / (1E6));
			float distance = currentLocation.distanceTo(placeLocation);
			distance = (int) distance / 1000.0f;
			placeLabelTextView.setText(placeLabel + " - " + distance + " km ("
					+ getResources().getString(R.string.the_crow_flies) + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}

		int maxLat = currentGeoLat > (int) (placePoint.getLatitudeE6()) ? currentGeoLat
				: (int) (placePoint.getLatitudeE6());
		int minLat = currentGeoLat < (int) (placePoint.getLatitudeE6()) ? currentGeoLat
				: (int) (placePoint.getLatitudeE6());
		int maxLon = currentGeoLong > (int) (placePoint.getLongitudeE6()) ? currentGeoLong
				: (int) (placePoint.getLongitudeE6());
		int minLon = currentGeoLong < (int) (placePoint.getLongitudeE6()) ? currentGeoLong
				: (int) (placePoint.getLongitudeE6());
		mapController.zoomToSpan(maxLat - minLat, maxLon - minLon);
		mapController.animateTo(new GeoPoint((maxLat + minLat) / 2,
				(maxLon + minLon) / 2));
		// mapController.setCenter(placePoint);

		// Map overlays
		List<Overlay> mapOverlays = mapView.getOverlays();
		mapOverlays.clear();
		try {
			Drawable profile = getResources().getDrawable(R.drawable.profile);
			// Log.v("POSITION", "hustPositionPoint: " + (hustPositionPoint ==
			// null));
			myPosItemOverlay = new PlacesItemizedOverlay(profile, this);
			OverlayItem pOverlay = new OverlayItem(hustPositionPoint,
					"Your current position", "");
			myPosItemOverlay.addOverlay(pOverlay);

			mapOverlays.add(myPosItemOverlay);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			drawPath_x(hustPositionPoint, placePoint, Color.BLUE, mapView);
		} catch (Exception e) {
			String msgtext = "";
			if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.ENGLISH))
				msgtext = "Can't route! Try again, please!!";
			else if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.VIETNAMESE))
				msgtext = "Không tìm được đường. Hãy thử lại!!!!";
			Toast t = Toast.makeText(getBaseContext(), msgtext, 8000);
			t.show();
			e.printStackTrace();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuIf = getMenuInflater();
		menuIf.inflate(R.menu.map_view_option_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Chon setting
		if (item.getItemId() == R.id.map_setting) {
			MapsSettingDialog settingDialog = new MapsSettingDialog(
					ShowPlaceOnMapsActivity.this, this.mapView);
			settingDialog.show();
		} else if (item.getItemId() == R.id.refresh) {
			if (detectLocationTask != null) {
				detectLocationTask.cancel(true);
				detectLocationTask = null;
			}
			detectLocationTask = new DetectLocationTask(
					ShowPlaceOnMapsActivity.this);
			detectLocationTask.execute();
		} else if (item.getItemId() == R.id.mapquess) {
			XmlAdapter.useMapQuess(getBaseContext(), true);
			Intent i = new Intent(getBaseContext(), ShowPlaceOnOsmap.class);
			i.putExtras(getIntent());
			startActivity(i);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public void drawPath_x(final GeoPoint from, final GeoPoint to,
			final int color, final MapView mMapView01) throws IOException,
			JSONException {

		new AsyncTask<Void, Void, Void>() {
			private InputStream is = null;
			private String result = "";

			@Override
			protected Void doInBackground(Void... params) {
				// tao url
				StringBuilder urlString = new StringBuilder();
				urlString
						.append("http://maps.googleapis.com/maps/api/directions/json?origin=");
				urlString
						.append(Double.toString((double) from.getLatitudeE6() / 1.0E6));
				urlString.append(",");
				urlString
						.append(Double.toString((double) from.getLongitudeE6() / 1.0E6));
				urlString.append("&destination=");// to
				urlString
						.append(Double.toString((double) to.getLatitudeE6() / 1.0E6));
				urlString.append(",");
				urlString
						.append(Double.toString((double) to.getLongitudeE6() / 1.0E6));

				urlString.append("&sensor=true");

				if (ServerConfig.LANGUAGE_CODE.equals("en")) {
					urlString.append("&language=en");
				} else {
					urlString.append("&language=vi");
				}

				Log.v("MAP", urlString.toString());

				// http post
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(urlString.toString());
					HttpResponse response = httpclient.execute(httpGet);
					HttpEntity entity = response.getEntity();
					is = entity.getContent();
				} catch (Exception e) {
					e.printStackTrace();
					// Log.e("log_tag",
					// "Error in http connection "+e.toString());
				}

				BufferedReader reader = null;
				try {
					// BufferedReader reader = new BufferedReader(new
					// InputStreamReader(is,"iso-8859-1"),8);
					reader = new BufferedReader(new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();
					result = sb.toString();
				} catch (Exception e) {
					// Log.e("log_tag",
					// "Error converting result "+e.toString());
				} finally {
					if (reader != null)
						try {
							reader.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void asdsds) {
				// convert response to string

				// Log.v("", "heheh 2 : "+result);

				// //request
				// URL url = new URL(urlString.toString());
				// HttpURLConnection httpConnection =
				// (HttpURLConnection)url.openConnection();
				// StringBuilder response=new StringBuilder();
				// if (httpConnection.getResponseCode() ==
				// HttpURLConnection.HTTP_OK)
				// {
				// BufferedReader input = new BufferedReader(new
				// InputStreamReader(httpConnection.getInputStream()));
				// String strLine = null;
				// while ((strLine = input.readLine()) != null)
				// {
				// response.append(strLine);
				// }
				// input.close();
				// }
				//
				// String jsonOutput = response.toString();

				// hien thi duong di
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray routeArray = jsonObject.getJSONArray("routes");
					String polyLine = routeArray.getJSONObject(0)
							.getJSONObject("overview_polyline")
							.getString("points");
					List<GeoPoint> listPolyLine = decodePoly(polyLine);

					mMapView01.getOverlays().add(new MyOverLay(from, from, 1));
					int w = (int) (5 * ShowPlaceOnMapsActivity.this
							.getResources().getDisplayMetrics().density);
					for (int i = 0; i < listPolyLine.size() - 1; i++) {
						GeoPoint start = new GeoPoint(listPolyLine.get(i)
								.getLatitudeE6(), listPolyLine.get(i)
								.getLongitudeE6());
						GeoPoint end = new GeoPoint(listPolyLine.get(i + 1)
								.getLatitudeE6(), listPolyLine.get(i + 1)
								.getLongitudeE6());
						mMapView01.getOverlays().add(
								new MyOverLay(start, end, 2, color, w));

					}
					mMapView01.getOverlays().add(
							new MyOverLay(
									listPolyLine.get(listPolyLine.size() - 1),
									listPolyLine.get(listPolyLine.size() - 1),
									3));
					// hien thi chi dan
					Drawable drawable1 = mMapView01.getResources().getDrawable(
							R.drawable.way_turn_mark);
					MyItemizedOverlay itemizedOverlay1 = new MyItemizedOverlay(
							drawable1, mMapView01);

					JSONArray stepArray = routeArray.getJSONObject(0)
							.getJSONArray("legs").getJSONObject(0)
							.getJSONArray("steps");
					for (int j = 0; j < stepArray.length(); j++) {
						String htmlInstruction = null;
						String distance = null;

						htmlInstruction = stepArray.getJSONObject(j).getString(
								"html_instructions");
						// Spanned sp=Html.fromHtml(htmlInstruction);
						distance = stepArray.getJSONObject(j)
								.getJSONObject("distance").getString("text");

						String lat = stepArray.getJSONObject(j)
								.getJSONObject("start_location")
								.getString("lat");
						String lng = stepArray.getJSONObject(j)
								.getJSONObject("start_location")
								.getString("lng");
						GeoPoint temp = new GeoPoint(
								(int) (Double.parseDouble(lat) * 1E6),
								(int) (Double.parseDouble(lng) * 1E6));
						itemizedOverlay1.addOverlay(new OverlayItem(temp, Html
								.fromHtml(htmlInstruction)
								+ "("
								+ distance
								+ ")", ""));

					}
					mMapView01.getOverlays().add(itemizedOverlay1);
					// add by dungct - tu dong refresh de ve duong di - khong
					// can cham
					// vao ban do.
					mapController.animateTo(new GeoPoint(
							(from.getLatitudeE6() + to.getLatitudeE6()) / 2,
							(from.getLongitudeE6() + to.getLongitudeE6()) / 2));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.execute();

	}

	private List<GeoPoint> decodePoly(String encoded) {

		List<GeoPoint> poly = new ArrayList<GeoPoint>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			GeoPoint p = new GeoPoint((int) (((double) lat / 1E5) * 1E6),
					(int) (((double) lng / 1E5) * 1E6));
			poly.add(p);
		}

		return poly;
	}

	protected Dialog onCreateDialog(final int pID) {

		switch (pID) {
		case SHOW_NO_DATA_DIALOG: {
			return new AlertDialog.Builder(this)
					.setCancelable(false)
					.setMessage(
							getResources().getString(R.string.data_not_update))
					.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface pDialog,
										final int pWhich) {
									finish();
								}
							}).create();
		}
		default:
			return super.onCreateDialog(pID);
		}
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
