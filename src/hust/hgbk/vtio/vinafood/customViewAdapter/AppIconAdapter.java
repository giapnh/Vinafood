package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.customview.GridItemCustomView;
import hust.hgbk.vtio.vinafood.main.R;

public class AppIconAdapter extends BaseAdapter implements ListAdapter {

	private Context mContext;

    private Integer[] mThumbIds = {
            R.drawable.search, 
            R.drawable.travel_plan,
            R.drawable.icon, 
            R.drawable.icon,
            R.drawable.icon, 
            R.drawable.icon,
    };

    private String[] mTitles = {
    		"Semantic search",
    		"Travel plan",
    		"No title",
    		"No title",
    		"No title",
    		"No title"
    };
    
    public AppIconAdapter(Context c) {
        mContext = c;
    }

	
	
	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridItemCustomView view;
	        if (convertView == null) {
	        	view = new GridItemCustomView(mContext);
	        	ImageView imageView = view.getImageView();
	        	TextView textView   = view.getTextView();
	        	
	        	imageView.setImageResource(mThumbIds[position]);
	        	textView.setText(mTitles[position]);
	            
	        } else {
	            view = (GridItemCustomView) convertView;
	        }

	        return view;
	}

}
