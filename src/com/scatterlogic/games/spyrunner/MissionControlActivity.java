package com.scatterlogic.games.spyrunner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import java.io.*;
import android.util.*;
import android.os.PowerManager;
import android.content.pm.*;


public class MissionControlActivity extends Activity {
	
	// This/These intent(s) will be used to transist to new activities from here...
	Intent missionReview;
	Intent musicServiceIntent;
	
	// We'll have four TextViews for the stats, and one for the feedback
	TextView stat1, stat2, stat3, stat4, feedback;
	
	//Test Song Read
	TextView songTest;
	
	//Let's get that exercise tracker to hand
	iExerciseTracker myExerciseTracker;
	float oldDistance; //A variable to see if the exercise tracker has updated...

	//and some thread variables to make the continuous loop clearer
	long elapsedTime;
	int targetHRZone;
	int currentHRZone;
	int periodsOfFail;
	
	//and something for the thread to check that the mission is continuing
	Handler mHandler;
	private boolean running;
	
	//A file to write to...
	RobinFileWriter thisMission;
	
	//a timer object to keep an eye on the clock
	RobinTimerObject timer;

	//Keep the screen alive
	PowerManager.WakeLock wl;
	
	//Music player object
	MusicPlayer musicPlayer;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Set Up Variables
		mHandler = new Handler();
		timer = new RobinTimerObject();
		myExerciseTracker = new RobinGPSTracker(this);
        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
		oldDistance = 0;
		
		//Set The Screen, lock into portrait
		setContentView(R.layout.activity_mission_control);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        //Create music player object     	
        musicPlayer = new MusicPlayer(getApplicationContext());
        
        //Prepare the music player in a separate thread
        musicPlayer.prepareAsync();
        
        //This is used for music testing
        songTest = (TextView) findViewById(R.id.song_test);
        
		stat1 = (TextView) findViewById(R.id.missioncontrolstat1);
		stat2 = (TextView) findViewById(R.id.missioncontrolstat2);
		stat3 = (TextView) findViewById(R.id.missioncontrolstat3);
		stat4 = (TextView) findViewById(R.id.missioncontrolstat4);
		feedback = (TextView) findViewById(R.id.missioncontrolfeedback);
		
		//This is used for music testing
		try {
			
			//songTest.setText(musicPlayer.songTitles.get(201));
		}
		finally {
			songTest.setText("Song Load Fail");
		}
		
		stat1.setText("Awaits");
		stat2.setText("Awaits2");
		stat3.setText("Awaits3");
		stat4.setText("Awaits4");
		

		//Launch the file
		thisMission = new RobinFileWriter("Mission Logs", "TestMission.txt");
		
		//*********************This bit is all about launching the Review Mission Screen, It probably won't be a button
		missionReview = new Intent(this, ReviewMissionActivity.class);
		findViewById(R.id.mission_review).setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	startActivity(missionReview);
		    } 
		});
		//**************************************************************************************************************
		
		//feedback.setText("Launching thread");
		
		running = true;
		mUpdateResults.run();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		myExerciseTracker.startTracker();
		timer.startTimer();
		wl.acquire();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		wl.release();
		thisMission.Append(myExerciseTracker.getDistance() + "km finished in " +
				timer.getElapsedTimeAsString());
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		myExerciseTracker.stopTracker();
		timer.stopTimer();
	}
	
	final Runnable mUpdateResults = new Runnable(){

		public void run(){
			if (running){
				
				updateScreenFields();
				mHandler.postDelayed(mUpdateResults, 500);
			}
    	}
    };
	private void updateScreenFields(){
		stat1.setText(myExerciseTracker.getSpeed() + "km/h");
		stat2.setText(myExerciseTracker.getDistance()+"km");
		stat3.setText(myExerciseTracker.getPace()+"mins/km");
		stat4.setText(timer.getElapsedTimeAsString());
			
		if (!(oldDistance == myExerciseTracker.getDistance())){
			if (myExerciseTracker.getCurrentLocation()==null){
				//feedback.setText("No sats");
			}
			else{
				feedback.setText(myExerciseTracker.getLocationAsString() + "\n");
			}
		}
	}
}
