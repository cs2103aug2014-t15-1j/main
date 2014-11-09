package parser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import database.DateTime;

public class DateParser {

    private static final String TYPE_TIME_NOW = "now";

    private static final int LENGTH_DATE_LONG = 3;

    private static final int LENGTH_DATE_SHORT = 2;

    /** <i>Everything began with 1819...</i> */
    private static final int YEAR_MINIMUM = 1819;

    private static final String TYPE_TIME_DATE = "time-date";
    private static final String TYPE_DATE_TIME = "date-time";
    private static final String TYPE_TIME_ONLY = "time-only";
    private static final String TYPE_DATE_ONLY = "date-only";
    private static final String TYPE_DATE_TODAY = "today";
    private static final String TYPE_DATE_TOMORROW = "tomorrow";
    private static final String TYPE_DATE_TMR = "tmr";

    private static final String[] LIST_DATE_WORDS = { TYPE_DATE_TODAY,
                                                     TYPE_DATE_TOMORROW,
                                                     TYPE_DATE_TMR };

    /**
     * The date/time format DateParser will use.
     */
    private static final DateFormat FORMAT_DATE_TIME = new SimpleDateFormat(
            "dd/MM/yyyy HHmm");

    /** See {@link Parser#getCurrDateTime()}. */
    static DateTime getCurrDateTime() {
        Calendar cal = Calendar.getInstance();
        String[] date = FORMAT_DATE_TIME.format(cal.getTime()).split(" ");
        return new DateTime(date[0], date[1]);
    }

    /** See {@link Parser#getCurrDateTimeStr()}. */
    static String getCurrDateTimeStr() {
        return getCurrDateTime().toString();
    }

    /** See {@link Parser#getCurrTimeStr()}. */
    static String getCurrTimeStr() {
        String currDate = getCurrDateTimeStr();
        String[] dateFields = currDate.split(" ");
        return dateFields[1];
    }

    /** See {@link Parser#getCurrDateStr()}. */
    static String getCurrDateStr() {
        String currDate = getCurrDateTimeStr();
        String[] dateFields = currDate.split(" ");
        return dateFields[0];
    }

    /** See {@link Parser#getTmrDateStr()}. */
    static String getTmrDateStr() {
        String today = getCurrDateStr();
        return getNextDayStr(today);
    }

    /** See {@link Parser#getDateFromNowStr(int)}. */
    static String getDateFromNowStr(int numDaysLater) {
        String date = getCurrDateStr();
        for (int i = 0; i < numDaysLater; i++) {
            date = getNextDayStr(date);
        }
        return date;
    }

