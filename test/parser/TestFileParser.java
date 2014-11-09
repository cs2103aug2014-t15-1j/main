package parser;

import static org.junit.Assert.*;

import org.junit.Test;

import database.DateTime;
import database.Task;

/**
 * Test Class for FileParser. All methods are called via the Parser class as
 * testing the facade separately will cause unnecessary double-work.
 * <p>
 * <i>Note that Assertions <strong>must</strong> be enabled for AssertionErrors
 * to be tested. Check the run configurations (of this test class or test suite)
 * and make sure it includes VM argument "-ea".</i>
 * 
 * @author Yeo Zi Xian, Justin (A0116208N)
 *
 */
public class TestFileParser {

    @Test
    public void testParseToTask() {
        System.out.println("\n>> Testing parseToTask()...");

        // Test parsing from stored data
        // Pre-cond: the data should have been formatted properly by Storage

        String result;
        String task;

        // Test full input
        task = tempTaskToString(Parser
                .parseToTask("do homework ### start: 20/04/2014 due: 20/04/2014 0300 "
                             + "completed: 22/04/2014 1800 #cs2103 #todo type: done\n"));
        result = "\n[[ Task ]]" + "\nName: do homework" + "\nStart: 20/04/2014"
                 + "\nDue: 20/04/2014 0300" + "\nCompleted: 22/04/2014 1800"
                 + "\nTags: [#cs2103, #todo]" + "\nStatus: done";
        assertEquals(result, task);

        // Test "empty" input
        task = tempTaskToString(Parser
                .parseToTask("### start: due: completed: type: todo\n"));
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
}
