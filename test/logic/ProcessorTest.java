package logic;

import static org.junit.Assert.*;
import objects.DateTime;
import objects.Task;

import org.junit.Before;
import org.junit.Test;

import database.TaskType;

import java.util.ArrayList;
import java.util.List;

/**
 * Integration Testing Note: Does not include gui
 * 
 * @author Ter Yao Xiang
 */

public class ProcessorTest {

    public static Processor TestProcessor = Processor.getInstance();
    private static DateTime testTime = new DateTime("10/10/2012", "1010");
    private static List<String> testTags = new ArrayList<String>();

    private static Task testTask1 = new Task("Do CS2103 Homework",
            new DateTime(), new DateTime(), new DateTime(), testTags,
            TaskType.TODO);
    private static Task testTask2 = new Task("", testTime, new DateTime(),
            new DateTime(), new ArrayList<String>(), TaskType.TODO);
    private static Task testTask3 = new Task("", new DateTime(), testTime,
            new DateTime(), new ArrayList<String>(), TaskType.TODO);
    private static Task testTask5 = new Task("", new DateTime(),
            new DateTime(), new DateTime(), testTags, TaskType.TODO);
    private static Task testTask6 = new Task("Do CS2103 Homework", testTime,
            testTime, new DateTime(), testTags, TaskType.TODO);
    private static Task testTask7 = new Task("Do EE2020 Homework",
            new DateTime(), new DateTime(), new DateTime(),
            new ArrayList<String>(), TaskType.TODO);
    private static Task testTask8 = new Task("Do EE2020 Homework", testTime,
            new DateTime(), new DateTime(), new ArrayList<String>(),
            TaskType.TODO);
    private static Task testTask9 = new Task("Do EE2020 Homework", testTime,
            testTime, new DateTime(), new ArrayList<String>(), TaskType.TODO);
    private static Task testTask11 = new Task("Do EE2020 Homework", testTime,
            testTime, new DateTime(), testTags, TaskType.TODO);

    @Before
    public void initialiseProcessor() {
        Processor.IS_UNIT_TEST = false;
        testTags.clear();
        TestProcessor = Processor.reset();
    }

    // This method check if between task1 and task2 is identical (in terms of
    // parameters)
    public boolean equalsObj(Task task1, Task task2) {
        boolean equal = true;
        try {
            if (!task1.getName().equals(task2.getName()))
                equal = false;
            if (task1.getDue() != null && task2.getDue() != null &&
                !task1.getDue().toString().equals(task2.getDue().toString()))
                equal = false;
            if (task1.getStart() != null &&
                task2.getStart() != null &&
                !task1.getStart().toString()
                        .equals(task2.getStart().toString()))
                equal = false;
            if (task1.getCompletedOn() != null &&
                task2.getCompletedOn() != null &&
                !task1.getCompletedOn().toString()
                        .equals(task2.getCompletedOn().toString()))
                equal = false;
            if (task1.getTags() != null && task2.getTags() != null &&
                !task1.getTags().toString().equals(task2.getTags().toString()))
                equal = false;
        } catch (NullPointerException e) {
        }
        return equal;
    }

    @Test
    // Test for 'Add' Command & Inclusive of Undo/Redo
    public void testAdd() throws Exception {
        // Add with name
        TestProcessor = Processor.reset();
        Result r1 = TestProcessor.processInput("add Do CS2103 Homework");
        assertTrue(equalsObj(testTask1, r1.getTasks().get(0)));
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchAllTasks().size() == 0);
        Result r1a = TestProcessor.processInput("redo");
        assertTrue(equalsObj(testTask1, r1a.getTasks().get(0)));

        // Add with start
        Result r2 = TestProcessor.processInput("add start 10/10/2012 1010");
        assertTrue(equalsObj(testTask2, r2.getTasks().get(0)));
        TestProcessor.processInput("undo");
        Result r2a = TestProcessor.processInput("redo");
        assertTrue(equalsObj(testTask2, r2a.getTasks().get(0)));

        // Add with due
        Result r3 = TestProcessor.processInput("add due 10/10/2012 1010");
        assertTrue(equalsObj(testTask3, r3.getTasks().get(0)));
        TestProcessor.processInput("undo");
        Result r3a = TestProcessor.processInput("redo");
        assertTrue(equalsObj(testTask3, r3a.getTasks().get(0)));

