package Parser;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import Storage.DateTime;

// TODO: Reorganise: move all aux (e.g. isInteger()) to Parser, move all specifics out?

public class DateParser {

    private static final int TYPE_TIME_DATE = 4;
    private static final int TYPE_DATE_TIME = 3;
    private static final int TYPE_TIME_ONLY = 2;
    private static final int TYPE_DATE_ONLY = 1;
    /**
     * The date/time format DateParser will use.
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
            "dd/MM/yyyy HHmm");

    // TODO: For testing; to remove
    protected static void printDate() {
        Calendar cal = Calendar.getInstance();
        System.out.println(DATE_FORMAT.format(cal.getTime()));
    }

    /**
     * Returns the current date and time in a <code>DateTime</code> object.
     * <p>
     * <i>Uses the default system settings for date and time.</i>
     * 
     * @return <code>DateTime</code> object containing date in
     *         <code>dd/MM/yyyy</code> and time in <code>HHmm</code>
     */
    public static DateTime getCurrDateTime() {
        Calendar cal = Calendar.getInstance();
        String[] date = DATE_FORMAT.format(cal.getTime()).split(" ");
        return new DateTime(date[0], date[1]);
    }

    /**
     * Returns the current date and time in a <code>String</code>.
     * <p>
     * <i>Uses the default system settings for date and time.</i>
     * 
     * @return <code>String</code> object containing date in
     *         <code>dd/MM/yyyy</code> and time in <code>HHmm</code>
     */
    public static String getCurrDateTimeStr() {
        return getCurrDateTime().toString();
    }

    /**
     * Returns the current date in a <code>String</code> object.
     * <p>
     * <i>Uses the default system settings for date.</i>
     * 
     * @return <code>String</code> object containing date in
     *         <code>dd/MM/yyyy</code>
     */
    public static String getCurrDateStr() {
        String currDate = getCurrDateTimeStr();
        String[] dateFields = currDate.split(" ");
        return dateFields[0];
    }

    /**
     * Returns the current time in a <code>String</code> object.
     * <p>
     * <i>Uses the default system settings for time.</i>
     * 
     * @return <code>String</code> object containing time in <code>HHmm</code>
     */
    public static String getCurrTimeStr() {
        String currDate = getCurrDateTimeStr();
        String[] dateFields = currDate.split(" ");
        return dateFields[1];
    }

    /**
     * Checks if the input <code>String</code> can be parsed into a
     * <code>DateTime</code> object. The date/time must be in one of four
     * formats (below).
     * <ol>
     * Formats accepted:
     * <li>Date only: dd/MM/yyyy
     * <li>Time only: HHmm
     * <li>Date and Time: dd/MM/yyyy HHmm
     * <li>Time and Date: HHmm dd/MM/yyyy
     * </ol>
     */
    public static boolean isValidDateTime(String str) {
        String[] strFields = str.split(" ");

        boolean validNumOfTerms = strFields.length > 0 && strFields.length <= 2;
        boolean containsDate = false;
        boolean containsTime = false;
        boolean containsMultipleDates = false;
        boolean containsMultipleTimes = false;
        boolean containsNonDateTime = false;

        for (int i = 0; i < strFields.length; i++) {
            if (isValidDate(strFields[i])) {
                if (!containsDate) {
                    containsDate = true;
                } else {
                    containsMultipleDates = true;
                }
            } else if (isValidTime(strFields[i])) {
                if (!containsTime) {
                    containsTime = true;
                } else {
                    containsMultipleTimes = true;
                }
            } else {
                containsNonDateTime = true;
            }
        }

        return validNumOfTerms && (containsDate || containsTime) &&
               !(containsMultipleDates || containsMultipleTimes) &&
               !containsNonDateTime;
    }

    /**
     * Checks and then parses the input <code>String</code> into a
     * <code>DateTime</code> object. The date/time must be in one of four
     * formats (below). An <code>IllegalArgumentException</code> will be thrown
     * if the input is not one of the following formats, or if the date/time
     * values are invalid (e.g. 40/01/2014 2401).
     * <ol>
     * Formats accepted:
     * <li>Date only: dd/MM/yyyy
     * <li>Time only: HHmm
     * <li>Date and Time: dd/MM/yyyy HHmm
     * <li>Time and Date: HHmm dd/MM/yyyy
     * </ol>
     * 
     * @return <code>DateTime</code> object containing date in
     *         <code>dd/MM/yyyy</code> and time in <code>HHmm</code>
     */
    public static DateTime parseToDateTime(String str) {
        if (str == null || str.isEmpty()) {
            return new DateTime();
        }

        if (!isValidDateTime(str)) {
            throw new IllegalArgumentException("Invalid input for parseToDate");
        }

        switch (getDateType(str)) {
            case TYPE_DATE_ONLY:
                return new DateTime(str, "");

            case TYPE_TIME_ONLY:
                return new DateTime(getCurrDateStr(), str);

            case TYPE_DATE_TIME:
                String[] dateFields1 = str.split(" ");
                assert (dateFields1.length == 2);
                return new DateTime(dateFields1[0], dateFields1[1]);

            case TYPE_TIME_DATE:
                String[] dateFields2 = str.split(" ");
                assert (dateFields2.length == 2);
                return new DateTime(dateFields2[1], dateFields2[0]);
        }

        // Code should not reach this point
        assert false : "parseToDateTime() failed to catch invalid date type";
        return null;
    }

