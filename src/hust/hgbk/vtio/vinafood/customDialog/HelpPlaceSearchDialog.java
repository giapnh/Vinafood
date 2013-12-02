package hust.hgbk.vtio.vinafood.customDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import hust.hgbk.vtio.vinafood.main.R;

public class HelpPlaceSearchDialog extends Dialog implements android.view.View.OnClickListener {
	Button cancelHelpPlaceSearchDialogButton;
	WebView webviewHelpPlaceSearchDialog;

	public HelpPlaceSearchDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help_place_search_dialog_layout);

		cancelHelpPlaceSearchDialogButton = (Button) findViewById(R.id.cancelHelpPlaceSearchDialogButton);
		cancelHelpPlaceSearchDialogButton.setOnClickListener(this);

		webviewHelpPlaceSearchDialog = (WebView) findViewById(R.id.helpContentWebView);
		//webviewHelpPlaceSearchDialog.loadUrl("http://www.google.com.vn/");
		webviewHelpPlaceSearchDialog.loadUrl("file:///android_asset/semantic_search_help.html");
	}

	@Override
	public void onClick(View view) {
		if (view == cancelHelpPlaceSearchDialogButton) {
			this.dismiss();
		}
	}
}
