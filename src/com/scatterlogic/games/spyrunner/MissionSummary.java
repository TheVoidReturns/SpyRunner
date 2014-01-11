package com.scatterlogic.games.spyrunner;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.widget.GridLayout.*;

public class MissionSummary
{
	String mTitle;
	String mBody;
	String mPreviousBest;
	Context context;
	//A class to define a mission summary window
	public MissionSummary(String titleString, String bodyString, String previousBest,Context appContext){
		mTitle = titleString;
		mBody = bodyString;
		mPreviousBest = previousBest;
		context = appContext;
	}
	
	public View getWindowBlob(){
			TextView returnView = new TextView(context);
		LayoutParams lps = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	}
}