    /**
     * Returns the date one day later than the input date. Input date must be of
     * the correct <code>String</code> format.
     * 
     * @param currDay
     *            Date in "dd/MM/yyyy" format
     * @return <code>String</code> object containing date in
     *         <code>dd/MM/yyyy</code>
     */
    private static String getNextDayStr(String currDay) {
        assert isValidDate(currDay) : "input <" + currDay + "> is invalid";
        String[] dateFields = currDay.split("/");

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

    /**
     * Converts an <code>int</code> to a two-character <code>String</code>, by
     * adding leading zeroes if necessary.
     */
    private static String toDoubleDigitStr(int num) {
        if (num < 10) {
            return "0" + Integer.toString(num);
        } else {
            return Integer.toString(num);
        }
    }

    /**
     * Converts an <code>int</code> to a four-character <code>String</code>, by
     * adding "20" in front if necessary.
     */
    private static String toFourDigitStr(int num) {
        assert (num >= YEAR_MINIMUM || (num >= 0 && num < 100));

        if (num < 100) {
            return "20" + Integer.toString(num);
        } else {
            return Integer.toString(num);
        }
    }

    /** Returns the current year in a four-digit String. */
    private static String getCurrYearStr() {
        String currDate = getCurrDateStr();
        String[] dateFields = currDate.split("/");
        assert (dateFields.length == 3);
        String year = dateFields[2];
        return year;
    }

    /** See {@link Parser#isValidDateTime(String)}. */
    static boolean isValidDateTime(String str) {
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

    /** See {@link Parser#parseToDateTime(String)}. */
    static DateTime parseToDateTime(String str) {
        if (str == null || str.isEmpty()) {
            return new DateTime();
        }

        if (!isValidDateTime(str)) {
            throw new IllegalArgumentException(
                    "Invalid input for parseToDateTime");
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
                time = formatTime(str);
                break;

            case TYPE_DATE_TIME:
                dateFields = str.split(" ");
                assert (dateFields.length == 2);
                date = formatDate(dateFields[0]);
                time = formatTime(dateFields[1]);
                break;

            case TYPE_TIME_DATE:
                dateFields = str.split(" ");
                assert (dateFields.length == 2);
                date = formatDate(dateFields[1]);
                time = formatTime(dateFields[0]);
                break;

            default:
                return new DateTime();
        }

        return new DateTime(date, time);

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

    /**
     * Converts an input date, which may be in word or short form, to the
     * program-wide standard of "dd/MM/yyyy".
     */
    private static String formatDate(String str) {
        String result = str;
        if (isValidWordDate(str)) {
            switch (str.toLowerCase()) {
                case TYPE_DATE_TODAY:
                    result = getCurrDateStr();
                    break;

                case TYPE_DATE_TOMORROW:
                case TYPE_DATE_TMR:
                    result = getTmrDateStr();
                    break;
            }
        } else if (isValidNumericalDate(str)) {
            if (isMissingYear(str)) {
                result = result + "/" + getCurrYearStr();
            }
            String[] dateFieldsStr = new String[3];
            int[] dateFieldsInt = splitDateToIntArray(result);
            dateFieldsStr[0] = toDoubleDigitStr(dateFieldsInt[0]);
            dateFieldsStr[1] = toDoubleDigitStr(dateFieldsInt[1]);
            dateFieldsStr[2] = toFourDigitStr(dateFieldsInt[2]);
            result = dateFieldsStr[0] + "/" + dateFieldsStr[1] + "/" +
                     dateFieldsStr[2];
        }

        return result;
    }
    
    /**
     * Converts an input time, which may be "now" or 24Hr time, to 24Hr time.
     */
    private static String formatTime(String str) {
        String result = str;
        if (isValidWordTime(str)) {
            result = getCurrTimeStr();
        }
        return result;
    }

    /**
     * Returns true if the input str is of the format d/M, where d can be
     * extended to dd and M can be MM.
     */
    private static boolean isMissingYear(String str) {
        String[] dateFields = str.split("/");
        assert (dateFields.length == LENGTH_DATE_SHORT || dateFields.length == LENGTH_DATE_LONG);
        return dateFields.length == LENGTH_DATE_SHORT;
    }

    /** Splits a date from the format of dd/MM/yyyy to an int array of size 3. */
    private static int[] splitDateToIntArray(String str) {
        assert (isValidNumericalDate(str));
        assert (!isMissingYear(str));
        String[] strArr = splitDateToStrArray(str);
        int[] intArr = new int[] { Integer.parseInt(strArr[0]),
                                  Integer.parseInt(strArr[1]),
                                  Integer.parseInt(strArr[2]) };
        return intArr;
    }

    /**
     * Splits a date from the format of "dd/MM/yyyy" to a String array {dd, MM,
     * yyyy}.
     */
    private static String[] splitDateToStrArray(String str) {
        return str.split("/");
    }

    /** Checks if the input array contains only 1 item. */
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

    /** See {@link Parser#isValidTime(String)}. */
    static boolean isValidTime(String timeStr) {
        return isValidWordTime(timeStr) || isValidNumericalTime(timeStr);
    }

    /**
     * Checks if the input <code>timeStr</code> is a valid word-based time. This
     * method currently only accepts "now" as a valid word-based time.
     */
    private static boolean isValidWordTime(String timeStr) {
        return timeStr.equalsIgnoreCase(TYPE_TIME_NOW);
    }

    /**
     * Checks if the input <code>timeStr</code> is a valid numerical time. This
     * method assumes the 24HR time format, i.e. 0000-2359.
     */
    private static boolean isValidNumericalTime(String timeStr) {
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

    /** See {@link Parser#isValidDate(String)}. */
    static boolean isValidDate(String str) {
        return isValidWordDate(str) || isValidNumericalDate(str);
    }

    /**
     * Checks if <code>str</code> is one of the words that are accepted as
     * substitutes for dates.
     */
    private static boolean isValidWordDate(String str) {
        return Arrays.asList(LIST_DATE_WORDS).contains(str.toLowerCase());
    }

    /**
     * Checks if the input <code>str</code> is a valid numerical date. The
     * allowed formats include day/month, as well as day/month/year formats,
     * where leading zeroes can be ignored and year can be truncated.
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
                hasValidCompLengths = lengthIsBetweenInc(1, 2, day) &&
                                      lengthIsBetweenInc(1, 2, month);
                hasValidIntComp = isValidMonth(month) &&
                                  isValidDay(day, month, year);
            } else if (components.length == 3) {
                year = components[2];
                hasValidCompLengths = lengthIsBetweenInc(1, 2, day) &&
                                      lengthIsBetweenInc(1, 2, month) &&
                                      lengthIsBetweenInc(2, 4, year) &&
                                      year.length() != 3;
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
     * Checks if the length of <code>str</code> is between
     * <code>rangeStartInc</code> and <code>rangeEndInc</code>, inclusive.
     */
    private static boolean lengthIsBetweenInc(int rangeStartInc,
                                              int rangeEndInc, String str) {
        return str.length() >= rangeStartInc && str.length() <= rangeEndInc;
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

    /** Checks if the input <code>int</code> is a valid calendar month number. */
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

    /** Returns true if <code>str</code> contains a valid date. */
    static boolean containsDate(String str) {
        String[] strFields = str.split(" ");

        for (int i = 0; i < strFields.length; i++) {
            if (isValidDate(strFields[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the first valid date String found in <code>str</code>. Should
     * only be called if <code>str</code> {@link #containsDate(String) contains
     * a Date}.
     */
    static String getFirstDate(String str) {
        assert containsDate(str) : "this method should be called only after checking if there's a date";
        String[] strFields = str.split(" ");

        for (int i = 0; i < strFields.length; i++) {
            if (isValidDate(strFields[i])) {
                return formatDate(strFields[i]);
            }
        }

        return null;
    }

    /** Returns true if <code>str</code> contains a valid time. */
    static boolean containsTime(String str) {
        String[] strFields = str.split(" ");

        for (int i = 0; i < strFields.length; i++) {
            if (isValidTime(strFields[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the first valid time String found in <code>str</code>. Should
     * only be called if <code>str</code> {@link #containsTime(String) contains
     * a Time}.
     */
    static String getFirstTime(String str) {
        assert containsTime(str) : "this method should be called only after checking if there's a time";
        String[] strFields = str.split(" ");

        for (int i = 0; i < strFields.length; i++) {
            if (isValidTime(strFields[i])) {
                return formatTime(strFields[i]);
            }
        }

        return null;
    }

    // FOR TESTING PURPOSES (Exploratory)
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Input as a DateTime:");
        String input = "";
        while (!input.equals("exit")) {
            input = sc.nextLine();
            try {
                if (!input.equals("exit")) {
                    System.out.println(parseToDateTime(input));
                } else {
                    System.out.println("End of test.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        sc.close();
    }

}
