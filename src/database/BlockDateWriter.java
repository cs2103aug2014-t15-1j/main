package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * In charge of writing blocked dates to file.
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class BlockDateWriter {

    /** The file object to read from. */
    private File file;

    /**
     * Default constructor.
     * 
     * Made private to prevent instantiation without providing filename.
     */
    private BlockDateWriter() {
    }

    /** Constructor. Creates file with provided filename if it does not exist. */
    public BlockDateWriter(String filename) {
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
     * Writes String containing blocked dates to system file.
     * 
     * @param allBlockDates
     *            Blocked dates to write to file.
     * @return True, if successfully written to file.
     */
    public boolean write(String allBlockDates) {
        try {
            FileWriter newFile = new FileWriter(file, false);
            newFile.write(allBlockDates);
            newFile.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}