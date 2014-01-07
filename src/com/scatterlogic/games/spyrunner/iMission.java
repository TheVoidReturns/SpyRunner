package com.scatterlogic.games.spyrunner;
import java.io.*;
import java.util.*;

public interface iMission
{
	//Will be passed a mission name on loading, which will determine the file name this loads prompts etc...
	String missionName;
	File missionFile;
	
	//this will check for prompts based on time, speed etc. and play them as appropriate
	public void checkPrompts(long timerValue, float speed, int HRate, double altitude);
	public String getFeedbackString(long timerValue);
}