    /**
     * Gets the date format of the input <code>String</code>. It is assumed that
     * the input has been checked and the <code>String</code> contains a valid
     * date and/or time.
     * <p>
     * Returns an <code>int</code> based on which format in the following list
     * the input corresponds to:
     * <ol>
     * <li>dd/MM/yyyy
     * <li>HHmm
     * <li>dd/MM/yyyy HHmm
     * <li>HHmm dd/MM/yyyy
     * </ol>
     * 
     * @return An integer from 1 to 4 based on which format the input
     *         corresponds to (in the above list).
     */
    private static int getDateType(String str) {
        assert (isValidDateTime(str)) : "Invalid DateTime for getDateType()!";
        String[] dateFields = str.split(" ");

        // TODO: Magic Strings
        if (isSingleItemArray(dateFields)) {
            if (firstItemIsDate(dateFields)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            if (firstItemIsDate(dateFields)) {
                return 3;
            } else {
                return 4;
            }
        }
    }

    /**
     * Checks if the input array contains only 1 item.
     */
    private static <E> boolean isSingleItemArray(E[] array) {
        return array.length == 1;
    }

    /**
     * Checks if the first item of the array is a valid date.
     */
    private static boolean firstItemIsDate(String[] dateFields) {
        assert (dateFields.length > 0) : "Empty array input for firstItemIsDate()";
        return isValidDate(dateFields[0]);
    }

    /**
     * Checks if the input <code>String</code> is in the accepted date format
     * <code>dd/MM/yyyy</code>.
     */
    public static boolean isValidDate(String str) {
        boolean hasTwoSlashes;
        boolean hasValidCompLengths;
        boolean hasIntComponents;
        boolean hasValidIntComp;

        try {
            String[] components = str.split("/");
            String day = components[0];
            String month = components[1];
            String year = components[2];
            hasTwoSlashes = (components.length == 3);
            hasValidCompLengths = (day.length() == 2) &&
                                  (month.length() == 2) && (year.length() == 4);
            hasIntComponents = Parser.isInteger(day) &&
                               Parser.isInteger(month) &&
                               Parser.isInteger(year);
            hasValidIntComp = isValidYear(year) && isValidMonth(month) &&
                              isValidDay(day, month, year);
        } catch (Exception e) {
            return false;
        }

        return hasTwoSlashes && hasValidCompLengths && hasIntComponents &&
               hasValidIntComp;
    }

    /**
     * Checks if the input date values can form a valid day.
     * <p>
     * Converts input <code>String</code> values to <code>int</code> values and
     * calls the overloaded method {@link #isValidDay(int, int, int)}.
     */
    private static boolean isValidDay(String day, String month, String year) {
        int dayNum = Integer.parseInt(day);
        int monthNum = Integer.parseInt(month);
        int yearNum = Integer.parseInt(year);
        return isValidDay(dayNum, monthNum, yearNum);
    }

    /**
     * Checks if the input <code>day</code> is valid, by checking if it falls
     * within the <code>month</code>'s number of days.
     * <p>
     * The input <code>year</code> is required to determine the number of days
     * in February (leap years).
     * 
     * @param day
     * @param month
     * @param year
     * @return <code>true</code> if the day is valid, <br>
     *         <code>false</code> otherwise.
     */
    private static boolean isValidDay(int day, int month, int year) {
        boolean isLeapYear = isLeapYear(year);

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return day > 0 && day <= 31;

            case 2:
                if (isLeapYear) {
                    return day > 0 && day <= 29;
                } else {
                    return day > 0 && day <= 28;
                }

            case 4:
            case 6:
            case 9:
            case 11:
                return day > 0 && day <= 30;
        }
        return false;
    }

    /**
     * Checks if the input <code>year</code> is a leap year. <br>
     * A leap year is a year that is divisible by 400, or divisible by 4 but not
     * by 100.
     * <p>
     * <i> Note: This formula started proper from 8 AD, and does not apply to
     * BC. </i>
     */
    private static boolean isLeapYear(int year) {
        assert (year >= 8) : "Invalid year! Our formula only works for after 8 AD";
        return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
    }

    /**
     * Checks if the input <code>String</code> can be parsed to a valid month.
     * 
     * <p>
     * <i> The input should be an integer String, and a valid month is between 1
     * and 12 inclusive. </i>
     * 
     * @param monthStr
     *            A <code>String</code> containing only an integer.
     */
    private static boolean isValidMonth(String monthStr) {
        int monthNum = Integer.parseInt(monthStr);
        return monthNum > 0 && monthNum <= 12;
    }

