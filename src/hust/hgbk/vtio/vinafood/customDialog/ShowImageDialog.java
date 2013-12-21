package hust.hgbk.vtio.vinafood.customDialog;

import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.customview.ShowImageFlipView;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShowImageDialog extends Dialog {
	// =================
	Context context;
	VtioCoreService services;

	// =================
	RelativeLayout imageLayout;
	ShowImageFlipView flipView;
	ProgressBar progressBar;

	// ==================
	ArrayList<String> listURL;

	public ShowImageDialog(Context _context, final String instanceURI) {
		super(_context, android.R.style.Theme_Translucent);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.show_image_layout);

		this.context = _context;
		final int width = (int) (context.getResources().getDisplayMetrics().widthPixels
				/ context.getResources().getDisplayMetrics().density - 20);
		imageLayout = (RelativeLayout) findViewById(R.id.imageLayout);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		flipView = (ShowImageFlipView) findViewById(R.id.flipView);

		services = new VtioCoreService();
		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				String imagesURI = "";
				try {
					String query = "select ?imageUri where {<"
							+ instanceURI
							+ "> vtio:hasMedia ?imageUri. ?imageUri rdf:type vtio:Image.}";
					ArrayList<ArrayList<String>> results = services
							.executeQuery(query, true);
					imagesURI = results.get(0).get(0);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (!imagesURI.equals("")) {
					String query = "Select distinct ?url where {<" + imagesURI
							+ "> vtio:hasURL ?url.}";
					ArrayList<ArrayList<String>> results = services
							.executeQuery(query, false);
					listURL = new ArrayList<String>();
					for (int i = 0; i < results.size(); i++) {
						listURL.add(results.get(i).get(0)
								.replace("^^" + NameSpace.xsd + "string", ""));
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (listURL == null || listURL.size() == 0) {
					Toast.makeText(context, "This place has no image!",
							Toast.LENGTH_SHORT).show();
					dismiss();
					return;
				} else if (listURL.size() == 1) {
					listURL.add(listURL.get(0));
				}
				for (int i = 0; i < listURL.size(); i++) {
					WebView view = new WebView(ShowImageDialog.this.context);
					view.getSettings().setJavaScriptEnabled(true);
					String data = "<html><head><style type='text/css'>body{text-align:center;}.inset {border-color:rgb(0,255,0);border-radius:10px;box-shadow:0px 0px 10px 1px #000;} </style></head><body><img class=\"inset\" src=\""
							+ listURL.get(i)
							+ "\" style=' background-color:transparent;' width='"
							+ width + "dip'  /></body></html>";
					view.getSettings().setAllowFileAccess(true);
					view.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY); // thanks
																				// Patrick!
					view.loadData(data, "text/html", "utf-8");
					view.setBackgroundColor(Color.argb(240, 0x11, 0x1d, 0x45));
					flipView.addView(view);
				}
				flipView.invalidate();
				imageLayout.invalidate();
				progressBar.setVisibility(View.GONE);

				// imageLayout.addView(flipView);
			}

		}.execute();
	}

}
