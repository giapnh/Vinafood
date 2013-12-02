package hust.hgbk.vtio.vinafood.customViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.customview.CustomGalleryView;
import hust.hgbk.vtio.vinafood.main.R;

import java.util.ArrayList;
import java.util.Random;

public class ReturnWebViewAdapter extends BaseAdapter {
	Context context;
	ArrayList<String> listUrls;
	CustomGalleryView gallery;
	//ArrayList<ImageView> listImageViews;
	ArrayList<View> listViews;
	
	Random random = new Random();
	
	public ReturnWebViewAdapter(Context context, ArrayList<String> urls, CustomGalleryView gallery) {
		this.context = context;
		this.listUrls = urls;
		this.gallery = gallery;
		
		listViews = new ArrayList<View>();
		
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(String url : listUrls) {
			Log.i("listUrl", url);
			final View view = layoutInflater.inflate(R.layout.show_image_item_layout, null);
			view.setTag(url);
			WebView webView = (WebView) view.findViewById(R.id.webview);
			TextView textView = (TextView) view.findViewById(R.id.textview);
			textView.setTag(url);
			webView.setTag(url);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setHorizontalScrollBarEnabled(false);
			webView.setVerticalScrollBarEnabled(false);
			webView.setBackgroundColor(0);
			webView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			listViews.add(view);
			
			JavaScriptInterface JSInterface = new JavaScriptInterface(context, url);
	        webView.addJavascriptInterface(JSInterface, "JSInterface"); 
			
			String data = "<html>" +
			"<head>" +
			"<style>* {margin:0;padding:0;}</style>" +
			"<script language=\"JavaScript\" type=\"text/javascript\">"+
			"function ImgError(source){"+
			"    JSInterface.changeActivity();" +	
			"	return true;"+
			"}"+
			"function ScaleImage(srcwidth, srcheight, targetwidth, targetheight, fLetterBox) { " +

            "var result = { width: 0, height: 0, fScaleToTargetWidth: true };" +

            "if ((srcwidth <= 0) || (srcheight <= 0) || (targetwidth <= 0) || (targetheight <= 0)) {" +
            "     return result;" +
            "}" +

            // scale to the target width
            "var scaleX1 = targetwidth;" +
            "var scaleY1 = (srcheight * targetwidth) / srcwidth;" +

            // scale to the target height
            "var scaleX2 = (srcwidth * targetheight) / srcheight;" +
            "var scaleY2 = targetheight;" +

            // now figure out which one we should use
            "var fScaleOnWidth = (scaleX2 > targetwidth);" +
            "if (fScaleOnWidth) {" +
             "   fScaleOnWidth = fLetterBox;" +
            "}" +
            "else {" +
             "  fScaleOnWidth = !fLetterBox;" +
            "}" +

            "if (fScaleOnWidth) {" +
             "   result.width = Math.floor(scaleX1);" +
              "  result.height = Math.floor(scaleY1);" +
               " result.fScaleToTargetWidth = true;" +
            "}" +
            "else {" +
             "   result.width = Math.floor(scaleX2);" +
              "  result.height = Math.floor(scaleY2);" +
               " result.fScaleToTargetWidth = false;" +
            "}" +
            "result.targetleft = Math.floor((targetwidth - result.width) / 2);" +
            "result.targettop = Math.floor((targetheight - result.height) / 2);" +
            "document.getElementById('image2').width=result.width;" +
            "document.getElementById('image2').height=result.height;" +
            "document.getElementById('image2').left=result.targetleft;" +
            "document.getElementById('image2').top=result.targettopt;" +
            "}" +
            "</script>" +
            "<body>" +
            "<form>" +
            "<div style=\"width: 300px; height: 300px; border: overflow: hidden; position: relative;\">" +
            "<img id=\"image2\" style=\"position:absolute; align=\"center\";\" src=\"" + url + "\" alt=\"Pulpit rock\" onload=\"ScaleImage(this.width, this.height, 300, 300, false)\"  onerror=\"ImgError(this)\">" +
            "</div>" +
			"</form>" +
			"</body>"+
			"</html>";
			
			//String data = "<img src=\""+url+"\" style=' background-color:rgba(10,10,10,0);' width='300dip' height='300dip'/>";
			webView.loadData(data, "text/html", "utf-8");
			
			/*if(!listAliveUrl.contains(url)) {
			    loadContent(url);
			}*/
		}
		
		/*listImageViews = new ArrayList<ImageView>();
		for(String url : listUrls) {
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics());
			int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics());
			imageView.setLayoutParams(new Gallery.LayoutParams(width, height));
			//imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.icon));
			imageView.setBackgroundColor(Color.DKGRAY);
			listImageViews.add(imageView);
			
			loadContent(url, imageView);
		}*/
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listViews.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listUrls.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		/*int index = arg0 % listImageViews.size();
		return listImageViews.get(index);*/
		return listViews.get(arg0);
	}
	
