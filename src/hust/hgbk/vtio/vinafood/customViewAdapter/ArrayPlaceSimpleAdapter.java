package hust.hgbk.vtio.vinafood.customViewAdapter;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.constant.SQLiteAdapter;
import hust.hgbk.vtio.vinafood.customview.PlaceItemView;
import hust.hgbk.vtio.vinafood.main.NewInstanceDetails;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.quickaction.ActionItem;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

public class ArrayPlaceSimpleAdapter extends ArrayAdapter<FullDataInstance> {
	final static int LIMIT = 8;
	int offsetOnList;
	Context context;
	ArrayList<FullDataInstance> listPlaceDataSimple;
	VtioCoreService services = new VtioCoreService();
	HashMap<Integer, Boolean> hashMapView;
	int countLoopQuery = 0;
	ArrayList<ArrayList<String>> queryResult;
	LoadInstanceDetails loadInstanceDetails;
	private int currentPosition = 0;

	ActionItem detailsAction = new ActionItem();
	ActionItem mapsAction = new ActionItem();
	ActionItem imagesAction = new ActionItem();
	ActionItem videosAction = new ActionItem();
	ActionItem speaksAction = new ActionItem();
	SQLiteAdapter sqLiteAdapter;

	public ArrayPlaceSimpleAdapter(Context context, int textViewResourceId,
			ArrayList<FullDataInstance> objects,
			ArrayList<ArrayList<String>> queryResult) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.listPlaceDataSimple = objects;
		this.sqLiteAdapter = SQLiteAdapter.getInstance(context);
		this.sqLiteAdapter.checkAndCreateDatabase();
		hashMapView = new HashMap<Integer, Boolean>();
		this.queryResult = queryResult;
		loadInstanceDetails(0, LIMIT);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		currentPosition = position;
		final PlaceItemView view;
		view = new PlaceItemView(context);
		if (position == getCount() - 1) {
			if (hashMapView.get(position) == null || !hashMapView.get(position)) {
				loadInstanceDetails = new LoadInstanceDetails(offsetOnList,
						LIMIT, view.getProgressBar());
				loadInstanceDetails.execute();
			}

		}

		final FullDataInstance placeItem = listPlaceDataSimple.get(position);
		view.setData(placeItem);

