package database;

import java.util.List;

/**
 * This class handles the logic involved in reading and writing of tasks to the
 * system file, and managing tasks.
 * 
 * Do not apply the singleton pattern to this class. Even if multiple instances
 * are created, all task data in lists and on file will still be synchronized.
 * For more details, view GitHub issue #64
 * (https://github.com/cs2103aug2014-t15-1j/main/issues/64).
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class DataFile {

    private DatabaseFacade databaseFacade;

    /**
     * Default constructor.
     * 
     * Checks if task file exists and allTasks list is empty before populating
     * list. If allTasks list is already populated, it signals that other
     * DataFile instances exist, and avoids populating task lists with duplicate
     * Task objects from file.
     */
    public DataFile() {
        databaseFacade = new DatabaseFacade();
    }

    /**
     * Returns a list of to-do Task objects. Exclusively contains undeleted
     * to-do tasks.
     * 
     * @return List of to-do Task objects.
     */
    public List<Task> getToDoTasks() {
        return databaseFacade.getToDoTasks();
    }

    /**
     * Returns a list of done Task objects. Exclusively contains undeleted done
     * tasks.
     * 
     * @return List of done Task objects.
     */
    public List<Task> getDoneTasks() {
        return databaseFacade.getDoneTasks();
    }

    /**
     * Returns a list of block Task objects. Exclusively contains undeleted
     * block tasks.
     * 
     * @return List of block Task objects.
     */
    public List<Task> getBlockTasks() {
        return databaseFacade.getBlockTasks();
    }

    /**
     * Returns a list of deleted Task objects. Exclusively contains deleted
     * deleted tasks.
     * 
     * @return List of deleted Task objects.
     */
    public List<Task> getDeletedTasks() {
        return databaseFacade.getDeletedTasks();
    }

    /**
     * Returns a list all Task objects. Contains references to to-do, done, and
     * block Task objects. Does not contain duplicate Task objects.
     * 
     * @return List of all Task objects.
     */
    public List<Task> getAllTasks() {
        return databaseFacade.getAllTasks();
    }

    /**
     * Given id, returns Task object. Includes to-do, done, and deleted tasks.
     * 
     * @param id
     *            The task's unique ID.
     * @return Task object of matching ID, or null if task is not in the list.
     */
    public Task getTask(int id) {
        return databaseFacade.getTask(id);
    }

    /**
     * Adds a new to-do task to lists and file. Populates relevant lists, and
     * updates file with new information.
     * 
     * @param task
     *            New Task object to be written to file.
     * @return True, if successfully written to file.
     */
    public boolean addNewTask(Task task) {
        return databaseFacade.add(task);
    }

    /**
     * Updates Task object's attributes with provided arguments. Null arguments
     * are provided for attributes that are not meant to be changed. Task to
     * edit is specified by Task object provided in argument.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to modify.
     * @param name
     *            New description, if any.
     * @param due
     *            New due date and time, if any.
     * @param start
     *            New scheduled start date and time, if any.
     * @param end
     *            New scheduled end date and time, if any.
     * @param tags
     *            New tags to append with, if any.
     * @return True, if file has been successfully updated with edit.
     */
    public boolean updateTaskInfo(Task task, String name, DateTime start,
                                  DateTime due, List<String> tags) {
        return databaseFacade.edit(task, name, start, due, tags);
    }

    /**
     * Updates Task object's attributes with provided arguments, based on ID
     * provided. Null arguments are provided for attributes that are not meant
     * to be changed.
     * 
     * Overloaded function.
     * 
     * @param id
     *            ID of Task object to modify.
     * @param name
     *            New description, if any.
     * @param due
     *            New due date and time, if any.
     * @param start
     *            New scheduled start date and time, if any.
     * @param end
     *            New scheduled end date and time, if any.
     * @param tags
     *            New tags to append with, if any.
     * @return True, if file has been successfully updated with edit.
     */
    public boolean updateTaskInfo(int id, String name, DateTime start,
                                  DateTime due, List<String> tags) {
        return databaseFacade.edit(id, name, start, due, tags);
    }

    /**
     * Deletes Task object provided in argument. Updates task lists and file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            The Task object to delete.
     * @return True, if file has been successfully updated with delete.
     */
    public boolean deleteTask(Task task) {
        return databaseFacade.delete(task);
    }

    /**
     * Deletes Task object based on the ID provided in argument. Updates task
     * lists and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            The ID of the Task object to delete.
     * @return True, if file has been successfully updated with delete.
     */
    public boolean deleteTask(int id) {
        return databaseFacade.delete(id);
    }

    /**
     * Restores deleted Task object provided in argument. Updates task lists and
     * file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            The Task object to restore.
     * @return True, if file has been successfully updated with restore.
     */
    public boolean restoreTask(Task task) {
        return databaseFacade.restore(task);
    }

    /**
     * Restores deleted Task object based on the ID provided in argument.
     * Updates task lists and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            The ID of the Task object to restore.
     * @return True, if file has been successfully updated with restore.
     */
    public boolean restoreTask(int id) {
        return databaseFacade.restore(id);
    }

    /**
     * Permanently deletes Task object provided in argument. Cannot be undone.
     * Used when undoing add commands. Decrements Task ID counter. Updates task
     * lists and file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            The Task object to permanently delete.
     * @return True, if file has been successfully updated with wipe.
     */
    public boolean wipeTask(Task task) {
        return databaseFacade.permanentlyDelete(task);
    }

    /**
     * Permanently deletes Task object based on the ID provided in argument.
     * Cannot be undone. Used when undoing add commands. Decrements Task ID
     * counter. Updates task lists and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            The ID of the Task object to permanently delete.
     * @return True, if file has been successfully updated with wipe.
     */
    public boolean wipeTask(int id) {
        return databaseFacade.permanentlyDelete(id);
    }

    /**
     * Permanently deletes all tasks in all lists, and clears file of data.
     * Cannot be undone. User should be prompted for confirmation before
     * executing this function.
     * 
     * @return True, if successfully cleared file of data.
     */
    public boolean wipeFile() {
        return databaseFacade.resetData();
    }

    /**
     * Marks Task object provided in argument as to-do. If Task was deleted,
     * restore it, and mark as to-do. Updates task lists and file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            The Task object to be marked as to-do.
     * @return True, if file has been successfully updated with change.
     */
    public boolean toDoTask(Task task) {
        return databaseFacade.toDo(task);
    }

    /**
     * Marks Task object based on ID provided in argument as to-do. If Task was
     * deleted, restore it, and mark as to-do. Updates task lists and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            The ID of the Task object to be marked as to-do.
     * @return True, if file has been successfully updated with change.
     */
    public boolean toDoTask(int id) {
        return databaseFacade.toDo(id);
    }

    /**
     * Marks Task object provided in argument as done. If Task was deleted,
     * restore it, and mark as done. Updates task lists and file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            The Task object to be marked as done.
     * @return True, if file has been successfully updated with change.
     */
    public boolean doneTask(Task task) {
        return databaseFacade.done(task);
    }

    /**
     * Marks Task object based on ID provided in argument as done. If Task was
     * deleted, restore it, and mark as done. Updates task lists and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            The ID of the Task object to be marked as done.
     * @return True, if file has been successfully updated with change.
     */
    public boolean doneTask(int id) {
        return databaseFacade.done(id);
    }
}