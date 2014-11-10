package database;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DatabaseLogicTest {

    private final DateTime EMPTY_DT = new DateTime();
    private final DateTime START = new DateTime("01/01/2014", "0000");
    private final DateTime DUE = new DateTime("02/01/2014", "0000");
    private final DateTime COMPLETE = new DateTime("03/01/2014", "0000");
    private final List<String> EMPTY_TAGS = new ArrayList<>();

    @Test
    public void testPopulationOfTaskLists() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        Task toDoTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);
        Task doneTask = new Task("", EMPTY_DT, EMPTY_DT, COMPLETE, EMPTY_TAGS,
                TaskType.DONE);
        Task blockTask = new Task("", START, DUE, EMPTY_DT, EMPTY_TAGS,
                TaskType.BLOCK);

        List<Task> tasks = new ArrayList<>();
        tasks.add(toDoTask);
        tasks.add(doneTask);
        tasks.add(blockTask);

        db.populateTaskLists(tasks);

        assertTrue("allTasks list contains all 3 Task objects", db
                .getAllTasks().containsAll(tasks));

        assertTrue("toDoTasks list contains todo Task object", db
                .getToDoTasks().contains(toDoTask));

        assertTrue("doneTasks list contains done Task object", db
                .getDoneTasks().contains(doneTask));

        assertTrue("blockTasks list contains block Task object", db
                .getBlockTasks().contains(blockTask));

        assertTrue("deletedTasks list is empty", db.getDeletedTasks().isEmpty());
    }

    @Test
    public void testPermanentlyDeleteAllTasks() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        Task toDoTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);
        Task doneTask = new Task("", EMPTY_DT, EMPTY_DT, COMPLETE, EMPTY_TAGS,
                TaskType.DONE);
        Task blockTask = new Task("", START, DUE, EMPTY_DT, EMPTY_TAGS,
                TaskType.BLOCK);
        Task deletedTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT,
                EMPTY_TAGS, TaskType.TODO);

        db.add(toDoTask);
        db.add(doneTask);
        db.add(blockTask);
        db.add(deletedTask);

        db.delete(deletedTask);

        assertFalse("allTasks list is not empty", db.getAllTasks().isEmpty());
        assertFalse("toDoTasks list is not empty", db.getToDoTasks().isEmpty());
        assertFalse("doneTasks list is not empty", db.getDoneTasks().isEmpty());
        assertFalse("blockTasks list is not empty", db.getBlockTasks()
                .isEmpty());
        assertFalse("deletedTasks list is not empty", db.getDeletedTasks()
                .isEmpty());

        // Clear static lists containing Task objects
        db.permanentlyDeleteAllTasks();

        assertTrue("allTasks list is empty", db.getAllTasks().isEmpty());
        assertTrue("toDoTasks list is empty", db.getToDoTasks().isEmpty());
        assertTrue("doneTasks list is empty", db.getDoneTasks().isEmpty());
        assertTrue("blockTasks list is empty", db.getBlockTasks().isEmpty());
        assertTrue("deletedTasks list is empty", db.getDeletedTasks().isEmpty());
    }

    @Test
    public void testSearchTaskById() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        Task task1 = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);
        Task task2 = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);
        Task task3 = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);

        db.add(task1);
        db.add(task2);
        db.add(task3);

        assertEquals(task2, db.searchTaskById(2));
        assertEquals(task3, db.searchTaskById(3));
        assertEquals(task1, db.searchTaskById(1));

        // Erroneous boundary cases
        assertNull("Task object does not exist", db.searchTaskById(0));
        assertNull("Task object does not exist", db.searchTaskById(4));
    }

    @Test
    public void testGetAllTaskInfo() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task1 = new Task(name, start, due, completedOn, tags, type);

        name = "Recess week";
        start = new DateTime("15/11/2014", "0000");
        due = new DateTime("23/11/2014", "2359");
        completedOn = EMPTY_DT;
        tags.clear();
        tags.add("#funStuff");
        tags.add("#studystudystudy");
        type = TaskType.BLOCK;

        Task task2 = new Task(name, start, due, completedOn, tags, type);

        name = "Testing is time consuming";
        start = new DateTime("01/01/2015", "0000");
        due = new DateTime("31/01/2015", "1000");
        completedOn = EMPTY_DT;
        tags.clear();
        tags.add("#hahaha");
        type = TaskType.TODO;

        Task task3 = new Task(name, start, due, completedOn, tags, type);

        db.add(task1);
        db.add(task2);
        db.add(task3);

        assertEquals("Testing is time consuming ### start: 01/01/2015 0000 "
                             + "due: 31/01/2015 1000 completed:  #hahaha type: TODO\n"
                             + "Do more unit tests ### start: 11/12/2014 1111 "
                             + "due: 13/12/2014 2222 completed: 14/12/2014 2359 #tests4lyfe type: DONE\n"
                             + "Recess week ### start: 15/11/2014 0000 "
                             + "due: 23/11/2014 2359 completed:  #funStuff #studystudystudy type: BLOCK\n",
                     db.getAllTaskInfo());
    }

    @Test
    public void testAdd() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task1 = new Task(name, start, due, completedOn, tags, type);

        name = "Recess week";
        start = new DateTime("15/11/2014", "0000");
        due = new DateTime("23/11/2014", "2359");
        completedOn = EMPTY_DT;
        tags.clear();
        tags.add("#funStuff");
        tags.add("#studystudystudy");
        type = TaskType.BLOCK;

        Task task2 = new Task(name, start, due, completedOn, tags, type);

        name = "Testing is time consuming";
        start = new DateTime("01/01/2015", "0000");
        due = new DateTime("31/01/2015", "1000");
        completedOn = EMPTY_DT;
        tags.clear();
        tags.add("#hahaha");
        type = TaskType.TODO;

        Task task3 = new Task(name, start, due, completedOn, tags, type);

        db.add(task1);
        assertTrue("Added to all list", db.getAllTasks().contains(task1));
        assertTrue("Added to done list", db.getDoneTasks().contains(task1));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(task1));
        assertFalse("Not in block list", db.getBlockTasks().contains(task1));
        assertFalse("Not in deleted list", db.getDeletedTasks().contains(task1));

        db.add(task2);
        assertTrue("Added to all list", db.getAllTasks().contains(task2));
        assertTrue("Added to block list", db.getBlockTasks().contains(task2));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(task2));
        assertFalse("Not in done list", db.getDoneTasks().contains(task2));
        assertFalse("Not in deleted list", db.getDeletedTasks().contains(task2));

        db.add(task3);
        assertTrue("Added to all list", db.getAllTasks().contains(task3));
        assertTrue("Added to toDo list", db.getToDoTasks().contains(task3));
        assertFalse("Not in done list", db.getDoneTasks().contains(task3));
        assertFalse("Not in block list", db.getBlockTasks().contains(task3));
        assertFalse("Not in deleted list", db.getDeletedTasks().contains(task3));
    }

    @Test(expected = java.lang.AssertionError.class)
    public void testAddingExistingTaskAssertionError() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task1 = new Task(name, start, due, completedOn, tags, type);
        Task task2 = new Task(task1);
        assertTrue(task1.equals(task2));

        db.add(task1);
        db.add(task2); // Adding equal object, should trigger assert
    }

    @Test
    public void testEditNull() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        assertFalse("Cannot edit null", db.edit(null, null, null, null, null));
    }

    @Test
    public void testEditResetName() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        db.add(task);
        assertTrue("Edited obj",
                   db.edit(task, null, EMPTY_DT, EMPTY_DT, EMPTY_TAGS));
        assertEquals("Name reset", "", task.getName());
        assertEquals("No change to start", start, task.getStart());
        assertEquals("No change to due", due, task.getDue());
        assertEquals("No change to completedOn", completedOn,
                     task.getCompletedOn());
        assertEquals("No change to tags", tags, task.getTags());
        assertEquals("No change to type", type, task.getType());
    }

    @Test
    public void testEditSetName() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        db.add(task);
        assertTrue("Edited obj",
                   db.edit(task, "hi", EMPTY_DT, EMPTY_DT, EMPTY_TAGS));
        assertEquals("Changed name", "hi", task.getName());
        assertEquals("No change to start", start, task.getStart());
        assertEquals("No change to due", due, task.getDue());
        assertEquals("No change to completedOn", completedOn,
                     task.getCompletedOn());
        assertEquals("No change to tags", tags, task.getTags());
        assertEquals("No change to type", type, task.getType());
    }

    @Test
    public void testEditResetStart() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        db.add(task);
        assertTrue("Edited obj", db.edit(task, "", null, EMPTY_DT, EMPTY_TAGS));
        assertEquals("Reset start", EMPTY_DT, task.getStart());
        assertEquals("No change to name", name, task.getName());
        assertEquals("No change to due", due, task.getDue());
        assertEquals("No change to completedOn", completedOn,
                     task.getCompletedOn());
        assertEquals("No change to tags", tags, task.getTags());
        assertEquals("No change to type", type, task.getType());
    }

    @Test
    public void testEditSetStart() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        db.add(task);
        assertTrue("Edited obj", db.edit(task, "", due, EMPTY_DT, EMPTY_TAGS));
        assertEquals("Changed start", due, task.getStart());
        assertEquals("No change to name", name, task.getName());
        assertEquals("No change to due", due, task.getDue());
        assertEquals("No change to completedOn", completedOn,
                     task.getCompletedOn());
        assertEquals("No change to tags", tags, task.getTags());
        assertEquals("No change to type", type, task.getType());
    }

    @Test
    public void testEditResetDue() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        db.add(task);
        assertTrue("Edited obj", db.edit(task, "", EMPTY_DT, null, EMPTY_TAGS));
        assertEquals("Reset due", EMPTY_DT, task.getDue());
        assertEquals("No change to name", name, task.getName());
        assertEquals("No change to start", start, task.getStart());
        assertEquals("No change to completedOn", completedOn,
                     task.getCompletedOn());
        assertEquals("No change to tags", tags, task.getTags());
        assertEquals("No change to type", type, task.getType());
    }

    @Test
    public void testEditSetDue() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        db.add(task);
        assertTrue("Edited obj", db.edit(task, "", EMPTY_DT, start, EMPTY_TAGS));
        assertEquals("Changed due", start, task.getDue());
        assertEquals("No change to name", name, task.getName());
        assertEquals("No change to start", start, task.getStart());
        assertEquals("No change to completedOn", completedOn,
                     task.getCompletedOn());
        assertEquals("No change to tags", tags, task.getTags());
        assertEquals("No change to type", type, task.getType());
    }

    @Test
    public void testEditResetTags() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        db.add(task);
        assertTrue("Edited obj", db.edit(task, "", EMPTY_DT, EMPTY_DT, null));
        assertEquals("Reset tags", EMPTY_TAGS, task.getTags());
        assertEquals("No change to name", name, task.getName());
        assertEquals("No change to start", start, task.getStart());
        assertEquals("No change to due", due, task.getDue());
        assertEquals("No change to completedOn", completedOn,
                     task.getCompletedOn());
        assertEquals("No change to type", type, task.getType());
    }

    @Test
    public void testEditSetTags() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        String name = "Do more unit tests";
        DateTime start = new DateTime("11/12/2014", "1111");
        DateTime due = new DateTime("13/12/2014", "2222");
        DateTime completedOn = new DateTime("14/12/2014", "2359");
        List<String> tags = new ArrayList<>();
        tags.add("#tests4lyfe");
        TaskType type = TaskType.DONE;

        Task task = new Task(name, start, due, completedOn, tags, type);

        List<String> newTags = new ArrayList<>();
        newTags.add("#haha");
        newTags.add("#NO");

        db.add(task);
        assertTrue("Edited obj", db.edit(task, "", EMPTY_DT, EMPTY_DT, newTags));
        assertEquals("Changed tags", newTags, task.getTags());
        assertEquals("No change to name", name, task.getName());
        assertEquals("No change to start", start, task.getStart());
        assertEquals("No change to due", due, task.getDue());
        assertEquals("No change to completedOn", completedOn,
                     task.getCompletedOn());
        assertEquals("No change to type", type, task.getType());
    }

    @Test
    public void testDelete() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        Task toDoTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);
        Task doneTask = new Task("", EMPTY_DT, EMPTY_DT, COMPLETE, EMPTY_TAGS,
                TaskType.DONE);
        Task blockTask = new Task("", START, DUE, EMPTY_DT, EMPTY_TAGS,
                TaskType.BLOCK);

        db.add(toDoTask);
        db.add(doneTask);
        db.add(blockTask);

        assertTrue("Deleted toDo obj", db.delete(toDoTask));
        assertTrue("In all list", db.getAllTasks().contains(toDoTask));
        assertTrue("In deleted list", db.getDeletedTasks().contains(toDoTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(toDoTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(toDoTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(toDoTask));

        assertTrue("Deleted done obj", db.delete(doneTask));
        assertTrue("In all list", db.getAllTasks().contains(doneTask));
        assertTrue("In deleted list", db.getDeletedTasks().contains(doneTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(doneTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(doneTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(doneTask));

        assertTrue("Deleted toDo obj", db.delete(blockTask));
        assertTrue("In all list", db.getAllTasks().contains(blockTask));
        assertTrue("In deleted list", db.getDeletedTasks().contains(blockTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(blockTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(blockTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(blockTask));

        assertFalse("Cannot delete null obj", db.delete(null));
        assertFalse("Cannot delete deleted obj", db.delete(toDoTask));
    }

    @Test
    public void testRestore() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        Task toDoTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);
        Task doneTask = new Task("", EMPTY_DT, EMPTY_DT, COMPLETE, EMPTY_TAGS,
                TaskType.DONE);
        Task blockTask = new Task("", START, DUE, EMPTY_DT, EMPTY_TAGS,
                TaskType.BLOCK);

        db.add(toDoTask);
        db.add(doneTask);
        db.add(blockTask);

        db.delete(toDoTask);
        db.delete(doneTask);
        db.delete(blockTask);

        assertTrue("Restored toDo obj", db.restore(toDoTask));
        assertTrue("In all list", db.getAllTasks().contains(toDoTask));
        assertTrue("In toDo list", db.getToDoTasks().contains(toDoTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(toDoTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(toDoTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(toDoTask));

        assertTrue("Restored done obj", db.restore(doneTask));
        assertTrue("In all list", db.getAllTasks().contains(doneTask));
        assertTrue("In done list", db.getDoneTasks().contains(doneTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(doneTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(doneTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(doneTask));

        assertTrue("Restored toDo obj", db.restore(blockTask));
        assertTrue("In all list", db.getAllTasks().contains(blockTask));
        assertTrue("In block list", db.getBlockTasks().contains(blockTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(blockTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(blockTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(blockTask));

        assertFalse("Cannot restore null obj", db.restore(null));
        assertFalse("Cannot restore restored obj", db.restore(toDoTask));
    }

    @Test
    public void testPermanentlyDelete() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        Task toDoTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);
        Task doneTask = new Task("", EMPTY_DT, EMPTY_DT, COMPLETE, EMPTY_TAGS,
                TaskType.DONE);
        Task blockTask = new Task("", START, DUE, EMPTY_DT, EMPTY_TAGS,
                TaskType.BLOCK);
        Task deletedTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT,
                EMPTY_TAGS, TaskType.TODO);

        db.add(toDoTask);
        db.add(doneTask);
        db.add(blockTask);
        db.add(deletedTask);
        db.delete(deletedTask);

        assertTrue("Perm deleted toDo obj", db.permanentlyDelete(toDoTask));
        assertFalse("Not in all list", db.getAllTasks().contains(toDoTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(toDoTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(toDoTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(toDoTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(toDoTask));

        assertTrue("Perm deleted done obj", db.permanentlyDelete(doneTask));
        assertFalse("Not in all list", db.getAllTasks().contains(doneTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(doneTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(doneTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(doneTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(doneTask));

        assertTrue("Perm deleted block obj", db.permanentlyDelete(blockTask));
        assertFalse("Not in all list", db.getAllTasks().contains(blockTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(blockTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(blockTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(blockTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(blockTask));

        assertTrue("Perm deleted deleted obj",
                   db.permanentlyDelete(deletedTask));
        assertFalse("Not in all list", db.getAllTasks().contains(deletedTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(deletedTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(deletedTask));
        assertFalse("Not in block list",
                    db.getBlockTasks().contains(deletedTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(deletedTask));

        assertFalse("Cannot delete null obj", db.permanentlyDelete(null));
    }

    @Test
    public void testMarkToDo() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        Task toDoTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);
        Task doneTask = new Task("", EMPTY_DT, EMPTY_DT, COMPLETE, EMPTY_TAGS,
                TaskType.DONE);
        Task blockTask = new Task("", START, DUE, EMPTY_DT, EMPTY_TAGS,
                TaskType.BLOCK);
        Task deletedTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT,
                EMPTY_TAGS, TaskType.TODO);

        db.add(toDoTask);
        db.add(doneTask);
        db.add(blockTask);
        db.add(deletedTask);
        db.delete(deletedTask);

        assertFalse("Cannot mark null obj as todo", db.markToDo(null));
        assertFalse("Cannot mark undeleted todo obj", db.markToDo(toDoTask));

        assertTrue("Marked done obj as todo", db.markToDo(doneTask));
        assertTrue("In all list", db.getAllTasks().contains(doneTask));
        assertTrue("In toDo list", db.getToDoTasks().contains(doneTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(doneTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(doneTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(doneTask));

        assertTrue("Marked block obj as todo", db.markToDo(blockTask));
        assertTrue("In all list", db.getAllTasks().contains(blockTask));
        assertTrue("In toDo list", db.getToDoTasks().contains(blockTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(blockTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(blockTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(blockTask));

        assertTrue("Marked deleted obj as todo", db.markToDo(deletedTask));
        assertTrue("In all list", db.getAllTasks().contains(deletedTask));
        assertTrue("In toDo list", db.getToDoTasks().contains(deletedTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(deletedTask));
        assertFalse("Not in block list",
                    db.getBlockTasks().contains(deletedTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(deletedTask));
    }

    @Test
    public void testMarkDone() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        Task toDoTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);
        Task doneTask = new Task("", EMPTY_DT, EMPTY_DT, COMPLETE, EMPTY_TAGS,
                TaskType.DONE);
        Task blockTask = new Task("", START, DUE, EMPTY_DT, EMPTY_TAGS,
                TaskType.BLOCK);
        Task deletedTask = new Task("", EMPTY_DT, EMPTY_DT, COMPLETE,
                EMPTY_TAGS, TaskType.DONE);

        db.add(toDoTask);
        db.add(doneTask);
        db.add(blockTask);
        db.add(deletedTask);
        db.delete(deletedTask);

        assertFalse("Cannot mark null obj as done", db.markDone(null));
        assertFalse("Cannot mark undeleted done obj", db.markDone(doneTask));

        assertTrue("Marked toDo obj as done", db.markDone(toDoTask));
        assertTrue("In all list", db.getAllTasks().contains(toDoTask));
        assertTrue("In done list", db.getDoneTasks().contains(toDoTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(toDoTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(toDoTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(doneTask));

        assertTrue("Marked block obj as done", db.markDone(blockTask));
        assertTrue("In all list", db.getAllTasks().contains(blockTask));
        assertTrue("In done list", db.getDoneTasks().contains(blockTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(blockTask));
        assertFalse("Not in block list", db.getBlockTasks().contains(blockTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(blockTask));

        assertTrue("Marked deleted obj as done", db.markDone(deletedTask));
        assertTrue("In all list", db.getAllTasks().contains(deletedTask));
        assertTrue("In done list", db.getDoneTasks().contains(deletedTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(deletedTask));
        assertFalse("Not in block list",
                    db.getBlockTasks().contains(deletedTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(deletedTask));
    }

    @Test
    public void testMarkBlock() {
        // Clear static lists containing Task objects
        DatabaseLogic db = new DatabaseLogic();
        db.permanentlyDeleteAllTasks();

        Task toDoTask = new Task("", EMPTY_DT, EMPTY_DT, EMPTY_DT, EMPTY_TAGS,
                TaskType.TODO);
        Task doneTask = new Task("", EMPTY_DT, EMPTY_DT, COMPLETE, EMPTY_TAGS,
                TaskType.DONE);
        Task blockTask = new Task("", START, DUE, EMPTY_DT, EMPTY_TAGS,
                TaskType.BLOCK);
        Task deletedTask = new Task("", START, DUE, EMPTY_DT, EMPTY_TAGS,
                TaskType.BLOCK);

        db.add(toDoTask);
        db.add(doneTask);
        db.add(blockTask);
        db.add(deletedTask);
        db.delete(deletedTask);

        assertFalse("Cannot mark null obj as block", db.markBlock(null));
        assertFalse("Cannot mark undeleted block obj", db.markBlock(blockTask));

        assertTrue("Marked toDo obj as block", db.markBlock(toDoTask));
        assertTrue("In all list", db.getAllTasks().contains(toDoTask));
        assertTrue("In block list", db.getBlockTasks().contains(toDoTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(toDoTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(toDoTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(toDoTask));

        assertTrue("Marked done obj as block", db.markBlock(doneTask));
        assertTrue("In all list", db.getAllTasks().contains(doneTask));
        assertTrue("In block list", db.getBlockTasks().contains(doneTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(doneTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(doneTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(doneTask));

        assertTrue("Marked deleted obj as block", db.markBlock(deletedTask));
        assertTrue("In all list", db.getAllTasks().contains(deletedTask));
        assertTrue("In block list", db.getBlockTasks().contains(deletedTask));
        assertFalse("Not in toDo list", db.getToDoTasks().contains(deletedTask));
        assertFalse("Not in done list", db.getDoneTasks().contains(deletedTask));
        assertFalse("Not in deleted list",
                    db.getDeletedTasks().contains(deletedTask));
    }
}