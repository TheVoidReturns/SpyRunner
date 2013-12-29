package com.scatterlogic.games.spyrunner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import android.os.Environment;
import android.util.Log;

//FileWriter: created on 12/06/2013 on train
//IMPORTANT:  You need to declare the following permissions in the manifest:
//<uses-permission android:name="WRITE_EXTERNAL_STORAGE" />
//<uses-permission android:name="READ_EXTERNAL_STORAGE" />

public class RobinFileWriter {
	File fileToHandle;
	File directory;
	public RobinFileWriter(String directory, String fileName){
		if (isExternalStorageWritable())
		{
			this.directory = new File(Environment.getExternalStorageDirectory()+"/"+directory);
			//this.directory = new File("/mnt/sdcard/Leia");

			Log.e("FileWriter", "Trying: " + this.directory.getAbsolutePath());
		}
		if (!this.directory.mkdirs())
		{
			Log.e("FileWriter", "Unable to create directory");
		}
		else Log.e("FileWriter", "Directory created");
		fileToHandle = new File(this.directory, fileName);
	}
	public boolean Append(String stringToAppend)
	{		
		String existingString = this.ReadFileContents();
		try 
		{
			FileOutputStream f = new FileOutputStream(this.fileToHandle);
			PrintWriter pw = new PrintWriter(f);
			pw.println(existingString + stringToAppend);
			pw.flush();
			pw.close();
			f.close();
			//Log.e("FileWriter", "Wrote " + stringToAppend);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			Log.e("FileWriter", "File Not Found: " + fileToHandle.getAbsolutePath());
			return false;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		Log.e("FileWriter", "Written: \n" + stringToAppend);
		return true;
	}
	
	public String ReadFileContents(){
		String output = "";
		try
		{
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(this.fileToHandle));
			while (inputStream.available() > 0)
			{
				output = output + (char) inputStream.read();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return output;
	}
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	    	Log.e("FileWriter", "External Writable");
	    	return true;
	    }
	    Log.e("FileWriter", "External Not Writable");
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        Log.e("FileWriter", "External Readable");
	    	return true;
	    }
	    Log.e("FileWriter", "External Not Readable");
	    return false;
	}
	public String tellMeAboutYourself(){
		return "I have directory " + directory.getAbsolutePath() + "\n" +
			" and file " + this.fileToHandle.getName();
	}
}
