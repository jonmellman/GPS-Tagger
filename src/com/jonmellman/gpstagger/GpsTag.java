package com.jonmellman.gpstagger;

/**
 * Created by jonmellman on 5/26/13.
 */
public class GpsTag {
    public static final String DEFAULT_LABEL = "Recent Tag";

    int _id;
    double _latitude, _longitude;
    String _label, _created_at;

    public GpsTag() {
        _id = -1;
        _latitude = -1.0;
        _longitude = -1.0;
        _label = DEFAULT_LABEL;
    }

    public GpsTag(int id, String label, double latitude, double longitude, String created_at) {
        _id = id;
        _latitude = latitude;
        _longitude = longitude;
        _label = label;
        _created_at = created_at;
    }

    public double get_latitude() {
        return _latitude;
    }

    public void set_latitude(double _latitude) {
        this._latitude = _latitude;
    }

    public double get_longitude() {
        return _longitude;
    }

    public void set_longitude(double _longitude) {
        this._longitude = _longitude;
    }

    public String get_label() {
        return _label;
    }

    public void set_label(String _label) {
        this._label = _label;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
    
    public String get_created_at() {
        return _created_at;
    }

    public void set_created_at(String _created_at) {
        this._created_at = _created_at;
    }


    @Override
    public String toString() {
        return get_id() + " | " + get_label() + " | " + get_latitude() + " | " + get_longitude() + " | " + get_created_at();
    }

}
