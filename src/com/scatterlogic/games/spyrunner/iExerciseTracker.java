package com.scatterlogic.games.spyrunner;

import java.util.ArrayList;

import android.location.Location;


//A group of activities to track progress within an exercise regime
//currently split between cadence sensing and gps tracking
public interface iExerciseTracker {
	
	//a boolean which declares if this particular method is viable
	public boolean isViable = false;
	
	//a boolean which is set by the program to "true" once it is ready for the exercise to begin
	public boolean readyToGo = false;
	
	//Entries relating to time
	public long getTimeMillis();
	public String getTimeString();
	
	//Entries relating to Speed.  Metric is default.
	public float getSpeed();
	public float getPace();
	public double getAltitudeGain();
	
	//Entries relating to distance travelled.  Metric is default.
	public float getDistance();
	
	//location finding method.  Returns a null location if gps is not enabled?
	public Location getCurrentLocation();
	public String getLocationAsString();
	
	//methods which will be called from the main flow.
	public void startTracker();
	public void pauseTracker();
	public void stopTracker();
}
