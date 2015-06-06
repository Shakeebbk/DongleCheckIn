package com.example.donglecheckin;

import java.io.IOException;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.donglecheckin.DCIConfig.CheckStatus;
import com.example.donglecheckin.SimpleGestureFilter.SimpleGestureListener;

public class DCIAlarm extends Activity implements SimpleGestureListener,  View.OnTouchListener{
	//Prefs
	public static final String MYPREFS = "DCISettings";
	Boolean OOFEnabled;
	Calendar endcalendar=null;
	Calendar startcalendar=null;
	Boolean isTouched=false;
	Boolean isSelected=false;
	private Handler handler, animhandler;
	
	private SimpleGestureFilter detector=null;
	
	DBProvider DB=null;
	PowerManager.WakeLock wl;
	
	Ringtone rG;
	
	TextView _view;
	ViewGroup _root;
	View _child1, _child2, _child3, animatedView;
	private int _xDelta;
	private int _yDelta;
	
	int n=0; 
	
	int backgroundimages[] = {R.drawable.alarm7, R.drawable.alarm6, R.drawable.alarm5, R.drawable.alarm4, R.drawable.alarm3, R.drawable.alarm2, R.drawable.alarm1, R.drawable.alarm8};

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED, 
									WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		
		Log.d("DCIAlarm", "onCreate:Create DB object");
		DB = new DBProvider(this, "DCI_DB");
		
		Log.d("DCIAlarm", "onCreate:Alarm Triggered");
		//Get the Prefs
		final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
		
		OOFEnabled = settings.getBoolean("OOFEnabled", false);
		startcalendar = Calendar.getInstance();
		startcalendar.set(settings.getInt("OOFStartYear", 2015), settings.getInt("OOFStartMonth", 1), settings.getInt("OOFStartDay", 1));

		endcalendar = Calendar.getInstance();
		endcalendar.set(settings.getInt("OOFEndYear", 2015), settings.getInt("OOFEndMonth", 1), settings.getInt("OOFEndDay", 1));
		
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTimeInMillis(System.currentTimeMillis());
		
		if((currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) ||
				(currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
			//end the alarm activity on weekends
			finish();
		}
		
		//check for OOF
		if(OOFEnabled) {
			Log.d("DCIAlarm", "OOF - OOFEnabled");
			 if((currentDate.getTimeInMillis() >= startcalendar.getTimeInMillis()) && (currentDate.getTimeInMillis() <= endcalendar.getTimeInMillis())) {
				 //OOF
				 Log.d("DCIAlarm", "OOF - Ending alarm");
				 try {
						wl.release();
						rG.stop();
					}
					catch(Throwable h) {
						Log.d("DCIAlarm#wakeLock", "Exception in releasing the wakelock-"+wl.isHeld());
				}
				finish();
			 }
		}
		
		setContentView(R.layout.activity_dcialarm);
		handleanimation();
		
		//cards
		_root = (ViewGroup)findViewById(R.id.AlarmLayout);
		_child1 = _root.findViewById(R.id.CheckInCard);
		_child1.setOnTouchListener(this);
		_child2 = _root.findViewById(R.id.AwayCard);
		_child2.setOnTouchListener(this);
		_child3 = _root.findViewById(R.id.LostCard);
		_child3.setOnTouchListener(this);
		
		//Acquire wake lock
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "DCIAlarm");
		wl.acquire();
		
