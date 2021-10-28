
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.regionserver.NoSuchColumnFamilyException;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.conf.Configuration;
import java.io.IOException;

public class HBASEWriteData {
    public static int row = 1;
    public static Configuration config;
    public static Connection connection;
    public static Table table;

    public HBASEWriteData() throws IOException {
        config = HBaseConfiguration.create();
        connection = ConnectionFactory.createConnection(config);
        table = connection.getTable(TableName.valueOf("peoples"));
    }
    public void writeData(String str) throws IOException {
        try {
            String[] data = str.split(",");
            Put p = new Put(Bytes.toBytes("row" + row++));
            p.addColumn(Bytes.toBytes("name"), Bytes.toBytes("col1"), Bytes.toBytes(data[0]));
            p.addColumn(Bytes.toBytes("age"), Bytes.toBytes("col2"), Bytes.toBytes(data[1]));
            p.addColumn(Bytes.toBytes("company"), Bytes.toBytes("col3"), Bytes.toBytes(data[2]));
            p.addColumn(Bytes.toBytes("building_code"), Bytes.toBytes("col4"), Bytes.toBytes(data[3]));
            p.addColumn(Bytes.toBytes("phone_Number"), Bytes.toBytes("col5"), Bytes.toBytes(data[4]));
            p.addColumn(Bytes.toBytes("address"), Bytes.toBytes("col6"), Bytes.toBytes(data[5]));
            table.put(p);
            System.out.println("Row Inserted ... ");
        } catch(NoSuchColumnFamilyException e) {
            e.printStackTrace();
            return;
        }
    }
    public  void createTable(String tableName) throws IOException {

        Admin hAdmin = connection.getAdmin();

        try {

            if (hAdmin.tableExists(TableName.valueOf(tableName))) {
                System.out.println(tableName + " already exists");
                return;
            }
            TableName tname = TableName.valueOf(tableName);
            TableDescriptorBuilder tableDescBuilder = TableDescriptorBuilder.newBuilder(tname);

            tableDescBuilder.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder("name".getBytes()).build())
                    .setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder("age".getBytes()).build())
                    .setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder("company".getBytes()).build())
                    .setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder("building_code".getBytes()).build())
                    .setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder("phone_Number".getBytes()).build())
                    .setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder("address".getBytes()).build())
                    .build();

            hAdmin.createTable(tableDescBuilder.build());

            System.out.println(tableName + "table crated");
        } catch(TableNotFoundException tableNotFoundException) {
            tableNotFoundException.printStackTrace();
        }

    }
}
