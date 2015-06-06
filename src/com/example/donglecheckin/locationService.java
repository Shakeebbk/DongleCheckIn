package com.example.donglecheckin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class locationService extends Service{
	public static final String MYPREFS = "DCISettings";
	
	  private Looper mServiceLooper;
	  private ServiceHandler mServiceHandler;
	
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
	    	Log.d("DCIAlarms", "onLocationChanged");
	        doWorkWithNewLocation(location);
	    }
	};
	 
	long minTime = 0;//5 * 1000; // Minimum time interval for update in seconds, i.e. 5 seconds.
	long minDistance = 0;//10 // Minimum distance change for update in meters, i.e. 10 meters.

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
		DB.DBUpdateLocation(location, isBetterLocation(getOldLocation(), location));	 
	    setOldLocation(location);
	}
	 
	private void setOldLocation(Location location) {
		// TODO Auto-generated method stub
		final SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
		
		int locationCount = settings.getInt("LocationAccuracyCount", 0);
		Log.d("DCIAlarm", "locationCount="+locationCount);
		if(locationCount == DCIConfig.LOCATION_CHANGE_COUNT) {
			locationCount=0;
			Log.d("DCIAlarm", "Stopping location updates");
			LocationManager locationManager = (LocationManager) this
	                .getSystemService(Context.LOCATION_SERVICE);
	 
			// Stop listening to location updates, also stops providers.
			locationManager.removeUpdates(locationListener);
			
			stopSelf();
		}
		else {
			locationCount++;
		}
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
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("locationService", "onDestroy");
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	  // Handler that receives messages from the thread
	  private final class ServiceHandler extends Handler {
	      public ServiceHandler(Looper looper) {
	          super(looper);
	      }
	      @Override
	      public void handleMessage(Message msg) {
	          // Normally we would do some work here, like download a file.
	          // For our sample, we just sleep for 5 seconds.
	    	  Log.d("locationService", "handleMessage");
	          long endTime = System.currentTimeMillis() + 5*1000;
	          while (System.currentTimeMillis() < endTime) {
	              synchronized (this) {
	                  try {
	                      wait(endTime - System.currentTimeMillis());
	                  } catch (Exception e) {
	                  }
	              }
	          }
	          // Stop the service using the startId, so that we don't stop
	          // the service in the middle of handling another job
	          stopSelf(msg.arg1);
	      }
	  }
	  
	  @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// Start up the thread running the service.  Note that we create a
	    // separate thread because the service normally runs in the process's
	    // main thread, which we don't want to block.  We also make it
	    // background priority so CPU-intensive work will not disrupt our UI.
	    HandlerThread thread = new HandlerThread("ServiceStartArguments",
	    		10);
	    thread.start();
		// Get the HandlerThread's Looper and use it for our Handler
	    mServiceLooper = thread.getLooper();
	    mServiceHandler = new ServiceHandler(mServiceLooper);
	}
	  @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		  Log.d("locationService", "onStartCommand");

		  DB = new DBProvider(this, "DCI_DB");
		// For each start request, send a message to start a job and deliver the
	      // start ID so we know which request we're stopping when we finish the job
	      Message msg = mServiceHandler.obtainMessage();
	      msg.arg1 = startId;
	      //mServiceHandler.sendMessage(msg);
	      
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
			Log.d("DCIAlarm", "getProviderName - "+getProviderName());
			locationManager.requestLocationUpdates(getProviderName(), minTime,
			        minDistance, locationListener);
	      // If we get killed, after returning from here, restart
	      return START_STICKY;
	}
}