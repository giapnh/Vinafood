package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import hust.hgbk.vtio.vinafood.customview.propertyWithValueView;
import hust.hgbk.vtio.vinafood.ontology.PropertyWithValue;

import java.util.ArrayList;
import java.util.List;

public class ArrayPropertyWithValueAdapter extends ArrayAdapter<PropertyWithValue> {
	Context context;
	ArrayList<PropertyWithValue> listPropertyWithValues = new ArrayList<PropertyWithValue>();
	
	public ArrayPropertyWithValueAdapter(Context context, int textViewResourceId, List<PropertyWithValue> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		if (objects != null)
			listPropertyWithValues = (ArrayList<PropertyWithValue>) objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PropertyWithValue propertyWithValue = listPropertyWithValues.get(position);
		propertyWithValueView view = new propertyWithValueView(context, propertyWithValue);
		return view;
	}
	
	@Override
	public int getCount() {
		return listPropertyWithValues.size();
	}
}
