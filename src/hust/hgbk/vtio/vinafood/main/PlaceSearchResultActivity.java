package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.Location;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.customViewAdapter.ArrayPlaceSimpleAdapter;
import hust.hgbk.vtio.vinafood.customViewAdapter.NewArrayPlaceSimpleAdapter;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.ICoreService;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;

import ken.soapservicelib.proxy.SoapServiceProxy;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.ImageView;

public class PlaceSearchResultActivity extends Activity {
	String titleSearch;
	String queryString;
	Float radius = 0f;
	ArrayList<String> listInstanceURI;
	ArrayList<ArrayList<String>> queryResult;
	VtioCoreService service = new VtioCoreService();
	private SoapServiceProxy<ICoreService> soapServiceProxy;

	ListView listResultView;
	ArrayList<FullDataInstance> listPlaceDataSimple;
	boolean isReasoning = true;

	String message;
	NewArrayPlaceSimpleAdapter newArrayPlaceSimpleAdapter;
	ArrayPlaceSimpleAdapter arrayPlaceSimpleAdapter;
	LoadAllInstanceTask loadAllInstanceTask;

	ImageView switchGridList;
	boolean isGrid = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		XmlAdapter.synConfig(this);
		setContentView(R.layout.result_place_search_layout);
		Bundle extra = this.getIntent().getExtras();
		queryString = extra.getString("QueryString");
		radius = extra.getFloat("radius", 0f);
		message = extra.getString("message");
		soapServiceProxy = new SoapServiceProxy<ICoreService>(
				ICoreService.class, ServerConfig.SERVICE_NAMESPACE,
				ServerConfig.getWSDLURL());

		listResultView = (ListView) findViewById(R.id.listResultView);
		// switchGridList = (ImageView) findViewById(R.id.list_grid);

