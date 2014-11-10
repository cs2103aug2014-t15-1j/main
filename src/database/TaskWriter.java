package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * In charge of writing task info to file.
 * 
 * @author A0116373J
 * 
 */

public class TaskWriter {

    /** The file object to read from. */
    private File file;

    /**
     * Default constructor.
     * 
     * Made private to prevent instantiation without providing filename.
     */
    private TaskWriter() {
    }

    /** Constructor. Creates file with provided filename if it does not exist. */
    public TaskWriter(String filename) {
        this();
        file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes String containing task info to system file.
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
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}