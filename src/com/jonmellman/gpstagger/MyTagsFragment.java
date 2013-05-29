package com.jonmellman.gpstagger;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MyTagsFragment extends ListFragment {
	private static final String LOGTAG = "My Tags Fragment";
	public static final byte FRAGMENT_ID = 1;
	
	ListView listView;
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	DatabaseHandler dbHandler = DatabaseHandler.getInstance(getActivity());
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT * FROM " +  dbHandler.getTableName(), null);
     //   this.getActivity().startManagingCursor(cursor);
        String[] fromColumns = new String[] {dbHandler.KEY_LABEL, dbHandler.KEY_CREATED_AT};
        int[] toControlIDs = new int[] {android.R.id.text1, android.R.id.text2}; //text field in android's default simple_list_item_1
        ListAdapter adapter = new SimpleCursorAdapter(this.getActivity(), android.R.layout.simple_list_item_2, cursor, fromColumns, toControlIDs, 0);
        setListAdapter(adapter);
    	
    	//it's safe to search for view objects by ID in onActivityCreated()
    	listView = (ListView) getView().findViewById(android.R.id.list);
    	this.registerForContextMenu(listView);
    	
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			    Log.i(LOGTAG, "Clicked item: " + id);
			    
			    //grab database ID of tag clicked, and pass it to ViewTagActivity
	            Intent intent = new Intent(view.getContext(), ViewTagActivity.class);
	            intent.putExtra(DatabaseHandler.KEY_ID, (int) id);
	            startActivity(intent);
			}
		});
 
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, R.string.delete);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    	case 0: 
    		DatabaseHandler.getInstance(getActivity()).deleteGpsTag((int) info.id);
    		onActivityCreated(null);
    		break;
    	}
    	return super.onContextItemSelected(item);
    }
    

	

}
