package gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import logic.CommandType;
import logic.Result.ResultType;

import org.junit.Test;

public class ResultGeneratorTest {

    @Test
    public void test_Add() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.ADD,
                false, ResultType.TASK);

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Added name";
        assertEquals(expected, actual);
    }

    @Test
    public void test_Add_Unsucessful() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, false, CommandType.ADD,
                false, ResultType.TASK);

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Not able to process 'add name'";
        assertEquals(expected, actual);
    }

    @Test
    public void test_Add_EmptyName() {

        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        outputs.add(new TaskStub(null, new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, false, CommandType.ADD,
                false, ResultType.TASK);
        try {
            resultGenerator.processResult(result, "add name");
        } catch (NullPointerException error) {
            assertTrue(error.getMessage().contains("Task name is null"));
        }
    }

    @Test
    public void test_Add_NeedConfirmation() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("21/10/2014", "18:53"), new DateTimeStub(
                        "21/10/2014", "21:00"), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.ADD,
                true, ResultType.TASK);

        String actual = resultGenerator.processResult(result, "add name");
        String expected = "Unable to add task. Task coincides with a blocked date. Key 'y' to override date or 'n' to abort";
        assertEquals(expected, actual);
    }

    @Test
    public void test_Delete() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.DELETE,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "delete name");
        String expected = "Deleted name";
        assertEquals(expected, actual);
    }

    @Test
    public void test_DeleteAll() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.DELETE,
                true, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "delete name");
        String expected = "This will erase all data, PERMANENTLY.  Key 'y' to continue or 'n' to abort";
        assertEquals(expected, actual);
    }

    @Test
    public void test_Edit() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
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

    @Test
    public void test_Display() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.DISPLAY,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "display");
        String expected = "1 task(s) found.";
        assertEquals(expected, actual);
    }

    @Test
    public void test_Display_EmptyFile() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        ResultStub result = new ResultStub(outputs, true, CommandType.DISPLAY,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "display");
        String expected = "No tasks to show.";
        assertEquals(expected, actual);
    }

    @Test
    public void test_Search() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        outputs.add(new TaskStub("name", new DateTimeStub("", ""),
                new DateTimeStub("", ""), new DateTimeStub("", ""), null));
        ResultStub result = new ResultStub(outputs, true, CommandType.SEARCH,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "search name");
        String expected = "Found 1 match(es).";
        assertEquals(expected, actual);
    }

    @Test
    public void test_Todo() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
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

    @Test
    public void test_Done() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
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

    @Test
    public void test_Undo() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        ResultStub result = new ResultStub(outputs, true, CommandType.UNDO,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "undo");
        String expected = "Command Undone.";
        assertEquals(expected, actual);
    }

    @Test
    public void test_Redo() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<TaskStub> outputs = new ArrayList<TaskStub>();
        ResultStub result = new ResultStub(outputs, true, CommandType.REDO,
                false, ResultType.TASK);
        String actual = resultGenerator.processResult(result, "redo");
        String expected = "Command Redone.";
        assertEquals(expected, actual);
    }

    @Test
    public void test_Block() {
        ResultGenerator resultGenerator = new ResultGenerator();
        ArrayList<BlockDateStub> outputs = new ArrayList<BlockDateStub>();
        outputs.add(new BlockDateStub(new DateTimeStub("21/10/2014", "1922"),
                new DateTimeStub("21/10/2014", "1927")));
        ResultStub result = new ResultStub(outputs, true, CommandType.BLOCK,
                false, ResultType.BLOCKDATE);
        String actual = resultGenerator
                .processResult(result,
                               "block 1922 21/10/2014 to 1927 21/10/2014");
        String expected = "BLOCKED: 21/10/2014 1922 to 21/10/2014 1927";
        assertEquals(expected, actual);
    }
}
