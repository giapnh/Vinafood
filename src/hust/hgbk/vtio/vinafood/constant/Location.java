package hust.hgbk.vtio.vinafood.constant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import hust.hgbk.vtio.vinafood.main.ChooseOnMap;
import hust.hgbk.vtio.vinafood.main.R;

public class Location {
	public static Float GEO_LAT_DEFAULT = 21.0287f;
	public static Float GEO_LON_DEFAULT = 105.8522f;

	private Float geoLat = GEO_LAT_DEFAULT;
	private Float geoLon = GEO_LON_DEFAULT;
	private static Location currentLocation = new Location();
	boolean isAccessing = false;

	synchronized public static Location getInstance() {
		return currentLocation;
	}

	synchronized public void setGeo(Float lat, Float lon)
			throws InterruptedException {
		if (isAccessing) {
			wait();
		}
		isAccessing = true;
		this.geoLat = lat;
		this.geoLon = lon;
		isAccessing = false;
		notifyAll();
	}

	synchronized public float getLatitude() throws InterruptedException {
		if (isAccessing) {
			wait();
		}
		return geoLat;
	}

	synchronized public float getLongtitude() throws InterruptedException {
		if (isAccessing) {
			wait();
		}
		return geoLon;
	}

	synchronized public static boolean isGetedLocation() {
		try {
			if (currentLocation.geoLat == 0f || currentLocation.geoLon == 0f) {
				return false;
			} else if (currentLocation.getLatitude() == GEO_LAT_DEFAULT
					&& currentLocation.getLongtitude() == GEO_LON_DEFAULT) {
				return false;
			} else
				return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static void showSettingLocationDialog(final Context ctx) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					// pauseSound();
					ctx.startActivity(new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					try {
						ctx.startActivity(new Intent(ctx, ChooseOnMap.class));

					} catch (NoClassDefFoundError e) {
						// TODO: handle exception
					}
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(ctx.getResources().getString(R.string.enable_gps))
				.setPositiveButton(
						ctx.getResources().getString(R.string.apply),
						dialogClickListener)
				.setNegativeButton(
						ctx.getResources().getString(R.string.select_on_map),
						dialogClickListener).show();
	}
}
