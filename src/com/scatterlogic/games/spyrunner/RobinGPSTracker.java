package com.scatterlogic.games.spyrunner;
//GPSTracker:  A class containing functions relating to the tracking of a device.
//Don't forget to add:
//<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
//To the manifest file or:
//<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class RobinGPSTracker implements iExerciseTracker
{
	//objects relating to acquiring fixes and updates
	LocationManager myLocationManager;
	LocationListener myLocationListener;
	
	//storage of latest location and log of locations
	Location myLocation;
	ArrayList <Location> locationsList = new ArrayList <Location>();
	 
	//this constant is used to change the provider for testing in low GPS areas.
	//final String provider = LocationManager.NETWORK_PROVIDER;
	final String provider = LocationManager.GPS_PROVIDER;
	
	//used to identify if tracking is currently happening
	boolean running;
	long startTime;
	
	long frequencyMilliseconds;
	float distance;
	
	Date systemTime = new Date();
	
	public RobinGPSTracker (Context context){
		startTime = System.currentTimeMillis();
		frequencyMilliseconds = 0;
		distance = 10;
		myLocationListener = new LocationListener(){
			@Override
			public void onLocationChanged(Location location) {
				Log.e("Location Listener", "In location changed: " +location.getLongitude() + ", " +
						location.getLatitude() + ", " + location.getAltitude());
				if (running){
					if (myLocation == null){
						myLocation = location;
					}
					else{
						distance += myLocation.distanceTo(location);
						myLocation = location;
						}
				}
				locationsList.add(location);
			}

			@Override
			public void onProviderDisabled(String provider) {
				//Log.e("Location Listener", "In provider disabled");
			}

			@Override
			public void onProviderEnabled(String provider) {
				//Log.e("Location Listener", "In provider enabled");
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				//Log.e("Location Listener", "In status changed");
			}
        };
    
        myLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        startTracker();
	}
	public void startTracker(){
		if (!running){
			myLocationManager.requestLocationUpdates(provider, this.frequencyMilliseconds, this.distance, myLocationListener);
			running = true;
		}
	}
	public void pauseTracker(){
		myLocation = null;
		running = false;
	}
	public void stopTracker(){
		if(running){
			myLocationManager.removeUpdates(myLocationListener);
			running = false;
		}
	}
	public Location getCurrentLocation(){
		return myLocation;
	}
	public synchronized boolean isRunning(){
		return this.running;
	}
	
	//not used in latest implementation
	public String coordinatesAsString(){
		if (!(myLocation==null))
		return "[" + myLocation.getLatitude() + ", " + 
				myLocation.getLongitude() + ", " +
				myLocation.getAltitude() + "], " +
				this.getTimeString() 	 + " (" +
				myLocation.getAccuracy() + ")";
		else return "Acquiring... " + ((System.currentTimeMillis()-startTime)/1000);
	}

	@Override
	public String getLocationAsString()
	{
		return coordinatesAsString();
	}
	
	public String coordinatesAsString(Location inLocation){
		if (!(inLocation==null))
			return inLocation.getLatitude() + ", " + 
					inLocation.getLongitude() + ", " +
					inLocation.getAltitude() + ", " +
					inLocation.getTime() 	 + " (" +
					inLocation.getAccuracy() + ")";
			else return "Acquiring... " + ((System.currentTimeMillis()-startTime)/1000);
	}

	@Override
	public long getTimeMillis() {
		if(!(myLocation==null))
			return myLocation.getTime();
		else
			return 0;
	}
	@Override
	public String getTimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		return formatter.format(new Date(getTimeMillis()));
	}
	@Override
	public float getSpeed() {
		if (!(myLocation == null))
			return myLocation.getSpeed();
		else return 0;
	}
	@Override
	public float getPace() {
		return 60/getSpeed();
	}
	@Override
	public synchronized float getDistance() {
		return distance/1000;
	}
	@Override
	public double getAltitudeGain() {
		// TODO Auto-generated method stub
		if (locationsList.size() > 3){
			return (locationsList.get(locationsList.size()-1).getAltitude() - 
					locationsList.get(locationsList.size()-2).getAltitude());
		}
		return 0;
	}
}
