package hust.hgbk.vtio.vinafood.main;

import android.app.Activity;
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
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
