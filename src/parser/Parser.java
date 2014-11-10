//@author A0116208N
package parser;

import objects.DateTime;
import objects.Task;
import logic.Command;

/**
 * This class is the facade for all other parsing methods. These include parsing
 * from user input, parsing from file, as well as date-related methods. The
 * facade pattern is used to simplify interactions with the parser package.
 * <p>
 * The Parser classes (DateParser, etc.) have been kept as static classes
 * because they store no instance data at all. There is no benefit in making
 * them instance objects; in fact, doing so will complicate code unnecessarily.
 * 
 */
public class Parser {

    /**
     * Parses the input String into a Command of the relevant type. The Command
     * will store relevant information contained in the String.
     * 
     * @return Command object of the relevant subclass
     * @throws IllegalArgumentException
     *             when a user input is invalid. The exception will contain a
     *             message related to the error.
     */
    public static Command parse(String input) throws IllegalArgumentException {
        return InputParser.parse(input);
    }

    /**
     * Forms a <code>Task</code> object by parsing a <code>String</code>
     * containing the stored string literals.
     * <p>
     * Note that the input <code>String</code> must be of the given
     * format(below), contain all four parameters names ("start:" to "type:"),
     * and have spaces between tags and parameter names. The position of the
     * tags is flexible as long as it comes after "start:".
     * 
     * @param text
     *            format:
     *            {@literal "<name> ### start: <date/time> due: <date/time> completed: 
     * <date/time> <tags> type: <type>"}
     */
    public static Task parseToTask(String text) {
        return FileParser.parse(text);
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
    // TODO: Add chain of @See for consistency between definition of valid
    // dates/times
    public static DateTime parseToDateTime(String str) {
        return DateParser.parseToDateTime(str);
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
        return DateParser.getCurrDateTime();
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
        return DateParser.getCurrDateTimeStr();
    }

    /**
     * Returns the current time in a <code>String</code> object.
     * <p>
     * <i>Uses the default system settings for time.</i>
     * 
     * @return <code>String</code> object containing time in <code>HHmm</code>
     */
    public static String getCurrTimeStr() {
        return DateParser.getCurrTimeStr();
    }

    /**
     * Returns the current date in a <code>String</code>.
     * <p>
     * <i>Uses the default system settings for date.</i>
     * 
     * @return <code>String</code> object containing date in
     *         <code>dd/MM/yyyy</code>
     */
    public static String getCurrDateStr() {
        return DateParser.getCurrDateStr();
    }

    /**
     * Returns tomorrow's date in a <code>String</code>.
     * <p>
     * <i>Uses the default system settings for date.</i>
     * 
     * @return <code>String</code> object containing date in
     *         <code>dd/MM/yyyy</code>
     */
    public static String getTmrDateStr() {
        return DateParser.getTmrDateStr();
    }

    /**
     * Returns the date of the day that is <code>numDaysLater</code> days from
     * today, in a <code>String</code> object.
     * <p>
     * <i>Uses the default system settings for date.</i>
     * 
     * @return <code>String</code> object containing date in
     *         <code>dd/MM/yyyy</code>.
     */
    public static String getDateFromNowStr(int numDaysLater) {
        return DateParser.getDateFromNowStr(numDaysLater);
    }

    /**
     * Checks if the input <code>String</code> is in the accepted date format
     * <code>dd/MM/yyyy</code>.
     */
    public static boolean isValidDate(String str) {
        return DateParser.isValidDate(str);
    }

    /**
     * Checks if the input <code>String</code> is a valid time. This method
     * assumes the 24HR time format, i.e. 0000-2359, or the word format "now".
     * 
     * <p>
     * <i> The input should be an integer String. </i>
     */
    public static boolean isValidTime(String timeStr) {
        return DateParser.isValidTime(timeStr);
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
     * <p>
     * Alternatives for date include "today", "tomorrow" and "tmr" Alternatives
     * for time include "now".
     */
    public static boolean isValidDateTime(String str) {
        return DateParser.isValidDateTime(str);
    }

}
