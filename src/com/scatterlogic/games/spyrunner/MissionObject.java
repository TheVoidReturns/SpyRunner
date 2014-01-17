package com.scatterlogic.games.spyrunner;

import android.util.Log;

public class MissionObject {
	//File content holders
	String missionFileName;
	RobinFileWriter missionFile;
	String missionFileContents;
	String[] tags;
	String[] values;
	
	//Was the mission loaded?
	boolean successfulLoading;
	
	public MissionObject(String missionFileName){
			//read in the file
			missionFile = new RobinFileWriter("Mission Files", missionFileName);
			missionFileContents = missionFile.ReadFileContents();
			if (missionFileContents.equals("")){
				successfulLoading = false;
			}
			else{
				successfulLoading = true;
			}
			
			//Read in all of the tags and values:
			
			//separate the lines
			String [] parsedFileContents = missionFileContents.split("\n");
			
			//read in the tags and values
			tags = new String[parsedFileContents.length];
			values = new String[parsedFileContents.length];
			
			String[] tempSecondParse;
			for (int i = 0; i < parsedFileContents.length; i++){
				tempSecondParse = parsedFileContents[i].split(":");
				tags[i] = tempSecondParse[0];
				values[i] = "";
				for (int j = 1; j < tempSecondParse.length; j++){
					values[i] = values[i] + tempSecondParse[j];
				}
			}
	}
	
	public String getParameter(String parameterName) {
		for (int i = 0; i < tags.length; i++){
			Log.d("simpleMission", "Comparing " + parameterName + " to " + tags[i]);
			if(parameterName.equalsIgnoreCase(tags[i]))
				return values[i];
		}
		return "Parameter not found.";
	}

	public void setParameter(String parameterName, String valueToSetTo) {
		for (int i = 0; i < tags.length; i++){
			Log.d("simpleMission", "Comparing " + parameterName + " to " + tags[i]);
			if(parameterName.equalsIgnoreCase(tags[i])){
				tags[i] = values[i];
				Log.d("simpleMission", parameterName + " set to " + valueToSetTo);
			}			
		}
	}
	public String[] getAllMatchingTagValues(String tagToMatch){
		String returnValues = "";
		Log.d("Mission Object", "Asked to find " + tagToMatch);
		for (int i = 0; i < tags.length; i++){
			if (tags[i].contains(tagToMatch))
				if (returnValues.equals("")) returnValues = values[i];
				else returnValues = returnValues + "/;/" + values[i];
		}
		if (returnValues.equals("")){
			returnValues = "0/,/Nothing Found for " + tagToMatch;
		}
		Log.d("Mission Object", "About to return parsed version of " + returnValues);
		return returnValues.split("/;/"); 
	}
}