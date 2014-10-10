package Test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import Logic.Processor;
import Logic.Result;
import Storage.Task;

import java.util.ArrayList;

public class ProcessorTest {
	
	public static Processor TestProcessor = Processor.getInstance();
	
	public static void main(String[] args) {
		TestProcessor.getFile().deleteAll();
	}
	
	private void initialiseProcessor() {
	    Processor TestProcessor = Processor.getInstance();
	    TestProcessor.getFile().deleteAll();
	    
	}
	
	public boolean equalsObj(Task task1, Task task2) {
		boolean equal = true;
		try {
			//System.out.println(equal);
			//if (task1.getId() != task2.getId())
				//equal = false;
			if (!task1.getName().equals(task2.getName()))
				equal = false;
			//System.out.println(equal);
			if (!task1.getMore().equals(task2.getMore()))
				equal = false;
			//System.out.println(equal);
			if (task1.getDue() != null && task2.getDue() != null && !task1.getDue().equals(task2.getDue()))
				equal = false;
			//System.out.println(equal);
			if (task1.getStart() != null && task2.getStart() != null && !task1.getStart().equals(task2.getStart()))
				equal = false;
			//System.out.println(equal);
			if (task1.getEnd() != null && task2.getEnd() != null && !task1.getEnd().equals(task2.getEnd()))
				equal = false;
			//System.out.println(equal);
			if (task1.getPriority() != null && task2.getPriority() != null && !task1.getPriority().equals(task2.getPriority()))
				equal = false;
			//System.out.println(equal);
			if (task1.getTags() != null && task2.getTags() != null && !task1.getTags().equals(task2.getTags()))
				equal = false;
			//System.out.println(equal);
		} catch (NullPointerException e) {
		}
		return equal;
	}
	
	@Test
	public void testAdd() throws IOException {
		Task t = new Task("Task1", "Add Bubble", null, null, null, null, new ArrayList<String>());
		Result r = TestProcessor.processInput("add n: Task1 m: Add Bubble");
		assertTrue(equalsObj(t, r.getTasks().get(0)));
	}

	@Test
	public void testEdit() throws IOException {
		// TODO Auto-generated method stub
		Task t = new Task("Task2", "Add Pigs", null, null, null, null, new ArrayList<String>());
		Result r = TestProcessor.processInput("edit 1 n: Task2 m: Add Pigs");
		assertTrue(equalsObj(r.getTasks().get(0), t));
	}

	@Test
	public void testDelete() throws IOException {
		Task t = new Task("Task2", "Add Pigs", null, null, null, null, new ArrayList<String>());
		//Result r1 = TestProcessor.processInput("add n: Task1 m: Add Bubble");
		Result r = TestProcessor.processInput("delete 1");
		assertTrue(equalsObj(r.getTasks().get(0), t));
		System.out.println(TestProcessor.getFile().getToDoTasks().size() + "-Size of todo 0");
		System.out.println(TestProcessor.getFile().getDeletedTasks().size() + "-Size of Deleted 1");
		
		//assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), t));
	}

	@Test
	public void testRestore() throws IOException {
		//Result r1 = TestProcessor.processInput("add n: Task1 m: Add Bubble");
		Result dis = TestProcessor.processInput("display all");
		System.out.println("Display: " + dis.getTasks().get(0).getId());
		TestProcessor.getFile().deleteTask(dis.getTasks().get(0).getId());
		Task t = new Task("Task1", "Add Bubble", null, null, null, null, new ArrayList<String>());
		
		Result r = TestProcessor.processInput("restore " + dis.getTasks().get(0).getId());
		assertTrue(equalsObj(r.getTasks().get(0), t));
		//assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), t));
		//assertTrue(TestProcessor.getFile().getDeletedTasks().size() == 0);
		
//		Result r4 = TestProcessor.processInput("delete " + dis.getTasks().get(0).getId() + 1);
	//	assertTrue(equalsObj(r4.getTasks().get(0), t));
		//assertEquals(TestProcessor.getFile().getToDoTasks().size(), 0);
		//assertTrue(equalsObj(TestProcessor.getFile().getDeletedTasks().get(0), t));
		
		//Result r5 = TestProcessor.processInput("restore all");
		//assertTrue(equalsObj(r5.getTasks().get(0), t));
		//assertTrue(equalsObj(TestProcessor.getFile().getToDoTasks().get(0), t));
		//assertEquals(TestProcessor.getFile().getDeletedTasks().size(), 0);
	}

	@Test
	public void testDisplay() throws IOException {
		testAdd();
		Task t = new Task("Task1", "Add Bubble", null, null, null, null, new ArrayList<String>());
		Result r = TestProcessor.processInput("display all");
		assertTrue(equalsObj(r.getTasks().get(0), t));
		assertEquals(r.getTasks().size(), 1);
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
	public void testDone() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void testTodo() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void testUndo() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void testRedo() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void testClear() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void testJoke() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void testExit() {
		// TODO Auto-generated method stub
		
	}
}
