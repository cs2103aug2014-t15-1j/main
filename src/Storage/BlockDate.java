package Storage;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * The BlockDate class keeps track of the start/end dates and times of a blocked
 * time period.
 * 
 * @author PierceAndy
 * 
 */

public class BlockDate {
    private DateTime start;
    private DateTime end;

    /** Default constructor. Attributes are initialized as empty DateTimes. */
    public BlockDate() {
        start = new DateTime();
        end = new DateTime();
    }

    /**
     * Constructor.
     * 
     * @param start
     *            when the blocked period starts.
     * @param end
     *            when the blocked period ends.
     */
    public BlockDate(DateTime start, DateTime end) {
        this.start = new DateTime(start);
        this.end = new DateTime(end);
    }

    @Getter
    public DateTime getStart() {
        return start;
    }

    @Setter
    public void setStart(DateTime start) {
        this.start = start;
    }

    /**
     * Resets start to empty DateTime.
     */
    public void resetStart() {
        start.resetDateTime();
    }

    @Getter
    public String getStartDate() {
        return start.getDate();
    }

    @Setter
    public void setStartDate(String startDate) {
        start.setDate(startDate);
    }

    /**
     * Resets start date to empty value.
     */
    public void resetStartDate() {
        start.resetDate();
    }

    @Getter
    public String getStartTime() {
        return start.getTime();
    }

    @Setter
    public void setStartTime(String startTime) {
        start.setTime(startTime);
    }

    /**
     * Resets start time to empty value.
     */
    public void resetStartTime() {
        start.resetTime();
    }

    @Getter
    public DateTime getEnd() {
        return end;
    }

    @Setter
    public void setEnd(DateTime end) {
        this.end = end;
    }

    /**
     * Resets end to empty DateTime.
     */
    public void resetEnd() {
        end.resetDateTime();
    }

    @Getter
    public String getEndDate() {
        return end.getDate();
    }

    @Setter
    public void setEndDate(String endDate) {
        end.setDate(endDate);
    }

    /**
     * Resets end date to empty value.
     */
    public void resetEndDate() {
        end.resetDate();
    }

    @Getter
    public String getEndTime() {
        return end.getTime();
    }

    @Setter
    public void setEndTime(String endTime) {
        end.setTime(endTime);
    }

    /**
     * Resets end time to empty value.
     */
    public void resetEndTime() {
        end.resetTime();
    }
}
