package com.jonmellman.gpstagger.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jonmellman.gpstagger.DatabaseHandler;
import com.jonmellman.gpstagger.GpsTag;

/**
 * ToggleCreate is an activity designed to be launched by the NFC Task
 * Launcher app upon NFC tag recognition. It effectively stores the user's
 * current location when an NFC tag is tapped.
 */

public class ToggleCreate extends Activity {
	public static final String LOGTAG = "ToggleCreate Activity";
	public static final String toggleKey = "com.jonmellman.gpstagger.toggleIDKey";
	
	//declare necessary location components
    private LocationManager locationManager;
    private MyLocationListener locationListener;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LOGTAG, "onCreate() called");
        
        registerForLocationUpdates();

        finish();
    }
	
    private void registerForLocationUpdates() {
        //initialize a LocationManager and LocationListener
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        //register for future updates (updates are sent to the LocationListener's onLocationChanged() event)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    
    private void unregisterForLocationUpdates() {
        if (locationManager != null && locationListener != null) {
        	locationManager.removeUpdates(locationListener);
        }
    }
    
    //called from LocationListener's onLocationChanged event
    public void gotLocation(Location location) {
        double lat, lon;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.i(LOGTAG, "Got location: " + lat + ", " + lon);

        	unregisterForLocationUpdates();
        	
            GpsTag newTag = new GpsTag(this);
            newTag.set_latitude(lat);
            newTag.set_longitude(lon);
            newTag.set_label("Car");
        	
        	int toggleID = DatabaseHandler.getInstance(this).addGpsTag(newTag);
        	SharedPreferences prefs = this.getSharedPreferences("com.jonmellman.gpstagger.toggleID", Context.MODE_PRIVATE);
        	prefs.edit().putInt(toggleKey, toggleID).commit();
        	
        	Toast.makeText(this, "Location data stored!", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            Log.i(LOGTAG, "Last location update is null");
        }
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            gotLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    }
}