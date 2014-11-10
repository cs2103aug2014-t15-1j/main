//@author A0116373J

package database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Unit tests for the database component classes.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ TaskReaderTest.class, TaskWriterTest.class,
                     DatabaseLogicTest.class })
public class DatabaseTestSuite {
}