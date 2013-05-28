package com.jonmellman.gpstagger;

import android.app.Fragment;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class MyTagsFragment extends ListFragment {
	private static final String LOGTAG = "My Tags Fragment";
	public static final byte FRAGMENT_ID = 1;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      DatabaseHandler dbHandler = MainActivity.getDbHandler();
      
      
      Cursor cursor = dbHandler.getReadableDatabase().rawQuery("SELECT * FROM " +  dbHandler.getTableName(), null);
      cursor.moveToFirst();
      while (cursor.moveToNext()) {
    	  Log.i(LOGTAG, cursor.getString(0) + " has label " + cursor.getString(1));
      }
      
      String[] fromColumns = new String[] {dbHandler.KEY_ID, dbHandler.KEY_LABEL};
      int[] toControlIDs = new int[] {android.R.id.text1, android.R.id.text2}; //text field in android's default simple_list_item_1
      ListAdapter adapter = new SimpleCursorAdapter(this.getActivity(), android.R.layout.simple_list_item_2, cursor, fromColumns, toControlIDs, 0);
      setListAdapter(adapter);
      
    }
	

}
