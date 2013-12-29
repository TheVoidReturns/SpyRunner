package com.scatterlogic.games.spyrunner;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.util.Log;

//A class for the reading, writing and logging of routes

public class RobinRouteLog {
	List <Location> PlacesVisited;
	Context context;
	RobinFileWriter file;
	Boolean imperialMeasurements;
	final static double MILES_MULTIPLIER = 0.621371192;
	
	public RobinRouteLog(Context context, String routeLogName, boolean imperialMeasurements){
		this.imperialMeasurements = imperialMeasurements;
		PlacesVisited = new ArrayList<Location>();
		this.context = context;
		file = new RobinFileWriter(context.getString(R.string.app_name), routeLogName);
	}
	public void add(Location location){
		PlacesVisited.add(location);
	}
	public void addIfBasicallyDifferent(Location location){
		if (getSize()==0) add(location);
		else if (!(coordinatesAsString(location)
					.equals(coordinatesAsString(getLastLocation())))) add(location);
		else Log.i("RouteLog", "Did not add location");
	}
	public Location getLastLocation(){
		if (PlacesVisited.size() > 0) return PlacesVisited.get(PlacesVisited.size()-1);
		else return null;
	}
	public Location getNthLocation(int n){
		return PlacesVisited.get(n);
	}
	
	public float getDistanceBetweenLogs(int logA, int logB){
		if (getSize()<2) return 0;
		if (logA >= PlacesVisited.size()) logA = PlacesVisited.size()-1;
		if (logB >= PlacesVisited.size()) logB = PlacesVisited.size()-1;
		return getDistanceBetweenPoints(getNthLocation(logA), getNthLocation(logB));		
	}
	public float getDistanceBetweenPoints(Location locationA, Location locationB){
		return locationB.distanceTo(locationA);
	}
	public long getTimeBetweenLogs(int logA, int logB){
		if (getSize()<2) return 0;
		if (logA >= PlacesVisited.size()) logA = PlacesVisited.size()-1;
		if (logB >= PlacesVisited.size()) logB = PlacesVisited.size()-1;
		return getTimeBetweenPoints(getNthLocation(logA), getNthLocation(logB));		
	}
	public long getTimeBetweenPoints(Location locationA, Location locationB){
		return locationB.getTime() - locationA.getTime();
	}
	public float getSpeedBetweenPoints(Location locationA, Location locationB){
		float distanceTravelled = getDistanceBetweenPoints(locationA, locationB);
		long timeTaken = getTimeBetweenPoints(locationA, locationB);
		Log.i("RouteLog", "Got Distance " + distanceTravelled + " and Time " + timeTaken +
				"making speed " + (distanceTravelled/timeTaken));
		return (distanceTravelled/timeTaken);
	}
	
	public float getTotalDistance(){
		float runningTotal =0;
		if (getSize() < 2) return 0;
		for(int i = 1; i < (getSize()-1); i++){
			runningTotal += getDistanceBetweenLogs(i,i+1);
		}
		return runningTotal;
	}
	
	public long getTotalTime(){
		if (getSize() < 2) return 0;
		else return getLastLocation().getTime();
	}
	public float getCurrentSpeed(){
		//Location get speed is in metres per second, as opposed to the standard metres per millisecond.
		if (getSize()>2) return getLastLocation().getSpeed()/1000;
		return 0;
	}
	public float getAverageSpeed(){
		long tempTime = getTotalTime();
		tempTime = tempTime - getNthLocation(1).getTime();
		return getTotalDistance()/tempTime;
	} 
	
	public String convertSpeedToString(float speed){
		//the speed given is metres / milliseconds or kilometres per second
		speed = speed * 60;	//kilometres per minute
		speed = speed * 60; //kilometres per hour
		
		if (imperialMeasurements) return round((speed * MILES_MULTIPLIER),1) + " mph";
		
		return round(speed,1) + " kmph";
	}
	public String convertDistanceToString(float distance){
		distance = distance / 1000;
		distance = round(distance,2);
		if (imperialMeasurements) return round((distance * MILES_MULTIPLIER),1) + "miles";
		else return round(distance,1) + " km";
	}
	
	public int getSize(){
		return PlacesVisited.size();
	}
	public void loadLog(){
		//TODO:  code stub
	}
	public String lastTenAsString(){
		String outString = "";
		int numberOfLoops = 10;
		if (getSize()<10) numberOfLoops = getSize();
		numberOfLoops--; //to count through Array correctly
		for (int i = 0; i <= numberOfLoops; i++){
			outString = outString + coordinatesAsString(getNthLocation(getSize()-(i+1))) + "\n";
		}
		if (getSize() > 1) outString = outString + "\n\n" + getLastLocation().getSpeed() + " kph" + "\n\n";
		outString = outString + "\n\n" + file.tellMeAboutYourself();
		return outString;
	}
	public String coordinatesAsString(Location inLocation){
		if (!(inLocation==null))
		return inLocation.getLatitude() + ", " +
				inLocation.getLongitude() + ", " +
				inLocation.getAltitude() + ", " +
				inLocation.getTime() 	 + " (" +
				inLocation.getAccuracy() + ")";
		else return "No fixes";
	}
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    Log.i("RouteLog", "dRounding " + value + " to " + ((float) tmp / factor));
	    return (double) tmp / factor;
	}
	public static float round(float value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    Log.i("RouteLog", "fRounding " + value + " to " + ((float) tmp / factor));
	    return (float) tmp / factor;
	}
	public void saveLog(){
		String totalLog = "";
		for (int i = 1; i < this.getSize(); i++){
			totalLog = totalLog + coordinatesAsString(getNthLocation(i)) + "\n";
		}
		file.Append(totalLog);
	}
}
