package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import hust.hgbk.vtio.vinafood.customViewAdapter.SimpleUriLabelAdapter;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.ontology.simple.InstanceDataSimple;

import java.util.ArrayList;

public class ChooseGeoPositionSpinner extends Spinner {
	Context ctx;
	public static String uri = "";
	public static double longitude = 105.8522;
	public static double latitude = 21.0287;

	public ChooseGeoPositionSpinner(Context context) {
		super(context);
		ctx = context;
		ArrayList<InstanceDataSimple> listCity = new ArrayList<InstanceDataSimple>();
		InstanceDataSimple hoanKiem = new InstanceDataSimple("Hồ Hoàn Kiếm",
		        "http://hust.se.vtio.owl#hoan-Kiem-lake");
		hoanKiem.setLongitude(105.8522);
		hoanKiem.setLatitude(21.0287);

		InstanceDataSimple samSon = new InstanceDataSimple("Bãi biển Sầm Sơn",
		        "http://hust.se.vtio.owl#");
		samSon.setLongitude(105.9);
		samSon.setLatitude(19.7333);

		InstanceDataSimple ducBa = new InstanceDataSimple("Nhà thờ đức bà",
		        "http://hust.se.vtio.owl#duc-ba-church");
		ducBa.setLongitude(106.6990);
		ducBa.setLatitude(10.7798);
		InstanceDataSimple bachKhoa = new InstanceDataSimple("ĐH Bách Khoa HN",
		        "http://hust.se.vtio.owl#");
		bachKhoa.setLongitude(105.8430);
		bachKhoa.setLatitude(21.0065);
		listCity.add(hoanKiem);
		listCity.add(samSon);
		listCity.add(ducBa);
		listCity.add(bachKhoa);

		SimpleUriLabelAdapter listcityAdapter = new SimpleUriLabelAdapter(context,
		        R.layout.simple_row_item, listCity);
		setAdapter(listcityAdapter);
		setSelection(0);
		setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				uri = ((InstanceDataSimple) arg0.getItemAtPosition(position)).getURI();
				longitude = ((InstanceDataSimple) arg0.getItemAtPosition(position)).getLongitude();
				latitude = ((InstanceDataSimple) arg0.getItemAtPosition(position)).getLatitude();
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
