package com.jonmellman.gpstagger;

import android.content.Context;

/**
 * GpsTag objects store a latitute, longitude, label, and created_at field.
 * They can then be added to the database and given a primary key id.
 */
public class GpsTag {

    int id;
    double latitude, longitude;
    String label, created_at;

    public GpsTag(Context context) {
        this.id = -1;
        this.latitude = -1.0;
        this.longitude = -1.0;
        this.label = context.getString(R.string.default_label);
    }

    public GpsTag(int id, String label, double latitude, double longitude, String created_at) {
    	this.id = id;
    	this.latitude = latitude;
    	this.longitude = longitude;
    	this.label = label;
    	this.created_at = created_at;
    }

    public double get_latitude() {
        return this.latitude;
    }

    public void set_latitude(double latitude) {
    	this.latitude = latitude;
    }

    public double get_longitude() {
        return this.longitude;
    }

    public void set_longitude(double longitude) {
        this.longitude = longitude;
    }

    public String get_label() {
        return this.label;
    }

    public void set_label(String label) {
        this.label = label;
    }

    public int get_id() {
        return id;
    }

    public void set_id(int id) {
        this.id = id;
    }
    
    public String get_created_at() {
        return created_at;
    }

    public void set_created_at(String created_at) {
        this.created_at = created_at;
    }


    @Override
    public String toString() {
        return get_id() + " | " + get_label() + " | " + get_latitude() + " | " + get_longitude() + " | " + get_created_at();
    }

}
