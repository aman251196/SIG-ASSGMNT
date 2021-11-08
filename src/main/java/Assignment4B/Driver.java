package Assignment4b;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static Constants.Constants.*;

public class Driver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        List<Scan> scans = new ArrayList();
        Scan scan = new Scan();
        scan.setCaching(500);
        scan.setCacheBlocks(false);
        scan.setAttribute(SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes(EMPLOYEE_TABLE_NAME));
        scans.add(scan);
        scan = new Scan();
        scan.setCaching(500);
        scan.setCacheBlocks(false);
        scan.setAttribute(SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes(BUILDING_TABLE_NAME));
        scans.add(scan);
        Configuration config = new Configuration();
        Job job = Job.getInstance(config);
        job.setJobName("MRTableReadWrite");
        job.setJarByClass(Driver.class);
        TableMapReduceUtil.initTableMapperJob(
                scans,
                MapperClass.class,
                IntWritable.class,
                Result.class,
                job
        );
        TableMapReduceUtil.initTableReducerJob(
                EMPLOYEES_WITH_CAFETRIA_CODE_TABLE,
                ReducerClass.class,
                job
        );
        Path output = new Path(HDFS_OUTPUT_PATH_4B);
        FileSystem fs = FileSystem.get(URI.create(HDFS_OUTPUT_PATH_4B), config);
        if (fs.exists(output)) {                                      // delete existing directory
            fs.delete(output, true);
        }
        FileOutputFormat.setOutputPath(job, new Path(HDFS_OUTPUT_PATH_4B));
        boolean b = job.waitForCompletion(true);
        System.out.println(b);
        if (job.isSuccessful()) {
            System.out.println("Cafeteria code added to employee table");
        }
    }
}