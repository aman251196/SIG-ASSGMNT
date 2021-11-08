package Constants;

public class Constants {
    public static final String HDFS_ROOT_URL              = "hdfs://localhost:9000/input/";
    public static final String HDFS_LOCAL_HOST            = "hdfs://localhost:9000/";
    public static final String HDFS_LOCAL_HOST_NAME       = "fs.defaultFS";
    public static final String HDFS_OUTPUT_PATH           = "hdfs://localhost:9000/output/";
    public static final String HBASE_INPUT_TABLE_NAME     = "peoples";
    public static final String HBASE_OUTPUT_TABLE_NAME    = "peoples1";
    public static final String EMP_CSV_PATH               = "Employee.csv";
    public static final String BUILDING_CSV_PATH          = "Building.csv";
    public static final String EMPLOYEE_TABLE_NAME        = "employees";
    public static final String BUILDING_TABLE_NAME        = "building";
    public static final String EMPLOYEE_HDFS_INPUT_PATH   = "hdfs://localhost:9000/employee/employeeSerializedFile-2";
    public static final String EMPLOYEE_HFILE_OUTPUT_PATH = "hdfs://localhost:9000/employee/employee_hfile1/";
    public static final String BUILDING_HFILE_OUTPUT_PATH = "hdfs://localhost:9000/employee/building_hfile/";
    public static final String BUILDING_HDFS_INPUT_PATH   = "hdfs://localhost:9000/building/";
    public static final String SCAN_ATTRIBUTES_TABLE_NAME = "scan.attributes.table.name";
    public static final String EMPLOYEES_WITH_CAFETRIA_CODE_TABLE = "employeesWithCafetriaCode";
    public static final byte[] EMPLOYEES_WITH_CAFETRIA_CODE_FAMILY = Bytes.toBytes("employeeCafeteriaCode");
    public static byte[] EMPLOYEE_TABLE_BYTES = Bytes.toBytes("employees");
    public static byte[] BUILDING_TABLE_BYTES = Bytes.toBytes("buildings");
    public static final String EMPLOYEE = "employee";
    public static final String BUILDING = "building";
    public static final String EMPLOYEE_DETAILS = "employee_details";
    public static final String BUILDING_DETAILS = "building_details";
    public static final String HDFS_OUTPUT_PATH_4B = "hdfs://localhost:9000/assignment4b";
}
