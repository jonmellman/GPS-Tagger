package com.jonmellman.gpstagger;

import android.support.v4.app.FragmentActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MakeTagFragment extends Fragment {
	public static final byte FRAGMENT_ID = 0;
	private static final String LOGTAG = "MakeTagFragment";
	
	Button tagButton; //set enabled in onResume of mainactivity?
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.make_tag_fragment, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	tagButton = (Button) getView().findViewById(R.id.tag_button);
    }
    
    public void gotLocation() {
    	if (tagButton != null) {
    		tagButton.setEnabled(true);
    	} else {
    		Log.i(LOGTAG, "Tried to enable null tag button.");
    	}
    }

}
