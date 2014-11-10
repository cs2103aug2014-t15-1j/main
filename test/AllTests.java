import gui.ResultGeneratorTest;
import logic.LogicUnitTest;
import logic.ProcessorTest;
import objects.DateTimeTest;
import objects.TaskTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import parser.TestDateParser;
import parser.TestInputParser;
import database.DatabaseTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ DatabaseTestSuite.class, DateTimeTest.class,
               TaskTest.class, ResultGeneratorTest.class, LogicUnitTest.class,
               ProcessorTest.class, TestDateParser.class, TestInputParser.class })
public class AllTests {

}
