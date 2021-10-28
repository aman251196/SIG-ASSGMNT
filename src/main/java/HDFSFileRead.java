import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static Constants.Constants.*;

public class HDFSFileRead {

    public static void main(String[] args) throws IOException {
        try {
            Configuration configuration = new Configuration();
            configuration.set(HDFS_LOCAL_HOST_NAME, HDFS_LOCAL_HOST);

            FileSystem fileSystem = FileSystem.get(configuration);

            FileStatus[] fileHdfs = fileSystem.listStatus(new Path(HDFS_ROOT_URL));
            Path[] paths = FileUtil.stat2Paths(fileHdfs);

            HBASEWriteData write = new HBASEWriteData();
            write.createTable("peoples");

            for (Path path : paths) {

                FSDataInputStream inputStream = fileSystem.open(path);
                InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String line = bufferedReader.readLine();
                while (line != null) {
                    line = bufferedReader.readLine();
                    if(line == null)
                        break;
                    write.writeData(line);
                }
                System.out.println("Inserted....>>>>>");
                inputStream.close();

            }

            fileSystem.close();
        } catch (FileNotFoundException fileNotFound){
            fileNotFound.printStackTrace();
            return;
        }

    }
}