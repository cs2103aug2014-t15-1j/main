package database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Unit tests for the database component classes.
 * 
 * @author A0116373J
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ TaskReaderTest.class, TaskWriterTest.class,
                     DatabaseLogicTest.class, TaskTest.class,
                     DateTimeTest.class })
public class DatabaseTestSuite {

}