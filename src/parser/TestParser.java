package parser;

import static org.junit.Assert.*;

import org.junit.Test;

import database.BlockDate;
import database.DateTime;
import database.Task;

public class TestParser {

    // Consider using object.equals() to test assertEquals()
    // Rejected: Errors in constructor will not be caught

    @Test
    public void testCommand() {
        System.out.println("\n>> Testing general inputs...");

        String result;
        String cmd;

        // Valid command with spaces in front/behind
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: HELP" + "\ncmd-info: ";
        cmd = Parser.parse("    hELp    ").toString();
        assertEquals("Command: spaces", result, cmd);

        System.out.println("...success!");
    }

    @Test(expected = AssertionError.class)
    public void failCommandNull() {
        System.out.println("\n>> Failing general inputs with a null...");

        Parser.parse(null);

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCommandInvalid() {
        System.out.println("\n>> Failing general inputs with random words...");

        Parser.parse("that homework it's #cs2103");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCommandEmpty() {
        System.out.println("\n>> Failing general inputs with empty string...");

        Parser.parse("");

        System.out.println("...success!");
    }

    @Test
    public void testParseToTask() {
        System.out.println("\n>> Testing parseToTask()...");

        // Test parsing from stored data
        // Pre-cond: the data should have been formatted properly by Storage

        String result;
        String task;

        // Test full input
        task = tempTaskToString(Parser
                .parseToTask("do homework ### start: 20/04/2014 due: 20/04/2014 0300 completed: 22/04/2014 1800 #cs2103 #todo status: done\n"));
        result = "\n[[ Task ]]" + "\nName: do homework" + "\nStart: 20/04/2014"
                 + "\nDue: 20/04/2014 0300" + "\nCompleted: 22/04/2014 1800"
                 + "\nTags: [#cs2103, #todo]" + "\nStatus: done";
        assertEquals(result, task);

        // Test "empty" input
        task = tempTaskToString(Parser
                .parseToTask("### start: due: completed: status: \n"));
        result = "\n[[ Task ]]" + "\nName: " + "\nStart: " + "\nDue: "
                 + "\nCompleted: " + "\nTags: []" + "\nStatus: todo";
        assertEquals(result, task);

        System.out.println("...success!");
    }

    private static String tempTaskToString(Task task) {
        String fullInfo = "\n[[ Task ]]";
        fullInfo += "\nName: " + task.getName();
        fullInfo += "\nStart: " + task.getStart();
        fullInfo += "\nDue: " + task.getDue();
        fullInfo += "\nCompleted: " + task.getCompletedOn();
        fullInfo += "\nTags: " + task.getTags();
        fullInfo += "\nStatus: ";
        fullInfo += task.isDone() ? "done" : "todo";

        return fullInfo;
    }

    @Test
    public void testCmdAdd() {
        System.out.println("\n>> Testing Add Command...");

        String result;
        String cmd;

        // Empty Add
        result = "\n[[ CMD-ADD: ]]" + "\nname: " + "\nstart: " + "\ndue: "
                 + "\ntags: []";
        cmd = Parser.parse("add").toString();
        assertEquals("Add: empty", result, cmd);

        // Basic Add ending with a parameter
        result = "\n[[ CMD-ADD: ]]" + "\nname: do homework it's cs2103 end"
                 + "\nstart: " + "\ndue: " + "\ntags: [#cs2103]";
        cmd = Parser.parse("add do homework it's #cs2103 cs2103 end")
                .toString();
        assertEquals("Add: simple, end param", result, cmd);

        // Full Add
        result = "\n[[ CMD-ADD: ]]"
                 + "\nname: do start up research # due from "
                 + "start 29/10/2014 soon. do quickly due thurs"
                 + "\nstart: 27/10/2014 0900" + "\ndue: 29/10/2014"
                 + "\ntags: [#cs2103, #work]";
        cmd = Parser.parse("  add   do start #cs2103  up research # "
                                   + "due from from  27/10/2014  0900 "
                                   + "start 29/10/2014 soon. do quickly due "
                                   + "thurs end  29/10/2014 #work\n")
                .toString();
        assertEquals("Add: full", result, cmd);

        // Testing missing date in start and end
        result = "\n[[ CMD-ADD: ]]" + "\nname: do work" + "\nstart: " +
                 DateParser.getCurrDateStr() + " 0300" + "\ndue: " +
                 DateParser.getCurrDateStr() + " 0600" + "\ntags: []";
        cmd = Parser.parse("add do work from 0300 to 0600\n").toString();
        assertEquals("Add: date filling", result, cmd);

        // Testing missing date in start with reordering
        result = "\n[[ CMD-ADD: ]]" + "\nname: do work"
                 + "\nstart: 29/10/2014 0300" + "\ndue: 29/10/2014 0600"
                 + "\ntags: []";
        cmd = Parser.parse("add do work from 29/10/2014 0600 due 0300\n")
                .toString();
        assertEquals("Add: 1 missing date, reordering", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdEdit() {
        System.out.println("\n>> Testing Edit Command...");

        String result;
        String cmd;

        // Edit with only id
        // TODO: Shouldn't this be invalid?
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 1" + "\nname: " + "\nstart: "
                 + "\ndue: " + "\ntags: []" + "\ndelete: []";
        cmd = Parser.parse("edit 1").toString();
        assertEquals("Edit: id only", result, cmd);

        // Full Edit with repeated parameters and consecutive parameters
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 3"
                 + "\nname: do homework due soon for CS2103 project"
                 + "\nstart: 22/04/2014 1200" + "\ndue: 23/04/2014"
                 + "\ntags: [#CS2103]" + "\ndelete: [due, start]";
        cmd = Parser
                .parse(" edIt 3 do  #CS2103 homework due  DUE  23/04/2014  start   22/04/2014   1200 "
                               + "soon for CS2103 delete  end  delete frOM project")
                .toString();
        assertEquals("Edit: full, repeated param, consecutive param", result,
                     cmd);

        // Testing delete parameter
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 4"
                 + "\nname: do homework delete tags by start delete"
                 + "\nstart: " + "\ndue: " + "\ntags: []"
                 + "\ndelete: [start, due, tags]";
        cmd = Parser
                .parse("edit 4 do homework delete start delete to delete tags delete tags by start delete")
                .toString();
        assertEquals("Edit: delete param", result, cmd);

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdEditEmpty() {
        System.out.println("\n>> Failing Edit Command with an empty String...");

        // Empty (invalid), mixed caps
        Parser.parse("eDIt");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdEditID() {
        System.out.println("\n>> Failing Edit Command with an invalid ID...");

        // Edit with words but no id
        Parser.parse("edit one two");

        System.out.println("...success!");
    }

    @Test
    public void testCmdDelete() {
        System.out.println("\n>> Testing Delete Command...");

        String result;
        String cmd;

        // Test possible input parameters
        String[] deleteParams = new String[] { "all", "search", "done" };

        for (String p : deleteParams) {
            result = "\n[[ CMD-Delete: ]]" + "\nrangeType: " + p + "\nid: ";
            cmd = Parser.parse("delete " + p).toString();
            assertEquals("Delete: parameter", result, cmd);
        }

        // Delete by ID
        result = "\n[[ CMD-Delete: ]]" + "\nrangeType: id" + "\nid: 11";
        cmd = Parser.parse("delete 11").toString();
        assertEquals("Delete: id", result, cmd);

        // Delete with mixed caps
        result = "\n[[ CMD-Delete: ]]" + "\nrangeType: done" + "\nid: ";
        cmd = Parser.parse("deLEte dONe").toString();
        assertEquals("Delete: mixed caps (done)", result, cmd);

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdDeleteEmpty() {
        System.out.println("\n>> Failing Delete Command with just spaces...");

        Parser.parse("delete    ");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdDeleteInvalid() {
        System.out
                .println("\n>> Failing Delete Command with invalid parameter...");

        Parser.parse("delete days");

        System.out.println("...success!");
    }

    @Test
    public void testCmdRestore() {
        System.out.println("\n>> Testing Restore Command...");

        String result;
        String cmd;

        // Restore by ID
        result = "\n[[ CMD-RESTORE: ]]" + "\nrangeType: id" + "\nid: 1";
        cmd = Parser.parse("   restore   1     ").toString();
        assertEquals("Restore: id, spaces", result, cmd);

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdRestoreEmpty() {
        System.out.println("\n>> Failing Restore Command with just spaces...");

        // Empty (invalid), mixed caps
        Parser.parse("restore");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdRestoreInvalid() {
        System.out
                .println("\n>> Failing Restore Command with an invalid parameter input...");

        // Empty (invalid), mixed caps
        Parser.parse("restore b");

        System.out.println("...success!");
    }

    @Test
    public void testCmdDisplay() {
        System.out.println("\n>> Testing Display Command...");

        String result;
        String cmd;

        // Empty display (displays todo tasks)
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: todo" + "\nid: ";
        cmd = Parser.parse("display").toString();
        assertEquals("Display: empty", result, cmd);

        // Empty display with spaces
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: todo" + "\nid: ";
        cmd = Parser.parse("display           ").toString();
        assertEquals("Display: empty, spaces", result, cmd);

        // Test possible input parameters
        String[] displayParams = new String[] { "all", "block", "search",
                                               "done", "deleted" };

        for (String p : displayParams) {
            result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: " + p + "\nid: ";
            cmd = Parser.parse("display " + p).toString();
            assertEquals("Display: parameter", result, cmd);
        }

        // Words after, mixed caps, spaces
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: block" + "\nid: ";
        cmd = Parser.parse("    disPLay    bLOck    a    b   c").toString();
        assertEquals("Display: block, extra, caps, spaces", result, cmd);

        // Display by ID
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: id" + "\nid: 2";
        cmd = Parser.parse("display 2").toString();
        assertEquals("Display: ID", result, cmd);

        // Switch 'display' with 'show'
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: id" + "\nid: 2";
        cmd = Parser.parse("show 2").toString();
        assertEquals("Display: show", result, cmd);

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdDisplayInvalid() {
        System.out
                .println("\n>> Failing Display Command with an invalid input...");

        Parser.parse("display blocka");

        System.out.println("...success!");
    }

    @Test
    public void testCmdSearch() {
        System.out.println("\n>> Testing Search Command...");

        String result;
        String cmd;

        // Full search: mixed caps command, status, date, keywords, tags
        result = "\n[[ CMD-SEARCH: ]]" + "\nstatus: all"
                 + "\ndate: 23/04/2014" + "\ntags: [#done, #cS2103]"
                 + "\nkeywords: [done, 23/04/201, homework, late]";
        cmd = Parser
                .parse("  seARch    ALL  done 23/04/2014 23/04/201 homework #done #cS2103 late ")
                .toString();
        assertEquals("Search: status, full combination", result, cmd);
        
        // Search without status
        result = "\n[[ CMD-SEARCH: ]]" + "\nstatus: todo"
                 + "\ndate: 23/04/2014" + "\ntags: [#cS2103]"
                 + "\nkeywords: [homework]";
        cmd = Parser
                .parse(" seARch   23/04/2014 homework #cS2103")
                .toString();
        assertEquals("Search: no status", result, cmd);

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdSearchEmpty() {
        System.out.println("\n>> Failing Search Command with just spaces...");

        Parser.parse("search      ");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdSearchDates() {
        System.out
                .println("\n>> Failing Search Command with multiple dates...");

        Parser.parse("search 23/04/2014 24/04/2014");

        System.out.println("...success!");
    }

    @Test
    public void testCmdDone() {
        System.out.println("\n>> Testing Done Command...");

        String result;
        String cmd;

        // Done all, extra spaces
        result = "\n[[ CMD-DONE: ]]" + "\nrangeType: all" + "\nid: "
                 + "\ndateTime: ";
        cmd = Parser.parse("done all   ").toString();
        assertEquals("Done: all, spaces", result, cmd);

        // Done by id, extra words after
        result = "\n[[ CMD-DONE: ]]" + "\nrangeType: id" + "\nid: 2"
                 + "\ndateTime: ";
        cmd = Parser.parse("done 2 hurrah").toString();
        assertEquals("Done: id, extra words", result, cmd);

        // Done by date
        result = "\n[[ CMD-DONE: ]]" + "\nrangeType: date" + "\nid: "
                 + "\ndateTime: 23/04/2014";
        cmd = Parser.parse("done 23/04/2014").toString();
        assertEquals("Done: date", result, cmd);

        System.out.println("...success!");

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failCmdDoneEmpty() {
        System.out
                .println("\n>> Failing Done Command with spaces...");

        Parser.parse("done      ");

        System.out.println("...success!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failCmdDoneInvalid() {
        System.out
                .println("\n>> Failing Done Command with an invalid parameter...");

        Parser.parse("done this");

        System.out.println("...success!");
    }

    @Test
    public void testCmdTodo() {
        System.out.println("\n>> Testing Todo Command...");

        String result;
        String cmd;

        // Todo id, extra numbers/words
        result = "\n[[ CMD-TODO: ]]" + "\nrangeType: id" + "\nid: 1";
        cmd = Parser.parse("todo 1 3 four").toString();
        assertEquals("Todo: id, extra", result, cmd);

        // Todo last
        result = "\n[[ CMD-TODO: ]]" + "\nrangeType: last" + "\nid: ";
        cmd = Parser.parse("todo last").toString();
        assertEquals("Todo: last", result, cmd);

        System.out.println("...success!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failCmdTodoEmpty() {
        System.out
                .println("\n>> Failing Todo Command with spaces...");

        Parser.parse("tODo      ");

        System.out.println("...success!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failCmdTodoInvalid() {
        System.out
                .println("\n>> Failing Todo Command with an invalid parameter...");

        Parser.parse("todo one");

        System.out.println("...success!");
    }

    @Test
    public void testCmdBlock() {
        System.out.println("\n>> Testing Block Command...");
        System.out.println("(Note that the date for time-only cases is " +
                           DateParser.getCurrDateStr() + ")");

        String result;
        String cmd;

        // TODO: is support for today/tomorrow required?

        // 1/4: Single date
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 23/04/2014 0000"
                 + "\nend: 23/04/2014 2359";
        cmd = Parser.parse("block 23/04/2014").toString();
        assertEquals("Block: single date", result, cmd);

        // 1/4: Single time [Note that date is generated by current date]
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: " +
                 DateParser.getCurrDateStr() + " 0300" + "\nend: " +
                 DateParser.getCurrDateStr() + " 2359";
        cmd = Parser.parse("bLOck 0300").toString();
        assertEquals("Block: single time", result, cmd);

        // 2/4: First date and time
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 23/04/2014 0600"
                 + "\nend: 23/04/2014 2359";
        cmd = Parser.parse("bLOck 0600 23/04/2014").toString();
        assertEquals("Block: single date/time", result, cmd);

        // 2/4: Latter Single date and time
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 23/04/2014 0000"
                 + "\nend: 23/04/2014 0600";
        cmd = Parser.parse("bLOck to 0600 23/04/2014").toString();
        assertEquals("Block: single date/time, to", result, cmd);

        // 2/4: Date-only range
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 23/04/2014 0000"
                 + "\nend: 25/04/2014 2359";
        cmd = Parser.parse("bLOck 23/04/2014 to 25/04/2014").toString();
        assertEquals("Block: date range", result, cmd);

        // 2/4: Date-only range with swap
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 23/04/2014 0000"
                 + "\nend: 25/04/2014 2359";
        cmd = Parser.parse("bLOck 25/04/2014 to 23/04/2014").toString();
        assertEquals("Block: date range, swap", result, cmd);

        // 2/4: Time-only range [Note that date is generated by current date]
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: " +
                 DateParser.getCurrDateStr() + " 0300" + "\nend: " +
                 DateParser.getCurrDateStr() + " 0600";
        cmd = Parser.parse("bLOck 0300 to 0600").toString();
        assertEquals("Block: time range", result, cmd);

        // 2/4: Time-only range with swap
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: " +
                 DateParser.getCurrDateStr() + " 0300" + "\nend: " +
                 DateParser.getCurrDateStr() + " 0600";
        cmd = Parser.parse("bLOck 0600 to 0300").toString();
        assertEquals("Block: time range, swap", result, cmd);

        // 2/4: 1st time 2nd date
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 23/04/2014 0000"
                 + "\nend: 23/04/2014 0600";
        cmd = Parser.parse("bLOck 23/04/2014 to 0600").toString();
        assertEquals("Block: 1st time, 2nd date", result, cmd);

        // 2/4: 2nd time 1st date
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 23/04/2014 0600"
                 + "\nend: 23/04/2014 2359";
        cmd = Parser.parse("bLOck 0600 to 23/04/2014").toString();
        assertEquals("Block: 2nd time, 1st date", result, cmd);

        // 3/4: missing 1st time, swap
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 24/04/2014 0600"
                 + "\nend: 23/04/2018 2359";
        cmd = Parser.parse("bLOck 23/04/2018 to 24/04/2014 0600").toString();
        assertEquals("Block: 3/4, no 1st time", result, cmd);

        // 3/4: missing 2nd time, time-date order
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 23/04/2014 0600"
                 + "\nend: 24/04/2014 2359";
        cmd = Parser.parse("bLOck 0600 23/04/2014 to 24/04/2014").toString();
        assertEquals("Block: 3/4, no 2nd time, time-date order", result, cmd);

        // 3/4: missing 1st date, time-date order, swap
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 24/04/2014 0300"
                 + "\nend: 24/04/2014 0600";
        cmd = Parser.parse("bLOck 0600 to 0300 24/04/2014").toString();
        assertEquals("Block: 3/4, no 1st date, swap, time-date order", result,
                     cmd);

        // 3/4: missing 2nd date
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 24/04/2014 0300"
                 + "\nend: 24/04/2014 0600";
        cmd = Parser.parse("bLOck 24/04/2014 0300 to 0600").toString();
        assertEquals("Block: 3/4, no 2nd date", result, cmd);

        // 4/4: swap with extra spaces and extra words
        result = "\n[[ CMD-BLOCK: ]]" + "\nstart: 23/04/2014 0600"
                 + "\nend: 24/04/2014 0300";
        cmd = Parser.parse("   bLOck    24/04/2014    0300 to  23/04/2014"
                                   + "   0600 extra words here 23/04/2014")
                .toString();
        assertEquals("Block: 4/4, swap, extra spaces", result, cmd);

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdBlockEmpty() {
        System.out
                .println("\n>> Failing Block Command with an empty String...");

        // Empty (invalid), mixed caps
        Parser.parse("bLOck");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdBlockSpaces() {
        System.out.println("\n>> Failing Block Command with just spaces...");

        // Spaces
        Parser.parse("block      ");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdBlockNoDt() {
        System.out
                .println("\n>> Failing Block Command with an invalid input...");

        // Invalid input parameter (random word)
        // Valid date after the word does not affect result.
        Parser.parse("block monday 23/04/2014");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdBlockNoTo() {
        System.out
                .println("\n>> Failing Block Command with two dates and no 'to'...");

        // Missing 'to'
        Parser.parse("block 23/04/2014 23/04/2014");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdBlockDate1() {
        System.out
                .println("\n>> Failing Block Command with an asterisk in date...");

        // Inproper characters
        Parser.parse("block 23/04/2*14");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdBlockDate2() {
        System.out
                .println("\n>> Failing Block Command with a negative year...");

        // Inproper characters
        Parser.parse("block 23/04/-114");

        System.out.println("...success!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failCmdBlockDate3() {
        System.out
                .println("\n>> Failing Block Command with a close-to-valid date...");

        // Inproper characters
        Parser.parse("block 23/04/201");

        System.out.println("...success!");
    }

    @Test
    public void testCmdUnblock() {
        System.out.println("\n>> Testing Unblock Command...");

        String result;
        String cmd;

        // ID, extra numbers/words, mixed caps
        result = "\n[[ CMD-UNBLOCK: ]]" + "\nid: 1";
        cmd = Parser.parse("unBLock 1 3 four").toString();
        assertEquals("Todo: id, extra, caps", result, cmd);

        System.out.println("...success!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failCmdUnblockEmpty() {
        System.out
                .println("\n>> Failing Unblock Command with spaces...");

        Parser.parse("unblock      ");

        System.out.println("...success!");
    }
    
    public void failCmdUnblockInvalid() {
        System.out
                .println("\n>> Failing Unblock Command with an invalid term...");

        Parser.parse("unblock one");

        System.out.println("...success!");
    }

    @Test
    public void testCmdOthers() {
        System.out
                .println("\n>> Testing Undo, Redo, Help, Reset, Exit Commands...");

        String result;
        String cmd;

        // Undo: caps, spaces
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: UNDO" + "\ncmd-info: ";
        cmd = Parser.parse("uNDo      ").toString();
        assertEquals("Undo: mixed caps, spaces", result, cmd);

        // Redo: caps, spaces
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: REDO" + "\ncmd-info: ";
        cmd = Parser.parse("rEdO      ").toString();
        assertEquals("Redo: mixed caps, spaces", result, cmd);

        // Exit: caps, spacesUNBLOCK
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: EXIT" + "\ncmd-info: ";
        cmd = Parser.parse("exIT      ").toString();
        assertEquals("Exit: mixed caps, spaces", result, cmd);

        // Reset
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: RESET" + "\ncmd-info: ";
        cmd = Parser.parse("reset").toString();
        assertEquals("Reset: simple", result, cmd);

        // Help: caps, spaces, extra words
        result = "\n[[ CMD-OTHERS ]]" + "\ncmd-type: HELP" + "\ncmd-info: ";
        cmd = Parser.parse("  heLP me      ").toString();
        assertEquals("Help: caps, spaces, extra words", result, cmd);

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
        sampleBD = new BlockDate(new DateTime("23/04/2014", "0500"),
                new DateTime("25/04/2014", "1000"));
        outputBD = Parser.parseToBlock("23/04/2014 0500 to 25/04/2014 1000");
        assertEquals(sampleBD, outputBD);

        System.out.println("...success!");
    }

    @Test(expected = AssertionError.class)
    public void failParseToBlock() {
        System.out.println("\n>> Failing parseToBlock()...");

        // Note: -ea needed to trigger AssertionError
        Parser.parseToBlock("23/04/2014 0000 to 23/04/2014");

        System.out.println("...success!");
    }
}
