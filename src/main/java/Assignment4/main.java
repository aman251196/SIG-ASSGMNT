package Assignment4;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

import static Constants.Constants.*;


public class main {
    public static void main(String args[]) throws IOException {
        Class<EmployeeMapperHBase> employeeMapperClass = EmployeeMapperHBase.class;
        Class<BuildingMapperHBase> buildingMapperClass = BuildingMapperHBase.class;
        //createTableEmployee("employees");
        //createTableBuilding(BUILDING_TABLE_NAME);
        Driver driver = new Driver();
        driver.driver(EMPLOYEE_TABLE_NAME, EMPLOYEE_HDFS_INPUT_PATH, EMPLOYEE_HFILE_OUTPUT_PATH, employeeMapperClass,null);
        //driver.driver(BUILDING_TABLE_NAME, BUILDING_HDFS_INPUT_PATH, BUILDING_HFILE_OUTPUT_PATH,null,buildingMapperClass);
    }

    private static void createTableEmployee(String tableNameToCreate ) throws IOException {
        try {
            Configuration config = HBaseConfiguration.create();
            config.addResource("/opt/homebrew/Cellar/hbase/2.4.6/libexec/conf/hbase-site.xml");
            config.set("zookeeper.znode.parent", "/hbase-secure");
            config.set("hbase.zookeeper.quorum", "localhost");
            config.set("hbase.zookeeper.property.client.port", "2181");
            config.setInt("timeout", 120000);
            config.set("hbase.master", "localhost:60000");
            Connection connection = ConnectionFactory.createConnection(config);
            Admin hAdmin = connection.getAdmin();

            if (hAdmin.tableExists(TableName.valueOf(tableNameToCreate))) {
                System.out.println("Table already exists");
                return;
            }
            TableName tname = TableName.valueOf(tableNameToCreate);
            TableDescriptorBuilder tableDescBuilder = TableDescriptorBuilder.newBuilder(tname);
            ColumnFamilyDescriptorBuilder columnDescBuilderEmployee = ColumnFamilyDescriptorBuilder
                    .newBuilder(Bytes.toBytes("employee"));
            tableDescBuilder.setColumnFamily(columnDescBuilderEmployee.build());
            tableDescBuilder.build();
            hAdmin.createTable(tableDescBuilder.build());
            System.out.println(tableNameToCreate);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTableBuilding(String tableNameToCreate) throws IOException {

        Configuration config = HBaseConfiguration.create();
        config.addResource("/opt/homebrew/Cellar/hbase/2.4.6/libexec/conf/hbase-site.xml");
        config.set("zookeeper.znode.parent", "/hbase-secure");
        config.set("hbase.zookeeper.quorum","localhost");
        config.set("hbase.zookeeper.property.client.port","2181");
        config.setInt("timeout", 120000);
        config.set("hbase.master", "localhost:60000");
        Connection connection = ConnectionFactory.createConnection(config);
        Admin hAdmin = connection.getAdmin();
        if (hAdmin.tableExists(TableName.valueOf(tableNameToCreate))) {
            System.out.println("table already exists");
            return;
        }
        TableName tname = TableName.valueOf(tableNameToCreate);
        TableDescriptorBuilder tableDescBuilder = TableDescriptorBuilder.newBuilder(tname);
        ColumnFamilyDescriptorBuilder columnDescBuilderBuilding = ColumnFamilyDescriptorBuilder
                .newBuilder(Bytes.toBytes("building"));
        tableDescBuilder.setColumnFamily(columnDescBuilderBuilding.build());
        tableDescBuilder.build();
        hAdmin.createTable(tableDescBuilder.build());
        System.out.println(tableNameToCreate);
    }
}