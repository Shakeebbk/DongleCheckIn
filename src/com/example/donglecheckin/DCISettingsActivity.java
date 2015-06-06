package com.example.donglecheckin;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class DCISettingsActivity extends Activity {
	//Prefs
		public static final String MYPREFS = "DCISettings";
		Boolean OOFEnabled;
		Calendar endcalendar=null;
		Calendar startcalendar=null;
		Boolean Alarm1Enabled;
		Boolean Alarm2Enabled;
		Boolean Alarm3Enabled;
		Calendar Alarm1=null;
		Calendar Alarm2=null;
		Calendar Alarm3=null;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			//Get the Prefs
			final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
			
			OOFEnabled = settings.getBoolean("OOFEnabled", false);
			startcalendar = Calendar.getInstance();
			startcalendar.set(settings.getInt("OOFStartYear", 2015), settings.getInt("OOFStartMonth", 1), settings.getInt("OOFStartDay", 1));

			endcalendar = Calendar.getInstance();
			endcalendar.set(settings.getInt("OOFEndYear", 2015), settings.getInt("OOFEndMonth", 1), settings.getInt("OOFEndDay", 1));
			
			Alarm1Enabled = settings.getBoolean("Alarm1Enabled", false);
			Alarm1 = Calendar.getInstance();
			Alarm1.set(0, 0, 0, settings.getInt("Alarm1Hour", 0), settings.getInt("Alarm1Mins", 0));
			
			Alarm2Enabled = settings.getBoolean("Alarm2Enabled", false);
			Alarm2 = Calendar.getInstance();
			Alarm2.set(0, 0, 0, settings.getInt("Alarm2Hour", 0), settings.getInt("Alarm2Mins", 0));
			
			Alarm3Enabled = settings.getBoolean("Alarm3Enabled", false);
			Alarm3 = Calendar.getInstance();
			Alarm3.set(0, 0, 0, settings.getInt("Alarm3Hour", 0), settings.getInt("Alarm3Mins", 0));
			
			setContentView(R.layout.dci_settings_layout);
			
			final CheckBox alarm1EnabledCheckBox = (CheckBox) findViewById(R.id.checkBox1);
			alarm1EnabledCheckBox.setChecked(Alarm1Enabled);
			final TextView alarm1TextView = (TextView) findViewById(R.id.alarm1Text);
			updateTextView(alarm1TextView, Alarm1Enabled, Alarm1, true);
			
			alarm1EnabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					Alarm1Enabled = isChecked;
					
					final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("Alarm1Enabled", Alarm1Enabled);
					editor.commit();
					
					alarm1TextView.performClick();
				}
			});
			alarm1TextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("DCISettingsActivity", "onClick");
					// TODO Auto-generated method stub
					if(Alarm1Enabled) {
						TimePickerDialog timePicker = new TimePickerDialog(v.getContext(), mOnTimeSetListener1, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);
						timePicker.show();
					}
					else {
						Intent intent = new Intent(getApplicationContext(), DCIAlarm.class);
						PendingIntent operation = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
						cancelAlarm(getApplicationContext(), operation);
					}
					updateTextView(alarm1TextView, Alarm1Enabled, Alarm1, true);
				}
			});
			
			final CheckBox alarm2EnabledCheckBox = (CheckBox) findViewById(R.id.checkBox2);
			alarm2EnabledCheckBox.setChecked(Alarm2Enabled);
			final TextView alarm2TextView = (TextView) findViewById(R.id.alarm2Text);
			updateTextView(alarm2TextView, Alarm2Enabled, Alarm2, true);
			
			alarm2EnabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					Alarm2Enabled = isChecked;
					
					final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("Alarm2Enabled", Alarm2Enabled);
					editor.commit();
					
					alarm2TextView.performClick();
				}
			});
			alarm2TextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("DCISettingsActivity", "onClick");
					// TODO Auto-generated method stub
					if(Alarm2Enabled) {
						TimePickerDialog timePicker = new TimePickerDialog(v.getContext(), mOnTimeSetListener2, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);
						timePicker.show();
					}
					else {
						Intent intent = new Intent(getApplicationContext(), DCIAlarm.class);
						PendingIntent operation = PendingIntent.getActivity(getApplicationContext(), 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
						cancelAlarm(getApplicationContext(), operation);
					}
					updateTextView(alarm2TextView, Alarm2Enabled, Alarm2, true);
				}
			});
			
			final CheckBox alarm3EnabledCheckBox = (CheckBox) findViewById(R.id.checkBox3);
			alarm3EnabledCheckBox.setChecked(Alarm3Enabled);
			final TextView alarm3TextView = (TextView) findViewById(R.id.alarm3Text);
			updateTextView(alarm3TextView, Alarm3Enabled, Alarm3, true);
			
			alarm3EnabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					Alarm3Enabled = isChecked;
					
					final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("Alarm3Enabled", Alarm3Enabled);
					editor.commit();
					
					alarm3TextView.performClick();
				}
			});
			alarm3TextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("DCISettingsActivity", "onClick");
					// TODO Auto-generated method stub
					if(Alarm3Enabled) {
						TimePickerDialog timePicker = new TimePickerDialog(v.getContext(), mOnTimeSetListener3, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);
						timePicker.show();
					}
					else {
						Intent intent = new Intent(getApplicationContext(), DCIAlarm.class);
						PendingIntent operation = PendingIntent.getActivity(getApplicationContext(), 3, intent, PendingIntent.FLAG_CANCEL_CURRENT);
						cancelAlarm(getApplicationContext(), operation);
					}
					updateTextView(alarm3TextView, Alarm3Enabled, Alarm3, true);
				}
			});
			
			final CheckBox OOFEnabledCheckBox = (CheckBox) findViewById(R.id.checkBox4);
			OOFEnabledCheckBox.setChecked(OOFEnabled);
			
			final TextView OOFStartTextView = (TextView)findViewById(R.id.textView4);
			updateTextView(OOFStartTextView, OOFEnabled, startcalendar, false);
			final TextView OOFEndTextView = (TextView)findViewById(R.id.textView5);
			updateTextView(OOFEndTextView, OOFEnabled, endcalendar, false);
			
			OOFEnabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					OOFEnabled = isChecked;
					
					final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("OOFEnabled", OOFEnabled);
					editor.commit();
					
					OOFStartTextView.performClick();
					updateTextView(OOFEndTextView, OOFEnabled, endcalendar, false);
				}
			});
			OOFStartTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("DCISettingsActivity", "onClick");
					// TODO Auto-generated method stub
					if(OOFEnabled) {
						DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), mOnDateSetListener1, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
						datePicker.show();
					}
					updateTextView(OOFStartTextView, OOFEnabled, startcalendar, false);
				}
			});
			OOFEndTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("DCISettingsActivity", "onClick");
					// TODO Auto-generated method stub
					if(OOFEnabled) {
						DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), mOnDateSetListener2, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
						datePicker.show();
					}
					updateTextView(OOFEndTextView, OOFEnabled, endcalendar, false);
				}
			});
		}
		
		private DatePickerDialog.OnDateSetListener mOnDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				Log.d("DCISettingsActivity", "onDateSet year "+year+" monthOfYear "+monthOfYear+" dayOfMonth "+dayOfMonth);
				
				startcalendar.set(Calendar.YEAR, year);
				startcalendar.set(Calendar.MONTH, monthOfYear);
				startcalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				startcalendar.set(Calendar.HOUR_OF_DAY, 0);
				startcalendar.set(Calendar.MINUTE, 0);
				final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("OOFStartYear", startcalendar.get(Calendar.YEAR));
				editor.putInt("OOFStartMonth", startcalendar.get(Calendar.MONTH));
				editor.putInt("OOFStartDay", startcalendar.get(Calendar.DAY_OF_MONTH));
				editor.commit();
				updateTextView((TextView)findViewById(R.id.textView4), OOFEnabled, startcalendar, false);
				findViewById(R.id.textView5).performClick();
			}
		};
		
		private DatePickerDialog.OnDateSetListener mOnDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				Log.d("DCISettingsActivity", "onDateSet year "+year+" monthOfYear "+monthOfYear+" dayOfMonth "+dayOfMonth);
				
				endcalendar.set(Calendar.YEAR, year);
				endcalendar.set(Calendar.MONTH, monthOfYear);
				endcalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				endcalendar.set(Calendar.HOUR_OF_DAY, 0);
				endcalendar.set(Calendar.MINUTE, 0);
				final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("OOFEndYear", endcalendar.get(Calendar.YEAR));
				editor.putInt("OOFEndMonth", endcalendar.get(Calendar.MONTH));
				editor.putInt("OOFEndDay", endcalendar.get(Calendar.DAY_OF_MONTH));
				editor.commit();
				updateTextView((TextView)findViewById(R.id.textView5), OOFEnabled, endcalendar, false);
			}
		};
		
		private TimePickerDialog.OnTimeSetListener mOnTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Log.d("DCISettingsActivity", "onTimeSet hourOfDay "+hourOfDay+" minute "+minute);
				Alarm1.setTimeInMillis(System.currentTimeMillis());
				Log.d("DCISettingsActivity", "onTimeSet:Date:"+Alarm1.get(Calendar.YEAR)+":"+Alarm1.get(Calendar.HOUR)+":"+Alarm1.get(Calendar.DAY_OF_MONTH));
				Log.d("DCISettingsActivity", "onTimeSet:Time"+Alarm1.get(Calendar.HOUR_OF_DAY)+":"+Alarm1.get(Calendar.MINUTE));
				Alarm1.set(Calendar.HOUR_OF_DAY, hourOfDay);
				Alarm1.set(Calendar.MINUTE, minute);
				Alarm1.set(Calendar.SECOND, 0);
				Log.d("DCISettingsActivity", "onTimeSet:Date:"+Alarm1.get(Calendar.YEAR)+":"+Alarm1.get(Calendar.HOUR)+":"+Alarm1.get(Calendar.DAY_OF_MONTH));
				Log.d("DCISettingsActivity", "onTimeSet:Time"+Alarm1.get(Calendar.HOUR_OF_DAY)+":"+Alarm1.get(Calendar.MINUTE));
								
				final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("Alarm1Hour", Alarm1.get(Calendar.HOUR_OF_DAY));
				editor.putInt("Alarm1Mins", Alarm1.get(Calendar.MINUTE));
				editor.commit();
				// update txtTime with the selected time
				updateTextView((TextView)findViewById(R.id.alarm1Text), Alarm1Enabled, Alarm1, true);
				
				//setAlarm
				Intent intent = new Intent(getApplicationContext(), DCIAlarm.class);
				PendingIntent operation = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				setAlarm(getApplicationContext(), Alarm1, operation);
			}
		};
		
		private TimePickerDialog.OnTimeSetListener mOnTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Log.d("DCISettingsActivity", "onTimeSet hourOfDay "+hourOfDay+" minute "+minute);
				Alarm2.setTimeInMillis(System.currentTimeMillis());
				Log.d("DCISettingsActivity", "onTimeSet:Date:"+Alarm2.get(Calendar.YEAR)+":"+Alarm2.get(Calendar.HOUR)+":"+Alarm2.get(Calendar.DAY_OF_MONTH));
				Log.d("DCISettingsActivity", "onTimeSet:Time"+Alarm2.get(Calendar.HOUR_OF_DAY)+":"+Alarm2.get(Calendar.MINUTE));
				Alarm2.set(Calendar.HOUR_OF_DAY, hourOfDay);
				Alarm2.set(Calendar.MINUTE, minute);
				Alarm2.set(Calendar.SECOND, 0);
				Log.d("DCISettingsActivity", "onTimeSet:Date:"+Alarm2.get(Calendar.YEAR)+":"+Alarm2.get(Calendar.HOUR)+":"+Alarm2.get(Calendar.DAY_OF_MONTH));
				Log.d("DCISettingsActivity", "onTimeSet:Time"+Alarm2.get(Calendar.HOUR_OF_DAY)+":"+Alarm2.get(Calendar.MINUTE));
								
				final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("Alarm2Hour", Alarm2.get(Calendar.HOUR_OF_DAY));
				editor.putInt("Alarm2Mins", Alarm2.get(Calendar.MINUTE));
				editor.commit();
				// update txtTime with the selected time
				updateTextView((TextView)findViewById(R.id.alarm2Text), Alarm2Enabled, Alarm2, true);
				
				//setAlarm
				Intent intent = new Intent(getApplicationContext(), DCIAlarm.class);
				PendingIntent operation = PendingIntent.getActivity(getApplicationContext(), 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				setAlarm(getApplicationContext(), Alarm2, operation);
			}
		};
		
		private TimePickerDialog.OnTimeSetListener mOnTimeSetListener3 = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Log.d("DCISettingsActivity", "onTimeSet hourOfDay "+hourOfDay+" minute "+minute);
				Alarm3.setTimeInMillis(System.currentTimeMillis());
				Log.d("DCISettingsActivity", "onTimeSet:Date:"+Alarm3.get(Calendar.YEAR)+":"+Alarm3.get(Calendar.HOUR)+":"+Alarm3.get(Calendar.DAY_OF_MONTH));
				Log.d("DCISettingsActivity", "onTimeSet:Time"+Alarm3.get(Calendar.HOUR_OF_DAY)+":"+Alarm3.get(Calendar.MINUTE));
				Alarm3.set(Calendar.HOUR_OF_DAY, hourOfDay);
				Alarm3.set(Calendar.MINUTE, minute);
				Alarm3.set(Calendar.SECOND, 0);
				Log.d("DCISettingsActivity", "onTimeSet:Date:"+Alarm3.get(Calendar.YEAR)+":"+Alarm3.get(Calendar.HOUR)+":"+Alarm3.get(Calendar.DAY_OF_MONTH));
				Log.d("DCISettingsActivity", "onTimeSet:Time"+Alarm3.get(Calendar.HOUR_OF_DAY)+":"+Alarm3.get(Calendar.MINUTE));
								
				final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("Alarm3Hour", Alarm3.get(Calendar.HOUR_OF_DAY));
				editor.putInt("Alarm3Mins", Alarm3.get(Calendar.MINUTE));
				editor.commit();
				// update txtTime with the selected time
				updateTextView((TextView)findViewById(R.id.alarm3Text), Alarm3Enabled, Alarm3, true);
				
				//setAlarm
				Intent intent = new Intent(getApplicationContext(), DCIAlarm.class);
				PendingIntent operation = PendingIntent.getActivity(getApplicationContext(), 3, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				setAlarm(getApplicationContext(), Alarm3, operation);
			}
		};
		
		//set alarm method
		private void setAlarm(Context context, Calendar alarmTime, PendingIntent operation) {
			AlarmManager alarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);

			Log.d("DCISettingsActivity", "setAlarm:Date:"+alarmTime.get(Calendar.YEAR)+":"+alarmTime.get(Calendar.HOUR)+":"+alarmTime.get(Calendar.DAY_OF_MONTH));
			Log.d("DCISettingsActivity", "setAlarm:Time:"+alarmTime.get(Calendar.HOUR_OF_DAY)+":"+alarmTime.get(Calendar.MINUTE));
			
			Log.d("DCISettingsActivity","setAlarm:Current system time-"+System.currentTimeMillis());
			Log.d("DCISettingsActivity","setAlarm:Alarm time-"+alarmTime.getTimeInMillis());

			if(alarmTime.getTimeInMillis() < System.currentTimeMillis()) {
				//If time has passed, set alarm the next day
				alarmTime.set(alarmTime.get(Calendar.YEAR), alarmTime.get(Calendar.MONTH), 1+alarmTime.get(Calendar.DAY_OF_MONTH));
			}
			long triggerAtMillis = alarmTime.getTimeInMillis();
			long intervalMillis = DCIConfig.REPEAT_ALARM_TIME_MS; //repeat every day
			
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, operation);
		}
		
		//cancel the alarm
		private void cancelAlarm(Context context, PendingIntent operation) {
			Log.d("DCISettingsActivity", "cancelAlarm: alarm cancelled!");
			AlarmManager alarm = (AlarmManager)context.getSystemService(ALARM_SERVICE);
			alarm.cancel(operation);
			operation.cancel();
		}
		
		//TextView updater
		private void updateTextView(TextView view, Boolean isEnabled, Calendar calendar, Boolean isAlarm) {
			view.setFocusable(false);
			if(isAlarm) {
				String AM_PM =  (calendar.get(Calendar.AM_PM)==0) ? "AM" : "PM";
				view.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE))+" "+AM_PM);
			}
			else {
				view.setText(String.format("%02d %s %04d", calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.MONTH,Calendar.SHORT, Locale.US), calendar.get(Calendar.YEAR)));
			}
			
			if(isEnabled) {
				view.setTextColor(Color.BLACK);
			}
			else {
				view.setTextColor(Color.LTGRAY);
			}
		}
}
