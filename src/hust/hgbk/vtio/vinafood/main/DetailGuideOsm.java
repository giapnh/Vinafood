package hust.hgbk.vtio.vinafood.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import com.mapquest.android.maps.GeoPoint;
import hust.hgbk.vtio.vinafood.customViewAdapter.ListViewAdapter_GuideOsm;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DetailGuideOsm extends Activity {
	ArrayList<String> guideArray = new ArrayList<String>();
	ArrayList<String> iconUrlArray = new ArrayList<String>();	
	double[] temp = new double[4];
	String placeLabel;
	String distance;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_detail_guide_osm);
        //
        Bundle extra = getIntent().getExtras();
		temp = extra.getDoubleArray("latlon");
		placeLabel=extra.getString("label");
		
        GeoPoint start = new GeoPoint(temp[0], temp[1]);
        GeoPoint end = new GeoPoint(temp[2], temp[3]);
        //
        try {
			getDetailGuideOSM(start, end);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		
		TextView endPoint = (TextView) findViewById(R.id.end_osm);
		endPoint.setText("Target : "+placeLabel);
		
		TextView disText = (TextView) findViewById(R.id.distance_osm);
		disText.setText("Distance :  " +distance+" km");
		//
		
		ListView listView = (ListView) findViewById(R.id.list_guide_osm);
		
		ClassDataSimple[] data=new ClassDataSimple[guideArray.size()];
		Log.v("", "xxxxxxxxxxxxxxxxx   : "+data.length);
		for(int i=0;i<data.length;i++){
			data[i]=new ClassDataSimple();
			data[i].setLabel(guideArray.get(i));
			data[i].setUri(iconUrlArray.get(i));
		}
		ListViewAdapter_GuideOsm listViewAdapter=new ListViewAdapter_GuideOsm(this, R.layout.listview_item_row_osm, data);
		listView.setAdapter(listViewAdapter);
		
    }    
    
    private void getDetailGuideOSM(GeoPoint start, GeoPoint end) throws IOException, JSONException{
    	//tao url
    	StringBuilder urlString = new StringBuilder();
    	urlString.append("http://mapquestapi.com/directions/v1/route?key=");
    	urlString.append("Fmjtd%7Cluua2gu22l%2Cbn%3Do5-hfzxl");
    	urlString.append("&from={latLng:{lat:"+String.valueOf(start.getLatitude())
    			+",lng:"+String.valueOf(start.getLongitude())+"}}");
    	urlString.append("&to={latLng:{lat:"+String.valueOf(end.getLatitude())
    			+",lng:"+String.valueOf(end.getLongitude())+"}}&unit=k");
    	//request
		URL url=new URL(urlString.toString());
		HttpURLConnection httpconn=(HttpURLConnection)url.openConnection();
		StringBuilder response=new StringBuilder();
		if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
	      {
	          BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
	          String strLine = null;
	          while ((strLine = input.readLine()) != null)
	          {
	              response.append(strLine);
	          }
	          input.close();
	      }
		
		String jsonOutput = response.toString();
		JSONObject jsonObject=new JSONObject(jsonOutput);
		JSONObject routeObject=jsonObject.getJSONObject("route");
		JSONArray legArray=routeObject.getJSONArray("legs");
		JSONArray maneuversArray=legArray.getJSONObject(0).getJSONArray("maneuvers");		
		
		distance=routeObject.getString("distance");
		for(int i=0;i<maneuversArray.length();i++){
			guideArray.add(maneuversArray.getJSONObject(i).getString("narrative"));
			iconUrlArray.add(maneuversArray.getJSONObject(i).getString("iconUrl"));
		}
		
		for(int j=0;j<guideArray.size();j++){
			Log.v("", "111111  :  "+guideArray.get(j));
			Log.v("", "222222  :  "+iconUrlArray.get(j));
		}
			Log.v("", distance);
    }
}