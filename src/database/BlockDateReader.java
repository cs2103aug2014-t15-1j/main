package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import parser.Parser;

/**
 * In charge of reading blocked dates from file, and returning a list of
 * BlockDate objects. Only run once at instantiation of TODO <insert name>
 * class.
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class BlockDateReader {

    /** The file object to read from. */
    private File file;

    /**
     * Default constructor.
     * 
     * Made private to prevent instantiation without providing filename.
     */
    private BlockDateReader() {
    }

    /** Constructor. */
    public BlockDateReader(String filename) {
        this();
        file = new File(filename);
    }

    /**
     * Reads blocked dates from file, and returns a list of BlockDate objects.
     * Creates file with provided filename if it does not exist.
     * 
     * @return List of blocked dates from file, or null if an error was
     *         encountered.
     */
    public List<BlockDate> read() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        List<BlockDate> blocksFromFile = new ArrayList<BlockDate>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String unparsedBlock = scanner.nextLine();
                BlockDate tempBlock = Parser.parseToBlock(unparsedBlock);
                blocksFromFile.add(tempBlock);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return blocksFromFile;
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