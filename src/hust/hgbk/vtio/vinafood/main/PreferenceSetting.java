package hust.hgbk.vtio.vinafood.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.Window;
import hust.hgbk.vtio.vinafood.constant.XmlAdapter;

public class PreferenceSetting extends PreferenceActivity{
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.setting_layout);
		try {
			float curVer = Float.parseFloat(getResources().getString(R.string.current_version));
			float newVer = Float.parseFloat(XmlAdapter.getNewVersion(this));
			if (newVer - curVer > 0.0f){
				PreferenceScreen intentPref = getPreferenceManager().createPreferenceScreen(this);
		        intentPref.setIntent(new Intent().setAction(Intent.ACTION_VIEW)
		                .setData(Uri.parse("market://details?id=hust.se.vtio.icompanion#rate")));
		        intentPref.setTitle("Update");
		        intentPref.setSummary(getResources().getString(R.string.new_version_available).replace("xxx", ""+newVer));
		        ((PreferenceCategory) findPreference("about_category")).addPreference(intentPref);
			}
			
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
	}

	
	
}
