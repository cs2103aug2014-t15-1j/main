package database;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Unit tests for the TaskWriter class.
 * 
 * @author A0116373J
 */

public class TaskWriterTest {

    private final String FILENAME = "testFile.txt";

    /**
     * Checks if constructor is capable of creating a new file, if it does not
     * exist beforehand. Deletes file if it already exists, to allow a new one
     * to be created.
     */
    @Test
    public void testTaskWriterConstructorNewFile() {
        try {
            File file = new File(FILENAME);

            // Deletion, if any exists
            Path filePath = Paths.get(FILENAME);
            Files.deleteIfExists(filePath);

            // Creation
            TaskWriter writer = new TaskWriter(FILENAME);

            assertTrue("File has been created", file.exists());

            // Getting rid of garbage files
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("Unhandled IOException: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Checks if constructor is capable of working with an existing file.
     */
    @Test
    public void testTaskWriterConstructorExistingFile() {
        try {
            // Creation
            File file = new File(FILENAME);
            file.createNewFile();

            assertTrue("File has been created", file.exists());

            TaskWriter writer = new TaskWriter(FILENAME);

            assertTrue("File still exists", file.exists());

            // Getting rid of garbage files
            Path filePath = Paths.get(FILENAME);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("Unhandled IOException: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Checks that TaskWriter can write to file,
     */
    @Test
    public void testWrite() {
        try {
            // Getting rid of garbage files
            Path filePath = Paths.get(FILENAME);
            Files.deleteIfExists(filePath);
        } catch (IOException e1) {
            System.out.println("Unhandled IOException: " + e1);
            e1.printStackTrace();
        }

        try {
            // Initializing
            File file = new File(FILENAME);
            TaskWriter writer = new TaskWriter(FILENAME);

            // Writing to file
            String testInfo = "test\n123###\n";
            assertTrue("Info is written", writer.write(testInfo));

            // Reading from file
            Scanner scanner = new Scanner(file);
            String readInfo = "";
            while (scanner.hasNextLine()) {
                readInfo += scanner.nextLine() + "\n";
            }

            // Checking equality
            assertEquals("Written and read info is equal", testInfo, readInfo);

            scanner.close();
        } catch (FileNotFoundException e2) {
            System.out.println("File not found: " + e2);
            e2.printStackTrace();
        }
    }
}