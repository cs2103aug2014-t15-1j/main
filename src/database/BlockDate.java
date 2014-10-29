package database;

import java.util.Comparator;

/**
 * The BlockDate class keeps track of the start/end dates and times of a blocked
 * time period.
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class BlockDate implements Comparable<BlockDate>, Comparator<BlockDate> {

    /**
     * Unique ID for each new Task object. Increments at new Task instantiation.
     * Cannot decrement unless undoing a new Task creation.
     */
    private static int newId = 1;

    /** ID is set at object instantiation. */
    private final int ID;

    private DateTime start;
    private DateTime end;

    /** Default constructor. Initialized with default DateTime objects. */
    public BlockDate() {
        ID = newId++;
        start = new DateTime();
        end = new DateTime();
    }

    /**
     * Constructor.
     * 
     * @param start
     *            When blocked time period starts.
     * @param end
     *            When blocked time period ends.
     */
    public BlockDate(DateTime start, DateTime end) {
        ID = newId++;
        this.start = new DateTime(start);
        this.end = new DateTime(end);
    }

    /**
     * Converts <code>BlockDate</code> to a <code>String</code> containing "
     * <code>DateTime1</code> to <code>DateTime2</code>"
     * 
     * @author Yeo Zi Xian, Justin
     */
    @Override
    public String toString() {
        return start.toString() + " to " + end.toString();
    }

    @Override
    public int compareTo(BlockDate blockDate) {
        return this.getEnd().compareTo(blockDate.getStart());
    }
    
    @Override
    public int compare(BlockDate blockDate, BlockDate otherBlockDate) {
        if (blockDate.getId() < blockDate.getId()) {
            return -1;
        } else if (blockDate.getId() > blockDate.getId()) {
            return 1;
        } else {
            return 0;
        }
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
        if (!(obj instanceof BlockDate)) {
            return false;
        } else if (obj == this) {
            return true;
        }

        BlockDate otherBlockDate = (BlockDate) obj;
        return otherBlockDate.start.equals(this.start) &&
               otherBlockDate.end.equals(this.end);
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
        return start.hashCode() + end.hashCode();
    }

    public boolean contains(BlockDate blockDate) {
        boolean contains = false;
        // Within Range of days
        if (this.getStart().compareTo(blockDate.getStart()) <= 0 &&
            this.getEnd().compareTo(blockDate.getEnd()) >= 0) {
            contains = true;
        }
        
        return contains;
    }

    // TODO rename?
    /** Reduces ID counter when BlockDate object is to be wiped. */
    public void wipeBlockDate() {
        newId--;
    }

    // TODO rename?
    /** Resets ID counter when all BlockDate objects are wiped. */
    public static void wipeAllBlockDates() {
        newId = 1;
    }

    public int getId() {
        return ID;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    /** Resets start's DateTime object's attributes to empty Strings. */
    public void resetStart() {
        start.resetDateTime();
    }

    public String getStartDate() {
        return start.getDate();
    }

    public void setStartDate(String startDate) {
        start.setDate(startDate);
    }

    /** Resets start's DateTime object's date to empty String. */
    public void resetStartDate() {
        start.resetDate();
    }

    public String getStartTime() {
        return start.getTime();
    }

    public void setStartTime(String startTime) {
        start.setTime(startTime);
    }

    /** Resets start's DateTime object's time to empty String. */
    public void resetStartTime() {
        start.resetTime();
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    /** Resets end's DateTime object's attributes to empty Strings. */
    public void resetEnd() {
        end.resetDateTime();
    }

    public String getEndDate() {
        return end.getDate();
    }

    public void setEndDate(String endDate) {
        end.setDate(endDate);
    }

    /** Resets end's DateTime object's date to empty String. */
    public void resetEndDate() {
        end.resetDate();
    }

    public String getEndTime() {
        return end.getTime();
    }

    public void setEndTime(String endTime) {
        end.setTime(endTime);
    }

    /** Resets end's DateTime object's time to empty String. */
    public void resetEndTime() {
        end.resetTime();
    }
}