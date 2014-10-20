package database;


/**
 * The BlockDate class keeps track of the start/end dates and times of a blocked
 * time period.
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class BlockDate implements Comparable<BlockDate> {

    private DateTime start;
    private DateTime end;

    /** Default constructor. Initialized with default DateTime objects. */
    public BlockDate() {
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
        this.start = new DateTime(start);
        this.end = new DateTime(end);
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
    
    public boolean contains(BlockDate blockDate) {
        boolean contains = false;
        //Within Range of days
        if (this.getStart().compareTo(blockDate.getStart()) >= 0 && this.getEnd().compareTo(blockDate.getEnd()) <= 0) {
            contains = true;
        }
        return contains;
    }
    
    public boolean equals(Object obj) {
        BlockDate blockDate = (BlockDate) obj;
        return blockDate.getStart().equalsTo(this.getStart()) && 
                blockDate.getEnd().equalsTo(this.getEnd());
    }
    
    public int compareTo(BlockDate blockDate) {
        return this.getEnd().compareTo(blockDate.getStart());
    }
}