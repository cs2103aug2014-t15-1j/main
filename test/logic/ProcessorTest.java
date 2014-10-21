package logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import database.DateTime;
import database.Task;

import java.util.ArrayList;
import java.util.List;

public class ProcessorTest {
	
	public static Processor TestProcessor = Processor.getInstance();
	private static  DateTime testTime = new DateTime("10/10/2012" ,"1010");
	private static  List<String> testTags = new ArrayList<String>();
	
	private static Task testTask1 = new Task("Do CS2103 Homework", null, null, null, testTags);
	private static  Task testTask2 = new Task(null, testTime, null, null, new ArrayList<String>());
	private static  Task testTask3 = new Task(null, null, testTime, null, new ArrayList<String>());
	private static  Task testTask4 = new Task(null, null, null, testTime, new ArrayList<String>());
	private static  Task testTask5 = new Task(null, null, null, null, testTags);
	private static  Task testTask6 = new Task("Do CS2103 Homework", testTime, testTime, testTime, testTags);
	private static  Task testTask7 = new Task("Do EE2020 Homework", null, null, null, new ArrayList<String>());
	
	@Before
	public void initialiseProcessor() {
	    testTags.clear();
        TestProcessor = Processor.reset();
	}
	
	//This method check if between task1 and task2 is identical (in terms of parameters)
	public boolean equalsObj(Task task1, Task task2) {
		boolean equal = true;
		try {
		    if (!task1.getName().equals(task2.getName()))
				equal = false;
			if (task1.getDue() != null && task2.getDue() != null && !task1.getDue().toString().equals(task2.getDue().toString()))
				equal = false;
			if (task1.getStart() != null && task2.getStart() != null && !task1.getStart().toString().equals(task2.getStart().toString()))
				equal = false;
			if (task1.getEnd() != null && task2.getEnd() != null && !task1.getEnd().toString().equals(task2.getEnd().toString()))
				equal = false;
			if (task1.getTags() != null && task2.getTags() != null && !task1.getTags().toString().equals(task2.getTags().toString()))
				equal = false;
		} catch (NullPointerException e) {
		}
		return equal;
	}
	
	@Test
	//Basic Test for 'Add' Command
	public void testAdd() throws Exception {
	    //Add with name
		Result r1 = TestProcessor.processInput("add n: Do CS2103 Homework");
		assertTrue(equalsObj(testTask1, r1.getTasks().get(0)));
        //Add with due
        Result r2 = TestProcessor.processInput("add d: 10/10/2012 1010");
        assertTrue(equalsObj(testTask2, r2.getTasks().get(0)));
        //Add with start
        Result r3 = TestProcessor.processInput("add s: 10/10/2012 1010");
        assertTrue(equalsObj(testTask3, r3.getTasks().get(0)));
        //Add with end
        Result r4 = TestProcessor.processInput("add e: 10/10/2012 1010");
        assertTrue(equalsObj(testTask4, r4.getTasks().get(0)));
        
        //Add with tags
        Result r5 = TestProcessor.processInput("add #homework");
        testTags.add("#homework");
        assertTrue(equalsObj(testTask5, r5.getTasks().get(0)));
        //All valid
        Result r6 = TestProcessor.processInput("add n: Do CS2103 Homework d: 10/10/2012 1010 s: 10/10/2012 1010 e: 10/10/2012 1010 #homework");
        assertTrue(equalsObj(testTask6, r6.getTasks().get(0)));
        
        //Test undo & redo of Add
        TestProcessor.processInput("undo");
        Result r8 = TestProcessor.processInput("redo");
        assertTrue(equalsObj(testTask1, r8.getTasks().get(0)));
	}

	@Test
    //Basic Test for 'Edit' Command
	public void testEdit() throws Exception {
	    Result r0 = TestProcessor.processInput("add n: Do CS2103 Homework");
		Result r1 = TestProcessor.processInput("edit 1 n: Do EE2020 Homework");
		assertTrue(equalsObj(r1.getTasks().get(0), testTask7));

		//Test undo & redo of Edit
		Result r2 =TestProcessor.processInput("undo");
		assertTrue(equalsObj(r2.getTasks().get(0), testTask1));
        Result r3 = TestProcessor.processInput("redo");
        assertTrue(equalsObj(r3.getTasks().get(0), testTask7));
	}

