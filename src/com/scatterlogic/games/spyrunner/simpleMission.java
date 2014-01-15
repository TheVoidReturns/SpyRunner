package com.scatterlogic.games.spyrunner;
import java.io.*;
import java.util.*;

import android.speech.tts.*;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.*;
import android.content.*;

public class simpleMission implements iMission, TextToSpeech.OnInitListener 
{
	String missionName;
	RobinFileWriter missionFile;

	//Array lists
	ArrayList<Milestone> timePrompts;
	ArrayList<Milestone> slowPrompts;
	ArrayList<Milestone> fastPrompts;
	

	//Array list position markers
	int timePromptsMarker;
	int slowPromptsMarker;
	int fastPromptsMarker;
	
	//Was the mission loaded?
	boolean successfulLoading;

	//Voice feedback
	TextToSpeech voiceFeedback;
	String feedback;
	
	public simpleMission(String missionName, Context caller){

		timePrompts = new ArrayList<Milestone>();
		slowPrompts = new ArrayList<Milestone>();
		fastPrompts = new ArrayList<Milestone>();
		
		timePromptsMarker = 0;
		slowPromptsMarker = 0;
		fastPromptsMarker = 0;
		
		//read in the file
		missionFile = new RobinFileWriter("Mission Files", missionName);
		if (missionFile.ReadFileContents().equals("")){
			successfulLoading = false;
		}
		else{
			successfulLoading = true;
		}
		String unparsedFileContents = missionFile.ReadFileContents();
		
		//separate the lines
		String[] parsedFileContents = unparsedFileContents.split("\n");
		
		//separate the elements within the lines (semicolon split values)
		for (int i = 2; i<parsedFileContents.length; i++){
			String[] parsedLine = parsedFileContents[i].split(";");
			long thisTime = Long.parseLong(parsedLine[0]);
			String thisDesc = parsedLine[1];
			String thisPrompt = parsedLine[2];
			boolean thisMultiFire = Boolean.parseBoolean(parsedLine[3]);
			int targetHR = Integer.parseInt(parsedLine[4]);
			
			//add the elements to the appropriate list
			Log.d("SimpleMission","Now adding " + thisPrompt + " to " + thisDesc);
			if (thisDesc.equals("Slow")) slowPrompts.add(new Milestone(thisTime, thisDesc, thisPrompt, thisMultiFire,0));
			else if (thisDesc.equals("Fast")) fastPrompts.add(new Milestone(thisTime, thisDesc, thisPrompt, thisMultiFire,0));
			else timePrompts.add(new Milestone(thisTime, thisDesc, thisPrompt, thisMultiFire,targetHR));
		}
		voiceFeedback = new TextToSpeech(caller,this);
	}

	@Override 
	public void checkPrompts(long timerValue, float speed, int HRate, double altitude)
	{
		Log.d("SimpleMission", "Comparing "+timerValue +" to "+ timePrompts.get(timePromptsMarker).millisTime);
		if (!voiceFeedback.isSpeaking()){
			if (timerValue >= timePrompts.get(timePromptsMarker).millisTime){
				feedback = timePrompts.get(timePromptsMarker).getPrompt();
				speakOut(feedback);
				timePromptsMarker++;
				if (timePromptsMarker >= timePrompts.size()) timePromptsMarker = timePrompts.size()-1;
			}
			if (timerValue >= slowPrompts.get(slowPromptsMarker).millisTime)
				slowPromptsMarker++;
			if (timerValue >= fastPrompts.get(fastPromptsMarker).millisTime)
				fastPromptsMarker++;
			
			if (slowPromptsMarker >= slowPrompts.size()) slowPromptsMarker = slowPrompts.size()-1;
			if (fastPromptsMarker >= fastPrompts.size()) fastPromptsMarker = fastPrompts.size()-1;
		
			if (timePrompts.get(timePromptsMarker).targetHR < HRate)
				speakOut(fastPrompts.get(fastPromptsMarker).getPrompt());
	
			if (timePrompts.get(timePromptsMarker).targetHR > HRate)
				speakOut(slowPrompts.get(slowPromptsMarker).getPrompt());
		}
	}

	@Override
	public String getFeedbackString(long timerValue)
	{
		if (!successfulLoading) return "File "+missionName+" not found.";
		else{
				if(timePrompts.size()<=timePromptsMarker){
					return "Mission Completed";
				}
				else return feedback;
		}
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
}
