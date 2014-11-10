//@author A0116373J

package objects;

import static org.junit.Assert.*;
import objects.DateTime;
import objects.Task;

import org.junit.Test;

/**
 * Unit tests for the DateTime class.
 */

public class DateTimeTest {

    private String datePattern() {
        return DateTime.getDatePattern();
    }

    private String timePattern() {
        return DateTime.getTimePattern();
    }

    private String dateTimePattern() {
        return DateTime.getDateTimePattern();
    }

    /**
     * Checks if regex String used to assert that dates provided as arguments
     * have valid formats and values.
     */
    @Test
    public void testGetDatePattern() {
        // True cases
        // This is the boundary case for empty values.
        assertTrue("Empty string", "".matches(datePattern()));

        // This is a boundary case for both minimum day and month partition.
        assertTrue("Min day and month", "01/01/2014".matches(datePattern()));

        // This is a boundary case for the maximum day in months with 30 days.
        assertTrue("Max day, Jun", "30/06/2014".matches(datePattern()));

        // This is a boundary case for both maximum day and month partition.
        assertTrue("Max day, Dec", "31/12/2014".matches(datePattern()));

        // This is a boundary case for the maximum day in non-leap February.
        assertTrue("Max day, Feb", "28/02/2014".matches(datePattern()));

        // This is a boundary case for the maximum day in leap year February.
        assertTrue("Max day, leap Feb", "29/02/2016".matches(datePattern()));

        // This is a boundary case for the maximum day in leap year February.
        assertTrue("Max day, leap Feb", "29/02/2000".matches(datePattern()));

        // This is a boundary case for the maximum day in leap year February.
        assertTrue("Max day, leap Feb", "29/02/2400".matches(datePattern()));

        // False cases
        // This is a boundary case for the maximum day in non-leap February.
        assertFalse("Not leap year", "29/02/2014".matches(datePattern()));

        // This is a boundary case for the maximum day in non-leap February.
        assertFalse("Not leap year", "29/02/2100".matches(datePattern()));

        // This is a boundary case for the minimum day value partition.
        assertFalse("Not valid day", "00/01/2014".matches(datePattern()));

        // This is a boundary case for the maximum day value partition.
        assertFalse("Not valid day", "32/01/2014".matches(datePattern()));

        // This is a boundary case for the maximum day in months with 30 days.
        assertFalse("Not valid day", "31/06/2014".matches(datePattern()));

        // This is a boundary case for the minimum month value partition.
        assertFalse("Not valid month", "01/00/2014".matches(datePattern()));

        // This is a boundary case for the maximum month value partition.
        assertFalse("Not valid month", "01/13/2014".matches(datePattern()));
    }

    /**
     * Checks if regex String used to assert that times provided as arguments
     * have valid formats and values.
     */
    @Test
    public void testGetTimePattern() {
        // True cases
        // This is the boundary case for empty values.
        assertTrue("Empty string", "".matches(timePattern()));

        // This is the boundary case for the minimum hour and minute partition.
        assertTrue("Min time", "0000".matches(timePattern()));

        // This is the boundary case for the maximum hour and minute partition.
        assertTrue("Max time", "2359".matches(timePattern()));

        // False cases
        // This is the boundary case for the maximum hour partition.
        assertFalse("More than 23 H", "2400".matches(timePattern()));

        // This is the boundary case for the maximum hour partition.
        assertFalse("More than 2X H", "3000".matches(timePattern()));

        // This is the boundary case for the maximum minute partition.
        assertFalse("More than 59 min", "2360".matches(timePattern()));
    }

    /**
     * Checks if regex String used to assert that DateTimes provided as
     * arguments have valid formats and values.
     */
    @Test
    public void testGetDateTimePattern() {
        // True cases
        // This is the boundary case for empty formats.
        assertTrue("Empty string", "".matches(dateTimePattern()));

        // This is the boundary case for empty date formats.
        assertTrue("Empty date string", "2359".matches(dateTimePattern()));

        // This is the boundary case for empty time formats.
        assertTrue("Empty time string", "11/11/2014".matches(dateTimePattern()));

        // This is the boundary case for date and time formats.
        assertTrue("Date time string",
                   "11/11/2014 2359".matches(dateTimePattern()));

        // False cases
        // This is the boundary case for empty date formats.
        assertFalse("Residual space", " 2359".matches(dateTimePattern()));

        // This is the boundary case for empty time formats.
        assertFalse("Residual space", "11/11/2014 ".matches(dateTimePattern()));

        // This is the boundary case for empty date and time formats.
        assertFalse("Residual space", " ".matches(dateTimePattern()));
    }

