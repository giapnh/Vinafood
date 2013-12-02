package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.main.R;

public class RelativeLayoutWithCloseButton extends RelativeLayout{
	TextView textView;
	
	

	public RelativeLayoutWithCloseButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Button button = new Button(context,attrs);
		button.setId(111);
		button.setBackgroundResource(R.drawable.balloon_overlay_close);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		addView(button, params);
		
		textView = new TextView(context, attrs);
		textView.setText(getResources().getString(R.string.you_are_stay_in)+" " + ServerConfig.currentCityLabel +"\n" + getResources().getString(R.string.change_city));
		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.addRule(RelativeLayout.LEFT_OF, 111);
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(15);
		textView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		addView(textView,params);
		
		LayoutParams layoutParams = this.generateLayoutParams(attrs);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		setLayoutParams(layoutParams);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setVisibility(GONE);
			}
		});
	}
	
	public void refresh(){
		textView.setText(getResources().getString(R.string.you_are_stay_in)+" " + ServerConfig.currentCityLabel +"\n"+ getResources().getString(R.string.change_city));
	}





	public RelativeLayoutWithCloseButton(Context context) {
		super(context);
		
	}

	public RelativeLayoutWithCloseButton(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
	}

	@Override
	public boolean isInEditMode() {
		// TODO Auto-generated method stub
		return super.isInEditMode();
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	
}
