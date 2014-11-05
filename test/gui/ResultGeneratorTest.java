package gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import logic.CommandType;
import logic.Result.ResultType;
import objects.BlockDateStub;
import objects.DateTimeStub;
import objects.ResultStub;
import objects.TaskStub;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import database.Task;

/*
 * This class contains the junit tests for the ResultGenerator.java class. 
 * It uses dependency injection to ensure that all results are based solely from ResultGenerator.java.
 * NOTE: Only the name parameters for tasks have been tested since ResultGenerator.java only uses the name parameter
 */

public class ResultGeneratorTest {

    private static ResultGenerator resultGenerator = new ResultGenerator();
    private static List<TaskStub> outputs = new ArrayList<TaskStub>();
    private static List<BlockDateStub> outputsDate = new ArrayList<BlockDateStub>();
    private static List<String> tags = new ArrayList<String>();
    
   @Before
   public void setUp(){
       tags.add("");
       outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                                new DateTimeStub("", ""), new DateTimeStub("", ""), tags));
       outputsDate.add(new BlockDateStub(
                                         new DateTimeStub("21/10/2014", "1922"), new DateTimeStub(
                                                 "21/10/2014", "1927")));
   }
    
    @After
    public void tearDown() {
        outputs.clear();
        outputsDate.clear();
    }
    
    @Test
    public void test_ResultGenerator(){
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
        test_Add_EmptyName();
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
        test_Undo_Block();
        tearDown();
        
        setUp();
        test_Undo();
        tearDown();
        
        setUp();
        test_Redo();
        tearDown();
        
        setUp();
        test_Redo_Block();
        tearDown();
        
        setUp();
        test_Block();
        tearDown();
        
        setUp();
        test_Unblock();
        tearDown();
           
        shell.dispose();
        display.dispose();
    }

    /**
     * Tests a generic successful add command for the Task ResultType.
     */
    
    public void test_Add() {
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
    
    public void test_Add_Unsucessful() {
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
    
    public void test_Add_NullName() {
        List<Task> outputsNullName = new ArrayList<Task>();
        outputsNullName.add(new TaskStub(null, new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), tags));
        ResultStub result = new ResultStub(outputsNullName, false, CommandType.ADD,
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
    
    public void test_Add_EmptyName() {
        List<Task> outputsEmptyName = new ArrayList<Task>();
        outputsEmptyName.add(new TaskStub("", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), tags));
        ResultStub result = new ResultStub(outputsEmptyName, false, CommandType.ADD,
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
    
    public void test_Add_NullStringName() {
        List<Task> outputsNullStringName = new ArrayList<Task>();
        outputsNullStringName.add(new TaskStub("null", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), tags));
        ResultStub result = new ResultStub(outputsNullStringName, false, CommandType.ADD,
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
    
    public void test_Add_NeedConfirmation() {
        List<Task> outputsNeedConfirmation = new ArrayList<Task>();
        outputsNeedConfirmation.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("21/10/2014", "18:53"), new DateTimeStub(
                        "21/10/2014", "21:00"), tags));
        ResultStub result = new ResultStub(outputsNeedConfirmation, true, CommandType.ADD,
                true, ResultType.TASK);

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Unable to add task. Task coincides with a blocked date.";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic delete
     */
    
    public void test_Delete() {
        ResultStub result = new ResultStub(outputs, true, CommandType.DELETE,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "delete name");
        String expected = "Deleted name";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic deleteAll, asks the user for confirmation
     */
    
    public void test_DeleteAll() {
        ResultStub result = new ResultStub(outputs, true, CommandType.DELETE,
                true, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "delete name");
        String expected = "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic edit
     */
    
    public void test_Edit() {
        Task task = outputs.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputs, true, CommandType.EDIT,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result,
                                                      "edit" + ID);
        String expected = "Edited name";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic display
     */
    
    public void test_Display() {
        ResultStub result = new ResultStub(outputs, true, CommandType.DISPLAY,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "display");
        String expected = "1 task(s) found.";
        assertEquals(expected, actual);
    }
    
    /**
     * Tests display for 20 tasks
     */
    
    public void test_Display_20_Tasks() {
        List<Task> outputsDisplayTest = new ArrayList<Task>();
        for(int index = 0; index < 10; index++){
            outputsDisplayTest.add(new TaskStub("name", new DateTimeStub("", ""),
                    new DateTimeStub("", ""), new DateTimeStub("", ""), tags));
            outputsDisplayTest.add(new TaskStub("email boss", new DateTimeStub("", ""),
                                     new DateTimeStub("", ""), new DateTimeStub("", ""), tags));
        }
        
        ResultStub result = new ResultStub(outputsDisplayTest, true, CommandType.DISPLAY,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "display");
        String expected = "20 task(s) found.";
        assertEquals(expected, actual);
    }

    /**
     * Tests display when there are no tasks to be displayed to the user
     */
    
    public void test_Display_EmptyFile() {
        tearDown();
        ResultStub result = new ResultStub(outputs, true, CommandType.DISPLAY,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "display");
        String expected = "No tasks to show.";
        assertEquals(expected, actual);
        setUp();
    }

    /**
     * Tests generic search
     */
    public void test_Search() {
        ResultStub result = new ResultStub(outputs, true, CommandType.SEARCH,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "search name");
        String expected = "Found 1 match(es).";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic ToDo
     */
    public void test_Todo() {
        Task task = outputs.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputs, true, CommandType.TODO,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "todo" + ID);
        String expected = "Marked name as todo.";
        assertEquals(expected, actual);
    }

    /**
     * Tests generic done
     */
    public void test_Done() {
        Task task = outputs.get(0);
        int ID = task.getId();
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
    public void test_Undo_Block() {
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
    public void test_Redo_Block() {
        ResultStub result = new ResultStub(outputsDate, true, CommandType.REDO,
                false, ResultType.BLOCKDATE);
        String actual = resultGenerator.processResult(result, "redo");
        String expected = "Command Redone.";
        assertEquals(expected, actual);
    }

    
    public void test_Block() {
        ResultStub result = new ResultStub(outputsDate, true,
                CommandType.BLOCK, false, ResultType.BLOCKDATE);
        String actual = resultGenerator
                .processResult(result,
                               "block 1922 21/10/2014 to 1927 21/10/2014");
        String expected = "BLOCKED: 21/10/2014 1922 to 21/10/2014 1927";
        assertEquals(expected, actual);
    }

    
    public void test_Unblock() {
        ResultStub result = new ResultStub(outputsDate, true,
                CommandType.UNBLOCK, false, ResultType.BLOCKDATE);
        String actual = resultGenerator
                .processResult(result,
                               "unblock 1922 21/10/2014 to 1927 21/10/2014");
        String expected = "UNBLOCKED: 21/10/2014 1922 to 21/10/2014 1927";
        assertEquals(expected, actual);
    }
    
    public void test_Restore(){
        Task task = outputs.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputs, true, CommandType.RESTORE, false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "restore" + ID);
        String expected = "Restored name";
        assertEquals(expected, actual);
    }

}
