package database;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

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
    final private static String FILENAME_TASK = "data_tasks.txt";
    /** Name of file to write BlackDates to. */
    final private static String FILENAME_BLOCKDATE = "data_blockdates.txt";
    
    /** Reads from file containing tasks */
    private TaskReader taskReader;

    /** Writes to file containing tasks */
    private TaskWriter taskWriter;
    
    /** Reads from file containing BlockDates. */
    private BlockDateReader blockDateReader;

    /** Writes to file containing BlockDates. */
    private BlockDateWriter blockDateWriter;

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

    /** Contains undeleted blocked dates. */
    private static List<BlockDate> allBlockDates = new ArrayList<BlockDate>();

    /** Contains deleted blocked dates. */
    private static List<BlockDate> deletedBlockDates = new ArrayList<BlockDate>();

    /**
     * Default constructor.
     * 
     * Checks if task file exists and allTasks list is empty before populating
     * list. If allTasks list is already populated, it signals that other
     * DataFile instances exist, and avoids populating task lists with duplicate
     * Task objects from file.
     */
    public DataFile() {
        taskReader = new TaskReader(FILENAME_TASK);
        taskWriter = new TaskWriter(FILENAME_TASK);
        blockDateReader = new BlockDateReader(FILENAME_BLOCKDATE);
        blockDateWriter = new BlockDateWriter(FILENAME_BLOCKDATE);
        if (taskReader.fileExists() && allTasks.isEmpty()) {
            populateTaskLists();
        }
        if (blockDateReader.fileExists() && allBlockDates.isEmpty()) {
            populateBlockDateList();
        }
    }

    /**
     * Populates task lists with data from system file. Deleted tasks are not
     * written to file, hence the deleted task list is not populated.
     * 
     * Benefits from branch prediction because of the order in which tasks are
     * written to the file. See {@link updateTaskFile()}.
     * 
     * @param file
     *            The file to read from.
     */
    private void populateTaskLists() {
        allTasks.addAll(taskReader.read());
        for (Task tempTask : allTasks) {
            if (!tempTask.isDone()) {
                toDoTasks.add(tempTask);
            } else {
                doneTasks.add(tempTask);
            }
        }
    }

    private void populateBlockDateList() {
        allBlockDates.addAll(blockDateReader.read());
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
        Collections.sort(toDoTasks, new Task());
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
        Collections.sort(doneTasks, new Task());
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
        Collections.sort(deletedTasks, new Task());
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
        Collections.sort(allTasks, new Task());
        return Collections.unmodifiableList(allTasks);
    }

    /**
     * Returns an unmodifiable view of the list of all undeleted blocked dates.
     * Attempts to modify the returned list, whether direct or via its iterator,
     * result in an UnsupportedOperationException.
     * 
     * @return Unmodifiable view of the specified list.
     */
    public List<BlockDate> getAllBlockDates() {
        Collections.sort(allBlockDates, new BlockDate());
        return Collections.unmodifiableList(allBlockDates);
    }

    /**
     * Returns an unmodifiable view of the list of deleted blocked dates.
     * Attempts to modify the returned list, whether direct or via its iterator,
     * result in an UnsupportedOperationException.
     * 
     * @return Unmodifiable view of the specified list.
     */
    public List<BlockDate> getDeletedBlockDates() {
        return Collections.unmodifiableList(deletedBlockDates);
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
    
    public BlockDate getBlockDate(int id) {
        BlockDate bD = searchBlockDateById(allBlockDates, id);
        return bD;
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
     * Given id, returns BlockDate object from specified task list.
     * 
     * @param blockDates
     *            The BlockDate list to search from.
     * @param id
     *            The BlockDate's unique ID.
     * @return BlockDate object of matching ID, or null if it is not in the
     *         list.
     */
    private BlockDate searchBlockDateById(List<BlockDate> blockDates, int id) {
        for (BlockDate bD : blockDates) {
            if (bD.getId() == id) {
                return bD;
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
        return updateTaskFile();
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
    private boolean updateTaskFile() {
        String updatedTaskInfo = getTaskInfo(toDoTasks);
        updatedTaskInfo += getTaskInfo(doneTasks);
        return writeTasksToFile(updatedTaskInfo);
    }
    
    private boolean updateBlockDateFile() {
        String updatedBlockDateInfo = getBlockDateInfo(allBlockDates);
        return writeBlockDatesToFile(updatedBlockDateInfo);
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

    private String getBlockDateInfo(List<BlockDate> blockDates) {
        String allBlockDateInfo = "";
        for (BlockDate bD : blockDates) {
            allBlockDateInfo += bD.toString() + "\n"; 
        }
        return allBlockDateInfo;
    }
    
    /**
     * Writes String containing all to-do and done task info to system file.
     * 
     * @param allTaskInfo
     *            Data of all to-do and done tasks to write to file.
     * @return True, if successfully written to file.
     */
    private boolean writeTasksToFile(String allTaskInfo) {
        return taskWriter.write(allTaskInfo);
    }
    
    private boolean writeBlockDatesToFile(String allBlockDateInfo) {
        return blockDateWriter.write(allBlockDateInfo);
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
    private boolean updateTaskInfoByObject(Task task, String name,
                                           DateTime due, DateTime start,
                                           DateTime end, List<String> tags) {
        if (task == null) {
            return false; // Invalid ID
        }

        if (name == null) {
            task.resetName();
        } else if (!name.isEmpty()) {
            task.setName(name);
        }

        if (due == null) {
            task.resetDue();
        } else if (!due.toString().isEmpty()) {
            task.setDue(due);
        }

        if (start == null) {
            task.resetStart();
        } else if (!start.toString().isEmpty()) {
            task.setStart(start);
        }

        if (end == null) {
            task.resetEnd();
        } else if (!end.toString().isEmpty()) {
            task.setEnd(end);
        }

        if (tags == null) {
            task.resetTags();
        } else if (!tags.isEmpty()) {
            task.setTags(tags);
        }

        return updateTaskFile();
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
        return updateTaskFile();
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
        return updateTaskFile();
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
        return updateTaskFile();
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
        BlockDate.wipeAllBlockDates();
        allTasks.clear();
        toDoTasks.clear();
        doneTasks.clear();
        deletedTasks.clear();
        allBlockDates.clear();
        deletedBlockDates.clear();

        return updateTaskFile() && updateBlockDateFile();
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
        return updateTaskFile();
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
        return updateTaskFile();
    }

    /**
     * Adds a new BlockDate to lists and file. Populates relevant list, and
     * updates file with new information.
     * 
     * @param bD
     *            New BlockDate object to be written to file.
     * @return True, if successfully written to file.
     */
    public boolean addNewBD(BlockDate bD) {
        allBlockDates.add(bD);
        return updateBlockDateFile();
    }

    /**
     * Deletes BlockDate object based on the ID provided in argument. Updates
     * BlockDate lists and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            The ID of the BlockDate object to delete.
     * @return True, if file has been successfully updated with deletion.
     */
    public boolean deleteBD(int id) {
        BlockDate bD = searchBlockDateById(allBlockDates, id);
        return deleteBDByObject(bD);
    }

    /**
     * Deletes BlockDate object provided in argument. Updates BlockDate lists
     * and file.
     * 
     * Overloaded function.
     * 
     * @param bD
     *            The BlockDate object to delete.
     * @return True, if file has been successfully updated with deletion.
     */
    public boolean deleteBD(BlockDate bD) {
        return deleteBDByObject(bD);
    }

    /**
     * Deletes object provided in argument. Removes object from allBlockDates
     * list, adds to deletedBlockDates list, and updates file.
     * 
     * @param bD
     *            The BlockDate object to delete.
     * @return True, if file has been successfully updated with deletion.
     */
    private boolean deleteBDByObject(BlockDate bD) {
        allBlockDates.remove(bD);
        deletedBlockDates.add(bD);
        return updateBlockDateFile();
    }

    /**
     * Restores BlockDate object based on the ID provided in argument. Updates
     * BlockDate lists and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            The ID of the BlockDate object to restore.
     * @return True, if file has been successfully updated with restoration.
     */
    public boolean restoreBD(int id) {
        BlockDate bD = searchBlockDateById(deletedBlockDates, id);
        return restoreBDByObject(bD);
    }

    /**
     * Restores BlockDate object provided in argument. Updates BlockDate lists
     * and file.
     * 
     * Overloaded function.
     * 
     * @param bD
     *            The BlockDate object to restore.
     * @return True, if file has been successfully updated with restoration.
     */
    public boolean restoreBD(BlockDate bD) {
        return restoreBDByObject(bD);
    }

    /**
     * Restores object provided in argument. Removes object from
     * deletedBlockDates list, adds to allBlockDates list, and updates file.
     * 
     * @param bD
     *            The BlockDate object to restore.
     * @return True, if file has been successfully updated with restoration.
     */
    private boolean restoreBDByObject(BlockDate bD) {
        deletedBlockDates.remove(bD);
        allBlockDates.add(bD);
        return updateBlockDateFile();
    }
    
    /**
     * Permanently deletes BlockDate object provided in argument. Cannot be undone.
     * Used when undoing add commands. Decrements BlockDate ID counter. Updates BlockDate 
     * lists and file.
     * 
     * Overloaded function.
     * 
     * @param task
     *            The BlockDate object to permanently delete.
     * @return True, if file has been successfully updated with wipe.
     */
    public boolean wipeBD(BlockDate bD) {
        return wipeBDByObject(bD);
    }

    /**
     * Permanently deletes BlockDate object based on the ID provided in argument.
     * Cannot be undone. Used when undoing add commands. Decrements BlockDate ID
     * counter. Updates BlockDate lists and file.
     * 
     * Overloaded function.
     * 
     * @param id
     *            The ID of the BlockDate object to permanently delete.
     * @return True, if file has been successfully updated with wipe.
     */
    public boolean wipeBD(int id) {
        BlockDate bD = searchBlockDateById(allBlockDates, id);
        return wipeBDByObject(bD);
    }

    /**
     * Permanently deletes BlockDate object provided in argument. Cannot be undone.
     * Used when undoing add commands. Decrements BlockDate ID counter. Removes
     * object from all lists, and updates file.
     * 
     * @param task
     *            The BlockDate object to permanently delete.
     * @return True, if file has been successfully updated with wipe.
     */
    private boolean wipeBDByObject(BlockDate bD) {
        if (bD == null) {
            return false; // Invalid ID
        }

        allBlockDates.remove(bD);
        bD.wipeBlockDate();
        return updateBlockDateFile();
    }
}