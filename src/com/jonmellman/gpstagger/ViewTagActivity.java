package com.jonmellman.gpstagger;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jonmellman on 5/26/13.
 */
public class ViewTagActivity extends Activity {
	private static final String LOGTAG = "ViewTagActivity";

    private EditText labelText;
    private GoogleMap map;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) //necessary for MapFragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_tag_activity);

        //grab the ID of the GpsTag to load
        Intent intent = getIntent();
        int tagID = intent.getIntExtra(DatabaseHandler.KEY_ID, -1);

        //load the GpsTag
        Log.i(LOGTAG, "Loading record with ID: " + tagID);
        DatabaseHandler dbHandler = MainActivity.getDbHandler();
        GpsTag gpsTag = dbHandler.getGpsTag(tagID);
        Log.i(LOGTAG, "Loaded GpsTag " + gpsTag.toString());

        labelText = (EditText) findViewById(R.id.labelText);
        labelText.setText(gpsTag.get_label());

        //store latitude and longitude in LatLng object to be passed to the map's camera
        LatLng latLng = new LatLng(gpsTag.get_latitude(), gpsTag.get_longitude());
        
        setUpMapIfNeeded(latLng);
        

    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setUpMapIfNeeded(LatLng latLng) {

        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
        	Log.i(LOGTAG, "Setting up map..");
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                                .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                // The Map is verified. It is now safe to manipulate the map.
            	map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            	map.addMarker(new MarkerOptions()
            			.position(latLng));

            }
        }
    }
}