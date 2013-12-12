package hust.hgbk.vtio.vinafood.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class VersionChangeLog extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.version_change_log);
	}
}
