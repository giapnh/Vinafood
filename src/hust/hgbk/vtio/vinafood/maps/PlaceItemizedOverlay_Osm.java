package hust.hgbk.vtio.vinafood.maps;

import hust.hgbk.vtio.vinafood.customDialog.PlaceAnnotationDialog;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;

public class PlaceItemizedOverlay_Osm extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;

	public PlaceItemizedOverlay_Osm(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		String placeURI = item.getSnippet();
		/*
		 * AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		 * dialog.setTitle(item.getTitle());
		 * dialog.setMessage(item.getSnippet());
		 * dialog.show();
		 */
		PlaceAnnotationDialog dialog = new PlaceAnnotationDialog(mContext, placeURI);
		dialog.show();

		return true;
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		return super.onTap(p, mapView);
	}
}
