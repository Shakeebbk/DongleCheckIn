package com.example.donglecheckin;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
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
	Context _context;
	@Override
	public void onReceive(Context context, Intent intent1) {
		// TODO Auto-generated method stub
		_context = context;
		final SharedPreferences settings = context.getSharedPreferences(MYPREFS, (Integer) null);
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
		
		Intent intent = new Intent(context, DCIAlarm.class);
		PendingIntent operation = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		if(Alarm1Enabled) setAlarm(context, Alarm1, operation);
		
		intent = new Intent(context, DCIAlarm.class);
		operation = PendingIntent.getActivity(context, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		if(Alarm2Enabled) setAlarm(context, Alarm2, operation);
		
		intent = new Intent(context, DCIAlarm.class);
		operation = PendingIntent.getActivity(context, 3, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		if(Alarm3Enabled) setAlarm(context, Alarm3, operation);
	}
	
	//set alarm method
			private void setAlarm(Context context, Calendar alarmTime, PendingIntent operation) {
				AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

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

}
