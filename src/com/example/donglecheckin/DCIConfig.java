package com.example.donglecheckin;

public interface DCIConfig {
	 public enum CheckStatus{
	    	DCI_LOGGED_IN, DCI_AWAY, DCI_LOST
	 }
	 
	 public int LOCATION_CHANGE_COUNT = 1;
	 public int REPEAT_ALARM_TIME_MS = 24 * 60 * 60 * 1000;
	 public int ALARM_SCREEN_TIMEOUT_MS = 60 * 30 * 1000;
	 
	 public int CARD_CHECKIN_DISTANCE = 990;
	 public int CARD_ACTION_DISTANCE = 10;
	 
	 public int CARD_MOVE_ANIM_TIME = 500;
}
