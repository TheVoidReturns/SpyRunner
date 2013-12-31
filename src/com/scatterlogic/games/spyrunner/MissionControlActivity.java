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
import android.widget.TextView;

public class MissionControlActivity extends Activity {
	
	// This/These intent(s) will be used to transist to new activities from here...
	Intent missionReview;
	
	// We'll have four TextViews for the stats, and one for the feedback
	TextView stat1, stat2, stat3, stat4, feedback;
	
	//Let's get that exercise tracker to hand
	iExerciseTracker mET;

	//and something for the thread to check that the mission is continuing
	Handler mHandler;
	private boolean running;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mET = new RobinGPSTracker(this);
		setContentView(R.layout.activity_mission_control);

		mHandler = new Handler();
		stat1 = (TextView) findViewById(R.id.missioncontrolstat1);
		stat2 = (TextView) findViewById(R.id.missioncontrolstat2);
		stat3 = (TextView) findViewById(R.id.missioncontrolstat3);
		stat4 = (TextView) findViewById(R.id.missioncontrolstat4);
		feedback = (TextView) findViewById(R.id.missioncontrolstat4);
		
		stat1.setText("Yo");
		stat2.setText("Yo too");
		stat3.setText("Yo three");
		stat4.setText("Yo four");
		
		missionReview = new Intent(this, ReviewMissionActivity.class);
		findViewById(R.id.mission_review).setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	startActivity(missionReview);
		    } 
		});
		feedback.setText("Launching thread");
		running = true;
		mUpdateResults.run();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mET.startTracker();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mET.stopTracker();
	}
	
	final Runnable mUpdateResults = new Runnable(){

		public void run(){
			if (running){
				updateResults();
				mHandler.postDelayed(mUpdateResults, 1000);
			}
    	}
    };
	private void updateResults(){
		stat1.setText(mET.getSpeed() + "");
		stat2.setText(mET.getTimeString());
		stat3.setText("N/A");
		stat4.setText("N/A");
		if (mET.getCurrentLocation()==null)
			feedback.setText("No sats");
		else
			feedback.setText(mET.getCurrentLocation().getLatitude() + "");
	}
}
