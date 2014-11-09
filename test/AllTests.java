import gui.ResultGeneratorTest;
import logic.LogicUnitTest;
import logic.ProcessorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import parser.TestDateParser;
import parser.TestParser;
import database.DatabaseTestSuite;
import database.DateTimeTest;
import database.TaskTest;

@RunWith(Suite.class)
@SuiteClasses({ DatabaseTestSuite.class, DateTimeTest.class,
               TaskTest.class, ResultGeneratorTest.class, LogicUnitTest.class,
               ProcessorTest.class, TestDateParser.class, TestParser.class })
public class AllTests {

}
