package hust.hgbk.vtio.vinafood.customview;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceItemView extends RelativeLayout {
	WebView imageWebView;
	ImageView imageView;
	TextView labelTextView;
	// TextView distanceTextView;
	TextView abstractTextView;
	TextView addressTextView;
	TextView typeTextView;
	LinearLayout ratingView;
	ProgressBar progressBar;

	private Context context;

	String uri;

	public PlaceItemView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater li = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.place_item_layout, this, true);

		imageWebView = (WebView) findViewById(R.id.imagePlaceWebview);
		imageView = (ImageView) findViewById(R.id.imagePlaceview);
		labelTextView = (TextView) findViewById(R.id.labelTextView);
		addressTextView = (TextView) findViewById(R.id.addressTextView);
		abstractTextView = (TextView) findViewById(R.id.abstractInfor);
		ratingView = (LinearLayout) findViewById(R.id.ratingLinearLayout);
		typeTextView = (TextView) findViewById(R.id.typeTextView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	public WebView getImageWebView() {
		return imageWebView;
	}

	public void setImageWebView(WebView imageWebView) {
		this.imageWebView = imageWebView;
	}

	public TextView getLabelTextView() {
		return labelTextView;
	}

	public void setLabelTextView(TextView labelTextView) {
		this.labelTextView = labelTextView;
	}

	public TextView getAddressTextView() {
		return addressTextView;
	}

	public void setAddressTextView(TextView addressTextView) {
		this.addressTextView = addressTextView;
	}

	public LinearLayout getRatingView() {
		return ratingView;
	}

	public void setRatingView(LinearLayout ratingView) {
		this.ratingView = ratingView;
	}

	public TextView getTypeTextView() {
		return typeTextView;
	}

	public void setTypeTextView(TextView typeTextView) {
		this.typeTextView = typeTextView;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public void setData(final FullDataInstance fullDataInstance) {
		uri = fullDataInstance.getUri();
		labelTextView.setText(fullDataInstance.getLabel() + " ("
				+ fullDataInstance.getDistanceString() + ")");
		typeTextView.setText(fullDataInstance.getType());
		addressTextView.setText(fullDataInstance.getAddress());
		if (fullDataInstance.getAbstractInfo().length() == 0) {
			abstractTextView.setVisibility(View.GONE);
		} else if (fullDataInstance.getAbstractInfo().length() < 150) {
			abstractTextView.setVisibility(View.VISIBLE);
			abstractTextView.setText(fullDataInstance.getAbstractInfo());
		} else {
			abstractTextView.setVisibility(View.VISIBLE);
			abstractTextView.setText(fullDataInstance.getAbstractInfo()
					.substring(0, 148) + " ...");
		}
		String iconUrl1 = "";
		int iconId1 = 0;

		try {
			iconUrl1 = OntologyCache.uriOfIcon.get(
					OntologyCache.hashMapTypeLabelToUri.get(fullDataInstance
							.getType() + "@" + ServerConfig.LANGUAGE_CODE))
					.getIconUrl();
			iconId1 = OntologyCache.uriOfIcon.get(
					OntologyCache.hashMapTypeLabelToUri.get(fullDataInstance
							.getType() + "@" + ServerConfig.LANGUAGE_CODE))
					.getIconId();
		} catch (Exception e) {
		}
		if (fullDataInstance.getImageURL().equals("")
				|| fullDataInstance.getImageURL().contains("anyType")) {
			try {
				imageWebView.setVisibility(View.GONE);
				imageView.setVisibility(View.VISIBLE);
				imageView.setImageResource(iconId1);
			} catch (Exception e) {
			}

		} else {
			imageView.setVisibility(View.GONE);
			imageWebView.setVisibility(View.VISIBLE);
			String data = "<div style='border: solid #bb3333  1px; position: absolute;   "
					+ "top: 0px; left: 0px; width:"
					+ getResources().getDimension(R.dimen.layx130)
					+ "px; height:"
					+ getResources().getDimension(R.dimen.layx130)
					+ "px; overflow:hidden;  '><img src=\""
					+ fullDataInstance.getImageURL()
					+ "\" style=' background-color:transparent;' width='"
					+ getResources().getDimension(R.dimen.layx130)
					+ "px' height='"
					+ getResources().getDimension(R.dimen.layx130)
					+ "px'/></div>";
			try {
				imageWebView.loadData(data, "text/html", "utf-8");
				imageWebView.reload();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		imageWebView.setBackgroundColor(Color.TRANSPARENT);
		imageWebView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					imageWebView.setClickable(false);
					new AsyncTask<Void, Void, ArrayList<String>>() {

						@Override
						protected ArrayList<String> doInBackground(
								Void... params) {
							String query = "select ?url where {"
									+ "<"
									+ fullDataInstance.getUri()
									+ "> vtio:hasMedia ?imageUri. ?imageUri rdf:type vtio:Image."
									+ "?imageUri vtio:hasURL ?url." + "}";
							ArrayList<ArrayList<String>> results = new VtioCoreService()
									.executeQuery(query, true);
							ArrayList<String> listUrl = new ArrayList<String>();
							for (int i = 0; i < results.size(); i++) {
								String url = results
										.get(i)
										.get(0)
										.replace(
												"^^" + NameSpace.xsd + "string",
												"");
								url = url.trim();
								url = url.replace(" ", "%20");
								listUrl.add(url);
							}
							return listUrl;
						}

						@Override
						protected void onPostExecute(ArrayList<String> result) {
							if (result.size() > 0) {
								Display display = ((Activity) context)
										.getWindowManager().getDefaultDisplay();
								Dialog dialog = new Dialog(getContext(),
										R.style.MyDialogTheme);
								dialog.setContentView(new ShowImageOfPlaceView(
										getContext(), result, dialog));
								dialog.getWindow().setLayout(
										display.getWidth() - 20,
										LayoutParams.WRAP_CONTENT);
								dialog.show();
							} else {
								Toast.makeText(getContext(),
										"This place has no image!",
										Toast.LENGTH_SHORT).show();
							}

							imageWebView.setClickable(true);
						}

					}.execute();

				}
				return false;
			}
		});
		final int iconId = iconId1;

		final int rate = fullDataInstance.getRatingNum();
		ratingView.removeAllViews();
		for (int i = 0; i < rate; i++) {
			ImageView ratingIcon = new ImageView(getContext());
			ratingIcon.setImageResource(R.drawable.rating_icon);
			ratingView.addView(ratingIcon);
		}
		for (int i = 0; i < (5 - rate); i++) {
			ImageView ratingIcon = new ImageView(getContext());
			ratingIcon.setImageResource(R.drawable.rating_disable_icon);
			ratingView.addView(ratingIcon);
		}
		this.setClickable(true);
	}
}
