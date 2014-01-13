package com.scatterlogic.games.spyrunner;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MissionSelectActivity extends Activity {
	Intent missionBriefing;

	private ArrayList<MissionSummary> availableMissions; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mission_select);
			availableMissions = new ArrayList<MissionSummary>();
			final LinearLayout lines = (LinearLayout) findViewById(R.id.lines);
			Log.d("Mission Select Activity", "So far, so good: lines found.");
			lines.setScrollContainer(true);
			lines.setBackgroundColor(Color.RED);
			
			getAvailableMissionList();
			for (int i = 0; i < availableMissions.size(); i++){
				Log.d("Mission Select Activity", "About to add " + availableMissions.get(i).mTitle + ".");
				RelativeLayout nextView = availableMissions.get(i).getWindowBlob();
				lines.addView(nextView);
				
				missionBriefing= new Intent(this, MissionBriefingActivity.class);
				nextView.setOnClickListener(new OnClickListener() {
				    public void onClick(View v) {
				    	startActivity(missionBriefing);
				    } 
				});
				
			}
		}
	private void getAvailableMissionList(){
		//TODO:  This, properly
		availableMissions.add(new MissionSummary("Mission 1","The Saga Begins", "Bad", this));
		availableMissions.add(new MissionSummary("Mission 2","The Saga Continues", "Bad", this));
		availableMissions.add(new MissionSummary("Mission 3","The Saga Ends", "Bad", this));
		availableMissions.add(new MissionSummary("Mission 4","The Saga Goes for an unneccessary sequel", "Bad", this));
	}
}







