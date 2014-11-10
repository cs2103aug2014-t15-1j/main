package gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import logic.CommandType;
import objects.DateTime;
import objects.DateTimeStub;
import objects.ResultStub;
import objects.Task;
import objects.TaskStub;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.TaskType;

/*
 * This class contains the junit tests for the ResultGenerator.java class. 
 * NOTE: Only the name parameters for tasks have been tested since ResultGenerator.java only uses the name parameter
 */

public class ResultGeneratorTest {

    private static ResultGenerator resultGenerator = new ResultGenerator();
    private static List<TaskStub> outputsToDo = new ArrayList<TaskStub>();
    private static List<TaskStub> outputsDate = new ArrayList<TaskStub>();
    private static List<TaskStub> outputsDone = new ArrayList<TaskStub>();
    private static List<String> tags = new ArrayList<String>();

    @Before
    public void setUp() {
        tags.add("");
        outputsToDo.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), tags,
                TaskType.TODO));

        outputsDate.add(new TaskStub("block", new DateTimeStub("06/11/2014",
                "2200"), new DateTimeStub("08/11/2014", "1400"),
                new DateTimeStub("", ""), tags, TaskType.BLOCK));

        outputsDone.add(new TaskStub("name", new DateTimeStub("06/11/2014",
                "2200"), new DateTimeStub("08/11/2014", "1400"),
                new DateTimeStub("08/11/2014", "1400"), tags, TaskType.DONE));
    }

    @After
    public void tearDown() {
        outputsToDo.clear();
        outputsDate.clear();
        outputsDone.clear();
    }

    @Test
    public void test_ResultGenerator() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);

        setUp();
        test_Add();
        tearDown();

        setUp();
        test_Add_Unsucessful();
        tearDown();

        setUp();
        test_Add_NullName();
        tearDown();

        setUp();
        test_Add_NullStringName();
        tearDown();

        setUp();
        test_Add_NeedConfirmation();
        tearDown();

        setUp();
        test_Delete();
        tearDown();

        setUp();
        test_DeleteAll();
        tearDown();

        setUp();
        test_Edit();
        tearDown();

        setUp();
        test_Display();
        tearDown();

        setUp();
        test_Display_20_Tasks();
        tearDown();

        setUp();
        test_Display_EmptyFile();
        tearDown();

        setUp();
        test_Search();
        tearDown();

        setUp();
        test_Todo();
        tearDown();

        setUp();
        test_Done();
        tearDown();

        setUp();
        test_Undo();
        tearDown();

        setUp();
        test_Redo();
        tearDown();

        setUp();
        test_Block();
        tearDown();

        setUp();
        test_Unblock();
        tearDown();

        setUp();
        test_Restore();
        tearDown();

        shell.dispose();
        display.dispose();
    }

    /**
     * Tests a generic successful add command.
     */

    public void test_Add() {

        ResultStub result = new ResultStub(outputsToDo, true, CommandType.ADD,
                false, "todo");

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Added name";
        assertEquals(expected, actual);

    }

    /**
     * Tests a generic unsuccessful add command for the Task. Since
     * ResultGenerator treats all unsucessful commands the same, only one test
     * case has been implemented for all the commands.
     */

    public void test_Add_Unsucessful() {

        ResultStub result = new ResultStub(outputsToDo, false, CommandType.ADD,
                false, "todo");

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Not able to process 'add name'";
        assertEquals(expected, actual);

    }

    /**
     * Tests an invalid Result Object for add command. The Result Object has a
     * null for task name. Since ResultGenerator.java treats an invalid Result
     * Object the same for all commands, only one test case for add has been
     * implemented.
     */
    public void test_Add_NullName() {

        List<TaskStub> outputsNullName = new ArrayList<TaskStub>();
        outputsNullName.add(new TaskStub(null, new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), tags,
                TaskType.TODO));
        ResultStub result = new ResultStub(outputsNullName, false,
                CommandType.ADD, false, "todo");
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
    public void test_Add_EmptyStringName() {

        List<TaskStub> outputsEmptyName = new ArrayList<TaskStub>();
        outputsEmptyName.add(new TaskStub("", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), tags,
                TaskType.TODO));
        ResultStub result = new ResultStub(outputsEmptyName, false,
                CommandType.ADD, false, "todo");
        try {
            resultGenerator.processResult(result, "add name");
        } catch (NullPointerException error) {
            assertTrue(error.getMessage().contains("Task name is invalid"));
        }

    }

    /**
     * Tests an invalid Result Object for add command. The result object has a
     * "null" string for task name. Since ResultGenerator.java treats an invalid
     * Result Object the same for all commands, only one test case for add has
     * been implemented.
     */

    public void test_Add_NullStringName() {

        List<TaskStub> outputsNullStringName = new ArrayList<TaskStub>();
        outputsNullStringName.add(new TaskStub("null",
                new DateTimeStub("", ""), new DateTimeStub("", ""),
                new DateTimeStub("", ""), tags, TaskType.TODO));
        ResultStub result = new ResultStub(outputsNullStringName, false,
                CommandType.ADD, false, "todo");
        try {
            resultGenerator.processResult(result, "add name");
        } catch (NullPointerException error) {
            assertTrue(error.getMessage().contains("Task name is null"));
        }

    }

    /**
     * Tests when an task to be added command coincides with the a blocked date.
     */

    public void test_Add_NeedConfirmation() {

        List<TaskStub> outputsNeedConfirmation = new ArrayList<TaskStub>();
        outputsNeedConfirmation.add(new TaskStub("name", new DateTimeStub("",
                ""), new DateTimeStub("21/10/2014", "18:53"), new DateTimeStub(
                "21/10/2014", "21:00"), tags, TaskType.TODO));
        ResultStub result = new ResultStub(outputsNeedConfirmation, true,
                CommandType.ADD, true, "todo");

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Unable to add task. Task coincides with a blocked date.";
        assertEquals(expected, actual);

    }

    /**
     * Tests generic delete
     */

    public void test_Delete() {

        ResultStub result = new ResultStub(outputsToDo, true,
                CommandType.DELETE, false, "todo");
        String actual = resultGenerator.processResult(result, "delete name");
        String expected = "Deleted name";
        assertEquals(expected, actual);

    }

    /**
     * Tests generic deleteAll, asks the user for confirmation. Reset is not
     * tested because it is the exact same implementation
     */

    public void test_DeleteAll() {

        ResultStub result = new ResultStub(outputsToDo, true,
                CommandType.DELETE, true, "todo");
        String actual = resultGenerator.processResult(result, "delete name");
        String expected = "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
        assertEquals(expected, actual);

    }

    /**
     * Tests generic edit
     */

    public void test_Edit() {

        Task task = outputsToDo.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.EDIT,
                false, "todo");
        String actual = resultGenerator.processResult(result, "edit" + ID);
        String expected = "Edited name";
        assertEquals(expected, actual);

    }

    /**
     * Tests generic display
     */

    public void test_Display() {

        ResultStub result = new ResultStub(outputsToDo, true,
                CommandType.DISPLAY, false, "todo");
        String actual = resultGenerator.processResult(result, "display");
        String expected = "1 task(s) found.";
        assertEquals(expected, actual);

    }

    /**
     * Tests display for 20 tasks
     */

    public void test_Display_20_Tasks() {

        List<TaskStub> outputsDisplayTest = new ArrayList<TaskStub>();
        for (int index = 0; index < 10; index++) {
            outputsDisplayTest.add(new TaskStub("name",
                    new DateTimeStub("", ""), new DateTimeStub("", ""),
                    new DateTimeStub("", ""), tags, TaskType.TODO));
            outputsDisplayTest.add(new TaskStub("email boss", new DateTimeStub(
                    "08/11/2014", "1200"), new DateTimeStub("15/12/2014",
                    "1800"), new DateTimeStub("19/11/2014", "1400"), tags,
                    TaskType.DONE));

            outputsDisplayTest
                    .add(new TaskStub("empty", new DateTimeStub("08/11/2014",
                            "1200"), new DateTimeStub("", ""),
                            new DateTimeStub("11/11/2014", "1700"), tags,
                            TaskType.DONE));

        }

        ResultStub result = new ResultStub(outputsDisplayTest, true,
                CommandType.DISPLAY, false, "todo");
        String actual = resultGenerator.processResult(result, "display");
        String expected = "30 task(s) found.";
        assertEquals(expected, actual);

    }

    /**
     * Tests display when there are no tasks to be displayed to the user
     */

    public void test_Display_EmptyFile() {

        List<TaskStub> outputEmpty = new ArrayList<TaskStub>();
        ResultStub result = new ResultStub(outputEmpty, true,
                CommandType.DISPLAY, false, "todo");
        String actual = resultGenerator.processResult(result, "display");
        String expected = "No tasks to show.";
        assertEquals(expected, actual);
        setUp();

    }

    /**
     * Tests generic search
     */

    public void test_Search() {

        ResultStub result = new ResultStub(outputsToDo, true,
                CommandType.SEARCH, false, "search");
        String actual = resultGenerator.processResult(result, "search name");
        String expected = "Found 1 match(es).";
        assertEquals(expected, actual);

    }

    /**
     * Tests generic ToDo
     */

    public void test_Todo() {

        Task task = outputsDone.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputsDone, true, CommandType.TODO,
                false, "todo");
        String actual = resultGenerator.processResult(result, "todo" + ID);
        String expected = "Marked name as todo.";
        assertEquals(expected, actual);

    }

    /**
     * Tests generic done
     */

    public void test_Done() {

        Task task = outputsToDo.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.DONE,
                false, "done");
        String actual = resultGenerator.processResult(result, "Done" + ID);
        String expected = "Marked name as done.";
        assertEquals(expected, actual);

    }

    /**
     * Tests generic undo. Undo a command related to a task e.g. add, edit,
     * delete
     */

    public void test_Undo() {

        ResultStub result = new ResultStub(outputsDone, true, CommandType.UNDO,
                false, "todo");
        String actual = resultGenerator.processResult(result, "undo");
        String expected = "Command Undone.";
        assertEquals(expected, actual);

    }

    /**
     * Test generic redo. Redo a command related to task e.g. add, edit, delete
     */

    public void test_Redo() {

        ResultStub result = new ResultStub(outputsToDo, true, CommandType.REDO,
                false, "todo");
        String actual = resultGenerator.processResult(result, "redo");
        String expected = "Command Redone.";
        assertEquals(expected, actual);

    }

    public void test_Block() {

        ResultStub result = new ResultStub(outputsDate, true,
                CommandType.BLOCK, false, "block");
        String actual = resultGenerator
                .processResult(result,
                               "block 06/11/2014 2200 to 08/11/2014 1400");

        String expected = "BLOCKED: 06/11/2014 2200 to 08/11/2014 1400";
        assertEquals(expected, actual);

    }

    public void test_Unblock() {

        ResultStub result = new ResultStub(outputsDate, true,
                CommandType.UNBLOCK, false, "block");
        String actual = resultGenerator
                .processResult(result,
                               "unblock 06/11/2014 2200 to 08/11/2014 1400");
        String expected = "UNBLOCKED: 06/11/2014 2200 to 08/11/2014 1400";
        assertEquals(expected, actual);

    }

    public void test_Restore() {

        Task task = outputsToDo.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputsToDo, true,
                CommandType.RESTORE, false, "todo");
        String actual = resultGenerator.processResult(result, "restore" + ID);
        String expected = "Restored name.";
        assertEquals(expected, actual);

    }

}
