package com.deltarail.schedule.main;

import com.deltarail.schedule.config.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.dynamodb.DynamoDBItemWritable;
import org.apache.hadoop.dynamodb.read.DynamoDBInputFormat;
import org.apache.hadoop.dynamodb.write.DynamoDBOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by RAllam on 13/05/2016.
 */
public class DynamoWriter {

    public static void main(String[] args) {
        Configuration.setUp();
        JavaSparkContext sc = Configuration.getSparkContext();
        JobConf jobConf = new JobConf(sc.hadoopConfiguration());
        jobConf.set("dynamodb.servicename", "dynamodb");
        jobConf.set("dynamodb.endpoint", "dynamodb.eu-west-1.amazonaws.com");
        jobConf.set("dynamodb.regionid", "eu-west-1");
        jobConf.set("dynamodb.input.tableName","JourneyData");
        jobConf.set("dynamodb.throughput.read.percent","0.5");
        jobConf.set("mapred.input.format.class","org.apache.hadoop.dynamodb.read.DynamoDBInputFormat");
        jobConf.set("mapred.output.format.class","org.apache.hadoop.dynamodb.write.DynamoDBOutputFormat");


        JobConf jobConf2 = new JobConf(sc.hadoopConfiguration());
        jobConf2.set("dynamodb.output.tableName","journeyReplica");
        jobConf2.set("dynamodb.throughput.write.percent","0.5");
        jobConf2.set("mapred.input.format.class","org.apache.hadoop.dynamodb.read.DynamoDBInputFormat");
        jobConf2.set("mapred.output.format.class","org.apache.hadoop.dynamodb.write.DynamoDBOutputFormat");

        JavaPairRDD<Text, DynamoDBItemWritable> events = sc.hadoopRDD(jobConf, DynamoDBInputFormat.class, Text.class, DynamoDBItemWritable.class);

//TODO transformations

//In this example we are only doing a copying
        events.saveAsHadoopDataset(jobConf2);
    }

}
