package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.LabelIconUriSimple;

public class SubClassHorizontalView extends LinearLayout {
	static String CLASS_URI;
	public static String currentClassURI;
	String parentClassUri;
	TextView subClassButton;
	TextView subClassTextView;
	ClassDataSimple subClass;
	public LinearLayout parentView;
	public LinearLayout subLayout;
	public LinearLayout propertyLayout;
	boolean isDim;

	public SubClassHorizontalView(Context context, ClassDataSimple subClass,
	        LinearLayout parentViewLayout, String classURI) {
		super(context);
		// TODO Auto-generated constructor stub
		CLASS_URI = classURI;
		currentClassURI = classURI;
		LayoutInflater li = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.sub_class_horizontal_view, this, true);
		subClassButton = (TextView) findViewById(R.id.subclassButton);
		subClassTextView = (TextView) findViewById(R.id.subclassTextView);
		subLayout = (LinearLayout) findViewById(R.id.subClassLayout);
		this.subClass = subClass;
		this.parentView = parentViewLayout;

		//LabelIconSimple labelIconSimple = OntologyCache.hashMapLabelIcon.get(subClass.getUri());
		//lva: thay doi cach lay label, thay class LabelIconSimple bang class LabelIconUriSimple
		LabelIconUriSimple labelIconUriSimple = OntologyCache.uriOfIcon.get(subClass.getUri());
		if (labelIconUriSimple != null) {
			Resources resources = getResources();
			subClassButton.setBackgroundDrawable(resources.getDrawable(labelIconUriSimple
			        .getIconId()));
			subClassTextView.setText(subClass.getLabel());
		}
		subLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uri = SubClassHorizontalView.this.subClass.getUri();
				if (isDim) {
					for (int i = 0; i < parentView.getChildCount(); i++) {
						SubClassHorizontalView view = ((SubClassHorizontalView) parentView
						        .getChildAt(i));
						view.setDim();

						parentView.removeViewAt(i);
						parentView.addView(view, i);
					}
					currentClassURI = uri;
					unDim();
					setGrey();
				} else {
					if (currentClassURI.equals(uri)) {
						for (int i = 0; i < parentView.getChildCount(); i++) {
							SubClassHorizontalView view = ((SubClassHorizontalView) parentView
							        .getChildAt(i));

							//							view.setWhite();
							view.unDim();
							parentView.removeViewAt(i);
							parentView.addView(view, i);
						}
						currentClassURI = CLASS_URI;
					} else {
						for (int i = 0; i < parentView.getChildCount(); i++) {
							SubClassHorizontalView view = ((SubClassHorizontalView) parentView
							        .getChildAt(i));
							view.setDim();
							parentView.removeViewAt(i);
							parentView.addView(view, i);
						}
						currentClassURI = uri;
						unDim();
						setGrey();
					}
				}

			}
		});
	}

	public SubClassHorizontalView(Context context, ClassDataSimple subClass,
	        LinearLayout parentViewLayout, LinearLayout propertyLayout, String classURI) {
		super(context);
		// TODO Auto-generated constructor stub
		CLASS_URI = classURI;
		currentClassURI = classURI;
		LayoutInflater li = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.sub_class_horizontal_view, this, true);
		subClassButton = (TextView) findViewById(R.id.subclassButton);
		subClassTextView = (TextView) findViewById(R.id.subclassTextView);
		subLayout = (LinearLayout) findViewById(R.id.subClassLayout);
		this.subClass = subClass;
		this.parentView = parentViewLayout;
		this.propertyLayout = propertyLayout;

		//LabelIconSimple labelIconSimple = OntologyCache.hashMapLabelIcon.get(subClass.getUri());
		//lva: thay doi cach lay label, thay class LabelIconSimple bang class LabelIconUriSimple
		LabelIconUriSimple labelIconUriSimple = OntologyCache.uriOfIcon.get(subClass.getUri());
		if (labelIconUriSimple != null) {
			Resources resources = getResources();
			subClassButton.setBackgroundDrawable(resources.getDrawable(labelIconUriSimple
			        .getIconId()));
			subClassTextView.setText(subClass.getLabel());
		}
		subLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uri = SubClassHorizontalView.this.subClass.getUri();
				if (isDim) {
					for (int i = 0; i < parentView.getChildCount(); i++) {
						SubClassHorizontalView view = ((SubClassHorizontalView) parentView
						        .getChildAt(i));
						view.setDim();

						parentView.removeViewAt(i);
						parentView.addView(view, i);
					}
					currentClassURI = uri;
					unDim();
					setGrey();
				} else {
					if (currentClassURI.equals(uri)) {
						for (int i = 0; i < parentView.getChildCount(); i++) {
							SubClassHorizontalView view = ((SubClassHorizontalView) parentView
							        .getChildAt(i));

							//							view.setWhite();
							view.unDim();
							parentView.removeViewAt(i);
							parentView.addView(view, i);
						}
						currentClassURI = CLASS_URI;
					} else {
						for (int i = 0; i < parentView.getChildCount(); i++) {
							SubClassHorizontalView view = ((SubClassHorizontalView) parentView
							        .getChildAt(i));
							view.setDim();
							parentView.removeViewAt(i);
							parentView.addView(view, i);
						}
						currentClassURI = uri;
						unDim();
						setGrey();
					}
				}

				// Thay doi giao dien o day,

				//neu bam vao tram xang thi hien cai 
				if (currentClassURI.contains("Filling-Station")) {
					SubClassHorizontalView.this.propertyLayout.getChildAt(5).setVisibility(VISIBLE);
				} else {
					SubClassHorizontalView.this.propertyLayout.getChildAt(5).setVisibility(GONE);
					// bỏ dấu tick cua thuoc cty
					try {
						LinearLayout temp = (LinearLayout) SubClassHorizontalView.this.propertyLayout
						        .getChildAt(5);
						CheckBox tempBox = (CheckBox) temp.getChildAt(0);
						tempBox.setChecked(false);
					} catch (Exception e) {
						// TODO: handle exception
						Log.d("LỖI GÁN KIỂU LAYOUT", "EXCEPTION");
					}

				}
			}
		});
	}

	public String getUri() {
		return subClass.getUri();
	}

	public void setDim() {
		if (isDim == false) {
			subLayout.setBackgroundResource(R.drawable.light_blue_white);
			isDim = true;
		}

	}

	public void unDim() {
		//		if (isDim == true ){

		subLayout.setBackgroundResource(R.drawable.light_blue_white);
		isDim = false;
		//		}

	}

	public void setGrey() {
		subLayout.setBackgroundResource(R.drawable.grey_bg);
	}
	//	public void setWhite(){
	//		subLayout.setBackgroundResource(R.drawable.while_cyan_bg);
	//	}

}
