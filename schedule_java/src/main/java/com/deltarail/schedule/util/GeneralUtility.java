package com.deltarail.schedule.util;

/**
 * Created by RAllam on 13/05/2016.
 */
public class GeneralUtility {
    //Platform independent line separator.
    public static String lineSeparator(int numberOfTimes) {
        StringBuilder sb = new StringBuilder("");
        for(int i=0; i< numberOfTimes; i++){
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
