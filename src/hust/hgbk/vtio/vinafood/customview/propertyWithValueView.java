package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.main.NewInstanceDetails;
import hust.hgbk.vtio.vinafood.main.R;
import hust.hgbk.vtio.vinafood.ontology.PropertyWithValue;

public class propertyWithValueView extends LinearLayout {

	PropertyWithValue propertyWithValue;
	Context context;

	TextView textViewPropertyLabel;
	TextView textViewPropertyValue;

	LinearLayout linearLayoutObject;
	Button buttonShowDetails;
	String objectValueUri;

	public propertyWithValueView(final Context context, final PropertyWithValue propertyWithValue) {
		super(context);
		this.propertyWithValue = propertyWithValue;
		this.context = context;

		LayoutInflater li = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.property_with_value_layout, this, true);

		textViewPropertyLabel = (TextView) findViewById(R.id.textViewPropertyLabel);
		textViewPropertyValue = (TextView) findViewById(R.id.textViewPropertyValue);
		buttonShowDetails = (Button) findViewById(R.id.buttonShowDetails);
		linearLayoutObject = (LinearLayout) findViewById(R.id.linearLayoutObject);

		String label = propertyWithValue.getProperty().getPropertyLabel()
		        .replace("^^" + NameSpace.xsd + "string", "")
		        .replace("@" + ServerConfig.LANGUAGE_CODE, "");
		String value = propertyWithValue.getValue();
		value = value.replace("^^" + NameSpace.xsd + "string", "");
		value = value.replace("^^" + NameSpace.xsd + "double", "");
		value = value.replace("^^" + NameSpace.xsd + "boolean", "");
		value = value.replace("^^" + NameSpace.xsd + "integer", "");
		value = value.replace("^^" + NameSpace.xsd + "int", "");

		String valueLabel = propertyWithValue.getValueLabel();

		if (propertyWithValue.getProperty().isObjectProperty()) {
			buttonShowDetails.setVisibility(View.VISIBLE);
			objectValueUri = value;
			value = value.replace(NameSpace.vtio, "");
		} else {
			buttonShowDetails.setVisibility(View.GONE);
		}
		if (label.startsWith("có")) {
			label = label.replace("có", "");
		}
		textViewPropertyLabel.setText(label);
		if (valueLabel == null || valueLabel.length() == 0) {
			valueLabel = value;
		}

		textViewPropertyValue.setText(valueLabel);
		final String tValueLabel = valueLabel;

		buttonShowDetails.setFocusable(false);
		buttonShowDetails.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (propertyWithValue.getProperty().isObjectProperty()) {
					Intent intent = new Intent(context, NewInstanceDetails.class);
					intent.putExtra("instanceURI", objectValueUri);
					intent.putExtra("instanceLabel", tValueLabel);
					context.startActivity(intent);
				}
			}
		});
	}
}
