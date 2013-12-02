package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<ClassDataSimple> {
	Context context;

	ArrayList<ClassDataSimple> arrayClass = new ArrayList<ClassDataSimple>();

	public CustomSpinnerAdapter(Context context, int textViewResourceId,
			List<ClassDataSimple> objects) {
		super(context, textViewResourceId, objects);

		this.context = context;
		this.arrayClass = (ArrayList<ClassDataSimple>) objects;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// return super.getView(position, convertView, parent);
		LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.custom_row_spinner, parent, false);
		TextView label = (TextView) row.findViewById(R.id.place);
		label.setText(arrayClass.get(position).getLabel());

		ImageView icon = (ImageView) row.findViewById(R.id.icon);
		/*
		 * if(OntologyCache.hashMapLabelIcon.containsKey(arrayClass.get(position)
		 * .getUri())){
		 * icon.setImageDrawable(OntologyCache.hashMapLabelIcon.get(
		 * arrayClass.get(position).getUri()).icon); }else{
		 * icon.setImageDrawable(null); }
		 */
		// lva: chinh lai cach thuc lay icon
		if (OntologyCache.uriOfIcon.containsKey(arrayClass.get(position)
				.getUri())) {
			Resources resources = context.getResources();
			icon.setImageDrawable(resources.getDrawable(OntologyCache.uriOfIcon
					.get(arrayClass.get(position).getUri()).getIconId()));
		} else {
			icon.setImageDrawable(null);
		}
		return row;
	}

	private Drawable imageOperations(Context ctx, String url,
			String saveFilename) {
		try {
			InputStream is = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object fetch(String address) throws MalformedURLException,
			IOException {
		URL url = new URL(address);
		Object content = url.getContent();

		return content;
	}
}