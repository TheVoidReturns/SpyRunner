package com.scatterlogic.games.spyrunner;
import android.content.Context;
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
 * Media Player Wrapper by Daniel Hawkes. Added to by Chris Peel
 */

public class MusicPlayer {
 
	private static String tag = "MusicPlayer";
	private MediaPlayer mPlayer;
	private State currentState;
	private MusicPlayer musicPlayer;
	UserMusic userMusic;
	Context context;
 
	public MusicPlayer(Context context) {
		musicPlayer = this;
		mPlayer = new MediaPlayer();
		userMusic = new UserMusic(context);
		currentState = State.IDLE;
		mPlayer.setOnPreparedListener(mOnPreparedListener);
		mPlayer.setOnCompletionListener(mOnCompletionListener);
		mPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
		mPlayer.setOnErrorListener(mOnErrorListener);
		mPlayer.setOnInfoListener(mOnInfoListener);
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		Log.d("MusicPlayer", "MusicPlayer Instantiated");
	}
 
	/* METHOD WRAPPING FOR STATE CHANGES */
	public static enum State {
		IDLE, ERROR, INITIALIZED, PREPARING, PREPARED, STARTED, STOPPED, PLAYBACK_COMPLETE, PAUSED;
	}
 
	public void setDataSource(String path) {
		if (currentState == State.IDLE) {
			try {
				mPlayer.setDataSource(path);
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
		Log.d(tag, "prepareAsync() New2");
		//if (EnumSet.of(State.INITIALIZED, State.STOPPED).contains(currentState)) {
			//mPlayer.prepareAsync();
			currentState = State.PREPARING;
		//} else throw new RuntimeException();
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
 
	public void start() {
		Log.d(tag, "start()");
		if (EnumSet.of(State.PREPARED, State.STARTED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
			mPlayer.start();
			currentState = State.STARTED;
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
			mPlayer.start();
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
	
}