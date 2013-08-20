package com.jonmellman.gpstagger.activities;

import com.jonmellman.gpstagger.DatabaseHandler;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * ToggleClear is an activity designed to be launched by the NFC Task
 * Launcher app upon NFC tag recognition. It clears the location stored
 * by the user when the ToggleCreate activity was launched.
 */

public class ToggleClear extends Activity {
	public static final String LOGTAG = "ToggleClear Activity";
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGTAG, "onCreate() called");
        
        DatabaseHandler dbHandler = DatabaseHandler.getInstance(this);
        SharedPreferences prefs = this.getSharedPreferences("com.jonmellman.gpstagger.toggleID", Context.MODE_PRIVATE);
        
        int tagID = prefs.getInt(ToggleCreate.toggleKey, -1);
        
        if (tagID == -1) {
        	Toast.makeText(this, "Error clearing location data", Toast.LENGTH_LONG).show();
        } else {
            dbHandler.deleteGpsTag(tagID);
        	Toast.makeText(this, "Location data cleared!", Toast.LENGTH_LONG).show();
        }
        
        finish();
    }
}