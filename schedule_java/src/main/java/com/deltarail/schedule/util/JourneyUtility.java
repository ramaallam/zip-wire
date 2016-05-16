package com.deltarail.schedule.util;

import com.deltarail.schedule.beans.Header;
import com.deltarail.schedule.beans.Journey;
import com.deltarail.schedule.beans.Location;
import org.apache.spark.broadcast.Broadcast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by RAllam on 09/05/2016.
 */
public class JourneyUtility implements Serializable {
    public static List<Journey> buildJourneys(Header header, List<String> fromTipLoc, List<String> toTipLoc, List<String> departureTimes, List<String> arrivalTimes, Broadcast<Map<String, Location>> locationsBroadcast) {
        List<Journey> journeys = new ArrayList<>();
        try {
            int headCounter = 0;
            for (String compositeId : header.getCompositeIDs()) {
                for (int outer = 0; outer < fromTipLoc.size(); outer++) {
                    for (int inner = 0; inner < toTipLoc.size(); inner++) {
                        if (inner < outer) continue;
                        Journey j = new Journey();
                        j.setCompositeID(compositeId);
                        j.setTrainID(header.getTrainID());
                        j.setDateRunning(header.getDatesRunning().get(headCounter));
                        j.setOrigin(header.getOrigin());
                        j.setDestination(header.getDestination());
                        j.setToc(header.getToc());
                        j.setPublishedOn(header.getPublishedOn());
                        String from = fromTipLoc.get(outer);
                        String to = toTipLoc.get(inner);
                        j.setDepartureTime(departureTimes.get(outer));
                        j.setArrivalTime(arrivalTimes.get(inner));
                        //TIPLOCS
                        j.setTipLocationFrom(from);
                        j.setTipLocationTo(to);
                        //Stanox
                        Location fromLocation = locationsBroadcast.value().get(from);
                        Location toLocation = locationsBroadcast.value().get(to);
                        j.setFromStanox(fromLocation.getStanox());
                        j.setToStanox(toLocation.getStanox());
                        //Stations
                        j.setFromStation(fromLocation.getName());
                        j.setToStation(toLocation.getName());
                        journeys.add(j);
                    }
                }
                headCounter++;
            }
        } catch (Exception e) {
            //Ignore the exception and hence the journeys related to the schedule (that BS record)
        }
        return journeys;
    }
}
