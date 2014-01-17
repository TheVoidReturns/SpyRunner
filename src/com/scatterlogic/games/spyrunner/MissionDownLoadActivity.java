package com.scatterlogic.games.spyrunner;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MissionDownLoadActivity extends Activity {
	Intent missionBriefing;

	private ArrayList<MissionSummary> availableMissions; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mission_select);
	}
	@Override
	protected void onResume() {
		super.onResume();
		//TODO Downloading code....
		TextView feedback = new TextView(this);
		feedback.setTextSize(TypedValue.COMPLEX_UNIT_PT, 14);
		//result.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		feedback.setText("Making files...");
		feedback.setId(1);
		feedback.setTextColor(Color.RED);

		LinearLayout lines = (LinearLayout) findViewById(R.id.lines);
		lines.addView(feedback);
		makeFiles();
		this.finish();
	}	
	private void makeFiles(){
		long[] missionTimings = new long[10];
		String parsableString = "";
		String[] missionPrompts = new String [10];
		String[] promptTitles = new String [10];
		
		int[] missionHrZones = new int[10];
		
		final long minsToMillis = 1000*60;
		
		//3 minutely events
		for (int i = 0; i<8; i++){
			missionTimings[i] = 3*minsToMillis*i;
		}
		missionTimings[8] = 0;
		missionTimings[9] = 0;
		
		RobinFileWriter currentFile = new RobinFileWriter("Mission Files", "Mission 1.mis");
		parsableString = "Briefing, Stage 1, Stage 2, Stage 3, Stage 4, Stage 5, Stage 6, Stage 7, Fast, Slow";
		promptTitles = parsableString.split(", ");
		parsableString = "Follow the man, Follow the man faster, Follow the man more slowly, Walk with a bizarre limp, " +
							"Run with your hands in the air, Run like you just don't care, Run like a fat bloke near the end of his run, " +
							"Warm down, He's getting away!, You're going too fast!";
		for (int i = 0; i<10; i++){
			missionHrZones[i] = 3;
		}
		
		currentFile.Append(makeAMission("Mission 1", "An easy mission",missionTimings, promptTitles, missionPrompts, missionHrZones));
	}
	private String makeAMission(String title, String description, long[] timings, String[] promptTitles, String[] prompts, int[] hrZones){
		String outString = title + "\n" + description + "\n";
		
		for (int i = 0; i < 10; i++){
			if (i < 8)
				outString = outString + timings[i] + ";" + promptTitles[i] + ";" + prompts[i] + ";false;" + hrZones[i] + "\n";
			else
				outString = outString + timings[i] + ";" + promptTitles[i] + ";" + prompts[i] + ";true;" + hrZones[i] + "\n";
		}
		return outString;
	}
}
	








