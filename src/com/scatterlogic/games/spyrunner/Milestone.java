package com.scatterlogic.games.spyrunner;

public class Milestone
{
	long millisTime;
	String prompt;
	String descriptor;
	int targetHR;
	boolean fired;
	boolean multipleFire;
	public Milestone(long millisTime, String descriptor, String prompt, boolean fired, boolean multipleFire,int targetHR){
		this.millisTime = millisTime;
		this.prompt = prompt;
		this.descriptor = descriptor;
		this.fired = fired;
		this.multipleFire = multipleFire;	
		this.targetHR = targetHR;
	}
	public String getPrompt(){
			if(fired) return "";
			else{
				if(!multipleFire) fired=true;
				return prompt;
			}
	}
}
