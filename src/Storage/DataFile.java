package Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import Parser.Parser;

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

    /** Name of file to write tasks to. */
    final private static String FILENAME = "Data";

    /** Exclusively contains undeleted to-do tasks. */
    private static List<Task> toDoTasks = new ArrayList<Task>();

    /** Exclusively contains undeleted done tasks. */
    private static List<Task> doneTasks = new ArrayList<Task>();

    /** Exclusively contains deleted to-do and deleted done tasks. */
    private static List<Task> deletedTasks = new ArrayList<Task>();

    /**
     * Contains references to all Task objects in toDoTasks, doneTasks, and
     * deletedTasks. Does not contain duplicate tasks.
     */
    private static List<Task> allTasks = new ArrayList<Task>();

    /**
     * Default constructor.
     * 
     * Checks if file exists and task lists are not populated before reading
     * from file, and populating lists with Task objects. If lists are already
     * populated, it signals that other DataFile instances exist, and avoids
     * populating the task lists with duplicate Task objects from file.
     */
    public DataFile() {
        File file = new File(FILENAME);
        if (file.exists() && allTasks.isEmpty()) {
            getTasksFromFile(file);
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Populates to-do and done task lists with data from system file. Deleted
     * tasks are not written to file, hence deleted task list is not populated.
     * 
     * Benefits from branch prediction because of the order in which tasks are
     * written to the file. See {@link updateFile()}.
     * 
     * @param file
     *            The file to read from.
     */
    private void getTasksFromFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String unparsedText = scanner.nextLine();
                Task newTask = Parser.parseToTask(unparsedText);

                allTasks.add(newTask);
                if (!newTask.isDone()) {
                    toDoTasks.add(newTask);
                } else {
                    doneTasks.add(newTask);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Returns an unmodifiable view of the list of to-do tasks. Attempts to
     * modify the returned list, whether direct or via its iterator, result in
     * an UnsupportedOperationException.
     * 
     * Exclusively contains undeleted to-do tasks.
     * 
     * @return Unmodifiable view of the specified list.
     */
    public List<Task> getToDoTasks() {
        return Collections.unmodifiableList(toDoTasks);
    }

    /**
     * Returns an unmodifiable view of the list of done tasks. Attempts to
     * modify the returned list, whether direct or via its iterator, result in
     * an UnsupportedOperationException.
     * 
     * Exclusively contains undeleted done tasks.
     * 
     * @return Unmodifiable view of the specified list.
     */
    public List<Task> getDoneTasks() {
        return Collections.unmodifiableList(doneTasks);
    }

    /**
     * Returns an unmodifiable view of the list of deleted tasks. Attempts to
     * modify the returned list, whether direct or via its iterator, result in
     * an UnsupportedOperationException.
     * 
     * Exclusively contains deleted to-do and deleted done tasks.
     * 
     * @return Unmodifiable view of the specified list.
     */
    public List<Task> getDeletedTasks() {
        return Collections.unmodifiableList(deletedTasks);
    }

    /**
     * Returns an unmodifiable view of the list of all tasks. Attempts to modify
     * the returned list, whether direct or via its iterator, result in an
     * UnsupportedOperationException.
     * 
     * Contains references to all Task objects in toDoTasks, doneTasks, and
     * deletedTasks. Does not contain duplicate tasks.
     * 
     * @return Unmodifiable view of the specified list.
     */
    public List<Task> getAllTasks() {
        return Collections.unmodifiableList(allTasks);
    }

    /**
     * Given id, returns Task object. Includes to-do, done, and deleted tasks.
     * 
     * @param id
     *            The task's unique ID.
     * @return Task object of matching ID, or null if task is not in the list.
     */
    public Task getTask(int id) {
        Task task = searchTaskById(allTasks, id);
        return task;
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
    private Task searchTaskById(List<Task> tasks, int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
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
        toDoTasks.add(task);
        allTasks.add(task);
        return updateFile();
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
        String updatedTaskInfo = getTaskInfo(toDoTasks);
        updatedTaskInfo += getTaskInfo(doneTasks);
        return writeToFile(updatedTaskInfo);
    }

    /**
     * Converts Task objects, from specified list, to a single String to write
     * to system file.
     * 
     * @param tasks
     *            The specified list to get Task objects from.
     * @return A single String containing all Task data to write to file.
     */
    private String getTaskInfo(List<Task> tasks) {
        String taskInfo = "";
        for (Task task : tasks) {
            taskInfo += task.getFullInfo() + "\n";
        }
        return taskInfo;
    }

    /**
     * Writes String, containing all to-do and done task data, to system file.
     * 
     * @param newFileText
     *            Data of all to-do and done tasks to write to file.
     * @return True, if successfully written to file.
     */
    private boolean writeToFile(String newFileText) {
        try {
            File file = new File(FILENAME);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter newFile = new FileWriter(file, false);
            newFile.write(newFileText);
            newFile.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    public boolean updateTaskInfo(Task task, String name, DateTime due,
                                  DateTime start, DateTime end,
                                  List<String> tags) {
        return updateTaskInfoByObject(task, name, due, start, end, tags);
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
    public boolean updateTaskInfo(int id, String name, DateTime due,
                                  DateTime start, DateTime end,
                                  List<String> tags) {
        Task task = searchTaskById(allTasks, id);
        return updateTaskInfoByObject(task, name, due, start, end, tags);
    }

    /**
     * Updates Task object's attributes with provided arguments. Null arguments
     * are provided for attributes that are not meant to be changed. Task to
     * edit is specified by Task object provided in argument.
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
    private boolean updateTaskInfoByObject(Task task, String name,
                                           DateTime due, DateTime start,
                                           DateTime end, List<String> tags) {
        if (task == null) {
            return false; // Invalid ID
        }
        if (name != null) {
            task.setName(name);
        }
        if (due != null) {
            task.setDue(due);
        }
        if (start != null) {
            task.setStart(start);
        }
        if (end != null) {
            task.setEnd(end);
        }
        if (tags != null) {
            task.addTags(tags);
        }

        return updateFile();
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
        return deleteTaskByObject(task);
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
        Task task = searchTaskById(toDoTasks, id);
        if (task == null) {
            task = searchTaskById(doneTasks, id);
        }
        return deleteTaskByObject(task);
    }

    /**
     * Deletes Task object provided in argument. Removes object from to-do or
     * done task list, adds to deleted task list, and updates file.
     * 
     * @param task
     *            The Task object to delete.
     * @return True, if file has been successfully updated with delete.
     */
    private boolean deleteTaskByObject(Task task) {
        if (task == null || task.isDeleted()) {
            return false; // Invalid ID or already deleted
        }

        task.setDeleted(true);
        deletedTasks.add(task);
        if (task.isDone()) {
            doneTasks.remove(task);
        } else {
            toDoTasks.remove(task);
        }
        return updateFile();
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
        return restoreTaskByObject(task);
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
        Task task = searchTaskById(deletedTasks, id);
        return restoreTaskByObject(task);
    }

    /**
     * Restores deleted Task object provided in argument. Removes object from
     * deleted task list, adds to to-do or done task list, and updates file.
     * 
     * @param task
     *            The Task object to restore.
     * @return True, if file has been successfully updated with restore.
     */
    private boolean restoreTaskByObject(Task task) {
        if (task == null || !task.isDeleted()) {
            return false; // Invalid ID or is not deleted task
        }

        task.setDeleted(false);
        deletedTasks.remove(task);
        if (task.isDone()) {
            doneTasks.add(task);
        } else {
            toDoTasks.add(task);
        }
        return updateFile();
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
        return wipeTaskByObject(task);
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
        Task task = searchTaskById(allTasks, id);
        return wipeTaskByObject(task);
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
    private boolean wipeTaskByObject(Task task) {
        if (task == null) {
            return false; // Invalid ID
        }

        allTasks.remove(task);
        if (task.isDeleted()) {
            deletedTasks.remove(task);
        } else if (task.isDone()) {
            doneTasks.remove(task);
        } else {
            toDoTasks.remove(task);
        }
        task.wipeTask();
        return updateFile();
    }

    /**
     * Permanently deletes all tasks in all lists, and clears file of data.
     * Cannot be undone. User should be prompted for confirmation before
     * executing this function.
     * 
     * @return True, if successfully cleared file of data.
     */
    public boolean wipeFile() {
        Task.wipeAllTasks();
        allTasks.clear();
        toDoTasks.clear();
        doneTasks.clear();
        deletedTasks.clear();

        return updateFile();
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
        return toDoTaskByObject(task);
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
        Task task = searchTaskById(allTasks, id);
        return toDoTaskByObject(task);
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
    private boolean toDoTaskByObject(Task task) {
        if (task == null || (!task.isDeleted() && !task.isDone())) {
            return false; // Invalid ID or is undeleted to-do task
        }

        task.setDone(false);
        if (task.isDeleted()) {
            return restoreTask(task);
        }
        toDoTasks.add(task);
        doneTasks.remove(task);
        return updateFile();
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
        return doneTaskByObject(task);
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
        Task task = searchTaskById(allTasks, id);
        return doneTaskByObject(task);
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
    private boolean doneTaskByObject(Task task) {
        if (task == null || (!task.isDeleted() && task.isDone())) {
            return false; // Invalid ID or is undeleted done task
        }

        task.setDone(true);
        if (task.isDeleted()) {
            return restoreTask(task);
        }
        doneTasks.add(task);
        toDoTasks.remove(task);
        return updateFile();
    }
}