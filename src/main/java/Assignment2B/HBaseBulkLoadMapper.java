package Assignment2B;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;

import java.io.IOException;

public class HBaseBulkLoadMapper {
    public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
        // this example is just copying the data from the source table...
        context.write(row, resultToPut(row,value));
    }

    private static Put resultToPut(ImmutableBytesWritable key, Result result) throws IOException {
        Put put = new Put(key.get());
        for (Cell kv : result.rawCells()) {
            put.add(kv);
        }
        return put;
    }
}
