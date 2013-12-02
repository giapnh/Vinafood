package hust.hgbk.vtio.vinafood.customDialog;
//package hust.se.vtio.customDialog;
//
//import hust.se.vtio.constant.NameSpace;
//import hust.se.vtio.icompanion.PlaceSearch;
//import hust.se.vtio.icompanion.R;
//import hust.se.vtio.ontology.PropertyData;
//import hust.se.vtio.query.Constraint;
//import hust.se.vtio.query.Variable;
//import hust.se.vtio.time.TimeFactory;
//import hust.se.vtioservice.VtioCoreService;
//
//import java.util.ArrayList;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.AdapterView.OnItemSelectedListener;
//
//public class AddConstraintDialog extends Dialog implements android.view.View.OnClickListener{
//	
//	TextView questionTextView;
//	Spinner  subjectSpinner;
//	Spinner  predicateSpinner;
//	Spinner  objectValueSpinner;
//	EditText dataValueEditText;
//	Button   saveConstraintButton;
//	Button   cancelConstraintDialogButton;
//	
//	Context context;
//	Constraint constraint;
//	ArrayList<PropertyData> listProperty;
//	VtioCoreService services = new VtioCoreService();
//	
//	public AddConstraintDialog(Context context) {
//		super(context);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.triple_item_layout);
//		this.context = context;
//		constraint= new Constraint();
//		
//		questionTextView = (TextView) findViewById(R.id.questionTextView);
//		subjectSpinner  = (Spinner) findViewById(R.id.subjectSpinner);
//		predicateSpinner = (Spinner) findViewById(R.id.predicateSpinner);
//		objectValueSpinner = (Spinner) findViewById(R.id.objectValueSpinner);
//		dataValueEditText = (EditText) findViewById(R.id.dataValueEditText);
//		saveConstraintButton = (Button) findViewById(R.id.saveConstraintButton);
//		cancelConstraintDialogButton = (Button) findViewById(R.id.cancelConstraintDialogButton);
//		
//		saveConstraintButton.setOnClickListener(this);
//		cancelConstraintDialogButton.setOnClickListener(this);
//		
//		/**
//		 * Do du lieu ten cac bien vao spinner subject
//		 */
//		ArrayList<String> listVarName = new ArrayList<String>();
//		for (int i=0; i<PlaceSearch.arrayVariable.size(); i++){
//			listVarName.add(PlaceSearch.arrayVariable.get(i).getName());
//		}
//		ArrayAdapter<String> subjectSpinnerAdapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, listVarName);
//		subjectSpinner.setAdapter(subjectSpinnerAdapter);
//		
//		// Su kien khi chon subject
//		subjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> adapter, View view,
//					int position, long id) {
//				Log.d("On Item Selected", "subject spinner selected item "+position);
//				// Lay ve URI lop cua subject duoc chon
//				String classURI = null;
//				for (int i=0; i<PlaceSearch.arrayVariable.size(); i++){
//					Variable var = PlaceSearch.arrayVariable.get(i);
//					if (var.getName().equals((String)adapter.getSelectedItem())){
//						classURI = var.getClassURI();
//						break;
//					}
//				}
//				Log.d("GET ALL PROPERTY ","START "+ TimeFactory.GET_CURRENT_TIME());
//				listProperty = services.getAllPropertyOfClass(classURI);
//				Log.d("GET ALL PROPERTY ", "FINISH "+TimeFactory.GET_CURRENT_TIME());
//				
//				ArrayList<String> listPropertyLabel = new ArrayList<String>();
//				for (int i=0; i<listProperty.size(); i++){
//					listPropertyLabel.add(listProperty.get(i).getPropertyLabel().replace("^^"+NameSpace.xsd+"string", ""));
//				}
//				ArrayAdapter<String> perdicateSpinnerAdapter = new ArrayAdapter<String>(AddConstraintDialog.this.context, android.R.layout.simple_spinner_item, listPropertyLabel);
//				predicateSpinner.setAdapter(perdicateSpinnerAdapter);
//				
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		// Chon xong predicate, kiem tra thuoc tinh va lay ve cac range cua predicate do
//		predicateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
//				Log.d("On Item Selected", "predicate spinner selected item "+position);
//				// Lay ra URI cua predicate
//				String predicateLabel = (String) adapter.getSelectedItem();
//				PropertyData predicateSelected = new PropertyData();
//				for (int i=0; i<listProperty.size(); i++){
//					if (listProperty.get(i).getPropertyLabel().contains(predicateLabel)){
//						predicateSelected = listProperty.get(i);
//						break;
//					}
//				}
//				
//				// Kiem tra xem predicate do la objec property hay data property
//				
//				//--- Neu la object property
//				
//				if (predicateSelected.isObjectProperty()){
//					// Lay ra list Class la range cua property do
//					
//					// Hien thi objectValueSpinner len
//					objectValueSpinner.setVisibility(View.VISIBLE);
//					// An dataValueEditText di
//					dataValueEditText.setVisibility(View.GONE);
//				}
//				
//				//-- Neu la data property
//				
//				else {
//					// Lay ve kieu gia tri  
//					// Hien thi dataValueEditText len
//					dataValueEditText.setVisibility(View.VISIBLE);
//					// An objectValueSpinner di
//					objectValueSpinner.setVisibility(View.GONE);
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				
//			}
//			
//		});
//	}
//
//	@Override
//	public void onClick(View view) {
//		if (view == saveConstraintButton){
//			
//		}
//		if (view == cancelConstraintDialogButton){
//			this.dismiss();
//		}
//	}
//}
