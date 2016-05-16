package com.deltarail.schedule.main;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.deltarail.schedule.beans.Header;
import com.deltarail.schedule.beans.Journey;
import com.deltarail.schedule.beans.Location;
import com.deltarail.schedule.config.Configuration;
import com.deltarail.schedule.factory.HeaderFactory;
import com.deltarail.schedule.util.DatesUtility;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.dynamodb.DynamoDBItemWritable;
import org.apache.hadoop.dynamodb.read.DynamoDBInputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import static com.deltarail.schedule.util.Filter.filterOnRecordIdentity;
import static com.deltarail.schedule.util.Filter.isValidActivity;
import static com.deltarail.schedule.util.GeneralUtility.lineSeparator;
import static com.deltarail.schedule.util.JourneyUtility.buildJourneys;

/**
 * Created by RAllam on 09/05/2016.
 */
public class SchedulesProcessor implements Serializable {
    static Logger log = Logger.getLogger(SchedulesProcessor.class.getName());
    private String outputLocation;

    public SchedulesProcessor(String outputLocation) {
        this.outputLocation = outputLocation;
    }

    public void processSchedules(String inputLocation, String locationFile, String atocFile) {
        JavaSparkContext sc = Configuration.getSparkContext();

        Broadcast<Map<String, String>> atocsBroadcast = sc.broadcast(getAtocs(sc, atocFile));
        Broadcast<Map<String, Location>> locationsBroadcast = sc.broadcast(getLocations(sc, locationFile));

//        Configuration.changeDelimiter(lineSeparator(2));
        Configuration.changeDelimiter("\r\n\r\n");
        JavaRDD<String> records = sc.textFile(inputLocation);
        JavaRDD<String> validRecords = records.filter(record -> DatesUtility.isValidPeriod(record));
        log.info("After valid records: ");
        JavaRDD<String> journeysRDD = validRecords.map(record -> {
            String[] lines = record.split("\n");
            Header header = new Header();
            final List<String> fromTipLoc = new ArrayList<String>();
            final List<String> toTipLoc = new ArrayList<String>();
            final List<String> departureTimes = new ArrayList<String>();
            final List<String> arrivalTimes = new ArrayList<String>();
            log.info("Before for loop.");
            for (String line : lines) {
                try {
                    if (!filterOnRecordIdentity(line)) continue;
                    if (!isValidActivity(line)) continue;
                    //TODO - the to index is 98.. to avoid exception temporarily making it as 87
//                String activity = line.startsWith("LI") ? line.substring(86, 87).trim() : "";
                    String activity = getActivity(line);
                    //TODO to consider only 7 chars for a tiploc
                    //If the data not available, filter out. and log accumulated.
                    String tiploc = line.substring(46, 53).trim();
                    if (tiploc == null) {
                        tiploc = "";
                    }
//                JourneyOtherDetails jDetails = new JourneyOtherDetails();
                    if (line.startsWith("BS")) {
                        header = HeaderFactory.createHeader(line);
                        header.setPublishedOn(LocalDate.now());
                    } //TODO all these constants to be added to a file
                    else if (line.startsWith("LO") || (line.startsWith("LI") && activity.equals("U"))) {
                        //Origin station
                        if (line.startsWith("LO")) {
                            Location loc = locationsBroadcast.value().get(tiploc);
                            //TODO if location happens to be null , Atoc happens to be null.
                            header.setOrigin(loc.getName());
                            String toc = line.substring(37, 39);
                            header.setToc(atocsBroadcast.value().get(toc));
                            departureTimes.add(line.substring(59, 63));
                        } else {
                            departureTimes.add(line.substring(73, 77));
                        }
                        fromTipLoc.add(tiploc);
                    } else if (line.startsWith("LI") && activity.equals("T")) {
                        fromTipLoc.add(tiploc);
                        departureTimes.add(line.substring(73, 77));
                        toTipLoc.add(tiploc);
                        arrivalTimes.add(line.substring(69, 73));
                    } else if (line.startsWith("LT") || (line.startsWith("LI") && activity.equals("D"))) {
                        //Destination Station
                        if (line.startsWith("LT")) {
                            Location loc = locationsBroadcast.value().get(tiploc);
                            header.setDestination(loc.getName());
                            arrivalTimes.add(line.substring(59, 63));
                        } else {
                            arrivalTimes.add(line.substring(69, 73));
                        }
                        toTipLoc.add(tiploc);
                    }
                } catch (Exception e) {
                    //ignore the line.
                }
            }
            List<Journey> journeys = buildJourneys(header, fromTipLoc, toTipLoc, departureTimes, arrivalTimes, locationsBroadcast);
            StringBuilder sb = new StringBuilder();
            for (Journey journey : journeys) {
                /*Table table = Configuration.setupAndGetDynamoConfig().getTable("JourneyData");
                try {
                    Map<String, Object> journeyMap = BeanUtils.describe(journey);
                    table.putItem(new Item().withPrimaryKey("CompositeId", journey.getCompositeID(), "Date", Integer.parseInt(journey.getDateRunning())).withMap("journeyDetails", journeyMap));
                } catch (Exception e) {
                    //ignore the entry
                    e.printStackTrace();
                    log.trace(e.getMessage()+"********** "+e.getCause());
                }*/
                sb.append(journey.toString()).append("\n");
            }
            log.debug("Journeys:" + sb.toString());
            return sb.toString();
        });
        journeysRDD.saveAsTextFile(outputLocation);
        writeToDynamo(journeysRDD.flatMap(record -> Arrays.asList(record.split("\n"))));
        log.debug("COUNT -----------" + journeysRDD.count());
    }

