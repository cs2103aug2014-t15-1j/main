package database;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for the DateTime class.
 * 
 * @author Pierce Anderson Fu
 * 
 */

public class DateTimeTest {

    /**
     * Checks if default constructor initializes objects with correct attribute
     * values.
     * 
     * @throws Exception
     */
    @Test
    public void testDefaultConstructorHasEmptyAttributes() throws Exception {
        DateTime dT = new DateTime();
        assertEquals("Empty date String", "", dT.getDate());
        assertEquals("Zeroed day", 0, dT.getDay());
        assertEquals("Zeroed month", 0, dT.getMonth());
        assertEquals("Zeroed year", 0, dT.getYear());
        assertEquals("Empty time String", "", dT.getTime());
    }

    /**
     * Checks if constructor that takes in Strings for date and time as
     * arguments initializes date and time values correctly, and attributes have
     * been cloned by value, not reference.
     * 
     * @throws Exception
     */
    @Test
    public void testConstructorStoreStringArgs() throws Exception {
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
     * Checks if constructor that takes in a DateTime object argument creates a
     * new object with correct attributes, and that attributes have been cloned
     * by value, not reference.
     * 
     * @throws Exception
     */
    @Test
    public void testConstructorClonesByValue() throws Exception {
        String date = "10/11/2014";
        String time = "2359";
        DateTime dT1 = new DateTime(date, time);
        DateTime dT2 = new DateTime(dT1);
        date = "";
        time = "";
        assertEquals("Cloned date", dT1.getDate(), dT2.getDate());
        assertEquals("Cloned day", dT1.getDay(), dT2.getDay());
        assertEquals("Cloned month", dT1.getMonth(), dT2.getMonth());
        assertEquals("Cloned year", dT1.getYear(), dT2.getYear());
        assertEquals("Cloned time", dT1.getTime(), dT2.getTime());

        dT1.setDate(date);
        dT1.setTime(time);
        assertEquals("Empty date String", "", dT1.getDate());
        assertEquals("Zeroed day", 0, dT1.getDay());
        assertEquals("Zeroed month", 0, dT1.getMonth());
        assertEquals("Zeroed year", 0, dT1.getYear());
        assertEquals("Empty time String", "", dT1.getTime());

        assertEquals("Cloned date by value", "10/11/2014", dT2.getDate());
        assertEquals("Cloned day by value", 10, dT2.getDay());
        assertEquals("Cloned month by value", 11, dT2.getMonth());
        assertEquals("Cloned year by value", 2014, dT2.getYear());
        assertEquals("Cloned time by value", "2359", dT2.getTime());
    }

    /**
     * Checks if resetDate() sets date attributes to correct value, without
     * affecting time attribute.
     * 
     * @throws Exception
     */
    @Test
    public void testResetDate() throws Exception {
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
    }

    /**
     * Checks if resetTime() sets time attributes to correct value, without
     * affecting date attribute.
     * 
     * @throws Exception
     */
    @Test
    public void testResetTime() throws Exception {
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
    }

    /**
     * Checks if resetDateTime() sets both date and time attributes to correct
     * values.
     * 
     * @throws Exception
     */
    @Test
    public void testResetDateTime() throws Exception {
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
    }

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
     * 
     * @throws Exception
     */
    @Test
    public void testGetDatePattern() throws Exception {
        // This is the boundary case for empty values.
        assertTrue("Empty string", "".matches(datePattern()));

        // This is a boundary case for both minimum day and month partition.
        assertTrue("Min day and month", "01/01/2014".matches(datePattern()));

        // This is a boundary case for the maximum day of months with 30 days.
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

        // This is a boundary case for the maximum day in non-leap February.
        assertFalse("Not leap year", "29/02/2014".matches(datePattern()));

        // This is a boundary case for the maximum day in non-leap February.
        assertFalse("Not leap year", "29/02/2100".matches(datePattern()));

        // This is a boundary case for the minimum day value partition.
        assertFalse("Not valid day", "00/01/2014".matches(datePattern()));

        // This is a boundary case for the maximum day value partition.
        assertFalse("Not valid day", "32/01/2014".matches(datePattern()));

        // This is a boundary case for the maximum day of months with 30 days.
        assertFalse("Not valid day", "31/06/2014".matches(datePattern()));

        // This is a boundary case for the minimum month value partition.
        assertFalse("Not valid month", "01/00/2014".matches(datePattern()));

        // This is a boundary case for the maximum month value partition.
        assertFalse("Not valid month", "01/13/2014".matches(datePattern()));
    }

    /**
     * Checks if regex String used to assert that times provided as arguments
     * have valid formats and values.
     * 
     * @throws Exception
     */
    @Test
    public void testGetTimePattern() throws Exception {
        // This is the boundary case for empty values.
        assertTrue("Empty string", "".matches(timePattern()));

        // This is the boundary case for the minimum hour and minute partition.
        assertTrue("Min time", "0000".matches(timePattern()));

        // This is the boundary case for the maximum hour and minute partition.
        assertTrue("Max time", "2359".matches(timePattern()));

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
     * 
     * @throws Exception
     */
    @Test
    public void testGetDateTimePattern() throws Exception {
        // This is the boundary case for empty formats.
        assertTrue("Empty string", "".matches(dateTimePattern()));
        
        // This is the boundary case for empty date formats.
        assertTrue("Empty date string", "2359".matches(dateTimePattern()));
        
        // This is the boundary case for empty time formats.
        assertTrue("Empty time string", "11/11/2014".matches(dateTimePattern()));
        
        // This is the boundary case for date and time formats.
        assertTrue("Date time string",
                   "11/11/2014 2359".matches(dateTimePattern()));
        
        // This is the boundary case for empty date formats.
        assertFalse("Residual space", " 2359".matches(dateTimePattern()));
        
        // This is the boundary case for empty time formats.
        assertFalse("Residual space", "11/11/2014 ".matches(dateTimePattern()));
        
        // This is the boundary case for empty date and time formats.
        assertFalse("Residual space", " ".matches(dateTimePattern()));
    }
}