package com.scatterlogic.games.spyrunner;
import java.io.*;
import java.util.*;

import android.speech.tts.*;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.*;
import android.content.*;

public class simpleMission implements iMission, TextToSpeech.OnInitListener 
{
	MissionObject thisMission;
	
	final long millisBetweenSpeedPrompts = 30000;
	long timeOfLastSpeedPrompt;
	
	//timer trackers
	long[] timedPromptTimers;
	String[] timedPrompts;
	int[] promptHRs;
	int timedPromptTracker;
	
	long[] slowPromptTimers;
	String[] slowPrompts;
	int slowPromptTracker;
	
	long[] fastPromptTimers;
	String[] fastPrompts;
	int fastPromptTracker;
	
	//Voice feedback
	TextToSpeech voiceFeedback;
	String feedback;
	
	public simpleMission(String missionFileName, Context caller){
		thisMission = new MissionObject(missionFileName);
		voiceFeedback = new TextToSpeech(caller,this);
		
		//Read in the timed events
		slowPromptTracker = 0;
		fastPromptTracker = 0;
		
		loadTimedEvents();
		loadSlowEvents();
		loadFastEvents();
	}

	@Override 
	public void checkPrompts(long timerValue, float speed, int HRate, double altitude)
	{
		if (timedPromptTracker < timedPrompts.length){
			if (timerValue >= timedPromptTimers[timedPromptTracker]){
					speakOut(timedPrompts[timedPromptTracker]);
					timedPromptTracker++;
			} else{
				if (HRate < promptHRs[timedPromptTracker]){
					if ((timerValue - timeOfLastSpeedPrompt) >= millisBetweenSpeedPrompts){
						speakOut(slowPrompts[slowPromptTracker]);
						timeOfLastSpeedPrompt = timerValue;
					}
				}
				if(HRate > promptHRs[timedPromptTracker]){
					if ((timerValue - timeOfLastSpeedPrompt) >= millisBetweenSpeedPrompts){
						speakOut(fastPrompts[fastPromptTracker]);
						timeOfLastSpeedPrompt = timerValue;
					}
				}
			}
			if (timerValue >= fastPromptTimers[fastPromptTracker]){
				fastPromptTracker++;
				if (fastPromptTracker == fastPromptTimers.length) fastPromptTracker--;
			}
			if (timerValue >= slowPromptTimers[slowPromptTracker]){
				slowPromptTracker++;
				if (slowPromptTracker == slowPromptTimers.length) slowPromptTracker--;
			}
		}
	}

	@Override
	public String getFeedbackString(long timerValue)
	{
		if (timedPromptTracker == 0) return "Go!";
		else return timedPrompts[timedPromptTracker - 1];
	}
	public void speakOut(String inString){
		Log.d("SimpleMission", "SpeakOut asked to utter " + inString);
		voiceFeedback.speak(inString,TextToSpeech.QUEUE_ADD,null);
	}
	//This sets up the voice feedback
	@Override
	public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = voiceFeedback.setLanguage(Locale.UK);

            if (result == TextToSpeech.LANG_MISSING_DATA
				|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                voiceFeedback.setSpeechRate((float) 0.8);
				speakOut("Voice feedback initiated.");
                Log.d("TTS", "Speech enabled");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
	}

	@Override
	public boolean isReady()
	{
		return true;
	}

	@Override
	public String getParameter(String parameterName) {
		return thisMission.getParameter(parameterName);
	}

	@Override
	public void setParameter(String parameterName, String valueToSetTo) {
		thisMission.setParameter(parameterName, valueToSetTo);
	}
	private void loadTimedEvents(){
		timedPromptTracker = 0;
		String[] tempStringArray = thisMission.getAllMatchingTagValues("TimedEvent");
		if (tempStringArray.length > 0){
			timedPromptTimers = new long[tempStringArray.length];
			timedPrompts = new String[tempStringArray.length];
			promptHRs = new int[tempStringArray.length];
			String tempString;
			for (int i = 0; i < tempStringArray.length; i++){
				Log.d("simpleMission", "Got " + tempStringArray[i]);
				
				tempString = tempStringArray[i].split("/,/")[0];
				Log.d("simpleMission", "tempString is *" + tempString + "*");
				timedPromptTimers[i] = Long.parseLong(tempString.replaceAll("\\D+",""));
				
				tempString = tempStringArray[i].split("/,/")[1];
				Log.d("simpleMission", "tempString is *" + tempString + "*");
				timedPrompts[i] = tempString;
				
				tempString = tempStringArray[i].split("/,/")[2];
				//Remove all non-digits before parsing
				tempString = tempString.replaceAll("\\D+","");

				Log.d("simpleMission", "tempString is *" + tempString + "*");
				promptHRs[i] = Integer.parseInt(tempString);
			}
		} else {
			timedPromptTimers = new long[1];
			timedPromptTimers[0] = 0;
			
			timedPrompts = new String[1];
			timedPrompts[0] = "Error!  No values fetched!";
			
			promptHRs = new int[1];
			promptHRs[0] = 1;
		}
	}

	private void loadSlowEvents(){
		String[] tempStringArray;
		tempStringArray = thisMission.getAllMatchingTagValues("SlowEvent");
		slowPromptTimers = new long[tempStringArray.length];
		slowPrompts = new String[tempStringArray.length];
		String tempString="";
		
		for (int i = 0; i < tempStringArray.length; i++){
			Log.d("simpleMission", "Got " + tempStringArray[i]);
			
			tempString = tempStringArray[i].split("/,/")[0];
			//Remove all non-digits before parsing
			tempString = tempString.replaceAll("\\D+","");
			slowPromptTimers[i] = Long.parseLong(tempString);
			
			tempString = tempStringArray[i].split("/,/")[1];
			slowPrompts[i] = tempString;
		}
	}
	
	private void loadFastEvents(){
		String[] tempStringArray;
		tempStringArray = thisMission.getAllMatchingTagValues("FastEvent");
		fastPromptTimers = new long[tempStringArray.length];
		fastPrompts = new String[tempStringArray.length];
		String tempString="";
		
		for (int i = 0; i < tempStringArray.length; i++){
			Log.d("simpleMission", "Got " + tempStringArray[i]);
			
			tempString = tempStringArray[i].split("/,/")[0];
			//Remove all non-digits before parsing
			tempString = tempString.replaceAll("\\D+","");
			fastPromptTimers[i] = Long.parseLong(tempString);
			
			tempString = tempStringArray[i].split("/,/")[1];
			fastPrompts[i] = tempString;
		}
	}
}
