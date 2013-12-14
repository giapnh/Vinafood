package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.config.log;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.constant.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

public class PlaceDetails extends Activity {
	final Activity activity = this;
	Context context;
	TextView waitTextView;
	VtioCoreService services;
	// String instanceURI = "";
	// String instanceLabel = "";
	// String classURI = "";
	// InstanceData data = new InstanceData();

	// ArrayList<PropertyWithValue> listPropertiesWithValue = new
	// ArrayList<PropertyWithValue>();

	FullDataInstance dataSimple;

	TextView textViewTitle;
	ListView listViewProperties;

	Button btnBack, btnNext;
	ViewFlipper viewFlipper;

	// String[] listIgnoreProperty = {"hasGeoPoint","hasMedia","hasLocation"};
	String[] listIgnoreProperty = { "hasLongtitude", "hasGeoPoint",
			"hasLatitude", "hasMedia", "hasLocation" };
	// String[] listIgnoreEnLabel = {"has longtitude",
	// "has Geo Point (lat-lon)", "has latitude", "has location"};
	// String[] listIgnoreVnLabel = {"có kinh độ", "có Geo Point", "có vĩ độ",
	// "có địa chỉ tại"};
	// String[] listIgnoreProperty =
	// {"hasGeoPoint","hasLatitude","hasMedia","hasLocation"};

	private final int SHOW_NO_DATA_DIALOG = 0;

