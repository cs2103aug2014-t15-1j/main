package parser;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test Class for FileParser. All methods are called via the Parser class as
 * testing the facade separately will cause unnecessary double-work.
 * <p>
 * <i>Note that Assertions <strong>must</strong> be enabled for AssertionErrors
 * to be tested. Check the run configurations (of this test class or test suite)
 * and make sure it includes VM argument "-ea".</i>
 */
//@author A0116208N
public class TestFileParser {

    /**
     * Tests parsing from stored data.
     * <p>
     * <i>Pre-condition: the data should have been formatted properly by
     * database.</i>
     */
    @Test
    public void testParseToTask() {
        System.out.println("\n>> Testing parseToTask()...");

        String result;
        String task;

        // Test full input
        task = Parser
                .parseToTask("do homework ### start: 20/04/2014 due: 20/04/2014 0300 "
                                     + "completed: 22/04/2014 1800 #cs2103 #todo type: done\n")
                .toString();
        result = "do homework ### start: 20/04/2014 due: 20/04/2014 0300 "
                 + "completed: 22/04/2014 1800 #cs2103 #todo type: DONE";
        assertEquals(result, task);

        // Test block task (ensure that merging BlockDate into Tasks was
        // successful)
        task = Parser
                .parseToTask("do homework ### start: 20/04/2014 0000 due: 20/04/2014 0300 "
                                     + "completed: 22/04/2014 1800 #cs2103 #todo type: block\n")
                .toString();
        result = "do homework ### start: 20/04/2014 0000 due: 20/04/2014 0300 "
                 + "completed: 22/04/2014 1800 #cs2103 #todo type: BLOCK";
        assertEquals(result, task);
        
        // Test user-input "keyword"s
        task = Parser
                .parseToTask("start: due: completed: ### start: due:  "
                                     + "completed:  ### #### type: todo\n")
                .toString();
        result = "start: due: completed: ### start:  due:  "
                 + "completed:  ### #### type: TODO";
        assertEquals(result, task);

        // Test "empty" input
        task = Parser.parseToTask("### start: due: completed: type: todo\n")
                .toString();
        result = " ### start:  due:  completed:  type: TODO";
        assertEquals(result, task);

        System.out.println("...success!");
    }

    /**
     * Missing any of the keywords, including the breakpoint "###" will cause
     * errors in the Parser.
     */
    @Test(expected = AssertionError.class)
    public void failParseMissingKeywords() {
        System.out
                .println("\n>> Failing parseToTask() with missing keywords...");

        Parser.parseToTask("do work start: 20/04/2014 due: 20/04/2014 0300 "
                           + "completed: 22/04/2014 1800 #cs2103 #todo type: done\n");

        System.out.println("...test failed!");
    }

    /**
     * Having too many keywords will create problems in the Parser.
     */
    @Test(expected = AssertionError.class)
    public void failParseExtraKeywords() {
        System.out
                .println("\n>> Failing parseToTask() with extra keywords...");

        Parser.parseToTask("work ### start: 20/04/2014 due: 20/04/2014 "
                           + "completed:  #cs2103 type: todo type: done\n");

        System.out.println("...test failed!");
    }
    
    /**
     * Having incorrect input for fields will create problems in the program itself.
     */
    @Test(expected = AssertionError.class)
    public void failParseInvalidFields() {
        System.out
                .println("\n>> Failing parseToTask() with invalid input...");

        Parser.parseToTask("work ### start: fail 20/04/2014 due: 20/04/2014 "
                           + "completed: #cs2103 type: todo\n");

        System.out.println("...test failed!");
    }
}
