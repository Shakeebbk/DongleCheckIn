package com.example.donglecheckin;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CheckInLogs extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_in_logs);
		
		DBProvider DB = new DBProvider(this, "DCI_DB");
		
		Log.d("CheckInLogs","OnCreate");
		//Debug
		DB.DBAdd(DCIConfig.CheckStatus.DCI_LOGGED_IN);
		DB.DBAdd(DCIConfig.CheckStatus.DCI_LOGGED_IN);
		DB.DBAdd(DCIConfig.CheckStatus.DCI_LOGGED_IN);
		DB.DBAdd(DCIConfig.CheckStatus.DCI_AWAY);
		DB.DBAdd(DCIConfig.CheckStatus.DCI_LOST);
		
		ArrayList<DCILogClass> log=DB.DBGet();
		
		TextView Time = (TextView)findViewById(R.id.timestamp1);
		Time.setText(log.get(0).timestamp);
		Time = (TextView)findViewById(R.id.status1);
		Log.d("CheckInLogs", "Time.getHeight() "+Time.getHeight());
		Time.setTextSize(Time.getHeight()-2);
		Time.setText(log.get(0).status.toString());
		Time = (TextView)findViewById(R.id.location1);
		Time.setTextSize(12);
		Time.setText(log.get(0).location);

		Time = (TextView)findViewById(R.id.timestamp2);
		Time.setText(log.get(1).timestamp);
		Time = (TextView)findViewById(R.id.status2);
		Time.setText(log.get(1).status.toString());
		Time = (TextView)findViewById(R.id.location2);
		Time.setText(log.get(1).location);
		
		Time = (TextView)findViewById(R.id.timestamp3);
		Time.setText(log.get(2).timestamp);
		Time = (TextView)findViewById(R.id.status3);
		Time.setText(log.get(2).status.toString());
		Time = (TextView)findViewById(R.id.location3);
		Time.setText(log.get(2).location);
		
		Time = (TextView)findViewById(R.id.timestamp4);
		Time.setText(log.get(3).timestamp);
		Time = (TextView)findViewById(R.id.status4);
		Time.setText(log.get(3).status.toString());
		Time = (TextView)findViewById(R.id.location4);
		Time.setText(log.get(3).location);
		
		Time = (TextView)findViewById(R.id.timestamp5);
		Time.setText(log.get(4).timestamp);
		Time = (TextView)findViewById(R.id.status5);
		Time.setText(log.get(4).status.toString());
		Time = (TextView)findViewById(R.id.location5);
		Time.setText(log.get(4).location);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check_in_logs, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this.getApplicationContext(), DCISettingsActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
