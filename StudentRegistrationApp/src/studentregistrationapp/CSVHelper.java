package studentregistrationapp;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author anita
 */
public class CSVHelper {
    


    public static void save(String record) {

        try(FileWriter fw = new FileWriter("students.csv", true)) {
            fw.write(record + "\n");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
