package com.scatterlogic.games.spyrunner;

import java.io.File;
import java.util.ArrayList;

//This describes the desired Music Player
public interface iMusicPlayer {
	ArrayList <String> songList = new ArrayList <String>();
	ArrayList <String> speedList = new ArrayList <String>();
	
	void play();
	void pause();
	void stop();
	void next();
	void previous();
	void restart();
	
	//A reduction in volume for voice feedback and mission events
	void shhh();
	void returnToOriginalVolume();
	
	void playSong(float targetSpeed);
	
	//this will receive speeds from the exerciseTracker via mission control, and
	//use it to update the song detail log
	void commitSpeed(float passedSpeed);
}
