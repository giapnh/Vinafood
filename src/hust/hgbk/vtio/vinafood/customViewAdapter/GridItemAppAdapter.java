package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import hust.hgbk.vtio.vinafood.customview.GridItemAppView;
import hust.hgbk.vtio.vinafood.entities.SubApplication;

import java.util.ArrayList;
import java.util.List;

public class GridItemAppAdapter extends ArrayAdapter<SubApplication>{
	Context context;
	ArrayList<SubApplication> listSubApplications;
	public GridItemAppAdapter(Context context, int textViewResourceId,
			List<SubApplication> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		listSubApplications = (ArrayList<SubApplication>) objects;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		GridItemAppView gridItemAppView;
		if (convertView == null){
			gridItemAppView = new GridItemAppView(context);
		} else {
			gridItemAppView = (GridItemAppView) convertView;
		}
		gridItemAppView.setUI(listSubApplications.get(position));
		return gridItemAppView;
	}

}
