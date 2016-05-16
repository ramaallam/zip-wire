package com.deltarail.schedule.beans;

import java.io.Serializable;

/**
 * Created by RAllam on 08/05/2016.
 */
public class Location implements Serializable{
    private String TIPLOC;
    private String name;
    private String stanox;

    public Location(String TIPLOC, String name, String stanox) {
        this.TIPLOC = TIPLOC;
        this.name = name;
        this.stanox = stanox;
    }

    public String getTIPLOC() {
        return TIPLOC;
    }

    public void setTIPLOC(String TIPLOC) {
        this.TIPLOC = TIPLOC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStanox() {
        return stanox;
    }

    public void setStanox(String stanox) {
        this.stanox = stanox;
    }
}
