package com.deltarail.schedule.data;

import com.deltarail.schedule.config.Configuration;
import org.apache.hadoop.mapred.JobConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;

/**
 * Created by RAllam on 10/05/2016.
 */
public class DynamoWriter implements Serializable{
    public static void main(String[] args) {
        JavaSparkContext sc = Configuration.getSparkContext();
        JobConf jobConf2 = new JobConf(sc.hadoopConfiguration());
        jobConf2.set("dynamodb.output.tableName", "journeydata");
        jobConf2.set("dynamodb.throughput.write.percent", "0.5");
        jobConf2.set("mapred.input.format.class", "org.apache.hadoop.dynamodb.read.DynamoDBInputFormat");
        jobConf2.set("mapred.output.format.class", "org.apache.hadoop.dynamodb.write.DynamoDBOutputFormat");

//        var events = sc.hadoopRDD(jobConf, classOf[DynamoDBInputFormat], classOf[Text], classOf[DynamoDBItemWritable])

//TODO transformations

//In this example we are only doing a copying
//        journeysRDD.mapsTo().saveAsHadoopDataset(jobConf2);
    }

}