	public class JavaScriptInterface {
        Context mContext;
        String url;

        /** Instantiate the interface and set the context */
        JavaScriptInterface(Context c, String url) {
            mContext = c;
            this.url = url;
        }

        public void changeActivity()
        {
        	((Activity) context).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					for(View view : listViews) {
						WebView webView = (WebView) view.findViewById(R.id.webview);
						TextView textView = (TextView) view.findViewById(R.id.textview);
						if(((String)webView.getTag()).equals(url)) {
					        webView.setVisibility(View.GONE);
					        textView.setVisibility(View.VISIBLE);
					        break;
						}
					}
				}
			});
        }
    }
}



/*private ArrayList<String> getCacheListAliveUrl() {
	File cacheFile = new File(context.getCacheDir().toString() + ALIVE_URL_FILE_NAME);
    if(cacheFile.exists()) {
    	try {
			FileInputStream fileInputStream = new FileInputStream(cacheFile);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			ArrayList<String> result = (ArrayList<String>) objectInputStream.readObject();
			return result;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    return new ArrayList<String>();
}

private void cacheListAliveUrl(String url) {
	listAliveUrl.add(url);
	File cacheFile = new File(context.getCacheDir().toString() + ALIVE_URL_FILE_NAME);
	if(cacheFile.exists()) {
		cacheFile.delete();
	}
	try {
		FileOutputStream fileOutputStream = new FileOutputStream(context.getCacheDir().toString() + ALIVE_URL_FILE_NAME);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(listAliveUrl);
		fileOutputStream.flush();
		objectOutputStream.flush();
		fileOutputStream.close();
		objectOutputStream.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		
	}
}

private ArrayList<String> getCacheListDieUrl() {
    File cacheFile = new File(context.getCacheDir().toString() + DIE_URL_FILE_NAME);
    if(cacheFile.exists()) {
    	try {
			FileInputStream fileInputStream = new FileInputStream(cacheFile);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			ArrayList<String> result = (ArrayList<String>) objectInputStream.readObject();
			return result;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    return new ArrayList<String>();
}

private void cacheListDieUrl(String url) {
	listDieUrl.add(url);
	File cacheFile = new File(context.getCacheDir().toString() + DIE_URL_FILE_NAME);
	if(cacheFile.exists()) {
		cacheFile.delete();
	}
	try {
		FileOutputStream fileOutputStream = new FileOutputStream(context.getCacheDir().toString() + DIE_URL_FILE_NAME);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(listDieUrl);
		fileOutputStream.flush();
		objectOutputStream.flush();
		fileOutputStream.close();
		objectOutputStream.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		
	}
}

private void loadContent(final String url) {
	new AsyncTask<Void, Void, Bitmap>() {

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap bitmap = null;
			
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if(bitmap == null || bitmap.getWidth() <= 1 || bitmap.getHeight() <= 1) {
				Log.v("TEST", "can not load image");
				for(int i = 0; i < listViews.size(); i ++) {
					View view = listViews.get(i);
					if(((String) view.getTag()).equals(url)) {
						TextView textView = (TextView) view.findViewById(R.id.textview);
						textView.setVisibility(View.VISIBLE);
						break;
					}
				}
				if(!listDieUrl.contains(url)) {
				    cacheListDieUrl(url);
				}
			} else {
				cacheListAliveUrl(url);
				Log.v("TEST", "Size: " + bitmap.getWidth() + "    " + bitmap.getHeight());
			}
		}
		
	}.execute();
}*/