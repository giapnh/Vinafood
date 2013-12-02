package hust.hgbk.vtio.vinafood.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.customViewAdapter.ReturnWebViewAdapter;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;

public class ShowImageOfPlaceView extends LinearLayout {
	Context context;
	String uri;
	VtioCoreService service;
	//ImageView imageView;
	CustomGalleryView gallery;
	LinearLayout progressLayout;
	LinearLayout contentLayout;
	Dialog dialog;
	
	String currentURL;

	public ShowImageOfPlaceView(Context context, ArrayList<String> listUrl, Dialog dialog) {
		super(context);
		
		this.context = context;
		this.dialog = dialog;
		this.uri = uri;
		service = new VtioCoreService();
		
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.show_image_of_place_view, this, true);
		
		//imageView = (ImageView) findViewById(R.id.imageView);
		gallery = (CustomGalleryView) findViewById(R.id.gallery);
		progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
		contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
		
		float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics());
		gallery.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, (int) height));
		final ReturnWebViewAdapter adapter = new ReturnWebViewAdapter(context, listUrl, null);
		gallery.setAdapter(adapter);
		gallery.setSpacing(20);
		gallery.setSelection(0, true);
		
		//imageView.setScaleType(ScaleType.CENTER_CROP);
		/*float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics());
		float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics());*/
		//imageView.setLayoutParams(new LinearLayout.LayoutParams((int) width, (int) height));
		
		//loadImage();
	}
	
	private void loadImage() {
		new AsyncTask<Void, Void, ArrayList<String>>() {

			@Override
			protected ArrayList<String> doInBackground(Void... params) {
				String query = "select distinct ?url {" +
			            "<" + uri + "> vtio:hasURL ?url." +
						"}";
				ArrayList<ArrayList<String>> results = service.executeQuery(query, false);
				ArrayList<String> listURL = new ArrayList<String>();
				ArrayList<Bitmap> listBitmap = new ArrayList<Bitmap>();
				
			    for (int i=0; i<results.size(); i++) {
			    	String url = results.get(i).get(0).replace("^^"+NameSpace.xsd+"string", "");
			    	url = url.trim();
					url = url.replace(" ", "%20");
				    listURL.add(url);
				    
					//check can url connect?
				    /*try {
			            ConnectivityManager cm = (ConnectivityManager) context

			            .getSystemService(Context.CONNECTIVITY_SERVICE);

			            if (cm.getActiveNetworkInfo().isConnectedOrConnecting()) {

			                URL link = new URL(url);
			                HttpURLConnection urlc = (HttpURLConnection) link.openConnection();
			                urlc.setConnectTimeout(1000); // mTimeout is in seconds

			                urlc.connect();

			                if (urlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
			                	listURL.add(url);
			                } else {
			                    //return false;
			                }
			            }
			        } catch (IOException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        }*/
			    }
			    Log.v("TEST", "size: " + listURL.size());
			    return listURL;
			}

			@Override
			protected void onPostExecute(ArrayList<String> result) {
				if(result.size() == 0) {
					if(dialog.isShowing()) {
						dialog.dismiss();
					}
					
					Toast toast = Toast.makeText(context, "this palce has no image", Toast.LENGTH_LONG);
					toast.show();
					
					return;
				}
				
				float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics());
				gallery.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, (int) height));
				final ReturnWebViewAdapter adapter = new ReturnWebViewAdapter(context, result, null);
				gallery.setAdapter(adapter);
				gallery.setSpacing(20);
				gallery.setSelection(0, true);
				
				//String data = "<div style='border: solid #bb3333  1px; position: absolute;   top: 0px; left: 0px; width:300px; height:300px; overflow:hidden;  '><img src=\""+imageURL+"\" style=' background-color:transparent;' width='300dip' height='300dip'/></div>";
				//imageView.loadData(data, "text/html", "utf-8");
			    
			    progressLayout.setVisibility(GONE);
			    contentLayout.setVisibility(VISIBLE);
			}
			
		}.execute();
	}
	
	/*private void loadImage() {
		new AsyncTask<Void, Void, ArrayList<String>>() {

			@Override
			protected ArrayList<String> doInBackground(Void... params) {
				String query = "select distinct ?url {" +
			            "<" + uri + "> vtio:hasURL ?url." +
						"}";
				ArrayList<ArrayList<String>> results = service.executeQuery(query, false);
				ArrayList<String> listURL = new ArrayList<String>();
				ArrayList<Bitmap> listBitmap = new ArrayList<Bitmap>();
				
			    for (int i=0; i<results.size(); i++) {
			    	String url = results.get(i).get(0).replace("^^"+NameSpace.xsd+"string", "");
			    	url = url.trim();
					url = url.replace(" ", "%20");
				    listURL.add(url);
				    
					//check can url connect?
				    try {
			            ConnectivityManager cm = (ConnectivityManager) context

			            .getSystemService(Context.CONNECTIVITY_SERVICE);

			            if (cm.getActiveNetworkInfo().isConnectedOrConnecting()) {

			                URL link = new URL(url);
			                HttpURLConnection urlc = (HttpURLConnection) link.openConnection();
			                urlc.setConnectTimeout(1000); // mTimeout is in seconds

			                urlc.connect();

			                if (urlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
			                	listURL.add(url);
			                } else {
			                    //return false;
			                }
			            }
			        } catch (IOException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        }
				    
			    }
			    Log.v("TEST", "size: " + listURL.size());
			    return listURL;
			}

			@Override
			protected void onPostExecute(ArrayList<String> result) {
				float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics());
				gallery.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, (int) height));
				final ReturnWebViewAdapter adapter = new ReturnWebViewAdapter(context, result, gallery);
				gallery.setAdapter(adapter);
				gallery.setSpacing(20);
				gallery.setSelection(0, true);
				String imageURL = (String) adapter.getItem(0);
				currentURL = imageURL;
				SoftReference<Bitmap> ref = OntologyCache.imageOfPlace.get(imageURL);
				Log.v("CACHE", "size: " + OntologyCache.imageOfPlace.size());
				Bitmap bitmap =  null;
				if(ref != null) {
					bitmap = ref.get();
				}
				
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
					Log.v("CACHE", "true");
				} else {
					Log.v("CACHE", "false");
					OntologyCache.imageOfPlace.remove(imageURL);
					loadBitmapAndShow(imageURL);
				}
				//String data = "<div style='border: solid #bb3333  1px; position: absolute;   top: 0px; left: 0px; width:300px; height:300px; overflow:hidden;  '><img src=\""+imageURL+"\" style=' background-color:transparent;' width='300dip' height='300dip'/></div>";
				//imageView.loadData(data, "text/html", "utf-8");
			    
			    gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						String imageURL = (String) adapter.getItem(arg2);
						currentURL = imageURL;
						Bitmap bitmap = null;// = OntologyCache.imageOfPlace.get(imageURL);
						SoftReference<Bitmap> ref = OntologyCache.imageOfPlace.get(imageURL);
						Log.v("CACHE", "size: " + OntologyCache.imageOfPlace.size());
						if(ref != null) {
							bitmap = ref.get();
						}
						
						if (bitmap != null) {
							imageView.setImageBitmap(bitmap);
							Log.v("CACHE", "true");
						} else {
							Log.v("CACHE", "false");
							OntologyCache.imageOfPlace.remove(imageURL);
							loadBitmapAndShow(imageURL);
						}
						String imageURL = (String) adapter.getItem(arg2);
						String data = "<div style='border: solid #bb3333  1px; position: absolute;   top: 0px; left: 0px; width:300px; height:300px; overflow:hidden;  '><img src=\""+imageURL+"\" style=' background-color:transparent;' width='300dip' height='300dip'/></div>";
						imageView.loadData(data, "text/html", "utf-8");
						imageView.reload();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
			    
			    progressLayout.setVisibility(GONE);
			    contentLayout.setVisibility(VISIBLE);
			}
			
		}.execute();
	}*/
	
	/*boolean isChangeImage = false;
	boolean isLoading = false;
	private void loadBitmapAndShow(final String url) {
		new AsyncTask<String, Void, Bitmap>() {
            
			@Override
			protected void onPreExecute() {
				isLoading = true;
			}

			@Override
			protected Bitmap doInBackground(String... params) {
				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory
							.decodeStream((InputStream) new URL(params[0]).getContent());
					OntologyCache.imageOfPlace.put(params[0], new SoftReference<Bitmap>(bitmap));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					Log.v("TEST", "can not load image " + params[0]);
					e.printStackTrace();
				}
				
				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				if(url.equals(currentURL)) {
				    imageView.setImageBitmap(result);
				}
			}
			
		}.execute(url);
	}*/
	
	
	/*private void loadImage() {
		new AsyncTask<Void, Void, ArrayList<String>>() {

			@Override
			protected ArrayList<String> doInBackground(Void... params) {
				String query = "select distinct ?url {" +
			            "<" + uri + "> vtio:hasURL ?url." +
						"}";
				ArrayList<ArrayList<String>> results = service.executeQuery(query, false);
				ArrayList<String> listURL = new ArrayList<String>();
				ArrayList<Bitmap> listBitmap = new ArrayList<Bitmap>();
				
			    for (int i=0; i<results.size(); i++) {
			    	String url = results.get(i).get(0).replace("^^"+NameSpace.xsd+"string", "");
			    	url = url.trim();
					url = url.replace(" ", "%20");
				    listURL.add(url);
				    
				    try {
						Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
						listBitmap.add(bitmap);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						Log.v("TEST", "can not load image " + url);
						e.printStackTrace();
					}
			    }
			    Log.v("TEST", "size: " + listURL.size());
			    return listURL;
			}

			@Override
			protected void onPostExecute(ArrayList<String> result) {
				if(result.size() <= 0) {
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);
					result.add(bitmap);
				}
				int size = result.size();
				while(size > 0 & size < 6) {
					for(int i = 0; i < size; i ++) {
						result.add(result.get(i));
					}
					size = result.size();
				}
					float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics());
					gallery.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, (int) height));
					final ContinuousAdapter adapter = new ContinuousAdapter(context, result);
					gallery.setAdapter(adapter);
					gallery.setSpacing(20);
					gallery.setSelection(Integer.MAX_VALUE/2, true);
					Bitmap bitmap = adapter.getBitmapSelected(Integer.MAX_VALUE/2);
					imageView.setImageBitmap(bitmap);
				    
				    gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							Bitmap bitmap = adapter.getBitmapSelected(arg2);
							imageView.setImageBitmap(bitmap);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
				    
				    progressLayout.setVisibility(View.GONE);
				
			}
			
		}.execute();
	}*/
    
	
}
