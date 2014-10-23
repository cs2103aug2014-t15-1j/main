package parser;

import static org.junit.Assert.*;
import logic.Command;

import org.junit.Test;

import database.BlockDate;
import database.DateTime;
import database.Task;

public class TestParser {

    // Consider using object.equals() to test assertEquals()
    // Rejected: Errors in constructor will not be caught
    
    
    @Test
    public void testInvalid() {
        System.out.println("\n>> Testing invalid commands...");

        // TEST INVALID COMMAND
        assertEquals("\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                     + "\ncmd-info: Error in initial command parsing", Parser
                .parse("that homework it's #cs2103").toString());

        // Empty Command
        assertEquals("\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                     + "\ncmd-info: Error in initial command parsing", Parser
                .parse("").toString());
        
        System.out.println("...success!");
    }
    
    @Test (expected=AssertionError.class)
    public void failInvalid() {
        System.out.println("\n>> Failing invalid commands...");

        Parser.parse(null);

        System.out.println("...success!");
    }

    @Test
    public void testParseToTask() {
        System.out.println("\n>> Testing parseToTask()...");

        // Test parsing from stored data
        // Pre-cond: the data should have been formatted properly by Storage

        String result;
        String task;

        task = tempTaskToString(Parser
                .parseToTask("nAme: do Due: #cs2103 wed namE: homework M: late "
                             + "start: priority: due: 9am eNd: now name: quickly #done\n"));
        result = "\n[[ Task ]]" + "\nName: do homework M: late quickly"
                 + "\nDue: null" + "\nStart: null" + "\nEnd: null"
                 + "\nTags: [#cs2103]" + "\nDoneness: #done";
        assertEquals(result, task);

        task = tempTaskToString(Parser
                .parseToTask("nAme: do Due: #cs2103 namE: homework M: late "
                             + "start: 23/04/2014 0300 due: 24/04/2014 eNd: 23/04/2014 0500 name: quickly #done\n"));
        result = "\n[[ Task ]]" + "\nName: do homework M: late quickly"
                 + "\nDue: 24/04/2014" + "\nStart: 23/04/2014 0300"
                 + "\nEnd: 23/04/2014 0500" + "\nTags: [#cs2103]"
                 + "\nDoneness: #done";
        assertEquals(result, task);

        System.out.println("...success!");
    }

    private static String tempTaskToString(Task task) {
        String fullInfo = "\n[[ Task ]]";
        fullInfo += "\nName: " + task.getName();
        fullInfo += "\nDue: " + task.getDue();
        fullInfo += "\nStart: " + task.getStart();
        fullInfo += "\nEnd: " + task.getEnd();
        fullInfo += "\nTags: " + task.getTags();
        fullInfo += "\nDoneness: ";
        fullInfo += task.isDone() ? "#done" : "#todo";

        return fullInfo;
    }

    @Test
    public void testCmdGetter() {
        System.out.println("\n>> Testing Command Getters...");
        // Test getters of a Command subclass (Edit)
        Command testEdit = Parser
                .parse("edit 3 cs2103 delete: name name nil name name n: todo homework delete: name name #cs2103");
        assertEquals("get:type", "EDIT", testEdit.getType().toString());
        assertEquals("get:error", null, testEdit.getError());
        assertEquals("get:name", "cs2103 nil todo homework",
                     testEdit.get("name"));
        assertEquals("get:delete", "name", testEdit.get("delete"));
        assertEquals("get:tags", "[#cs2103]", testEdit.getTags().toString());

        System.out.println("...success!");
    }

