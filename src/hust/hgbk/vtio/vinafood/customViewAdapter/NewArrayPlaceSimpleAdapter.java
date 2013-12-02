package hust.hgbk.vtio.vinafood.customViewAdapter;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.constant.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.customview.PlaceItemView;
import hust.hgbk.vtio.vinafood.main.NewInstanceDetails;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.ICoreService;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;
import java.util.HashMap;

import ken.soapservicelib.proxy.SoapServiceProxy;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

public class NewArrayPlaceSimpleAdapter extends ArrayAdapter<FullDataInstance> {
	final static int LIMIT = 5;
	int offsetOnList = 0;
	Context context;
	ArrayList<FullDataInstance> listPlaceDataSimple;
	VtioCoreService services = new VtioCoreService();
	HashMap<Integer, Boolean> hashMapView;
	int countLoopQuery = 0;
	private int currentPosition = 0;
	boolean cache = false;
	boolean uriReasoning = false;
	boolean isStop = false;
	String queryString;
	LoadInstanceTask loadInstanceTask;
	SQLiteAdapter sqLiteAdapter;
	private SoapServiceProxy<ICoreService> soapServiceProxy;

	public NewArrayPlaceSimpleAdapter(Context context, int textViewResourceId,
			ArrayList<FullDataInstance> objects, String queryString,
			boolean cache, boolean uriReasoning) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.queryString = queryString;
		soapServiceProxy = new SoapServiceProxy<ICoreService>(
				ICoreService.class, ServerConfig.SERVICE_NAMESPACE,
				ServerConfig.getWSDLURL());

