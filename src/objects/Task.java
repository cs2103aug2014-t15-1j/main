//@author A0116373J
package objects;

import java.util.List;
import java.util.ArrayList;

import parser.Parser;

/**
 * A Task object contains attributes to store a task's unique id, description,
 * start date and time, due date and time, completed date and time, relevant
 * tags to ease searching and categorizing, task type, and deletion state.
 */

public class Task implements Comparable<Task> {

    /**
     * Unique id for each new Task object. Increments at new Task instantiation.
     * Should be decremented when undoing an object creation.
     */
    private static int newId = 1;

    /** Id is set at object instantiation. */
    private final int ID;

    /** Description of task. */
    private String name = "";

    /** Scheduled start date and time, format: DD/MM/YYYY HHMM. */
    private DateTime start = new DateTime();

    /** Scheduled end or due date and time, format: DD/MM/YYYY HHMM. */
    private DateTime due = new DateTime();

    /** Date and time at which task was marked as done, format: DD/MM/YYYY HHMM. */
    private DateTime completedOn = new DateTime();

    /** Each stored tag contains a # before the word. */
    private List<String> tags = new ArrayList<String>();

    /** Tasks can be of todo, done, or block type. */
    private TaskType type = TaskType.TODO;

    /** Deleted status takes precedence over type. */
    private boolean deleted = false;

    /** Default constructor. Empty object attributes. Doesn't increment id. */
    public Task() {
        ID = 0;
    }

    /**
     * Constructor. Used when creating a new Task. Increments ID counter.
     * 
     * @param name
     *            Task description.
     * @param start
     *            Start date and time, format: DD/MM/YYYY HHMM
     * @param due
     *            End/due date and time, format: DD/MM/YYYY HHMM
     * @param completedOn
     *            Date and time task was completed on, format: DD/MM/YYYY HHMM
     * @param tags
     *            To aid searching and categorizing. Each tag contains #.
     * @param type
     *            Type of task.
     */
    public Task(String name, DateTime start, DateTime due,
            DateTime completedOn, List<String> tags, TaskType type) {
        assert name != null : "Name cannot be null";
        assert start != null : "Start cannot be null";
        assert start.toString().matches(DateTime.getDateTimePattern()) : "Start must have correct format & value";
        assert due != null : "Due cannot be null";
        assert due.toString().matches(DateTime.getDateTimePattern()) : "Due must have correct format & value";
        assert completedOn != null : "CompletedOn cannot be null";
        assert completedOn.toString().matches(DateTime.getDateTimePattern()) : "CompletedOn must have correct format & value";
        assert tags != null : "Tags cannot be null";
        assert type != null : "Type cannot be null";
        if (type == TaskType.BLOCK) {
            assert !start.isEmpty() : "Block types must have start";
            assert !due.isEmpty() : "Block types must have due";
        }

        this.ID = newId++;
        this.name = name;
        this.start = new DateTime(start);
        this.due = new DateTime(due);
        this.completedOn = new DateTime(completedOn);
        this.tags.addAll(tags);
        this.type = type;
    }

    /**
     * Copy constructor. Does not increment ID counter.
     * 
     * Creates a deep copy of provided Task object.
     * 
     * @param task
     *            The existing Task object to create a deep copy from.
     */
    public Task(Task task) {
        assert task != null : "Cannot clone null";

        ID = task.ID;
        name = task.name;
        start = new DateTime(task.start);
        due = new DateTime(task.due);
        completedOn = new DateTime(task.completedOn);
        tags.addAll(task.tags);
        type = task.type;
        deleted = task.deleted;
    }

    /**
     * Compares this Task object with the argument Task object for ordering.
     * Returns a negative integer, zero, or a positive integer if this object's
     * DateTime is earlier than, equal to, or later than that of the specified
     * object. Start DateTimes are first used in the comparison if they are not
     * empty, otherwise due DateTimes are used. Implementation is dependent on
     * DateTime's compareTo().
     * 
     * @return A negative integer, zero, or a positive integer if this object's
     *         date is earlier than, equal to, or later than that of the
     *         argument object.
     */
    @Override
    public int compareTo(Task otherTask) {
        DateTime taskDate = this.due;
        DateTime otherTaskDate = otherTask.due;
        if (!this.start.isEmpty()) {
            taskDate = this.start;
        }
        if (!otherTask.start.isEmpty()) {
            otherTaskDate = otherTask.start;
        }
        return taskDate.compareTo(otherTaskDate);
    }

