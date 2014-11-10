package parser;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test Class for DateParser. All methods are called via the Parser class as
 * testing the facade separately will cause unnecessary double-work.
 * <p>
 * <i>Note that Assertions <strong>must</strong> be enabled for AssertionErrors
 * to be tested. Check the run configurations (of this test class or test suite)
 * and make sure it includes VM argument "-ea".</i>
 */
//@author A0116208N
public class TestDateParser {

    @Test
    public void testCurrDtGetters() {
        System.out.println("\n>> Testing Current Date/Time...");

        // Cannot use automated tests for this because current time is
        // constantly changing
        System.out.println("DateTime: " + Parser.getCurrDateTime());
        System.out.println("DateTimeStr: " + Parser.getCurrDateTimeStr());
        System.out.println("TodayDateStr: " + Parser.getCurrDateStr());
        System.out.println("TmrDateStr: " + Parser.getTmrDateStr());
        System.out.println("DateStr 7 days from now: " + Parser.getDateFromNowStr(7));
        System.out.println("DateStr 30 days from now: " + Parser.getDateFromNowStr(30));
        System.out.println("Date: " + Parser.getCurrDateStr());
        System.out.println("Time: " + Parser.getCurrTimeStr());

        System.out.println("...success!");
    }
    
    @Test
    public void testValidTime() {
        System.out.println("\n>> Testing isValidTime...");

        // Lower Boundary
        assertTrue(Parser.isValidTime("0000"));
        assertTrue(Parser.isValidTime("0001"));
        assertTrue(Parser.isValidTime("0100"));
        assertFalse(Parser.isValidTime("-001"));
        // Upper Boundary
        assertTrue(Parser.isValidTime("2359"));
        assertFalse(Parser.isValidTime("2400"));
        assertFalse(Parser.isValidTime("0060"));
        // Other cases
        assertTrue(Parser.isValidTime("now"));
        // Extreme
        assertFalse(Parser.isValidTime("9999"));
        assertFalse(Parser.isValidTime("abc"));

        System.out.println("...success!");
    }
   
    @Test
    public void testValidDate() {
        System.out.println("\n>> Testing isValidDate...");

        // Lower Boundary
        assertTrue(Parser.isValidDate("01/01/1819"));
        assertFalse(Parser.isValidDate("00/01/1819"));
        assertFalse(Parser.isValidDate("01/00/1819"));
        assertFalse(Parser.isValidDate("01/01/1818"));
        // Upper Boundary
        assertTrue(Parser.isValidDate("31/12/9999"));
        assertFalse(Parser.isValidDate("32/12/9999"));
        assertFalse(Parser.isValidDate("31/13/9999"));
        // Upper Boundary (Leap Years)
        assertTrue(Parser.isValidDate("29/02/2016"));
        assertFalse(Parser.isValidDate("30/02/2016"));
        assertTrue(Parser.isValidDate("28/02/2100"));
        assertFalse(Parser.isValidDate("29/02/2100"));
        // Word Dates
        assertTrue(Parser.isValidDate("today"));
        assertTrue(Parser.isValidDate("tomorrow"));
        assertTrue(Parser.isValidDate("tmr"));
        // Extreme
        assertFalse(Parser.isValidDate("-1/02/2016"));
        assertFalse(Parser.isValidDate("01/-2/2016"));
        assertFalse(Parser.isValidDate("01/02/-016"));
        assertFalse(Parser.isValidDate("2359"));

        System.out.println("...success!");
    }
    
    @Test
    public void testValidDateTime() {
        System.out.println("\n>> Testing isValidDateTime...");

        // Date only cases should follow isValidDate
        assertTrue(Parser.isValidDateTime("01/01/1819"));
        assertFalse(Parser.isValidDateTime("00/01/1819"));
        // Leap Years
        assertTrue(Parser.isValidDateTime("29/02/2012"));
        assertFalse(Parser.isValidDateTime("30/02/2012"));
        assertTrue(Parser.isValidDateTime("28/02/2011"));
        assertFalse(Parser.isValidDateTime("29/02/2011"));
        // Time only cases should follow isValidTime
        assertTrue(Parser.isValidDateTime("2359"));
        assertFalse(Parser.isValidDateTime("2400"));
        // Date-Time order (Valid, invalid date, invalid time)
        assertTrue(Parser.isValidDateTime("01/01/1819 0000"));
        assertFalse(Parser.isValidDateTime("01/01/1819 000"));
        assertFalse(Parser.isValidDateTime("01/00/1819 0000"));
        assertFalse(Parser.isValidDateTime("01/01/1819 2400"));
        assertTrue(Parser.isValidDateTime("today 0000"));
        // Time-Date order
        assertTrue(Parser.isValidDateTime("0000 01/01/1819"));
        assertFalse(Parser.isValidDateTime("000 01/01/1819"));
        assertFalse(Parser.isValidDateTime("0000 01/00/1819"));
        assertFalse(Parser.isValidDateTime("2400 01/01/1819"));
        assertTrue(Parser.isValidDateTime("0000 today"));
        // Extreme cases
        assertFalse(Parser.isValidDateTime("aaa"));
        assertFalse(Parser.isValidDateTime("aaaa 01/01/1819"));
        assertFalse(Parser.isValidDateTime("0000 aa/aa/aaaa"));
        
        System.out.println("...success!");
    }
    
    @Test
    public void testParseToDate() {
        System.out.println("\n>> Testing parseToDate...");

        String expected;
        String result;
        
        // null
        expected = "";
        result = Parser.parseToDateTime(null).toString();
        assertEquals(expected, result);
        // Empty
        expected = "";
        result = Parser.parseToDateTime("").toString();
        assertEquals(expected, result);
        // Valid date
        expected = "01/01/1819";
        result = Parser.parseToDateTime("01/01/1819").toString();
        assertEquals(expected, result);
        // Valid time
        expected = Parser.getCurrDateStr() + " 0000";
        result = Parser.parseToDateTime("0000").toString();
        assertEquals(expected, result);
        // Valid Date-Time
        expected = "01/01/1819 2200";
        result = Parser.parseToDateTime("01/01/1819 2200").toString();
        assertEquals(expected, result);
        // Valid Time-Date
        expected = "01/01/1819 2200";
        result = Parser.parseToDateTime("2200 01/01/1819").toString();
        assertEquals(expected, result);
        
        System.out.println("...success!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failParseInvalid() {
        System.out.println("\n>> Failing parseToDate with invalid input...");

        Parser.parseToDateTime("day");

        System.out.println("...success!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failParseInvalidDate() {
        System.out.println("\n>> Failing parseToDate with invalid date...");

        Parser.parseToDateTime("23/04/1818");

        System.out.println("...success!");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void failParseInvalidTime() {
        System.out.println("\n>> Failing parseToDate with invalid time...");

        Parser.parseToDateTime("2400");

        System.out.println("...success!");
    }
    
    // TODO: Test containsDate/Time and getFirstDate/Time methods
}