		// Add all fulldatainstance to list
		listPlaceDataSimple = objects;
		this.sqLiteAdapter = SQLiteAdapter.getInstance(context);
		this.sqLiteAdapter.checkAndCreateDatabase();
		hashMapView = new HashMap<Integer, Boolean>();
		this.cache = cache;
		this.uriReasoning = uriReasoning;
		doLoadInstanceTask(0, cache, uriReasoning);
		notifyDataSetChanged();
	}

	public NewArrayPlaceSimpleAdapter(Context context, int textViewResourceId,
			ArrayList<FullDataInstance> objects, SQLiteAdapter sqLiteAdapter) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.isStop = true;
		soapServiceProxy = new SoapServiceProxy<ICoreService>(
				ICoreService.class, ServerConfig.SERVICE_NAMESPACE,
				ServerConfig.getWSDLURL());

		// Add all fulldatainstance to list
		listPlaceDataSimple = objects;
		this.sqLiteAdapter = sqLiteAdapter;
		hashMapView = new HashMap<Integer, Boolean>();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		currentPosition = position;
		final PlaceItemView view;
		view = new PlaceItemView(context);
		if (position == getCount() - 1) {
			if (!isStop
					&& (hashMapView.get(position) == null || !hashMapView
							.get(position))) {
				if (loadInstanceTask != null) {
					loadInstanceTask.cancel(true);
				}
				loadInstanceTask = new LoadInstanceTask(queryString,
						view.getProgressBar());
				loadInstanceTask.execute();
			}

		}

		final FullDataInstance placeItem = listPlaceDataSimple.get(position);
		final String uri = placeItem.getUri();
		final String label = placeItem.getLabel() + "("
				+ placeItem.getDistanceString() + ")";
		String type = placeItem.getType();
		String distance = placeItem.getDistanceString();
		final String address = placeItem.getAddress();
		final String hasAbstract = placeItem.getAbstractInfo();
		final String imageURL = placeItem.getImageURL();
		final double longitude = placeItem.getLongitude();
		final double latitude = placeItem.getLatitude();

		view.getLabelTextView().setText(label);
		view.getLabelTextView().setSelected(true);
		view.getTypeTextView().setText(type);
		view.getAddressTextView().setText(address);
		String iconUrl1 = "";
		int iconId1 = 0;

		try {
			// lva: chinh lai cach thuc lay url

			iconUrl1 = OntologyCache.uriOfIcon.get(
					OntologyCache.hashMapTypeLabelToUri.get(type + "@"
							+ ServerConfig.LANGUAGE_CODE)).getIconUrl();
			iconId1 = OntologyCache.uriOfIcon.get(
					OntologyCache.hashMapTypeLabelToUri.get(type + "@"
							+ ServerConfig.LANGUAGE_CODE)).getIconId();
			// Log.d(getClass().getSimpleName(), "Dong 172"+ iconUrl1 +
			// String.valueOf(iconId1));
		} catch (Exception e) {
		}

		if (imageURL.equals("") || imageURL.contains("anyType")) {

			try {
				// String data =
				// "<img src=\""+iconUrl1+"\" style='border: solid #bb3333 1px; position: absolute;   top: 0px; left: 0px; background-color:transparent;'  width='32px'/>";
				// //String data = "<img src=\""+imageURL+"\"/>";
				// Log.d("WEB VIEW DATA ", "DATA: "+data);
				// view.getImageWebView().loadData(data, "text/html", "utf-8");
				view.getImageWebView().setVisibility(View.GONE);
				view.getImageView().setVisibility(View.VISIBLE);
				// view.getImageView().setImageResource(iconId1);
				view.getImageView().setScaleType(ScaleType.FIT_XY);
			} catch (Exception e) {
			}

		} else {
			view.getImageView().setVisibility(View.GONE);
			view.getImageWebView().setVisibility(View.VISIBLE);
			String data = "<div style='border: solid #bb3333  1px; position: absolute;   top: 0px; left: 0px;  overflow:hidden;  '><img src=\""
					+ imageURL
					+ "\" style=' background-color:transparent;'width='"
					+ context.getResources().getDimension(R.dimen.layx200)
					+ "px;'height = '"
					+ context.getResources().getDimension(R.dimen.layy200)
					+ "px'    /></div>";
			try {
				view.getImageWebView().loadData(data, "text/html", "utf-8");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		final String iconUrl = iconUrl1;
		final int iconId = iconId1;

		final int rate = placeItem.getRatingNum();
		view.getRatingView().removeAllViews();
		for (int i = 0; i < rate; i++) {
			ImageView ratingIcon = new ImageView(this.context);
			ratingIcon.setImageResource(R.drawable.rating_icon);
			view.getRatingView().addView(ratingIcon);
		}
		for (int i = 0; i < (5 - rate); i++) {
			ImageView ratingIcon = new ImageView(this.context);
			ratingIcon.setImageResource(R.drawable.rating_disable_icon);
			view.getRatingView().addView(ratingIcon);
		}
		view.setClickable(true);
		hashMapView.put(position, true);

		view.findViewById(R.id.for_click_view).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								NewInstanceDetails.class);
						Bundle bundle = new Bundle();
						bundle.putString("abstractInfo",
								placeItem.getAbstractInfo());
						bundle.putString("address", placeItem.getAddress());
						bundle.putString("imageURL", placeItem.getImageURL());
						bundle.putString("label", placeItem.getLabel());
						bundle.putDouble("latitude", placeItem.getLatitude());
						bundle.putDouble("longitude", placeItem.getLongitude());
						bundle.putString("location", placeItem.getLocation());
						bundle.putString("phone", placeItem.getPhone());
						bundle.putInt("ratingNum", placeItem.getRatingNum());
						bundle.putString("type", placeItem.getType());
						bundle.putString("uri", placeItem.getUri());
						bundle.putString("wellKnown", placeItem.getWellKnown());
						intent.putExtras(bundle);
						context.startActivity(intent);
					}
				});

		return view;
	}

	class LoadInstanceTask extends AsyncTask<Void, Void, Void> {
		// int offset;
		ProgressBar progressBar;

		public LoadInstanceTask(String query, ProgressBar progressBar) {
			// this.offset = offset;
			this.progressBar = progressBar;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			doLoadInstanceTask(offsetOnList, cache, uriReasoning);
			return null;
		}

		protected void onCancelled() {
			Log.v("KEN", "Load result cancel");
		}

		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Void result) {
			progressBar.setVisibility(View.GONE);
			// setTitleHelpResult(queryResult.size());
			notifyDataSetChanged();
		}
	}

	public void doLoadInstanceTask(int offset, boolean cache,
			boolean uriReasoning) {
		String query = queryString + " LIMIT " + LIMIT + " OFFSET " + offset;
		long a = System.currentTimeMillis();

		FullDataInstance[] listPlaces = soapServiceProxy.getiComCoreService()
				.getFullDataInstace(query, ServerConfig.LANGUAGE_CODE,
						uriReasoning, ServerConfig.VTIO_REPOSITORY_KEY);
		for (FullDataInstance fullDataInstance : listPlaces) {
			listPlaceDataSimple.add(fullDataInstance);
		}

		if (listPlaces.length < LIMIT - 2) {
			isStop = true;
		}
		if (listPlaces.length > 0) {
			offsetOnList = offsetOnList + LIMIT;

		}
		Log.v("TIME", "loaded uri: " + (System.currentTimeMillis() - a));

		Log.v("TIME", "size: " + listPlaceDataSimple.size() + "query: " + query);

	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}
	// public interface OnLastPositionListener {
	// public void onLastPosition(int position);
	// }
	//
	// public void setOnLastPositionListener(
	// OnLastPositionListener onLastPositionListener) {
	// this.onLastPositionListener = onLastPositionListener;
	// }

}
