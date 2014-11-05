package database;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

/**
 * Unit tests for the Task class.
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class TaskTest {

    @Test
    public void testDefaultConstructorHasEmptyAttributes() {
        Task task = new Task();
        assertEquals("Blank ID", 0, task.getId());
        assertTrue("Empty name", task.getName().isEmpty());
        assertTrue("Empty start DateTime", task.getStart().isEmpty());
        assertTrue("Empty due DateTime", task.getDue().isEmpty());
        assertTrue("Empty completedOn DateTime", task.getCompletedOn()
                .isEmpty());
        assertTrue("Empty tags", task.getTags().isEmpty());
        assertTrue("Default type", task.isToDo());
        assertFalse("Not deleted", task.isDeleted());
    }

    @Test
    public void testConstructorStoresArgs() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        assertEquals("Same name", name, task.getName());
        assertEquals("Same start", start, task.getStart());
        assertEquals("Same due", due, task.getDue());
        assertEquals("Same completedOn", completedOn, task.getCompletedOn());
        assertEquals("Same tags", tags, task.getTags());
        assertEquals("Same type", type, task.getType());

        name = "";
        start = new DateTime();
        due = new DateTime();
        completedOn = new DateTime();
        tags.clear();
        type = TaskType.BLOCK;

        // Check that values are cloned by value
        assertEquals("Cloned name by value", "Do more unit tests",
                     task.getName());
        assertEquals("Cloned start by value", "11/12/2014 1111", task
                .getStart().toString());
        assertEquals("Cloned due by value", "13/12/2014 2222", task.getDue()
                .toString());
        assertEquals("Cloned completedOn by value", "14/12/2014 2359", task
                .getCompletedOn().toString());
        assertTrue("Cloned tags by value",
                   task.getTags().contains("#tests4lyfe"));
        assertTrue("Cloned type by value", task.isDone());
    }

    @Test
    public void testCloningConstructorClonesByValue() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task1 = new Task(name, start, due, completedOn, tags, type);
        Task task2 = new Task(task1);

        assertEquals("Same name", task1.getName(), task2.getName());
        assertEquals("Same start", task1.getStart(), task2.getStart());
        assertEquals("Same due", task1.getDue(), task2.getDue());
        assertEquals("Same completedOn", task1.getCompletedOn(),
                     task2.getCompletedOn());
        assertEquals("Same tags", task1.getTags(), task2.getTags());
        assertEquals("Same type", task1.getType(), task2.getType());
        assertTrue("Equal objects", task1.equals(task2));

        name = "";
        start.resetDateTime();
        due.resetDateTime();
        completedOn.resetDateTime();
        tags.add("haystack");
        type = TaskType.BLOCK;

        task1.setName(name);
        task1.setStart(start);
        task1.setDue(due);
        task1.setTags(tags);
        task1.setType(TaskType.TODO);

        // Check that values are cloned by value
        assertEquals("Cloned name by value", "Do more unit tests",
                     task2.getName());
        assertEquals("Cloned start by value", "11/12/2014 1111", task2
                .getStart().toString());
        assertEquals("Cloned due by value", "13/12/2014 2222", task2.getDue()
                .toString());
        assertEquals("Cloned completedOn by value", "14/12/2014 2359", task2
                .getCompletedOn().toString());
        assertFalse("Cloned tags by value",
                    task2.getTags().contains("#haystack"));
        assertTrue("Cloned type by value", task2.isDone());
        assertFalse("Not equal objects", task1.equals(task2));
    }

    @Test
    public void testCompareTo() {
        final String DATE1 = "01/01/2014";
        final String DATE2 = "02/01/2014";
        final String TIME = "0000";

        final DateTime DT1 = new DateTime(DATE1, TIME);
        final DateTime DT2 = new DateTime(DATE2, TIME);
        final DateTime emptyDT = new DateTime();

        String name = "";
        DateTime completedOn = emptyDT;
        List<String> tags = new ArrayList<>();
        TaskType type = TaskType.TODO;

        // Comparing due DateTimes only
        Task task1 = new Task(name, emptyDT, DT1, completedOn, tags, type);
        Task task2 = new Task(name, emptyDT, DT2, completedOn, tags, type);
        assertTrue("First is earlier than second", task1.compareTo(task2) < 0);
        assertTrue("Second is later than first", task2.compareTo(task1) > 0);

        // Comparing start DateTimes only
        task1 = new Task(name, DT1, emptyDT, completedOn, tags, type);
        task2 = new Task(name, DT2, emptyDT, completedOn, tags, type);
        assertTrue("First is earlier than second", task1.compareTo(task2) < 0);
        assertTrue("Second is later than first", task2.compareTo(task1) > 0);

        // Comparing start with due DateTimes
        task1 = new Task(name, DT1, emptyDT, completedOn, tags, type);
        task2 = new Task(name, emptyDT, DT2, completedOn, tags, type);
        assertTrue("First is earlier than second", task1.compareTo(task2) < 0);
        assertTrue("Second is later than first", task2.compareTo(task1) > 0);

        // Comparing due with start DateTimes
        task1 = new Task(name, emptyDT, DT1, completedOn, tags, type);
        task2 = new Task(name, DT2, emptyDT, completedOn, tags, type);
        assertTrue("First is earlier than second", task1.compareTo(task2) < 0);
        assertTrue("Second is later than first", task2.compareTo(task1) > 0);
    }

    @Test
    public void testContains() {
    }

    @Test
    public void testEquals() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        // Different values
        String emptyName = "";
        DateTime emptyDateTime = new DateTime();
        List<String> emptyTags = new ArrayList<>();
        TaskType toDoType = TaskType.TODO;

        Task task1 = new Task(name, start, due, completedOn, tags, type);
        Task task2 = new Task(name, start, due, completedOn, tags, type);
        Task task3 = new Task(emptyName, start, due, completedOn, tags, type);
        Task task4 = new Task(name, emptyDateTime, due, completedOn, tags, type);
        Task task5 = new Task(name, start, emptyDateTime, completedOn, tags,
                type);
        Task task6 = new Task(name, start, due, emptyDateTime, tags, type);
        Task task7 = new Task(name, start, due, completedOn, emptyTags, type);
        Task task8 = new Task(name, start, due, completedOn, tags, toDoType);

        assertFalse("Different types", task1.equals(new DateTime()));
        assertTrue("Equals to self", task1.equals(task1));
        assertTrue("Equal values", task1.equals(task2));
        assertFalse("Different name", task1.equals(task3));
        assertFalse("Different start", task1.equals(task4));
        assertFalse("Different due", task1.equals(task5));
        assertFalse("Different completedOn", task1.equals(task6));
        assertFalse("Different tags", task1.equals(task7));
        assertFalse("Different type", task1.equals(task8));
    }

    /**
     * Checks if hashCode function follows the following general contracts of
     * hashCode:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during an
     * execution of a Java application, the hashCode method must consistently
     * return the same integer
     * <li>Check if two DateTime objects are equal according to the equals(Task)
     * method, then calling the hashCode method on each of the two objects must
     * produce the same integer result.
     * </ul>
     */
    @Test
    public void testHashCode() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task1 = new Task(name, start, due, completedOn, tags, type);
        Task task2 = new Task(name, start, due, completedOn, tags, type);
        Task task3 = new Task("", start, due, completedOn, tags, type);

        // Testing equal self, i.e. consistent value across different calls.
        assertTrue("Equal values", task1.equals(task1));
        assertEquals("Equal hashcodes", task1.hashCode(), task1.hashCode());

        // Testing equal values.
        assertTrue("Equal values", task1.equals(task2));
        assertEquals("Equal hashcodes", task1.hashCode(), task2.hashCode());

        // Testing non-equal values.
        assertFalse("Equal values", task1.equals(task3));
        assertNotEquals("Not equal hashcodes", task1.hashCode(),
                        task3.hashCode());
    }

    @Test
    public void testToString() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        assertEquals("Matches toString pattern",
                     "Do more unit tests ### start: 11/12/2014 1111 due: 13/12/2014 2222 "
                             + "completed: 14/12/2014 2359 #tests4lyfe type: DONE",
                     task.toString());

        name = "";
        start = new DateTime();
        due = new DateTime();
        completedOn = new DateTime();
        tags.clear();
        type = TaskType.BLOCK;

        task = new Task(name, start, due, completedOn, tags, type);

        assertEquals("Matches toString pattern",
                     " ### start:  due:  completed:  type: BLOCK",
                     task.toString());
    }

    @Test
    public void testGetSummary() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        assertEquals("Matches summary format",
                     "Do more unit tests 11/12/2014 1111 13/12/2014 2222 "
                             + "#tests4lyfe DONE", task.getSummary());
    }

    @Test
    public void testDecrementId() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task1 = new Task(name, start, due, completedOn, tags, type);
        Task task2 = new Task(name, start, due, completedOn, tags, type);
        assertTrue("Newer object has higher ID", task2.getId() > task1.getId());

        Task.decrementId();
        Task task3 = new Task(name, start, due, completedOn, tags, type);
        assertTrue("ID decremented by 1", task2.getId() == task3.getId());
    }

    @Test
    public void testResetId() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task1 = new Task(name, start, due, completedOn, tags, type);
        Task task2 = new Task(name, start, due, completedOn, tags, type);
        assertTrue("Newer object has higher ID", task2.getId() > task1.getId());

        Task.resetId();
        Task task3 = new Task(name, start, due, completedOn, tags, type);
        assertTrue("ID reset to 1", task3.getId() == 1);
    }

    @Test
    public void testResetAttributes() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        task.resetName();
        assertTrue("Name is reset", task.getName().isEmpty());
        assertFalse("Original varaible is not affected", name.isEmpty());

        task.resetStart();
        assertTrue("Start is reset", task.getStart().isEmpty());
        assertFalse("Original varaible is not affected", start.isEmpty());

        task.resetDue();
        assertTrue("Due is reset", task.getDue().isEmpty());
        assertFalse("Original varaible is not affected", due.isEmpty());

        task.setType(TaskType.TODO);
        assertTrue("CompletedOn is reset", task.getCompletedOn().isEmpty());
        assertFalse("Original varaible is not affected", completedOn.isEmpty());

        task.resetTags();
        assertTrue("Tags are reset", task.getTags().isEmpty());
        assertFalse("Original varaible is not affected", tags.isEmpty());
    }

    @Test
    public void testTypeGettersAndSetters() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime();
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.TODO;

        Task task = new Task(name, start, due, completedOn, tags, type);
        assertTrue("Is todo task", task.isToDo());
        assertFalse("Not done task", task.isDone());
        assertFalse("Not block task", task.isBlock());
        assertTrue("CompletedOn is empty", task.getCompletedOn().isEmpty());

        task.setType(TaskType.DONE);
        assertFalse("Not todo task", task.isToDo());
        assertTrue("Is done task", task.isDone());
        assertFalse("Not block task", task.isBlock());
        assertFalse("CompletedOn is not empty", task.getCompletedOn().isEmpty());

        DateTime originalCompletedOn = task.getCompletedOn();
        task.setType(TaskType.DONE);
        assertEquals("CompletedOn did not change", originalCompletedOn,
                     task.getCompletedOn());

        task.setType(TaskType.BLOCK);
        assertFalse("Not todo task", task.isToDo());
        assertFalse("Not done task", task.isDone());
        assertTrue("Is block task", task.isBlock());
        assertTrue("CompletedOn is empty", task.getCompletedOn().isEmpty());
    }

    @Test
    public void testDeletedGetterAndSetter() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;
        
        Task task = new Task(name, start, due, completedOn, tags, type);
        assertFalse("Task is not deleted", task.isDeleted());
        
        task.setDeleted(true);
        assertTrue("Task is deleted", task.isDeleted());
    }

    @Test
    public void testIsFloating() {
        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;
        
        Task task = new Task(name, start, due, completedOn, tags, type);
        assertFalse("Not floating task", task.isFloating());
        
        task.resetStart();
        assertFalse("Not floating task", task.isFloating());
        
        task.setStart(start);
        task.resetDue();
        assertFalse("Not floating task", task.isFloating());
        
        task.resetStart();
        assertTrue("Is floating task", task.isFloating());
    }
}