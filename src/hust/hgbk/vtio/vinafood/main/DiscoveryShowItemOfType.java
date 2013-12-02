package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.main.DiscoveryActivity.FoodLocation;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DiscoveryShowItemOfType extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	Context ctx;

	int typeID;
	ArrayList<String> keys = new ArrayList<String>();
	Hashtable<String, FoodLocation> hashtable;

	ListView listView;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.ctx = DiscoveryShowItemOfType.this;
		// Load data
		typeID = getIntent().getExtras().getInt("type");
		hashtable = DiscoveryActivity.hashtable.get(Integer.valueOf(typeID));
		Iterator<String> iterator = hashtable.keySet().iterator();
		while (iterator.hasNext()) {
			keys.add(iterator.next());
		}

		setContentView(R.layout.discovery_show_all_item_of_type);
		((TextView) findViewById(R.id.txt_sub_menu)).setText(getResources()
				.getString(R.string.txt_discovery));
		// List type of food
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setAdapter(new DiscoveryAdapter());
	}

	public void onStop() {
		super.onStop();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public class DiscoveryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return hashtable.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(ctx, R.layout.discovery_item_layout,
						null);
				WebView content = (WebView) convertView
						.findViewById(R.id.webview);
				TextView title = (TextView) convertView
						.findViewById(R.id.title);
				TextView address = (TextView) convertView
						.findViewById(R.id.address);

				title.setText(keys.get(position));
				address.setText("Địa chỉ: "
						+ hashtable.get(keys.get(position)).address);
				content.loadData(hashtable.get(keys.get(position)).content,
						"text/html", "utf-8");
			}
			return convertView;
		}
	}

}
