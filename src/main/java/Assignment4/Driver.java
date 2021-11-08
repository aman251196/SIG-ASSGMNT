package Assignment4;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapred.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.hbase.client.Connection;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.hbase.tool.BulkLoadHFiles;


import static Constants.Constants.HDFS_LOCAL_HOST;
import static Constants.Constants.HDFS_LOCAL_HOST_NAME;


public class Driver {
    public void driver(String tableToInsert, String hdfsInputPath, String hfileOutputPath, Class<EmployeeMapperHBase> employeeMapperClass,  Class<BuildingMapperHBase> buildingMapperClass) {
        try {
            Configuration configuration = new Configuration();
            configuration.set(HDFS_LOCAL_HOST, HDFS_LOCAL_HOST_NAME);
            configuration.set("zookeeper.znode.parent", "/hbase-secure");
            configuration.set("hbase.master","localhost:60000");
            configuration.set("hbase.zookeeper.property.clientPort", "2181");
            configuration.set("hbase.zookeeper.quorum", "localhost");
            configuration.set("fs.hdfs.imp", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
            configuration.set("hbase.fs.tmp.dir", "/Users/sahilbhandari/");

            Path output = new Path(hfileOutputPath);
            FileSystem hdfs = FileSystem.get(URI.create(hfileOutputPath), configuration);
            if (hdfs.exists(output)) {                                      // delete existing directory
                hdfs.delete(output, true);
            }

            Job job = Job.getInstance(configuration);
            job.setJarByClass(Driver.class);
            job.setJobName("EmployeMRJob");
            job.setInputFormatClass(FileInpFormat.class);
//            job.setInputFormatClass(TextInputFormat.class);
            //TextInputFormat.setInputPaths(job, hdfsInputPath);

            job.setNumReduceTasks(0);

            FileInputFormat.setInputPaths(job, hdfsInputPath);
            job.setMapOutputKeyClass(ImmutableBytesWritable.class);
            if (employeeMapperClass != null) {
                job.setMapperClass(employeeMapperClass);
            }
            else {
                job.setMapperClass(buildingMapperClass);
            }
            FileOutputFormat.setOutputPath(job, new Path(hfileOutputPath));
            job.setMapOutputValueClass(Put.class);
            TableName tableName = TableName.valueOf(tableToInsert);

            Connection conn = ConnectionFactory.createConnection(configuration);
            Table tablee = conn.getTable(tableName);
            RegionLocator regionLocator = conn.getRegionLocator(tableName);

            HFileOutputFormat2.configureIncrementalLoad(job, tablee, regionLocator);
            System.out.println("After incremental load" );

            boolean b = job.waitForCompletion(true);
            System.out.println(b);
            if (job.isSuccessful()) {
                //It bulk uploads data into the the table
                try {
                    System.out.println("Starting bulk loading .... " );
                    Configuration config = HBaseConfiguration.create();
                    BulkLoadHFiles.create(config).bulkLoad(tableName, new Path(hfileOutputPath));
                    System.out.println("successful upload to " + tableToInsert + " table");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
