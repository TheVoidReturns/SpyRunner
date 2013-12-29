package com.scatterlogic.games.spyrunner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	Intent missionSelect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		missionSelect = new Intent(this, MissionSelectActivity.class);
		
		
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.missionselectbutton).setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	startActivity(missionSelect);
		    } 
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
	
	protected void onPause(Bundle savedInstanceState){
		super.onPause();
	}

}