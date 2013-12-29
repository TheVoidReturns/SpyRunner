package com.scatterlogic.games.spyrunner;

//This class operates a timer
//Created by Robin Peel on 15/05/2013
public class RobinTimerObject {
	private String sTimerValue;
	private long startTime;
	private long elapsedTime;
	private boolean running;
	public RobinTimerObject(){
		sTimerValue = "Error";
		startTime = 0;
		elapsedTime = 0;
		running = false;
	}
	public void startTimer(){
		if(!running){
			startTime = System.currentTimeMillis()-elapsedTime;  //this allows the timer to pick up where it left off if paused
			running = true;
		}
	}
	public void stopTimer(){
		if (running){
			elapsedTime = System.currentTimeMillis() - startTime;
			running = false;
		}
	}
	public void resetTimer(){
		elapsedTime = 0;
	}
	public long getElapsedTimeAsLong(){
		if (running)return System.currentTimeMillis() - startTime;
		else return elapsedTime;
	}
	public synchronized boolean isRunning(){
		return running;
	}
	public String getElapsedTimeAsString(){
		String result = "";
		long et = this.getElapsedTimeAsLong();
		long hours = (((et/1000) 	//total seconds
							/60)	//total minutes
							/60);	//total hours
		et -= hours*60*60*1000;		//take the hours from the total
		
		long minutes = 	((et/1000) 	//total seconds
						   	/60);	//total minutes
		et -= minutes*60*1000;		//take the minutes from the total
		
		long seconds = et/1000;
		
		if (hours<10) result = result + "0";
		result = result + hours + ":";
		if (minutes<10) result = result + "0";
		result = result + minutes + ":";
		if (seconds<10) result = result + "0";
		result = result + seconds;
		
		return result;
	}
}
