package hust.hgbk.vtio.vinafood.main;
/*package hust.se.activity;

import hust.se.customViewAdapter.AppIconAdapter;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.GridView;

public class Test extends Activity {
    
	//private static 		 String URL="http://192.168.1.2:8080/VTIO-SERVER/VtioServicePort?WSDL";
	private static final String NAMESPACE = "http://vtioservice.se.hust/";
    private static final String METHOD_NAME = "getAllSubClassOf";
    private static final String SOAP_ACTION = NAMESPACE+METHOD_NAME;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        
        GridView gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(new AppIconAdapter(this));
        
        
        SoapObject soapRequestObject = new SoapObject(NAMESPACE, METHOD_NAME);
        soapRequestObject.addProperty("arg0", "Place");
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
        envelope.setOutputSoapObject(soapRequestObject);
        
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		SoapObject result = (SoapObject)envelope.bodyIn;
		String[] propertyNameArray = {"classComment","classLabel","classURI","hasSubClass","hasSuperClass"};
		for (int i=0; i<result.getPropertyCount(); i++){
			Log.d("OBJECT ", ""+i);
			SoapObject soapItem = (SoapObject) result.getProperty(i);
			for (int j=0; j<propertyNameArray.length; j++){
				String propertyName  = propertyNameArray[j];
				String propertyValue="";
				try {
					propertyValue = soapItem.getProperty(propertyName).toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.d("--Property ", propertyName+": "+propertyValue);
			}
		}
		
		System.out.println("xyz");
		
    }
}*/