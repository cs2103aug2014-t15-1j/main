package database;

import java.util.Comparator;
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

public class Task implements Comparable<Task>, Comparator<Task> {

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

    public Task() {
        this.ID = 0;
    }

    /**
     * Converts Task object to a single String to write to system file.
     * Parameter text are added to aid in parsing text when reading from file.
     * 
     * @return String to write to system file for storage.
     */
    @Override
    public String toString() {
        String tempString = "";
        tempString += name + " ### ";
        tempString += "due: " + due.toString() + " ";
        tempString += "start: " + start.toString() + " ";
        tempString += "end: " + end.toString() + " ";
        tempString += concatanateTags();
        tempString += "status: " + (done ? "done" : "todo");

        return tempString;
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
        summary += concatanateTags();
        return summary;
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
     * Indicates whether some other object is "equal to" this one.
     * 
     * The equals method implements an equivalence relation on non-null object
     * references:
     * <ul>
     * <li>It is reflexive: for any non-null reference value x, x.equals(x)
     * should return true.
     * <li>It is symmetric: for any non-null reference values x and y,
     * x.equals(y) should return true if and only if y.equals(x) returns true.
     * <li>It is transitive: for any non-null reference values x, y, and z, if
     * x.equals(y) returns true and y.equals(z) returns true, then x.equals(z)
     * should return true.
     * <li>It is consistent: for any non-null reference values x and y, multiple
     * invocations of x.equals(y) consistently return true or consistently
     * return false, provided no information used in equals comparisons on the
     * objects is modified.
     * <li>For any non-null reference value x, x.equals(null) should return
     * false.
     * </ul>
     * 
     * The equals method for class Object implements the most discriminating
     * possible equivalence relation on objects; that is, for any non-null
     * reference values x and y, this method returns true if and only if x and y
     * refer to the same object (x == y has the value true).
     * 
     * Note that it is generally necessary to override the hashCode method
     * whenever this method is overridden, so as to maintain the general
     * contract for the hashCode method, which states that equal objects must
     * have equal hash codes.
     * 
     * 
     * @param obj
     *            The reference object with which to compare.
     * 
     * @returns True if this object is the same as the obj argument; false
     *          otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Task)) {
            return false;
        } else if (obj == this) {
            return true;
        }

        Task otherTask = (Task) obj;
        return otherTask.deleted == this.deleted &&
               otherTask.done == this.done &&
               otherTask.name.equals(this.name) &&
               otherTask.due.equals(this.due) &&
               otherTask.start.equals(this.start) &&
               otherTask.end.equals(this.end) &&
               otherTask.tags.equals(this.tags);
    }

    /**
     * Returns a hash code value for the object. This method is supported for
     * the benefit of hash tables such as those provided by HashMap.
     * 
     * The general contract of hashCode is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during an
     * execution of a Java application, the hashCode method must consistently
     * return the same integer, provided no information used in equals
     * comparisons on the object is modified. This integer need not remain
     * consistent from one execution of an application to another execution of
     * the same application.
     * <li>If two objects are equal according to the equals(Object) method, then
     * calling the hashCode method on each of the two objects must produce the
     * same integer result.
     * <li>It is not required that if two objects are unequal according to the
     * equals(java.lang.Object) method, then calling the hashCode method on each
     * of the two objects must produce distinct integer results. However, the
     * programmer should be aware that producing distinct integer results for
     * unequal objects may improve the performance of hash tables.
     * </ul>
     * 
     * As much as is reasonably practical, the hashCode method defined by class
     * Object does return distinct integers for distinct objects.
     * 
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
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

    @Override
    public int compare(Task task, Task otherTask) {
        if (task.getId() < otherTask.getId()) {
            return -1;
        } else if (task.getId() > otherTask.getId()) {
            return 1;
        } else {
            return 0;
        }
    }
}