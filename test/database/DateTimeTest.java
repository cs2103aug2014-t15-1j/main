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

    @Test
    public void testDefaultConstructorHasEmptyAttributes() throws Exception {
        DateTime dT = new DateTime();
        assertEquals("Empty date String", "", dT.getDate());
        assertEquals("Zeroed day", 0, dT.getDay());
        assertEquals("Zeroed month", 0, dT.getMonth());
        assertEquals("Zeroed year", 0, dT.getYear());
        assertEquals("Empty time String", "", dT.getTime());
    }

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

    @Test
    public void testGetDatePattern() throws Exception {
        assertTrue("Empty string", "".matches(datePattern()));
        assertTrue("Min day and month", "01/01/2014".matches(datePattern()));
        assertTrue("Max day, Jun", "30/06/2014".matches(datePattern()));
        assertTrue("Max day, Dec", "31/12/2014".matches(datePattern()));
        assertTrue("Max day, Feb", "28/02/2014".matches(datePattern()));
        assertTrue("Max day, leap Feb", "29/02/2016".matches(datePattern()));
        assertTrue("Max day, leap Feb", "29/02/2000".matches(datePattern()));
        assertTrue("Max day, leap Feb", "29/02/2400".matches(datePattern()));
        assertFalse("Not leap year", "29/02/2014".matches(datePattern()));
        assertFalse("Not leap year", "29/02/2100".matches(datePattern()));
        assertFalse("Not valid day", "00/01/2014".matches(datePattern()));
        assertFalse("Not valid day", "32/01/2014".matches(datePattern()));
        assertFalse("Not valid day", "31/06/2014".matches(datePattern()));
        assertFalse("Not valid month", "01/00/2014".matches(datePattern()));
        assertFalse("Not valid month", "01/13/2014".matches(datePattern()));
    }

    @Test
    public void testGetTimePattern() throws Exception {
        assertTrue("Empty string", "".matches(timePattern()));
        assertTrue("Min time", "0000".matches(timePattern()));
        assertTrue("Max time", "2359".matches(timePattern()));
        assertFalse("More than 23 H", "2400".matches(timePattern()));
        assertFalse("More than 59 min", "2360".matches(timePattern()));
        assertFalse("More than 2X H", "3000".matches(timePattern()));
    }

    @Test
    public void testGetDateTimePattern() throws Exception {
        assertTrue("Empty string", "".matches(dateTimePattern()));
        assertTrue("Empty date string", "2359".matches(dateTimePattern()));
        assertTrue("Empty time string", "11/11/2014".matches(dateTimePattern()));
        assertTrue("Date time string",
                   "11/11/2014 2359".matches(dateTimePattern()));
        assertFalse("Residual space", " 2359".matches(dateTimePattern()));
        assertFalse("Residual space", "11/11/2014 ".matches(dateTimePattern()));
        assertFalse("Residual space", " ".matches(dateTimePattern()));
    }
}