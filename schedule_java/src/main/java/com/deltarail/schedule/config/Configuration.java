package com.deltarail.schedule.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;

/**
 * Created by RAllam on 09/05/2016.
 */
public class Configuration implements Serializable{
    private static JavaSparkContext sc;
    public static void setUp(){
        //TODO master to be modified
        SparkConf conf = new SparkConf().setAppName("ScheduleJob");
        sc = new JavaSparkContext(conf);
        sc.setLogLevel("DEBUG");
//        changeDelimiter("\r\n\r\n");
    }
    public static JavaSparkContext getSparkContext() {
        return sc;
    }
    public static void changeDelimiter(String delimiter){
        sc.hadoopConfiguration().set("textinputformat.record.delimiter", delimiter);
    }
    public static DynamoDB setupAndGetDynamoConfig(){
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(new ProfileCredentialsProvider()).withEndpoint("https://dynamodb.eu-west-1.amazonaws.com");
        client.setRegion(Region.getRegion(Regions.EU_WEST_1));
        DynamoDB dynamoDB = new DynamoDB(client);
        return dynamoDB;
    }
}
