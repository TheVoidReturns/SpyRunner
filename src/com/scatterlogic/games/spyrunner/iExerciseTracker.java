package com.scatterlogic.games.spyrunner;

import android.location.Location;


//A group of activities to track progress within an exercise regime
//currently split between cadence sensing and gpd tracking
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
	
	//Entries relating to distance travelled.  Metric is default.
	public float getDistance();
	
	//location finding method.  Returns a null location if gps is not enabled?
	public Location getCurrentLocation();
	
	//methods which will be called from the main flow.
	public void startTracker();
	public void pauseTracker();
	public void stopTracker();
}
