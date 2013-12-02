//package hust.hgbk.vtio.vinafood.main;
//
//import hust.hgbk.vtio.vinafood.constant.SQLiteAdapter;
//import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
//import hust.hgbk.vtio.vinafood.customViewAdapter.ArrayPlaceResultOnMainAdapter;
//import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
//
//import java.util.ArrayList;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.GridView;
//import android.widget.TextView;
//
//public class OnNearByFragment extends Fragment {
//	View v;
//	Context ctx;
//
//	String titleSearch;
//	Float radius = 1f;
//	GridView listResultView;
//	TextView noFavorPlaceTextView;
//	ArrayList<FullDataInstance> listFullDataInstance = new ArrayList<FullDataInstance>();
//
//	String message;
//	ArrayPlaceResultOnMainAdapter arrayPlaceSimpleAdapter;
//	LoadAllInstanceTask loadAllInstanceTask;
//
//	public OnNearByFragment(Context ctx) {
//		super();
//		this.ctx = ctx;
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		v = View.inflate(ctx, R.layout.result_place_layout, null);
//		listResultView = (GridView) v.findViewById(R.id.listResultView);
//		noFavorPlaceTextView = (TextView) v
//				.findViewById(R.id.no_search_result_textview);
//		loadAllInstanceTask = new LoadAllInstanceTask();
//		loadAllInstanceTask.execute();
//		// Button showAllButton = (Button) v.findViewById(R.id.show_all_button);
//		// showAllButton.setOnClickListener(new OnClickListener() {
//		// @Override
//		// public void onClick(View v) {
//		// // TODO Auto-generated method stub
//		// if (listFullDataInstance.size() > 0) {
//		// Intent intent = new Intent(ctx, XmlAdapter
//		// .getShowAllOnMapActivity(ctx));
//		// int currentPosition = 0;
//		// if (arrayPlaceSimpleAdapter != null) {
//		// currentPosition = arrayPlaceSimpleAdapter
//		// .getCurrentPosition();
//		// }
//		// Log.v("POSITION", "current: " + currentPosition);
//		// int begin = (currentPosition - 3) > 0 ? (currentPosition - 3)
//		// : 0;
//		// int finish = (currentPosition + 3) < listFullDataInstance
//		// .size() ? (currentPosition + 3)
//		// : (listFullDataInstance.size());
//		// for (int i = begin; i < finish; i++) {
//		// Log.v("POSITION", listFullDataInstance.get(i).getUri());
//		// intent.putExtra("URI" + (i - begin),
//		// listFullDataInstance.get(i).getUri());
//		// intent.putExtra("label" + (i - begin),
//		// listFullDataInstance.get(i).getLabel());
//		// // added by Dungct
//		//
//		// intent.putExtra("longtitude" + (i - begin), Double
//		// .toString(listFullDataInstance.get(i)
//		// .getLongitude()));
//		// intent.putExtra("latitude" + (i - begin), Double
//		// .toString(listFullDataInstance.get(i)
//		// .getLatitude()));
//		// // end added
//		// //
//		// intent.putExtra("type" + (i - begin),
//		// listFullDataInstance.get(i).getType());
//		// }
//		// if (radius == 0f) {
//		// intent.putExtra("range", 2f);
//		// } else {
//		// intent.putExtra("range", radius);
//		// }
//		// startActivity(intent);
//		// }
//		// }
//		// });
//		return v;
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		XmlAdapter.synConfig(ctx);
//		message = getResources().getString(R.string.please_wait);
//
//	}
//
//	public class LoadAllInstanceTask extends AsyncTask<Void, Void, Void> {
//
//		Dialog progressLayout;
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			FullDataInstance[] dataInstances = SQLiteAdapter.getInstance(ctx)
//					.getAllFavoritePlace(8, 0);
//			for (int i = 0; i < dataInstances.length; i++) {
//				listFullDataInstance.add(dataInstances[i]);
//			}
//			// dungct: truy van tim wellKnown - Place - bat suy dien
//			arrayPlaceSimpleAdapter = new ArrayPlaceResultOnMainAdapter(ctx,
//					R.layout.place_item_layout, listFullDataInstance);
//			return null;
//		}
//
//		protected void onCancelled() {
//		}
//
//		@Override
//		protected void onPreExecute() {
//			progressLayout = new Dialog(ctx,
//					android.R.style.Theme_Translucent_NoTitleBar);
//			progressLayout.setContentView(R.layout.loading_layout);
//			progressLayout.show();
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//
//			if (listFullDataInstance.size() <= 0) {
//				noFavorPlaceTextView.setText(getResources().getString(
//						R.string.no_favorite_place));
//				noFavorPlaceTextView.setVisibility(View.VISIBLE);
//			} else {
//				noFavorPlaceTextView.setVisibility(View.GONE);
//			}
//			listResultView.setAdapter(arrayPlaceSimpleAdapter);
//			progressLayout.dismiss();
//			listResultView.setVisibility(View.VISIBLE);
//		}
//	}
//
//	public void onStop() {
//		super.onStop();
//
//	}
//
//	protected void showhelpDialog(String message) {
//		new AlertDialog.Builder(ctx)
//				.setTitle(getResources().getString(R.string.help))
//				.setMessage(message)
//				.setCancelable(false)
//				.setPositiveButton(getResources().getString(R.string.close),
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//							}
//						}).show();
//	}
//}