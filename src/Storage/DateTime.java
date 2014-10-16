package Storage;

/**
 * The DateTime class encapsulates date and time attributes in one neat little
 * package. Date and time attributes are managed as Strings.
 * 
 * Date and time formats, respectively: "DD/MM/YYYY" and "HHMM".
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class DateTime {

    /** Regex to check for empty Strings as valid input. */
    private final static String OR_EMPTY_PATTERN = "|^$";

    /** Regex for valid date values and format: "DD/MM/YYYY" or empty String. */
    private final static String DATE_PATTERN = "((((((0[1-9]|[12][0-9]|3[0-1])/(0[13578]|1[02]))|" +
                                               "((0[1-9]|[12][0-9]|30)/(0[469]|11))|" +
                                               "((0[1-9]|1[0-9]|2[0-8])/02))/\\d{4})|" +
                                               "(29/02/((\\d{2}(0[48]|[2468][048]|[13579][26]))|" +
                                               "(([02468][048]|[13579][26])00))))" +
                                               OR_EMPTY_PATTERN + ")";

    /** Regex for valid time values and format: "HHMM" or empty String. */
    private final static String TIME_PATTERN = "(((([0-1][0-9])|(2[0-3]))[0-5][0-9])" +
                                               OR_EMPTY_PATTERN + ")";

    /**
     * Regex for valid date and time values and format: "DD/MM/YYYY HHMM",
     * "DD/MM/YYYY", "HHMM", or empty String.
     */
    private final static String DATE_TIME_PATTERN = "(" + DATE_PATTERN + " " +
                                                    TIME_PATTERN + ")|" +
                                                    DATE_PATTERN + "|" +
                                                    TIME_PATTERN +
                                                    OR_EMPTY_PATTERN;

    /** Date format: "DD/MM/YYYY" */
    private String date = "";

    /** Time format: "HHMM" */
    private String time = "";

    /** Default constructor. Attributes are initialized as empty Strings. */
    public DateTime() {
    }

    /**
     * Constructor.
     * 
     * @param date
     *            Format: "DD/MM/YYYY" or empty String.
     * @param time
     *            Format: "HHMM" or empty String.
     */
    public DateTime(String date, String time) {
        assert date.matches(DATE_PATTERN);
        assert time.matches(TIME_PATTERN);
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
        assert dateTime.toString().matches(DATE_TIME_PATTERN);
        this.date = dateTime.date;
        this.time = dateTime.time;
    }

    /**
     * Returns a String object representing this DateTime's value.
     * 
     * @return A String representation of the date and time value of this
     *         object. Format: "DD/MM/YYYY HHMM", ""DD/MM/YYYY", "HHMM", or
     *         empty String.
     */
    @Override
    public String toString() {
        return date + " " + time;
    }

    /** @return Date, format: "DD/MM/YYYY" or empty String. */
    public String getDate() {
        return date;
    }

    /**
     * @param date
     *            Format: "DD/MM/YYYY" or empty String.
     */
    public void setDate(String date) {
        assert date.matches(DATE_PATTERN);
        this.date = date;
    }

    /** Resets date attribute to empty String. */
    public void resetDate() {
        date = "";
    }

    /** @return Time, format: "HHMM" or empty String. */
    public String getTime() {
        return time;
    }

    /**
     * @param time
     *            Format: "HHMM" or empty String.
     */
    public void setTime(String time) {
        time.matches(TIME_PATTERN);
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

    /**
     * Regex for valid date values and format: "DD/MM/YYYY HHMM"
     * 
     * @return Regex for valid date values and format: "DD/MM/YYYY HHMM"
     */
    public static String getDatePattern() {
        return DATE_PATTERN;
    }

    /**
     * Regex for valid time values and format: "DD/MM/YYYY HHMM"
     * 
     * @return Regex for valid time values and format: "DD/MM/YYYY HHMM"
     */
    public static String getTimePattern() {
        return TIME_PATTERN;
    }

    /**
     * Returns regex for valid date and time values and format:
     * "DD/MM/YYYY HHMM"
     * 
     * @return Regex for valid date and time values and format:
     *         "DD/MM/YYYY HHMM"
     */
    public static String getDateTimePattern() {
        return DATE_TIME_PATTERN;
    }
}