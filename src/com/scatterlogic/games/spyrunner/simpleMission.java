package com.scatterlogic.games.spyrunner;
import java.io.*;
import java.util.*;

import android.speech.tts.*;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.*;
import android.content.*;

public class simpleMission implements iMission, OnInitListener 
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
	
	public simpleMission(String missionName, Context caller){
		
		//read in the file
		missionFile = new RobinFileWriter("Mission Files", missionName + ".txt");
		if (missionFile.ReadFileContents().equals("")){
			//makeDummyMission####################
			makeDummyMission();
			//####################################
			successfulLoading = false;
		}
		else{
			successfulLoading = true;
		}
		String unparsedFileContents = missionFile.ReadFileContents();
		
		//separate the lines
		String[] parsedFileContents = unparsedFileContents.split("\n");
		
		//separate the elements within the lines (semicolon split values)
		for (int i = 1; i<parsedFileContents.length; i++){
			String[] parsedLine = parsedFileContents[i].split(";");
			long thisTime = Long.parseLong(parsedLine[0]);
			String thisDesc = parsedLine[1];
			String thisPrompt = parsedLine[2];
			boolean thisMultiFire = Boolean.parseBoolean(parsedLine[3]);
			int targetHR = Integer.parseInt(parsedLine[4]);
			
			//add the elements to the appropriate list
			if (thisDesc.equals("Slow")) slowPrompts.add(new Milestone(thisTime, thisDesc, thisPrompt, false, thisMultiFire,0));
			else if (thisDesc.equals("Fast")) fastPrompts.add(new Milestone(thisTime, thisDesc, thisPrompt, false, thisMultiFire,0));
			else timePrompts.add(new Milestone(thisTime, thisDesc, thisPrompt, false, thisMultiFire,targetHR));
		}
		voiceFeedback = new TextToSpeech(caller,this);
	}

	@Override
	public void checkPrompts(long timerValue, float speed, int HRate, double altitude)
	{

		if (!voiceFeedback.isSpeaking()){
			if (timerValue >= timePrompts.get(timePromptsMarker).millisTime){
				timePromptsMarker++;
				if (timePromptsMarker >= timePrompts.size()) timePromptsMarker = timePrompts.size()-1;
				voiceFeedback.speak(timePrompts.get(timePromptsMarker).getPrompt(),TextToSpeech.QUEUE_FLUSH,null);
			}
			if (timerValue >= slowPrompts.get(slowPromptsMarker).millisTime)
				slowPromptsMarker++;
			if (timerValue >= fastPrompts.get(fastPromptsMarker).millisTime)
				fastPromptsMarker++;
			
			if (slowPromptsMarker >= slowPrompts.size()) slowPromptsMarker = slowPrompts.size()-1;
			if (fastPromptsMarker >= fastPrompts.size()) fastPromptsMarker = fastPrompts.size()-1;
		
			if (timePrompts.get(timePromptsMarker).targetHR < HRate)
				voiceFeedback.speak(fastPrompts.get(fastPromptsMarker).getPrompt(),TextToSpeech.QUEUE_FLUSH,null);
	
			if (timePrompts.get(timePromptsMarker).targetHR > HRate)
				voiceFeedback.speak(slowPrompts.get(slowPromptsMarker).getPrompt(),TextToSpeech.QUEUE_FLUSH,null);
		}
	}

	@Override
	public String getFeedbackString(long timerValue)
	{
		if (!successfulLoading) return "File "+missionName+" not found.";
		else{
				if(timePrompts.size()>=timePromptsMarker){
					return "Mission Completed";
				}
				else return timePrompts.get(timePromptsMarker).getPrompt();
		}
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
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
	}
	void makeDummyMission(){
		missionFile.Append("0;Slow;The undetermined thing is getting away!;false;0");
		missionFile.Append("0;Fast;You're going too fast!;false;0");
		missionFile.Append("2000;Briefing;Let's go nice and slowly;false;2");
		missionFile.Append("300000;Fast;Speed up for the reason to do with the plot!;false;4");
		missionFile.Append("360000;Fast;WELL DONE.  Now slow down again.;false;3");
		missionFile.Append("600000;Fast;Speed up for the reason to do with the plot!;false;4");
		missionFile.Append("660000;Fast;Nice!;false;4");
	}
}