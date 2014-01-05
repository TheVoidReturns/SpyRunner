package com.scatterlogic.games.spyrunner;

public interface iHeartRateZoneFinder {
	//A boolean to see if this particular heart rate zone finder is viable
	public boolean canBeEstablished = false;
	
	//the current heart rate zone
	public int getHeartRateZone(float speed, double alitutudeGain);
}
