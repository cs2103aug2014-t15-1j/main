package logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import parser.TaskParam;


public class ProcessorUnitTest {
    
    @Before
    public void initialise() {
        Processor.IS_UNIT_TEST = true;
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
        
        //Add with Name, due
        contents.add(due);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with Name, due, start
        contents.add(start);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with Name, due, start, tag
        contents.add(tag);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with Name, due, tag
        contents.remove(start);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with Name, start, tag
        contents.add(start);
        contents.remove(due);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());

        //Add with Name, tag
        contents.remove(start);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with Name, start
        contents.remove(tag);
        contents.add(start);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with start, due
        contents.add(start);
        contents.add(due);
        contents.remove(name);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with start, due, tag
        contents.add(tag);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with start, tag
        contents.remove(due);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with due, tag
        contents.remove(start);
        contents.add(due);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with due
        contents.remove(tag);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //Add with tag
        contents.remove(due);
        contents.add(tag);
        cmd = new CommandAdd(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
    }
    
    @Test
    public void testEditCommand() {
        TaskParamStub id = new TaskParamStub("id", "1");
        TaskParamStub name = new TaskParamStub("name", "Do CS2103 Homework");
        TaskParamStub due = new TaskParamStub("due", "10/10/2014 1200");
        TaskParamStub start = new TaskParamStub("start", "10/10/2014 1200");
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
        
        // - Name
        contents.add(name);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        // - Due
        contents.remove(name);
        contents.add(due);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        // - Start
        contents.remove(due);
        contents.add(start);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());

        // - Tags
        contents.remove(start);
        contents.add(tag);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        // - Delete Due
        contents.remove(tag);
        contents.add(deleteDue);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        // - Delete Start
        contents.remove(deleteDue);
        contents.add(deleteStart);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        // - Delete Tags
        contents.remove(deleteStart);
        contents.add(deleteTags);
        cmd = new CommandEdit(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
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
        
        //      - Valid id
        TaskParamStub rangeType = new TaskParamStub("rangeType", "id");
        contents.add(rangeType);
        TaskParamStub id = new TaskParamStub("id", "1");
        contents.add(id);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        contents.remove(id);
        id = new TaskParamStub("id", "5");
        contents.add(id);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //      - Invalid id
        contents.remove(id);
        id = new TaskParamStub("id", "-1");
        contents.add(id);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertTrue(!result.isSuccess());
        
        contents.remove(id);
        id = new TaskParamStub("id", "0");
        contents.add(id);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertTrue(!result.isSuccess());
        
        // - Delete search
        contents = new ArrayList<TaskParam>();
        rangeType = new TaskParamStub("rangeType", "search");
        contents.add(rangeType);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());

        // - Delete all
        contents = new ArrayList<TaskParam>();
        rangeType = new TaskParamStub("rangeType", "all");
        contents.add(rangeType);
        cmd = new CommandDelete(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        assertTrue(result.needsConfirmation());
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
        //      - Valid id
        TaskParamStub rangeType = new TaskParamStub("rangeType", "id");
        contents.add(rangeType);
        TaskParamStub id = new TaskParamStub("id", "1");
        contents.add(id);
        cmd = new CommandRestore(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //      - Invalid id
        contents.remove(id);
        id = new TaskParamStub("id", "-1");
        contents.add(id);
        cmd = new CommandRestore(contents);
        result = cmd.execute(true);
        assertTrue(!result.isSuccess());
        
        contents.remove(id);
        id = new TaskParamStub("id", "0");
        contents.add(id);
        cmd = new CommandRestore(contents);
        result = cmd.execute(true);
        assertTrue(!result.isSuccess());
        
        // - Restore search
        contents = new ArrayList<TaskParam>();
        rangeType = new TaskParamStub("rangeType", "search");
        contents.add(rangeType);
        cmd = new CommandRestore(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
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
        //      - Valid id
        TaskParamStub rangeType = new TaskParamStub("rangeType", "id");
        contents.add(rangeType);
        cmd = new CommandDone(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //      - Invalid id
        contents.remove(id);
        id = new TaskParamStub("id", "-1");
        contents.add(id);
        cmd = new CommandDone(contents);
        result = cmd.execute(true);
        assertTrue(!result.isSuccess());
        
        contents.remove(id);
        id = new TaskParamStub("id", "0");
        contents.add(id);
        cmd = new CommandDone(contents);
        result = cmd.execute(true);
        assertTrue(!result.isSuccess());

        // - Done using date
        contents = new ArrayList<TaskParam>();
        rangeType = new TaskParamStub("rangeType", "date");
        contents.add(rangeType);
        TaskParamStub date = new TaskParamStub("date", "");
        contents.add(date);
        cmd = new CommandDone(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
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
        //      - Valid id
        TaskParamStub rangeType = new TaskParamStub("rangeType", "id");
        contents.add(rangeType);
        cmd = new CommandTodo(contents);
        result = cmd.execute(true);
        assertTrue(result.isSuccess());
        
        //      - Invalid id
        contents.remove(id);
        id = new TaskParamStub("id", "-1");
        contents.add(id);
        cmd = new CommandTodo(contents);
        result = cmd.execute(true);
        assertTrue(!result.isSuccess());
        
        contents.remove(id);
        id = new TaskParamStub("id", "0");
        contents.add(id);
        cmd = new CommandTodo(contents);
        result = cmd.execute(true);
        assertTrue(!result.isSuccess());
    }
    
    @Test
    public void testBlockCommand() {
        //Block
        List<TaskParam> contents = new ArrayList<TaskParam>();
        CommandBlock cmdBlock = new CommandBlock(contents);
        Result result = cmdBlock.execute(true);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testUnblockCommand() {
        List<TaskParam> contents = new ArrayList<TaskParam>();
        //Unblock
        //      - Valid id
        TaskParamStub id = new TaskParamStub("id", "1");
        contents.add(id);
        CommandUnblock cmdUnblock = new CommandUnblock(contents);
        Result result = cmdUnblock.execute(true);
        assertTrue(result.isSuccess());
        
        //      - Invalid id
        contents.remove(id);
        id = new TaskParamStub("id", "0");
        contents.add(id);
        cmdUnblock = new CommandUnblock(contents);
        result = cmdUnblock.execute(true);
        assertTrue(!result.isSuccess());
        
        contents.remove(id);
        id = new TaskParamStub("id", "-1");
        contents.add(id);
        cmdUnblock = new CommandUnblock(contents);
        result = cmdUnblock.execute(true);
        assertTrue(!result.isSuccess());
    }
}
