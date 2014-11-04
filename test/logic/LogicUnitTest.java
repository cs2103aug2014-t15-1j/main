package logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import objects.TaskParamStub;

import org.junit.Before;
import org.junit.Test;

import parser.TaskParam;


public class LogicUnitTest {
    
    public Processor processor;
    
    @Before
    public void initialise() {
        Processor.IS_UNIT_TEST = true;
        processor = Processor.getInstance();
        Processor.reset();
    }
    
    @Test
    public void testAddCommand() {
        TaskParamStub name = new TaskParamStub("name", "Do CS2103 Homework");
        TaskParamStub due = new TaskParamStub("due", "10/10/2014 1200");
        TaskParamStub start = new TaskParamStub("start", "09/10/2014 1200");
        TaskParamStub tag = new TaskParamStub("tag", "#CS2103");
        List<TaskParam> contents = new ArrayList<TaskParam>();
        
        //Add with Name
        CommandAdd cmd = new CommandAdd(contents);
        Result result = cmd.execute(true);
        contents.add(name);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with Name, due
        contents.add(due);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with Name, due, start
        contents.add(start);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with Name, due, start, tag
        contents.add(tag);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Test Retrieval of Information
        assertTrue(cmd.get("name").equals("Do CS2103 Homework"));
        assertTrue(cmd.get("start").equals("09/10/2014 1200"));
        assertTrue(cmd.get("due").equals("10/10/2014 1200"));
        assertTrue(cmd.getTags().contains("#CS2103"));
        assertNull(cmd.get("random"));
        
        //Other combinations of Task Parameters
        
        //Add with Name, due, tag
        contents.remove(start);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with Name, start, tag
        contents.add(start);
        contents.remove(due);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());

