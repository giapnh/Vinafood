package hust.hgbk.vtio.vinafood.main;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;
import hust.hgbk.vtio.vinafood.customview.ConstraintHelper;
import hust.hgbk.vtio.vinafood.customview.SubClassHorizontalView;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;
import hust.hgbk.vtio.vinafood.query.Constraint;
import hust.hgbk.vtio.vinafood.query.Query;
import hust.hgbk.vtio.vinafood.query.Variable;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class DinningServiceSearch extends Activity {

	Context ctx;
	ImageButton btnSearch;
	EditText edtSearch;
	Button advancedButton;
	ImageView clearButton;

	// Dialog to show filter list
	Dialog filterDialog;

	// Input manager: show keyboard
	InputMethodManager imm;

	public static ArrayList<Variable> arrayVariable;
	public static ArrayList<Constraint> arrayConstraint;
	static String currentLanguage = "";

	public static final String CLASS_URI = NameSpace.vtio + "Dining-Service";

	VtioCoreService service = new VtioCoreService();
	// Subclass of dining service
	public static ArrayList<ClassDataSimple> listSubClass = new ArrayList<ClassDataSimple>();

	float radius = 1f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.giapnh_new_dinning_service);
		arrayConstraint = new ArrayList<Constraint>();
		arrayVariable = new ArrayList<Variable>();
		XmlAdapter.synConfig(this);
		ctx = this;
		init();
	}

	public void searchWithKey(String keyWord) {
		String query = "SELECT DISTINCT  ?search {  ?search rdf:type <"
				+ SubClassHorizontalView.currentClassURI
				+ ">.  ?search"
				+ " vtio:hasLocation ?addresscity .   ?addresscity vtio:isPartOf <"
				+ ServerConfig.currentCityUri + ">.}";
		keyWord = keyWord.trim();
		if (keyWord.length() > 0) {
			if (keyWord.toLowerCase().equals("atm")) {
				query = "SELECT DISTINCT  ?search {" + "   {?search rdf:type <"
						+ SubClassHorizontalView.currentClassURI + ">."
						+ "    ?search vtio:hasLocation ?add_0."
						+ "    ?add_0 vtio:isPartOf <"
						+ ServerConfig.currentCityUri + ">."
						+ "	 ?search vtio:nearBy ?n. ?n rdf:type vtio:ATM.}} ";
			} else
				query = "SELECT DISTINCT  ?search {" + "   {?search rdf:type <"
						+ SubClassHorizontalView.currentClassURI + ">."
						+ "    ?search vtio:hasLocation ?add_0."
						+ "    ?add_0 vtio:isPartOf <"
						+ ServerConfig.currentCityUri + ">."
						+ "	 ?search fti:match '" + keyWord + "*'.} "
						+ "   UNION {" + "	 ?search rdf:type <"
						+ SubClassHorizontalView.currentClassURI + ">."
						+ "	 ?search vtio:hasLocation ?add_0."
						+ "    ?add_0 vtio:isPartOf <"
						+ ServerConfig.currentCityUri + ">."
						+ "	 ?add_0 vtio:isPartOf ?add_1."
						+ "	 ?add_1 fti:match '" + keyWord + "*'.} "
						+ "   UNION {" + "	 ?search rdf:type <"
						+ SubClassHorizontalView.currentClassURI + ">."
						+ "    ?class rdfs:subClassOf vtio:Cuisine-Style. "
						+ "	 ?class fti:match '" + keyWord + "*'. "
						+ "	 ?search vtio:hasLocation ?add_0. "
						+ "    ?add_0 vtio:isPartOf <"
						+ ServerConfig.currentCityUri + ">."
						+ "	 ?search vtio:hasCuisineStyle ?cuisine. "
						+ "	 ?cuisine rdf:type ?class. } }";
			// dungct: Ket thuc truy van moi - toc do nhanh hon gap 2
		}

		String message;
		try {
			String classLabel = getResources().getString(
					R.string.dining_service);
			message = getResources().getString(R.string.you_want_find_a) + " "
					+ classLabel + "\n";
			if (keyWord.length() == 0) {
				message = getResources().getString(R.string.show_all) + " "
						+ classLabel;
			} else {
				message = message
						+ getResources().getString(R.string.with_key_word)
						+ "'..." + keyWord + "...'";
			}

		} catch (Exception e) {
			message = getResources().getString(R.string.dining_service) + "\n";
			if (keyWord.length() == 0) {
				message = getResources().getString(R.string.show_all) + " "
						+ message;
			} else {
				message = message
						+ getResources().getString(R.string.with_key_word)
						+ "'..." + keyWord + "...'";
			}
		}

		Intent intent = new Intent(ctx, PlaceSearchResultActivity.class);
		intent.putExtra("QueryString", query);
		intent.putExtra("radius", 1f);
		intent.putExtra("message", message);
		startActivity(intent);
	}

	public void init() {
		edtSearch = (EditText) findViewById(R.id.edtSearch);
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		clearButton = (ImageView) findViewById(R.id.btnClearText);
		SubClassHorizontalView.currentClassURI = CLASS_URI;

		edtSearch.postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager mInputMethodManager = (InputMethodManager) DinningServiceSearch.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				mInputMethodManager.showSoftInput(edtSearch, 0);
			}
		}, 300);
		clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edtSearch.setText("");
			}
		});

		edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchWithKey(edtSearch.getText().toString());
				}
				return false;
			}
		});
		new GetAllSubClassTask().execute();
		setScope();
	}

	// Search
	String purpose = null;
	ClassDataSimple businessType = null;
	String cuisineStyle = null;

	public void onSearch(View v) {
		arrayVariable.clear();
		Variable variable = new Variable("Dining Service that you want",
				"search", SubClassHorizontalView.currentClassURI, true);
		arrayVariable.add(variable);
		if (businessType != null) {
			arrayVariable.clear();
			variable = new Variable("Dining Service that you want", "search",
					businessType.getUri(), true);
			arrayVariable.add(variable);
		}

		/**
		 * Lay ra tat ca cac rang buoc
		 */
		if (arrayConstraint == null) {
			arrayConstraint = new ArrayList<Constraint>();
		} else {
			arrayConstraint.clear();
		}

		String keyWord = edtSearch.getText().toString().trim();
		// Has name
		if (!keyWord.trim().equals("")) {
			Constraint hasNameConstraint = ConstraintHelper.getConstraint(
					ConstraintHelper.HAS_NAME, keyWord);
			arrayConstraint.add(hasNameConstraint);
		}

		if (cuisineStyle != null) {
			Constraint cuisineStyleConstraint = ConstraintHelper.getConstraint(
					ConstraintHelper.HAS_CUISINE_STYLE, cuisineStyle);
			arrayConstraint.add(cuisineStyleConstraint);
		}

		String message;
		try {
			String classLabel = getResources().getString(
					R.string.dining_service);
			for (int i = 0; i < listSubClass.size(); i++) {
				if (listSubClass.get(i).getUri()
						.equals(SubClassHorizontalView.currentClassURI)) {
					classLabel = listSubClass.get(i).getLabel();
					break;
				}
			}
			message = getResources().getString(R.string.you_want_find_a) + " "
					+ classLabel + "\n";

		} catch (Exception e) {
			message = getResources().getString(R.string.dining_service) + "\n";
		}

		float radius = this.radius;
		if (radius > 0)
			message = message + getResources().getString(R.string.in_range)
					+ " " + radius + " km.";
		// Hien thi activity ket qua

		Intent intent = new Intent(ctx, PlaceSearchResultActivity.class);
		Query query = new Query();
		query.setArrayVariable(arrayVariable);
		query.setArrayConstraint(arrayConstraint);
		if (radius == 0f) {
			intent.putExtra("QueryString", query.getQueryStringWithCity());
		} else {
			intent.putExtra("QueryString", query.getQueryString());
		}
		System.out.println(query.getQueryString());
		intent.putExtra("radius", this.radius);
		intent.putExtra("message", message);
		startActivity(intent);
	}

	ArrayAdapter<String> filterListAdapter;

	public void onCityFilter(View v) {
		final Dialog dialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.single_choise_listview_layout);
		ListView listView = (ListView) dialog.findViewById(R.id.listView);
		final String cityNames[] = ServerConfig.listOfCity();
		listView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.single_item, R.id.itemName, cityNames));
		TextView layName = (TextView) dialog.findViewById(R.id.layoutName);
		layName.setText(getResources().getString(R.string.txt_city));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				String cityName = cityNames[position];
				((TextView) DinningServiceSearch.this
						.findViewById(R.id.cityValue)).setText(cityName);
				String cityInfos[] = ServerConfig.infoOfCity(cityName);
				ServerConfig.currentCityLabel = cityInfos[1];
				ServerConfig.currentCityUri = cityInfos[2];
				dialog.dismiss();
			}

		});
		Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void onBusineseFilter(View v) {
		filterDialog = new Dialog(DinningServiceSearch.this,
				android.R.style.Theme_NoTitleBar);
		filterDialog.setContentView(R.layout.single_choise_listview_layout);
		ListView filterList = (ListView) filterDialog
				.findViewById(R.id.listView);
		TextView layName = (TextView) filterDialog
				.findViewById(R.id.layoutName);
		layName.setText(getResources().getString(R.string.txt_businese_type));
		String[] str = new String[listSubClass.size()];
		for (int i = 0; i < listSubClass.size(); i++) {
			str[i] = listSubClass.get(i).getLabel();
		}
		// Bind data
		filterListAdapter = new ArrayAdapter<String>(this,
				R.layout.single_item, R.id.itemName, str);
		filterList.setAdapter(filterListAdapter);
		filterList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				businessType = listSubClass.get(position);
				((TextView) DinningServiceSearch.this
						.findViewById(R.id.typeValue)).setText(listSubClass
						.get(position).getLabel());
				filterDialog.dismiss();
			}
		});
		filterDialog.findViewById(R.id.btnCancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						filterDialog.dismiss();
					}
				});
		filterDialog.show();
	}

	// TODO
	public void onPurposeFilter(View v) {
		filterDialog = new Dialog(DinningServiceSearch.this,
				android.R.style.Theme_NoTitleBar);
		filterDialog.setContentView(R.layout.single_choise_listview_layout);
		TextView layName = (TextView) filterDialog
				.findViewById(R.id.layoutName);
		layName.setText(getResources().getString(R.string.txt_purpose));
		ListView filterList = (ListView) filterDialog
				.findViewById(R.id.listView);
		// Bind data
		final String[] data = getResources().getStringArray(
				R.array.purpose_list);
		filterListAdapter = new ArrayAdapter<String>(this,
				R.layout.single_item, R.id.itemName, data);
		filterList.setAdapter(filterListAdapter);
		filterList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				((TextView) DinningServiceSearch.this
						.findViewById(R.id.purposeValue))
						.setText(data[position]);
				filterDialog.dismiss();
			}
		});
		filterDialog.show();
	}

	public void onCuisineStyle(View v) {
		filterDialog = new Dialog(DinningServiceSearch.this,
				android.R.style.Theme_NoTitleBar);
		filterDialog.setContentView(R.layout.single_choise_listview_layout);
		TextView layName = (TextView) filterDialog
				.findViewById(R.id.layoutName);
		layName.setText(getResources().getString(R.string.txt_cuisine));
		ListView filterList = (ListView) filterDialog
				.findViewById(R.id.listView);
		// Bind data
		final String[] data = getResources().getStringArray(
				R.array.cuisine_style_list);
		filterListAdapter = new ArrayAdapter<String>(this,
				R.layout.single_item, R.id.itemName, data);
		filterList.setAdapter(filterListAdapter);
		filterList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				cuisineStyle = data[position];
				((TextView) DinningServiceSearch.this
						.findViewById(R.id.styleValue)).setText(data[position]);
				filterDialog.dismiss();
			}
		});
		filterDialog.show();
	}

	float temp = 0;

	public void setScope() {
		final TextView value = (TextView) findViewById(R.id.range_text);
		Button plus = (Button) findViewById(R.id.btn_plus);
		Button minus = (Button) findViewById(R.id.btn_minus);
		plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				temp += 1;
				value.setText("" + temp);
				try {
					radius = Integer.parseInt(value.getText().toString());
				} catch (NumberFormatException exception) {
					radius = 1;
				}
			}
		});
		minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (temp > 1) {
					temp -= 1;
				}
				value.setText("" + temp);
				try {
					radius = Integer.parseInt(value.getText().toString());
				} catch (NumberFormatException exception) {
					radius = 1;
				}
			}
		});
	}

	// On icon back inside clicked listener
	public void onBack(View v) {
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

	class GetAllSubClassTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			SubClassHorizontalView.currentClassURI = CLASS_URI;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			while (listSubClass.size() == 0)
				listSubClass = service.getAllAdaptedSubClassOf(CLASS_URI);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}
}
