package database;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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

public class DatabaseLogic {

    /** Exclusively contains undeleted todo tasks. */
    private static List<Task> toDoTasks = new ArrayList<Task>();

    /** Exclusively contains undeleted done tasks. */
    private static List<Task> doneTasks = new ArrayList<Task>();

    /** Exclusively contains undeleted block tasks. */
    private static List<Task> blockTasks = new ArrayList<Task>();

    /** Exclusively contains deleted todo, done, and block tasks. */
    private static List<Task> deletedTasks = new ArrayList<Task>();

    /**
     * Contains references to all Task objects in toDoTasks, doneTasks,
     * blockTasks, and deletedTasks. Does not contain duplicate objects.
     */
    private static List<Task> allTasks = new ArrayList<Task>();

    /** Default constructor. */
    public DatabaseLogic() {
    }

    /**
     * Populates Task lists. Deleted tasks are not written to file, hence the
     * deleted task list is not populated.
     * 
     * Benefits from branch prediction because of the order in which tasks are
     * written to the file. See {@link updateTaskFile()}.
     * 
     * @param file
     *            The file to read from.
     */
    public void populateTaskLists(List<Task> tasks) {
        allTasks.addAll(tasks);
        for (Task task : allTasks) {
            switch (task.getType()) {
                case TODO:
                    toDoTasks.add(task);
                    break;
                case DONE:
                    doneTasks.add(task);
                    break;
                case BLOCK:
                    blockTasks.add(task);
                    break;
                default:
                    assert false : "Is not todo, done, or block type Task object";
                    break;
            }
        }
    }

    /**
     * Returns a list of to-do Task objects. Exclusively contains undeleted
     * to-do tasks.
     * 
     * @return List of to-do Task objects.
     */
    public List<Task> getToDoTasks() {
        Collections.sort(toDoTasks);
        return Collections.unmodifiableList(toDoTasks);
    }

    /**
     * Returns a list of done Task objects. Exclusively contains undeleted done
     * tasks.
     * 
     * @return List of done Task objects.
     */
    public List<Task> getDoneTasks() {
        Collections.sort(doneTasks);
        return Collections.unmodifiableList(doneTasks);
    }

    /**
     * Returns a list of block Task objects. Exclusively contains undeleted
     * block tasks.
     * 
     * @return List of block Task objects.
     */
    public List<Task> getBlockTasks() {
        Collections.sort(blockTasks);
        return Collections.unmodifiableList(blockTasks);
    }

    /**
     * Returns a list of deleted Task objects. Exclusively contains deleted
     * deleted tasks.
     * 
     * @return List of deleted Task objects.
     */
    public List<Task> getDeletedTasks() {
        Collections.sort(deletedTasks);
        return Collections.unmodifiableList(deletedTasks);
    }

    /**
     * Returns a list all Task objects. Contains references to to-do, done, and
     * block Task objects. Does not contain duplicate Task objects.
     * 
     * @return List of all Task objects.
     */
    public List<Task> getAllTasks() {
        Collections.sort(allTasks);
        return Collections.unmodifiableList(allTasks);
    }

