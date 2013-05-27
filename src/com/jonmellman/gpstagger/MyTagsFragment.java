package com.jonmellman.gpstagger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyTagsFragment extends Fragment {
	public static final byte FRAGMENT_ID = 1;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.my_tags_fragment, container, false);
    }
	

}