    private Map<String, Location> getLocations(JavaSparkContext sc, String locationFile) {
        return sc.textFile(locationFile).map(line -> {
            String[] items = line.split(",");
            String tiploc = items[0];
            String name = items.length > 5 ? items[4] : "";
            String stanox = items.length > 9 ? items[8] : "     ";
            return new Location(tiploc, name, stanox);
        }).mapToPair(location -> new Tuple2<String, Location>(location.getTIPLOC(), location)).collectAsMap();
    }

    private Map<String, String> getAtocs(JavaSparkContext sc, String atocFile) {
        return sc.textFile(atocFile).mapToPair(line -> {
            String[] items = line.split(",");
            return new Tuple2<String, String>(items[1], items[0]);
        }).collectAsMap();
    }

    private String getActivity(String line) {
        String activity = "";
        try {
            //TODO .. 86-98
            if (line.startsWith("LI")) {
                activity = line.substring(86, 87).trim();
            }
        } catch (StringIndexOutOfBoundsException e) {
            //Ignore the line.
        }
        return activity;
    }

    private void writeToDynamo_old(JavaRDD<String> journeysRDD) {
        JavaSparkContext sc = Configuration.getSparkContext();
        JobConf jobConf = new JobConf(sc.hadoopConfiguration());

        jobConf.set("dynamodb.servicename", "dynamodb");
        jobConf.set("dynamodb.endpoint", "dynamodb.eu-west-1.amazonaws.com");
        jobConf.set("dynamodb.regionid", "eu-west-1");
        jobConf.set("dynamodb.output.tableName", "TEST");
        jobConf.set("dynamodb.throughput.write.percent", "0.5");
        jobConf.set("mapred.input.format.class", "org.apache.hadoop.dynamodb.read.DynamoDBInputFormat");
        jobConf.set("mapred.output.format.class", "org.apache.hadoop.dynamodb.write.DynamoDBOutputFormat");

//        sc.hadoopRDD(jobConf, DynamoDBInputFormat.class, Text.class, DynamoDBItemWritable.class);
        /*JavaPairRDD<Map<String, AttributeValue>, Journey> dummy = journeysRDD.flatMap(record -> Arrays.asList(record.split("\n"))).map(line -> Journey.parse(line))
                .mapToPair(journey -> {
                    Map<String, AttributeValue> key = new HashedMap();
                    key.put("CompositeId", new AttributeValue(journey.getCompositeID()));
                    key.put("Date", new AttributeValue().withN(journey.getDateRunning()));
                    return new Tuple2<Map<String, AttributeValue>, Journey>(key, journey);
                });*/
        JavaPairRDD<Text, Journey> dummy = journeysRDD.flatMap(record -> Arrays.asList(record.split("\n"))).map(line -> Journey.parse(line))
                .mapToPair(j -> {
                    return new Tuple2<Text, Journey>(new Text("CompositeId:" + j.getCompositeID() + ",Date:" + Integer.parseInt(j.getDateRunning())), j);
                });
        dummy.saveAsHadoopDataset(jobConf);
    }

    private void writeToDynamo(JavaRDD<String> journeysRDD) {
        Table table = Configuration.setupAndGetDynamoConfig().getTable("JourneyData");
        List<String> jStr = journeysRDD.collect();
        System.out.println("Count after flatmap :" + journeysRDD.count());
        for (String s : jStr) {
            try {
                Journey journey = Journey.parse(s);
                Map<String, Object> journeyMap = BeanUtils.describe(journey);
                table.putItem(new Item().withPrimaryKey("CompositeId", journey.getCompositeID(), "Date", Integer.parseInt(journey.getDateRunning())).withMap("journeyDetails", journeyMap));
            } catch (Exception e) {
                //ignore the entry
            }
        }
    }
}
