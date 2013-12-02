package hust.hgbk.vtio.vinafood.customDialog;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import hust.hgbk.vtio.vinafood.constant.Ontology;
import hust.hgbk.vtio.vinafood.customViewAdapter.ArrayClassAdapter;
import hust.hgbk.vtio.vinafood.customview.ChooseListView;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;

import java.util.ArrayList;

public class ChooseClassDialog extends Dialog  implements android.view.View.OnClickListener{
	Context context;
	
	ListView listClassView;
	Button okButton;
	Button cancelButton;
	
	public ChooseClassDialog(Context context) {
		super(context);
		this.context = context;
		ChooseListView view = new ChooseListView(context);
		this.setContentView(view);
		listClassView = view.getListView();
		okButton = view.getOkButton();
		cancelButton = view.getCancelButton();
		
		ArrayList<ClassDataSimple> allClass = Ontology.allClass;
		ArrayClassAdapter adapter = new ArrayClassAdapter(context, R.layout.simple_list_item_1, allClass);
		listClassView.setAdapter(adapter);
		
		cancelButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		this.dismiss();
	}

}
