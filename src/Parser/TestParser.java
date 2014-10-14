package Parser;

import static org.junit.Assert.*;

import org.junit.Test;

import Logic.Command;
import Storage.Task;

public class TestParser {

    // Consider using object.equals() to test assertEquals()
    @Test
    public void testInvalid() {
        // TEST INVALID COMMAND
        assertEquals("\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                     + "\ncmd-info: Error in initial command parsing", Parser
                .parse("that homework it's #cs2103").toString());
    }

    @Test
    public void testParseToTask() {
        // Test parsing from stored data
        // Pre-cond: the data should have been formatted properly by Storage
        String task1 = tempTaskToString(Parser
                .parseToTask("nAme: do Due: #cs2103 wed namE: homework M: late "
                             + "start: priority: due: 9am eNd: now name: quickly #done\n"));
        String result1 = "\n[[ Task ]]" + "\nName: do homework M: late quickly"
                         + "\nDue: wed 9am" + "\nStart: priority:"
                         + "\nEnd: now" + "\nTags: [#cs2103]"
                         + "\nDoneness: #Done";
        assertEquals(result1, task1);
    }

    // Auxiliary toString() for testParseToTask()
    private static String tempTaskToString(Task task) {
        String result = "\n[[ Task ]]";
        result = result.concat("\nName: " + task.getName());
        result = result.concat("\nDue: " + task.getDue());
        result = result.concat("\nStart: " + task.getStart());
        result = result.concat("\nEnd: " + task.getEnd());
        result = result.concat("\nTags: " + task.getTags());
        result = result.concat("\nDoneness: " +
                               (task.isDone() ? "#Done" : "#ToDo"));
        return result;
    }
    
    @Test
    public void testCmdGetter() {
        // Test getters of a Command subclass (Edit)
        Command testEdit = Parser
                .parse("edit 3 delete: name name nil name name n: todo homework delete: name name #cs2103");
        assertEquals("type", "EDIT", testEdit.getType().toString());
        assertEquals("error", null, testEdit.getError());
        assertEquals("name", "name name nil name name todo homework name name", testEdit.get("name"));
        assertEquals("tags", "[#cs2103]", testEdit.getTags().toString());
    }
    
    @Test
    public void testCmdAdd() {
        // Empty Add
        String result0 = "\n[[ CMD-ADD: ]]" +
                "\nname: null" +
                "\ndue: null" +
                "\nstart: null" +
                "\nend: null" +
                "\ntags: []";
        String cmd0 = Parser.parse("add").toString();
        assertEquals("Add: Empty", result0, cmd0);
        
        // Basic Add ending with a parameter
        String result1 = "\n[[ CMD-ADD: ]]" +
                "\nname: do homework it's cs2103" +
                "\ndue: null" +
                "\nstart: null" +
                "\nend: null" +
                "\ntags: [#cs2103]";
        String cmd1 = Parser.parse("add do homework it's #cs2103 cs2103 end:").toString();
        assertEquals("Add: Simple, End param", result1, cmd1);
        
        // Full Add with repeated parameters and consecutive parameters
        String result2 = "\n[[ CMD-ADD: ]]" +
                "\nname: do homework late quickly" +
                "\ndue: thurs" +
                "\nstart: wed 9am" +
                "\nend: now" +
                "\ntags: [#cs2103]";
        String cmd2 = Parser
                .parse("add name: do start: #cs2103 wed name: homework late "
                       + "due: start: 9am end: now name: quickly due: thurs\n").toString();
        assertEquals("Add: Full, Repeated params, Empty param", result2, cmd2);
        
        // Add parameters typed without spaces after colons
        String result3 = "\n[[ CMD-ADD: ]]" +
                "\nname: homework" +
                "\ndue: null" +
                "\nstart: null" +
                "\nend: today" +
                "\ntags: []";
        String cmd3 = Parser.parse("add name:homework start:end:start:end:today").toString();
        assertEquals("Add: No-spaces", result3, cmd3);
        
    }
    
    

}
