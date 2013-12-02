package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import hust.hgbk.vtio.vinafood.customview.SubApplicationView;
import hust.hgbk.vtio.vinafood.entities.SubApplication;

import java.util.ArrayList;
import java.util.List;

public class ArraySubAppAdapter extends ArrayAdapter<SubApplication> {

	Context context;
	ArrayList<SubApplication> listApp = new ArrayList<SubApplication>();
	
	public ArraySubAppAdapter(Context context, int textViewResourceId, List<SubApplication> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.listApp = (ArrayList<SubApplication>) objects;
	}

	@Override
	public int getCount() {
		if (listApp!=null){
			return listApp.size();
		}
		else return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.v("TEST2", "Main " + position);
		SubApplicationView view = (SubApplicationView) convertView;
		if (view==null){
			view = new SubApplicationView(context);
		}
		
		/**
		 * Truyen thong tin vao view
		 */
		SubApplication subApp = listApp.get(position);
		
		try {
			view.getAppImageView().setImageResource(subApp.getAppIconId());
		} catch (Exception e) {
//			Log.e("LIST APP", "Image create error");
		}
		
		try {
			view.getAppNameTextView().setText(subApp.getAppName());
		} catch (Exception e) {
//			Log.e("LIST APP", "Name create error");
		}
		
		try {
			view.getAppDescriptionTextView().setText(subApp.getAppDescription());
		} catch (Exception e) {
//			Log.e("LIST APP", "Description create error");
		}
		
		
		return view;
	}
}
