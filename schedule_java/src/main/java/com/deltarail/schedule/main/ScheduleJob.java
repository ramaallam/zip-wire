package com.deltarail.schedule.main;

import com.deltarail.schedule.config.Configuration;
import com.deltarail.schedule.util.Initializer;

import java.io.Serializable;

/**
 * Created by RAllam on 05/05/2016.
 */
public class ScheduleJob implements Serializable {

    public static void main(String[] args) {
        setUp();
        initialize(args[1], args[2]);
        String inputLocation = args[0];
        String outputLocation = args[3];
        SchedulesProcessor processor = new SchedulesProcessor(outputLocation);
        processor.processSchedules(inputLocation, args[1], args[2]);
    }

    private static void setUp() {
        Configuration.setUp();
    }

    private static void initialize(String locationFile, String atocFile) {
        Initializer.initalize(locationFile, atocFile);
    }


}