    /**
     * Checks if default constructor initializes objects with empty attributes.
     */
    @Test
    public void testDefaultConstructorHasEmptyAttributes() {
        DateTime dT = new DateTime();
        assertEquals("Empty date String", "", dT.getDate());
        assertEquals("Zeroed day", 0, dT.getDay());
        assertEquals("Zeroed month", 0, dT.getMonth());
        assertEquals("Zeroed year", 0, dT.getYear());
        assertEquals("Empty time String", "", dT.getTime());
        assertTrue("Empty DateTime object", dT.isEmpty());
    }

    /**
     * Checks if constructor, that takes in Strings for date and time as
     * arguments, initializes date and time values correctly, and that
     * attributes have been cloned by value, not reference.
     */
    @Test
    public void testConstructorStoreStringArgs() {
        String date = "10/11/2014";
        String time = "2359";
        DateTime dT = new DateTime(date, time);
        date = "";
        time = "";
        assertEquals("Stored date by value", "10/11/2014", dT.getDate());
        assertEquals("Parsed day from date", 10, dT.getDay());
        assertEquals("Parsed month from date", 11, dT.getMonth());
        assertEquals("Parsed year from date", 2014, dT.getYear());
        assertEquals("Stored time by value", "2359", dT.getTime());
    }

    /**
     * Checks if constructor is able to handle empty String arguments correctly.
     * Boundary test for constructor arguments.
     */
    @Test
    public void testConstructorTakesEmptyStringsArgs() {
        String date = "10/11/2014";
        String time = "2359";
        DateTime dT1 = new DateTime(date, "");
        DateTime dT2 = new DateTime("", time);
        assertEquals("Stored date by value", "10/11/2014", dT1.getDate());
        assertEquals("Parsed day from date", 10, dT1.getDay());
        assertEquals("Parsed month from date", 11, dT1.getMonth());
        assertEquals("Parsed year from date", 2014, dT1.getYear());
        assertEquals("Stored empty time", "", dT1.getTime());

        assertEquals("Stored empty date", "", dT2.getDate());
        assertEquals("Did not parse day", 0, dT2.getDay());
        assertEquals("Did not parse month", 0, dT2.getMonth());
        assertEquals("Did not parse year", 0, dT2.getYear());
        assertEquals("Stored time by value", "2359", dT2.getTime());
    }

    /**
     * Checks if constructor that takes in a DateTime object argument creates a
     * new object with correct attributes, and that attributes have been cloned
     * by value, not reference.
     */
    @Test
    public void testConstructorClonesByValue() {
        String date = "10/11/2014";
        String time = "2359";
        DateTime dT1 = new DateTime(date, time);
        DateTime dT2 = new DateTime(dT1);
        date = "";
        time = "";

        // Check that attributes have been cloned successfully
        assertEquals("Cloned date", dT1.getDate(), dT2.getDate());
        assertEquals("Cloned day", dT1.getDay(), dT2.getDay());
        assertEquals("Cloned month", dT1.getMonth(), dT2.getMonth());
        assertEquals("Cloned year", dT1.getYear(), dT2.getYear());
        assertEquals("Cloned time", dT1.getTime(), dT2.getTime());

        dT1.resetDateTime();
        assertTrue("Empty DateTime", dT1.isEmpty());

        // Check that attributes have been cloned by value
        assertEquals("Cloned date by value", "10/11/2014", dT2.getDate());
        assertEquals("Cloned day by value", 10, dT2.getDay());
        assertEquals("Cloned month by value", 11, dT2.getMonth());
        assertEquals("Cloned year by value", 2014, dT2.getYear());
        assertEquals("Cloned time by value", "2359", dT2.getTime());
    }