	private String latStr = "";
	private String lonStr = "";
	private String imageStr = "";
	private TextView noDataUpdate;
	SQLiteAdapter sqLiteAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.place_details_layout);
		this.context = PlaceDetails.this;
		Bundle extra = getIntent().getExtras();
		// instanceURI = extra.getString("instanceURI");
		// instanceLabel = extra.getString("instanceLabel");

		dataSimple = new FullDataInstance();
		dataSimple.setUri(extra.getString("uri"));
		dataSimple.setAbstractInfo(extra.getString("abstractInfo"));
		dataSimple.setAddress(extra.getString("address"));
		dataSimple.setImageURL(extra.getString("imageURL"));
		dataSimple.setLabel(extra.getString("label"));
		dataSimple.setLatitude(extra.getDouble("latitude"));
		dataSimple.setLongitude(extra.getDouble("longitude"));
		dataSimple.setLocation(extra.getString("location"));
		dataSimple.setPhone(extra.getString("phone"));
		dataSimple.setRatingNum(extra.getInt("ratingNum"));
		dataSimple.setType(extra.getString("type"));
		dataSimple.setUri(extra.getString("uri"));
		dataSimple.setWellKnown(extra.getString("wellKnown"));

		services = new VtioCoreService();
		// float longitude = -1;
		// float latitude = -1;
		// ArrayList<PropertyWithValue> listTemp = new
		// ArrayList<PropertyWithValue>();
		// new LoadInstanceDetails(listTemp).execute();
		this.sqLiteAdapter = SQLiteAdapter.getInstance(context);
		this.sqLiteAdapter.checkAndCreateDatabase();
		// Save this location information to RecentView data
		sqLiteAdapter.addPlaceToRecentViewTable(dataSimple);
		loadData();
	}

	private void loadData() {
		ToggleButton favoristLay = (ToggleButton) findViewById(R.id.btn_bookmark);

		// LinearLayout favoristLay = (LinearLayout)
		// findViewById(R.id.btnFavorist);
		// final TextView favoristText = (TextView) favoristLay
		// .findViewById(R.id.btnText);
		if (sqLiteAdapter.isAFavoritePlace(dataSimple.getUri())) {
			favoristLay.setChecked(true);
		} else {
			favoristLay.setChecked(false);
		}
		favoristLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!sqLiteAdapter.isAFavoritePlace(dataSimple.getUri())) {
					sqLiteAdapter.addPlaceToFavoriteTable(dataSimple);
				} else {
					sqLiteAdapter.deletePlaceFromFavoriteTable(dataSimple
							.getUri());
				}
			}
		});

		WebView webView = (WebView) findViewById(R.id.placeImageView);
		System.out.println("Width = " + webView.getWidth());
		final int width = (int) (getResources().getDisplayMetrics().widthPixels
				/ getResources().getDisplayMetrics().density - 10);
		// Set image for webview image
		String data = "";
		data = "<html><head></head><body><img  src=\""
				+ dataSimple.getImageURL()
				+ "\" style=' background-color:transparent;' width='" + width
				+ "px;'height = '"
				+ getResources().getDimension(R.dimen.layy300)
				+ "px'    /></body></html>";
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setPluginsEnabled(true);
		webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.loadData(data, "text/html", "utf-8");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				log.e("Webview error");
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});

		textViewTitle = (TextView) findViewById(R.id.placeTypesTextView);
		textViewTitle.setText(dataSimple.getLabel());
		// Rating
		LinearLayout ratingLay = (LinearLayout) findViewById(R.id.ratingLayout);
		final int rate = dataSimple.getRatingNum();
		ratingLay.removeAllViews();
		for (int i = 0; i < rate; i++) {
			ImageView ratingIcon = new ImageView(PlaceDetails.this);
			ratingIcon.setImageResource(R.drawable.star_rating_full_dark);
			ratingLay.addView(ratingIcon);
		}
		for (int i = 0; i < (5 - rate); i++) {
			ImageView ratingIcon = new ImageView(PlaceDetails.this);
			ratingIcon.setImageResource(R.drawable.star_rating_empty_dark);
			ratingLay.addView(ratingIcon);
		}
		// Address
		TextView txtAddress = (TextView) findViewById(R.id.placeAddressTextView);
		txtAddress.setText(dataSimple.getAddress());
		// Phone
		TextView txtPhone = (TextView) findViewById(R.id.txt_phone);
		if (!dataSimple.getPhone().contains("anyType")) {
			txtPhone.setText(dataSimple.getPhone());
		} else {
			txtPhone.setText("Không có số điện thoại địa điểm này");
		}
		// Infor detail
		TextView txtInforDetail = (TextView) findViewById(R.id.txt_infor_detail);
		txtInforDetail.setText(dataSimple.getAbstractInfo());
	}

	public void onStop() {
		super.onStop();
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

	public void onViewMap(View v) {
		String iconUrl = "";
		int iconId = 0;
		try {
			iconUrl = OntologyCache.uriOfIcon.get(
					OntologyCache.hashMapTypeLabelToUri.get(dataSimple
							.getType() + "@" + ServerConfig.LANGUAGE_CODE))
					.getIconUrl();
			iconId = OntologyCache.uriOfIcon.get(
					OntologyCache.hashMapTypeLabelToUri.get(dataSimple
							.getType() + "@" + ServerConfig.LANGUAGE_CODE))
					.getIconId();
		} catch (Exception e) {
		}

		Intent intent = new Intent(context,
				XmlAdapter.getShowOnMapActivity(context));
		intent.putExtra("URI", dataSimple.getUri());
		intent.putExtra("label", dataSimple.getLabel());
		intent.putExtra("lat", dataSimple.getLatitude());
		intent.putExtra("long", dataSimple.getLongitude());
		try {
			intent.putExtra("iconurl", iconUrl);
			intent.putExtra("iconId", iconId);
		} catch (Exception e) {
		}
		this.context.startActivity(intent);
	}

	public void onPhoneCall(View v) {
		String phoneNumber = dataSimple.getPhone();
		if (phoneNumber != null && !phoneNumber.equals("")
				&& !phoneNumber.contains("anyType")) {
			try {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ phoneNumber));
				((Activity) context).startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.not_support_call), Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(context, "Không có số điện thoại của địa điểm này!",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onRating(View v) {
	}

	public void onSearchButtonClick(View v) {
		Intent intent = new Intent(PlaceDetails.this,
				DinningServiceSearch.class);
		startActivity(intent);
	}

	public void onBack(View v) {
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
