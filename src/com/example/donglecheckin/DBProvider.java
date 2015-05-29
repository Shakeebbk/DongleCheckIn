package com.example.donglecheckin;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.donglecheckin.DCIConfig.CheckStatus;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class DBProvider{
	public static SQLiteDatabase messagesDB;
	private Activity mainActivity;
	
	private int _idKey = 0;
	private int stateKey = 0;
	private int locationKey = 0;
	private int TSKey = 0;
	private int precisionKey = 0;
	
	public DBProvider(Activity MainActivity, String DBName) {
		super();
		Log.d("DBProvider","Constructor:DBName "+DBName);
		this.mainActivity = MainActivity;
		messagesDB = mainActivity.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null);
		//set 10 MB max size
		messagesDB.setMaximumSize(1000 * 1000);
		Log.d("DBProvider","DB OPeration: "+"CREATE TABLE IF NOT EXISTS DCILogs(_id INTEGER PRIMARY KEY, State VARCHAR,Location VARCHAR,TS VARCHAR, Precision INT);");
		messagesDB.execSQL("CREATE TABLE IF NOT EXISTS DCILogs(_id INTEGER PRIMARY KEY, State VARCHAR,Location VARCHAR,TS VARCHAR, Precision INT);");
	}

	public void DBAdd(DCIConfig.CheckStatus status) {
		String StatusString;
		String locationString;
		Location location;
		LocationManager locationManager;
		String timestamp;
		
		Log.d("DBProvider", "DBAdd - status:"+status);
		switch(status) {
		case DCI_LOGGED_IN:
			StatusString = "DCI_LOGGED_IN";
			break;
		case DCI_AWAY:
			StatusString = "DCI_AWAY";
			break;
		case DCI_LOST:
			StatusString = "DCI_LOST";
			break;
			default:
				StatusString = "DCI_AWAY";
				break;
		}
		Log.d("DBProvider", "DBAdd - StatusString:"+StatusString);
		
		//check if we can add it
		final Cursor c = messagesDB.rawQuery("SELECT * FROM DCILogs ORDER BY _id DESC", null);
		if((c.getCount() == 5) && (status != CheckStatus.DCI_LOGGED_IN)) {
			c.moveToLast();
			if(c.getString(stateKey).equals(StatusString)) {
				DBDelete(c.getInt(_idKey));
			}
			else {
				c.moveToPrevious();
				if (c.getString(stateKey).equals(StatusString)) {
					c.moveToLast();
					DBDelete(c.getInt(_idKey));
				}
				else {
					//No room!
					Log.d("DBProvider", "DBAdd - NO ROOM!");
					return;
				}
			}
		}		
		
		locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
		location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		try {
			locationString = "Latitude:"+location.getLatitude()+";Longitude:"+location.getLongitude();
			Log.d("DBProvider", "DBAdd - locationString:"+locationString);
		}
		catch(NullPointerException e) {
			locationString = "Latitude:"+"00"+";Longitude:"+"00";
			Log.d("DBProvider", "DBAdd - Null locationString!");
		}

		timestamp = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
		
		//update the DB
		try {
			Log.d("DBProvider", "Inserting the msg now");
			messagesDB.beginTransactionNonExclusive();
			Log.d("DBProvider", "INSERT INTO DCILogs(State, Location, TS) VALUES('"+StatusString+"','"+
					locationString+"','"+timestamp+"',0"+"');");
			messagesDB.execSQL("INSERT INTO DCILogs(State, Location, TS) VALUES('"+StatusString+"','"+
					locationString+"','"+timestamp+"',0"+"');");
			messagesDB.setTransactionSuccessful();
			messagesDB.endTransaction();
		} catch (SQLException e) {
			Log.d("DBProvider", "JSON DB insertion ERROR 1");
			e.printStackTrace();
		}
		finally {
			//nothing
		}
	}
	
	public ArrayList<DCILogClass> DBGet() {
		// Retrieving all records
		Log.d("DBProvider","DB OPeration: "+"SELECT * FROM DCILogs ORDER BY _id DESC");
		final Cursor c = messagesDB.rawQuery("SELECT * FROM DCILogs ORDER BY _id DESC", null);
		ArrayList<DCILogClass> classesArray = new ArrayList<DCILogClass>();
		DCIConfig.CheckStatus status;
		
		while(c.moveToNext()) {
			switch(c.getString(stateKey)) {
			case "DCI_LOGGED_IN":
				status = DCIConfig.CheckStatus.DCI_LOGGED_IN;
				break;
			case "DCI_AWAY":
				status = DCIConfig.CheckStatus.DCI_AWAY;
				break;
			case "DCI_LOST":
				status = DCIConfig.CheckStatus.DCI_LOST;
				break;
				default:
					status = DCIConfig.CheckStatus.DCI_AWAY;
					break;
			}
			classesArray.add(new DCILogClass(status, c.getString(TSKey), c.getString(locationKey)));
		}
		
		return classesArray;
	}
	
	public void DBDelete(int id) {
		//delete the entry and notify adapter change
		Log.d("DBProvider","DB OPeration: "+"DELETE FROM DCILogs WHERE _id="+(id));
		messagesDB.execSQL("DELETE FROM DCILogs WHERE _id="+(id));
	}
	
	public void DBUpdateLocation(Location location) {
		Log.d("DBProvider","DBUpdateLocation - "+location.getLatitude()+" : "+location.getLatitude());
		Log.d("DBProvider","DB OPeration: "+"SELECT * FROM DCILogs ORDER BY _id DESC");
		final Cursor c = messagesDB.rawQuery("SELECT * FROM DCILogs ORDER BY _id DESC", null);
		
		while(c.moveToNext()) {
			if(c.getInt(precisionKey) == 0) {
				Log.d("DBProvider", "UPDATE messages SET Precision='1' WHERE _id="+(c.getInt(_idKey)));
            	messagesDB.execSQL("UPDATE messages SET Precision='1' WHERE _id="+(c.getInt(_idKey)));
			}
			else {
				return;
			}
		}
	}
}
