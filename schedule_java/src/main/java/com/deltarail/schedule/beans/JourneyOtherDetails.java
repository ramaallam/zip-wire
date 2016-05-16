package com.deltarail.schedule.beans;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by RAllam on 08/05/2016.
 */
public class JourneyOtherDetails implements Serializable {
    private LocalDate scheduledArrivalTime;
    private LocalDate scheduledDepartTime;


    public LocalDate getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setScheduledArrivalTime(LocalDate scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public LocalDate getScheduledDepartTime() {
        return scheduledDepartTime;
    }

    public void setScheduledDepartTime(LocalDate scheduledDepartTime) {
        this.scheduledDepartTime = scheduledDepartTime;
    }

}