        //Add with Name, tag
        contents.remove(start);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with Name, start
        contents.remove(tag);
        contents.add(start);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with start, due
        contents.add(start);
        contents.add(due);
        contents.remove(name);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with start, due, tag
        contents.add(tag);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with start, tag
        contents.remove(due);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with due, tag
        contents.remove(start);
        contents.add(due);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with due
        contents.remove(tag);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Add with tag
        contents.remove(due);
        contents.add(tag);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Test to String
        assertNotNull(cmd.toString());
        
    }
    
    @Test
    public void testEditCommand() {
        TaskParamStub id = new TaskParamStub("id", "1");
        TaskParamStub name = new TaskParamStub("name", "Do CS2103 Homework");
        TaskParamStub due = new TaskParamStub("due", "10/10/2014 1200");
        TaskParamStub start = new TaskParamStub("start", "09/10/2014 1200");
        TaskParamStub tag = new TaskParamStub("tag", "#CS2103");
        List<TaskParam> contents = new ArrayList<TaskParam>();
        TaskParamStub deleteDue = new TaskParamStub("delete", "due");
        TaskParamStub deleteStart = new TaskParamStub("delete", "start");
        TaskParamStub deleteTags = new TaskParamStub("delete", "tags");
        
        //Expect: Fails when ID is not given
        CommandEdit cmd = new CommandEdit(contents);
        Result result = cmd.execute(true);
        assertFalse(result.isSuccess());
        
        //Expect: Successes
        
        // - Id
        contents.add(id);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        assertTrue(cmd.get("id").equals("1"));
        
        // - Name
        contents.add(name);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Test Retrieval of Information - name
        assertTrue(cmd.get("name").equals("Do CS2103 Homework"));
        
        // - Due
        contents.remove(name);
        contents.add(due);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Test Retrieval of Information - due
        assertTrue(cmd.get("due").equals("10/10/2014 1200"));
        
        // - Start
        contents.remove(due);
        contents.add(start);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Test Retrieval of Information - start
        assertTrue(cmd.get("start").equals("09/10/2014 1200"));

        // - Tags
        contents.remove(start);
        contents.add(tag);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Test Retrieval Information - Tags
        assertTrue(cmd.getTags().contains("#CS2103"));
        
        // - Delete Due
        contents.remove(tag);
        contents.add(deleteDue);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        // - Delete Start
        contents.remove(deleteDue);
        contents.add(deleteStart);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------   Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        // - Delete Tags
        contents.remove(deleteStart);
        contents.add(deleteTags);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Test to String
        assertNotNull(cmd.toString());
    }
    
    @Test
    public void testDeleteCommand() {
        List<TaskParam> contents = new ArrayList<TaskParam>();
        CommandDelete cmd = new CommandDelete(contents);
        Result result = cmd.execute(true);

        //Expect: Fails when no parameter is not given
        assertFalse(result.isSuccess());

        //Expect: Success
        
        // - Delete using <id>
        
        // ------ Valid id: 1
        TaskParamStub rangeType = new TaskParamStub("rangeType", "id");
        contents.add(rangeType);
        TaskParamStub id = new TaskParamStub("id", "1");
        contents.add(id);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        assertTrue(cmd.get("rangeType").equals("id"));
        assertTrue(cmd.get("id").equals("1"));
        
        // ------ Valid id (Positive id)
        contents.remove(id);
        id = new TaskParamStub("id", "20");
        contents.add(id);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        // ------ Invalid id (Negative id)
        contents.remove(id);
        id = new TaskParamStub("id", "-1");
        contents.add(id);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertFalse(result.isSuccess());
        
        // ------ Invalid id (Zero)
        contents.remove(id);
        id = new TaskParamStub("id", "0");
        contents.add(id);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertFalse(result.isSuccess());
        
        // - Delete search
        contents = new ArrayList<TaskParam>();
        rangeType = new TaskParamStub("rangeType", "search");
        contents.add(rangeType);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement  
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        assertTrue(cmd.get("rangeType").equals("search"));
        
        // - Delete all
        contents = new ArrayList<TaskParam>();
        rangeType = new TaskParamStub("rangeType", "all");
        contents.add(rangeType);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        assertTrue(result.needsConfirmation());
        assertTrue(cmd.get("rangeType").equals("all"));
        
        assertNotNull(cmd.toString());
    }
    
    @Test
    public void testRestoreCommand() {
        List<TaskParam> contents = new ArrayList<TaskParam>();
        CommandRestore cmd = new CommandRestore(contents);
        Result result = cmd.execute(true);

        //Expect: Fails when no parameter is not given
        assertFalse(result.isSuccess());

        //Expect: Success
        
        // - Restore using <id>
        // ------ Valid id
        TaskParamStub rangeType = new TaskParamStub("rangeType", "id");
        contents.add(rangeType);
        TaskParamStub id = new TaskParamStub("id", "1");
        contents.add(id);
        cmd = new CommandRestore(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        assertTrue(cmd.get("rangeType").equals("id"));
        assertTrue(cmd.get("id").equals("1"));
        
        // ------ Invalid id (Negative value)
        contents.remove(id);
        id = new TaskParamStub("id", "-1");
        contents.add(id);
        cmd = new CommandRestore(contents);
        result = cmd.execute(true);
        assertFalse(result.isSuccess());
        
        // ------ Invalid id (Zero value)
        contents.remove(id);
        id = new TaskParamStub("id", "0");
        contents.add(id);
        cmd = new CommandRestore(contents);
        result = cmd.execute(true);
        assertFalse(result.isSuccess());
        
        // - Restore search
        contents = new ArrayList<TaskParam>();
        rangeType = new TaskParamStub("rangeType", "search");
        contents.add(rangeType);
        cmd = new CommandRestore(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        assertTrue(cmd.get("rangeType").equals("search"));
        
        assertNotNull(cmd.toString());
    }
    
    @Test
    public void testDoneCommand() {
        List<TaskParam> contents = new ArrayList<TaskParam>();
        CommandDone cmd = new CommandDone(contents);
        Result result = cmd.execute(true);

        //Expect: Fails when insufficient parameter is given
        assertFalse(result.isSuccess());
        TaskParamStub id = new TaskParamStub("id", "1");
        contents.add(id);
        assertFalse(result.isSuccess());
        
        //Expect: Successes
        // - Done using <id>
        // ------ Valid id (Positive value > 1)
        TaskParamStub rangeType = new TaskParamStub("rangeType", "id");
        contents.add(rangeType);
        cmd = new CommandDone(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        assertTrue(cmd.get("rangeType").equals("id"));
        assertTrue(cmd.get("id").equals("1"));
        
        // ------ Invalid id (Negative)
        contents.remove(id);
        id = new TaskParamStub("id", "-1");
        contents.add(id);
        cmd = new CommandDone(contents);
        result = cmd.execute(true);
        assertFalse(result.isSuccess());
        
        // ------ Invalid id (Zero value)
        contents.remove(id);
        id = new TaskParamStub("id", "0");
        contents.add(id);
        cmd = new CommandDone(contents);
        result = cmd.execute(true);
        assertFalse(result.isSuccess());

        // - Done using date
        contents = new ArrayList<TaskParam>();
        rangeType = new TaskParamStub("rangeType", "date");
        contents.add(rangeType);
        TaskParamStub date = new TaskParamStub("date", "");
        contents.add(date);
        cmd = new CommandDone(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        assertTrue(cmd.get("rangeType").equals("date"));
        assertNotNull(cmd.get("date"));
        
        assertNotNull(cmd.toString());
    }
    
    @Test
    public void testTodoCommand() {
        List<TaskParam> contents = new ArrayList<TaskParam>();
        CommandTodo cmd = new CommandTodo(contents);
        Result result = cmd.execute(true);

        //Expect: Fails when insufficient parameter is given
        assertFalse(result.isSuccess());
        TaskParamStub id = new TaskParamStub("id", "1");
        contents.add(id);
        assertFalse(result.isSuccess());
        
        //Expect: Successes
        // - Done using <id>
        // ------ Valid id
        TaskParamStub rangeType = new TaskParamStub("rangeType", "id");
        contents.add(rangeType);
        cmd = new CommandTodo(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        // ------ Invalid id (Negative value)
        contents.remove(id);
        id = new TaskParamStub("id", "-1");
        contents.add(id);
        cmd = new CommandTodo(contents);
        result = cmd.execute(true);
        assertFalse(result.isSuccess());
        
        // ------ Invalid id (Zero value)
        contents.remove(id);
        id = new TaskParamStub("id", "0");
        contents.add(id);
        cmd = new CommandTodo(contents);
        result = cmd.execute(true);
        assertFalse(result.isSuccess());

        assertNotNull(cmd.toString());
    }
    
    @Test
    public void testBlockCommand() {
        //Block
        List<TaskParam> contents = new ArrayList<TaskParam>();
        TaskParamStub start = new TaskParamStub("start", "11/10/2014 0000");
        TaskParamStub end = new TaskParamStub("end", "12/10/2014 2359");
        contents.add(start);
        contents.add(end);
        //Test blocking of date range
        CommandBlock cmd = new CommandBlock(contents);
        Result result = cmd.execute(true);
        assertTrue(result.isSuccess());
        // ------ Test path for complement
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        
        //Test Retrieval of Information
        assertTrue(cmd.get("start").equals("11/10/2014 0000"));
        assertTrue(cmd.get("end").equals("12/10/2014 2359"));

        assertNotNull(cmd.toString());
    }

    @Test
    public void testUnblockCommand() {
        List<TaskParam> contents = new ArrayList<TaskParam>();
        //Unblock
        // ------ Valid id (Positive values > 0)
        TaskParamStub id = new TaskParamStub("id", "1");
        contents.add(id);
        CommandUnblock cmd = new CommandUnblock(contents);
        Result result = cmd.execute(true);
        assertTrue(result.isSuccess());
        result = cmd.executeComplement();
        assertTrue(result.isSuccess());
        assertTrue(cmd.get("id").equals("1"));
        
        // ------ Invalid id (Zero Value)
        contents.remove(id);
        id = new TaskParamStub("id", "0");
        contents.add(id);
        cmd = new CommandUnblock(contents);
        result = cmd.execute(true);
        assertFalse(result.isSuccess());
        
        // ------ Invalid id (Negative Value)
        contents.remove(id);
        id = new TaskParamStub("id", "-1");
        contents.add(id);
        cmd = new CommandUnblock(contents);
        result = cmd.execute(true);
        assertFalse(result.isSuccess());

        assertNotNull(cmd.toString());
    }
    
    @Test
    public void testOthers() {
        //Test creation of CommandOther object
        CommandOthers cmd = new CommandOthers("help");
        Result result = cmd.execute(true);
        assertTrue(result.isSuccess());
        cmd = new CommandOthers("reset");
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        cmd = new CommandOthers("undo");
        result = cmd.execute(true);
        assertFalse(result.isSuccess());
        cmd = new CommandOthers("redo");
        result = cmd.execute(true);
        assertFalse(result.isSuccess());
        cmd = new CommandOthers("exit");
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        assertNotNull(cmd.toString());
    }
    
    @Test
    public void testSearch() {
        //Test search
        List<TaskParam> contents = new ArrayList<TaskParam>();
        TaskParamStub status = new TaskParamStub("status", "todo");
        TaskParamStub date = new TaskParamStub("date", "09/10/2014");
        TaskParamStub tag = new TaskParamStub("tag", "#CS2103");
        TaskParamStub word = new TaskParamStub("word", "homework");
        contents.add(status);
        contents.add(date);
        contents.add(tag);
        contents.add(word);
        
        CommandSearch cmd = new CommandSearch(contents);
        Result result = cmd.execute(true);
        assertTrue(result.isSuccess());
        assertTrue(cmd.getTags().contains("#CS2103"));
        assertTrue(cmd.getKeywords().contains("homework"));
        assertNotNull(cmd.toString());
    }
    
    @Test
    public void testDisplay() {
        //Test creation of CommandDisplay object
        List<TaskParam> contents = new ArrayList<TaskParam>();
        TaskParamStub rangeType = new TaskParamStub("rangeType", "todo");
        contents.add(rangeType);
        CommandDisplay cmd = new CommandDisplay(contents);
        Result result = cmd.execute(true);
        
        //RangeType of Search
        contents.remove(rangeType);
        rangeType = new TaskParamStub("rangeType", "search");
        contents.add(rangeType);
        cmd = new CommandDisplay(contents);
        result = cmd.execute(true);
        
        //RangeType of Block Dates
        contents.remove(rangeType);
        rangeType = new TaskParamStub("rangeType", "block");
        contents.add(rangeType);
        cmd = new CommandDisplay(contents);
        result = cmd.execute(true);
        
        //RangeType of Done Tasks
        contents.remove(rangeType);
        rangeType = new TaskParamStub("rangeType", "done");
        contents.add(rangeType);
        cmd = new CommandDisplay(contents);
        result = cmd.execute(true);
        
        //RangeType of Deleted Tasks
        contents.remove(rangeType);
        rangeType = new TaskParamStub("rangeType", "deleted");
        contents.add(rangeType);
        cmd = new CommandDisplay(contents);
        result = cmd.execute(true);
        
        //RangeType of All Tasks
        contents.remove(rangeType);
        rangeType = new TaskParamStub("rangeType", "all");
        contents.add(rangeType);
        cmd = new CommandDisplay(contents);
        result = cmd.execute(true);
        
        //RangeType of id
        contents.remove(rangeType);
        rangeType = new TaskParamStub("rangeType", "id");
        contents.add(rangeType);
        TaskParamStub id = new TaskParamStub("id", "1");        
        contents.add(rangeType);
        contents.add(id);
        cmd = new CommandDisplay(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        assertNotNull(cmd.toString());
    }
    
    @Test
    public void testFetchKeys() {
        assertTrue(processor.fetchInputDownKey() instanceof String);
        assertTrue(processor.fetchInputUpKey() instanceof String);
    }
}
