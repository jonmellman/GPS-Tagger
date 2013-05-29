/**
 *  MainActivity.java holds the core layout for GPS Tagger.
 * It holds the tab navigator and loads the MakeTagFragment and MyTagsFragment.
 */


package com.jonmellman.gpstagger.activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.jonmellman.gpstagger.R;
import com.jonmellman.gpstagger.fragments.MakeTagFragment;
import com.jonmellman.gpstagger.fragments.MyTagsFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    private final String LOGTAG = "MainActivity";
    private final String FRAGMENT_TAG = "MakeTagFragment"; //key used to grab correct fragment

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
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}

	/* Replace FrameLayout container in the layout with the correct fragment,
	 * according to which tab is selected.
	 */
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.i(LOGTAG, "Tab selected");
		Fragment fragment = new Fragment();
		switch (tab.getPosition()) {
		case MakeTagFragment.FRAGMENT_ID:
			fragment = new MakeTagFragment();
			break;
		case MyTagsFragment.FRAGMENT_ID:
			fragment = new MyTagsFragment();
			break;
		}

		getFragmentManager().beginTransaction().replace(R.id.container, fragment, FRAGMENT_TAG).commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
	
    /* Called when user taps the "Tag" button - call MakeTagFragment's createTag method.
     */
    public void onClick(View view) {
    	MakeTagFragment mtFragment = (MakeTagFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    	mtFragment.createTag(view);
    }
}
