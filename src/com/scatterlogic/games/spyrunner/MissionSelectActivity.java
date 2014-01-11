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
import java.util.*;
import android.widget.*;


public class MissionSelectActivity extends Activity {
	Intent missionBriefing;

	private ArrayList<String> availableMissions; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_mission_select);
			availableMissions = new ArrayList<String>();
			final LinearLayout lines = (LinearLayout) findViewById(R.layout.activity_mission_select);
			lines.setScrollContainer(true);
			
			availableMissions = getAvailableMissionList();
			for (int i = 0; i < availableMissions.size(); i++){
				workers.add(new Worker(this, ost[i]));
				lines.addView(workers.get(i).getWindowBlob());
			}
		}
}







