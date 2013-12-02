package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import hust.hgbk.vtio.vinafood.main.R;

public class ChooseListView extends LinearLayout {
	Context context;
	ListView listView;
	Button okButton;
	Button cancelButton;

	public ChooseListView(Context context) {
		super(context);
		LayoutInflater li = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.choose_list_layout, this, true);

		listView = (ListView) findViewById(R.id.listView);
		okButton = (Button) findViewById(R.id.okButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
	}

	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public Button getOkButton() {
		return okButton;
	}

	public void setOkButton(Button okButton) {
		this.okButton = okButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

}
