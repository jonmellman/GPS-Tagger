package com.jonmellman.gpstagger.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jonmellman.gpstagger.DatabaseHandler;
import com.jonmellman.gpstagger.GpsTag;
import com.jonmellman.gpstagger.R;
import com.jonmellman.gpstagger.R.id;
import com.jonmellman.gpstagger.R.layout;

/**
 * Activity for viewing a single tag from database ID passed in the intent.
 */

public class ViewTagActivity extends Activity {
	private static final String LOGTAG = "ViewTagActivity";

    private EditText labelText;
    private GoogleMap map;
    private int tagID;
    private GpsTag gpsTag;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) //necessary for MapFragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_tag_activity);

        //grab the ID of the GpsTag to load
        tagID = getIntent().getIntExtra(DatabaseHandler.KEY_ID, -1);

        //load the GpsTag
        Log.i(LOGTAG, "Loading record with ID: " + tagID);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance(this);
        gpsTag = dbHandler.getGpsTag(tagID);
        Log.i(LOGTAG, "Loaded GpsTag " + gpsTag.toString());
        
        //initialize the label with the correct text
        labelText = (EditText) findViewById(R.id.labelText);
        labelText.setText(gpsTag.get_label());

        //store latitude and longitude in LatLng object to be passed to the map's camera
        LatLng latLng = new LatLng(gpsTag.get_latitude(), gpsTag.get_longitude());
        
        //initialize the map
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
                // The Map is verified. Update camera position and zoom, and add a marker.
            	map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            	map.addMarker(new MarkerOptions()
            			.position(latLng));
            }
        }
    }
    
    /*
     * On confirm button clicked to save tag label.
     */
    public void onConfirm(View view) {
    	DatabaseHandler dbHandler = DatabaseHandler.getInstance(this);
    	dbHandler.updateGpsTagLabel(tagID, labelText.getText().toString());
    	Toast toast =  Toast.makeText(this, R.string.updated_tag, Toast.LENGTH_SHORT);
    	toast.show();
    }
    
    /*
     * Launch map in Google Maps (or another application that registers the intent)
     */
    public void openInGM(View view) {

      String label = gpsTag.get_label();
      double latitude = gpsTag.get_latitude();
      double longitude = gpsTag.get_longitude();
      
      String uriBegin = "geo:" + latitude + "," + longitude;
      String query = latitude + "," + longitude + "(" + label + ")";
      String encodedQuery = Uri.encode(query);
      String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
      Uri uri = Uri.parse(uriString);
      Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
      startActivity(intent);
    }
}