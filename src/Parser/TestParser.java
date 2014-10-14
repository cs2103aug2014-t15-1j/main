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
                .parse("edit 3 cs2103 delete: name name nil name name n: todo homework delete: name name #cs2103");
        assertEquals("get:type", "EDIT", testEdit.getType().toString());
        assertEquals("get:error", null, testEdit.getError());
        assertEquals("get:name", "cs2103 nil todo homework",
                     testEdit.get("name"));
        assertEquals("get:delete", "name", testEdit.get("delete"));
        assertEquals("get:tags", "[#cs2103]", testEdit.getTags().toString());
    }

    @Test
    public void testCmdAdd() {
        // TODO: Change due, start, end test values to proper date/times
        // TODO: Test date/time cases

        // Empty Add
        String result0 = "\n[[ CMD-ADD: ]]" + "\nname: null" + "\ndue: null"
                         + "\nstart: null" + "\nend: null" + "\ntags: []";
        String cmd0 = Parser.parse("add").toString();
        assertEquals("Add: empty", result0, cmd0);

        // Basic Add ending with a parameter
        String result1 = "\n[[ CMD-ADD: ]]" + "\nname: do homework it's cs2103"
                         + "\ndue: null" + "\nstart: null" + "\nend: null"
                         + "\ntags: [#cs2103]";
        String cmd1 = Parser.parse("add do homework it's #cs2103 cs2103 end:")
                .toString();
        assertEquals("Add: simple, end param", result1, cmd1);

        // Full Add with repeated parameters and consecutive parameters
        String result2 = "\n[[ CMD-ADD: ]]"
                         + "\nname: do homework late quickly" + "\ndue: thurs"
                         + "\nstart: wed 9am" + "\nend: now"
                         + "\ntags: [#cs2103]";
        String cmd2 = Parser
                .parse("add name: do start: #cs2103 wed name: homework late "
                               + "due: start: 9am end: now name: quickly due: thurs\n")
                .toString();
        assertEquals("Add: full, repeated params, consecutive param", result2,
                     cmd2);

        // Full Add with shorthand
        String result2B = "\n[[ CMD-ADD: ]]" + "\nname: do homework"
                          + "\ndue: thurs" + "\nstart: tues" + "\nend: wed"
                          + "\ntags: [#cs2103]";
        String cmd2B = Parser.parse("add do s: #cs2103 tues n: homework "
                                            + "d: thurs e: wed\n").toString();
        assertEquals("Add: full, shorthand", result2B, cmd2B);

        // Add parameters typed without spaces after colons
        String result3 = "\n[[ CMD-ADD: ]]" + "\nname: homework"
                         + "\ndue: null" + "\nstart: null" + "\nend: today"
                         + "\ntags: []";
        String cmd3 = Parser
                .parse("add name:homework start:end:start:end:today")
                .toString();
        assertEquals("Add: no-spaces", result3, cmd3);

        // Full Add with mixed capitals for parameters
        String result4 = "\n[[ CMD-ADD: ]]"
                         + "\nname: do homework late quickly" + "\ndue: thurs"
                         + "\nstart: wed 9am" + "\nend: now"
                         + "\ntags: [#cs2103]";
        String cmd4 = Parser
                .parse("aDD nAMe: do stArt: #cs2103 wed naMe: homework late "
                               + "duE: start: 9am end: now NAME: quickly D: thurs\n")
                .toString();
        assertEquals("Add: full, mixed caps", result4, cmd4);
        
        // Add with mixed capitals for no-space parameters and shorthand
        String result5 = "\n[[ CMD-ADD: ]]" + "\nname: homework"
                         + "\ndue: null" + "\nstart: null" + "\nend: today"
                         + "\ntags: []";
        String cmd5 = Parser
                .parse("add S:e:N:homework staRt:E:S:eNd: today")
                .toString();
        assertEquals("Add: no-spaces, mixed caps", result5, cmd5);

    }

    @Test
    public void testCmdEdit() {
        // Empty Edit
        String result0 = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                         + "\ncmd-info: Invalid id for edit";
        String cmd0 = Parser.parse("edit").toString();
        assertEquals("Edit: empty", result0, cmd0);

        // Edit with words but no id (invalid)
        String result1 = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                         + "\ncmd-info: Invalid id for edit";
        String cmd1 = Parser.parse("edit one two").toString();
        assertEquals("Edit: invalid id", result1, cmd1);

        // Edit with only id
        // TODO: Shouldn't this be invalid?
        String result2 = "\n[[ CMD-EDIT: ]]" + "\nid: 1" + "\nname: null"
                         + "\ndue: null" + "\nstart: null" + "\nend: null"
                         + "\ntags: []" + "\ndelete: null";
        String cmd2 = Parser.parse("edit 1").toString();
        assertEquals("Edit: id only", result2, cmd2);

        // Simple Edit
        String result3 = "\n[[ CMD-EDIT: ]]" + "\nid: 2"
                         + "\nname: do homework" + "\ndue: 23/04/2014"
                         + "\nstart: null" + "\nend: null" + "\ntags: []"
                         + "\ndelete: null";
        String cmd3 = Parser.parse("edit 2 do homework due: 23/04/2014")
                .toString();
        assertEquals("Edit: simple", result3, cmd3);

        // Full Edit with repeated parameters and consecutive parameters
        String result4 = "\n[[ CMD-EDIT: ]]" + "\nid: 3"
                         + "\nname: do homework for CS2103 project"
                         + "\ndue: 23/04/2014" + "\nstart: 22/04/2014"
                         + "\nend: 22/04/2014" + "\ntags: [#CS2103]"
                         + "\ndelete: end";
        String cmd4 = Parser
                .parse("edit 3 do #CS2103 homework due: 23/04/2014 start: 22/04/2014 "
                               + "name: end: 22/04/2014 name: for CS2103 delete: end name: project")
                .toString();
        assertEquals("Edit: full, repeated param, consecutive param", result4,
                     cmd4);

        // Edit with non-delete parameters after delete
        String result5 = "\n[[ CMD-EDIT: ]]" + "\nid: 4"
                         + "\nname: do homework by tonight" + "\ndue: null"
                         + "\nstart: null" + "\nend: null" + "\ntags: []"
                         + "\ndelete: end";
        String cmd5 = Parser.parse("edit 4 do homework delete: end by tonight")
                .toString();
        assertEquals("Edit: delete param", result5, cmd5);

        // Full Edit with shorthand
        String result6 = "\n[[ CMD-EDIT: ]]" + "\nid: 5"
                         + "\nname: do homework for CS2103 project"
                         + "\ndue: 23/04/2014" + "\nstart: 22/04/2014"
                         + "\nend: 22/04/2014" + "\ntags: [#CS2103]"
                         + "\ndelete: end";
        String cmd6 = Parser
                .parse("edit 5 do #CS2103 homework d: 23/04/2014 s: 22/04/2014 "
                               + "n: e: 22/04/2014 n: for CS2103 delete: end n: project")
                .toString();
        assertEquals("Edit: full, shorthand", result6, cmd6);

        // Edit parameters typed without spaces after colons
        String result7 = "\n[[ CMD-EDIT: ]]" + "\nid: 6"
                         + "\nname: do homework for CS2103 project"
                         + "\ndue: 23/04/2014" + "\nstart: 22/04/2014"
                         + "\nend: 22/04/2014" + "\ntags: [#CS2103]"
                         + "\ndelete: end";
        String cmd7 = Parser
                .parse("edit 6 do #CS2103 homework d:23/04/2014 s:22/04/2014 "
                               + "n:e:22/04/2014 n:for CS2103 delete:end n:project")
                .toString();
        assertEquals("Edit: no-space", result7, cmd7);

        // Full Edit with mixed capitals for parameters
        String result8 = "\n[[ CMD-EDIT: ]]" + "\nid: 7"
                         + "\nname: do homework for CS2103 project"
                         + "\ndue: 23/04/2014" + "\nstart: 22/04/2014"
                         + "\nend: 22/04/2014" + "\ntags: [#CS2103]"
                         + "\ndelete: end";
        String cmd8 = Parser
                .parse("edit 7 do #CS2103 homework dUe: 23/04/2014 stARt: 22/04/2014 "
                               + "N:E: 22/04/2014 Name:for CS2103 deLEte: end N:project")
                .toString();
        assertEquals("Edit: mix caps", result8, cmd8);

    }

    @Test
    public void testCmdDelete() {
        // Empty delete (invalid)
        String result0 = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                         + "\ncmd-info: No arguments for delete";
        String cmd0 = Parser.parse("delete").toString();
        assertEquals("Delete: empty, invalid", result0, cmd0);

        // Invalid delete parameter
        String result1 = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                         + "\ncmd-info: Invalid argument for delete";
        String cmd1 = Parser.parse("delete days").toString();
        assertEquals("Delete: invalid", result1, cmd1);

        // Delete all tasks
        String result2 = "\n[[ CMD-Delete: ]]" + "\nrangeType: all"
                         + "\nid: null";
        String cmd2 = Parser.parse("delete all").toString();
        assertEquals("Delete: all", result2, cmd2);

        // Delete searched tasks
        String result3 = "\n[[ CMD-Delete: ]]" + "\nrangeType: search"
                         + "\nid: null";
        String cmd3 = Parser.parse("delete search").toString();
        assertEquals("Delete: search", result3, cmd3);

        // Delete done tasks
        String result4 = "\n[[ CMD-Delete: ]]" + "\nrangeType: done"
                         + "\nid: null";
        String cmd4 = Parser.parse("delete done").toString();
        assertEquals("Delete: done", result4, cmd4);

        // Delete by ID
        String result5 = "\n[[ CMD-Delete: ]]" + "\nrangeType: id" + "\nid: 11";
        String cmd5 = Parser.parse("delete 11").toString();
        assertEquals("Delete: id", result5, cmd5);

        // Delete with mixed caps
        String result6 = "\n[[ CMD-Delete: ]]" + "\nrangeType: done"
                         + "\nid: null";
        String cmd6 = Parser.parse("deLEte dONe").toString();
        assertEquals("Delete: mixed caps (done)", result6, cmd6);

    }
    @Test
    public void testCmdRestore() {
        // Empty Restore
        String result0 = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                         + "\ncmd-info: No arguments for restore";
        String cmd0 = Parser.parse("restore").toString();
        assertEquals("Restore: empty, invalid", result0, cmd0);

        // Invalid Restore
        String result1 = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                         + "\ncmd-info: Invalid argument for restore";
        String cmd1 = Parser.parse("restore b").toString();
        assertEquals("Restore: invalid (word!=all)", result1, cmd1);

        // Restore All
        String result2 = "\n[[ CMD-RESTORE: ]]" + "\nrangeType: all"
                         + "\nid: null";
        String cmd2 = Parser.parse("restore all").toString();
        assertEquals("Restore: all", result2, cmd2);

        // Restore by ID
        String result3 = "\n[[ CMD-RESTORE: ]]" + "\nrangeType: id"
                         + "\nid: 1";
        String cmd3 = Parser.parse("restore 1").toString();
        assertEquals("Restore: id", result3, cmd3);

        // Restore All with mixed caps
        String result4 = "\n[[ CMD-RESTORE: ]]" + "\nrangeType: all"
                         + "\nid: null";
        String cmd4 = Parser.parse("restore aLL").toString();
        assertEquals("Restore: all, mixed caps", result4, cmd4);

    }

    @Test
    public void testCmdHelp() {
        // Invalid help parameter
        String result0 = "\n[[ CMD-HELP: ]]" + "\nfield: invalid";
        String cmd0 = Parser.parse("help me").toString();
        assertEquals("Help: invalid", result0, cmd0);

        // Empty help
        String result1 = "\n[[ CMD-HELP: ]]" + "\nfield: null";
        String cmd1 = Parser.parse("help").toString();
        assertEquals("Help: empty", result1, cmd1);

        // Help All
        String result2 = "\n[[ CMD-HELP: ]]" + "\nfield: all";
        String cmd2 = Parser.parse("help all").toString();
        assertEquals("Help: all", result2, cmd2);

        // Help with command name
        String result3 = "\n[[ CMD-HELP: ]]" + "\nfield: add";
        String cmd3 = Parser.parse("help add").toString();
        assertEquals("Help: command name (add)", result3, cmd3);

        // Help with mixed capitals
        String result4 = "\n[[ CMD-HELP: ]]" + "\nfield: add";
        String cmd4 = Parser.parse("hELp aDd").toString();
        assertEquals("Help: mixed caps (add)", result4, cmd4);

    }
}
