package hust.hgbk.vtio.vinafood.customViewAdapter;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;

import java.util.List;

import android.content.Context;

public class ArrayPlaceResultOnMainAdapter extends ArrayPlaceResultAdapter {

	public ArrayPlaceResultOnMainAdapter(Context context,
			int textViewResourceId, List<FullDataInstance> objects) {
		super(context, textViewResourceId, objects);
	}

	private String classUri;
	private double geoLat;
	private double geoLon;
	private float radius;
	private boolean hasPreference;
	private String keyWord;

	public void loadPlaceDataInFirst(String classUri, double geoLat,
			double geoLon, float radius, boolean hasPreference, String keyWord) {
		this.classUri = classUri;
		this.geoLat = geoLat;
		this.geoLon = geoLon;
		this.radius = radius;
		this.hasPreference = hasPreference;
		this.keyWord = keyWord;
		loadPlaceDataInFirst();
	}

	String[] preferences;

	@Override
	protected FullDataInstance[] callServiceResult(int limit, int offset) {
		if (hasPreference && preferences == null) {
			try {
				preferences = new String[OntologyCache.preferUser.size()];
				for (int i = 0; i < OntologyCache.preferUser.size(); i++) {
					preferences[i] = OntologyCache.preferUser.get(i).getUri();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// return
		// soapServiceProxy.getiComCoreService().getFullDataInstanceWithPresAndRanking(classUri,
		// geoLat, geoLon, radius, hasPreference, keyWord, preferences, LIMIT,
		// offset, ServerConfig.LANGUAGE_CODE,
		// ServerConfig.VTIO_REPOSITORY_KEY);
		return soapServiceProxy.getiComCoreService()
				.getFullDataInstaceWithPreference(classUri, geoLat, geoLon,
						radius, hasPreference, keyWord, preferences, LIMIT,
						offset, ServerConfig.LANGUAGE_CODE,
						ServerConfig.VTIO_REPOSITORY_KEY);
	}

	@Override
	protected void callServiceLog(FullDataInstance[] fullDataInstances,
			int limit, int offset) throws Exception {
		// logProxy.getiComCoreService().logMainService(classUri, geoLat,
		// geoLon,
		// radius, keyWord, offset, limit,
		// SignInAccount.getAccountInfo(context).getEmail());
		// if (hasPreference && preferences.length > 0) {
		// logProxy.getiComCoreService().updatePreferenceStat(preferences);
		// logProxy.getiComCoreService()
		// .updatePlaceStat(getUriStrings(fullDataInstances), false,
		// false, true, false);
		// } else {
		// logProxy.getiComCoreService()
		// .updatePlaceStat(getUriStrings(fullDataInstances), true,
		// false, false, false);
		// }
	}

}
