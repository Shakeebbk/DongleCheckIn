package com.example.donglecheckin;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.donglecheckin.DCIConfig.CheckStatus;

public class DCIAlarm extends Activity {
	//Prefs
	public static final String MYPREFS = "DCISettings";
	Boolean OOFEnabled;
	Calendar endcalendar=null;
	Calendar startcalendar=null;
	
	DBProvider DB=null;
	
	LocationListener locationListener = new LocationListener() {
		 
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }
	 
	    @Override
	    public void onProviderEnabled(String provider) {

	    }
	 
	    @Override
	    public void onProviderDisabled(String provider) {

	    }
	 
	    @Override
	    public void onLocationChanged(Location location) {
	        // Do work with new location. Implementation of this method will be covered later.
	        doWorkWithNewLocation(location);
	    }
	};
	 
	long minTime = 5 * 1000; // Minimum time interval for update in seconds, i.e. 5 seconds.
	long minDistance = 10; // Minimum distance change for update in meters, i.e. 10 meters.
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			 if((currentDate.getTimeInMillis() > startcalendar.getTimeInMillis()) && (currentDate.getTimeInMillis() < endcalendar.getTimeInMillis())) {
				 //OOF
				 Log.d("DCIAlarm", "OOF - Ending alarm");
				 finish();
			 }
		}
		
		setContentView(R.layout.activity_dcialarm);
		
		LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		 
		// Returns last known location, this is the fastest way to get a location fix.
		Location fastLocation = locationManager.getLastKnownLocation(getProviderName());
		if(fastLocation != null) {
			doWorkWithNewLocation(fastLocation);
		}
		
		
 		// Assign LocationListener to LocationManager in order to receive location updates.
		// Acquiring provider that is used for location updates will also be covered later.
		// Instead of LocationListener, PendingIntent can be assigned, also instead of 
		// provider name, criteria can be used, but we won't use those approaches now.
		locationManager.requestLocationUpdates(getProviderName(), minTime,
		        minDistance, locationListener);
		
		//Handler
		Handler handler = new Handler();
		handler.postDelayed(timeout, 30*1000);
		
		Button checkIn = (Button)findViewById(R.id.CheckInButton);
		checkIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DB.DBAdd(CheckStatus.DCI_LOGGED_IN);
			}
		});
		Button Away = (Button)findViewById(R.id.AwayButton);
		Away.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DB.DBAdd(CheckStatus.DCI_AWAY);
			}
		});
		Button Lost = (Button)findViewById(R.id.LostButton);
		Lost.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DB.DBAdd(CheckStatus.DCI_LOST);
			}
		});
		
	}
	
	private Runnable timeout = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d("DCIAlarm", "Timeout!");
			//Handle timeout	
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
	
	/**
	 * Get provider name.
	 * @return Name of best suiting provider.
	 * */
	String getProviderName() {
	    LocationManager locationManager = (LocationManager) this
	            .getSystemService(Context.LOCATION_SERVICE);
	 
	    Criteria criteria = new Criteria();
	    criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired power consumption level.
	    criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
	    criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
	    criteria.setAltitudeRequired(false); // Choose if you use altitude.
	    criteria.setBearingRequired(false); // Choose if you use bearing.
	    criteria.setCostAllowed(false); // Choose if this provider can waste money :-)
	 
	    // Provide your criteria and flag enabledOnly that tells
	    // LocationManager only to return active providers.
	    return locationManager.getBestProvider(criteria, true);
	}
	/**
	* Make use of location after deciding if it is better than previous one.
	*
	* @param location Newly acquired location.
	*/
	void doWorkWithNewLocation(Location location) {
		Log.d("DCIAlarm", "doWorkWithNewLocation");
	    if(isBetterLocation(getOldLocation(), location)) {
	    }
	 
	    setOldLocation(location);
	}
	 
	private void setOldLocation(Location location) {
		// TODO Auto-generated method stub
		final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
		
		int locationCount = settings.getInt("LocationAccuracyCount", 0);
		if(locationCount == 5) {
			LocationManager locationManager = (LocationManager) this
	                .getSystemService(Context.LOCATION_SERVICE);
	 
			// Stop listening to location updates, also stops providers.
			locationManager.removeUpdates(locationListener);
			
			DB.DBUpdateLocation(location);
		}
		locationCount++;
		Editor editor = settings.edit();
		editor.putFloat("Latitude", (float)location.getLatitude());
		editor.putFloat("Longitude", (float) location.getLongitude());
		editor.putLong("Time", location.getTime());
		editor.putFloat("Accuracy", location.getAccuracy());
		editor.putInt("LocationAccuracyCount", locationCount);
		editor.commit();
	}

	private Location getOldLocation() {
		// TODO Auto-generated method stub
		final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
		Location location = new Location(getProviderName());	
		
		location.setLatitude(settings.getFloat("Latitude", 0));
		location.setLatitude(settings.getFloat("Longitude", 0));
		location.setTime(settings.getLong("Time", 0));
		location.setAccuracy(settings.getFloat("Accuracy", 0));
		return location;
	}

	/**
	* Time difference threshold set for one minute.
	*/
	static final int TIME_DIFFERENCE_THRESHOLD = 1 * 60 * 1000;
	 
	/**
	* Decide if new location is better than older by following some basic criteria.
	* This algorithm can be as simple or complicated as your needs dictate it.
	* Try experimenting and get your best location strategy algorithm.
	* 
	* @param oldLocation Old location used for comparison.
	* @param newLocation Newly acquired location compared to old one.
	* @return If new location is more accurate and suits your criteria more than the old one.
	*/
	boolean isBetterLocation(Location oldLocation, Location newLocation) {
	    // If there is no old location, of course the new location is better.
	    if(oldLocation == null) {
	        return true;
	    }
	 
	    // Check if new location is newer in time.
	    boolean isNewer = newLocation.getTime() > oldLocation.getTime();
	 
	    // Check if new location more accurate. Accuracy is radius in meters, so less is better.
	    boolean isMoreAccurate = newLocation.getAccuracy() < oldLocation.getAccuracy();       
	    if(isMoreAccurate && isNewer) {         
	        // More accurate and newer is always better.         
	        return true;     
	    } else if(isMoreAccurate && !isNewer) {         
	        // More accurate but not newer can lead to bad fix because of user movement.         
	        // Let us set a threshold for the maximum tolerance of time difference.         
	        long timeDifference = newLocation.getTime() - oldLocation.getTime(); 
	 
	        // If time difference is not greater then allowed threshold we accept it.         
	        if(timeDifference > -TIME_DIFFERENCE_THRESHOLD) {
	            return true;
	        }
	    }
	 
	    return false;
	}
}
