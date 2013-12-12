package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.customview.SubClassHorizontalView;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MenuActivity extends Activity {
	Context ctx;
	ImageButton btnSearch;
	Button advancedButton;
	String currentLanguage = "";
	public final String CLASS_URI = NameSpace.vtio + "Dining-Service";

	VtioCoreService service = new VtioCoreService();
	// background switcher
	ViewFlipper flipper;
	Thread flipperThread;
	RelativeLayout about;

	String queryString;
	Float radius = 0f;
	Handler uiThread = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				flipper.showNext();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.ctx = MenuActivity.this;
		setContentView(R.layout.mainmenu_layout);
		initial();
	}

	public void initial() {
		findViewById();
		flipperThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					uiThread.sendEmptyMessage(1);
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		flipperThread.start();
		SubClassHorizontalView.currentClassURI = CLASS_URI;
	}

	private void findViewById() {
		flipper = (ViewFlipper) this.findViewById(R.id.sampleFlipper);
		btnSearch = (ImageButton) this.findViewById(R.id.btnSearch);
		about = (RelativeLayout) this.findViewById(R.id.about);
	}

	public void searchWithKey(String keyWord) {
		String query = "SELECT DISTINCT  ?search {  ?search rdf:type <"
				+ SubClassHorizontalView.currentClassURI
				+ ">.  ?search"
				+ " vtio:hasLocation ?addresscity .   ?addresscity vtio:isPartOf <"
				+ ServerConfig.currentCityUri + ">.}";
		keyWord = keyWord.trim();
		if (keyWord.length() > 0) {
			if (keyWord.toLowerCase().equals("atm")) {
				query = "SELECT DISTINCT  ?search {" + "   {?search rdf:type <"
						+ SubClassHorizontalView.currentClassURI + ">."
						+ "    ?search vtio:hasLocation ?add_0."
						+ "    ?add_0 vtio:isPartOf <"
						+ ServerConfig.currentCityUri + ">."
						+ "	 ?search vtio:nearBy ?n. ?n rdf:type vtio:ATM.}} ";
			} else
				query = "SELECT DISTINCT  ?search {" + "   {?search rdf:type <"
						+ SubClassHorizontalView.currentClassURI + ">."
						+ "    ?search vtio:hasLocation ?add_0."
						+ "    ?add_0 vtio:isPartOf <"
						+ ServerConfig.currentCityUri + ">."
						+ "	 ?search fti:match '" + keyWord + "*'.} "
						+ "   UNION {" + "	 ?search rdf:type <"
						+ SubClassHorizontalView.currentClassURI + ">."
						+ "	 ?search vtio:hasLocation ?add_0."
						+ "    ?add_0 vtio:isPartOf <"
						+ ServerConfig.currentCityUri + ">."
						+ "	 ?add_0 vtio:isPartOf ?add_1."
						+ "	 ?add_1 fti:match '" + keyWord + "*'.} "
						+ "   UNION {" + "	 ?search rdf:type <"
						+ SubClassHorizontalView.currentClassURI + ">."
						+ "    ?class rdfs:subClassOf vtio:Cuisine-Style. "
						+ "	 ?class fti:match '" + keyWord + "*'. "
						+ "	 ?search vtio:hasLocation ?add_0. "
						+ "    ?add_0 vtio:isPartOf <"
						+ ServerConfig.currentCityUri + ">."
						+ "	 ?search vtio:hasCuisineStyle ?cuisine. "
						+ "	 ?cuisine rdf:type ?class. } }";
		}

		String message;
		try {
			String classLabel = getResources().getString(
					R.string.dining_service);
			message = getResources().getString(R.string.you_want_find_a) + " "
					+ classLabel + "\n";
			if (keyWord.length() == 0) {
				message = getResources().getString(R.string.show_all) + " "
						+ classLabel;
			} else {
				message = message
						+ getResources().getString(R.string.with_key_word)
						+ "'..." + keyWord + "...'";
			}

		} catch (Exception e) {
			message = getResources().getString(R.string.dining_service) + "\n";
			if (keyWord.length() == 0) {
				message = getResources().getString(R.string.show_all) + " "
						+ message;
			} else {
				message = message
						+ getResources().getString(R.string.with_key_word)
						+ "'..." + keyWord + "...'";
			}
		}

		queryString = query;
		Intent intent = new Intent(ctx, PlaceSearchResultActivity.class);
		intent.putExtra("QueryString", queryString);
		intent.putExtra("radius", radius);
		intent.putExtra("message", message);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void onNearBy(View v) {
		radius = 10f;
		searchWithKey("");
	}

	public void onDiscovery(View v) {
		Intent intent = new Intent(MenuActivity.this, DiscoveryActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void onFavorist(View v) {
		Intent intent = new Intent(MenuActivity.this,
				FavoritePlaceActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void onRecentView(View v) {
		Intent intent = new Intent(MenuActivity.this, RecentViewActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	boolean isShowAbout = false;

	public void onAboutUs(View v) {
		about.setVisibility(View.VISIBLE);
		about.startAnimation(AnimationUtils.loadAnimation(ctx,
				R.anim.slide_in_left));
		isShowAbout = true;
		TextView view = (TextView) about.findViewById(R.id.txt_other_app);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse(ServerConfig.SIG_APP_URI);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}

	public void onSearchButtonClick(View v) {
		Intent intent = new Intent(this, DinningServiceSearch.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isShowAbout) {
				about.setVisibility(View.GONE);
				about.startAnimation(AnimationUtils.loadAnimation(ctx,
						R.anim.slide_out_right));
			} else {
				finish();
			}
			isShowAbout = false;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onResume() {
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		super.onResume();
	}

}