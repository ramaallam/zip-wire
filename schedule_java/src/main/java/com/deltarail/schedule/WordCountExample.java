package com.deltarail.schedule;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Created by RAllam on 04/05/2016.
 */
public class WordCountExample {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("org.sparkexample.WordCount");
        JavaSparkContext sc = new JavaSparkContext(conf);

        sc.hadoopConfiguration().set("textinputformat.record.delimiter", "\n");

        JavaRDD<String> file = sc.textFile(args[0]);

        /*JavaRDD<String> words = file.flatMap(line -> Arrays.asList(line.split(" ")));
        JavaPairRDD<String, Integer> pairs = words.mapToPair(word -> new Tuple2<String, Integer>(word, 1));
        JavaPairRDD<String, Integer> counter = pairs.reduceByKey((a, b) -> a + b);*/

        JavaPairRDD<String, Integer> pairs = file.mapToPair(record -> new Tuple2<String, Integer>(record, 1));

        sc.hadoopConfiguration().set("textinputformat.record.delimiter", "\n");
        JavaRDD<String> file2 = sc.textFile(args[0]);
        JavaPairRDD<String, Integer> two = file2.mapToPair(record -> new Tuple2<String, Integer>(record, 1));
//        pairs.mapPartitionsWithIndex((idx, iter) -> { if (idx == 0) iter.remove(); return iter; });
        pairs.saveAsTextFile(args[1]);
        two.saveAsTextFile(args[2]);
    }
}