		loadAllInstanceTask = new LoadAllInstanceTask();
		loadAllInstanceTask.execute();
	}

	class LoadAllInstanceTask extends AsyncTask<Void, Void, Void> {
		Dialog progressLayout;
		// TextView waitTextView;
		@Override
		protected Void doInBackground(Void... arg0) {
			listPlaceDataSimple = new ArrayList<FullDataInstance>();
			if (radius == 0f) {
				// tham so cuoi cung la Cache On hay Off.
				newArrayPlaceSimpleAdapter = new NewArrayPlaceSimpleAdapter(
						PlaceSearchResultActivity.this,
						R.layout.place_item_layout, listPlaceDataSimple,
						queryString, false, isReasoning);
			} else {
				try {
					queryResult = service.executeQueryWithGeoConstrains(
							queryString, true, 0, Location.getInstance()
									.getLatitude(), Location.getInstance()
									.getLongtitude(), radius);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				arrayPlaceSimpleAdapter = new ArrayPlaceSimpleAdapter(
						PlaceSearchResultActivity.this,
						R.layout.place_item_layout, listPlaceDataSimple,
						queryResult);
			}
			return null;
		}

		protected void onCancelled() {
		}
 
		@Override
		protected void onPreExecute() {
			// msgTextView = (TextView) findViewById(R.id.progress_bar_text);
			// waitTextView = (TextView) findViewById(R.id.ads_textview);
			progressLayout = new Dialog(PlaceSearchResultActivity.this,
					android.R.style.Theme_Translucent_NoTitleBar);
			progressLayout.setContentView(R.layout.loading_layout);
			progressLayout.show();
			// waitTextView.setText(AdsInfoSentences.getRandomAdsSentence());
			// msgTextView.setText(message);
		}

		@Override
		protected void onPostExecute(Void result) {
			if (newArrayPlaceSimpleAdapter != null) {
				listResultView.setAdapter(newArrayPlaceSimpleAdapter);
			} else {
				listResultView.setAdapter(arrayPlaceSimpleAdapter);
			}
			progressLayout.dismiss();
			listResultView.setVisibility(View.VISIBLE);
		}
	}

	public void onStop() {
		super.onStop();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			super.onKeyDown(keyCode, event);
		}

		return super.onKeyDown(keyCode, event);
	}

	public String getQueryInstanceWithGeoConstrains(String classUri,
			double geoLat, double geoLon, float radius, boolean hasPreference) {

		String queryString = "select distinct ?place where{"
				+ " GEO OBJECT "
				+ " SUBTYPE 'http://franz.com/ns/allegrograph/3.0/geospatial/spherical/degrees/-180.0/180.0/-90.0/90.0/5.0'"
				+ " HAVERSINE (POINT(" + geoLon + ", " + geoLat + "), "
				+ radius + " KM) {" + " ?place vtio:hasGeoPoint ?loc."
				+ " ?place rdf:type <" + classUri + ">."
				+ " }  where{       	       ";
		if (OntologyCache.preferUser.size() > 0 && hasPreference) {
			queryString = queryString + " ?place  vtio:relatedToTopic ?t. {";
			queryString = queryString + " { ?t rdf:type <"
					+ OntologyCache.preferUser.get(0).getUri() + "> } ";
			try {
				for (int i = 1; i < OntologyCache.preferUser.size(); i++) {
					queryString = queryString + "UNION { ?t rdf:type <"
							+ OntologyCache.preferUser.get(i).getUri() + ">. }";
				}
			} catch (Exception e) {
			}
			queryString = queryString + " } ";
		}
		queryString = queryString
				+ " }} ORDER BY geo:haversine-km(?loc,(POINT(" + geoLon + ", "
				+ geoLat + ")) )";
		Log.v("QUERY", queryString);
		return queryString;
	}

	protected void showhelpDialog(String message) {
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.help))
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.close),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
	}

	public void onBack(View v) {
		finish();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show_all_button:
			if (listPlaceDataSimple != null && listPlaceDataSimple.size() > 0) {
				Intent intent = new Intent(
						PlaceSearchResultActivity.this,
						XmlAdapter
								.getShowAllOnMapActivity(PlaceSearchResultActivity.this));
				int currentPosition = 0;
				if (arrayPlaceSimpleAdapter != null) {
					currentPosition = arrayPlaceSimpleAdapter
							.getCurrentPosition();
				} else if (newArrayPlaceSimpleAdapter != null) {
					currentPosition = newArrayPlaceSimpleAdapter
							.getCurrentPosition();
				}
				int begin = (currentPosition - 3) > 0 ? (currentPosition - 3)
						: 0;
				int finish = (currentPosition + 3) < listPlaceDataSimple.size() ? (currentPosition + 3)
						: (listPlaceDataSimple.size());
				for (int i = begin; i < finish; i++) {
					intent.putExtra("URI" + (i - begin), listPlaceDataSimple
							.get(i).getUri());
					intent.putExtra("label" + (i - begin), listPlaceDataSimple
							.get(i).getLabel());

					intent.putExtra("longtitude" + (i - begin), Double
							.toString(listPlaceDataSimple.get(i).getLatitude()));
					intent.putExtra("latitude" + (i - begin), Double
							.toString(listPlaceDataSimple.get(i).getLatitude()));
					intent.putExtra("type" + (i - begin), listPlaceDataSimple
							.get(i).getType());
				}

				if (radius == 0f) {
					intent.putExtra("range", 0f);
				} else {
					intent.putExtra("range", radius);
				}
				startActivity(intent);
			}
			break;
		// case R.id.list_grid:
		// if (isGrid) {
		// isGrid = false;
		// listResultView.setNumColumns(1);
		// switchGridList.setBackgroundResource(R.drawable.grid);
		// } else {
		// isGrid = true;
		// listResultView.setNumColumns(2);
		// switchGridList.setBackgroundResource(R.drawable.list);
		// }
		// break;
		}
	}
}
