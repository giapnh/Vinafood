package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.customViewAdapter.SimpleUriLabelAdapter;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.ontology.simple.InstanceDataSimple;

import java.util.ArrayList;

public class ChooseCitiSpinner extends Spinner {
	Context ctx;
	public static String uri = "";
	public static String label = "";

	public ChooseCitiSpinner(Context context) {
		super(context);
		ctx = context;
		ArrayList<InstanceDataSimple> listCity = new ArrayList<InstanceDataSimple>();
		for (int i = 0; i < ServerConfig.cityString.length; i++) {
			listCity.add(new InstanceDataSimple(ServerConfig.cityString[i][1],
			        ServerConfig.cityString[i][2]));
		}
		//		listCity.add(new InstanceDataSimple("Hồ Chí Minh", "http://hust.se.vtio.owl#hochiminh-city"));

		SimpleUriLabelAdapter listcityAdapter = new SimpleUriLabelAdapter(context,
		        R.layout.simple_row_item, listCity);
		setAdapter(listcityAdapter);
		setSelection(0);
		setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				uri = ((InstanceDataSimple) arg0.getItemAtPosition(position)).getURI();
				label = ((InstanceDataSimple) arg0.getItemAtPosition(position)).getLabel();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void setAdapter(SpinnerAdapter spinnerAdapter) {
		super.setAdapter(spinnerAdapter);
	}

	public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
		super.setOnItemSelectedListener(onItemSelectedListener);
	}

}
