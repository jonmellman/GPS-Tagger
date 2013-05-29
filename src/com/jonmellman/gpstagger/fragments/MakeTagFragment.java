package com.jonmellman.gpstagger.fragments;


import com.jonmellman.gpstagger.DatabaseHandler;
import com.jonmellman.gpstagger.GpsTag;
import com.jonmellman.gpstagger.R;
import com.jonmellman.gpstagger.R.id;
import com.jonmellman.gpstagger.R.layout;
import com.jonmellman.gpstagger.R.string;
import com.jonmellman.gpstagger.activities.ViewTagActivity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Fragment containing the large "Tap to Tag!" button. Also hosts location updates.
 */

public class MakeTagFragment extends Fragment {
	public static final byte FRAGMENT_ID = 0;
	private static final String LOGTAG = "MakeTagFragment";
	
	//declare necessary location components
    private LocationManager locationManager;
    private MyLocationListener locationListener;
    private Location currentLocation = null;
	
    //"Tap to tag" button
	private Button tagButton;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.make_tag_fragment, container, false);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        
		//initialize button as disabled
        tagButton = (Button) getView().findViewById(R.id.tag_button);
        tagButton.setText(R.string.waiting_for_data);
        tagButton.setEnabled(false);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		//stop getting location updates when fragment isn't visible
		unregisterForLocationUpdates();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//start getting updates when fragment becomes visible
		registerForLocationUpdates();
	}
	
    private void registerForLocationUpdates() {
        //initialize a LocationManager and LocationListener
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
    public void updateLocation(Location location) {
        double lat, lon;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.i(LOGTAG, "Got location: " + lat + ", " + lon);
        } catch (NullPointerException e) {
            Log.i(LOGTAG, "Last location update is null");
        }

        if (tagButton != null && !tagButton.isEnabled()) {
        	//first location data receieved - enable button
	    	tagButton.setEnabled(true);
	    	tagButton.setText(R.string.tag);  
        }
        currentLocation = location;
    }
    
    public Location getCurrentLocation() {
        return currentLocation;
    }
    
    //called from MainActivity's onClick() method
    public void createTag(View view) {    	
        Log.i(LOGTAG, "Attempting to create GpsTag..");
        if (currentLocation != null) {
            //make tag with currentLocation
            GpsTag newTag = new GpsTag();
            newTag.set_latitude(getCurrentLocation().getLatitude());
            newTag.set_longitude(getCurrentLocation().getLongitude());
            
            //add tag to database and save primary key ID, to be passed to ViewTagActivity
            int tagID = DatabaseHandler.getInstance(getActivity()).addGpsTag(newTag);

            Log.i(LOGTAG, "Created tag with id " + tagID + ". Launching ViewTagActivity..");

            //launch view tag activity
            Intent intent = new Intent(getActivity(), ViewTagActivity.class);
            intent.putExtra(DatabaseHandler.KEY_ID, tagID);
        	Toast.makeText(getActivity(), R.string.created_tag, Toast.LENGTH_LONG).show();
            startActivity(intent);
        } else {
        	//should never fire since button will be disabled until currentLocation isn't null
        	Toast.makeText(getActivity(), R.string.location_indeterminate, Toast.LENGTH_LONG).show();
            Log.i(LOGTAG, "Location is null, cannot create tag");
        }
    }
    
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.i(LOGTAG, location.getProvider() + ": updating location..");
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    }

}
