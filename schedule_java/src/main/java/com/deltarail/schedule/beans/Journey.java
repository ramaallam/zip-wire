package com.deltarail.schedule.beans;

import org.apache.hadoop.dynamodb.DynamoDBItemWritable;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RAllam on 04/05/2016.
 */
/*
    Composite ID	train ID & Date
    Train ID	train id
    Date	YYYYMMDD
    From	STANOX
    To 	STANOX
    LongFrom	Long Name
    LongTo	Long Name
    TIPLOCTo	TIPLOC
    TIPLOCFrom	TIPLOC
    Depart	Epoch timestamp
    Arrive	Epoch timestamp
    Origin	Long Name
    Destination	Long Name
    TOC	Long Name
    Published on	YYYYMMDDHHmmss
 */
public class Journey extends DynamoDBItemWritable implements Serializable {
    private String trainID; //BS
    private String dateRunning;
    private String compositeID;
    private String origin; //LO
    private String destination; //LT
    private String toc;
    private LocalDate publishedOn;

    private String fromStanox; //LO,LI - from the Location csv file
    private String toStanox; //LI,LT - from the location csv file
    private String fromStation;
    private String toStation;
    private String tipLocationFrom; // from the enriched file
    private String tipLocationTo; // from the enriched file

    private String departureTime; // from the enriched file
    private String arrivalTime; // from the enriched file

    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public String getCompositeID() {
        return compositeID;
    }

    public void setCompositeID(String compositeID) {
        this.compositeID = compositeID;
    }

    public String getDateRunning() {
        return dateRunning;
    }

    public void setDateRunning(String dateRunning) {
        this.dateRunning = dateRunning;
    }

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

    public String getFromStanox() {
        return fromStanox;
    }

    public void setFromStanox(String fromStanox) {
        this.fromStanox = fromStanox;
    }

    public String getToStanox() {
        return toStanox;
    }

    public void setToStanox(String toStanox) {
        this.toStanox = toStanox;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getTipLocationFrom() {
        return tipLocationFrom;
    }

    public void setTipLocationFrom(String tipLocationFrom) {
        this.tipLocationFrom = tipLocationFrom;
    }

    public String getTipLocationTo() {
        return tipLocationTo;
    }

    public void setTipLocationTo(String tipLocationTo) {
        this.tipLocationTo = tipLocationTo;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    private String TABCHAR = "\t";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(compositeID).append(TABCHAR).append(trainID).append(TABCHAR).append(dateRunning).append(TABCHAR).append(departureTime).append(TABCHAR)
                .append(arrivalTime).append(TABCHAR).append(fromStanox).append(TABCHAR).append(toStanox)
                .append(TABCHAR).append(fromStation).append(TABCHAR).append(toStation).append(TABCHAR).append(tipLocationFrom).append(TABCHAR).append(tipLocationTo)
                .append(TABCHAR).append(origin).append(TABCHAR).append(destination).append(TABCHAR).append(toc).append(TABCHAR).append(publishedOn);
        return sb.toString();
    }

    public static Journey parse(String str) {
        String[] arr = str.split("\t");
        Journey j = new Journey();
        j.setCompositeID(arr[0]);
        j.setTrainID(arr[1]);
        j.setDateRunning(arr[2]);
        j.setFromStanox(arr[3]);
        j.setToStanox(arr[4]);
        j.setFromStation(arr[5]);
        j.setToStation(arr[6]);
        j.setTipLocationFrom(arr[7]);
        j.setTipLocationTo(arr[8]);
        j.setOrigin(arr[9]);
        j.setDestination(arr[10]);
        j.setToc(arr[11]);
        j.setDepartureTime(arr[12]);
        j.setArrivalTime(arr[13]);
//        j.setPublishedOn(arr[14]);
        return j;
    }
}