    /**
     * Indicates whether some other object is "equal to" this Task object.
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
        return ID == otherTask.ID && type.equals(otherTask.type) &&
               name.equals(otherTask.name) && start.equals(otherTask.start) &&
               due.equals(otherTask.due) &&
               completedOn.equals(otherTask.completedOn) &&
               tags.equals(otherTask.tags);
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
     * Converts Task object to a single String. Parameters are added to aid in
     * parsing text when reading from file.
     * 
     * @return Task object in String format with parameters.
     */
    @Override
    public String toString() {
        String tempString = "";
        tempString += name + " ### ";
        tempString += "start: " + start.toString() + " ";
        tempString += "due: " + due.toString() + " ";
        tempString += "completed: " + completedOn.toString() + " ";
        tempString += concatanateTags();
        tempString += "type: " + type.toString();

        return tempString;
    }

    /**
     * Concatenates list of tags into a single String.
     * 
     * @return Concatenated String of tags.
     */
    private String concatanateTags() {
        String concatanatedTags = "";
        for (String tempTag : tags) {
            concatanatedTags += tempTag + " ";
        }
        return concatanatedTags;
    }

    /**
     * Stores searchable Task attributes in a single String. To be processed by
     * a search function matching search terms.
     * 
     * @return String containing searchable Task object attributes.
     */
    public String getSummary() {
        String summary = name + " ";
        summary += start + " ";
        summary += due + " ";
        summary += concatanateTags();
        summary += type;
        return summary;
    }

    /**
     * Task object id getter.
     * 
     * @return Task object's id.
     */
    public int getId() {
        return ID;
    }

    /** Reduces ID counter by one. */
    public static void decrementId() {
        --newId;
    }

    /** Resets ID counter. */
    public static void resetId() {
        newId = 1;
    }

    /**
     * Task object name getter.
     * 
     * @return Task object's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Task object name setter
     * 
     * @param name
     *            The new name to set the Task object to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Resets task description to an empty String. */
    public void resetName() {
        name = "";
    }

    /**
     * Task object start getter.
     * 
     * @return Task object's start.
     */
    public DateTime getStart() {
        DateTime copyOfStart = new DateTime(start);
        return copyOfStart;
    }

    /**
     * Task object start setter.
     * 
     * @param start
     *            The new start to set the Task object to.
     */
    public void setStart(DateTime start) {
        this.start = new DateTime(start);
    }

    /** Resets scheduled start date and time to an empty String. */
    public void resetStart() {
        start.resetDateTime();
    }

    /**
     * Task object due getter.
     * 
     * @return Task object's due.
     */
    public DateTime getDue() {
        DateTime copyOfDue = new DateTime(due);
        return copyOfDue;
    }

    /**
     * Task object due setter.
     * 
     * @param due
     *            The new due to set the Task object to.
     */
    public void setDue(DateTime due) {
        this.due = new DateTime(due);
    }

    /** Resets due date and time to an empty String. */
    public void resetDue() {
        due.resetDateTime();
    }

    /**
     * Task object completedOn getter.
     * 
     * @return Task object's completedOn.
     */
    public DateTime getCompletedOn() {
        DateTime copyOfCompletedOn = new DateTime(completedOn);
        return copyOfCompletedOn;
    }

    /**
     * Task object tags getter.
     * 
     * @return Task object's tags.
     */
    public List<String> getTags() {
        List<String> copyOfTags = new ArrayList<String>();
        copyOfTags.addAll(tags);
        return copyOfTags;
    }

    /**
     * Task object tags setter.
     * 
     * @param tags
     *            The new tags to set the Task object to.
     */
    public void setTags(List<String> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    /** Resets list of tags to an empty list. */
    public void resetTags() {
        tags.clear();
    }

    /** Checks if is todo type */
    public boolean isToDo() {
        return type == TaskType.TODO;
    }

    /** Checks if is done type */
    public boolean isDone() {
        return type == TaskType.DONE;
    }

    /** Checks if is block type */
    public boolean isBlock() {
        return type == TaskType.BLOCK;
    }

    /**
     * Task object type getter.
     * 
     * @return Task object's type.
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Task object type setter. If set to done, completedOn is set to that
     * instant. If set to todo or block, completedOn is reset.
     * 
     * @param type
     *            The new type to set the Task object to.
     */
    public void setType(TaskType type) {
        this.type = type;
        if (type == TaskType.DONE && completedOn.isEmpty()) {
            completedOn = new DateTime(Parser.getCurrDateTime());
            assert completedOn.toString()
                    .matches(DateTime.getDateTimePattern()) : "CompletedOn must have correct format & value";
        } else if (type == TaskType.TODO || type == TaskType.BLOCK) {
            completedOn.resetDateTime();
        }
    }

    /** Checks if is deleted */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Task object deleted setter.
     * 
     * @param deleted
     *            The deleted status to set the Task object to.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /** Checks if is floating task, i.e. has no start or end date. */
    public boolean isFloating() {
        return start.isEmpty() && due.isEmpty();
    }
}