package parser;

import java.util.Arrays;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import database.DateTime;

public class DateParser {

    private static final int YEAR_MINIMUM = 1819;

    private static final String TYPE_TIME_DATE = "time-date";
    private static final String TYPE_DATE_TIME = "date-time";
    private static final String TYPE_TIME_ONLY = "time-only";
    private static final String TYPE_DATE_ONLY = "date-only";
    private static final String TYPE_DATE_TODAY = "today";
    private static final String TYPE_DATE_TMR = "tomorrow";

    /**
     * The date/time format DateParser will use.
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
            "dd/MM/yyyy HHmm");

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

    public static String getTmrDateStr() {
        String today = getCurrDateStr();
        String[] dateFields = today.split("/");

        int day = Integer.parseInt(dateFields[0]);
        int tmr = day + 1;
        int month = Integer.parseInt(dateFields[1]);
        int year = Integer.parseInt(dateFields[2]);

        if (!isValidDay(tmr, month, year)) {
            tmr = 1;
            month++;
            if (!isValidMonth(month)) {
                month = 1;
                year++;
            }
        }

        dateFields = new String[] { toDoubleDigitStr(tmr),
                                   toDoubleDigitStr(month),
                                   Integer.toString(year) };
        String date = dateFields[0] + "/" + dateFields[1] + "/" + dateFields[2];
        return date;
    }

    private static String toDoubleDigitStr(int num) {
        if (num < 10) {
            return "0" + Integer.toString(num);
        } else {
            return Integer.toString(num);
        }
    }

    private static String toFourDigitStr(int num) {
        assert (num >= YEAR_MINIMUM || (num >= 0 && num < 100));

        if (num < 100) {
            return "20" + Integer.toString(num);
        } else {
            return Integer.toString(num);
        }
    }

    private static String getCurrYearStr() {
        String currDate = getCurrDateStr();
        String[] dateFields = currDate.split("/");
        assert (dateFields.length == 3);
        String year = dateFields[2];
        return year;
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

        String date = "";
        String time = "";
        String[] dateFields;
        switch (getDateType(str)) {
            case TYPE_DATE_ONLY:
                date = formatDate(str);
                break;

            case TYPE_TIME_ONLY:
                date = getCurrDateStr();
                time = str;
                break;

            case TYPE_DATE_TIME:
                dateFields = str.split(" ");
                assert (dateFields.length == 2);
                date = formatDate(dateFields[0]);
                time = dateFields[1];
                break;

            case TYPE_TIME_DATE:
                dateFields = str.split(" ");
                assert (dateFields.length == 2);
                date = formatDate(dateFields[1]);
                time = dateFields[0];
                break;
        }

        if (date.isEmpty() && time.isEmpty()) {
            return new DateTime();
        } else {
            return new DateTime(date, time);
        }
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
    private static String getDateType(String str) {
        assert (isValidDateTime(str)) : "Invalid DateTime for getDateType()!";
        String[] dateFields = str.trim().split(" ");
    
        // TODO: Magic Strings
        if (isSingleItemArray(dateFields)) {
            if (firstItemIsDate(dateFields)) {
                return TYPE_DATE_ONLY;
            } else {
                return TYPE_TIME_ONLY;
            }
        } else {
            if (firstItemIsDate(dateFields)) {
                return TYPE_DATE_TIME;
            } else {
                return TYPE_TIME_DATE;
            }
        }
    }

    private static String formatDate(String str) {
        String result = str;
        if (isValidWordDate(str)) {
            switch (str.toLowerCase()) {
                case TYPE_DATE_TODAY:
                    result = getCurrDateStr();
                    break;

                case TYPE_DATE_TMR:
                    result = getTmrDateStr();
                    break;
            }
        } else if (isValidNumericalDate(str)) {
            if (isMissingYear(str)) {
                result = result + "/" + getCurrYearStr();
            }
            String[] dateFieldsStr = new String[3];
            int[] dateFieldsInt = splitToDateToIntArray(result);
            dateFieldsStr[0] = toDoubleDigitStr(dateFieldsInt[0]);
            dateFieldsStr[1] = toDoubleDigitStr(dateFieldsInt[1]);
            dateFieldsStr[2] = toFourDigitStr(dateFieldsInt[2]);
            result = dateFieldsStr[0] + "/" + dateFieldsStr[1] + "/" +
                     dateFieldsStr[2];
        }

        return result;
    }

    private static boolean isMissingYear(String str) {
        String[] dateFields = str.split("/");
        assert (dateFields.length == 2 || dateFields.length == 3);
        return dateFields.length == 2;
    }

    private static int[] splitToDateToIntArray(String str) {
        assert (isValidNumericalDate(str));
        String[] strArr = splitDateToStrArray(str);
        int[] intArr = new int[] { Integer.parseInt(strArr[0]),
                                  Integer.parseInt(strArr[1]),
                                  Integer.parseInt(strArr[2]) };
        return intArr;
    }

    private static String[] splitDateToStrArray(String str) {
        return str.split("/");
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
    public static boolean isValidTime(String timeStr) {
        try {
            String hoursStr = timeStr.substring(0, 2);
            String minStr = timeStr.substring(2, 4);

            int hoursInt = Integer.parseInt(hoursStr);
            int minInt = Integer.parseInt(minStr);

            boolean isValidHH = (hoursStr.equals("00") || hoursInt > 0) &&
                                hoursInt < 24;
            boolean isValidMM = (minStr.equals("00") || minInt > 0) &&
                                minInt < 60;

            return isValidHH && isValidMM;
        } catch (IndexOutOfBoundsException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the input <code>String</code> is in the accepted date format
     * <code>dd/MM/yyyy</code>.
     */
    public static boolean isValidDate(String str) {
        return isValidWordDate(str) || isValidNumericalDate(str);
    }

