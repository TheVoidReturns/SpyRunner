package com.scatterlogic.games.spyrunner;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.graphics.*;

public class MissionSummary
{
	String mTitle;
	TextView tTitle;
	String mBody;
	TextView tBody;
	String mPreviousBest;
	TextView tPreviousBest;
	String mFileTitle;
	
	Context context;
	RelativeLayout windowBlob;
	static int idCounter = 0;
	//A class to define a mission summary window
	public MissionSummary(String titleString, String bodyString, String previousBest, String fileName,Context appContext){
		mFileTitle = fileName;
		mTitle = titleString;
		mBody = bodyString;
		mPreviousBest = previousBest;
		context = appContext;
		
		tTitle = setStandardTextField(mTitle, idCounter + 1);
		tBody = setLittleTextField(mBody, idCounter + 2);
		tPreviousBest = setStandardTextField(mPreviousBest, idCounter + 3);
		idCounter = idCounter + 3;
	}
	
	private TextView setStandardTextField(String textToPut, int idNo){
		TextView result = new TextView(context);
		result.setTextSize(TypedValue.COMPLEX_UNIT_PT, 14);
		//result.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		result.setText(textToPut);
		result.setId(idNo);
		result.setTextColor(Color.BLACK);
		return result;
	}


	private TextView setLittleTextField(String textToPut, int idNo){
		TextView result = new TextView(context);
		result.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
		result.setText(textToPut);
		result.setId(idNo);
		result.setTextColor(Color.BLACK);
		return result;
	}	
	public RelativeLayout getWindowBlob(){
		windowBlob = new RelativeLayout(context);
		windowBlob.setBackgroundColor(Color.GRAY);
		makeLayout(context);
		return windowBlob;
	}
	private void makeLayout(Context caller){
		LayoutParams nf = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		nf.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		windowBlob.addView(tTitle, nf);
		
		LayoutParams tf = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		tf.addRule(RelativeLayout.BELOW, tTitle.getId());
		tf.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		windowBlob.addView(tBody, tf);
		
		LayoutParams starttb = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		starttb.addRule(RelativeLayout.BELOW, tTitle.getId());
		starttb.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		windowBlob.addView(tPreviousBest, starttb);
	}
	public String getFileName(){
		return mFileTitle;
	}
}
