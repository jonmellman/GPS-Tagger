package com.jonmellman.gpstagger;

import android.support.v4.app.FragmentActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MakeTagFragment extends Fragment {
	public static final byte FRAGMENT_ID = 0;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.make_tag_fragment, container, false);
    }

}
