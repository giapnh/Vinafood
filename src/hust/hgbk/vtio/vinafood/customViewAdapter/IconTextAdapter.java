package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.main.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author Ken
 *	Lá»›p nÃ y extends vÃ o ArrayAdapter<String>
 *	Viáº¿t Ä‘Ã¨ hÃ m getView Ä‘á»ƒ tráº£ ra kiá»ƒu mong muá»‘n
 */
public class IconTextAdapter extends ArrayAdapter<String> {
	private LayoutInflater inflater;
	private int resource;
	Context context;

	
	/**
	 * 
	 * @param ctx Context hiá»‡n táº¡i
	 * @param textViewResourceID Layout Ä‘á»ƒ hiá»ƒn thá»‹ tá»«ng dÃ²ng
	 * @param iconTextCouple Ä�á»‘i tÆ°á»£ng cáº·p icon - khÃ¡i niá»‡m
	 */

	public ArrayList<String> conceptsArray;
	
	public IconTextAdapter(Context ctx, int textViewResourceID, ArrayList<String> conceptsArray) {
		super(ctx, textViewResourceID,conceptsArray);
		context = ctx;
		
		inflater = LayoutInflater.from(ctx);
		resource = textViewResourceID;
		
		this.conceptsArray = conceptsArray;
//		Log.v("KEN", ""+ conceptsArray.size());
/*		for (Iterator i = conceptsArray.iterator(); i.hasNext();) {
			String s =(String) i.next();
			this.add(s);
			Log.v("KEN", s);
		}
		*/
	}
	
	public void addString(ArrayList<String> conceptsArray){
		for (Iterator i = conceptsArray.iterator(); i.hasNext();) {
			String s =(String) i.next();
			this.add(s);
			notifyDataSetChanged();
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		TextView textView;
		TextView iconView;

		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
		}
		textView = (TextView) convertView.findViewById(R.id.text);
		iconView = (TextView) convertView.findViewById(R.id.icon);
		String item = (String) this.getItem(position);
//		Log.v("TEST", "item: " +item);
		textView.setText(item);
		//add label true for object - khanhnq
		String uri = "";
		if (item.equals(context.getResources().getString(R.string.true1))){
			uri = "true";
		} else if (item.equals(context.getResources().getString(R.string.is_welknown))){
			uri = "welknown";
		} else {
			uri = OntologyCache.hashMapLabelToURI.get(item);
		}
		
		Drawable itemIcon;
		
		/*if (OntologyCache.hashMapLabelIcon.get(uri) != null){
			itemIcon = OntologyCache.hashMapLabelIcon.get(uri).icon;
			if (itemIcon != null){
				
				iconView.setBackgroundDrawable(itemIcon);
			} else iconView.setBackgroundColor(Color.TRANSPARENT);
		} else iconView.setBackgroundColor(Color.TRANSPARENT);*/
		
		if (OntologyCache.uriOfIcon.get(uri) != null){
			Resources resources = context.getResources();
			itemIcon = resources.getDrawable(OntologyCache.uriOfIcon.get(uri).getIconId());
			if (itemIcon != null){
				
				iconView.setBackgroundDrawable(itemIcon);
			} else iconView.setBackgroundColor(Color.TRANSPARENT);
		} else iconView.setBackgroundColor(Color.TRANSPARENT);
		
		
		itemIcon = null;
		return convertView;
	}
	
	

}