    /**
     * Checks if toString function works as intended, and that spaces only occur
     * when appropriate.
     */
    @Test
    public void testToString() {
        // Case for partition with non-empty date and non-empty time args.
        String date = "10/11/2014";
        String time = "2359";
        DateTime dT = new DateTime(date, time);
        assertEquals("Non-empty date and time", "10/11/2014 2359",
                     dT.toString());
        assertFalse("Object is not empty", dT.isEmpty());

        // Boundary case for partition with empty date and non-empty time args.
        dT.setDate("");
        assertEquals("Empty date, non-empty time", "2359", dT.toString());
        assertFalse("Object is not empty", dT.isEmpty());

        // Boundary case for partition with non-empty date and empty time args.
        dT.setDate(date);
        dT.setTime("");
        assertEquals("Non-empty date, empty time", "10/11/2014", dT.toString());
        assertFalse("Object is not empty", dT.isEmpty());

        // Boundary case for partition with empty date and empty time args.
        dT.setDate("");
        assertEquals("Empty date and time", "", dT.toString());
        assertTrue("Object is empty", dT.isEmpty());
    }

    /**
     * Checks if compareTo function is correct and reflexive across the
     * following cases:
     * <ul>
     * <li>Different year values.
     * <li>Different month values.
     * <li>Different day values.
     * <li>Equal date values.
     * <li>Equal date but different time values.
     * <li>Equal date and time values.
     * <li>One has time values, but the other doesn't.
     * <li>One has date values, but the other doesn't.
     * </ul>
     */
    @Test
    public void testCompareTo() {
        String date1 = "10/10/2010";
        String date2 = "10/10/2011";
        String time1 = "0000";
        String time2 = "2359";

        // Different year, empty time
        DateTime dT1 = new DateTime(date1, "");
        DateTime dT2 = new DateTime(date2, "");
        assertTrue("First is earlier than second", dT1.compareTo(dT2) < 0);
        assertTrue("Second is later than first", dT2.compareTo(dT1) > 0);

        // Different month, empty time
        date2 = "10/11/2010";
        dT2.setDate(date2);
        assertTrue("First is earlier than second", dT1.compareTo(dT2) < 0);
        assertTrue("Second is later than first", dT2.compareTo(dT1) > 0);

        // Different day, empty time
        date2 = "11/10/2010";
        dT2.setDate(date2);
        assertTrue("First is earlier than second", dT1.compareTo(dT2) < 0);
        assertTrue("Second is later than first", dT2.compareTo(dT1) > 0);

        // Equal date, empty time
        dT2.setDate(date1);
        assertTrue("First is equal to second", dT1.compareTo(dT2) == 0);
        assertTrue("Second is equal to first", dT2.compareTo(dT1) == 0);

        // Equal date, different time
        dT1.setTime(time1);
        dT2.setTime(time2);
        assertTrue("First is earlier than second", dT1.compareTo(dT2) < 0);
        assertTrue("Second is later than first", dT2.compareTo(dT1) > 0);

        // Equal date, equal time
        dT2.setTime(time1);
        assertTrue("First is equal to second", dT1.compareTo(dT2) == 0);
        assertTrue("Second is equal to first", dT2.compareTo(dT1) == 0);

        // Equal date, first with time, second without
        dT2.resetTime();
        assertTrue("First is earlier than second", dT1.compareTo(dT2) < 0);
        assertTrue("Second is later than first", dT2.compareTo(dT1) > 0);

        // First with DateTime value, second without
        dT2.resetDateTime();
        assertTrue("First is earlier than second", dT1.compareTo(dT2) < 0);
        assertTrue("Second is later than first", dT2.compareTo(dT1) > 0);

        // Both without DateTime values
        dT1.resetDateTime();
        assertTrue("First is equal to second", dT1.compareTo(dT2) == 0);
        assertTrue("Second is equal to first", dT2.compareTo(dT1) == 0);
    }

    /**
     * Checks if equals function is correct across the following cases:
     * <ul>
     * <li>Not equal to non-DateTime types.
     * <li>Equals to self.
     * <li>Equals to another object with equal attribute values.
     * <li>Not equal to another object with different attribute values.
     * </ul>
     */
    @Test
    public void testEquals() {
        String date1 = "10/10/2010";
        String date2 = "11/11/2011";
        DateTime dT1 = new DateTime(date1, "");
        DateTime dT2 = new DateTime(date1, "");
        DateTime dT3 = new DateTime(date2, "");

        assertFalse("Different types", dT1.equals(new Task()));
        assertTrue("Identical to self", dT1.equals(dT1));
        assertTrue("Equal values", dT1.equals(dT2));
        assertFalse("Not equal values", dT1.equals(dT3));
    }

