//@author A0116208N
import gui.ResultGeneratorTest;
import logic.LogicUnitTest;
import logic.ProcessorTest;
import objects.ObjectsTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import parser.TestDateParser;
import parser.TestFileParser;
import parser.TestInputParser;
import database.DatabaseTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ DatabaseTestSuite.class, ObjectsTestSuite.class,
               ResultGeneratorTest.class, LogicUnitTest.class,
               ProcessorTest.class, TestDateParser.class,
               TestInputParser.class, TestFileParser.class })
public class AllTests {

}