    @Test
    public void testCmdHelp() {
        System.out.println("\n>> Testing Help Command...");

        String result;
        String cmd;

        // Empty Help
        result = "\n[[ CMD-HELP: ]]" + "\nfield: null";
        cmd = Parser.parse("help").toString();
        assertEquals("Help: empty", result, cmd);

        // Help All
        result = "\n[[ CMD-HELP: ]]" + "\nfield: all";
        cmd = Parser.parse("help all").toString();
        assertEquals("Help: all", result, cmd);

        // Help with command name + mixed caps
        result = "\n[[ CMD-HELP: ]]" + "\nfield: add";
        cmd = Parser.parse("hELp adD").toString();
        assertEquals("Help: command name (add)", result, cmd);

        // Invalid help parameter
        result = "\n[[ CMD-HELP: ]]" + "\nfield: invalid";
        cmd = Parser.parse("help me").toString();
        assertEquals("Help: invalid", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdAdd() {
        System.out.println("\n>> Testing Add Command...");
        // TODO: Change due, start, end test values to proper date/times
        // TODO: Test date/time cases

        // Ignore Add for now

        String result;
        String cmd;

        // Empty Add
        result = "\n[[ CMD-ADD: ]]" + "\nname: null" + "\ndue: " + "\nstart: "
                 + "\nend: " + "\ntags: []";
        cmd = Parser.parse("add").toString();
        assertEquals("Add: empty", result, cmd);

        // Basic Add ending with a parameter
        result = "\n[[ CMD-ADD: ]]" + "\nname: do homework it's cs2103"
                 + "\ndue: " + "\nstart: " + "\nend: " + "\ntags: [#cs2103]";
        cmd = Parser.parse("add do homework it's #cs2103 cs2103 end:")
                .toString();
        assertEquals("Add: simple, end param", result, cmd);

        // Full Add with repeated parameters and consecutive parameters
        result = "\n[[ CMD-ADD: ]]" + "\nname: do homework late quickly"
                 + "\ndue: " + "\nstart: " + "\nend: " + "\ntags: [#cs2103]";
        cmd = Parser
                .parse("add name: do start: #cs2103 wed name: homework late "
                               + "due: start: 0900 end: now name: quickly due: thurs\n")
                .toString();
        assertEquals("Add: full, repeated params, consecutive param", result,
                     cmd);

        // Full Add with shorthand
        result = "\n[[ CMD-ADD: ]]" + "\nname: do homework"
                 + "\ndue: 24/04/2014" + "\nstart: 22/04/2014"
                 + "\nend: 23/04/2014" + "\ntags: [#cs2103]";
        cmd = Parser.parse("add do s: #cs2103 23/04/2014 n: homework "
                                   + "d: 24/04/2014 e: 22/04/2014\n")
                .toString();
        assertEquals("Add: full, shorthand", result, cmd);

        // Add parameters typed without spaces after colons
        result = "\n[[ CMD-ADD: ]]" + "\nname: homework" + "\ndue: "
                 + "\nstart: " + "\nend: 23/04/2014" + "\ntags: []";
        cmd = Parser.parse("add name:homework start:end:start:end:23/04/2014")
                .toString();
        assertEquals("Add: no-spaces", result, cmd);

        // Full Add with mixed capitals for parameters
        result = "\n[[ CMD-ADD: ]]" + "\nname: do homework late quickly"
                 + "\ndue: 25/04/2014 2359" + "\nstart: 23/04/2014 0300"
                 + "\nend: 24/04/2014 0400" + "\ntags: [#cs2103]";
        cmd = Parser
                .parse("aDD nAMe: do stArt: #cs2103 naMe: homework late "
                               + "duE: 2359 start: 23/04/2014 0300 end: 0400 24/04/2014 "
                               + "NAME: quickly D: 25/04/2014\n").toString();
        assertEquals("Add: full, mixed caps", result, cmd);

        // Add with mixed capitals for no-space parameters and shorthand
        // And a space after no-space parameters
        result = "\n[[ CMD-ADD: ]]" + "\nname: homework" + "\ndue: "
                 + "\nstart: " + "\nend: 23/04/2014" + "\ntags: []";
        cmd = Parser.parse("add S:e:N:homework staRt:E:S:eNd: 23/04/2014")
                .toString();
        assertEquals("Add: no-spaces, mixed caps", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdEdit() {
        System.out.println("\n>> Testing Edit Command...");

        String result;
        String cmd;

        // Empty Edit
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: Invalid id for edit";
        cmd = Parser.parse("edit").toString();
        assertEquals("Edit: empty", result, cmd);

        // Edit with words but no id (invalid)
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: Invalid id for edit";
        cmd = Parser.parse("edit one two").toString();
        assertEquals("Edit: invalid id", result, cmd);

        // Edit with only id
        // TODO: Shouldn't this be invalid?
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 1" + "\nname: null" + "\ndue: "
                 + "\nstart: " + "\nend: " + "\ntags: []" + "\ndelete: null";
        cmd = Parser.parse("edit 1").toString();
        assertEquals("Edit: id only", result, cmd);

        // Simple Edit
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 2" + "\nname: do homework"
                 + "\ndue: 23/04/2014" + "\nstart: " + "\nend: " + "\ntags: []"
                 + "\ndelete: null";
        cmd = Parser.parse("edit 2 do homework due: 23/04/2014").toString();
        assertEquals("Edit: simple", result, cmd);

        // CURRENTLY HAS AN ERROR WITH TIME IN DATETIME
        // Full Edit with repeated parameters and consecutive parameters
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 3"
                 + "\nname: do homework for CS2103 project"
                 + "\ndue: 23/04/2014" + "\nstart: 22/04/2014 0200"
                 + "\nend: 22/04/2014 0200" + "\ntags: [#CS2103]"
                 + "\ndelete: end";
        cmd = Parser
                .parse("edit 3 do #CS2103 homework due: 23/04/2014 start: 22/04/2014 0200 "
                               + "name: end: 22/04/2014 0200 name: for CS2103 delete: end name: project")
                .toString();
        assertEquals("Edit: full, repeated param, consecutive param", result,
                     cmd);

        // Edit with non-delete parameters after delete
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 4"
                 + "\nname: do homework by tonight" + "\ndue: " + "\nstart: "
                 + "\nend: " + "\ntags: []" + "\ndelete: end";
        cmd = Parser.parse("edit 4 do homework delete: end by tonight")
                .toString();
        assertEquals("Edit: delete param", result, cmd);

        // Full Edit with shorthand
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 5"
                 + "\nname: do homework for CS2103 project"
                 + "\ndue: 23/04/2014" + "\nstart: 22/04/2014"
                 + "\nend: 22/04/2014" + "\ntags: [#CS2103]" + "\ndelete: end";
        cmd = Parser
                .parse("edit 5 do #CS2103 homework d: 23/04/2014 s: 22/04/2014 "
                               + "n: e: 22/04/2014 n: for CS2103 delete: end n: project")
                .toString();
        assertEquals("Edit: full, shorthand", result, cmd);

        // Edit parameters typed without spaces after colons
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 6"
                 + "\nname: do homework for CS2103 project"
                 + "\ndue: 23/04/2014" + "\nstart: 22/04/2014"
                 + "\nend: 22/04/2014" + "\ntags: [#CS2103]" + "\ndelete: name";
        cmd = Parser
                .parse("edit 6 do #CS2103 homework d:23/04/2014 s:22/04/2014 "
                               + "n:e:22/04/2014 n:for CS2103 delete:name n:project")
                .toString();
        assertEquals("Edit: no-space", result, cmd);

        // Full Edit with mixed capitals for parameters
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 7"
                 + "\nname: do homework for CS2103 project"
                 + "\ndue: 23/04/2014" + "\nstart: 22/04/2014"
                 + "\nend: 22/04/2014" + "\ntags: [#CS2103]" + "\ndelete: end";
        cmd = Parser
                .parse("edit 7 do #CS2103 homework dUe: 23/04/2014 stARt: 22/04/2014 "
                               + "N:E: 22/04/2014 Name:for CS2103 deLEte: end N:project")
                .toString();
        assertEquals("Edit: mix caps", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdDelete() {
        System.out.println("\n>> Testing Delete Command...");

        String result;
        String cmd;

        // Empty delete (invalid)
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: No arguments for delete";
        cmd = Parser.parse("delete").toString();
        assertEquals("Delete: empty, invalid", result, cmd);

        // Invalid delete parameter
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: Invalid argument for delete";
        cmd = Parser.parse("delete days").toString();
        assertEquals("Delete: invalid", result, cmd);

        // Delete all tasks
        result = "\n[[ CMD-Delete: ]]" + "\nrangeType: all" + "\nid: null";
        cmd = Parser.parse("delete all").toString();
        assertEquals("Delete: all", result, cmd);

        // Delete searched tasks
        result = "\n[[ CMD-Delete: ]]" + "\nrangeType: search" + "\nid: null";
        cmd = Parser.parse("delete search").toString();
        assertEquals("Delete: search", result, cmd);

        // Delete done tasks
        result = "\n[[ CMD-Delete: ]]" + "\nrangeType: done" + "\nid: null";
        cmd = Parser.parse("delete done").toString();
        assertEquals("Delete: done", result, cmd);

        // Delete by ID
        result = "\n[[ CMD-Delete: ]]" + "\nrangeType: id" + "\nid: 11";
        cmd = Parser.parse("delete 11").toString();
        assertEquals("Delete: id", result, cmd);

        // Delete with mixed caps
        result = "\n[[ CMD-Delete: ]]" + "\nrangeType: done" + "\nid: null";
        cmd = Parser.parse("deLEte dONe").toString();
        assertEquals("Delete: mixed caps (done)", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdRestore() {
        System.out.println("\n>> Testing Restore Command...");

        String result;
        String cmd;

        // Empty Restore
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: No arguments for restore";
        cmd = Parser.parse("restore").toString();
        assertEquals("Restore: empty, invalid", result, cmd);

        // Invalid Restore
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: Invalid argument for restore";
        cmd = Parser.parse("restore b").toString();
        assertEquals("Restore: invalid (word!=all)", result, cmd);

        // Restore by ID
        result = "\n[[ CMD-RESTORE: ]]" + "\nrangeType: id" + "\nid: 1";
        cmd = Parser.parse("restore 1").toString();
        assertEquals("Restore: id", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdDisplay() {
        System.out.println("\n>> Testing Display Command...");

        String result;
        String cmd;

        // Empty display (displays todo tasks)
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: all" + "\nid: null";
        cmd = Parser.parse("display").toString();
        assertEquals("Display: empty", result, cmd);

        // Empty display with spaces
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: all" + "\nid: null";
        cmd = Parser.parse("display           ").toString();
        assertEquals("Display: empty, spaces", result, cmd);

        // Display block
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: block" + "\nid: null";
        cmd = Parser.parse("display block").toString();
        assertEquals("Display: block", result, cmd);

        // Display block with spaces
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: block" + "\nid: null";
        cmd = Parser.parse("display block     ").toString();
        assertEquals("Display: block, spaces", result, cmd);

        // Display block with words after
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: block" + "\nid: null";
        cmd = Parser.parse("display block a b c").toString();
        assertEquals("Display: block, extra rangeTypes", result, cmd);

        // Display block with words mixed caps
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: block" + "\nid: null";
        cmd = Parser.parse("diSpLAy bloCk").toString();
        assertEquals("Display: block, mixed caps", result, cmd);

        // Display by ID
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: id" + "\nid: 2";
        cmd = Parser.parse("display 2").toString();
        assertEquals("Display: ID", result, cmd);

        // Switch 'display' with 'show'
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: id" + "\nid: 2";
        cmd = Parser.parse("show 2").toString();
        assertEquals("Display: show", result, cmd);

        // Invalid field
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: Invalid argument for display";
        cmd = Parser.parse("display blocka").toString();
        assertEquals("Display: invalid", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdSearch() {
        System.out.println("\n>> Testing Search Command...");

        String result;
        String cmd;

        // Empty Search (invalid)
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: No arguments for search";
        cmd = Parser.parse("search").toString();
        assertEquals("Search: empty", result, cmd);

        // Empty Search with spaces (invalid)
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: No arguments for search";
        cmd = Parser.parse("search     ").toString();
        assertEquals("Search: empty, spaces", result, cmd);

        // Full search: mixed caps command, date, keywords, tags
        result = "\n[[ CMD-SEARCH: ]]" + "\nCmdType: SEARCH"
                 + "\ndate: 23/04/2014" + "\ntags: [#done, #cS2103, #don]"
                 + "\nkeywords: [23/04/201, homework, late]";
        cmd = Parser
                .parse("seARch 23/04/2014 24/04/2014 23/04/201 homework #done #cS2103 #don #todo late ")
                .toString();
        assertEquals("Search: full combination", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdDone() {
        System.out.println("\n>> Testing Done Command...");

        String result;
        String cmd;

        // Empty Done (invalid)
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: No arguments for done";
        cmd = Parser.parse("done").toString();
        assertEquals("Done: empty", result, cmd);

        // Empty Done + extra spaces
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: No arguments for done";
        cmd = Parser.parse("done      ").toString();
        assertEquals("Done: empty, spaces", result, cmd);

        // Done all, extra spaces
        result = "\n[[ CMD-DONE: ]]" + "\nrangeType: all" + "\nid: null"
                 + "\ndateTime: null";
        cmd = Parser.parse("done all   ").toString();
        assertEquals("Done: all, spaces", result, cmd);

        // Done by id, extra words after
        result = "\n[[ CMD-DONE: ]]" + "\nrangeType: id" + "\nid: 2"
                 + "\ndateTime: null";
        cmd = Parser.parse("done 2 hurrah").toString();
        assertEquals("Done: id, extra words", result, cmd);

        // Done by date
        result = "\n[[ CMD-DONE: ]]" + "\nrangeType: date" + "\nid: null"
                 + "\ndateTime: 23/04/2014";
        cmd = Parser.parse("done 23/04/2014").toString();
        assertEquals("Done: date", result, cmd);

        // Invalid parameter
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: Invalid argument for done";
        cmd = Parser.parse("done this").toString();
        assertEquals("Done: invalid", result, cmd);

        System.out.println("...success!");

    }

    @Test
    public void testCmdTodo() {
        System.out.println("\n>> Testing Todo Command...");

        String result;
        String cmd;

        // Empty Todo (invalid), mixed caps, spaces
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: No arguments for todo";
        cmd = Parser.parse("tODo      ").toString();
        assertEquals("Todo: empty, caps, spaces", result, cmd);

        // Todo invalid parameter
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: Invalid argument for todo";
        cmd = Parser.parse("todo one").toString();
        assertEquals("Todo: invalid", result, cmd);

        // Todo id, extra numbers/words
        result = "\n[[ CMD-TODO: ]]" + "\nrangeType: id" + "\nid: 1";
        cmd = Parser.parse("todo 1 3 four").toString();
        assertEquals("Todo: id, extra", result, cmd);

        // Todo last
        result = "\n[[ CMD-TODO: ]]" + "\nrangeType: last" + "\nid: null";
        cmd = Parser.parse("todo last").toString();
        assertEquals("Todo: last", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdBlock() {
        System.out.println("\n>> Testing Block Command...");

        String result;
        String cmd;

        // TODO: permutations for dates+times
        System.out.println(Parser.parse("block 23/04/2014 to 23/05/2014"));
        System.out.println(Parser.parse("block 23/04/201"));
        System.out.println(Parser.parse("block 23/4/2014"));
        System.out.println(Parser.parse("block 2/04/2014"));
        System.out.println(Parser.parse("block 23/13/2014"));
        System.out.println(Parser.parse("block 33/04/2014"));
        System.out.println(Parser.parse("block -3/04/2014"));
        System.out.println(Parser.parse("block 03/04/-014"));
        System.out.println(Parser.parse("block 23/04/2014 23/05/2014"));
        System.out.println(Parser.parse("block 23/04/2014 to 23/05/2012"));
        System.out.println(Parser.parse("block 23/04/2012 to 23/05/2014"));
        System.out.println(Parser.parse("block 23/04/1*14"));

        // Empty (invalid), mixed caps, spaces
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: No arguments for block";
        cmd = Parser.parse("bLOck      ").toString();
        assertEquals("Block: empty, caps, spaces", result, cmd);

        // Invalid parameter (random word)
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: Invalid argument for block";
        cmd = Parser.parse("block monday").toString();
        assertEquals("Block: invalid/random", result, cmd);

        // Invalid parameter (today)
        // TODO: is support for today/tomorrow required?
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: Invalid argument for block";
        cmd = Parser.parse("block today 23/04/2014").toString();
        assertEquals("Block: invalid/today", result, cmd);

        // Single date
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 23/04/2014 0000"
                 + "\nend: 23/04/2014 2359";
        cmd = Parser.parse("bLOck 23/04/2014").toString();
        assertEquals("Block: single", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdUnblock() {
        System.out.println("\n>> Testing Unblock Command...");

        String result;
        String cmd;

        // Empty (invalid), mixed caps, spaces
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: No arguments for unblock";
        cmd = Parser.parse("unBLock      ").toString();
        assertEquals("Unblock: empty, caps, spaces", result, cmd);

        // Invalid parameter
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: ERROR"
                 + "\ncmd-info: Invalid argument for unblock";
        cmd = Parser.parse("unblock one").toString();
        assertEquals("unblock: invalid", result, cmd);

        // ID, extra numbers/words
        result = "\n[[ CMD-UNBLOCK: ]]" + "\nid: 1";
        cmd = Parser.parse("unBLock 1 3 four").toString();
        assertEquals("Todo: id, extra", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdOthers() {
        System.out.println("\n>> Testing Undo, Redo & Exit Commands...");

        String result;
        String cmd;

        // Undo: caps, spaces
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: UNDO" + "\ncmd-info: null";
        cmd = Parser.parse("uNDo      ").toString();
        assertEquals("Undo: mixed caps, spaces", result, cmd);

        // Redo: caps, spaces
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: REDO" + "\ncmd-info: null";
        cmd = Parser.parse("rEdO      ").toString();
        assertEquals("Redo: mixed caps, spaces", result, cmd);

        // Exit: caps, spacesUNBLOCK
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: EXIT" + "\ncmd-info: null";
        cmd = Parser.parse("exIT      ").toString();
        assertEquals("Exit: mixed caps, spaces", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testParseToBlock() {
        System.out.println("\n>> Testing parseToBlock()...");

        String result;
        String blockDate;

        // Single date
        result = "23/04/2014 0000 to 23/04/2014 2359";
        blockDate = Parser.parseToBlock("23/04/2014 0000 to 23/04/2014 2359")
                .toString();
        assertEquals(result, blockDate);

        // Across days [string comparison]
        result = "23/04/2014 0500 to 25/04/2014 1000";
        blockDate = Parser.parseToBlock("23/04/2014 0500 to 25/04/2014 1000")
                .toString();
        assertEquals(result, blockDate);

        BlockDate sampleBD;
        BlockDate outputBD;

        // Across days [object comparison]
        sampleBD = new BlockDate(new DateTime("23/04/2014", "0500"), new DateTime(
                "25/04/2014", "1000"));
        outputBD = Parser.parseToBlock("23/04/2014 0500 to 25/04/2014 1000");
        assertEquals(sampleBD, outputBD);
    }

    @Test(expected = AssertionError.class)
    public void failParseToBlock() {
        System.out.println("\n>> Failing parseToBlock()...");
        
        // Note: -ea needed to trigger AssertionError
        System.out
                .println(Parser.parseToBlock("23/04/2014 0000 to 23/04/2014"));
    }
}
