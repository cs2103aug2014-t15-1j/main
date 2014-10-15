package Parser;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import Storage.DateTime;

public class DateParser {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
            "dd/MM/yyyy HHmm");

    public static void printDate() {
        Calendar cal = Calendar.getInstance();
        System.out.println(DATE_FORMAT.format(cal.getTime()));
    }

    public static DateTime getCurrDateTime() {
        Calendar cal = Calendar.getInstance();
        return new DateTime(DATE_FORMAT.format(cal.getTime()));
    }

    public static String getCurrDateTimeStr() {
        return getCurrDateTime().toString();
    }

    public static String getCurrDateStr() {
        String currDate = getCurrDateTimeStr();
        String[] dateFields = currDate.split(" ");
        return dateFields[0];
    }

    public static String getCurrTimeStr() {
        String currDate = getCurrDateTimeStr();
        String[] dateFields = currDate.split(" ");
        return dateFields[1];
    }

    public static DateTime parseToDate(String str) {
        if (!isValidDateTime(str)){
            throw new IllegalArgumentException("Invalid input for parseToDate");
        }
        
        switch (getDateType(str)) {
            case 1:
                return new DateTime(str, "");
            case 2:
                return new DateTime(getCurrDateStr(), str);
            case 3:
                return new DateTime(str);
            case 4:
                //TODO: some safety checks
                String[] dateFields = str.split(" ");
                return new DateTime(dateFields[1], dateFields[0]);
        }

        return null;
    }

    private static boolean isValidDateTime(String str) {
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

    private static boolean isSingleItemArray(String[] dateFields) {
        return dateFields.length == 1;
    }

    private static boolean firstItemIsDate(String[] dateFields) {
        return isValidDate(dateFields[0]);
    }

    private static boolean firstDateEarlier(String first, String second) {
        int[] date1 = splitToDatesInt(first);
        int[] date2 = splitToDatesInt(second);

        int day1 = date1[0];
        int mth1 = date1[1];
        int yr1 = date1[2];
        int day2 = date2[0];
        int mth2 = date2[1];
        int yr2 = date2[2];

        if (yr1 < yr2) {
            return true;
        } else if (yr1 == yr2) {
            if (mth1 < mth2) {
                return true;
            } else if (mth1 == mth2 && day1 < day2) {
                return true;
            }
        }

        System.out.println("second is earlier");
        return false;
    }

    private static int[] splitToDatesInt(String str) {
        assert (isValidDate(str));

        String[] split = str.split("/");

        int[] result = new int[3];
        result[0] = Integer.parseInt(split[0]);
        result[1] = Integer.parseInt(split[1]);
        result[2] = Integer.parseInt(split[2]);

        return result;
    }

    // TODO: Reorganise: move all aux to Parser, move all specifics out?
    private static boolean isValidDate(String str) {
        // Tentatively, dates = "DD/MM/YYYY"
        boolean hasTwoSlashes;
        boolean hasValidCompLengths;
        boolean hasIntComponents;
        boolean hasValidIntComp;

        try {
            String[] components = str.split("/");
            hasTwoSlashes = (components.length == 3);
            hasValidCompLengths = (components[0].length() == 2) &&
                                  (components[1].length() == 2) &&
                                  (components[2].length() == 4);
            hasIntComponents = Parser.isInteger(components[0]) &&
                               Parser.isInteger(components[1]) &&
                               Parser.isInteger(components[2]);
            hasValidIntComp = isValidYear(components[2]) &&
                              isValidMonth(components[1]) &&
                              isValidDay(components[0], components[1],
                                         components[2]);
        } catch (Exception e) {
            return false;
        }

        return hasTwoSlashes && hasValidCompLengths && hasIntComponents &&
               hasValidIntComp;
    }

    // TODO: Extract to isValidDay(int, int, int) - use this method to parseInt
    public static boolean isValidDay(String day, String month, String year) {
        int dayNum = Integer.parseInt(day);
        int monthNum = Integer.parseInt(month);
        int yearNum = Integer.parseInt(year);
        boolean isLeapYear = isLeapYear(yearNum);

        switch (monthNum) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return dayNum > 0 && dayNum <= 31;

            case 2:
                if (isLeapYear) {
                    return dayNum > 0 && dayNum <= 29;
                } else {
                    return dayNum > 0 && dayNum <= 28;
                }

            case 4:
            case 6:
            case 9:
            case 11:
                return dayNum > 0 && dayNum <= 30;
        }
        return false;
    }

    public static boolean isLeapYear(int year) {
        return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
    }

    public static boolean isValidMonth(String string) {
        int monthNum = Integer.parseInt(string);
        return monthNum > 0 && monthNum <= 12;
    }

    public static boolean isValidYear(String string) {
        int yearNum = Integer.parseInt(string);
        // TODO: Magic strings in year, month, day
        return yearNum >= 1819;
    }

    private static boolean isValidTime(String str) {
        // Time = MMHH
        try {
            String hours_str = str.substring(0, 2);
            String min_str = str.substring(2, 4);

            int hours_int = Integer.parseInt(hours_str);
            int min_int = Integer.parseInt(min_str);

            boolean isValidHH = hours_int >= 0 && hours_int < 24;
            boolean isValidMM = min_int >= 0 && min_int < 60;

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
        System.out.println("-400: " + isLeapYear(-400));
        
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
        System.out.println("23/04/2014: " + parseToDate("23/04/2014"));
        System.out.println("2200: " + parseToDate("2200"));
        System.out.println("23/04/2014 2200: " + parseToDate("23/04/2014 2200"));
        System.out.println("2200 23/04/2014: " + parseToDate("2200 23/04/2014"));
        // TODO: test exception
        // System.out.println("exception: " + parseToDate("aaa"));
        
    }
}
