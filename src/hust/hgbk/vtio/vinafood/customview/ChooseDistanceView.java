package hust.hgbk.vtio.vinafood.customview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import hust.hgbk.vtio.vinafood.main.R;

import java.text.DecimalFormat;

public class ChooseDistanceView extends LinearLayout {
	//	private ChooseDistanceClickListener chooseDistanceClickListener;
	public float distance = 1f;
	private EditText rangeText;
	private boolean isStopThread = true;
	private final float DELTA = 0.2f;

	public ChooseDistanceView(Context context, float d) {
		super(context);
		this.distance = d;
		LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(
		        Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.choose_distance_layout, this, true);

		Button minusButton = (Button) findViewById(R.id.btn_minus);
		Button plusButton = (Button) findViewById(R.id.btn_plus);
		rangeText = (EditText) findViewById(R.id.range_text);
		DecimalFormat myFormatter = new DecimalFormat("#.##");
		String output = myFormatter.format(distance);
		rangeText.setText(output);
		minusButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					distance = Float.parseFloat(rangeText.getText().toString().replace(',', '.'));
				} catch (Exception e) {
				}
				distance = distance - DELTA;
				if (distance < 0)
					distance = 0.0f;
				DecimalFormat myFormatter = new DecimalFormat("#.##");
				String output = myFormatter.format(distance);
				rangeText.setText(output);
			}
		});
		minusButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.v("TEST", "Press: " + event.getPressure());
				if (event.getAction() == event.ACTION_DOWN) {
					Thread t = changeFastDistanceThread(-DELTA);
					isStopThread = false;
					t.start();
				} else if (event.getAction() != event.ACTION_MOVE) {
					isStopThread = true;
				}
				return true;
			}
		});
		plusButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					distance = Float.parseFloat(rangeText.getText().toString().replace(',', '.'));
				} catch (Exception e) {
				}
				distance = distance + DELTA;
				if (distance < 0)
					distance = 0.0f;
				DecimalFormat myFormatter = new DecimalFormat("#.##");
				String output = myFormatter.format(distance);
				rangeText.setText(output);
			}
		});
		plusButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.v("TEST", "Press: " + event.getPressure());
				if (event.getAction() == event.ACTION_DOWN) {
					Thread t = changeFastDistanceThread(DELTA);
					isStopThread = false;
					t.start();
				} else if (event.getAction() != event.ACTION_MOVE) {
					isStopThread = true;
				}
				return true;
			}
		});
	}

	public float getDistance() {
		try {
			float f = Float.parseFloat(rangeText.getText().toString().replace(',', '.'));
			if (f < 0)
				return 0f;
			return f;
		} catch (Exception e) {
			return 1.0f;
		}
	}

	public Thread changeFastDistanceThread(final float delta) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isStopThread) {
					Message msg = handler.obtainMessage(0, delta);
					handler.sendMessage(msg);
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			float delta = (Float) msg.obj;
			try {
				distance = Float.parseFloat(rangeText.getText().toString().replace(',', '.'));
			} catch (Exception e) {
			}
			distance = distance + delta;
			if (distance < 0)
				distance = 0.0f;
			DecimalFormat myFormatter = new DecimalFormat("#.##");
			String output = myFormatter.format(distance);
			rangeText.setText(output);
			super.handleMessage(msg);
		}

	};

	public ChooseDistanceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.distance = 1f;
		LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(
		        Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.choose_distance_layout, this, true);
		Button minusButton = (Button) findViewById(R.id.btn_minus);
		Button plusButton = (Button) findViewById(R.id.btn_plus);
		rangeText = (EditText) findViewById(R.id.range_text);
		DecimalFormat myFormatter = new DecimalFormat("#.##");
		String output = myFormatter.format(distance);
		rangeText.setText(output);
		minusButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					distance = Float.parseFloat(rangeText.getText().toString().replace(',', '.'));
				} catch (Exception e) {
				}
				distance = distance - DELTA;
				if (distance < 0)
					distance = 0.0f;
				DecimalFormat myFormatter = new DecimalFormat("#.##");
				String output = myFormatter.format(distance);
				rangeText.setText(output);
			}
		});
		minusButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.v("TEST", "Press: " + event.getPressure());
				if (event.getAction() == event.ACTION_DOWN) {
					Thread t = changeFastDistanceThread(-DELTA);
					isStopThread = false;
					t.start();
				} else if (event.getAction() != event.ACTION_MOVE) {
					isStopThread = true;
				}
				return true;
			}
		});
		plusButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					distance = Float.parseFloat(rangeText.getText().toString().replace(',', '.'));
				} catch (Exception e) {
				}
				distance = distance + DELTA;
				if (distance < 0)
					distance = 0.0f;
				DecimalFormat myFormatter = new DecimalFormat("#.##");
				String output = myFormatter.format(distance);
				rangeText.setText(output);
			}
		});
		plusButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.v("TEST", "Press: " + event.getPressure());
				if (event.getAction() == event.ACTION_DOWN) {
					Thread t = changeFastDistanceThread(DELTA);
					isStopThread = false;
					t.start();
				} else if (event.getAction() != event.ACTION_MOVE) {
					isStopThread = true;
				}
				return true;
			}
		});
	}

	public ChooseDistanceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setDistanceToEditText(float distance) {
		this.distance = distance;
		DecimalFormat myFormatter = new DecimalFormat("#.##");
		String output = myFormatter.format(distance);
		rangeText.setText(output);
	}
}
