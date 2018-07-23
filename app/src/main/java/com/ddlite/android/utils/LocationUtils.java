package com.ddlite.android.utils;

import android.location.Location;

/**
 * Encapsulates all the location logic like taking user permission to get location, caching location,
 * listening to location changes etc
 */
public class LocationUtils {

    public static Location getLastKnownLocation() {
        Location location = new Location(/*LocationManager.GPS_PROVIDER*/"");
        location.setLatitude(37.422740);
        location.setLongitude(-122.139956);
        return location;
    }
}
