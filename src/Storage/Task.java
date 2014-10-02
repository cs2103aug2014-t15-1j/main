package Storage;

import java.util.ArrayList;

public class Task {
    private static int newId = 1;   // ID for each new task object
	
	// Task Object Attributes
    private final int ID; // ID only changes once, at each program start
    private String name = "";
    private String more = "";
    private String due = "";   //CHANGE TO DATE type?
    private String start = ""; //CHANGE TO DATE type?
    private String end = "";   //CHANGE TO DATE type?
    private String priority = "";
    private ArrayList<String> tags = new ArrayList<String>();
    private boolean deleted = false;  // useful?
    private boolean done = false;
    
    // Constructor
    public Task(String name, String more, String due, String start, 
            String end, String priority, ArrayList<String> tags) {
        this.ID = newId;
        newId++;
        this.name = name;
        this.more = more;
        this.due = due;
        this.start = start;
        this.end = end;
        this.priority = priority;
        this.tags = tags;
    }
    
    public String stringify() {
        String stringifiedTask = "Name: " + name + " ";
        stringifiedTask += "More: " + more + " ";
        stringifiedTask += "Due: " + due + " ";
        stringifiedTask += "Start: " + start + " ";
        stringifiedTask += "End: " + end + " ";
        stringifiedTask += "Priority: " + priority + " ";
        stringifiedTask += tags + " "; // TODO ArrayList to string
        stringifiedTask += done ? "#Done" : "#ToDo";
        
        return stringifiedTask;
    }
    
    //===== Getters and setters =====//
    
    // Getters
    public int getId() {
        return ID;
    }
    
    public String getName() {
        return name;
    }
    
    public String getMore() {
        return more;
    }
    
    public String getDue() {
        return due;
    }
    
    public String getStart() {
        return start;
    }
    
    public String getEnd() {
        return end;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public ArrayList<String> getTags() {
        return tags;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    public boolean isDone() {
        return done;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setMore(String more) {
        this.more = more;
    }
    
    public void setDue(String due) {
        this.due = due;
    }
    
    public void setStart(String start) {
        this.start = start;
    }
    
    public void setEnd(String end) {
        this.end = end;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
    
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public void setDone(boolean done) {
        this.done = done;
    }
    
    
    
    
    /* Legacy code from YX
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
	}*/
}
