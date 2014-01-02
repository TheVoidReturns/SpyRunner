package com.scatterlogic.games.spyrunner;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;

public class MusicPlayer extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
	/*Still need to implement: 
		Audio Focus stuff
		audio becoming noisy (user unplugging earphones for example)
		Read user's music content - this will also be needed in the music setup activity to assign ratings etc.
	*/
	
	boolean isPrepared = false;
	boolean isPlaying = false;
    MediaPlayer musicPlayer = null;
//    private static final String ACTION_PLAY = "com.example.action.PLAY";
    String songName = "Test Songname";
    int notification_id = 1234;
    ContentResolver contentResolver;
    
    ArrayList<String> songTitles  = new  ArrayList<String>();
    ArrayList<Long> songIds  = new  ArrayList<Long>();
    int numSongs;
        
    @SuppressWarnings("deprecation")
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent.getAction().equals(ACTION_PLAY)) {
        	musicPlayer = new MediaPlayer();
        	musicPlayer.setOnPreparedListener(this);
        	musicPlayer.setOnErrorListener(this);
        	musicPlayer.prepareAsync(); // prepare asynchronously to not block main thread (could take a while to initialise)
        	musicPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        	contentResolver = getContentResolver(); //Used for reading the the user's music
        	
            //Need to run as a foreground status, with an entry in the notification bar and a way for the user to use that notification to
            //open the Mission Control activity      
        	 PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
        	                 new Intent(getApplicationContext(), MissionControlActivity.class),
        	                 PendingIntent.FLAG_UPDATE_CURRENT);
        	 Notification notification = new Notification();
        	 notification.tickerText = "Hello Hello Hello!";
        	 notification.icon = R.drawable.robin8bit;
        	 notification.flags |= Notification.FLAG_ONGOING_EVENT;
        	 //This method is deprecated but am using for this first implementation
        	 notification.setLatestEventInfo(getApplicationContext(), "MusicPlayerSample",
        	                 "Playing: " + songName, pi);
        	 startForeground(notification_id, notification);
        	         	 
//        }
        //Fudge
        return 1;
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
    	this.isPrepared = true;
    	
    	//Populate the song arrays
   	 	getUserMusic();
        player.start();
        
    }
    
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // Need to react to the error?
        // The MediaPlayer has moved to the Error state, must be reset!
    	mp.reset();
    	mp.prepareAsync();
    	
    	//Fudge
    	return true;
    }
        
    //Ensure the media player is released properly - don't rely on the garbage collector;
    @Override
    public void onDestroy() {
        if (musicPlayer != null) musicPlayer.release();
    }
    
    public void getUserMusic() {
    	Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    	Cursor cursor = contentResolver.query(uri, null, null, null, null);
    	if (cursor == null) {
    	    // query failed, handle error.
    	} else if (!cursor.moveToFirst()) {
    	    // no media on the device
    	} else {
    	    int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
    	    int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
    	    do {
    	       songIds.add(cursor.getLong(idColumn));
    	       songTitles.add(cursor.getString(titleColumn));
    	    } while (cursor.moveToNext());
    	    numSongs = songIds.size();
    	}
    	
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void play() {
		this.musicPlayer.start();
		this.isPlaying = true;
		stopForeground(false);
	}
	
	public void playSong() {
		long id = (long)songIds.get(1);
		Uri contentUri = ContentUris.withAppendedId(
		        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

		musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			musicPlayer.setDataSource(getApplicationContext(), contentUri);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void pause() {
		this.musicPlayer.pause();
		this.isPlaying = false;
	}
	
	public void stop() {
		this.musicPlayer.stop();
		this.isPlaying = false;
		stopForeground(true);
		musicPlayer.release();
	}
	
	public void skip() {
		//this.musicPlayer.selectTrack(1);
	}
}

