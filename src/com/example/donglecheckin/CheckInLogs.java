package com.example.donglecheckin;

import java.util.ArrayList;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckInLogs extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_in_logs);
		
		Log.d("CheckInLogs","OnCreate");
		/*
		//Debug
		DB.DBAdd(DCIConfig.CheckStatus.DCI_LOGGED_IN);
		DB.DBAdd(DCIConfig.CheckStatus.DCI_LOGGED_IN);
		DB.DBAdd(DCIConfig.CheckStatus.DCI_LOGGED_IN);
		DB.DBAdd(DCIConfig.CheckStatus.DCI_AWAY);
		DB.DBAdd(DCIConfig.CheckStatus.DCI_LOST);*/
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("CheckInLogs", "onResume");
		DBProvider DB = new DBProvider(this, "DCI_DB");
		ArrayList<DCILogClass> log=DB.DBGet();
		TextView Time=null;
		TextView Status=null;
		TextView location=null;
		ImageView image=null;
		LinearLayout ll=null;
		
		Log.d("CheckInLogs", "log size-"+log.size());
		
		int linearLayouts[] = { R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5 };
		int timeViews[] = { R.id.timestamp1, R.id.timestamp2 , R.id.timestamp3, R.id.timestamp4, R.id.timestamp5};
		int statusViews[] = { R.id.status1, R.id.status2 , R.id.status3, R.id.status4, R.id.status5};
		int imageViews[] = { R.id.imageView1, R.id.imageView2 , R.id.imageView3, R.id.imageView4, R.id.imageView5};
		int locationViews[] = { R.id.location1, R.id.location2 , R.id.location3, R.id.location4, R.id.location5};
		
		BitmapDrawable bG = (BitmapDrawable)getApplicationContext().getResources().getDrawable(R.drawable.green);
		
		for(int n=0; n<5; n++) {
			Time = (TextView)findViewById(timeViews[n]);
			Status = (TextView)findViewById(statusViews[n]);
			image = (ImageView)findViewById(imageViews[n]);
			location = (TextView)findViewById(locationViews[n]);
			ll = (LinearLayout) findViewById(linearLayouts[n]);
			if(n>=log.size()) {
				Time.setVisibility(View.INVISIBLE);
				Status.setVisibility(View.INVISIBLE);
				location.setVisibility(View.INVISIBLE);
				image.setVisibility(View.INVISIBLE);
				ll.setBackgroundColor(getResources().getColor(R.color.white));
			}
			else {
				Log.d("CheckInLogs", "log.get(n).timestamp - "+log.get(n).timestamp);
				Time.setText(log.get(n).timestamp);
				Log.d("CheckInLogs", "Time.getText() - "+Time.getText());
				
				String StatusString;
				switch(log.get(n).status) {
				case DCI_LOGGED_IN:
					StatusString = "Logged In";
					bG = (BitmapDrawable)getApplicationContext().getResources().getDrawable(R.drawable.green);
					image.setImageDrawable(bG);
					break;
				case DCI_AWAY:
					StatusString = "Away";
					bG = (BitmapDrawable)getApplicationContext().getResources().getDrawable(R.drawable.yellow);
					image.setImageDrawable(bG);
					break;
				case DCI_LOST:
					StatusString = "LOST!!";
					bG = (BitmapDrawable)getApplicationContext().getResources().getDrawable(R.drawable.red);
					image.setImageDrawable(bG);
					break;
					default:
						StatusString = "Away";
						bG = (BitmapDrawable)getApplicationContext().getResources().getDrawable(R.drawable.yellow);
						image.setImageDrawable(bG);
						break;						
				}
				Log.d("CheckInLogs", "StatusString -"+StatusString);
				Status.setText(StatusString);
				Log.d("CheckInLogs", "Status.getText() - "+Status.getText());
				
				Log.d("CheckInLogs", "log.get(n).location-"+log.get(n).location);
				location.setText(log.get(n).location);
				Log.d("CheckInLogs", "location.getText() - "+location.getText());
			}
		}
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
			TaskStackBuilder taskBuilder = TaskStackBuilder.create(this);
			taskBuilder.addParentStack(DCISettingsActivity.class);
			taskBuilder.addNextIntent(intent).startActivities();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
