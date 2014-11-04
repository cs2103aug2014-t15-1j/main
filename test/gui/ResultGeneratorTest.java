package gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import logic.CommandType;
import logic.Result.ResultType;
import objects.BlockDateStub;
import objects.DateTimeStub;
import objects.ResultStub;
import objects.TaskStub;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

/*
 * This class contains the junit tests for the ResultGenerator.java class. 
 * It uses dependency injection to ensure that all results are based solely from ResultGenerator.java.
 * NOTE: Only the name parameters for tasks have been tested since ResultGenerator.java only uses the name parameter
 */

public class ResultGeneratorTest {

    private static ResultGenerator resultGenerator = new ResultGenerator();
    private static ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
    private static ArrayList<BlockDateStub> outputsDate = new ArrayList<BlockDateStub>();

    @After
    public void tearDown() {
        outputs.clear();
        outputsDate.clear();
    }

    /**
     * Tests a generic successful add command for the Task ResultType.
     */
    @Test
    public void test_Add() {
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.ADD,
                false, ResultType.TASK);

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Added name";
        assertEquals(expected, actual);
    }

    /**
     * Tests a generic unsuccessful add command for the Task ResultType. Since
     * ResultGenerator treats all unsucessful commands the same, only one test
     * case has been implemented for all the commands.
     */
    @Test
    public void test_Add_Unsucessful() {
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, false, CommandType.ADD,
                false, ResultType.TASK);

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Not able to process 'add name'";
        assertEquals(expected, actual);
    }

    /**
     * Tests an invalid Result Object for add command for Task Result Type. The
     * Result Object has a null for task name. Since ResultGenerator.java treats
     * an invalid Result Object the same for all commands, only one test case
     * for add has been implemented.
     */
    @Test
    public void test_Add_NullName() {
        outputs.add(new TaskStub(null, new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, false, CommandType.ADD,
                false, ResultType.TASK);
        try {
            resultGenerator.processResult(result, "add name");
        } catch (NullPointerException error) {
            assertTrue(error.getMessage().contains("Task name is invalid"));
        }
    }

    /**
     * Tests an invalid Result Object for add command for Task Result Type. The
     * result object has a empty string for task name. Since
     * ResultGenerator.java treats an invalid Result Object the same for all
     * commands, only one test case for add has been implemented.
     */
    @Test
    public void test_Add_EmptyName() {
        outputs.add(new TaskStub("", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, false, CommandType.ADD,
                false, ResultType.TASK);
        try {
            resultGenerator.processResult(result, "add name");
        } catch (NullPointerException error) {
            assertTrue(error.getMessage().contains("Task name is invalid"));
        }
    }

    /**
     * Tests an invalid Result Object for add command for Task Result Type. The
     * result object has a "null" string for task name. Since
     * ResultGenerator.java treats an invalid Result Object the same for all
     * commands, only one test case for add has been implemented.
     */
    @Test
    public void test_Add_NullStringName() {
        outputs.add(new TaskStub("null", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, false, CommandType.ADD,
                false, ResultType.TASK);
        try {
            resultGenerator.processResult(result, "add name");
        } catch (NullPointerException error) {
            assertTrue(error.getMessage().contains("Task name is null"));
        }
    }

    /**
     * Tests when an task to be added command coincides with the a blocked date.
     */
    @Test
    public void test_Add_NeedConfirmation() {
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("21/10/2014", "18:53"), new DateTimeStub(
                        "21/10/2014", "21:00"), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.ADD,
                true, ResultType.TASK);

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Unable to add task. Task coincides with a blocked date. Key 'y' to override date or 'n' to abort";
        assertEquals(expected, actual);
    }

    @Ignore
    public void test_CommandType_NullTaskResultType() {
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, null, false,
                ResultType.TASK);

        try {
            resultGenerator.processResult(result, "add name");
        } catch (IllegalArgumentException error) {
            assertTrue(error.getMessage().contains("Illegal Command Type"));
        }
    }

    @Ignore
    public void test_CommandType_NullBlockDateResultType() {
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, null, false,
                ResultType.BLOCKDATE);

        try {
            resultGenerator.processResult(result, "add name");
        } catch (IllegalArgumentException error) {
            assertTrue(error.getMessage().contains("Illegal Command Type"));
        }
    }

    /**
     * Tests generic delete
     */
    @Test
    public void test_Delete() {
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.DELETE,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "delete name");
        String expected = "Deleted name";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic deleteAll, asks the user for confirmation
     */
    @Test
    public void test_DeleteAll() {
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.DELETE,
                true, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "delete name");
        String expected = "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic edit
     */
    @Test
    public void test_Edit() {
        TaskStub task = new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null);
        outputs.add(task);
        ResultStub result = new ResultStub(outputs, true, CommandType.EDIT,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result,
                                                      "edit" + task.getID());
        String expected = "Edited name";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic display
     */
    @Test
    public void test_Display() {
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.DISPLAY,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "display");
        String expected = "1 task(s) found.";
        assertEquals(expected, actual);
    }

    /**
     * Tests display when there are no tasks to be displayed to the user
     */
    @Test
    public void test_Display_EmptyFile() {
        ResultStub result = new ResultStub(outputs, true, CommandType.DISPLAY,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "display");
        String expected = "No tasks to show.";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic search
     */
    @Test
    public void test_Search() {
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.SEARCH,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "search name");
        String expected = "Found 1 match(es).";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic ToDo
     */
    @Test
    public void test_Todo() {
        TaskStub task = new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null);
        int ID = task.getId();
        outputs.add(task);
        ResultStub result = new ResultStub(outputs, true, CommandType.TODO,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "todo" + ID);
        String expected = "Marked name as todo.";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic done
     */
    @Test
    public void test_Done() {
        TaskStub task = new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null);
        int ID = task.getId();
        outputs.add(task);
        ResultStub result = new ResultStub(outputs, true, CommandType.DONE,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "Done" + ID);
        String expected = "Marked name as done.";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic undo for ResultType Task. Undo a command related to a task
     * e.g. add, edit, delete
     */
    @Test
    public void test_Undo() {
        ResultStub result = new ResultStub(outputs, true, CommandType.UNDO,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "undo");
        String expected = "Command Undone.";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic undo for ResultType blockdate. Undo a block/unblock date
     * command
     */
    @Test
    public void test_Undo_Block() {
        outputsDate.add(new BlockDateStub(
                new DateTimeStub("21/10/2014", "1922"), new DateTimeStub(
                        "21/10/2014", "1927")));
        ResultStub result = new ResultStub(outputsDate, true, CommandType.UNDO,
                false, ResultType.BLOCKDATE);
        String actual = resultGenerator.processResult(result, "redo");
        String expected = "Command Undone.";
        assertEquals(expected, actual);
    }

    /**
     * Test generic redo for ResultType task. Redo a command related to task
     * e.g. add, edit, delete
     */
    @Test
    public void test_Redo() {
        ResultStub result = new ResultStub(outputs, true, CommandType.REDO,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "redo");
        String expected = "Command Redone.";
        assertEquals(expected, actual);
    }

    /**
     * Test generic redo for ResultType block date. Redo a block/unblock date
     * command
     */
    @Test
    public void test_Redo_Block() {
        outputsDate.add(new BlockDateStub(
                new DateTimeStub("21/10/2014", "1922"), new DateTimeStub(
                        "21/10/2014", "1927")));
        ResultStub result = new ResultStub(outputsDate, true, CommandType.REDO,
                false, ResultType.BLOCKDATE);
        String actual = resultGenerator.processResult(result, "redo");
        String expected = "Command Redone.";
        assertEquals(expected, actual);
    }

    @Ignore
    // Block has not been fully implemented
    public void test_Block() {
        outputsDate.add(new BlockDateStub(
                new DateTimeStub("21/10/2014", "1922"), new DateTimeStub(
                        "21/10/2014", "1927")));
        ResultStub result = new ResultStub(outputsDate, true,
                CommandType.BLOCK, false, ResultType.BLOCKDATE);
        String actual = resultGenerator
                .processResult(result,
                               "block 1922 21/10/2014 to 1927 21/10/2014");
        String expected = "BLOCKED: 21/10/2014 1922 to 21/10/2014 1927";
        assertEquals(expected, actual);
    }

    @Ignore
    // Block has not been fully implemented
    public void test_Unblock() {
        outputsDate.add(new BlockDateStub(
                new DateTimeStub("21/10/2014", "1922"), new DateTimeStub(
                        "21/10/2014", "1927")));
        ResultStub result = new ResultStub(outputsDate, true,
                CommandType.UNBLOCK, false, ResultType.BLOCKDATE);
        String actual = resultGenerator
                .processResult(result,
                               "unblock 1922 21/10/2014 to 1927 21/10/2014");
        String expected = "UNBLOCKED: 21/10/2014 1922 to 21/10/2014 1927";
        assertEquals(expected, actual);
    }

}
