package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;

import java.util.ArrayList;
import java.util.List;

public class ArrayClassAdapter extends ArrayAdapter<ClassDataSimple> {
	
	Context context;
	ArrayList<ClassDataSimple> arrayClass = new ArrayList<ClassDataSimple>();
	
	public ArrayClassAdapter(Context context, int textViewResourceId, List<ClassDataSimple> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		arrayClass = (ArrayList<ClassDataSimple>) objects;
	}
	
	@Override
	public int getCount() {
		return arrayClass.size();
	} 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView view = (TextView) convertView;
		if (view == null){
			view = new TextView(this.context);
		}
		
		view.setText(arrayClass.get(position).getLabel().replace("^^"+NameSpace.xsd+"string", ""));
		
		return view;
	}
}