		view.setClickable(true);
		hashMapView.put(position, true);
		view.findViewById(R.id.for_click_view).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								NewInstanceDetails.class);
						Bundle bundle = new Bundle();
						bundle.putString("abstractInfo",
								placeItem.getAbstractInfo());
						bundle.putString("address", placeItem.getAddress());
						bundle.putString("imageURL", placeItem.getImageURL());
						bundle.putString("label", placeItem.getLabel());
						bundle.putDouble("latitude", placeItem.getLatitude());
						bundle.putDouble("longitude", placeItem.getLongitude());
						bundle.putString("location", placeItem.getLocation());
						bundle.putString("phone", placeItem.getPhone());
						bundle.putInt("ratingNum", placeItem.getRatingNum());
						bundle.putString("type", placeItem.getType());
						bundle.putString("uri", placeItem.getUri());
						bundle.putString("wellKnown", placeItem.getWellKnown());
						intent.putExtras(bundle);
						context.startActivity(intent);
					}
				});

		return view;
	}

	class LoadInstanceDetails extends AsyncTask<Void, Void, Void> {

		int offset;
		int limit;
		ProgressBar progressBar;

		public LoadInstanceDetails(int offset, int limit,
				ProgressBar progressBar) {
			super();
			this.offset = offset;
			this.limit = limit;
			this.progressBar = progressBar;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			countLoopQuery = 0;
			loadInstanceDetails(offset, limit);
			return null;
		}

		protected void onPreExecute() {
			progressBar.setVisibility(ProgressBar.VISIBLE);

		}

		@Override
		protected void onPostExecute(Void result) {
			notifyDataSetChanged();
			progressBar.setVisibility(ProgressBar.GONE);
		}
	}

	public void loadInstanceDetails(int offset, int limit) {
		if (queryResult.size() <= offset) {
			return;
		} else {
			if (queryResult.size() < offset + limit) {
				limit = queryResult.size() - offset;
			}
			offsetOnList = offset + limit;
		}

		String queryString = ""
				+ "SELECT distinct ?uri ?label ?type  ?location ?isWellknown ?abstract ?imageURL ?ratingNum ?long ?lat ?loc1 ?loc2 ?loc3 ?loc4 ?loc5 ?phone"
				+ " WHERE {{";

		String placeURI = "";
		// if (queryResult.size()> offset) {
		placeURI = queryResult.get(offset).get(0);
		if (!placeURI.contains("general")) {
			queryString = queryString
					+ "	{ SELECT ?label WHERE  {<"
					+ placeURI
					+ "> rdfs:label ?label."
					+ "FILTER(lang(?label)='"
					+ ServerConfig.LANGUAGE_CODE
					+ "')"
					+ "} LIMIT 1 } "
					+ "			{{?uri rdfs:label ?label. FILTER(?uri= <"
					+ placeURI
					+ ">)}  }"
					+ "		 	OPTIONAL{SELECT ?abstract WHERE { <"
					+ placeURI
					+ "> vtio:hasAbstract ?abstract} LIMIT 1 }"
					+ "			OPTIONAL{SELECT ?location WHERE { <"
					+ placeURI
					+ "> vtio:hasLocation ?location.} LIMIT 1 }"
					+
					/*
					 * "			OPTIONAL{<"+placeURI+
					 * "> rdf:type ?typeURI. ?typeURI rdfs:label ?type. FILTER regex(Str(?typeURI), 'vtio') "
					 * +
					 * "FILTER(lang(?type)='"+ServerConfig.LANGUAGE_CODE+"')} "
					 * +
					 */
					// dungct: chinh lai de support ontology co nhan tieng Viet
					// khong dau ben canh co dau.
					"			OPTIONAL {<"
					+ placeURI
					+ "> rdf:type ?typeURI. {SELECT ?type WHERE {?typeURI rdfs:label ?type . FILTER(lang(?type)='"
					+ ServerConfig.LANGUAGE_CODE
					+ "')} LIMIT 1 } "
					+ "FILTER regex(Str(?typeURI), 'vtio') } "
					+
					// end modification
					"			OPTIONAL{SELECT ?isWellknown WHERE { <"
					+ placeURI
					+ "> vtio:isWellKnown ?isWellknown. } LIMIT 1 }"
					+ "			OPTIONAL{SELECT ?long WHERE { <"
					+ placeURI
					+ "> vtio:hasLongtitude ?long. } LIMIT 1 }"
					+ "			OPTIONAL{SELECT ?lat WHERE { <"
					+ placeURI
					+ "> vtio:hasLatitude ?lat. } LIMIT 1 }"
					+ "			OPTIONAL{SELECT ?ratingNum WHERE { <"
					+ placeURI
					+ "> vtio:hasRating ?rating. ?rating vtio:hasNumberStar ?ratingNum.} LIMIT 1 }"
					+ "			OPTIONAL{SELECT ?imageURL WHERE { <"
					+ placeURI
					+ "> vtio:hasMedia ?media. ?media rdf:type vtio:Image. ?media vtio:hasURL ?imageURL.} LIMIT 1 } "
					+ "OPTIONAL { SELECT ?loc1 ?loc2 ?loc3 ?loc4 where {  "
					+ "OPTIONAL { "
					+ "<"
					+ placeURI
					+ "> vtio:hasLocation ?x. ?x vtio:isPartOf ?y. \n"
					+ "OPTIONAL { SELECT ?x WHERE {{?x vtio:hasValue ?loc1.} UNION {?x rdfs:label ?loc1.}} LIMIT 1  } \n"
					+ "OPTIONAL { SELECT ?y WHERE {?x vtio:isPartOf ?y. ?y rdfs:label ?loc2. FILTER(lang(?loc2)='"
					+ ServerConfig.LANGUAGE_CODE
					+ "')} LIMIT 1 }  \n"
					+ "OPTIONAL { SELECT ?z WHERE {?y vtio:isPartOf ?z. ?z rdfs:label ?loc3. FILTER(lang(?loc3)='"
					+ ServerConfig.LANGUAGE_CODE
					+ "')} LIMIT 1  } \n"
					// end update 10-06-2012
					+ "OPTIONAL {?z vtio:isPartOf ?k. ?k rdfs:label ?loc4. FILTER(lang(?loc4)='"
					+ ServerConfig.LANGUAGE_CODE
					+ "')} \n"
					+ "}} LIMIT 1} \n"
					// end update 20-06-2012
					+ "OPTIONAL {select ?phone where {?uri vtio:hasContact ?contact. ?contact vtio:hasPhoneNumber ?phone.  }  LIMIT 1   } \n"
					+ " }  \n";
			for (int i = offset + 1; i < offset + limit; i++) {
				// Lay ArrayList chua tap thong tin tra ve cho mot instance
				ArrayList<String> resultString = queryResult.get(i);
				// Lay URI
				// String instanceURI = resultString.get(0);
				placeURI = resultString.get(0);
				// Log.d("STAAR", "GEO SEARCH RESULT "+placeURI);
				if (!placeURI.contains("general")) {
					queryString = queryString
							+ " UNION {	{ SELECT ?label WHERE  {<"
							+ placeURI
							+ "> rdfs:label ?label."
							+ "FILTER(lang(?label)='"
							+ ServerConfig.LANGUAGE_CODE
							+ "')"
							+ "} LIMIT 1 } "
							+ "			{ {?uri rdfs:label ?label. FILTER(?uri= <"
							+ placeURI
							+ ">)}  }"
							+ "		 	OPTIONAL{SELECT ?abstract WHERE { <"
							+ placeURI
							+ "> vtio:hasAbstract ?abstract} LIMIT 1 }"
							+ "			OPTIONAL{SELECT ?location WHERE { <"
							+ placeURI
							+ "> vtio:hasLocation ?location.} LIMIT 1 }"
							+
							/*
							 * "			OPTIONAL{<"+placeURI+
							 * "> rdf:type ?typeURI. ?typeURI rdfs:label ?type. FILTER regex(Str(?typeURI), 'vtio') "
							 * +
							 * "FILTER(lang(?type)='"+ServerConfig.LANGUAGE_CODE
							 * +"')} " +
							 */
							// dungct: chinh lai de support ontology co nhan
							// tieng Viet khong dau ben canh co dau.
							"			OPTIONAL {<"
							+ placeURI
							+ "> rdf:type ?typeURI. {SELECT ?type WHERE {?typeURI rdfs:label ?type . FILTER(lang(?type)='"
							+ ServerConfig.LANGUAGE_CODE
							+ "')} LIMIT 1 } "
							+ "FILTER regex(Str(?typeURI), 'vtio') } "
							+
							// end modification
							"			OPTIONAL{SELECT ?isWellknown WHERE { <"
							+ placeURI
							+ "> vtio:isWellKnown ?isWellknown. } LIMIT 1 }"
							+ "			OPTIONAL{SELECT ?long WHERE { <"
							+ placeURI
							+ "> vtio:hasLongtitude ?long. } LIMIT 1 }"
							+ "			OPTIONAL{SELECT ?lat WHERE { <"
							+ placeURI
							+ "> vtio:hasLatitude ?lat. } LIMIT 1 }"
							+ "			OPTIONAL{SELECT ?ratingNum WHERE { <"
							+ placeURI
							+ "> vtio:hasRating ?rating. ?rating vtio:hasNumberStar ?ratingNum.} LIMIT 1 }"
							+ "			OPTIONAL{SELECT ?imageURL WHERE { <"
							+ placeURI
							+
							// "> vtio:hasMedia ?media. ?media rdf:type vtio:Image. ?media vtio:hasURL ?imageURL.} LIMIT 1 } }"
							"> vtio:hasMedia ?media. ?media rdf:type vtio:Image. ?media vtio:hasURL ?imageURL.} LIMIT 1 } "

							// chuyen } o cuoi vao sau menh de OPTIONAL cho dia
							// chi o cuoi cung
							// ; mot phan query ban dau dung o day
							// }
							// // dungct 22-06-2012 xu ly truong hop 1 duong
							// thuoc nhieu hon 1 quan - chi hien 1 dia diem thay
							// vi nhieu
							//
							// +
							// "OPTIONAL { SELECT ?loc1 ?loc2 ?loc3 ?loc4 where {  "
							// + "OPTIONAL { "
							// + "<"
							// + placeURI
							// +
							// "> vtio:hasLocation ?x. ?x vtio:isPartOf ?y. \n"
							// +
							// "OPTIONAL { SELECT ?x WHERE {{?x vtio:hasValue ?loc1.} UNION {?x rdfs:label ?loc1.}} LIMIT 1  }}} \n"
							//
							// // bo sung query lay dia chi von duoc goi nhieu
							// lan trong
							// // + "OPTIONAL { " +
							// //
							// "<"+placeURI+"> vtio:hasLocation ?x. ?x vtio:isPartOf ?y. "
							// +
							// // "OPTIONAL { ?x vtio:hasValue ?loc1. } " +
							// +
							// "OPTIONAL {{SELECT ?y WHERE {?x vtio:isPartOf ?y. ?y rdfs:label ?loc2. FILTER(lang(?loc2)='"+ServerConfig.LANGUAGE_CODE+"')} LIMIT 1}  } "
							// +
							// "OPTIONAL { {SELECT ?z WHERE {?y vtio:isPartOf ?z. ?z rdfs:label ?loc3. FILTER(lang(?loc3)='"+ServerConfig.LANGUAGE_CODE+"')} LIMIT 1} } "
							// +
							// // them 1 } o sau LIMIT
							// "OPTIONAL {{?z vtio:isPartOf ?k. ?k rdfs:label ?loc4. FILTER(lang(?loc4)='"+ServerConfig.LANGUAGE_CODE+"')} } }"
							// +
							// "OPTIONAL {select ?phone where {?uri vtio:hasContact ?contact. ?contact vtio:hasPhoneNumber ?phone.  }  LIMIT 1  }} ";
							// dungct 22-06-2012 xu ly truong hop 1 duong thuoc
							// nhieu hon 1 quan - chi hien 1 dia diem thay vi
							// nhieu
							+ "OPTIONAL { SELECT ?loc1 ?loc2 ?loc3 ?loc4 where {  "
							+ "OPTIONAL { "
							+ "<"
							+ placeURI
							+ "> vtio:hasLocation ?x. ?x vtio:isPartOf ?y. \n"
							+ "OPTIONAL { SELECT ?x WHERE {{?x vtio:hasValue ?loc1.} UNION {?x rdfs:label ?loc1.}} LIMIT 1  } \n"
							/*
							 * +
							 * "OPTIONAL {?x vtio:isPartOf ?y. ?y rdfs:label ?loc2. FILTER(lang(?loc2)='"
							 * + languageCode + "')  }  \n" +
							 * "OPTIONAL { ?y vtio:isPartOf ?z. ?z rdfs:label ?loc3. FILTER(lang(?loc3)='"
							 * + languageCode + "')  } \n"
							 */
							// dungct 10-06-2012: chinh lai de support ontology
							// co nhan tieng Viet khong dau ben canh co dau - do
							// duong pho
							// quan hien tai co hai nhan tieng Viet
							+ "OPTIONAL { SELECT ?y WHERE {?x vtio:isPartOf ?y. ?y rdfs:label ?loc2. FILTER(lang(?loc2)='"
							+ ServerConfig.LANGUAGE_CODE
							+ "')} LIMIT 1 }  \n"
							+ "OPTIONAL { SELECT ?z WHERE {?y vtio:isPartOf ?z. ?z rdfs:label ?loc3. FILTER(lang(?loc3)='"
							+ ServerConfig.LANGUAGE_CODE
							+ "')} LIMIT 1  } \n"
							// end update 10-06-2012
							+ "OPTIONAL {?z vtio:isPartOf ?k. ?k rdfs:label ?loc4. FILTER(lang(?loc4)='"
							+ ServerConfig.LANGUAGE_CODE
							+ "')} \n"
							+ "}} LIMIT 1} \n"
							// end update 20-06-2012
							+ "OPTIONAL {select ?phone where {?uri vtio:hasContact ?contact. ?contact vtio:hasPhoneNumber ?phone.  }  LIMIT 1   } \n"
							+ " }  \n";

				}
			}
			// queryString = queryString +
			// "FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')"+
			// "FILTER(lang(?type)='"+ServerConfig.LANGUAGE_CODE+"')"
			queryString = queryString + "}";
		}
		Log.v("QUERY", "QUERY  = " + queryString);
		long a = System.currentTimeMillis();
		Log.v("TIME", "begin: " + a);

		ArrayList<ArrayList<String>> placeInfo = services.executeQuery(
				queryString, false);
		Log.v("TIME", "begin: " + (System.currentTimeMillis() - a));
		Log.v("QUERY", "INFO = " + placeInfo.size());
		if (placeInfo.size() == 0) {
			countLoopQuery++;
			if (countLoopQuery == 5)
				return;
			loadInstanceDetails(offset, limit);
			return;
		}
		// Log.v("QUERYGEO", "INFO = " + placeInfo.size());
		// try {
		// placeInfo = service.executeQuery(queryString, false);
		// } catch (Exception e) {
		// Toast.makeText(ctx, "Network error!", Toast.LENGTH_LONG);
		// finish();
		// }
		/*
		 * if (placeInfo == null) { // Toast.makeText(ctx, "Error",
		 * Toast.LENGTH_LONG); finish(); }
		 */

		// Doc vao cac doi tuong DataPlaceSimple
		for (int i = 0; i < placeInfo.size(); i++) {

			ArrayList<String> resultString = placeInfo.get(i);

			String uri = "";
			try {
				uri = resultString.get(0).replace(
						"@" + ServerConfig.LANGUAGE_CODE, "");
			} catch (Exception e) {
			}

			String label = "";
			try {
				label = resultString.get(1).replace(
						"@" + ServerConfig.LANGUAGE_CODE, "");
			} catch (Exception e) {
			}

			String type = "";
			try {
				type = resultString.get(2).replace(
						"@" + ServerConfig.LANGUAGE_CODE, "");
			} catch (Exception e) {
			}

			String location = "";
			try {
				location = resultString.get(3);
			} catch (Exception e) {
			}

			String isWellknown = "";
			try {
				isWellknown = resultString.get(4);
			} catch (Exception e) {
				// TODO: handle exception
			}

			String hasAbstract = "";
			try {
				hasAbstract = resultString.get(5);
			} catch (Exception e) {
			}

			String imageURL = "";
			try {
				imageURL = resultString.get(6);
			} catch (Exception e) {
			}

			String ratingLabel = "0";
			try {
				if (!resultString.get(7).contains("anyType")) {
					ratingLabel = resultString.get(7).replace(
							"^^" + NameSpace.xsd + "integer", "");
				}
			} catch (Exception e) {
			}
			// add by dungct
			String longValue = "";
			try {
				longValue = resultString.get(8).replace(
						"^^" + NameSpace.xsd + "double", "");
			} catch (Exception e) {

			}

			String latValue = "";
			try {
				latValue = resultString.get(9).replace(
						"^^" + NameSpace.xsd + "double", "");
			} catch (Exception e) {

			}
			// bo sung them phan lay du lieu dia chi tao thanh xau - dungct
			// index loc1 tuong ung chi so 10
			String placeFullAddress = "";
			String partOfAddress = "";
			for (int j = 10; j < resultString.size() - 1; j++) {
				if (!(resultString.get(j).contains("anyType"))) {
					if (j != 10) {

						partOfAddress = "-"
								+ resultString
										.get(j)
										.replace(
												"^^" + NameSpace.xsd + "string",
												"")
										.replace(
												"@"
														+ ServerConfig.LANGUAGE_CODE,
												"");
						if (partOfAddress.indexOf("street") != -1)
							partOfAddress = partOfAddress.replace("street",
									"str");
						if (partOfAddress.indexOf("Street") != -1)
							partOfAddress = partOfAddress.replace("Street",
									"str");
						if (partOfAddress.indexOf("district") != -1)
							partOfAddress = partOfAddress.replace("district",
									"dist");
						if (partOfAddress.indexOf("District") != -1)
							partOfAddress = partOfAddress.replace("District",
									"dist");
						if (partOfAddress.indexOf("city") != -1)
							partOfAddress = partOfAddress.replace("city", "");
						if (partOfAddress.indexOf("City") != -1)
							partOfAddress = partOfAddress.replace("City", "");
						if (partOfAddress.indexOf("country") != -1)
							partOfAddress = partOfAddress
									.replace("country", "");
						placeFullAddress = placeFullAddress + partOfAddress;

					} else {
						placeFullAddress = placeFullAddress
								+ resultString
										.get(j)
										.replace(
												"^^" + NameSpace.xsd + "string",
												"")
										.replaceAll(
												"@"
														+ ServerConfig.LANGUAGE_CODE,
												"");
					}
				}
			}

			String phoneNumber = "";
			try {
				phoneNumber = resultString.get(resultString.size() - 1)
						.replace("^^" + NameSpace.xsd + "string", "");
			} catch (Exception e) {

			}

			// end add
			FullDataInstance returnValue = new FullDataInstance();
			returnValue.setUri(uri);
			returnValue.setLabel(label.replace("^^" + NameSpace.xsd + "string",
					""));
			returnValue.setAbstractInfo(hasAbstract.replace("^^"
					+ NameSpace.xsd + "string", ""));
			returnValue.setType(type.replace("^^" + NameSpace.xsd + "string",
					""));
			returnValue.setWellKnown(isWellknown.replace("^^" + NameSpace.xsd
					+ "boolean", ""));
			// Bo khong goi service lay dia chi
			// returnValue.setAddress(services.getAddressOfPlace(uri));
			returnValue.setAddress(placeFullAddress);
			returnValue.setImageURL(imageURL.replace("^^" + NameSpace.xsd
					+ "string", ""));
			returnValue.setPhone(phoneNumber);
			// add by dungct

			try {

				if (longValue.equals(""))
					returnValue.setLongitude(0);
				else
					returnValue.setLongitude(Double.parseDouble(longValue));
				if (latValue.equals(""))
					returnValue.setLatitude(0);
				else
					returnValue.setLatitude(Double.parseDouble(latValue));
			} catch (Exception e) {
				// Log.v("LOI", placeURI + "Lo:" + longValue + "La:"+ latValue
				// );
				returnValue.setLatitude(0);
				returnValue.setLongitude(0);
				e.printStackTrace();
			}
			// end add
			// Log.d("IMAGES", "URL: "+returnValue.getImageURL());
			// Log.d("LONGLAT", "Long: "+returnValue.getLongtitude() +
			// " Lat:"+returnValue.getLatitude());

			returnValue.setRatingNum(Integer.valueOf(ratingLabel));
			Log.v("COUNT", "list: " + (listPlaceDataSimple == null)
					+ " return: " + (returnValue == null));
			listPlaceDataSimple.add(returnValue);
			Log.d("QUERY", "uri " + uri);
		}

	}

	public void cancelAsynTask() {
		if (loadInstanceDetails != null) {
			loadInstanceDetails.cancel(true);
		}

	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

}
