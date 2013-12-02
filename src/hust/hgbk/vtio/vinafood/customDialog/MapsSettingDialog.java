package hust.hgbk.vtio.vinafood.customDialog;

import hust.hgbk.vtio.vinafood.customview.MapConfigView;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.maps.MapView;

public class MapsSettingDialog extends Dialog implements android.view.View.OnClickListener {
	Context context;
	Button btnCancel;
	Button btnOk;
	RadioButton satelliteView;
	RadioButton trafficView;
	MapView mapView;

	public MapsSettingDialog(Context context, MapView mapView) {
		super(context);
		this.context = context;
		this.setTitle("Map settings");

		this.mapView = mapView;
		MapConfigView configView = new MapConfigView(context);
		btnCancel = configView.getBtn_cancel();
		btnOk = configView.getBtn_ok();

		trafficView = configView.getTrafficViewRadio();
		satelliteView = configView.getSatelliteViewRadio();

		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);

		setContentView(configView);
	}

	@Override
	public void onClick(View view) {
		if (view == btnCancel) {
			this.dismiss();
		} else if (view == btnOk) {
			if (trafficView.isChecked()) {
				mapView.setSatellite(false);
				mapView.setTraffic(true);
			} else if (satelliteView.isChecked()) {
				mapView.setTraffic(false);
				mapView.setSatellite(true);
			}
			this.dismiss();
		}
	}
}
