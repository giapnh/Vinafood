package hust.hgbk.vtio.vinafood.customDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import hust.hgbk.vtio.vinafood.main.R;

public class AskAnswerDialog extends Dialog implements OnClickListener {
	Context context;
	boolean ans;

	/**
	 * Views
	 */
	LinearLayout linearLayoutTrue, linearLayoutFalse;
	Button buttonOk;

	public AskAnswerDialog(Context context, boolean ans) {
		super(context);
		setContentView(R.layout.dialog_ask_result);
		this.setTitle("Answer");
		this.context = context;
		this.ans = ans;

		linearLayoutTrue = (LinearLayout) findViewById(R.id.linearLayoutTrue);
		linearLayoutFalse = (LinearLayout) findViewById(R.id.linearLayoutFalse);
		buttonOk = (Button) findViewById(R.id.buttonOK);

		buttonOk.setOnClickListener(this);

		if (ans) {
			linearLayoutTrue.setVisibility(View.VISIBLE);
			linearLayoutFalse.setVisibility(View.GONE);
		} else {
			linearLayoutTrue.setVisibility(View.GONE);
			linearLayoutFalse.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View arg0) {
		Log.d("", "DIALOG ONCLICK");
		if (ans) {
			((Activity) context).finish();
			this.dismiss();
		} else {
			((Activity) context).finish();
			this.dismiss();
		}
	}

}
