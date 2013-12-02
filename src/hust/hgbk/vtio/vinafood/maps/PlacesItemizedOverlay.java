package hust.hgbk.vtio.vinafood.maps;

import hust.hgbk.vtio.vinafood.customDialog.PlaceAnnotationDialog;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class PlacesItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private boolean isTapaple = false;

	public PlacesItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	public PlacesItemizedOverlay(Drawable defaultMarker, Context context, boolean isTapable) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		this.isTapaple = isTapable;
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	public GeoPoint getGeoPoint() {
		if (mOverlays.size() > 0) {
			return mOverlays.get(0).getPoint();
		} else
			return null;
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
		if (!isTapaple && placeURI.length() > 0) {
			PlaceAnnotationDialog dialog = new PlaceAnnotationDialog(mContext, placeURI);
			dialog.show();
		}

		return true;
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		if (isTapaple) {
			OverlayItem overlay = new OverlayItem(p, "hihi", "haha");
			mOverlays.clear();
			addOverlay(overlay);
		}

		return super.onTap(p, mapView);
	}
}
