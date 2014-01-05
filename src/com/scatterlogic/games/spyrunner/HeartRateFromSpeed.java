package com.scatterlogic.games.spyrunner;

public class HeartRateFromSpeed implements iHeartRateZoneFinder{

	float zones[];
	
	public HeartRateFromSpeed(float zone5,float zone4,float zone3,float zone2,float zone1){
		zones = new float[5];
		zones[0]=zone1;
		zones[1]=zone2;
		zones[2]=zone3;
		zones[3]=zone4;
		zones[4]=zone5;
	}
	
	@Override
	public int getHeartRateZone(float speed, double alitutudeGain) {
		for (int i = 4; i > 0 ; i --){
			if (speed >= zones[i]){
				return i+1;
			}
		}
		return 1;
	}

}
