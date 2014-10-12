package Test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import Logic.Processor;
import Logic.Result;
import Storage.Task;

import java.util.ArrayList;

public class ProcessorTest {
	
	public static Processor TestProcessor = Processor.getInstance();
	
	@Before
	public void initialiseProcessor() {
        TestProcessor.getFile().wipeFile();	    
	}
	
	//This method check if between task1 and task2 is identical (in terms of parameters)
	public boolean equalsObj(Task task1, Task task2) {
		boolean equal = true;
		try {
		    if (!task1.getName().equals(task2.getName()))
				equal = false;
			if (task1.getDue() != null && task2.getDue() != null && !task1.getDue().equals(task2.getDue()))
				equal = false;
			if (task1.getStart() != null && task2.getStart() != null && !task1.getStart().equals(task2.getStart()))
				equal = false;
			if (task1.getEnd() != null && task2.getEnd() != null && !task1.getEnd().equals(task2.getEnd()))
				equal = false;
			if (task1.getTags() != null && task2.getTags() != null && !task1.getTags().equals(task2.getTags()))
				equal = false;
		} catch (NullPointerException e) {
		}
		return equal;
	}
	
	@Test
	//Basic Test for 'Add' Command
	public void testAdd() throws Exception {
		Task t = new Task("Task1 Add Bubble", null, null, null, new ArrayList<String>());
		Result r = TestProcessor.processInput("add n: Task1 Add Bubble");
		assertTrue(equalsObj(t, r.getTasks().get(0)));
		
		//Test undo & redo of Add
        Result r1 = TestProcessor.processInput("undo");
		Result r2 = TestProcessor.processInput("redo");
		assertTrue(equalsObj(t, r2.getTasks().get(0)));
	}

	@Test
    //Basic Test for 'Edit' Command
	public void testEdit() throws Exception {
	    Task t = new Task("Task2 Add Pigs", null, null, null, new ArrayList<String>());
	    Result r0 = TestProcessor.processInput("add n: Task1 Add Bubble");
		Result r = TestProcessor.processInput("edit " +r0.getTasks().get(0).getId()+ " n: Task2 Add Pigs");
		assertTrue(equalsObj(r.getTasks().get(0), t));

		//Test undo & redo of Edit
		Result r1 = TestProcessor.processInput("undo");
        Result r2 = TestProcessor.processInput("redo");
        assertTrue(equalsObj(r2.getTasks().get(0), t));
	}

