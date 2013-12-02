package hust.hgbk.vtio.vinafood.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class ActivitySearch extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		
		LinearLayout eventConstraintLayout = (LinearLayout) findViewById(R.id.eventTimeConstraintLayout);
		eventConstraintLayout.setVisibility(View.VISIBLE);
		
	}
}
