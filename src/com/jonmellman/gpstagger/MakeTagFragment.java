package com.jonmellman.gpstagger;


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

public class MakeTagFragment extends Fragment {
	public static final byte FRAGMENT_ID = 0;
	private static final String LOGTAG = "MakeTagFragment";
	
	Button tagButton; //set enabled in onResume of mainactivity?

    LocationManager locationManager;
    MyLocationListener locationListener;
    
    Location currentLocation = null;
	
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
        tagButton.setEnabled(false);
        tagButton.setText(R.string.waiting_for_data);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterForLocationUpdates();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		registerForLocationUpdates();
	}
	
    private void registerForLocationUpdates() {
        //initialize a LocationManager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        //register for future updates (updates are sent to the onLocationChanged() event
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    
    private void unregisterForLocationUpdates() {
        if (locationManager != null && locationListener != null) {
        	locationManager.removeUpdates(locationListener);
        }
    }
    
    public void updateLocation(Location location) {
        double lat, lon;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.i(LOGTAG, "Got location: " + lat + ", " + lon);
        } catch (NullPointerException e) {
            Log.i(LOGTAG, "Last location update is null");
        }

        if (tagButton != null && !tagButton.isEnabled()) { //first location data receieved!
	    	tagButton.setEnabled(true);
	    	tagButton.setText(R.string.tag);  
        }
        currentLocation = location;
    }
    
    public Location getCurrentLocation() {
        return currentLocation;
    }
    
    public void createTag(View view) {    	
        Log.i(LOGTAG, "Attempting to create GpsTag..");
        if (currentLocation != null) {
            //make tag with currentLocation
            GpsTag newTag = new GpsTag();
            newTag.set_latitude(getCurrentLocation().getLatitude());
            newTag.set_longitude(getCurrentLocation().getLongitude());
            int tagID = DatabaseHandler.getInstance(getActivity()).addGpsTag(newTag); //get primary key of newly added tag

            Log.i(LOGTAG, "Created tag with id " + tagID + ". Launching ViewTagActivity..");

            //launch view tag activity
            Intent intent = new Intent(getActivity(), ViewTagActivity.class);
            intent.putExtra(DatabaseHandler.KEY_ID, tagID);
        	Toast.makeText(getActivity(), "Tag created!", Toast.LENGTH_LONG).show();
            startActivity(intent);
        } else {
        	Toast.makeText(getActivity(), "Could not determine location", Toast.LENGTH_LONG).show();
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
