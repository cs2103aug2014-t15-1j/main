package Storage;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * The DateTime class encapsulates date and time attributes in one neat little
 * package. Date and time attributes are managed as Strings.
 * 
 * @author PierceAndy
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
     * @param date
     *            value to initialize object's date attribute to.
     * @param time
     *            value to initialize object's time attribute to.
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
     *            the existing DateTime object to clone date and time attributes
     *            by value from.
     */
    public DateTime(DateTime dateTime) {
        this.date = dateTime.date;
        this.time = dateTime.time;
    }

    @Getter
    public String getDate() {
        return date;
    }

    @Setter
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Resets date attribute to empty String.
     */
    public void resetDate() {
        date = "";
    }

    @Getter
    public String getTime() {
        return time;
    }

    @Setter
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Resets time attribute to empty String.
     */
    public void resetTime() {
        time = "";
    }

    /**
     * Resets date and time attributes to empty Strings.
     */
    public void resetDateTime() {
        resetDate();
        resetTime();
    }

}