        // Add with end
        Result r4 = TestProcessor.processInput("add end 10/10/2012 1010");
        assertTrue(equalsObj(testTask3, r4.getTasks().get(0)));
        TestProcessor.processInput("undo");
        Result r4a = TestProcessor.processInput("redo");
        assertTrue(equalsObj(testTask3, r4a.getTasks().get(0)));

        // Add with tags
        List<String> tagsList = new ArrayList<String>();
        Result r5 = TestProcessor.processInput("add #homework");
        tagsList.add("#homework");
        testTask5.setTags(tagsList);

        assertTrue(equalsObj(testTask5, r5.getTasks().get(0)));
        TestProcessor.processInput("undo");
        Result r5a = TestProcessor.processInput("redo");
        assertTrue(equalsObj(testTask5, r5a.getTasks().get(0)));

        // All valid parameters
        Result r6 = TestProcessor
                .processInput("add Do CS2103 Homework due 10/10/2012 1010 start 10/10/2012 1010 #homework");
        testTask6.setTags(tagsList);
        assertTrue(equalsObj(testTask6, r6.getTasks().get(0)));
        TestProcessor.processInput("undo");
        Result r6a = TestProcessor.processInput("redo");
        assertTrue(equalsObj(testTask6, r6a.getTasks().get(0)));