    /**
     * Checks if the input <code>String</code> is a valid year.
     * 
     * <p>
     * <i> The input should be an integer String, and a valid month is taken to
     * be after 1819 (arbitrary value). </i>
     * 
     * @param yearStr
     *            A <code>String</code> containing only an integer.
     */
    private static boolean isValidYear(String yearStr) {
        int yearNum = Integer.parseInt(yearStr);
        // TODO: Magic strings in year, month, day
        return yearNum >= 1819;
    }

    /**
     * Checks if the input <code>String</code> is a valid time. This method
     * assumes the 24HR time format, i.e. 0000-2359.
     * 
     * <p>
     * <i> The input should be an integer String. </i>
     * 
     * @param timeStr
     *            A <code>String</code> containing only an integer of the format
     *            <code>HHmm</code>
     */
    private static boolean isValidTime(String timeStr) {
        try {
            String hoursStr = timeStr.substring(0, 2);
            String minStr = timeStr.substring(2, 4);

            int hoursInt = Integer.parseInt(hoursStr);
            int minInt = Integer.parseInt(minStr);

            boolean isValidHH = hoursInt >= 0 && hoursInt < 24;
            boolean isValidMM = minInt >= 0 && minInt < 60;

            return isValidHH && isValidMM;
        } catch (IndexOutOfBoundsException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String args[]) {
        // TODO: extract to test case
        System.out.println("//LEAPYEAR:");
        System.out.println("2000: " + isLeapYear(2000));
        System.out.println("2001: " + isLeapYear(2001));
        System.out.println("2004: " + isLeapYear(2004));
        System.out.println("2100: " + isLeapYear(2100));
        System.out.println("2200: " + isLeapYear(2200));
        System.out.println("2300: " + isLeapYear(2300));
        System.out.println("2304: " + isLeapYear(2304));
        System.out.println("2400: " + isLeapYear(2400));
        // Invalid: System.out.println("-400: " + isLeapYear(-400));

        System.out.println("//TIME:");
        System.out.println("0000: " + isValidTime("0000"));
        System.out.println("0001: " + isValidTime("0001"));
        System.out.println("0100: " + isValidTime("0100"));
        System.out.println("0060: " + isValidTime("0060"));
        System.out.println("2400: " + isValidTime("2400"));
        System.out.println("2359: " + isValidTime("2359"));
        System.out.println("9999: " + isValidTime("9999"));
        System.out.println("abc: " + isValidTime("abc"));

        System.out.println("//Getters:");
        System.out.println("DateTime: " + getCurrDateTime());
        System.out.println("DateTimeStr: " + getCurrDateTimeStr());
        System.out.println("Date: " + getCurrDateStr());
        System.out.println("Time: " + getCurrTimeStr());

        System.out.println("//DateTimeValid:");
        System.out.println("23/04/2014 " + isValidDateTime("23/04/2014"));
        System.out.println("2359 " + isValidDateTime("2359"));
        System.out.println("23/04/2014 2200 " +
                           isValidDateTime("23/04/2014 2200"));
        System.out.println("2200 23/04/2014 " +
                           isValidDateTime("2200 23/04/2014"));
        System.out.println("2200 29/02/2014 " +
                           isValidDateTime("2200 29/02/2014"));
        System.out.println("2200 29/02/2012 " +
                           isValidDateTime("2200 29/02/2012"));
        System.out.println("23/04/2014 2400 " +
                           isValidDateTime("23/04/2014 2400"));
        System.out.println("23/13/2014 2200 " +
                           isValidDateTime("23/13/2014 2200"));
        System.out.println("23/04/2014 asdd " +
                           isValidDateTime("23/04/2014 asdd"));
        System.out.println("aaaaaaaaaa 2200 " +
                           isValidDateTime("aaaaaaaaaa 2200"));
        System.out.println("aaaaaaaaaa aaaa " +
                           isValidDateTime("aaaaaaaaaa aaaa"));

        System.out.println("//parseToDate:");
        System.out.println("23/04/2014: " + parseToDateTime("23/04/2014"));
        System.out.println("2200: " + parseToDateTime("2200"));
        System.out.println("23/04/2014 2200: " +
                           parseToDateTime("23/04/2014 2200"));
        System.out.println("2200 23/04/2014: " +
                           parseToDateTime("2200 23/04/2014"));
        // TODO: test exception
        // System.out.println("exception: " + parseToDate("aaa"));

        DateTime dt = new DateTime("23/04/2014", "2300");
        DateTime dt2 = new DateTime("24/04/2014", "2300");
        System.out.println("dt<dt2: " + dt.compareTo(dt2));

        dt = new DateTime("23/04/2014", "2300");
        dt2 = new DateTime("23/04/2014", "2300");
        System.out.println("dt=dt2: " + dt.compareTo(dt2));

        dt = new DateTime("23/04/2014", "2300");
        dt2 = new DateTime("22/04/2014", "2300");
        System.out.println("dt>dt2: " + dt.compareTo(dt2));

        dt = new DateTime("23/04/2014", "0000");
        dt2 = new DateTime("23/04/2014", "2359");
        System.out.println("dt<dt2: " + dt.compareTo(dt2));
    }
}
