package com.scatterlogic.games.spyrunner;

import java.util.ArrayList;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.content.ContentUris;
import android.content.Context;

public class UserMusic implements Runnable {
	Context context;
	ContentResolver contentResolver;
	ArrayList<String> songTitles  = new  ArrayList<String>();
	ArrayList<Long> songIds  = new  ArrayList<Long>();
    ArrayList<Uri> songUris = new ArrayList<Uri>();
    int numSongs;
    boolean cursorError;
	
	public UserMusic(Context mycontext) {
		this.context = mycontext; 
		run();
	}
	
	@Override
	public void run() {
		ContentResolver contentResolver = context.getContentResolver();
    	Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    	Log.d("MusicPlayer" , "uri");
    	Cursor cursor = null;
    	
    	try{
    		cursor = contentResolver.query(uri, null, null, null, null);
    		cursorError = false;
    	}
    	finally{
    		cursorError = true;
    	}
    	Log.d("MusicPlayer" , "cursor");
    	
    	if (cursor == null) {
    	    // query failed, handle error.
    	} else if (!cursor.moveToFirst()) {
    	    // no media on the device
    	} else {
    	    int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
    	    int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
    	    do {
    	       Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(idColumn));
    	       songIds.add(cursor.getLong(idColumn));
    	       songUris.add(contentUri);
    	       songTitles.add(cursor.getString(titleColumn));
    	    } while (cursor.moveToNext());
    	    numSongs = songUris.size();
    	    //Silly logs!  What if I don't have 201 songs?
    	    //Log.d("MusicPlayer", songIds.get(201).toString());
    	    //Log.d("MusicPlayer", songTitles.get(201));
    	}
		
	}
		
}
