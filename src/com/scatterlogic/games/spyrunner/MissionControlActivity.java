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

public class MissionControlActivity extends Activity {
	Intent missionReview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mission_control);

		missionReview = new Intent(this, ReviewMissionActivity.class);
		findViewById(R.id.mission_review).setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	startActivity(missionReview);
		    } 
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
}
