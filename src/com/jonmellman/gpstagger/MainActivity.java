package com.jonmellman.gpstagger;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.ActionBar;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    private final String LOGTAG = "MainActivity";
    private static DatabaseHandler dbHandler;

    LocationManager locationManager;
    MyLocationListener locationListener;

    Button tagButton;

    Location currentLocation;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //set up actionbar to show tabs
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        //add tabs
        actionBar.addTab(actionBar.newTab().setText(R.string.make_tag)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.my_tags)
                .setTabListener(this));

        //initialize database handler
        dbHandler = DatabaseHandler.getInstance(this);

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
            Log.i(LOGTAG, location.getProvider() + ": updating location..");
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



	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		
		Fragment fragment = new Fragment();
		switch (tab.getPosition()) {
		case MakeTagFragment.FRAGMENT_ID:
			fragment = new MakeTagFragment();
			break;
		case MyTagsFragment.FRAGMENT_ID:
			fragment = new MyTagsFragment();
			break;
		}
		
		//replace FrameLayout container in the layout with the correct fragment
		getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
}
