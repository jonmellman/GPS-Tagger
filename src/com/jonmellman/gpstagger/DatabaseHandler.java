package com.jonmellman.gpstagger;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

/**
 * Created by jonmellman on 5/26/13.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String LOGTAG = "DatabaseHandler";

    private static DatabaseHandler dbHandlerInstance = null;

    private static final int DATABASE_VERSION = 2;
    
    public static final String DATABASE_NAME = "gpsTagsManager";
    public static final String TABLE_GPSTAGS = "gpsTags";
    public static final String KEY_ID = "_id";
    public static final String KEY_LABEL = "label";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";


    public DatabaseHandler(Context context) {    	
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public String getTableName() {
    	return TABLE_GPSTAGS;
    }

    //used to ensure only one instance of the database handler exists at a time
    public static DatabaseHandler getInstance(Context context) {
        if (dbHandlerInstance == null) {
            dbHandlerInstance = new DatabaseHandler(context.getApplicationContext());
        }

        return dbHandlerInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_GPSTAGS_TABLE = "CREATE TABLE " + TABLE_GPSTAGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LABEL + " TEXT,"
                + KEY_LATITUDE + " REAL," + KEY_LONGITUDE + " REAL" + ")";
        sqLiteDatabase.execSQL(CREATE_GPSTAGS_TABLE);

        Log.i(LOGTAG, "Initialized database: " + sqLiteDatabase.toString());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN) //needed for db.close()
    public int addGpsTag(GpsTag gpsTag) {
        Log.i(LOGTAG, "Inserting record: " + gpsTag.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LABEL, gpsTag.get_label());
        values.put(KEY_LATITUDE, gpsTag.get_latitude());
        values.put(KEY_LONGITUDE, gpsTag.get_longitude());

        long tagID = db.insert(TABLE_GPSTAGS, null, values);
        db.close();

        return (int) tagID;
    }

    public GpsTag getGpsTag(int id) {
        Log.i(LOGTAG, "Fetching record with ID " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        //SELECT * FROM TABLE_GPSTAGS WHERE KEY_ID = id
        Cursor cursor = db.query(TABLE_GPSTAGS, new String[] {KEY_ID, KEY_LABEL, KEY_LATITUDE, KEY_LONGITUDE},
                KEY_ID + "= ?", new String[] {String.valueOf(id) }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        //new GpsTag(ID, Label, Latitude, Longitude)
        GpsTag gpsTag = new GpsTag(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));

        Log.i(LOGTAG, "Fetched record " + gpsTag.toString());
        cursor.close();
        db.close();

        return gpsTag;
    }
    
    public void updateGpsTagLabel(int id, String newLabel) {
        Log.i(LOGTAG, "Updating tag: " + this.getGpsTag(id).toString());
        SQLiteDatabase db = this.getWritableDatabase();

        GpsTag oldTag = this.getGpsTag(id);
        
        ContentValues values = new ContentValues();
        values.put(KEY_LABEL, newLabel);
        values.put(KEY_LATITUDE, oldTag.get_latitude());
        values.put(KEY_LONGITUDE, oldTag.get_longitude());
        
        db.update(TABLE_GPSTAGS, values, KEY_ID + "= ?", new String[] {String.valueOf(id)});
        db.close();
        Log.i(LOGTAG, "Updated tag to: " + this.getGpsTag(id).toString());
    }

//    public void updateGpsTag(GpsTag gpsTag) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_LABEL, gpsTag.get_label());
//        values.put(KEY_LATITUDE, gpsTag.get_latitude());
//        values.put(KEY_LONGITUDE, gpsTag.get_longitude());
//
//        db.update(TABLE_GPSTAGS, values, KEY_ID + "= ?", new String[] {String.valueOf(gpsTag.get_id())});
//        db.close();
//    }
    
    public void deleteGpsTag(int id) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_GPSTAGS, KEY_ID + " = ?", new String[] { String.valueOf(id) });
    	db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    	Log.i(LOGTAG, "Database upgrade called");
    	sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GPSTAGS);
        onCreate(sqLiteDatabase);

    }
}
