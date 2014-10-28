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
    public void testCommand() {
        System.out.println("\n>> Testing general inputs...");

        String result;
        String cmd;

        // Valid command with spaces in front
        result = "\n[[ CMD-HELP: ]]" + "\nfield: ";
        cmd = Parser.parse("              help").toString();
        assertEquals("Help: empty", result, cmd);

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
                .parseToTask("do homework ### due: 23/04/2014 0300 start: 20/04/2014 end: 21/04/2014 1800 #cs2103 #todo status: done\n"));
        result = "\n[[ Task ]]" + "\nName: do homework"
                 + "\nDue: 23/04/2014 0300" + "\nStart: 20/04/2014"
                 + "\nEnd: 21/04/2014 1800" + "\nTags: [#cs2103, #todo]"
                 + "\nStatus: done";
        assertEquals(result, task);

        // Test "empty" input
        task = tempTaskToString(Parser
                .parseToTask("### due: start: end: status: \n"));
        result = "\n[[ Task ]]" + "\nName: " + "\nDue: " + "\nStart: "
                 + "\nEnd: " + "\nTags: []" + "\nStatus: todo";
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
        fullInfo += "\nStatus: ";
        fullInfo += task.isDone() ? "done" : "todo";

        return fullInfo;
    }

    @Test
    public void testCmdHelp() {
        System.out.println("\n>> Testing Help Command...");

        String result;
        String cmd;

        // Empty Help
        result = "\n[[ CMD-HELP: ]]" + "\nfield: ";
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

        String result;
        String cmd;

        // Empty Add
        result = "\n[[ CMD-ADD: ]]" + "\nname: " + "\ndue: " + "\nstart: "
                 + "\nend: " + "\ntags: []";
        cmd = Parser.parse("add").toString();
        assertEquals("Add: empty", result, cmd);

        // Basic Add ending with a parameter
        result = "\n[[ CMD-ADD: ]]" + "\nname: do homework it's cs2103 end"
                 + "\ndue: " + "\nstart: " + "\nend: " + "\ntags: [#cs2103]";
        cmd = Parser.parse("add do homework it's #cs2103 cs2103 end")
                .toString();
        assertEquals("Add: simple, end param", result, cmd);

        // Full Add
        result = "\n[[ CMD-ADD: ]]"
                 + "\nname: do start up research # due from "
                 + "start 29/10/2014 due soon. do quickly due thurs"
                 + "\ndue: 29/10/2014" + "\nstart: 27/10/2014 0900"
                 + "\nend: 28/10/2014" + "\ntags: [#cs2103, #work]";
        cmd = Parser
                .parse("  add   do start #cs2103  up research # due from from  27/10/2014  0900 "
                               + "start 29/10/2014  due end  28/10/2014 "
                               + "soon. do quickly due thurs due  29/10/2014 #work\n")
                .toString();
        assertEquals("Add: full", result, cmd);

        // Testing missing date in start and end
        result = "\n[[ CMD-ADD: ]]" + "\nname: do work" + "\ndue: " +
                 "\nstart: " + DateParser.getCurrDateStr() + " 0300" +
                 "\nend: " + DateParser.getCurrDateStr() + " 0600" +
                 "\ntags: []";
        cmd = Parser.parse("add do work from 0300 to 0600\n").toString();
        assertEquals("Add: date filling", result, cmd);

        // Testing missing date in start with reordering
        result = "\n[[ CMD-ADD: ]]" + "\nname: do work" + "\ndue: "
                 + "\nstart: 29/10/2014 0300" + "\nend: 29/10/2014 0600"
                 + "\ntags: []";
        cmd = Parser.parse("add do work from 29/10/2014 0600 to 0300\n")
                .toString();
        assertEquals("Add: date filling", result, cmd);

        System.out.println("...success!");
    }

    @Test
    public void testCmdEdit() {
        System.out.println("\n>> Testing Edit Command...");

        String result;
        String cmd;

        // Edit with only id
        // TODO: Shouldn't this be invalid?
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 1" + "\nname: " + "\ndue: "
                 + "\nstart: " + "\nend: " + "\ntags: []" + "\ndelete: ";
        cmd = Parser.parse("edit 1").toString();
        assertEquals("Edit: id only", result, cmd);

        // Simple Edit
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 2" + "\nname: do homework"
                 + "\ndue: 23/04/2014" + "\nstart: " + "\nend: " + "\ntags: []"
                 + "\ndelete: ";
        cmd = Parser.parse("edit 2 do homework due 23/04/2014").toString();
        assertEquals("Edit: simple", result, cmd);

        // Full Edit with repeated parameters and consecutive parameters
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 3"
                 + "\nname: do homework for CS2103 project"
                 + "\ndue: 23/04/2014" + "\nstart: 22/04/2014 0200"
                 + "\nend: 22/04/2014 1200" + "\ntags: [#CS2103]"
                 + "\ndelete: end";
        cmd = Parser
                .parse(" edIt 3 do  #CS2103 homework due   23/04/2014 start   22/04/2014   1200 "
                               + "for CS2103 delete  end  END  0200   22/04/2014   project")
                .toString();
        assertEquals("Edit: full, repeated param, consecutive param", result,
                     cmd);

        // Testing delete parameter
        result = "\n[[ CMD-EDIT: ]]" + "\nid: 4"
                 + "\nname: do homework delete tags by start delete" + "\ndue: "
                 + "\nstart: " + "\nend: " + "\ntags: []" + "\ndelete: end tags";
        cmd = Parser.parse("edit 4 do homework delete end delete tags delete tags by start delete")
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
        result = "\n[[ CMD-Delete: ]]" + "\nrangeType: all" + "\nid: ";
        cmd = Parser.parse("delete all").toString();
        assertEquals("Delete: all", result, cmd);

        // Delete searched tasks
        result = "\n[[ CMD-Delete: ]]" + "\nrangeType: search" + "\nid: ";
        cmd = Parser.parse("delete search").toString();
        assertEquals("Delete: search", result, cmd);

        // Delete done tasks
        result = "\n[[ CMD-Delete: ]]" + "\nrangeType: done" + "\nid: ";
        cmd = Parser.parse("delete done").toString();
        assertEquals("Delete: done", result, cmd);

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
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: all" + "\nid: ";
        cmd = Parser.parse("display").toString();
        assertEquals("Display: empty", result, cmd);

        // Empty display with spaces
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: all" + "\nid: ";
        cmd = Parser.parse("display           ").toString();
        assertEquals("Display: empty, spaces", result, cmd);

        // Display block
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: block" + "\nid: ";
        cmd = Parser.parse("display block").toString();
        assertEquals("Display: block", result, cmd);

        // Display block with spaces
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: block" + "\nid: ";
        cmd = Parser.parse("display block     ").toString();
        assertEquals("Display: block, spaces", result, cmd);

        // Display block with words after
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: block" + "\nid: ";
        cmd = Parser.parse("display block a b c").toString();
        assertEquals("Display: block, extra rangeTypes", result, cmd);

        // Display block with words mixed caps
        result = "\n[[ CMD-DISPLAY: ]]" + "\nrangeType: block" + "\nid: ";
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
        result = "\n[[ CMD-TODO: ]]" + "\nrangeType: last" + "\nid: ";
        cmd = Parser.parse("todo last").toString();
        assertEquals("Todo: last", result, cmd);

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
