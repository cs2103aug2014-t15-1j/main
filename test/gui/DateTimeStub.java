package gui;

import database.DateTime;

/*
 * This class is to be used for testing ONLY.
 * DateTimeStub allows dependency injection for DateTime Objects
 * NOTE: I have only implemented the constructor
 */
public class DateTimeStub extends DateTime {
    private final static String OR_EMPTY_PATTERN = "|^$";
    private final static String DATE_PATTERN = "((((((0[1-9]|[12][0-9]|3[0-1])/(0[13578]|1[02]))|" +
                                               "((0[1-9]|[12][0-9]|30)/(0[469]|11))|" +
                                               "((0[1-9]|1[0-9]|2[0-8])/02))/\\d{4})|" +
                                               "(29/02/((\\d{2}(0[48]|[2468][048]|[13579][26]))|" +
                                               "(([02468][048]|[13579][26])00))))" +
                                               OR_EMPTY_PATTERN + ")";

    private final static String TIME_PATTERN = "(((([0-1][0-9])|(2[0-3]))[0-5][0-9])" +
                                               OR_EMPTY_PATTERN + ")";

    private final static String DATE_TIME_PATTERN = "(" + DATE_PATTERN + " " +
                                                    TIME_PATTERN + ")|" +
                                                    DATE_PATTERN + "|" +
                                                    TIME_PATTERN +
                                                    OR_EMPTY_PATTERN;
    private String date = "";
    private int day = 0;
    private int month = 0;
    private int year = 0;
    private String time = "";

    public DateTimeStub(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public DateTimeStub() {
    }
}
