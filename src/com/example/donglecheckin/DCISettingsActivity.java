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
						TimePickerDialog timePicker = new TimePickerDialog(v.getContext(), mOnTimeSetListener1, Alarm1.get(Calendar.HOUR_OF_DAY), Alarm1.get(Calendar.MINUTE), false);
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
						DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), mOnDateSetListener1, startcalendar.get(Calendar.YEAR), startcalendar.get(Calendar.MONTH), startcalendar.get(Calendar.DAY_OF_MONTH));
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
						DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), mOnDateSetListener2, endcalendar.get(Calendar.YEAR), endcalendar.get(Calendar.MONTH), endcalendar.get(Calendar.DAY_OF_MONTH));
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
		
		//set alarm method
		private void setAlarm(Context context, Calendar alarmTime, PendingIntent operation) {
			AlarmManager alarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);

			Log.d("DCISettingsActivity", "setAlarm:Date:"+alarmTime.get(Calendar.YEAR)+":"+alarmTime.get(Calendar.HOUR)+":"+alarmTime.get(Calendar.DAY_OF_MONTH));
			Log.d("DCISettingsActivity", "setAlarm:Time:"+alarmTime.get(Calendar.HOUR_OF_DAY)+":"+alarmTime.get(Calendar.MINUTE));
			
			Log.d("DCISettingsActivity","setAlarm:Current system time-"+System.currentTimeMillis());
			Log.d("DCISettingsActivity","setAlarm:Alarm time-"+alarmTime.getTimeInMillis());

			long triggerAtMillis = alarmTime.getTimeInMillis();
			long intervalMillis = 60*60*1000; //repeat every day
			
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