		detector = new SimpleGestureFilter(this,this);
		isTouched = false;
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.AlarmLayout);
		//BitmapDrawable bG = (BitmapDrawable)layout.getResources().getDrawable(R.drawable.first);
		//layout.setBackground(bG);
		layout.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				BitmapDrawable bG;
				if((isTouched == false) && (event.getAction() == MotionEvent.ACTION_DOWN)) {
					isTouched=true;
					isSelected = false;
					Log.d("DCIAlarm#onTouch", "onTouch-active");
					//bG = (BitmapDrawable)v.getResources().getDrawable(R.drawable.second);
					//v.setBackground(bG);
				}
				if((isTouched == true) && (event.getAction() == MotionEvent.ACTION_UP)) {
					isTouched=false;
					Log.d("DCIAlarm#onTouch", "onTouch-release");
					if(!isSelected) {
						//bG = (BitmapDrawable)v.getResources().getDrawable(R.drawable.first);
						//v.setBackground(bG);
					}
				}
				return false;
			}
		});
		
		//Start location service - to acquire best possible location
		Log.d("locationService", "locationService intent creation");
		Intent serviceIntent = new Intent(this, com.example.donglecheckin.locationService.class);
		startService(serviceIntent);		
		
		//Handler
		handler = new Handler();
		handler.postDelayed(timeout, DCIConfig.ALARM_SCREEN_TIMEOUT_MS);

		Button checkIn = (Button)findViewById(R.id.CheckInButton);
		checkIn.setVisibility(View.INVISIBLE);
		checkIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DB.DBAdd(CheckStatus.DCI_LOGGED_IN);
				//Intent intent = new Intent(v.getContext(), CheckInLogs.class);
				//startActivity(intent);

				handler.removeCallbacksAndMessages(null);
				animhandler.removeCallbacksAndMessages(null);
				finish();
			}
		});
		Button Away = (Button)findViewById(R.id.AwayButton);
		Away.setVisibility(View.INVISIBLE);
		Away.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DB.DBAdd(CheckStatus.DCI_AWAY);
				//Intent intent = new Intent(v.getContext(), CheckInLogs.class);
				//startActivity(intent);

				handler.removeCallbacksAndMessages(null);
				animhandler.removeCallbacksAndMessages(null);
				finish();
			}
		});
		Button Lost = (Button)findViewById(R.id.LostButton);
		Lost.setVisibility(View.INVISIBLE);
		Lost.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DB.DBAdd(CheckStatus.DCI_LOST);
				//Intent intent = new Intent(v.getContext(), CheckInLogs.class);
				//startActivity(intent);

				handler.removeCallbacksAndMessages(null);
				animhandler.removeCallbacksAndMessages(null);
				finish();
			}
		});
	}
	
	
	private void handleanimation() {
		final LinearLayout layout = (LinearLayout)findViewById(R.id.alarmlinear);
		int timeout=0;
		 if(n==8) {
			 n=0;
			 timeout=2000;
			 layout.setBackground(getResources().getDrawable(backgroundimages[7], null));
		 }
		 else {
			 timeout=40;
		 }
		 animhandler = new Handler();
		 animhandler.postDelayed(new Runnable() {

		        @Override
		        public void run() {
		            // TODO Auto-generated method stub
		            // change image here
		            change();

		        }

		        private void change() {
		            // TODO Auto-generated method stub
		        	layout.setBackground(getResources().getDrawable(backgroundimages[n], null));
		        	n++;
		            handleanimation();
		        }
		    }, timeout);
		
	}
	
	private Runnable timeout = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d("DCIAlarm", "Timeout!");
			//Handle timeout
			DB.DBAdd(CheckStatus.DCI_AWAY);
			//Intent intent = new Intent(getApplicationContext(), CheckInLogs.class);
			//startActivity(intent);
			finish();
		}
	};
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
         this.detector.onTouchEvent(me);
       return super.dispatchTouchEvent(me);
    }
	protected void onResume() {
		super.onResume();
		Log.d("DCIAlarm", "onResume");
		isTouched = false;
		isSelected = false;
		handler = new Handler();
		handler.postDelayed(timeout, DCIConfig.ALARM_SCREEN_TIMEOUT_MS);
		handleanimation();
		//Play ringtone
		Uri alarmTone;
		if((rG == null)) {
			alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			rG = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);
		}
		if((rG == null)) {
			alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			rG = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);
		}
		try {
			if(!rG.isPlaying()) {
				rG.play();
			}
		}
		catch(Exception e) {
			
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dcialarm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("DCIAlarm#onDestroy", "onDestroy");
		rG.stop();
		handler.removeCallbacksAndMessages(null);
		animhandler.removeCallbacksAndMessages(null);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		handler.removeCallbacksAndMessages(null);
		animhandler.removeCallbacksAndMessages(null);
		try {
			wl.release();
			rG.stop();
		}
		catch(Throwable h) {
			Log.d("DCIAlarm#wakeLock", "Exception in releasing the wakelock-"+wl.isHeld());
		}
	}

	@Override
	public void onSwipe(int direction) {
		// TODO Auto-generated method stub
		Log.d("DCIAlarm#onTouch", "onSwipe - "+direction);
		switch(direction) {
		case SimpleGestureFilter.SWIPE_UP:
			isSelected = true;
			//layout = (RelativeLayout)findViewById(R.id.AlarmLayout);
			//bG = (BitmapDrawable)layout.getResources().getDrawable(R.drawable.final_red);
			//layout.setBackground(bG);
			//button = (Button) findViewById(R.id.LostButton);
			//button.performClick();
			break;
		case SimpleGestureFilter.SWIPE_DOWN:
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			isSelected = true;
			//layout = (RelativeLayout)findViewById(R.id.AlarmLayout);
			//bG = (BitmapDrawable)layout.getResources().getDrawable(R.drawable.final_green);
			//layout.setBackground(bG);
			//button = (Button) findViewById(R.id.CheckInButton);
			//button.performClick();
			break;
		case SimpleGestureFilter.SWIPE_RIGHT:
			isSelected = true;
			Log.d("DCIAlarm#onTouch", "onSwipe - RIGHT");
			//layout = (RelativeLayout)findViewById(R.id.AlarmLayout);
			//bG = (BitmapDrawable)layout.getResources().getDrawable(R.drawable.final_yellow);
			//layout.setBackground(bG);
			//button = (Button) findViewById(R.id.AwayButton);
			//button.performClick();
			break;
		}
	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		Log.d("DCIAlarm#onTouch", "onDoubleTap");
	}
	public boolean onTouch(View view, MotionEvent event) {
		Log.d("Motion", "OnTouch");
	    final int X = (int) event.getRawX();
	    final int Y = (int) event.getRawY();
	    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) view.getLayoutParams();
	    switch (event.getAction() & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_DOWN:
	            _xDelta = X - lParams.leftMargin;
	            _yDelta = Y - lParams.topMargin;
	            break;
	        case MotionEvent.ACTION_UP:
	            break;
	        case MotionEvent.ACTION_POINTER_DOWN:
	            break;
	        case MotionEvent.ACTION_POINTER_UP:
	            break;
	        case MotionEvent.ACTION_MOVE:
	        	lParams.leftMargin = X - _xDelta;
	            //layoutParams.topMargin = Y - _yDelta;
	            //layoutParams.rightMargin = -250;
	            //layoutParams.bottomMargin = -250;
	            view.setLayoutParams(lParams);
	            break;
	    }
	    _root.invalidate();
	    Log.d("Motion", "isSelected "+isSelected+"X "+X);
	    if((isSelected) && (X < DCIConfig.CARD_CHECKIN_DISTANCE)) {
	    	final LinearLayout layout = (LinearLayout)findViewById(R.id.alarmlinear);
	    	layout.setBackground(getResources().getDrawable(R.color.white, null));
	    	animhandler.removeCallbacksAndMessages(null);
	    	animatedView = view;
	    	TranslateAnimation linanim = new TranslateAnimation(0, -X+_xDelta, 0, 0);
			Log.d("Motion", "Y "+Y);
			linanim.setDuration(DCIConfig.CARD_MOVE_ANIM_TIME);
			linanim.setFillAfter(true);
			linanim.setAnimationListener(new MyAnimationListener());
			view.setAnimation(linanim);
			linanim.start();
	    }
	    return true;
	}
	private class MyAnimationListener implements AnimationListener{

	    @Override
	    public void onAnimationEnd(Animation animation) {
	    	Animation anim = (Animation)AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
    		LinearLayout rellayout = (LinearLayout)findViewById(R.id.alarmlinear);
    		rellayout.setAnimation(anim);
    		anim.start();
	    	switch(animatedView.getId()) {
	    	case R.id.CheckInCard:
	    		Log.d("#MyAnimationListener", "CheckIn");
    			findViewById(R.id.CheckInButton).performClick();
	    		break;
	    	case R.id.AwayCard:
	    		Log.d("#MyAnimationListener", "Awawy");
    			findViewById(R.id.AwayButton).performClick();
	    		break;
	    	case R.id.LostCard:
	    		Log.d("#MyAnimationListener", "Lost");
    			findViewById(R.id.LostButton).performClick();
	    		break;
	    	}
        }

	    @Override
	    public void onAnimationRepeat(Animation animation) {
	    }

	    @Override
	    public void onAnimationStart(Animation animation) {
	    }

	}
}