        // Test undo & redo of Add
        TestProcessor.processInput("undo");
        Result r8 = TestProcessor.processInput("redo");
        assertTrue(equalsObj(testTask6, r8.getTasks().get(0)));
    }

    @Test
    // Test for 'Edit' Command
    public void testEdit() throws Exception {
        TestProcessor.processInput("add Do CS2103 Homework");
        // Edit Name
        Result r1 = TestProcessor.processInput("edit 1 Do EE2020 Homework");
        assertTrue(equalsObj(r1.getTasks().get(0), testTask7));

        // Test undo & redo of Edit Name
        Result r2 = TestProcessor.processInput("undo");
        assertTrue(equalsObj(r2.getTasks().get(0), testTask1));
        Result r3 = TestProcessor.processInput("redo");
        assertTrue(equalsObj(r3.getTasks().get(0), testTask7));

        // Test Edit of Start
        TestProcessor.processInput("edit 1 start 10/10/2012 1010");
        TestProcessor.processInput("undo");
        Result r6 = TestProcessor.processInput("redo");
        assertTrue(equalsObj(r6.getTasks().get(0), testTask8));
        TestProcessor.processInput("edit 1 due 10/10/2012 1010");
        TestProcessor.processInput("undo");
        Result r9 = TestProcessor.processInput("redo");
        assertTrue(equalsObj(r9.getTasks().get(0), testTask9));

        // Test Edit Tags
        List<String> tagsList = new ArrayList<String>();
        tagsList.add("#homework");
        testTask11.setTags(tagsList);
        TestProcessor.processInput("edit 1 #homework");
        TestProcessor.processInput("undo");
        Result r15 = TestProcessor.processInput("redo");
        assertTrue(equalsObj(r15.getTasks().get(0), testTask11));

        // Test Edit of Delete:
        // Delete tag
        Result r16 = TestProcessor.processInput("edit 1 delete tags");
        assertTrue(equalsObj(r16.getTasks().get(0), testTask9));
        // Delete due
        Result r17 = TestProcessor.processInput("edit 1 delete due");
        assertTrue(equalsObj(r17.getTasks().get(0), testTask8));
        // Delete start
        Result r18 = TestProcessor.processInput("edit 1 delete start");
        assertTrue(equalsObj(r18.getTasks().get(0), testTask7));
    }

    @Test
    // Test for 'Delete' Command
    public void testDelete() throws Exception {
        TestProcessor.processInput("add Do CS2103 Homework");
        TestProcessor.processInput("add Do EE2020 Homework");
        Result r2 = TestProcessor.processInput("delete 1");
        assertTrue(equalsObj(r2.getTasks().get(0), testTask1));
        assertTrue(equalsObj(TestProcessor.fetchToDoTasks().get(0), testTask7));

        // 2. Test delete <id> where id is negative
        Result r3 = TestProcessor.processInput("delete -1");
        assertTrue(!r3.isSuccess());

        // 3. Test delete <id> where id does not exist
        Result r4 = TestProcessor.processInput("delete 3");
        assertTrue(!r4.isSuccess());

        // Test Undo & Redo of Delete <id>
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchDeletedTasks().size() == 0);

        TestProcessor.processInput("redo");
        assertTrue(equalsObj(TestProcessor.fetchToDoTasks().get(0), testTask7));

        // Test delete search
        TestProcessor.processInput("add Task2");
        TestProcessor.processInput("add Task2");
        TestProcessor.processInput("add Task3");
        TestProcessor.processInput("search task2");
        TestProcessor.processInput("delete search");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 2);

        // Undo & Redo of Delete Search
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 4);

        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 2);

    }

    @Test
    // Test for 'Restore' Command
    public void testRestore() throws Exception {
        TestProcessor.processInput("add Do CS2103 Homework");
        TestProcessor.processInput("delete 1");

        Result r1 = TestProcessor.processInput("restore 1");
        assertTrue(equalsObj(r1.getTasks().get(0), testTask1));
        assertTrue(equalsObj(TestProcessor.fetchToDoTasks().get(0), testTask1));
        assertTrue(TestProcessor.fetchDeletedTasks().size() == 0);
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);

        // Begins Undo & Redo Test of Restore <id>
        TestProcessor.processInput("undo");
        assertTrue(equalsObj(TestProcessor.fetchDeletedTasks().get(0),
                             testTask1));
        assertTrue(TestProcessor.fetchDeletedTasks().size() == 1);
        assertTrue(TestProcessor.fetchToDoTasks().size() == 0);

        TestProcessor.processInput("redo");
        assertTrue(equalsObj(TestProcessor.fetchToDoTasks().get(0), testTask1));
        assertTrue(TestProcessor.fetchDeletedTasks().size() == 0);
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
    }

    @Test
    // Test for 'Display'/'Show' Command
    public void testDisplay() throws Exception {
        // Test display
        TestProcessor.processInput("add Do CS2103 Homework");
        Result r1 = TestProcessor.processInput("display");
        assertTrue(equalsObj(r1.getTasks().get(0), testTask1));
        assertEquals(r1.getTasks().size(), 1);

        TestProcessor.processInput("add Do EE2020 Homework");
        Result r4 = TestProcessor.processInput("display 2");
        assertTrue(equalsObj(r4.getTasks().get(0), testTask7));
        assertEquals(r4.getTasks().size(), 1);

        // Test show
        Result r5 = TestProcessor.processInput("show");
        assertEquals(r5.getTasks().size(), 2);
    }

    @Test
    // Test for 'Search' Command
    public void testSearch() throws Exception {
        TestProcessor.processInput("add Do CS2103 Homework #cshomework");
        TestProcessor.processInput("add Do CS2103 Homework #cshomework");
        TestProcessor.processInput("add Do CS2103 Homework #cshomework");
        TestProcessor.processInput("add Do CS2103 Homework");
        TestProcessor.processInput("add Do CS2103 Homework");
        TestProcessor.processInput("add Do CS2103 Homework");

        // Test search by tags
        Result r6 = TestProcessor.processInput("search #cshomework");
        assertTrue(r6.getTasks().size() == 3);

        // Test search by keyword
        Result r7 = TestProcessor.processInput("search cs2103");
        assertTrue(r7.getTasks().size() == 6);

        // Test search by keyword OR tags
        Result r8 = TestProcessor.processInput("search cs2103 #cshomework");
        assertTrue(r8.getTasks().size() == 3);
    }

    @Test
    // Boundary Test Case: Consecutive undo/redo for delete search
    public void testDeleteSearchUndoRedo() throws Exception {
        TestProcessor.processInput("add Task1");
        TestProcessor.processInput("add Task1");
        TestProcessor.processInput("add Task2");
        TestProcessor.processInput("add Task2");
        TestProcessor.processInput("add Task2");
        TestProcessor.processInput("add Task3");

        Result r6 = TestProcessor.processInput("search task2");
        assertTrue(r6.getTasks().size() == 3);
        TestProcessor.processInput("delete search");

        TestProcessor.processInput("search task1");
        TestProcessor.processInput("delete search");
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 3);
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 6);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 3);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
    }

    @Test
    public void testDone() throws Exception {
        TestProcessor.processInput("add Task1");
        TestProcessor.processInput("add Task2");
        TestProcessor.processInput("add Task3");

        // Equivalence Partition
        assertTrue(TestProcessor.fetchToDoTasks().size() == 3);
        // 1. Test done <id>
        TestProcessor.processInput("done 1");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 2);
        TestProcessor.processInput("done 3");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
        // 2. Test invalid done <id>
        TestProcessor.processInput("done -1");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
        TestProcessor.processInput("done 0");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
        TestProcessor.processInput("done 3");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
        TestProcessor.processInput("done 5");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);

        // Test undo and redo of done
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 2);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 2);
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 3);
    }

    @Test
    public void testTodo() throws Exception {
        TestProcessor.processInput("add Task1");
        TestProcessor.processInput("add Task2");
        TestProcessor.processInput("add Task3");

        // Test todo <id>
        TestProcessor.processInput("done 1");
        TestProcessor.processInput("done 2");
        TestProcessor.processInput("done 3");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 0);

        // Test todo <id>
        TestProcessor.processInput("todo 1");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
        TestProcessor.processInput("todo 2");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 2);
        TestProcessor.processInput("todo 2");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 2);
        TestProcessor.processInput("todo 0");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 2);
        TestProcessor.processInput("todo -1");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 2);

        // Test undo and redo of Todo
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 2);
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 0);
    }

    @Test
    public void testDoneDate() throws Exception {
        TestProcessor.processInput("add Task1 due 11/10/2014");
        TestProcessor.processInput("add Task2 due 11/10/2014");
        TestProcessor.processInput("add Task3");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 3);
        TestProcessor.processInput("done 11/10/2014");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);

        // Test Undo and Redo of Done by date
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 3);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.fetchToDoTasks().size() == 1);
    }

    @Test
    public void testDisplayShowValid() throws Exception {
        TestProcessor.processInput("block Task1 from 09/10/2014 to 09/10/2014");
        TestProcessor.processInput("add Task2 due 11/10/2014");
        TestProcessor.processInput("add Task3");
        // Test Show Block
        Result r1 = TestProcessor.processInput("show block");
        assertTrue(r1.getTasks().size() == 1);

        // Test Show All
        Result r2 = TestProcessor.processInput("show");
        assertTrue(r2.getTasks().size() == 3);

        // Test Show Done
        Result r3 = TestProcessor.processInput("show done");
        assertTrue(r3.getTasks().size() == 0);
        TestProcessor.processInput("done 2");
        Result r4 = TestProcessor.processInput("show done");
        assertTrue(r4.getTasks().size() == 1);

        // Test Show Deleted
        Result r5 = TestProcessor.processInput("show deleted");
        assertTrue(r5.getTasks().size() == 0);
        TestProcessor.processInput("delete 2");
        Result r6 = TestProcessor.processInput("show deleted");
        assertTrue(r6.getTasks().size() == 1);

        // Test Show Search
        TestProcessor.processInput("search task");
        Result r7 = TestProcessor.processInput("show search");
        assertTrue(r7.getTasks().size() == 1);

        // Test Show Someday
        Result r8 = TestProcessor.processInput("show someday");
        assertTrue(r8.getTasks().size() == 1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisplayShowInvalid() throws Exception {
        TestProcessor.processInput("block Task1 from 09/10/2014 to 09/10/2014");
        TestProcessor.processInput("add Task2 due 11/10/2014");
        TestProcessor.processInput("add Task3");

        // Test Invalid input
        Result r9 = TestProcessor.processInput("show somday");
        assertFalse(r9.isSuccess());
    }

    @Test(expected = Error.class)
    public void testAddWithBlock() throws Exception {
        TestProcessor
                .processInput("block Block1 from 09/10/2014 to 09/10/2014");
        TestProcessor.processInput("add Task2 due 09/10/2014");
    }

    @Test
    public void testAddWithBlock2() throws Exception {
        // Test Addition of more than one block date
        TestProcessor
                .processInput("block Block1 from 09/10/2014 to 09/10/2014");
        TestProcessor
                .processInput("block Block2 from 10/10/2014 to 10/10/2014");
    }

    @Test(expected = Error.class)
    public void testAddWithBlock3() throws Exception {
        // Boundary case test - Tasks with time and no time
        // Expected to fail
        TestProcessor
                .processInput("block Block1 from 09/10/2014 to 09/10/2014");
        TestProcessor
                .processInput("block Block2 from 10/10/2014 to 10/10/2014");
        TestProcessor
                .processInput("block Block3 from 09/10/2014 0900 to 10/10/2014 1000");
    }

    @Test
    public void testBlockUndoRedo() throws Exception {
        TestProcessor.processInput("block Task1 from 09/10/2014 to 09/10/2014");
        assertTrue(TestProcessor.fetchBlockTasks().size() == 1);
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.fetchBlockTasks().size() == 0);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.fetchBlockTasks().size() == 1);
    }
}
