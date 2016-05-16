package com.deltarail.schedule.beans;

import java.io.Serializable;

/**
 * Created by RAllam on 08/05/2016.
 */
public class Atoc implements Serializable{
    private String atocCode;
    private String description;

    public Atoc(String atocCode, String description) {
        this.atocCode = atocCode;
        this.description = description;
    }

    public String getAtocCode() {
        return atocCode;
    }

    public void setAtocCode(String atocCode) {
        this.atocCode = atocCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
