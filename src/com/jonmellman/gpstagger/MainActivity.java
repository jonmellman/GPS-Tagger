package com.jonmellman.gpstagger;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final String LOGTAG = "MainActivity";
    private static DatabaseHandler dbHandler;

    LocationManager locationManager;
    MyLocationListener locationListener;

    Button tagButton;

    Location currentLocation;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOGTAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize database handler
        dbHandler = DatabaseHandler.getInstance(this);

        //initialize components
        tagButton = (Button) findViewById(R.id.tag_button);

        //begin receiving location data for when user creates a tag
        registerForLocationData();
    }

    private void registerForLocationData() {

        //initialize criteria for provider data
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        //initialize a LocationManager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //determine which provider to use for initial location grab
//        String provider = locationManager.getBestProvider(criteria, true);
        String provider = locationManager.GPS_PROVIDER;

        if (provider == null) {
            Log.i(LOGTAG, "Provider is null.");
        } else {
            Log.i(LOGTAG, "Got provider: " + provider);
        }

        //get last known location
        Location location = locationManager.getLastKnownLocation(provider);
        Log.i(LOGTAG, "Getting last known location..");
        setCurrentLocation(location);

        LocationListener locationListener = new MyLocationListener();

        //register for future updates (updates are sent to the onLocationChanged() event
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    //not sure how else to access db records from other classes?
    public static DatabaseHandler getDbHandler() {
        return dbHandler;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	
    	if (locationManager != null && locationListener != null) {
        	Log.i(LOGTAG, "Activity paused; removing location updates");
    		locationManager.removeUpdates(locationListener);
    	}
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	if (locationManager != null && locationListener != null) {
    		Log.i(LOGTAG, "Activity resumed; beginning location updates");
    		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    	}
    	
    }

    /* Called when user taps the "Tag" button
     */
    public void createTag(View view) {
        Log.i(LOGTAG, "Attempting to create GpsTag..");
        if (currentLocation != null) {
            //make tag with currentLocation
            GpsTag newTag = new GpsTag();
            newTag.set_latitude(getCurrentLocation().getLatitude());
            newTag.set_longitude(getCurrentLocation().getLongitude());
            int tagID = dbHandler.addGpsTag(newTag); //get primary key of newly added tag

            Log.i(LOGTAG, "Created tag with id " + tagID + ". Launching ViewTagActivity..");

            //launch view tag activity
            Intent intent = new Intent(this, ViewTagActivity.class);
            intent.putExtra(DatabaseHandler.KEY_ID, tagID);
            startActivity(intent);
        } else {
            Log.i(LOGTAG, "Location is null, cannot create tag");
        }
    }

    public void setCurrentLocation(Location location) {
        double lat, lon;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.i(LOGTAG, "Got location: " + lat + ", " + lon);
        } catch (NullPointerException e) {
            Log.i(LOGTAG, "Last location update is null");
        }

        currentLocation = location;
    }



    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.i(LOGTAG, "Updating location..");
            setCurrentLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.i(LOGTAG, "Provider status changed: " + s);
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
