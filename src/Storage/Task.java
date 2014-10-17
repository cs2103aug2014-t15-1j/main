package Storage;

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

public class Task {

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
        this.ID = newId;
        newId++;
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
     * Converts Task object to a single String to write to system file.
     * Parameter tags are added to aid in parsing text when reading from file.
     * TODO fix extraneous spaces when attributes are empty.
     * 
     * @return String to write to system file for storage.
     */
    @Override
    public String toString() {
        String fullInfo = "Name: " + name + " ";
        fullInfo += "Due: " + due.toString() + " ";
        fullInfo += "Start: " + start.toString() + " ";
        fullInfo += "End: " + end.toString() + " ";
        fullInfo += concatanateTags();
        fullInfo += done ? "#done" : "#todo";

        return fullInfo;
    }

    /**
     * Stores searchable Task attributes in a single String. To be processed by
     * a search function matching search terms.
     * 
     * @return String containing searchable Task attributes.
     */
    public String getSummary() {
        String summary = name + " ";
        summary += due + " ";
        summary += start + " ";
        summary += end + " ";
        return summary;
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

    /** Reduces ID counter when Task object is to be wiped. */
    public void wipeTask() {
        newId--;
    }

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
    public void addTags(List<String> tags) {
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
    /*
     * Legacy code from YX public Task(Command cmd) { this.cmdType =
     * cmd.getType(); this.details = ; this.numTasks++; }
     * 
     * public Command getCommand() { return this.cmd; }
     * 
     * public boolean delete() { numTasks--; // Do some deleting return true; }
     * 
     * public Map<String, String> getDetails() { return this.details; }
     * 
     * @Override public String toString() { String output = ""; for
     * (Map.Entry<String, String> entry : details.entrySet()) { output +=
     * entry.getKey() + ": " + entry.getValue() + "\n"; }
     * 
     * if (output.length() > 1) { output = output.substring(0, output.length() -
     * 1); }
     * 
     * return output; }
     */
}