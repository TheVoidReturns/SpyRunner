package com.scatterlogic.games.spyrunner;
import android.content.Context;

import java.util.ArrayList;
import java.util.Random;
import android.media.MediaPlayer;
import java.io.IOException;
import java.util.EnumSet;

import android.media.AudioManager;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
 
/**
 * A wrapper class for {@link android.media.MediaPlayer}.
 * <p>
 * Encapsulates an instance of MediaPlayer, and makes a record of its internal state accessible via a
 * {@link MediaPlayerWrapper#getState()} accessor. Most of the frequently used methods are available, but some still
 * need adding.
 * </p>
 * Media Player State Wrapper by Daniel Hawkes. 
 * Rest of the class by Chris Peel
 */

public class MusicPlayer {
 
	private static String tag = "MusicPlayer";
	private MediaPlayer mPlayer;
	private State currentState;
	private MusicPlayer musicPlayer;
	UserMusic userMusic;
	Context context;
	Random random;
	boolean isPrepared = false;
	ArrayList<Integer> playHistoryIndex  = new  ArrayList<Integer>();
	int currentSongIndex;
	int historyIndex = 0; //Need to know where in the play history we are so user can skip back and forth consistently
	
	public MusicPlayer(Context mcontext) {
		context = mcontext;

		musicPlayer = this;
		mPlayer = new MediaPlayer();
		userMusic = new UserMusic(context);
		currentState = State.IDLE;
		random = new Random();
		currentSongIndex = random.nextInt(userMusic.numSongs);
		playHistoryIndex.add(currentSongIndex);
		
		mPlayer.setOnPreparedListener(mOnPreparedListener);
		mPlayer.setOnCompletionListener(mOnCompletionListener);
		mPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
		mPlayer.setOnErrorListener(mOnErrorListener);
		mPlayer.setOnInfoListener(mOnInfoListener);
		
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		musicPlayer.setDataSource(currentSongIndex);
		Log.d("MusicPlayer", "MusicPlayer First Instance Instantiated");
	}
	
	/* METHOD WRAPPING FOR STATE CHANGES */
	public static enum State {
		IDLE, ERROR, INITIALIZED, PREPARING, PREPARED, STARTED, STOPPED, PLAYBACK_COMPLETE, PAUSED;
	}
 
