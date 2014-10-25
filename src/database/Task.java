package database;

import java.util.List;
import java.util.ArrayList;

/**
 * A Task object contains attributes to store a task's unique ID, description,
 * due date and time, scheduled starting date and time, scheduled ending date
 * and time, relevant tags to ease searching and categorizing, completion state,
 * and deletion state.
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class Task implements Comparable<Task> {

    /**
     * Unique ID for each new Task object. Increments at new Task instantiation.
     * Cannot decrement unless undoing a new Task creation.
     */
    private static int newId = 1;

    /** ID is set at object instantiation. */
    private final int ID;

    /** Description of task. */
    private String name = "";

    /** Due date and time, format: DD/MM/YYYY HHMM. */
    private DateTime due = new DateTime();

    /** Scheduled start date and time, format: DD/MM/YYYY HHMM. */
    private DateTime start = new DateTime();

    /** Scheduled end date and time, format: DD/MM/YYYY HHMM. */
    private DateTime end = new DateTime();

    /** Each stored tag contains a # before the word. */
    private List<String> tags = new ArrayList<String>();

    /** Tasks are created as to-do by default. */
    private boolean done = false;

    /** Deleted status takes precedence over to-do or done status. */
    private boolean deleted = false;

    /**
     * Constructor.
     * 
     * Used when creating a new Task. Increments ID counter.
     * 
     * @param name
     *            Task description.
     * @param due
     *            Due date and time, format: DD/MM/YYYY HHMM
     * @param start
     *            Scheduled start date and time, format: DD/MM/YYYY HHMM
     * @param end
     *            Scheduled end date and time, format: DD/MM/YYYY HHMM
     * @param tags
     *            To aid searching and categorizing. Each tag contains #.
     */
    public Task(String name, DateTime due, DateTime start, DateTime end,
            List<String> tags) {
        this.ID = newId++;
        this.name = name;
        this.due = due;
        this.start = start;
        this.end = end;
        this.tags = tags;
    }

    /**
     * Cloning constructor. Does not increment ID counter.
     * 
     * @param task
     *            The existing Task object to clone attributes by value from.
     */
    public Task(Task task) {
        this.ID = task.getId();
        this.name = task.getName();
        this.due = task.getDue();
        this.start = task.getStart();
        this.end = task.getEnd();
        this.tags.addAll(task.getTags());
        this.deleted = task.isDeleted();
        this.done = task.isDone();
    }

    /**
     * Compares this Task object with the specified Task object for order.
     * Returns a negative integer, zero, or a positive integer if this object's
     * due date is less earlier than, equal to, or later than that of the
     * specified object. Implementation is dependent on DateTime's compareTo().
     * 
     * @return A negative integer, zero, or a positive integer if this object's
     *         due date is less earlier than, equal to, or later than that of
     *         the specified object.
     */
    @Override
    public int compareTo(Task otherTask) {
        return this.due.compareTo(otherTask.due);
    }

    /**
     * Converts Task object to a single String to write to system file.
     * Parameter tags are added to aid in parsing text when reading from file.
     * 
     * @return String to write to system file for storage.
     */
    @Override
    public String toString() {
        String tempString = "";
        tempString += name + " ";
        tempString += "due: " + due.toString() + " ";
        tempString += "start: " + start.toString() + " ";
        tempString += "end: " + end.toString() + " ";
        tempString += concatanateTags();
        tempString += done ? "#done" : "#todo";

        return tempString;
    }

    /**
     * Concatenates list of tags into a single String.
     * 
     * @return Concatenated String of tags.
     */
    private String concatanateTags() {
        String concatanatedTags = "";
        if (!tags.isEmpty()) { // TODO Necessary?
            for (String tempTag : tags) {
                concatanatedTags += tempTag + " ";
            }
        }
        return concatanatedTags;
    }

    // TODO rename?
    /** Reduces ID counter when Task object is to be wiped. */
    public void wipeTask() {
        newId--;
    }

    // TODO rename?
    /** Resets ID counter when all Tasks objects are wiped. */
    public static void wipeAllTasks() {
        newId = 1;
    }

    public int getId() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Resets task description to an empty String. */
    public void resetName() {
        name = "";
    }

    public DateTime getDue() {
        return due;
    }

    public void setDue(DateTime due) {
        this.due = due;
    }

    /** Resets due date and time to an empty String. */
    public void resetDue() {
        due.resetDateTime();
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    /** Resets scheduled start date and time to an empty String. */
    public void resetStart() {
        start.resetDateTime();
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    /** Resets scheduled end date and time to an empty String. */
    public void resetEnd() {
        end.resetDateTime();
    }

    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags
     *            List of tags to append to existing list of tags.
     */
    public void setTags(List<String> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    /**
     * @param tags
     *            List of tags to remove from existing list of tags.
     */
    public void removeTags(List<String> tags) {
        this.tags.removeAll(tags);
    }

    /** Resets list of tags to an empty list. */
    public void resetTags() {
        tags.clear();
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}