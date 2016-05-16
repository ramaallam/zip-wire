package com.deltarail.schedule.util;

import com.deltarail.schedule.beans.Location;
import org.apache.spark.broadcast.Broadcast;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by RAllam on 09/05/2016.
 */
public class LocationsCache implements Serializable{
    private static Broadcast<Map<String, Location>> broadCastLocations;
    public static void setLocations(Broadcast<Map<String, Location>> loc){
        broadCastLocations = loc;
    }
    public static String getStationName(String tiploc) {
        Map<String, Location> locations = broadCastLocations.value();
        //TODO what if it is null
        Location loc =  locations.get(tiploc);
        if(loc != null){
            return loc.getName();
        } else {
            return  null;
        }
    }
    public static String getStanox(String tiploc) {
        Map<String, Location> locations = broadCastLocations.value();
        //TODO what if it is null
        return locations.get(tiploc).getStanox();
    }
}
