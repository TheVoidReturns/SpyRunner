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
		RobinFileWriter currentFile = new RobinFileWriter("Mission Files", "Mission 1.mis");
		String missionFile = "Title:Mission 1\n"+
				"Description:Go to the briefing\n"+
				"Briefing:Attend the briefing given by Major Morgan.\n"+
				"TimedEvent:1000/,/Attend the briefing location/,/2\n"+
				"FastEvent:1000/,/No rush!  You don't want to turn up too out of breath!\n"+
				"SlowEvent:1000/,/You're not going to make it on time at this rate.  Pick it up a bit.\n"+
				"TimedEvent:300000/,/The major's going to be there early, let's make sure we're there before him!  Step it up to a sustainable jog./,/3\n"+
				"TimedEvent:600000/,/Stretch it out.  Don't sprint, just pick it up a bit./,/4\n"+
				"TimedEvent:660000/,/OK, good pace!  Let's have a nice, slow recovery jog./,/2\n"+
				"TimedEvent:900000/,/Let's try a sprint for one minute/,/5\n"+
				"TimedEvent:960000/,/Good speed!  Now recover again./,/2\n"+
				"TimedEvent:1140000/,/That should be better, let's get back to a standard jog./,/3\n"+
				"TimedEvent:1500000/,/Let's try one last stretch out./,/4\n"+
				"TimedEvent:1560000/,/And now to finish off.  Just 4 minutes to go./,/3\n"+
				"TimedEvent:1800000/,/Mission Complete.  You've made it with time to spare./,/3\n";
		currentFile.Append(missionFile);
		
		currentFile = new RobinFileWriter("Mission Files", "Mission 2.mis");
		missionFile = "Title:Mission 2\n"+
				"Description:  Maintain tracking distance to subject.\n"+
				"Briefing:You are carrying a tracking device, following the location of John Doe, an associate of Mr Big.  You need to be close enough for the device to track, but not so close you're seen...\n"+
				"TimedEvent:1000/,/Scour the area for the signal/,/2\n"+
				"FastEvent:300000/,/You're getting too close!\n"+
				"SlowEvent:300000/,/He's getting away, speed up!.\n"+
				"TimedEvent:300000/,/Found him, you shuold be able to keep hold of him at your current rate./,/3\n"+
				"TimedEvent:600000/,/He's sped up, don't lose him./,/4\n"+
				"TimedEvent:660000/,/Good work!  He's puffed out, and slowed down./,/3\n"+
				"TimedEvent:900000/,/He's sprinting away from you!  Keep up!/,/5\n"+
				"TimedEvent:960000/,/OK, he's tired and slowed down a lot./,/2\n"+
				"TimedEvent:1140000/,/He's recovered, get back to your normal pace./,/3\n"+
				"TimedEvent:1500000/,/The batteries are getting weak.  Get closer./,/4\n"+
				"TimedEvent:1560000/,/OK, that's close enough./,/3\n"+
				"TimedEvent:1800000/,/Mission Complete.  He's gone to ground.  We've got the address./,/3\n";
		currentFile.Append(missionFile);

		currentFile = new RobinFileWriter("Mission Files", "Mission 3.mis");
		missionFile = "Title:Mission 3\n"+
				"Description:  Signal Intercept.\n"+
				"Briefing:There are signals being transmitted from the address identified last time.  They are scanning for you, and will detect if you remain too still or if you overheat.  You will need to maintain a gentle pace whilst we intercept their signals.\n"+
				"TimedEvent:1000/,/Get into the vicinity of the signal/,/2\n"+
				"FastEvent:300000/,/You're heating up too much, slow down!\n"+
				"SlowEvent:300000/,/They're picking you up, you not moving fast enough!.\n"+
				"TimedEvent:300000/,/Area identified.  Intercepting signal now./,/3\n"+
				"TimedEvent:600000/,/Signal intercept is now twenty five percent complete./,/3\n"+
				"TimedEvent:900000/,/Signal intercept is now fifty percent completed/,/3\n"+
				"TimedEvent:1200000/,/Seventy five percent of signal intercept completed/,/3\n"+
				"TimedEvent:1500000/,/Signal intercept completed.  Leave the area undetected./,/3\n"+
				"TimedEvent:1800000/,/Mission Completed.  Analysing signal intercept now.../,/3\n";
		currentFile.Append(missionFile);
		
		currentFile = new RobinFileWriter("Mission Files", "Mission 4.mis");
		missionFile = "Title:Mission 4\n"+
				"Description:  Do another thing.\n"+
				"Briefing:Do a thing involving running\n"+
				"TimedEvent:1000/,/Warm Up/,/2\n"+
				"FastEvent:300000/,/You're going too fast!\n"+
				"SlowEvent:300000/,/He's getting away, speed up!.\n"+
				"TimedEvent:300000/,/Found him, you shuold be able to keep hold of him at your current rate./,/3\n"+
				"TimedEvent:600000/,/He's sped up, don't lose him./,/4\n"+
				"TimedEvent:660000/,/Good work!  He's puffed out, and slowed down./,/3\n"+
				"TimedEvent:900000/,/He's sprinting away from you!  Keep up!/,/5\n"+
				"TimedEvent:960000/,/OK, he's tired and slowed down a lot./,/2\n"+
				"TimedEvent:1140000/,/He's recovered, get back to your normal pace./,/3\n"+
				"TimedEvent:1500000/,/The batteries are getting weak.  Get closer./,/4\n"+
				"TimedEvent:1560000/,/OK, that's close enough./,/3\n"+
				"TimedEvent:1800000/,/Mission Complete.  He's gone to ground.  We've got the address./,/3\n";
		currentFile.Append(missionFile);

		currentFile = new RobinFileWriter("Mission Files", "Mission 5.mis");
		missionFile = "Title:Mission 5\n"+
				"Description: Test Mission.\n"+
				"Briefing:You are carrying a tracking device, following the location of John Doe, an associate of Mr Big.  You need to be close enough for the device to track, but not so close you're seen...\n"+
				"TimedEvent:1000/,/One second has passed/,/2\n"+
				"FastEvent:30000/,/Fast Event Triggered!\n"+
				"SlowEvent:30000/,/Slow Event Triggered!.\n"+
				"TimedEvent:30000/,/Thirty Second event./,/3\n"+
				"TimedEvent:60000/,/One minute event./,/4\n"+
				"TimedEvent:90000/,/Ninety second event!/,/5\n"+
				"TimedEvent:120000/,/Two minute event./,/2\n"+
				"TimedEvent:180000/,/Three minute event./,/3\n";
		currentFile.Append(missionFile);
		
		currentFile = new RobinFileWriter("Mission Files", "Mission 6.mis");
		missionFile = "Title:Mission 6\n"+
				"Description:  Maintain tracking distance to subject.\n"+
				"Briefing:You are carrying a tracking device, following the location of John Doe, an associate of Mr Big.  You need to be close enough for the device to track, but not so close you're seen...\n"+
				"TimedEvent:1000/,/Scour the area for the signal/,/2\n"+
				"FastEvent:300000/,/You're getting too close!\n"+
				"SlowEvent:300000/,/He's getting away, speed up!.\n"+
				"TimedEvent:300000/,/Found him, you shuold be able to keep hold of him at your current rate./,/3\n"+
				"TimedEvent:600000/,/He's sped up, don't lose him./,/4\n"+
				"TimedEvent:660000/,/Good work!  He's puffed out, and slowed down./,/3\n"+
				"TimedEvent:900000/,/He's sprinting away from you!  Keep up!/,/5\n"+
				"TimedEvent:960000/,/OK, he's tired and slowed down a lot./,/2\n"+
				"TimedEvent:1140000/,/He's recovered, get back to your normal pace./,/3\n"+
				"TimedEvent:1500000/,/The batteries are getting weak.  Get closer./,/4\n"+
				"TimedEvent:1560000/,/OK, that's close enough./,/3\n"+
				"TimedEvent:1800000/,/Mission Complete.  He's gone to ground.  We've got the address./,/3\n";
		currentFile.Append(missionFile);
		}
}