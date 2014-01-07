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
import android.widget.ImageButton;
import java.io.*;
import java.util.Locale;

import android.util.*;
import android.os.PowerManager;
import android.content.pm.*;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.OnInitListener;
import java.util.*;

public class MissionControlActivity extends Activity {

	// This/These intent(s) will be used to transist to new activities from here...
	Intent missionReview;
	Intent musicServiceIntent;
	
	// We'll have four TextViews for the stats, and one for the feedback
	TextView stat1, stat2, stat3, stat4, feedback;
	ImageButton musicPlay, musicPause, musicPrevious, musicNext;
	
	//Test Song Read
	TextView songTest;
	
	//Let's get that exercise tracker to hand
	iExerciseTracker myExerciseTracker;
	iHeartRateZoneFinder myHeartRateZoneFinder;
	float oldDistance; //A variable to see if the exercise tracker has updated...

	//and some thread variables to make the continuous loop clearer
	long elapsedTime;
	float currentSpeed;
	double altitudeGain;
	int targetHRZone;
	int currentHRZone;
	int mileStonesPassed;
	int periodsOfFail;
	String songTitle;
	
	//and something for the thread to check that the mission is continuing
	Handler mHandler;
	private boolean running;
	
	//A file to write to...
	RobinFileWriter thisMission;
	
	
	
	//a timer object to keep an eye on the clock
	RobinTimerObject timer;
	
	//A speech thing
	TextToSpeech voiceFeedback;
	
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
		mileStonesPassed = 0;
		
		//The lines below will eventually be calculated, and will need to have either a file or
		//list of values passed...  The list of values is km/h, but may eventually contain altitude factors...
		myHeartRateZoneFinder = new HeartRateFromSpeed(11,10,9,8,7);
		
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
		
		musicPlay = (ImageButton) findViewById(R.id.music_play);
		musicPause = (ImageButton) findViewById(R.id.music_pause);
		musicPrevious = (ImageButton) findViewById(R.id.music_previous);
		musicNext = (ImageButton) findViewById(R.id.music_next);
		
		
		//Set click listeners
		musicPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicPlayer.play();
                
            }
        });
		musicPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicPlayer.pause();
            }
        });
		musicPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	musicPlayer.previous();
            }
        });
		musicNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicPlayer.next();
            }
        });
		
		//This is used for music testing
		//while (musicPlayer.isPrepared == false) {
			try {
				//songTest.setText(musicPlayer.getCurrentSongTitle());
			}
			finally {
				songTest.setText("Song title not read");
			}
		//}
		
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
	
	//Here is the looping thread, set to re-launch itself every half second at the moment...
	
	final Runnable mUpdateResults = new Runnable(){

		//this "run" section is the main loop, and will call each of the facets of the exercise
		//tracker
		public void run(){
			if (running){
				//update the timer
				elapsedTime = timer.getElapsedTimeAsLong();
								
				//obtain the current speed and altitude gain
				currentSpeed = myExerciseTracker.getSpeed();
				altitudeGain = myExerciseTracker.getAltitudeGain();
				
				//which then translates as a heart zone (in the absence of an HR monitor)
				currentHRZone = myHeartRateZoneFinder.getHeartRateZone(currentSpeed, altitudeGain);
				
				//Current song title
				songTitle = musicPlayer.getCurrentSongTitle();
				
				//A method to be written...
				missionReact();
				
				//And update the text fields
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
		
		//Read song title
		songTest.setText(songTitle);
			
		if (!(oldDistance == myExerciseTracker.getDistance())){
			if (myExerciseTracker.getCurrentLocation()==null){
				//feedback.setText("No sats");
			}
			else{
				//feedback.setText(myExerciseTracker.getLocationAsString() + "\n");
			}
		}
	}
	//this is the method which decides if any audio feedback or log information is needed
	private void missionReact(){
	}

	public void loadMission(){
		
	}
}
