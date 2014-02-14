package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.database.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.entities.Topic;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DiscoveryCuisineWithHelthActivity extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	public Context ctx;
	public ListView listView;
	public TextView indexPage;
	DiscoveryHelthAdapter adapter;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.ctx = DiscoveryCuisineWithHelthActivity.this;
		setContentView(R.layout.discovery_amthuc_suckhoe_main);
		listView = (ListView) this.findViewById(R.id.category);
		indexPage = (TextView) this.findViewById(R.id.index_page);
		adapter = new DiscoveryHelthAdapter();
		adapter.load(adapter.LIMIT, 0);
		listView.setAdapter(adapter);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void onNext(View v) {
		if (adapter.CURR_PAGE != adapter.NUM_PAGE) {
			adapter.CURR_PAGE++;
			adapter.load(adapter.LIMIT, adapter.OFFSET + adapter.LIMIT);
			adapter.notifyDataSetChanged();
		}
	}

	public void onPrev(View v) {
		if (adapter.CURR_PAGE != 1) {
			adapter.CURR_PAGE--;
			adapter.load(adapter.LIMIT, adapter.OFFSET - adapter.LIMIT);
			adapter.notifyDataSetChanged();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class DiscoveryHelthAdapter extends BaseAdapter {
		public int TOTAL = 0;
		public int NUM_PAGE = 0;
		public int CURR_PAGE = 1;
		public int LIMIT = 10;
		public int OFFSET = 0;
		Topic[] topics;

		public DiscoveryHelthAdapter() {
			TOTAL = SQLiteAdapter.getInstance(ctx).topicCount();
			NUM_PAGE = (int) Math.ceil(TOTAL / LIMIT);
			indexPage.setText(CURR_PAGE + "/" + NUM_PAGE);
		}

		@Override
		public void notifyDataSetChanged() {
			indexPage.setText(CURR_PAGE + "/" + NUM_PAGE);
			super.notifyDataSetChanged();
		}

		public void load(int limit, int offset) {
			topics = SQLiteAdapter.getInstance(ctx).getTopics(limit, offset);
			OFFSET = offset;
		}

		@Override
		public int getCount() {
			return topics.length;
		}

		@Override
		public Object getItem(int paramInt) {
			return topics[paramInt];
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(final int paramInt, View convertView,
				ViewGroup paramViewGroup) {
			View v = convertView;
			if (v == null) {
				v = View.inflate(ctx,
						R.layout.discovery_amthuc_suckhoe_row_layout, null);
			}
			ImageView icon = (ImageView) v.findViewById(R.id.img_icon);
			TextView title = (TextView) v.findViewById(R.id.title);
			TextView description = (TextView) v.findViewById(R.id.description);
			Picasso.with(ctx).load(topics[paramInt].imgLink).resize(130, 130)
					.centerCrop().into(icon);
			title.setText(topics[paramInt].title);
			String txtDescription = topics[paramInt].description.substring(0,
					topics[paramInt].description.length() > 200 ? 200
							: topics[paramInt].description.length());
			description.setText(txtDescription + "...");
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(
							DiscoveryCuisineWithHelthActivity.this,
							DiscoveryCuisineWithHelthViewTopicActivity.class);
					intent.putExtra("topic", topics[paramInt]);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			});
			return v;
		}
	}
}
