package Assignment2B;
import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;

import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapred.InvalidJobConfException;
import org.apache.hadoop.mapreduce.Job;


import java.io.IOException;


import static Constants.Constants.*;
public class HBaseBulkLoadDriver  {

    public static void main(String[] args) {
        try {
            Configuration config = HBaseConfiguration.create();
            config.set("zookeeper.znode.parent", "/hbase-secure");
            config.set("hbase.master","localhost:60000");
            config.set("hbase.zookeeper.property.clientPort", "2181");
            config.set("hbase.zookeeper.quorum", "localhost");
            config.set(HDFS_LOCAL_HOST_NAME, HDFS_LOCAL_HOST);

            Job job =  Job.getInstance(config);
            job.setJobName("ReadWriteJob");
            job.setJarByClass(HBaseBulkLoadMapper.class);    // class that contains mapper

            Scan scan = new Scan();
            scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
            scan.setCacheBlocks(false);  // don't set to true for MR jobs

            TableMapReduceUtil.initTableMapperJob(HBASE_INPUT_TABLE_NAME, scan, HBaseBulkLoadMapper.class, null, null, job);
            TableMapReduceUtil.initTableReducerJob(
                    HBASE_OUTPUT_TABLE_NAME,      // output table
                    null,             // reducer class
                    job);
            job.setNumReduceTasks(0);

            boolean b = job.waitForCompletion(true);
            job.monitorAndPrintJob();
            if (!b) {
                throw new IOException("error with job!"+job.getJobState());
            }
        }  catch(InvalidJobConfException | RetriesExhaustedException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
