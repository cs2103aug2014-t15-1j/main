//@author A0116373J
package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import logic.Log;

/**
 * This class is in charge of writing task data to file.
 */

public class TaskWriter {

    /** For logging purposes */
    private static void log(String output) {
        if (Log.LOGGING_ENABLED) {
            Log.getLogger().info(output);
        }
    }
    
    /** The file object to write to. */
    private File file;

    /**
     * Default constructor.
     * 
     * Made private to prevent instantiation without providing filename.
     */
    private TaskWriter() {
    }

    /**
     * Constructor. Creates file with provided filename if it does not exist.
     * 
     * @param filename
     *            The name of the file to write to.
     */
    public TaskWriter(String filename) {
        this();
        file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log("IOException, cannot create new file");
                System.out.println("Unhandled IOException: " + e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes String containing task info to file.
     * 
     * @param allTaskInfo
     *            Info of tasks to write to file.
     * @return True, if successfully written to file.
     */
    public boolean write(String allTaskInfo) {
        try {
            FileWriter newFile = new FileWriter(file, false);
            newFile.write(allTaskInfo);
            newFile.close();
            return true;
        } catch (IOException e) {
            log("IOException, failed writing to file");
            System.out.println("Unhandled IOException: " + e);
            e.printStackTrace();
            return false;
        }
    }
}