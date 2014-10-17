package Storage;

/**
 * The DateTime class encapsulates date and time attributes in a single package.
 * Date and time attributes are managed as Strings. Date String is internally
 * split into day, month, and year integer attributes for easier access.
 * 
 * Date and time formats, respectively: "DD/MM/YYYY" and "HHMM".
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class DateTime implements Comparable<DateTime> {

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

    /** Automatically parsed from date value. */
    private int day = 0;

    /** Automatically parsed from date value. */
    private int month = 0;

    /** Automatically parsed from date value. */
    private int year = 0;

    /** Time format: "HHMM" */
    private String time = "";

    /**
     * Default constructor. Attributes are initialized as empty Strings and
     * zeroed integers.
     */
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
        if (!date.isEmpty()) {
            parseAndSplitDate(date);
        }
    }

    /**
     * Parses and splits date string for storing in day, month, and year integer
     * attributes.
     * 
     * @param date
     *            Is non-empty String, format: "DD/MM/YYYY".
     */
    private void parseAndSplitDate(String date) {
        assert date.matches(DATE_PATTERN);
        assert !date.isEmpty();
        day = Integer.parseInt(date.substring(0, 2));
        month = Integer.parseInt(date.substring(3, 5));
        year = Integer.parseInt(date.substring(6));
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
        this.day = dateTime.day;
        this.month = dateTime.month;
        this.year = dateTime.year;
        this.time = dateTime.time;
    }

    /**
     * Compares this <code>DateTime</code> with an input <code>DateTime</code><br>
     * Returns 1 if it is later (larger) than the other <code>DateTime</code>, 0
     * if they are the same, or -1 if it is earlier (smaller).
     * 
     * @return <ul>
     *         <li>1 if this <code>DateTime</code> is later than the input
     *         <code>DateTime</code>,
     *         <li>0 if they are the same date and time,
     *         <li>-1 if this <DateTime> is earlier.
     *         </ul>
     * 
     * @author Yeo Zi Xian, Justin
     */
    @Override
    public int compareTo(DateTime otherDateTime) {
        int day1 = this.day;
        int mth1 = this.month;
        int yr1 = this.year;
        int time1 = Integer.parseInt(this.time);
        int day2 = otherDateTime.day;
        int mth2 = otherDateTime.month;
        int yr2 = otherDateTime.year;
        int time2 = Integer.parseInt(otherDateTime.time);

        // Check year for differences
        if (yr1 < yr2) {
            return -1;
        } else if (yr1 > yr2) {
            return 1;
        }

        // Then check month for differences
        if (mth1 < mth2) {
            return -1;
        } else if (mth1 > mth2) {
            return 1;
        }

        // Then check day for differences
        if (day1 < day2) {
            return -1;
        } else if (day1 > day2) {
            return 1;
        }

        // Then check time for differences
        if (time1 < time2) {
            return -1;
        } else if (time1 > time2) {
            return 1;
        }

        // No differences detected
        return 0;
    }

    public boolean equalsTo(DateTime otherDateTime) {
        return (compareTo(otherDateTime) == 0);
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
        if (date.isEmpty() && time.isEmpty()) {
            return "";
        } else if (date.isEmpty()) {
            return time;
        } else if (time.isEmpty()) {
            return date;
        } else {
            return date + " " + time;
        }
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
        if (date.isEmpty()) {
            resetDate();
        } else {
            this.date = date;
            parseAndSplitDate(date);
        }
    }

    /** Resets date attribute to empty String. */
    public void resetDate() {
        date = "";
        resetDay();
        resetMonth();
        resetYear();
    }

    /**
     * Gets day value parsed from date value.
     * 
     * @return Day value in integer type.
     */
    public int getDay() {
        return day;
    }

    /** Zeroes day attribute. */
    private void resetDay() {
        day = 0;
    }

    /**
     * Gets month value parsed from date value.
     * 
     * @return Month value in integer type.
     */
    public int getMonth() {
        return month;
    }

    /** Zeroes month attribute. */
    private void resetMonth() {
        month = 0;
    }

    /**
     * Gets year value parsed from date value.
     * 
     * @return Year value in integer type.
     */
    public int getYear() {
        return year;
    }

    /** Zeroes year attribute. */
    private void resetYear() {
        year = 0;
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