	public void setDataSource(int songIndex) {
		if (currentState == State.IDLE) {
			try {
				mPlayer.setDataSource(context, userMusic.songUris.get(songIndex));
				currentState = State.INITIALIZED;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else throw new RuntimeException();
	}
 
	public void prepareAsync() {
//		if (EnumSet.of(State.INITIALIZED, State.STOPPED).contains(currentState)) {
			mPlayer.prepareAsync();
			currentState = State.PREPARING;
//		} else throw new RuntimeException();
		
		Log.d(tag, "prepared Async()"); 
	}
 
	public boolean isPlaying() {
		Log.d(tag, "isPlaying()");
		if (currentState != State.ERROR) {
			return mPlayer.isPlaying();
		} else throw new RuntimeException();
	}
 
	public void seekTo(int msec) {
		Log.d(tag, "seekTo()");
		if (EnumSet.of(State.PREPARED, State.STARTED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
			mPlayer.seekTo(msec);
		} else throw new RuntimeException();
	}
 
	public void pause() {
		Log.d(tag, "pause()");
		if (EnumSet.of(State.STARTED, State.PAUSED).contains(currentState)) {
			mPlayer.pause();
			currentState = State.PAUSED;
		} else throw new RuntimeException();
	}
 
	public void play() {
		Log.d(tag, "play()");
		if (EnumSet.of(State.PREPARED, State.STARTED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
			mPlayer.start();
			currentState = State.STARTED;
			Log.d("Music Player play()", playHistoryIndex.get(playHistoryIndex.size() - 1).toString());
			Log.d("Music Player play()", String.valueOf(currentSongIndex ));
			Log.d("Music Player play()", String.valueOf(historyIndex ));
			Log.d("Music Player play()", "PlayHistoryIndex Size = " + String.valueOf(playHistoryIndex.size() - 1));
			//Add song to Music History 
			//Do not add if the song about to play is the same as the one that was playing previously (e.g. user pauses music)
			//Also do not add if the user is in the middle of their play history from the session
			if (playHistoryIndex.get(playHistoryIndex.size() - 1) != currentSongIndex && historyIndex == playHistoryIndex.size()) {
				playHistoryIndex.add(currentSongIndex);
			}
						
		} else throw new RuntimeException();
	}
 
	public void stop() {
		Log.d(tag, "stop()");
		if (EnumSet.of(State.PREPARED, State.STARTED, State.STOPPED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(
				currentState)) {
			mPlayer.stop();
			currentState = State.STOPPED;
		} else throw new RuntimeException();
	}
 
	public void reset() {
		Log.d(tag, "reset()");
		mPlayer.reset();
		currentState = State.IDLE;
	}
	
	public void next() {
		musicPlayer.stop();
		musicPlayer.reset();
		
		//set data source
		if(historyIndex < playHistoryIndex.size() - 1) {
			Log.d(tag, "playHistoryIndex Array size is: " + String.valueOf(playHistoryIndex.size()));
			musicPlayer.setDataSource(playHistoryIndex.get(historyIndex + 1));
		}
		else {
			int newSong = random.nextInt(userMusic.numSongs);
			musicPlayer.setDataSource(newSong);
			currentSongIndex = newSong;
		}
		musicPlayer.prepareAsync();
		historyIndex++;
	}
	
	public void previous() {
		//Only change song if there is a previous one to go to
		if (historyIndex > 0) {
			musicPlayer.stop();
			musicPlayer.reset();
			Log.e(tag, "playHistoryIndex Array size is: " + String.valueOf(playHistoryIndex.size()));
			musicPlayer.setDataSource(playHistoryIndex.get(historyIndex - 1));
			musicPlayer.prepareAsync();
			currentSongIndex = playHistoryIndex.get(historyIndex - 1);
			historyIndex--;	
		}
	}
 
	/**
	 * @return The current state of the mediaplayer state machine.
	 */
	public State getState() {
		Log.d(tag, "getState()");
		return currentState;
	}
 
	public void release() {
		Log.d(tag, "release()");
		mPlayer.release();
	}
 
	/* INTERNAL LISTENERS */
	private OnPreparedListener mOnPreparedListener = new OnPreparedListener() {
 
		@Override
		public void onPrepared(MediaPlayer mp) {
			Log.d(tag, "on prepared");
			currentState = State.PREPARED;
			musicPlayer.onPrepared(mp);
			currentState = State.STARTED;
		}
	};
	private OnCompletionListener mOnCompletionListener = new OnCompletionListener() {
 
		@Override
		public void onCompletion(MediaPlayer mp) {
			Log.d(tag, "on completion");
			currentState = State.PLAYBACK_COMPLETE;
			musicPlayer.onCompletion(mp);
		}
	};
	private OnBufferingUpdateListener mOnBufferingUpdateListener = new OnBufferingUpdateListener() {
 
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			Log.d(tag, "on buffering update");
			musicPlayer.onBufferingUpdate(mp, percent);
		}
	};
	private OnErrorListener mOnErrorListener = new OnErrorListener() {
 
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Log.d(tag, "on error");
			currentState = State.ERROR;
			musicPlayer.onError(mp, what, extra);
			return false;
		}
	};
	private OnInfoListener mOnInfoListener = new OnInfoListener() {
 
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			Log.d(tag, "on info");
			musicPlayer.onInfo(mp, what, extra);
			return false;
		}
	};
 
	/* EXTERNAL STUBS TO OVERRIDE */
	public void onPrepared(MediaPlayer mp) {
		isPrepared = true;
		musicPlayer.play();
	}
 
	public void onCompletion(MediaPlayer mp) {
		
		
	}
 
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		
	}
 
	boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}
 
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		return false;
	}
	
	/* OTHER STUFF */
	public int getCurrentPosition() {
		if (currentState != State.ERROR) {
			return mPlayer.getCurrentPosition();
		} else {
			return 0;
		}
	}
 
	public int getDuration() {
		// Prepared, Started, Paused, Stopped, PlaybackCompleted
		if (EnumSet.of(State.PREPARED, State.STARTED, State.PAUSED, State.STOPPED, State.PLAYBACK_COMPLETE).contains(
				currentState)) {
			return mPlayer.getDuration();
		} else {
			return 100;
		}
	}
	
	public void playSong(String songTitle) {
		//Get array index of the song
		Uri songUri = null; 
		int i = 0;
		do {
			if (userMusic.songTitles.get(i).equals(songTitle)) {
				songUri = userMusic.songUris.get(i);				
			} 
			i++;
		} while (songUri == null | i < userMusic.numSongs);
		
		//Try and play the requested song
		try {
			mPlayer.setDataSource(context, songUri);
			Log.e("MusicPlayer", "Data Source is set");
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
		
	public String getCurrentSongTitle() {
		return userMusic.songTitles.get(currentSongIndex);
	}
	
}