    private static boolean isValidWordDate(String str) {
        String[] dateWords = { "today", "tomorrow" };
        return Arrays.asList(dateWords).contains(str.toLowerCase());
    }

    /**
     * @param str
     * @return
     */
    private static boolean isValidNumericalDate(String str) {
        boolean result;

        try {
            String[] components = str.split("/");
            String day = components[0];
            String month = components[1];
            String year; // Needed to calculate leap years

            boolean hasValidCompLengths = false;
            boolean hasValidIntComp = false;

            if (components.length == 2) {
                year = getCurrYearStr();
                hasValidCompLengths = (day.length() >= 1 && day.length() <= 2) &&
                                      (month.length() >= 1 && month.length() <= 2);
                hasValidIntComp = isValidMonth(month) &&
                                  isValidDay(day, month, year);
            } else if (components.length == 3) {
                year = components[2];
                hasValidCompLengths = (day.length() >= 1 && day.length() <= 2) &&
                                      (month.length() >= 1 && month.length() <= 2) &&
                                      (year.length() == 2 || year.length() == 4);
                hasValidIntComp = isValidYear(year) && isValidMonth(month) &&
                                  isValidDay(day, month, year);
            }

            result = hasValidCompLengths && hasValidIntComp;
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    /**
     * Checks if the input date values can form a valid day.
     * <p>
     * Converts input <code>String</code> values to <code>int</code> values and
     * calls the overloaded method {@link #isValidDay(int, int, int)}.
     */
    private static boolean isValidDay(String day, String month, String year) {
        try {
            int dayNum = Integer.parseInt(day);
            int monthNum = Integer.parseInt(month);
            int yearNum = Integer.parseInt(year);
            return isValidDay(dayNum, monthNum, yearNum);
        } catch (Exception e) {
            return false;
        }
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
        assert isValidYear(Integer.toString(year));

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
        try {
            int monthNum = Integer.parseInt(monthStr);
            return isValidMonth(monthNum);
        } catch (Exception e) {
            return false;
        }
    }

    // TODO
    private static boolean isValidMonth(int month) {
        return month > 0 && month <= 12;
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
        try {
            int yearNum = Integer.parseInt(yearStr);
            // TODO: Magic strings in year, month, day
            if (yearStr.length() == 4) {
                return yearNum >= YEAR_MINIMUM;
            } else if (yearStr.length() == 2) {
                return (yearNum >= 0 && yearNum < 100);
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**

     */
    public static boolean containsDate(String str) {
        String[] strFields = str.split(" ");

        for (int i = 0; i < strFields.length; i++) {
            if (isValidDate(strFields[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @param str
     * @return
     */
    public static String getFirstDate(String str) {
        assert containsDate(str) : "this method should be called only after checking if there's a date";
        String[] strFields = str.split(" ");

        for (int i = 0; i < strFields.length; i++) {
            if (isValidDate(strFields[i])) {
                return formatDate(strFields[i]);
            }
        }

        return null;
    }

    /**

     */
    public static boolean containsTime(String str) {
        String[] strFields = str.split(" ");

        for (int i = 0; i < strFields.length; i++) {
            if (isValidTime(strFields[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @param str
     * @return
     */
    public static String getFirstTime(String str) {
        String[] strFields = str.split(" ");

        for (int i = 0; i < strFields.length; i++) {
            if (isValidTime(strFields[i])) {
                return formatDate(strFields[i]);
            }
        }

        return null;
    }
}
