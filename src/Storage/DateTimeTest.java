package Storage;

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
        assertEquals("Empty date String", dT.getDate(), "");
        assertEquals("Empty time String", dT.getTime(), "");
    }

    @Test
    public void testConstructorStoreStringArgs() throws Exception {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dT = new DateTime(date, time);
        date = "";
        time = "";
        assertEquals("Stored date by value", dT.getDate(), "10/10/2014");
        assertEquals("Stored time by value", dT.getTime(), "2359");
    }

    @Test
    public void testConstructorClonesByValue() throws Exception {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dT1 = new DateTime(date, time);
        DateTime dT2 = new DateTime(dT1);
        date = "";
        time = "";
        assertEquals("Cloned date", dT1.getDate(), dT2.getDate());
        assertEquals("Cloned time", dT1.getTime(), dT2.getTime());
        dT1.setDate(date);
        dT1.setTime(time);
        assertEquals("Empty date String", dT1.getDate(), "");
        assertEquals("Empty time String", dT1.getTime(), "");
        assertNotEquals("Cloned date by value", dT1.getDate(), dT2.getDate());
        assertNotEquals("Cloned time by value", dT1.getTime(), dT2.getTime());
    }

    @Test
    public void testResetDate() throws Exception {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dT = new DateTime(date, time);
        dT.resetDate();
        date = "";
        time = "";
        assertEquals("Emoty date String", dT.getDate(), "");
        assertEquals("Stored time by value", dT.getTime(), "2359");
    }

    @Test
    public void testResetTime() throws Exception {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dT = new DateTime(date, time);
        dT.resetTime();
        date = "";
        time = "";
        assertEquals("Stored date by value", dT.getDate(), "10/10/2014");
        assertEquals("Empty time String", dT.getTime(), "");
    }

    @Test
    public void testResetDateTime() throws Exception {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dT = new DateTime(date, time);
        dT.resetDateTime();
        date = "";
        time = "";
        assertEquals("Empty date String", dT.getDate(), "");
        assertEquals("Empty time String", dT.getTime(), "");
    }
}