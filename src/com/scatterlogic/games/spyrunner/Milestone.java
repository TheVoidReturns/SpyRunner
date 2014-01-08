package com.scatterlogic.games.spyrunner;
import android.util.*;

public class Milestone
{
	long millisTime;
	String prompt;
	String descriptor;
	int targetHR;
	boolean fired;
	boolean multipleFire;
	public Milestone(long millisTime, String descriptor, String prompt,  boolean multipleFire,int targetHR){
		this.millisTime = millisTime;
		this.prompt = prompt;
		this.descriptor = descriptor;
		this.fired = false;
		this.multipleFire = multipleFire;	
		this.targetHR = targetHR;
		Log.d("Milestone", "Milestone " + descriptor + ": " + prompt + " established.");
	}
	public String getPrompt(){
			if(fired) return "";
			else{
				if(!multipleFire) fired=true;
				Log.d("Milestone", "Milestone " + descriptor + ": " + prompt + " fired.");
				return prompt;
			}
	}
}
