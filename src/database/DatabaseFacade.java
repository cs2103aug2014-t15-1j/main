package database;

import java.util.List;

/**
 * This facade class handles the classes involved in reading and writing of
 * tasks to the file for storage, and managing the data structure of tasks.
 * 
 * @author A0116373J
 * 
 */

public class DatabaseFacade {

    /** Name of file to write tasks to. */
    private final static String FILENAME = "data_tasks.txt";

    /** Handles task data structure logic. */
    private DatabaseLogic databaseLogic;

    /** Reads from file containing tasks */
    private TaskReader taskReader;

    /** Writes to file containing tasks */
    private TaskWriter taskWriter;

    /**
     * Default constructor.
     * 
     * Checks if allTasks list is empty before populating list. If allTasks list
     * is already populated, it signals that other DatabaseLogic instances
     * exist, and avoids populating task lists with duplicate Task objects from
     * file.
     */
    public DatabaseFacade() {
        databaseLogic = new DatabaseLogic();
        taskReader = new TaskReader(FILENAME);
        taskWriter = new TaskWriter(FILENAME);
        if (databaseLogic.getAllTasks().isEmpty()) {
            databaseLogic.populateTaskLists(taskReader.read());
        }
    }

    /**
     * Returns a list of undeleted todo Task objects.
     * 
     * @return List of undeleted todo Task objects.
     */
    public List<Task> getToDoTasks() {
        return databaseLogic.getToDoTasks();
    }

    /**
     * Returns a list of undeleted done Task objects.
     * 
     * @return List of undeleted done Task objects.
     */
    public List<Task> getDoneTasks() {
        return databaseLogic.getDoneTasks();
    }

    /**
     * Returns a list of undeleted block Task objects.
     * 
     * @return List of undeleted block Task objects.
     */
    public List<Task> getBlockTasks() {
        return databaseLogic.getBlockTasks();
    }

    /**
     * Returns a list of deleted todo, done, and block Task objects.
     * 
     * @return List of deleted todo, done, and block Task objects.
     */
    public List<Task> getDeletedTasks() {
        return databaseLogic.getDeletedTasks();
    }

    /**
     * Returns a list all Task objects. Contains references to to-do, done, and
     * block Task objects. Does not contain duplicate Task objects.
     * 
     * @return List of all Task objects.
     */
    public List<Task> getAllTasks() {
        return databaseLogic.getAllTasks();
    }

    /**
     * Given id, returns Task object of matching id.
     * 
     * @param id
     *            The task's unique id.
     * @return Task object of matching id, or null if id is invalid.
     */
    public Task getTask(int id) {
        return databaseLogic.searchTaskById(id);
    }

    /**
     * Writes all current Task information to file for storage. Deleted tasks
     * are not stored. Tasks are written in this order: todo, done, block.
     * 
     * @return True, if successfully written to file.
     */
    private boolean updateFile() {
        String allTaskInfo = databaseLogic.getAllTaskInfo();
        return taskWriter.write(allTaskInfo);
    }

    /**
     * Adds a new Task object into Task data structure and file.
     * 
     * @param task
     *            New Task object to be added.
     * @return True, if successfully added to data structure and file.
     */
    public boolean add(Task task) {
        boolean logicSuccess = databaseLogic.add(task);
        return logicSuccess && updateFile();
    }

    /**
     * Based on id provided, updates Task object's attributes with provided
     * arguments. Changes are saved to data structure and file. Provide null
     * arguments for attributes to be reset to empty values. Provide empty
     * arguments for attributes to keep the same.
     * 
     * Overloaded function.
     * 
     * @param id
     *            Id of Task object to modify.
     * @param name
     *            New description, if any.
     * @param start
     *            New start date and time, if any.
     * @param due
     *            New due date and time, if any.
     * @param tags
     *            New tags, if any.
     * @return True, if successfully edited Task object in data structure and
     *         file.
     */
    public boolean edit(int id, String name, DateTime start, DateTime due,
                        List<String> tags) {
        Task task = getTask(id);
        return edit(task, name, start, due, tags);
    }

    /**
     * Based on object provided, updates Task object's attributes with provided
     * arguments. Changes are saved to data structure and file. Provide null
     * arguments for attributes to be reset to empty values. Provide empty
     * arguments for attributes to keep the same.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to modify.
     * @param name
     *            New description, if any.
     * @param start
     *            New start date and time, if any.
     * @param due
     *            New due date and time, if any.
     * @param tags
     *            New tags, if any.
     * @return True, if successfully edited Task object in data structure and
     *         file.
     */
    public boolean edit(Task task, String name, DateTime start, DateTime due,
                        List<String> tags) {
        boolean logicSuccess = databaseLogic.edit(task, name, start, due, tags);
        return logicSuccess && updateFile();
    }

