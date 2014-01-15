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
import java.io.*;
import android.os.*;


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
				final String extra = availableMissions.get(i).getFileName();
				
				if (!(extra.equals("N/A"))){
					missionBriefing= new Intent(this, MissionBriefingActivity.class);
					nextView.setOnClickListener(new OnClickListener() {
					    public void onClick(View v) {
							missionBriefing.putExtra("FileName",extra);
					    	startActivity(missionBriefing);
					    } 
					});
				} else {
					missionBriefing= new Intent(this, MissionDownLoadActivity.class);
					nextView.setOnClickListener(new OnClickListener() {
					    public void onClick(View v) {
					    	startActivity(missionBriefing);
					    } 
					});

				}
				
			}
		}
	private void getAvailableMissionList(){
		try{
			File missionFolder = new File(Environment.getExternalStorageDirectory()+"/Mission Files");
			//gets a list of the files
			File[] sdDirList = missionFolder.listFiles();
			
			String missionTitle;
			String missionDesc;
			String pBest;
			File fileToHandle;
			for(int i=0;i<sdDirList.length;i++){
				fileToHandle = new File(Environment.getExternalStorageDirectory()+
							"/Mission Files/"+sdDirList[i].getName());
				String params = ReadFileContents(fileToHandle);
				String [] tokenized = params.split("\n");
				missionTitle = tokenized[0];
				missionDesc = tokenized[1];
				pBest = "N/K";
				availableMissions.add(new MissionSummary(missionTitle,missionDesc, pBest,fileToHandle.getName(),this));
			}
		}
		catch (NullPointerException e) {
		    availableMissions.add(new MissionSummary("No missions found", "Click here to download new missions", "", "N/A",this));
		}
	}
	
	public String ReadFileContents(File fileToHandle){
		String output = "";
		try
		{
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fileToHandle));
			while (inputStream.available() > 0)
			{
				output = output + (char) inputStream.read();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}

		return output;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        Log.d("FileWriter", "External Readable");
	    	return true;
	    }
	    Log.d("FileWriter", "External Not Readable");
	    return false;
	}
}