	@Test
    //Basic Test for 'Delete' Command
	public void testDelete() throws Exception {
	    Task t1 = new Task("Task1 Add Bubble", null, null, null, new ArrayList<String>());
        Task t2 = new Task("Task2 Add Pigs", null, null, null, new ArrayList<String>());
	    
	    Result r0 = TestProcessor.processInput("add n: Task1 Add Bubble");
		TestProcessor.processInput("add n: Task2 Add Pigs");
		Result r = TestProcessor.processInput("delete "+r0.getTasks().get(0).getId());
		assertTrue(equalsObj(r.getTasks().get(0), t1));
		assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), t2));
		
		//Test Undo & Redo of Delete <id>
		TestProcessor.processInput("undo");
		assertTrue(TestProcessor.getFile().getDeletedTasks().size() == 0);
        
		TestProcessor.processInput("redo");
        assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), t2));
        
        //Test Undo & Redo of Delete Search
        TestProcessor.processInput("add n: Task2 Add Bubble");
        TestProcessor.processInput("add n: Task2 Add Bubble");
        TestProcessor.processInput("add n: Task3 Add Bubble");
        TestProcessor.processInput("search task2");
        TestProcessor.processInput("delete search");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
        
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 4);
        
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);

	}

	@Test
    //Basic Test for 'Restore' Command
	public void testRestore() throws Exception {
	    Task t = new Task("Task1 Add Bubble", null, null, null, new ArrayList<String>());
        
		Result r0 = TestProcessor.processInput("add n: Task1 Add Bubble");
		TestProcessor.processInput("delete " + r0.getTasks().get(0).getId());
		Result r2 = TestProcessor.processInput("restore "+ r0.getTasks().get(0).getId());
		assertTrue(equalsObj(r2.getTasks().get(0), t));
		assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), t));
		assertTrue(TestProcessor.getFile().getDeletedTasks().size() == 0);
		assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
		 
        //Begins Undo & Redo Test of Restore <id>
        TestProcessor.processInput("undo");
        assertTrue(equalsObj(TestProcessor.getFile().getDeletedTasks().get(0), t));
        assertTrue(TestProcessor.getFile().getDeletedTasks().size() == 1);
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 0);
        
        TestProcessor.processInput("redo");
        assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), t));
        assertTrue(TestProcessor.getFile().getDeletedTasks().size() == 0);
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
	}

	@Test
    //Basic Test for 'Display'/'Show' Command
	public void testDisplay() throws Exception {
		Task t = new Task("Task1 Add Bubble", null, null, null, new ArrayList<String>());
		TestProcessor.processInput("add n: Task1 Add Bubble");
		Result r1 = TestProcessor.processInput("display");
		assertTrue(equalsObj(r1.getTasks().get(0), t));
        assertEquals(r1.getTasks().size(), 1);
        
        Task t2 = new Task("Task2 Add Bubble", null, null, null, new ArrayList<String>());
        Result r3 =TestProcessor.processInput("add n: Task2 Add Bubble");
		Result r4 = TestProcessor.processInput("display "+ r3.getTasks().get(0).getId());
		assertTrue(equalsObj(r4.getTasks().get(0), t2));
        assertEquals(r4.getTasks().size(), 1);
        
        Result r5 = TestProcessor.processInput("show");
        assertEquals(r5.getTasks().size(), 2);
	}
	
	@Test 
    //Basic Test for 'Search' Command
	public void testSearch() throws Exception {
	    TestProcessor.processInput("add n: Task1 Add Bubble");
	    TestProcessor.processInput("add n: Task1 Add Bubble");
	    TestProcessor.processInput("add n: Task2 Add Bubble");
	    TestProcessor.processInput("add n: Task2 Add Bubble");
	    TestProcessor.processInput("add n: Task2 Add Bubble");
	    TestProcessor.processInput("add n: Task3 Add Bubble");
        
	    Result r6 = TestProcessor.processInput("search task2");
	    assertTrue(r6.getTasks().size() == 3);
	    Result r7 = TestProcessor.processInput("search task");
        assertTrue(r7.getTasks().size() == 6);
	}
	
	@Test
	//Boundary Test Case: Consecutive undo/redo for delete search
	public void testDeleteSearchUndoRedo() throws Exception {
	    TestProcessor.processInput("add n: Task1 Add Bubble");
        TestProcessor.processInput("add n: Task1 Add Bubble");
        TestProcessor.processInput("add n: Task2 Add Bubble");
        TestProcessor.processInput("add n: Task2 Add Bubble");
        TestProcessor.processInput("add n: Task2 Add Bubble");
        TestProcessor.processInput("add n: Task3 Add Bubble");
        
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
		// TODO Auto-generated method stub
		
	}

	@Test
	public void testUnblock() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void testDone() throws Exception {
	    Result r1 = TestProcessor.processInput("add n: Task1 Add Bubble");
	    Result r2 = TestProcessor.processInput("add n: Task2 Add Bubble");
	    Result r3 = TestProcessor.processInput("add n: Task3 Add Bubble");
        TestProcessor.processInput("done 1");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 2);
        TestProcessor.processInput("done 3");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
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
	    Result r1 = TestProcessor.processInput("add n: Task1 Add Bubble");
	    Result r2 = TestProcessor.processInput("add n: Task2 Add Bubble");
	    Result r3 = TestProcessor.processInput("add n: Task3 Add Bubble");
        TestProcessor.processInput("done "+r1.getTasks().get(0).getId());
        TestProcessor.processInput("done "+r2.getTasks().get(0).getId());
        TestProcessor.processInput("done "+r3.getTasks().get(0).getId());
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 0);
        TestProcessor.processInput("todo "+r1.getTasks().get(0).getId());
        TestProcessor.processInput("todo "+r2.getTasks().get(0).getId());
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 1);
        TestProcessor.processInput("redo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 2);
        TestProcessor.processInput("undo");
        TestProcessor.processInput("undo");
        assertTrue(TestProcessor.getFile().getToDoTasks().size() == 0);
	}
}