    /**
     * Based on id provided, deletes undeleted Task object. Changes are saved to
     * data structure and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            Id of Task object to delete.
     * @return True, if successfully deleted Task object in data structure and
     *         file.
     */
    public boolean delete(int id) {
        Task task = getTask(id);
        return delete(task);
    }

    /**
     * Based on object provided, deletes undeleted Task object. Changes are
     * saved to data structure and file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to delete.
     * @return True, if successfully deleted Task object in data structure and
     *         file.
     */
    public boolean delete(Task task) {
        boolean logicSuccess = databaseLogic.delete(task);
        return logicSuccess && updateFile();
    }

    /**
     * Based on id provided, restores deleted Task object. Changes are saved to
     * data structure and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            Id of Task object to restore.
     * @return True, if successfully restored Task object in data structure and
     *         file.
     */
    public boolean restore(int id) {
        Task task = getTask(id);
        return restore(task);
    }

    /**
     * Based on object provided, restores deleted Task object. Changes are saved
     * to data structure and file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to restore.
     * @return True, if successfully restored Task object in data structure and
     *         file.
     */
    public boolean restore(Task task) {
        boolean logicSuccess = databaseLogic.restore(task);
        return logicSuccess && updateFile();
    }

    /**
     * Based on id provided, permanently deletes Task object. Cannot be undone.
     * Used when undoing add commands. Decrements Task ID counter. Changes are
     * saved to data structure and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            Id of Task object to permanently delete.
     * @return True, if successfully deleted Task object in data structure and
     *         file permanently.
     */
    public boolean permanentlyDelete(int id) {
        Task task = getTask(id);
        return permanentlyDelete(task);
    }

    /**
     * Based on object provided, permanently deletes Task object. Cannot be
     * undone. Used when undoing add commands. Decrements Task ID counter.
     * Changes are saved to data structure and file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to permanently delete.
     * @return True, if successfully deleted Task object in data structure and
     *         file permanently.
     */
    public boolean permanentlyDelete(Task task) {
        boolean logicSuccess = databaseLogic.permanentlyDelete(task);
        return logicSuccess && updateFile();
    }

    /**
     * Permanently deletes all Task objects in data structure and file. Cannot
     * be undone. User should be prompted for confirmation before executing this
     * function.
     * 
     * @return True, if successfully permanently cleared of all Task data.
     */
    public boolean resetData() {
        boolean logicSuccess = databaseLogic.permanentlyDeleteAllTasks();
        return logicSuccess && updateFile();
    }

    /**
     * Based on id provided, marks Task object as todo. If Task was deleted,
     * restores it, and marks as todo. Changes are saved to data structure and
     * file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            Id of Task object to be marked as todo.
     * @return True, if successfully marked Task object in data structure and
     *         file as todo.
     */
    public boolean markToDo(int id) {
        Task task = getTask(id);
        return markToDo(task);
    }

    /**
     * Based on object provided, marks Task object as todo. If Task was deleted,
     * restores it, and marks as todo. Changes are saved to data structure and
     * file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to be marked as todo.
     * @return True, if successfully marked Task object in data structure and
     *         file as todo.
     */
    public boolean markToDo(Task task) {
        boolean logicSuccess = databaseLogic.markToDo(task);
        return logicSuccess && updateFile();
    }

    /**
     * Based on id provided, marks Task object as done. If Task was deleted,
     * restores it, and marks as done. Changes are saved to data structure and
     * file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            Id of Task object to be marked as done.
     * @return True, if successfully marked Task object in data structure and
     *         file as done.
     */
    public boolean markDone(int id) {
        Task task = getTask(id);
        return markDone(task);
    }

    /**
     * Based on object provided, marks Task object as done. If Task was deleted,
     * restores it, and marks as done. Changes are saved to data structure and
     * file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to be marked as done.
     * @return True, if successfully marked Task object in data structure and
     *         file as done.
     */
    public boolean markDone(Task task) {
        boolean logicSuccess = databaseLogic.markDone(task);
        return logicSuccess && updateFile();
    }

    /**
     * Based on id provided, marks Task object as block. If Task was deleted,
     * restores it, and marks as block. Changes are saved to data structure and
     * file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            Id of Task object to be marked as block.
     * @return True, if successfully marked Task object in data structure and
     *         file as block.
     */
    public boolean markBlock(int id) {
        Task task = getTask(id);
        return markBlock(task);
    }

    /**
     * Based on object provided, marks Task object as block. If Task was
     * deleted, restores it, and marks as block. Changes are saved to data
     * structure and file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to be marked as block.
     * @return True, if successfully marked Task object in data structure and
     *         file as block.
     */
    public boolean markBlock(Task task) {
        boolean logicSuccess = databaseLogic.markBlock(task);
        return logicSuccess && updateFile();
    }
}