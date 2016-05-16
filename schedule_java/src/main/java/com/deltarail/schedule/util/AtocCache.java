package com.deltarail.schedule.util;

import com.deltarail.schedule.beans.Location;
import org.apache.spark.broadcast.Broadcast;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by RAllam on 09/05/2016.
 */
public class AtocCache implements Serializable{
    private static Map<String, String> atocs;
    private static Broadcast<Map<String, String>> broadCastAtocs;
    public static void setAtocs(Broadcast<Map<String, String>> atocCodes){
        broadCastAtocs = atocCodes;
    }
    public static String getAtocLongName(String atocCode){
        Map<String, String> atocs = broadCastAtocs.value();
        //TODO what if it is null
        return atocs.get(atocCode);
    }
}
