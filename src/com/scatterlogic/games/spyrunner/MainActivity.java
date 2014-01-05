package com.scatterlogic.games.spyrunner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/* Main Menu class, composed on 29/12/2013
 * Just a list of intents, followed by click listeners for some buttons
 */

public class MainActivity extends Activity {
	Intent missionSelect;
	Intent bonusFeatureSelect;
	Intent spyProfileSelect;
	Intent reviewMissionSelect;
	Intent settingSelect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//here are the intents
		missionSelect = new Intent(this, MissionSelectActivity.class);
		bonusFeatureSelect = new Intent(this, BonusFeatureActivity.class);
		spyProfileSelect = new Intent(this, SpyProfileActivity.class);
		reviewMissionSelect = new Intent(this, ReviewMissionActivity.class);
		settingSelect = new Intent(this, SettingsActivity.class);
		
		//display the screen
		setContentView(R.layout.activity_main);
		
		
		//and set up the listeners
		findViewById(R.id.missionselectbutton).setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	startActivity(missionSelect);
		    } 
		});
		findViewById(R.id.bonusfeaturebutton).setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	startActivity(bonusFeatureSelect);
		    } 
		});
		findViewById(R.id.spyprofilebutton).setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	startActivity(spyProfileSelect);
		    } 
		});
		findViewById(R.id.reviewmissionsbutton).setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	startActivity(reviewMissionSelect);
		    } 
		});
		findViewById(R.id.settingsbutton).setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	startActivity(settingSelect);
		    } 
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
}