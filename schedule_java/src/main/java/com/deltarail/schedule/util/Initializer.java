package com.deltarail.schedule.util;

import com.deltarail.schedule.beans.Location;
import com.deltarail.schedule.config.Configuration;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by RAllam on 09/05/2016.
 */
public class Initializer implements Serializable{
    public static void initalize(String locationFile, String atocFile){
        initializeLocations(locationFile);
        initializeAtocs(atocFile);
    }
    private static void initializeLocations(String locationFile){
        JavaSparkContext sc = Configuration.getSparkContext();
        Map<String, Location> locations = sc.textFile(locationFile).map(line -> {
            String[] items = line.split(",");
            String tiploc = items[0];
            String name = items.length > 5 ? items[4] : "";
            String stanox = items.length > 9 ? items[8] : "     ";
            return new Location(tiploc, name, stanox);
        }).mapToPair(location -> new Tuple2<String, Location>(location.getTIPLOC(), location)).collectAsMap();
        LocationsCache.setLocations(sc.broadcast(locations));
    }
    private static void initializeAtocs(String atocFile){
        JavaSparkContext sc = Configuration.getSparkContext();
        Map<String, String> atocs = sc.textFile(atocFile).mapToPair(line -> {
            String[] items = line.split(",");
            return new Tuple2<String, String>(items[1], items[0]);
        }).collectAsMap();
        AtocCache.setAtocs(sc.broadcast(atocs));
    }
}
