package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import parser.Parser;

/**
 * In charge of reading task info from file, and returning a list of Task
 * objects. Only run once at instantiation of DataFile class.
 * 
 * @author A0116373J
 * 
 */

public class TaskReader {

    /** The file object to read from. */
    private File file;

    /**
     * Default constructor.
     * 
     * Made private to prevent instantiation without providing filename.
     */
    private TaskReader() {
    }

    /** Constructor. */
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

                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String unparsedText = scanner.nextLine();
                    Task tempTask = Parser.parseToTask(unparsedText);
                    tasksFromFile.add(tempTask);
                }
                scanner.close();

                return tasksFromFile;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Checks if file with provided filename exists.
     * 
     * @return True if file exists.
     */
    public boolean fileExists() {
        return file.exists();
    }
}