package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import hust.hgbk.vtio.vinafood.main.R;

public class MapConfigView extends LinearLayout {
	Context context;
	RadioGroup mapTypeGroup;
	RadioButton trafficViewRadio;
	RadioButton satelliteViewRadio;
	Button btn_cancel;
	Button btn_ok;

	public MapConfigView(Context context) {
		super(context);
		this.context = context;

		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
		        Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.map_config_layout, this, true);

		mapTypeGroup = (RadioGroup) findViewById(R.id.mapTypeRadioGroup);
		trafficViewRadio = (RadioButton) findViewById(R.id.trafficRadio);
		satelliteViewRadio = (RadioButton) findViewById(R.id.satelliteRadio);
		btn_cancel = (Button) findViewById(R.id.map_setting_dialog_cancel_btn);
		btn_ok = (Button) findViewById(R.id.map_setting_dialog_ok_btn);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public RadioGroup getMapTypeGroup() {
		return mapTypeGroup;
	}

	public void setMapTypeGroup(RadioGroup mapTypeGroup) {
		this.mapTypeGroup = mapTypeGroup;
	}

	public RadioButton getTrafficViewRadio() {
		return trafficViewRadio;
	}

	public void setTrafficViewRadio(RadioButton trafficViewRadio) {
		this.trafficViewRadio = trafficViewRadio;
	}

	public RadioButton getSatelliteViewRadio() {
		return satelliteViewRadio;
	}

	public void setSatelliteViewRadio(RadioButton satelliteViewRadio) {
		this.satelliteViewRadio = satelliteViewRadio;
	}

	public Button getBtn_cancel() {
		return btn_cancel;
	}

	public void setBtn_cancel(Button btnCancel) {
		btn_cancel = btnCancel;
	}

	public Button getBtn_ok() {
		return btn_ok;
	}

	public void setBtn_ok(Button btnOk) {
		btn_ok = btnOk;
	}

}
