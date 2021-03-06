//@author A0116373J
package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import logic.Log;
import objects.Task;
import parser.Parser;

/**
 * This class is in charge of reading task data from file, and returning a list
 * of Task objects. Only run once at instantiation of the DatabaseFacade class.
 */

public class TaskReader {

    /** For logging purposes */
    private static void log(String output) {
        if (Log.LOGGING_ENABLED) {
            Log.getLogger().info(output);
        }
    }
    
    /** The file object to read from. */
    private File file;

    /**
     * Default constructor.
     * 
     * Made private to prevent instantiation without providing filename.
     */
    private TaskReader() {
    }

    /**
     * Constructor.
     * 
     * @param filename
     *            The name of the file to read from.
     */
    public TaskReader(String filename) {
        this();
        file = new File(filename);
    }

    /**
     * Reads task info from file, and returns a list of Task objects. Creates
     * file with provided filename if it does not exist.
     * 
     * @return List of tasks from file, or null if an error was encountered.
     */
    public List<Task> read() {
        if (file.exists()) {
            try {
                List<Task> tasksFromFile = new ArrayList<Task>();

                // Read tasks from file
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String unparsedText = scanner.nextLine();
                    Task tempTask = Parser.parseToTask(unparsedText);
                    tasksFromFile.add(tempTask);
                }
                scanner.close();

                return tasksFromFile;
            } catch (FileNotFoundException e) {
                log("File not found exception");
                System.out.println("File not found: " + e);
                e.printStackTrace();
            }
        } else { // File does not exist
            try {
                file.createNewFile();
            } catch (IOException e) {
                log("IOException, cannot create new file");
                System.out.println("Unhandled IOException: " + e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Checks if file exists. Filename is provided in constructor.
     * 
     * @return True if file exists.
     */
    public boolean fileExists() {
        return file.exists();
    }
}