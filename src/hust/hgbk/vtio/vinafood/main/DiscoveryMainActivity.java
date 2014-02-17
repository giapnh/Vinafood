package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DiscoveryMainActivity extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	public Context ctx;
	
	
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discovery_main_layout);
		ctx = DiscoveryMainActivity.this;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void onMonngon(View v) {
		Intent intent = new Intent(DiscoveryMainActivity.this,
				DiscoveryHanoiFamousActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void onCuisineWithHelth(View v) {
		new CuisineHelthLoader().start();
	}

	public void onCookbook(View v) {
		new CookbookLoader().start();
	}

	class CuisineHelthLoader extends Thread {

		public void run() {
			SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(ctx);
			sqLiteAdapter.createDiscoveryTable();
			try {
				OntologyCache.preferUser = sqLiteAdapter
						.getAllPreferenceClass();
			} catch (Exception e) {
			}
			Intent intent = new Intent(DiscoveryMainActivity.this,
					DiscoveryCuisineWithHelthActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);

		}
	}

	class CookbookLoader extends Thread {

		public void run() {
			SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(ctx);
			sqLiteAdapter.createCookbookTable();
			try {
				OntologyCache.preferUser = sqLiteAdapter
						.getAllPreferenceClass();
			} catch (Exception e) {
			}
			Intent intent = new Intent(DiscoveryMainActivity.this,
					DiscoveryCookbookActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);

		}
	}

	/*
	 * On Click search button
	 */
	public void onSearchButtonClick(View v) {
		Intent intent = new Intent(DiscoveryMainActivity.this,
				DinningServiceSearch.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
