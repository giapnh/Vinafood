package hust.hgbk.vtio.vinafood.customDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import hust.hgbk.vtio.vinafood.customview.PlaceAnnotationView;

public class PlaceAnnotationDialog extends Dialog{
	Context context;
	public PlaceAnnotationDialog(Context context, String placeURI) {
		super(context);
		this.context = context;
		PlaceAnnotationView view = new PlaceAnnotationView(context, placeURI);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
	}
}
