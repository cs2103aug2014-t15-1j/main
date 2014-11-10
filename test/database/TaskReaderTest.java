package database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import objects.DateTime;
import objects.Task;

import org.junit.Test;

/**
 * Unit tests for the TaskReader class.
 * 
 * @author A0116373J
 */

public class TaskReaderTest {

    private final String FILENAME = "testFile.txt";

    @Test
    public void testReadNonExistentFile() {
        try {
            // Deletion, if any exists
            Path filePath = Paths.get(FILENAME);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("Unhandled IOException: " + e);
            e.printStackTrace();
        }

        File file = new File(FILENAME);

        assertFalse("File does not exist", file.exists());

        TaskReader reader = new TaskReader(FILENAME);
        assertNull("Null return for nonexistent file", reader.read());
        assertTrue("File has been created", file.exists());
    }

    @Test
    public void testReadExistingFile() {
        try {
            // Deletion, if any exists
            Path filePath = Paths.get(FILENAME);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("Unhandled IOException: " + e);
            e.printStackTrace();
        }

        try {
            File file = new File(FILENAME);
            file.createNewFile();

            // Create new Task obj for writing and comparison
            Task tempTask = new Task("", new DateTime(), new DateTime(),
                    new DateTime(), new ArrayList<String>(), TaskType.TODO);
            Task.decrementId(); // To facilitate Task.equals()

            // Write to file
            FileWriter newFile = new FileWriter(file, false);
            newFile.write(tempTask.toString());
            newFile.close();

            // Read from file
            TaskReader reader = new TaskReader(FILENAME);
            List<Task> tasks = reader.read();

            assertTrue("Task was successfully read from file",
                       tasks.contains(tempTask));
        } catch (IOException e) {
            System.out.println("Unhandled IOException: " + e);
            e.printStackTrace();
        }
    }
}