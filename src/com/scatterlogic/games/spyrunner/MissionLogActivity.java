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
import com.scatterlogic.games.spyrunner.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MissionLogActivity extends Activity {
	Handler mHandler = new Handler();
	boolean running;
	Intent missionLog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		running = true;
		setContentView(R.layout.activity_mission_log);
	}

	final Runnable mUpdateResults = new Runnable(){
    	
		public void run(){
			if (running){
				updateResults();
				mHandler.postDelayed(mUpdateResults, 100);
			}
    	}
    };
    
    public void updateResults(){
    	
    }
}
