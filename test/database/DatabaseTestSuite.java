package database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TaskReaderTest.class, TaskWriterTest.class,
                     DatabaseLogicTest.class, TaskTest.class,
                     DateTimeTest.class })
public class DatabaseTestSuite {

}