package gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import logic.CommandType;
import objects.DateTimeStub;
import objects.ResultStub;
import objects.TaskStub;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.Task;
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
   public void setUp(){
       tags.add("");
       outputsToDo.add(new TaskStub("name", new DateTimeStub("", ""),
                                new DateTimeStub("", ""), new DateTimeStub("", ""), tags,  TaskType.TODO));
       outputsDate.add(new TaskStub("", new DateTimeStub("06/11/2014", "2200"),
                                    new DateTimeStub("", ""), new DateTimeStub("08/11/2014", "1400"), tags,  TaskType.BLOCK));
       outputsDone.add(new TaskStub("name", new DateTimeStub("06/11/2014", "2200"),
                                    new DateTimeStub("08/11/2014", "1400"), new DateTimeStub("08/11/2014", "1400"), tags,  TaskType.DONE));
   }
   
    @After
    public void tearDown() {
        outputsToDo.clear();
        outputsDate.clear();
        outputsDone.clear();
    }
   
    /**
     * Tests a generic successful add command for the Task ResultType.
     */
    @Test
    public void test_Add() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.ADD,
                false);

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Added name";
        assertEquals(expected, actual);
        
        shell.dispose();
        display.dispose();
    }

    /**
     * Tests a generic unsuccessful add command for the Task. Since
     * ResultGenerator treats all unsucessful commands the same, only one test
     * case has been implemented for all the commands.
     */
    @Test
    public void test_Add_Unsucessful() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        ResultStub result = new ResultStub(outputsToDo, false, CommandType.ADD,
                false);

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Not able to process 'add name'";
        assertEquals(expected, actual);
        
        shell.dispose();
        display.dispose();
    }

    /**
     * Tests an invalid Result Object for add command for Task Result Type. The
     * Result Object has a null for task name. Since ResultGenerator.java treats
     * an invalid Result Object the same for all commands, only one test case
     * for add has been implemented.
     */
    @Test
    public void test_Add_NullName() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        List<TaskStub> outputsNullName = new ArrayList<TaskStub>();
        outputsNullName.add(new TaskStub(null, new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), tags, TaskType.TODO));
        ResultStub result = new ResultStub(outputsNullName, false, CommandType.ADD,
                false);
        try {
            resultGenerator.processResult(result, "add name");
        } catch (NullPointerException error) {
            assertTrue(error.getMessage().contains("Task name is invalid"));
        }
        
        shell.dispose();
        display.dispose();
    }

    /**
     * Tests an invalid Result Object for add command for Task Result Type. The
     * result object has a empty string for task name. Since
     * ResultGenerator.java treats an invalid Result Object the same for all
     * commands, only one test case for add has been implemented.
     */
    @Test
    public void test_Add_EmptyName() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        List<TaskStub> outputsEmptyName = new ArrayList<TaskStub>();
        outputsEmptyName.add(new TaskStub("", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), tags, TaskType.TODO));
        ResultStub result = new ResultStub(outputsEmptyName, false, CommandType.ADD,
                false);
        try {
            resultGenerator.processResult(result, "add name");
        } catch (NullPointerException error) {
            assertTrue(error.getMessage().contains("Task name is invalid"));
        }
        
        shell.dispose();
        display.dispose();
    }

    /**
     * Tests an invalid Result Object for add command for Task Result Type. The
     * result object has a "null" string for task name. Since
     * ResultGenerator.java treats an invalid Result Object the same for all
     * commands, only one test case for add has been implemented.
     */
    @Test
    public void test_Add_NullStringName() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        List<TaskStub> outputsNullStringName = new ArrayList<TaskStub>();
        outputsNullStringName.add(new TaskStub("null", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), tags, TaskType.TODO));
        ResultStub result = new ResultStub(outputsNullStringName, false, CommandType.ADD,
                false);
        try {
            resultGenerator.processResult(result, "add name");
        } catch (NullPointerException error) {
            assertTrue(error.getMessage().contains("Task name is null"));
        }
        

        shell.dispose();
        display.dispose();
    }

    /**
     * Tests when an task to be added command coincides with the a blocked date.
     */
    @Test
    public void test_Add_NeedConfirmation() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);      
        
        List<TaskStub> outputsNeedConfirmation = new ArrayList<TaskStub>();
        outputsNeedConfirmation.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("21/10/2014", "18:53"), new DateTimeStub(
                        "21/10/2014", "21:00"), tags, TaskType.TODO));
        ResultStub result = new ResultStub(outputsNeedConfirmation, true, CommandType.ADD,
                true);

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Unable to add task. Task coincides with a blocked date.";
        assertEquals(expected, actual);
        
        shell.dispose();
        display.dispose();
    }

    /**
     * Tests generic delete
     */
    @Test
    public void test_Delete() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);   
        
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.DELETE,
                false);
        String actual = resultGenerator.processResult(result, "delete name");
        String expected = "Deleted name";
        assertEquals(expected, actual);
        
        shell.dispose();
        display.dispose();
    }

    /**
     * Tests generic deleteAll, asks the user for confirmation
     */
    @Test
    public void test_DeleteAll() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.DELETE,
                true);
        String actual = resultGenerator.processResult(result, "delete name");
        String expected = "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
        assertEquals(expected, actual);
        
        shell.dispose();
        display.dispose();
    }

    /**
     * Tests generic edit
     */
    @Test
    public void test_Edit() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        Task task = outputsToDo.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.EDIT,
                false);
        String actual = resultGenerator.processResult(result,
                                                      "edit" + ID);
        String expected = "Edited name";
        assertEquals(expected, actual);
        

        shell.dispose();
        display.dispose();
    }

    /**
     * Tests generic display
     */
    @Test
    public void test_Display() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.DISPLAY,
                false);
        String actual = resultGenerator.processResult(result, "display");
        String expected = "1 task(s) found.";
        assertEquals(expected, actual);
        
        shell.dispose();
        display.dispose();
    }
    
    /**
     * Tests display for 20 tasks
     */
    @Test
    public void test_Display_20_Tasks() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        List<TaskStub> outputsDisplayTest = new ArrayList<TaskStub>();
        for(int index = 0; index < 10; index++){
            outputsDisplayTest.add(new TaskStub("name", new DateTimeStub("", ""),
                    new DateTimeStub("", ""), new DateTimeStub("", ""), tags, TaskType.TODO));
            outputsDisplayTest.add(new TaskStub("email boss", new DateTimeStub("08/11/2014", "1200"),
                                     new DateTimeStub("15/12/2014", "1800"), new DateTimeStub("19/11/2014", "1400"), tags, TaskType.DONE));

            outputsDisplayTest.add(new TaskStub("", new DateTimeStub("08/11/2014", "1200"),
                                     new DateTimeStub("", ""), new DateTimeStub("11/11/2014", "1700"), tags, TaskType.DONE));
            
            
        }
        
        ResultStub result = new ResultStub(outputsDisplayTest, true, CommandType.DISPLAY,
                false);
        String actual = resultGenerator.processResult(result, "display");
        String expected = "20 task(s) found.";
        assertEquals(expected, actual);
        
        shell.dispose();
        display.dispose();
    }

    /**
     * Tests display when there are no tasks to be displayed to the user
     */
    @Test
    public void test_Display_EmptyFile() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        List<TaskStub> outputEmpty = new ArrayList<TaskStub>();
        ResultStub result = new ResultStub(outputEmpty, true, CommandType.DISPLAY,
                false);
        String actual = resultGenerator.processResult(result, "display");
        String expected = "No tasks to show.";
        assertEquals(expected, actual);
        setUp();
        

        shell.dispose();
        display.dispose();
    }

    /**
     * Tests generic search
     */
    @Test
    public void test_Search() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.SEARCH,
                false);
        String actual = resultGenerator.processResult(result, "search name");
        String expected = "Found 1 match(es).";
        assertEquals(expected, actual);

        shell.dispose();
        display.dispose();
    }

    /**
     * Tests generic ToDo
     */
    @Test
    public void test_Todo() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        Task task = outputsDone.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputsDone, true, CommandType.TODO,
                false);
        String actual = resultGenerator.processResult(result, "todo" + ID);
        String expected = "Marked name as todo.";
        assertEquals(expected, actual);
        

        shell.dispose();
        display.dispose();
    }

    /**
     * Tests generic done
     */
    @Test
    public void test_Done() {

        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        Task task = outputsToDo.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.DONE,
                false);
        String actual = resultGenerator.processResult(result, "Done" + ID);
        String expected = "Marked name as done.";
        assertEquals(expected, actual);
        

        shell.dispose();
        display.dispose();
    }

    /**
     * Tests generic undo. Undo a command related to a task
     * e.g. add, edit, delete
     */
    @Test
    public void test_Undo() {

        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
       ResultStub result = new ResultStub(outputsDone, true, CommandType.UNDO,
                                          false);
        String actual = resultGenerator.processResult(result, "undo");
        String expected = "Command Undone.";
        assertEquals(expected, actual);
        

        shell.dispose();
        display.dispose();
    }

    /**
     * Test generic redo. Redo a command related to task
     * e.g. add, edit, delete
     */
    @Test
    public void test_Redo() {

        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.REDO,
                false);
        String actual = resultGenerator.processResult(result, "redo");
        String expected = "Command Redone.";
        assertEquals(expected, actual);
        
        shell.dispose();
        display.dispose();
    }

    @Test
    public void test_Block() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        ResultStub result = new ResultStub(outputsDate, true,
                CommandType.BLOCK, false);
        String actual = resultGenerator
                .processResult(result,
                               "block 06/11/2014 2200 to 08/11/2014 1400");
        String expected = "BLOCKED: 06/11/2014 2200 to 08/11/2014 1400";
        assertEquals(expected, actual);

        shell.dispose();
        display.dispose();
    }

    @Test
    public void test_Unblock() {
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        ResultStub result = new ResultStub(outputsDate, true,
                CommandType.UNBLOCK, false);
        String actual = resultGenerator
                .processResult(result,
                               "unblock 06/11/2014 2200 to 08/11/2014 1400");
        String expected = "UNBLOCKED: 06/11/2014 2200 to 08/11/2014 1400";
        assertEquals(expected, actual);
        
        shell.dispose();
        display.dispose();
    }
    
    @Test
    public void test_Restore(){
        Display display = new Display();
        Shell shell = new Shell(display);
        MainScreen.runProgram(shell);
        
        Task task = outputsToDo.get(0);
        int ID = task.getId();
        ResultStub result = new ResultStub(outputsToDo, true, CommandType.RESTORE, false);
        String actual = resultGenerator.processResult(result, "restore" + ID);
        String expected = "Restored name";
        assertEquals(expected, actual);
        
        shell.dispose();
        display.dispose();
    }

}
