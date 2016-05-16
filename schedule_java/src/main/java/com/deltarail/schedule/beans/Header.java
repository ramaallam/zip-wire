package com.deltarail.schedule.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RAllam on 08/05/2016.
 */
public class Header implements Serializable{
//    private String compositeID; // BS
    private String trainID; //BS
    List<String> datesRunning = new ArrayList<>();
    List<String> compositeIDs = new ArrayList<>();
    private String origin; //LO
    private String destination; //LT
    private String toc;
    private LocalDate publishedOn;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getToc() {
        return toc;
    }

    public void setToc(String toc) {
        this.toc = toc;
    }

    public LocalDate getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(LocalDate publishedOn) {
        this.publishedOn = publishedOn;
    }

    public List<String> getDatesRunning() {
        return datesRunning;
    }

    public void setDatesRunning(List<String> datesRunning) {
        this.datesRunning = datesRunning;
    }

    public List<String> getCompositeIDs() {
        return compositeIDs;
    }

    public void setCompositeIDs(List<String> compositeIDs) {
        this.compositeIDs = compositeIDs;
    }

    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

}
