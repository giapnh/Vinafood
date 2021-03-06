package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.Location;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.customViewAdapter.ArrayPlaceSimpleAdapter;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PlaceSearchResultActivity extends Activity {

	ArrayList<String> listInstanceURI;
	ArrayList<ArrayList<String>> queryResult;
	VtioCoreService service = new VtioCoreService();
	private SoapServiceProxy<ICoreService> soapServiceProxy;

	ListView listResultView;
	ArrayList<FullDataInstance> listPlaceDataSimple;
	boolean isReasoning = true;

	ArrayPlaceSimpleAdapter arrayPlaceSimpleAdapter;
	LoadAllInstanceTask loadAllInstanceTask;

	ImageView switchGridList;
	boolean isGrid = false;
	public final String CLASS_URI = NameSpace.vtio + "Dining-Service";
	// Data for load
	String keyword;
	Float radius = 0f;
	String cuisineStype = "";
	String classUri = "";
	String purpose = "";
	boolean hasConstraint = false;

	TextView noResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		XmlAdapter.synConfig(this);
		setContentView(R.layout.result_place_search_layout);
		noResult = (TextView) findViewById(R.id.no_search_result);
		// Get extras data
		Bundle extra = this.getIntent().getExtras();
		keyword = extra.getString("keyword");
		radius = extra.getFloat("radius", 0f);
		cuisineStype = extra.getString("cuisineStyle");
		classUri = extra.getString("classUri");
		purpose = extra.getString("purpose");
		hasConstraint = extra.getBoolean("hasConstraint");

		soapServiceProxy = new SoapServiceProxy<ICoreService>(
				ICoreService.class, ServerConfig.SERVICE_NAMESPACE,
				ServerConfig.getWSDLURL());
		listResultView = (ListView) findViewById(R.id.listResultView);
		loadAllInstanceTask = new LoadAllInstanceTask();
		loadAllInstanceTask.execute();
		TextView subMenuTitle = (TextView) findViewById(R.id.txt_sub_menu);
		subMenuTitle.setText("Kết quả");
	}

	class LoadAllInstanceTask extends AsyncTask<Void, Void, Void> {
		Dialog progressLayout;

		// TextView waitTextView;
		@Override
		protected Void doInBackground(Void... arg0) {
			listPlaceDataSimple = new ArrayList<FullDataInstance>();
			if (radius == 0f) {
			} else {
				arrayPlaceSimpleAdapter = new ArrayPlaceSimpleAdapter(
						PlaceSearchResultActivity.this,
						R.layout.place_item_layout, listPlaceDataSimple);
				try {
					if (!hasConstraint) {
						arrayPlaceSimpleAdapter.loadPlaceDataInFirst(CLASS_URI,
								Location.getInstance().getLatitude(), Location
										.getInstance().getLongtitude(), radius,
								false, keyword);
					} else {
						arrayPlaceSimpleAdapter.loadPlaceDataInFirst(classUri,
								Location.getInstance().getLatitude(),//
								Location.getInstance().getLongtitude(),//
								radius, false, keyword, //
								"", "", cuisineStype, 0, 0);// FIXME
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		protected void onCancelled() {
		}

		@Override
		protected void onPreExecute() {
			progressLayout = new Dialog(PlaceSearchResultActivity.this,
					android.R.style.Theme_Translucent_NoTitleBar);
			progressLayout.setContentView(R.layout.loading_layout);
			progressLayout.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			progressLayout.dismiss();
			if (arrayPlaceSimpleAdapter.listPlaceDataSimple.size() == 0) {
				noResult.setVisibility(View.VISIBLE);
			} else {
				listResultView.setAdapter(arrayPlaceSimpleAdapter);
				listResultView.setVisibility(View.VISIBLE);
			}
		}
	}

	public void onStop() {
		super.onStop();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
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
		case R.id.btnSearch:
			Intent intent = new Intent(this, DinningServiceSearch.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
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

	public void onBack(View v) {
		this.finish();
	}

}