	@Test
    //Basic Test for 'Delete' Command
	public void testDelete() throws Exception {	    
	    //Test Delete <id>
	    Result r0 = TestProcessor.processInput("add n: Do CS2103 Homework");
		Result r1 = TestProcessor.processInput("add n: Do EE2020 Homework");
		Result r2 = TestProcessor.processInput("delete 1");
		assertTrue(equalsObj(r2.getTasks().get(0), testTask1));
		assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), testTask7));
		
		//Test delete <id> where id is negative
		Result r3 = TestProcessor.processInput("delete -1");
		assertTrue(!r3.isSuccess());
		//Test delete <id> where id does not exist
		Result r4 = TestProcessor.processInput("delete 3");
		assertTrue(!r4.isSuccess());
		
		//Test Undo & Redo of Delete <id>
		TestProcessor.processInput("undo");
		assertTrue(TestProcessor.getFile().getDeletedTasks().size() == 0);
        
		TestProcessor.processInput("redo");
        assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), testTask7));
        
        //Test delete search
        TestProcessor.processInput("add n: Task2");
        TestProcessor.processInput("add n: Task2");
        TestProcessor.processInput("add n: Task3");
        TestProcessor.processInput("search task2");
        TestProcessor.processInput("delete search");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 2);
        
        //Undo & Redo of Delete Search
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 4);
        
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 2);

	}

	@Test
    //Basic Test for 'Restore' Command
	public void testRestore() throws Exception {
	    //Test Restore <id>
		Result r0 = TestProcessor.processInput("add n: Do CS2103 Homework");
		TestProcessor.processInput("delete 1");
		
		Result r1 = TestProcessor.processInput("restore 1");
		assertTrue(equalsObj(r1.getTasks().get(0), testTask1));
		assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), testTask1));
		assertTrue(TestProcessor.getFile().getDeletedTasks().size() == 0);
		assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
		 
        //Begins Undo & Redo Test of Restore <id>
        TestProcessor.processInput("undo");
        assertTrue(equalsObj(TestProcessor.getFile().getDeletedTasks().get(0), testTask1));
        assertTrue(TestProcessor.getFile().getDeletedTasks().size() == 1);
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 0);
        
        TestProcessor.processInput("redo");
        assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), testTask1));
        assertTrue(TestProcessor.getFile().getDeletedTasks().size() == 0);
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
	}

	@Test
    //Basic Test for 'Display'/'Show' Command
	public void testDisplay() throws Exception {
	    //Test display
		TestProcessor.processInput("add n: Do CS2103 Homework");
		Result r1 = TestProcessor.processInput("display");
		assertTrue(equalsObj(r1.getTasks().get(0), testTask1));
        assertEquals(r1.getTasks().size(), 1);
        
        //Test display <id>
        Result r3 = TestProcessor.processInput("add n: Do EE2020 Homework");
		Result r4 = TestProcessor.processInput("display 2");
		assertTrue(equalsObj(r4.getTasks().get(0), testTask7));
        assertEquals(r4.getTasks().size(), 1);
        
        //Test show
        Result r5 = TestProcessor.processInput("show");
        assertEquals(r5.getTasks().size(), 2);
	}
	
	@Test 
    //Basic Test for 'Search' Command
	public void testSearch() throws Exception {
	    TestProcessor.processInput("add n: Do CS2103 Homework #cshomework");
	    TestProcessor.processInput("add n: Do CS2103 Homework #cshomework");
	    TestProcessor.processInput("add n: Do CS2103 Homework #cshomework");
	    TestProcessor.processInput("add n: Do CS2103 Homework");
	    TestProcessor.processInput("add n: Do CS2103 Homework");
	    TestProcessor.processInput("add n: Do CS2103 Homework");
        
	    //Test search by tags
	    Result r6 = TestProcessor.processInput("search #cshomework");
	    assertTrue(r6.getTasks().size() == 3);
	    
	    //Test search by keyword
	    Result r7 = TestProcessor.processInput("search cs2103");
        assertTrue(r7.getTasks().size() == 6);
        
        //Test search by keyword OR tags
        Result r8 = TestProcessor.processInput("search cs2103 #cshomework");
        assertTrue(r8.getTasks().size() == 6);
	}
	
	@Test
	//Boundary Test Case: Consecutive undo/redo for delete search
	public void testDeleteSearchUndoRedo() throws Exception {
	    TestProcessor.processInput("add n: Task1");
        TestProcessor.processInput("add n: Task1");
        TestProcessor.processInput("add n: Task2");
        TestProcessor.processInput("add n: Task2");
        TestProcessor.processInput("add n: Task2");
        TestProcessor.processInput("add n: Task3");
        
        Result r6 = TestProcessor.processInput("search task2");
        assertTrue(r6.getTasks().size() == 3);
        TestProcessor.processInput("delete search");

        TestProcessor.processInput("search task1");
        TestProcessor.processInput("delete search");
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 3);
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 6);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 3);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
	}
	
	@Test
	public void testBlock() {
	    TestProcessor.processInput("block 23/04/2012 to 23/05/2014");
		assertTrue(TestProcessor.getBlockedDates().size() == 1);
	}

	@Test
	public void testUnblock() {
        assertTrue(TestProcessor.getBlockedDates().size() == 0);
	    TestProcessor.processInput("block 23/04/2012 to 23/05/2014");
        assertTrue(TestProcessor.getBlockedDates().size() == 1);
        //Test unblock invalid id
        Result r1 = TestProcessor.processInput("unblock 0");
        assertTrue(TestProcessor.getBlockedDates().size() == 1);
        Result r2 = TestProcessor.processInput("unblock -1");
        assertTrue(TestProcessor.getBlockedDates().size() == 1);
        Result r3 = TestProcessor.processInput("unblock 5");
        assertTrue(TestProcessor.getBlockedDates().size() == 1);
        //Test unblock valid id
	    Result r4 = TestProcessor.processInput("unblock 1");
	    assertTrue(TestProcessor.getBlockedDates().size() == 0);
	}

	@Test
	public void testDone() throws Exception {
	    TestProcessor.processInput("add n: Task1");
	    TestProcessor.processInput("add n: Task2");
	    TestProcessor.processInput("add n: Task3");
	    
	    //Test done <id>
        TestProcessor.processInput("done 1");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 2);
        TestProcessor.processInput("done 3");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
        
        //Test undo and redo of done
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 2);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
        TestProcessor.processInput("undo");
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 3);
	}

	@Test
	public void testTodo() throws Exception {
	    Result r1 = TestProcessor.processInput("add n: Task1");
	    Result r2 = TestProcessor.processInput("add n: Task2");
	    Result r3 = TestProcessor.processInput("add n: Task3");
	    
	    //Test todo <id>
        TestProcessor.processInput("done 1");
        TestProcessor.processInput("done 2");
        TestProcessor.processInput("done 3");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 0);
        
        //Test todo <id>
        TestProcessor.processInput("todo 1");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
        TestProcessor.processInput("todo 2");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 2);
        
        //Test undo and redo of Todo
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 2);
        TestProcessor.processInput("undo");
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 0);
	}
}