    /**
     * Given id, returns Task object from specified task list.
     * 
     * @param tasks
     *            The task list to search from.
     * @param id
     *            The task's unique ID.
     * @return Task object of matching ID, or null if task is not in the list.
     */
    public Task searchTaskById(int id) {
        for (Task task : allTasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null; // Invalid id
    }

    public String getAllTaskInfo() {
        String allTaskInfo = getTaskInfo(toDoTasks);
        allTaskInfo += getTaskInfo(doneTasks);
        allTaskInfo += getTaskInfo(blockTasks);
        return allTaskInfo;
    }

    /**
     * Converts Task objects, from specified list, to a single String to write
     * to system file.
     * 
     * @param tasks
     *            The specified list to get Task objects from.
     * @return A single String containing all Task info to write to file.
     */
    private String getTaskInfo(List<Task> tasks) {
        String allTaskInfo = "";
        for (Task task : tasks) {
            allTaskInfo += task.toString() + "\n";
        }
        return allTaskInfo;
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
        assert !allTasks.contains(task);
        assert !task.isDeleted();
        
        allTasks.add(task);
        switch (task.getType()) {
            case TODO:
                return toDoTasks.add(task);
            case DONE:
                return doneTasks.add(task);
            case BLOCK:
                return blockTasks.add(task);
            default:
                assert false : "Is not todo, done, or block type Task object";
                return false;
        }
    }

    /**
     * Updates Task object's attributes with provided arguments. Null arguments
     * are provided for attributes to reset. Non-empty, non-null arguments are
     * used to update attributes' data. Empty, non-null arguments are provided
     * for attributes that are not meant to be changed.
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
     * @return True, if file has been successfully updated.
     */
    public boolean edit(Task task, String name, DateTime start, DateTime due,
                        List<String> tags) {
        if (task == null) {
            return false; // Invalid ID
        }

        if (name == null) {
            task.resetName();
        } else if (!name.isEmpty()) {
            task.setName(name);
        }

        if (start == null) {
            task.resetStart();
        } else if (!start.toString().isEmpty()) {
            task.setStart(start);
        }

        if (due == null) {
            task.resetDue();
        } else if (!due.toString().isEmpty()) {
            task.setDue(due);
        }

        if (tags == null) {
            task.resetTags();
        } else if (!tags.isEmpty()) {
            task.setTags(tags);
        }
        return true;
    }

    /**
     * Deletes Task object provided in argument. Removes object from to-do or
     * done task list, adds to deleted task list, and updates file.
     * 
     * @param task
     *            The Task object to delete.
     * @return True, if file has been successfully updated with delete.
     */
    public boolean delete(Task task) {
        if (task == null || task.isDeleted()) {
            return false; // Invalid ID or already deleted
        }

        task.setDeleted(true);
        deletedTasks.add(task);
        switch (task.getType()) {
            case TODO:
                return toDoTasks.remove(task);
            case DONE:
                return doneTasks.remove(task);
            case BLOCK:
                return blockTasks.remove(task);
            default:
                assert false : "Is not todo, done, or block type Task object";
                return false;
        }
    }

    /**
     * Restores deleted Task object provided in argument. Removes object from
     * deleted task list, adds to to-do or done task list, and updates file.
     * 
     * @param task
     *            The Task object to restore.
     * @return True, if file has been successfully updated with restore.
     */
    public boolean restore(Task task) {
        if (task == null || !task.isDeleted()) {
            return false; // Invalid ID or is not deleted task
        }

        task.setDeleted(false);
        deletedTasks.remove(task);
        switch (task.getType()) {
            case TODO:
                return toDoTasks.add(task);
            case DONE:
                return doneTasks.add(task);
            case BLOCK:
                return blockTasks.add(task);
            default:
                assert false : "Is not todo, done, or block type Task object";
                return false;
        }
    }

    /**
     * Permanently deletes Task object provided in argument. Cannot be undone.
     * Used when undoing add commands. Decrements Task ID counter. Removes
     * object from all lists, and updates file.
     * 
     * @param task
     *            The Task object to permanently delete.
     * @return True, if file has been successfully updated with wipe.
     */
    public boolean permanentlyDelete(Task task) {
        if (task == null) {
            return false; // Invalid ID
        }

        Task.decrementId();
        allTasks.remove(task);
        if (task.isDeleted()) {
            return deletedTasks.remove(task);
        } else {
            switch (task.getType()) {
                case TODO:
                    return toDoTasks.remove(task);
                case DONE:
                    return doneTasks.remove(task);
                case BLOCK:
                    return blockTasks.remove(task);
                default:
                    assert false : "Is not todo, done, or block type Task object";
                    return false;
            }
        }
    }

    /**
     * Permanently deletes all tasks in all lists, and clears file of data.
     * Cannot be undone. User should be prompted for confirmation before
     * executing this function.
     * 
     * @return True, if successfully cleared file of data.
     */
    public boolean permanentlyDeleteAllTasks() {
        Task.resetId();
        allTasks.clear();
        toDoTasks.clear();
        doneTasks.clear();
        blockTasks.clear();
        deletedTasks.clear();
        return true;
    }

    /**
     * Marks Task object provided in argument as to-do. If Task was deleted,
     * restore it, and mark as to-do. Removes object from done or deleted list,
     * adds to to-do list, and updates file.
     * 
     * @param task
     *            The Task object to mark as to-do.
     * @return True, if file has been successfully updated with change.
     */
    public boolean markToDo(Task task) {
        if (task == null || (task.isToDo() && !task.isDeleted())) {
            return false; // Invalid ID or is undeleted to-do task
        }

        if (task.isDeleted()) {
            return restore(task);
        }
        switch (task.getType()) {
            case DONE:
                doneTasks.remove(task);
                break;
            case BLOCK:
                blockTasks.remove(task);
                break;
            default:
                assert false : "Is not todo, done, or block type Task object";
                break;
        }
        task.setType(TaskType.TODO);
        return toDoTasks.add(task);
    }

    /**
     * Marks Task object provided in argument as done. If Task was deleted,
     * restore it, and mark as done. Removes object from to-do or deleted list,
     * adds to done list, and updates file.
     * 
     * @param task
     *            The Task object to mark as done.
     * @return True, if file has been successfully updated with change.
     */
    public boolean markDone(Task task) {
        if (task == null || (task.isDone() && !task.isDeleted())) {
            return false; // Invalid ID or is undeleted done task
        }

        if (task.isDeleted()) {
            return restore(task);
        }
        switch (task.getType()) {
            case TODO:
                toDoTasks.remove(task);
                break;
            case BLOCK:
                blockTasks.remove(task);
                break;
            default:
                assert false : "Is not todo, done, or block type Task object";
                break;
        }
        task.setType(TaskType.DONE);
        return doneTasks.add(task);
    }

    /**
     * Marks Task object provided in argument as block type. If Task was
     * deleted, restore it, and mark as block type. Removes object from to-do or
     * deleted list, adds to block list, and updates file.
     * 
     * @param task
     *            The Task object to mark as block type.
     * @return True, if file has been successfully updated with change.
     */
    public boolean markBlock(Task task) {
        if (task == null || (task.isBlock() && !task.isDeleted())) {
            return false; // Invalid ID or is undeleted block task
        }

        if (task.isDeleted()) {
            return restore(task);
        }
        switch (task.getType()) {
            case TODO:
                toDoTasks.remove(task);
                break;
            case DONE:
                doneTasks.remove(task);
                break;
            default:
                assert false : "Is not todo, done, or block type Task object";
                break;
        }
        task.setType(TaskType.BLOCK);
        return blockTasks.add(task);
    }
}