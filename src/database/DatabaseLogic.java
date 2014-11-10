//@author A0116373J
package database;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import objects.DateTime;
import objects.Task;

/**
 * This class handles the logic involved in managing the data structure of
 * tasks.
 * 
 * Do not apply the singleton pattern to this class. Even if multiple instances
 * are created, all task data in lists and on file will still be synchronized.
 * For more details, view GitHub issue #64
 * (https://github.com/cs2103aug2014-t15-1j/main/issues/64).
 */

public class DatabaseLogic {

    /** Only contains undeleted todo tasks. */
    private static List<Task> toDoTasks = new ArrayList<Task>();

    /** Only contains undeleted done tasks. */
    private static List<Task> doneTasks = new ArrayList<Task>();

    /** Only contains undeleted block tasks. */
    private static List<Task> blockTasks = new ArrayList<Task>();

    /** Only contains deleted todo, done, and block tasks. */
    private static List<Task> deletedTasks = new ArrayList<Task>();

    /**
     * Contains references to all Task objects in toDoTasks, doneTasks,
     * blockTasks, and deletedTasks lists. Does not contain duplicate objects.
     */
    private static List<Task> allTasks = new ArrayList<Task>();

    /** Default constructor. */
    public DatabaseLogic() {
    }

    /**
     * Populates Task lists at DatabaseFacade instantiation. Deleted tasks are
     * not saved to file, hence the deleted task list is not populated.
     * 
     * @param tasks
     *            The list of Tasks objects used to populate the Task lists in
     *            DatabaseLogic with.
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
     * Returns a list of undeleted todo Task objects.
     * 
     * @return List of undeleted todo Task objects.
     */
    public List<Task> getToDoTasks() {
        Collections.sort(toDoTasks);
        return Collections.unmodifiableList(toDoTasks);
    }

    /**
     * Returns a list of undeleted done Task objects.
     * 
     * @return List of undeleted done Task objects.
     */
    public List<Task> getDoneTasks() {
        Collections.sort(doneTasks);
        return Collections.unmodifiableList(doneTasks);
    }

    /**
     * Returns a list of undeleted block Task objects.
     * 
     * @return List of undeleted block Task objects.
     */
    public List<Task> getBlockTasks() {
        Collections.sort(blockTasks);
        return Collections.unmodifiableList(blockTasks);
    }

    /**
     * Returns a list of deleted todo, done, and block Task objects.
     * 
     * @return List of deleted todo, done, and block Task objects.
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
     * Given id, returns Task object of matching id.
     * 
     * @param id
     *            The task's unique id.
     * @return Task object of matching id, or null if id is invalid.
     */
    public Task searchTaskById(int id) {
        for (Task task : allTasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null; // Invalid id
    }

    /**
     * Converts all Task objects into a single String. For writing to file.
     * 
     * @return A single String containing all Task objects' info.
     */
    public String getAllTaskInfo() {
        String allTaskInfo = getTaskInfo(toDoTasks);
        allTaskInfo += getTaskInfo(doneTasks);
        allTaskInfo += getTaskInfo(blockTasks);
        return allTaskInfo;
    }

    /**
     * Converts Task objects from specified list into a single String. For
     * writing to file.
     * 
     * @param tasks
     *            The specified list to get Task objects from.
     * @return A single String containing Task objects' info.
     */
    private String getTaskInfo(List<Task> tasks) {
        String allTaskInfo = "";
        for (Task task : tasks) {
            allTaskInfo += task.toString() + "\n";
        }
        return allTaskInfo;
    }

    /**
     * Adds a new Task object into respective Task lists.
     * 
     * @param task
     *            New Task object to be added to Task lists.
     * @return True, if successfully added to Task lists.
     */
    public boolean add(Task task) {
        assert !allTasks.contains(task) : "Should not add duplicates";
        assert !task.isDeleted() : "Add is meant to handle new, undeleted tasks only";

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
     * Based on object provided, updates Task object's attributes with provided
     * arguments. Changes are saved to Task lists. Provide null arguments for
     * attributes to be reset to empty values. Provide empty arguments for
     * attributes to keep the same.
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
     * @return True, if successfully edited Task object in Task lists.
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
     * Based on object provided, deletes undeleted Task object. Remove from
     * respective Task lists, and add to deleted Task list.
     * 
     * @param task
     *            Task object to delete.
     * @return True, if successfully deleted Task object.
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
     * Based on object provided, restores deleted Task object. Remove from
     * deleted Task list, and add to respective Task lists.
     * 
     * @param task
     *            Task object to restore.
     * @return True, if successfully restored Task object.
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
     * Based on object provided, permanently deletes Task object. Cannot be
     * undone. Used when undoing add commands. Decrements Task ID counter.
     * Remove from every Task list.
     * 
     * @param task
     *            Task object to permanently delete.
     * @return True, if successfully deleted Task object permanently.
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
     * Permanently deletes all Task objects in every Task list. Cannot be
     * undone. User should be prompted for confirmation before executing this
     * function.
     * 
     * @return True, if successfully permanently cleared of all Task data.
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
     * Based on object provided, marks Task object as todo. If Task was deleted,
     * restores it, and marks as todo. Removed from respective Task lists, and
     * added to todo Task list.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to be marked as todo.
     * @return True, if successfully marked Task object as todo.
     */
    public boolean markToDo(Task task) {
        if (task == null || (task.isToDo() && !task.isDeleted())) {
            return false; // Invalid ID or is undeleted to-do task
        }

        if (task.isDeleted()) {
            task.setType(TaskType.TODO);
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
     * Based on object provided, marks Task object as done. If Task was deleted,
     * restores it, and marks as done. Removed from respective Task lists, and
     * added to done Task list.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to be marked as done.
     * @return True, if successfully marked Task object as done.
     */
    public boolean markDone(Task task) {
        if (task == null || (task.isDone() && !task.isDeleted())) {
            return false; // Invalid ID or is undeleted done task
        }

        if (task.isDeleted()) {
            task.setType(TaskType.DONE);
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
     * Based on object provided, marks Task object as block. If Task was
     * deleted, restores it, and marks as block. Removed from respective Task
     * lists, and added to block Task list.
     * 
     * Overloaded function.
     * 
     * @param task
     *            Task object to be marked as block.
     * @return True, if successfully marked Task object as block.
     */
    public boolean markBlock(Task task) {
        if (task == null || (task.isBlock() && !task.isDeleted())) {
            return false; // Invalid ID or is undeleted block task
        }

        if (task.isDeleted()) {
            task.setType(TaskType.BLOCK);
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