    /**
     * Checks if hashCode function follows the following general contracts of
     * hashCode:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during an
     * execution of a Java application, the hashCode method must consistently
     * return the same integer
     * <li>Check if two DateTime objects are equal according to the
     * equals(DateTime) method, then calling the hashCode method on each of the
     * two objects must produce the same integer result.
     * </ul>
     */
    @Test
    public void testHashCode() {
        String date1 = "10/10/2010";
        String date2 = "11/11/2011";
        DateTime dT1 = new DateTime(date1, "");
        DateTime dT2 = new DateTime(date1, "");
        DateTime dT3 = new DateTime(date2, "");

        // Testing equal self, i.e. consistent value across different calls.
        assertTrue("Equal values", dT1.equals(dT1));
        assertEquals("Equal hashcodes", dT1.hashCode(), dT1.hashCode());

        // Testing equal values.
        assertTrue("Equal values", dT1.equals(dT2));
        assertEquals("Equal hashcodes", dT1.hashCode(), dT2.hashCode());

        // Testing non-equal values.
        assertFalse("Equal values", dT1.equals(dT3));
        assertNotEquals("Not equal hashcodes", dT1.hashCode(), dT3.hashCode());
    }

    /**
     * Checks if isEarlierThan function returns correct boolean values and
     * reflexivity.
     */
    @Test
    public void testIsEarlierThan() {
        String date1 = "10/10/2010";
        String date2 = "11/11/2011";
        DateTime dT1 = new DateTime(date1, "");
        DateTime dT2 = new DateTime(date2, "");

        assertTrue("dT1 is earlier than dT2", dT1.isEarlierThan(dT2));
        assertFalse("dT2 is not earlier than dT1", dT2.isEarlierThan(dT1));
        assertFalse("dT1 is not earlier than itself", dT1.isEarlierThan(dT1));
    }

    /**
     * Checks if isLaterThan function returns correct boolean values and
     * reflexivity.
     */
    @Test
    public void testIsLaterThan() {
        String date1 = "10/10/2010";
        String date2 = "11/11/2011";
        DateTime dT1 = new DateTime(date1, "");
        DateTime dT2 = new DateTime(date2, "");

        assertTrue("dT2 is later than dT1", dT2.isLaterThan(dT1));
        assertFalse("dT1 is not later than dT2", dT1.isLaterThan(dT2));
        assertFalse("dT2 is not later than itself", dT2.isLaterThan(dT2));
    }

    /**
     * Checks if resetDate function sets date attributes to empty value, without
     * affecting time attribute.
     */
    @Test
    public void testResetDate() {
        String date = "10/11/2014";
        String time = "2359";
        DateTime dT = new DateTime(date, time);
        dT.resetDate();
        date = "";
        time = "";
        assertEquals("Empty date String", "", dT.getDate());
        assertEquals("Zeroed day", 0, dT.getDay());
        assertEquals("Zeroed month", 0, dT.getMonth());
        assertEquals("Zeroed year", 0, dT.getYear());
        assertEquals("Stored time by value", "2359", dT.getTime());
        assertFalse("Non-empty DateTime object", dT.isEmpty());
    }

    /**
     * Checks if resetTime function sets time attributes to empty value, without
     * affecting date attribute.
     */
    @Test
    public void testResetTime() {
        String date = "10/11/2014";
        String time = "2359";
        DateTime dT = new DateTime(date, time);
        dT.resetTime();
        date = "";
        time = "";
        assertEquals("Stored date by value", "10/11/2014", dT.getDate());
        assertEquals("Stored day by value", 10, dT.getDay());
        assertEquals("Stored month by value", 11, dT.getMonth());
        assertEquals("Stored year by value", 2014, dT.getYear());
        assertEquals("Empty time String", "", dT.getTime());
        assertFalse("Non-empty DateTime object", dT.isEmpty());
    }

    /**
     * Checks if resetDateTime function sets both date and time attributes to
     * empty values.
     */
    @Test
    public void testResetDateTime() {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dT = new DateTime(date, time);
        dT.resetDateTime();
        date = "";
        time = "";
        assertEquals("Empty date String", "", dT.getDate());
        assertEquals("Zeroed day", 0, dT.getDay());
        assertEquals("Zeroed month", 0, dT.getMonth());
        assertEquals("Zeroed year", 0, dT.getYear());
        assertEquals("Empty time String", "", dT.getTime());
        assertTrue("Empty DateTime object", dT.isEmpty());
    }
}