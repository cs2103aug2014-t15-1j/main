package Storage;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateTimeTest {

    @Test
    public void testDefaultConstructorHasEmptyAttributes() throws Exception {
        DateTime dateTime = new DateTime();
        assertEquals("Empty date String attribute", dateTime.getDate(), "");
        assertEquals("Empty time String attribute", dateTime.getTime(), "");
    }

    @Test
    public void testConstructorStoreStringArgs() throws Exception {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dateTime = new DateTime(date, time);
        date = "";
        time = "";
        assertEquals("Stored date attribute in DateTime object", dateTime.getDate(), "10/10/2014");
        assertEquals("Stored time attribute in DateTime object", dateTime.getTime(), "2359");
    }

    @Test
    public void testConstructorClonesByValue() throws Exception {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dateTime1 = new DateTime(date, time);
        DateTime dateTime2 = new DateTime(dateTime1);
        date = "";
        time = "";
        assertEquals("Cloned date attribute", dateTime1.getDate(), dateTime2.getDate());
        assertEquals("Cloned time attribute", dateTime1.getTime(), dateTime2.getTime());
        dateTime1.setDate(date);
        dateTime1.setTime(time);
        assertEquals("Empty date String attribute", dateTime1.getDate(), "");
        assertEquals("Empty time String attribute", dateTime1.getTime(), "");
        assertNotEquals("Cloned dateTime2 date attribute by value", dateTime1.getDate(), dateTime2.getDate());
        assertNotEquals("Cloned dateTime2 time attribute by value", dateTime1.getTime(), dateTime2.getTime());
    }

    @Test
    public void testResetDate() throws Exception {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dateTime = new DateTime(date, time);
        dateTime.resetDate();
        assertEquals("Empty date String attribute", dateTime.getDate(), "");
        assertEquals("Stored time attribute in DateTime object", dateTime.getTime(), "2359");
    }

    @Test
    public void testResetTime() throws Exception {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dateTime = new DateTime(date, time);
        dateTime.resetTime();
        assertEquals("Stored date attribute in DateTime object", dateTime.getDate(), "10/10/2014");
        assertEquals("Empty time String attribute", dateTime.getTime(), "");
    }

    @Test
    public void testResetDateTime() throws Exception {
        String date = "10/10/2014";
        String time = "2359";
        DateTime dateTime = new DateTime(date, time);
        dateTime.resetDateTime();
        assertEquals("Empty date String attribute", dateTime.getDate(), "");
        assertEquals("Empty time String attribute", dateTime.getTime(), "");
    }

}
