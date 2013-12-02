package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.constant.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.customViewAdapter.NewArrayPlaceSimpleAdapter;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class FavoritePlaceActivity extends Activity {
	Context ctx;

	String titleSearch;
	Float radius = 0f;
	ListView listResultView;
	TextView noFavorPlaceTextView;
	ArrayList<FullDataInstance> listFullDataInstance = new ArrayList<FullDataInstance>();

	String message;
	NewArrayPlaceSimpleAdapter arrayPlaceSimpleAdapter;
	LoadAllInstanceTask loadAllInstanceTask;
	SQLiteAdapter sqLiteAdapter;

	public FavoritePlaceActivity(Context ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_place_layout);
		listResultView = (ListView) findViewById(R.id.listResultView);
		noFavorPlaceTextView = (TextView) this
				.findViewById(R.id.no_search_result_textview);
		loadAllInstanceTask = new LoadAllInstanceTask();
		loadAllInstanceTask.execute();
		message = getResources().getString(R.string.please_wait);
	}

	public class LoadAllInstanceTask extends AsyncTask<Void, Void, Void> {

		Dialog progressLayout;

		@Override
		protected Void doInBackground(Void... arg0) {
			FullDataInstance[] dataInstances = SQLiteAdapter.getInstance(ctx)
					.getAllFavoritePlace(8, 0);
			for (int i = 0; i < dataInstances.length; i++) {
				listFullDataInstance.add(dataInstances[i]);
			}
			arrayPlaceSimpleAdapter = new NewArrayPlaceSimpleAdapter(ctx,
					R.layout.place_item_layout, listFullDataInstance,
					sqLiteAdapter);
			return null;
		}

		@Override
		protected void onPreExecute() {
			progressLayout = new Dialog(ctx,
					android.R.style.Theme_Translucent_NoTitleBar);
			progressLayout.setContentView(R.layout.loading_layout);
			progressLayout.show();
		}

		@Override
		protected void onPostExecute(Void result) {

			if (listFullDataInstance.size() <= 0) {
				noFavorPlaceTextView.setText(getResources().getString(
						R.string.no_favorite_place));
				noFavorPlaceTextView.setVisibility(View.VISIBLE);
			} else {
				noFavorPlaceTextView.setVisibility(View.GONE);
			}
			listResultView.setAdapter(arrayPlaceSimpleAdapter);
			progressLayout.dismiss();
			listResultView.setVisibility(View.VISIBLE);
		}
	}

	public void onStop() {
		super.onStop();

	}

	protected void showhelpDialog(String message) {
		new AlertDialog.Builder(ctx)
				.setTitle(getResources().getString(R.string.help))
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.close),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
	}
}