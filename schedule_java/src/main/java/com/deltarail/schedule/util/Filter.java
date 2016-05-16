package com.deltarail.schedule.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by RAllam on 09/05/2016.
 */
public class Filter implements Serializable{
    public static boolean filterOnRecordIdentity(String line) {
        return line.startsWith("BS") || line.startsWith("BX") || line.startsWith("LO") || line.startsWith("LI") || line.startsWith("LT");
    }

    public static boolean filterOnActivity(String line) {
        return line.startsWith("LI") ? validActivities().contains(getActivity(line)) : true;
    }

    public static boolean isValidActivity(String line) {
        if(line.startsWith("LI")) {
            return validActivities().contains(getActivity(line));
        } else return true;
    }

    private static List<String> validActivities() {
        return Arrays.asList(new String[]{"T", "U", "D"});
    }
    private static String getActivity(String line) {
        String activity = "";
        try {
            //TODO .. 86-98
            activity = line.substring(86, 87).trim();
        } catch (StringIndexOutOfBoundsException e) {
            //Ignore the line.
        }
        return activity;
    }
}
