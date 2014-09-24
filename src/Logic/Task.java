package Logic;

import java.util.ArrayList;
import java.util.Map;

import Parser.Command;
import Parser.TaskParam;

public class Task {
	private static int numTasks = 0;
	private String cmdType;
	private Map<String, String> details;
	private static ArrayList<Task> tasks;
    
	//TASK OBJECT ATTRIBUTES
	private int uid;
	private String type;
    private static int uid_counter = 1;
    
    // Consideration: "edit last"
    // Also can just be a string, let the logic handle?
    private static Command last;
    
    // 'Add' and 'Edit' fields
    private String name;
    private String more;
    private String due;   //CHANGE TO DATE?
    private String start; //CHANGE TO DATE?
    private String end;   //CHANGE TO DATE?
    private String priority;
    private ArrayList<String> tags;
    private String delete;
    
	public Task(Command cmd) {
		 this.cmdType = cmd.getType();
		 this.details = ;
		 this.numTasks++;
	}
	
	public Command getCommand() {
		return this.cmd;
	}
	
	public boolean delete() {
		numTasks--;
		//Do some deleting
		return true;
	}
	
	public Map<String, String> getDetails() {
		return this.details;
	}
	
	@Override
	public String toString() {
		String output = "";
		for (Map.Entry<String, String> entry : details.entrySet()) {
		    output += entry.getKey() + ": " + entry.getValue() + "\n";
		}
		
		if (output.length() > 1) {
			output = output.substring(0, output.length() - 1);
		}
		
		return output;
	}
}
