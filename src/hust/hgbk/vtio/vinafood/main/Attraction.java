package hust.hgbk.vtio.vinafood.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.AdsInfoSentences;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.customViewAdapter.NewArrayPlaceSimpleAdapter;
import hust.hgbk.vtio.vinafood.ontology.simple.PlaceDataSimple;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;

public class Attraction extends Activity {
	String titleSearch;
	String queryString;
	Float radius = 0f;
	ArrayList<String> listInstanceURI;
	ArrayList<ArrayList<String>> queryResult;
	VtioCoreService service = new VtioCoreService();
	// TextView titleSearchTextView;
	ListView listResultView;
	// ArrayList<InstanceData> listPlaceInstance;
	ArrayList<FullDataInstance> listPlaceDataSimple;

	ProgressDialog progressDialog;
	String message;
	NewArrayPlaceSimpleAdapter arrayPlaceSimpleAdapter;
	LoadAllInstanceTask loadAllInstanceTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		XmlAdapter.synConfig(this);
		setContentView(R.layout.result_place_search_layout);

		Bundle extra = this.getIntent().getExtras();
		queryString = "select distinct ?uri where {"
				+ " ?uri rdf:type vtio:Place." + " ?uri vtio:isWellKnown true."
				+ " ?uri vtio:hasLocation ?addresscity .  "
				+ " ?addresscity vtio:isPartOf <" + ServerConfig.currentCityUri
				+ ">.}";
		radius = 0f;
		message = getResources().getString(R.string.please_wait);
		// Log.v("TEST2", ""+ radius);
		// titleSearchTextView = (TextView)
		// findViewById(R.id.serchTitleTextView);
		listResultView = (ListView) findViewById(R.id.listResultView);

		/**
		 * Load ngam cac ket qua
		 */
		loadAllInstanceTask = new LoadAllInstanceTask();
		loadAllInstanceTask.execute();
		//
		Animation slide = AnimationUtils.loadAnimation(this,
				R.anim.slide_top_intro);
		Button showAllButton = (Button) findViewById(R.id.show_all_button);
		showAllButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listPlaceDataSimple.size() > 0) {
					Intent intent = new Intent(Attraction.this, XmlAdapter
							.getShowAllOnMapActivity(Attraction.this));
					int currentPosition = 0;
					if (arrayPlaceSimpleAdapter != null) {
						currentPosition = arrayPlaceSimpleAdapter
								.getCurrentPosition();
					}
					Log.v("POSITION", "current: " + currentPosition);
					int begin = (currentPosition - 3) > 0 ? (currentPosition - 3)
							: 0;
					int finish = (currentPosition + 3) < listPlaceDataSimple
							.size() ? (currentPosition + 3)
							: (listPlaceDataSimple.size());
					for (int i = begin; i < finish; i++) {
						Log.v("POSITION", listPlaceDataSimple.get(i).getUri());
						intent.putExtra("URI" + (i - begin),
								listPlaceDataSimple.get(i).getUri());
						intent.putExtra("label" + (i - begin),
								listPlaceDataSimple.get(i).getLabel());
						// added by Dungct

						intent.putExtra("longtitude" + (i - begin), Double
								.toString(listPlaceDataSimple.get(i)
										.getLongitude()));
						intent.putExtra("latitude" + (i - begin), Double
								.toString(listPlaceDataSimple.get(i)
										.getLatitude()));
						// end added

						intent.putExtra("type" + (i - begin),
								listPlaceDataSimple.get(i).getType());

					}
					if (radius == 0f) {
						intent.putExtra("range", 2f);
					} else {
						intent.putExtra("range", radius);
					}
					startActivity(intent);
				}
			}
		});
	}

	class LoadAllInstanceTask extends AsyncTask<Void, Void, Void> {
		RelativeLayout progressLayout;
		TextView msgTextView;
		TextView waitTextView;

		@Override
		protected Void doInBackground(Void... arg0) {
			// Log.d("QUERY-TIME",
			// "EXECUTE QUERY START <"+TimeFactory.GET_CURRENT_TIME()+">");
			listPlaceDataSimple = new ArrayList<FullDataInstance>();
			// dungct: truy van tim wellKnown - Place - bat suy dien
			arrayPlaceSimpleAdapter = new NewArrayPlaceSimpleAdapter(
					Attraction.this, R.layout.place_item_layout,
					listPlaceDataSimple, queryString, true, true);
			return null;
		}

		protected void onCancelled() {
			Log.v("KEN", "Load result cancel");
		}

		@Override
		protected void onPreExecute() {
			// progressDialog = new ProgressDialog(Attraction.this);
			// if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.ENGLISH))
			// progressDialog.setMessage(message);
			// else if
			// (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.VIETNAMESE))
			// progressDialog.setMessage(message);
			// progressDialog.show();
			msgTextView = (TextView) findViewById(R.id.progress_bar_text);
			waitTextView = (TextView) findViewById(R.id.ads_textview);
			progressLayout = (RelativeLayout) findViewById(R.id.progress_layout);
			progressLayout.setVisibility(View.VISIBLE);
			waitTextView.setText(AdsInfoSentences.getRandomAdsSentence());
			msgTextView.setText(message);
		}

		@Override
		protected void onPostExecute(Void result) {
			/*
			 * ArrayPlaceAdapter arrayPlaceAdapter = new
			 * ArrayPlaceAdapter(SearchResultActivity.this,
			 * R.layout.place_item_layout, listPlaceInstance);
			 * titleSearchTextView
			 * .setText("Has "+listPlaceInstance.size()+" instances matched");
			 * listResultView.setAdapter(arrayPlaceAdapter);
			 */

			// titleSearchTextView.setVisibility(View.GONE);

			listResultView.setAdapter(arrayPlaceSimpleAdapter);

			progressLayout.setVisibility(View.GONE);
			listResultView.setVisibility(View.VISIBLE);
			// progressDialog.dismiss();
		}
	}

	// public void recycle(){
	// loadAllInstanceTask.cancel(true);
	// listInstanceURI = null;
	// queryResult = null;
	// service = null;
	// listPlaceDataSimple = null;
	// progressDialog = null;
	// if (arrayPlaceSimpleAdapter != null){
	// // arrayPlaceSimpleAdapter.recycle();
	// arrayPlaceSimpleAdapter = null;
	// }
	//
	// }

	public void onStop() {
		super.onStop();
		Log.v("KEN", "Stop result activity");

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// loadAllInstanceTask.cancel(true);
			// arrayPlaceSimpleAdapter.cancelAsynTask();
			Log.v("KEN", "Clear variable");
			super.onKeyDown(keyCode, event);
		}

		return super.onKeyDown(keyCode, event);
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
}