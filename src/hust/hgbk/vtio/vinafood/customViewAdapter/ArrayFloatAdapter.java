package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.main.R;

import java.util.ArrayList;
import java.util.List;

public class ArrayFloatAdapter extends ArrayAdapter<Float> {
	Context context;
	String titleDefault = "";
	ArrayList<Float> arrayFloat;
	public ArrayFloatAdapter(Context context, int textViewResourceId,
			List<Float> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.arrayFloat = (ArrayList<Float>) objects;
	}
	public ArrayFloatAdapter(Context context, int textViewResourceId,
			List<Float> objects, String titleDefault) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.arrayFloat = (ArrayList<Float>) objects;
		this.titleDefault = titleDefault;
	}
	@Override
	public int getCount() {
		return arrayFloat.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view;
		if (convertView == null){
			view = new TextView(context);
		} else {
			view = (TextView) convertView;
		}
		view.setTextColor(Color.BLUE);
		if (!titleDefault.equals("")){
			if (position == 0){
				view.setText(titleDefault);
			} else {
				view.setText(""+arrayFloat.get(position));
			}
			
		} else {
			view.setText(""+arrayFloat.get(position));
		}
		return view;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView view;
		LayoutInflater inflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (TextView) inflater.inflate(R.layout.simple_row_item, parent, false);
		if (!titleDefault.equals("")){
			if (position == 0){
				view.setText(titleDefault);
			} else {
				view.setText(""+arrayFloat.get(position));
			}
			
		} else {
			view.setText(""+arrayFloat.get(position));
		}
		
		return view;
	}
}
