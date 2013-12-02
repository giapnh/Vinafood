package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import hust.hgbk.vtio.vinafood.customview.InstanceSimpleItemView;
import hust.hgbk.vtio.vinafood.ontology.simple.InstanceDataSimple;

import java.util.ArrayList;
import java.util.List;

public class ArrayInstanceSimpleAdapter extends ArrayAdapter<InstanceDataSimple>{

	Context context;
	ArrayList<InstanceDataSimple> instances = new ArrayList<InstanceDataSimple>();
	
	public ArrayInstanceSimpleAdapter(Context context, int textViewResourceId, List<InstanceDataSimple> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		instances = (ArrayList<InstanceDataSimple>) objects;
	}
	
	@Override
	public int getCount() {
		return instances.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InstanceSimpleItemView view;
		if (convertView!=null){
			view = (InstanceSimpleItemView) convertView;
		} else {
			view = new InstanceSimpleItemView(context);
		}
		
		view.createContent(instances.get(position));
		
		return view;
	}
}
