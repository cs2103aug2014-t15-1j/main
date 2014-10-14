package Storage;

/**
 * The DateTime class encapsulates date and time attributes in one neat little
 * package. Date and time attributes are managed as Strings.
 * 
 * Date and time formats, respectively: DD/MM/YYYY HHMM.
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class DateTime {

    /** Date format: DD/MM/YYYY */
    private String date = "";

    /** Time format: HHMM */
    private String time = "";

    /** Default constructor. Attributes are initialized as empty Strings. */
    public DateTime() {
    }

    /**
     * Constructor.
     * 
     * TODO remove magic numbers
     * 
     * @param dateTime
     *            Format: DD/MM/YYYY HHMM.
     */
    public DateTime(String dateTime) {
        String tempString[] = dateTime.split(" ");
        date = tempString[0];
        time = tempString[1];
    }

    /**
     * Constructor.
     * 
     * @param date
     *            Format: DD/MM/YYYY.
     * @param time
     *            Format: HHMM.
     */
    public DateTime(String date, String time) {
        this.date = date;
        this.time = time;
    }

    /**
     * Cloning constructor.
     * 
     * Takes a DateTime object, clones its date and time attributes by value,
     * and creates a new DateTime object with the cloned values.
     * 
     * @param dateTime
     *            The existing DateTime object to clone date and time attributes
     *            by value from.
     */
    public DateTime(DateTime dateTime) {
        this.date = dateTime.date;
        this.time = dateTime.time;
    }
    
    /**
     * TODO comment
     */
    public String toString() {
        return date + time; 
    }

    /** @return Date, format: DD/MM/YYYY. */
    public String getDate() {
        return date;
    }

    /**
     * @param date
     *            Format: DD/MM/YYYY.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /** Resets date attribute to empty String. */
    public void resetDate() {
        date = "";
    }

    /** @return Time, format: HHMM. */
    public String getTime() {
        return time;
    }

    /**
     * @param time
     *            Format: HHMM.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /** Resets time attribute to empty String. */
    public void resetTime() {
        time = "";
    }

    /** Resets date and time attributes to empty Strings. */
    public void resetDateTime() {
        resetDate();
        resetTime();
    }
}