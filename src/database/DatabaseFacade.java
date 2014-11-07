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

public class DatabaseFacade {

    private DatabaseLogic databaseLogic;

    /** Name of file to write tasks to. */
    final private static String FILENAME_TASK = "data_tasks.txt";

    /** Reads from file containing tasks */
    private TaskReader taskReader;

    /** Writes to file containing tasks */
    private TaskWriter taskWriter;

    /**
     * Default constructor.
     * 
     * Checks if task file exists and allTasks list is empty before populating
     * list. If allTasks list is already populated, it signals that other
     * DataFile instances exist, and avoids populating task lists with duplicate
     * Task objects from file.
     */
    public DatabaseFacade() {
        databaseLogic = new DatabaseLogic();
        taskReader = new TaskReader(FILENAME_TASK);
        taskWriter = new TaskWriter(FILENAME_TASK);
        if (taskReader.fileExists() && databaseLogic.getAllTasks().isEmpty()) {
            databaseLogic.populateTaskLists(taskReader.read());
        }
    }

    /**
     * Returns a list of to-do Task objects. Exclusively contains undeleted
     * to-do tasks.
     * 
     * @return List of to-do Task objects.
     */
    public List<Task> getToDoTasks() {
        return databaseLogic.getToDoTasks();
    }

    /**
     * Returns a list of done Task objects. Exclusively contains undeleted done
     * tasks.
     * 
     * @return List of done Task objects.
     */
    public List<Task> getDoneTasks() {
        return databaseLogic.getDoneTasks();
    }

    /**
     * Returns a list of block Task objects. Exclusively contains undeleted
     * block tasks.
     * 
     * @return List of block Task objects.
     */
    public List<Task> getBlockTasks() {
        return databaseLogic.getBlockTasks();
    }

    /**
     * Returns a list of deleted Task objects. Exclusively contains deleted
     * deleted tasks.
     * 
     * @return List of deleted Task objects.
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
     * Given id, returns Task object. Includes to-do, done, and deleted tasks.
     * 
     * @param id
     *            The task's unique ID.
     * @return Task object of matching ID, or null if task is not in the list.
     */
    public Task getTask(int id) {
        return databaseLogic.searchTaskById(getAllTasks(), id);
    }

    /**
     * Writes to system file with current data in to-do and done lists. Deleted
     * tasks are not written to file.
     * 
     * To-do tasks are written first, followed by done tasks, instead of
     * haphazardly in random order, to aid in branch prediction when reading
     * from file. See {@link getTasksFromFile(File)}.
     * 
     * @return True, if successfully written to file.
     */
    private boolean updateFile() {
        String allTaskInfo = databaseLogic.getAllTaskInfo();
        return taskWriter.write(allTaskInfo);
    }
    
    /**
     * Adds a new to-do task to lists and file. Populates relevant lists, and
     * updates file with new information.
     * 
     * @param task
     *            New Task object to be written to file.
     * @return True, if successfully written to file.
     */
    public boolean add(Task task) {
        boolean success = databaseLogic.add(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean edit(int id, String name, DateTime start,
                                  DateTime due, List<String> tags) {
        Task task = databaseLogic.searchTaskById(databaseLogic.getAllTasks(), id);
        boolean success = databaseLogic.edit(task, name, start, due, tags);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
     * @param start
     *            New start date and time, if any.
     * @param due
     *            New end/due date and time, if any.
     * @param tags
     *            New tags to append with, if any.
     * @return True, if file has been successfully updated with edit.
     */
    public boolean edit(Task task, String name, DateTime start,
                                  DateTime due, List<String> tags) {
        boolean success = databaseLogic.edit(task, name, start, due, tags);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean delete(int id) {
        Task task = databaseLogic.searchTaskById(getAllTasks(), id);
        boolean success = databaseLogic.delete(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean delete(Task task) {
        boolean success = databaseLogic.delete(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean restore(int id) {
        Task task = databaseLogic.searchTaskById(getDeletedTasks(), id);
        boolean success = databaseLogic.restore(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean restore(Task task) {
        boolean success = databaseLogic.restore(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean permanentlyDelete(int id) {
        Task task = databaseLogic.searchTaskById(getAllTasks(), id);
        boolean success = databaseLogic.permanentlyDelete(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean permanentlyDelete(Task task) {
        boolean success = databaseLogic.permanentlyDelete(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
    }

    /**
     * Permanently deletes all tasks in all lists, and clears file of data.
     * Cannot be undone. User should be prompted for confirmation before
     * executing this function.
     * 
     * @return True, if successfully cleared file of data.
     */
    public boolean resetData() {
        boolean success = databaseLogic.permanentlyDeleteAllTasks();
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean markToDo(int id) {
        Task task = databaseLogic.searchTaskById(getAllTasks(), id);
        boolean success = databaseLogic.markToDo(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean markToDo(Task task) {
        boolean success = databaseLogic.markToDo(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean markDone(int id) {
        Task task = databaseLogic.searchTaskById(getAllTasks(), id);
        boolean success = databaseLogic.markDone(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
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
    public boolean markDone(Task task) {
        boolean success = databaseLogic.markDone(task);
        if (success) {
            return updateFile();
        } else {
            return false;
        }
    }
}